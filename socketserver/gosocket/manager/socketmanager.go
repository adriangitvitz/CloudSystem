package manager

import (
	"log"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

type Connection struct {
	WS   *websocket.Conn
	Send chan []byte
}

type Channel struct {
	lock        sync.RWMutex
	Register    chan *Connection
	Unregister  chan *Connection
	Connections map[*Connection]bool
	Broadcast   chan []byte
}

var upgrader = websocket.Upgrader{
    CheckOrigin: func(r *http.Request) bool {
        return true // Allow any origin
    },
}
var channels = make(map[string]*Channel)

var (
	// How long to await a pong response from client
	pongWait = 40 * time.Second
	// Interval has to be less than the await time - We multiply by 0.9 to get 90% of time
	// The reason why it has to be less than PingRequency is becuase otherwise it will send a new Ping before getting response
	pingInterval = (pongWait * 9) / 10
)

func newChannel() *Channel {
	return &Channel{
		Register:    make(chan *Connection),
		Unregister:  make(chan *Connection),
		Connections: make(map[*Connection]bool),
		Broadcast:   make(chan []byte),
	}
}

func Handler(w http.ResponseWriter, r *http.Request) {
	log.Println("Starting to listen")
	// Upgrade HTTP Connection
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Printf("Error in listener %s \n", err)
		return
	}
	connection := &Connection{WS: ws, Send: make(chan []byte)}
	channelname := r.URL.Query().Get("chan")
	log.Println(channelname)
	// Lock new connections until channel selection
	channel, found := channels[channelname]

	if !found {
		channel = newChannel()
		channels[channelname] = channel
		go handleChannel(channel)
	}
	channel.Register <- connection
	go handleConnection(connection, channel)
}

func handleChannel(c *Channel) {
	for {
		select {
		case connection := <-c.Register:
			c.lock.Lock()
			c.Connections[connection] = true
			c.lock.Unlock()
		case connection := <-c.Unregister:
			c.lock.Lock()
			if _, ok := c.Connections[connection]; ok {
				delete(c.Connections, connection)
				close(connection.Send)
			}
			c.lock.Unlock()
		case message := <-c.Broadcast:
			c.lock.RLock()
			for connection := range c.Connections {
				select {
				case connection.Send <- message:
				default:
					close(connection.Send)
					delete(c.Connections, connection)
				}
			}
			c.lock.RUnlock()
		}
	}
}

func handleConnection(connection *Connection, c *Channel) {
	defer func() {
		c.Unregister <- connection
		connection.WS.Close()
	}()
	// Message Max Size in Bytes
	// connection.WS.SetReadLimit(1024)
	// Configuring wait time for Pong response
	// Has to be done to set the first initial timer
	// if err := connection.WS.SetReadDeadline(time.Now().Add(pongWait)); err != nil {
	// 	log.Fatal(err)
	// 	return
	// }
	// Configure how to handle Pong responses
	// connection.WS.SetPongHandler(connection.pongHandler)
	go func() {
		for msg := range connection.Send {
			if err := connection.WS.WriteMessage(websocket.TextMessage, msg); err != nil {
				return
			}
		}
	}()
	for {
		_, message, err := connection.WS.ReadMessage()
		if err != nil {
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
				log.Printf("Error in closed message received %s \n", err)
				c.Unregister <- connection
				connection.WS.Close()
			}
			break
		}
		c.Broadcast <- message
	}
}

func (c *Connection) pongHandler(pongMsg string) error {
	return c.WS.SetReadDeadline(time.Now().Add(pongWait))
}
