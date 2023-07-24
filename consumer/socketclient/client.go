package socketclient

import (
	"log"
	"net/url"
	"os"

	"github.com/gorilla/websocket"
)

func Sendmessage(status string) {
	dialer := websocket.DefaultDialer
	uri := url.URL{Scheme: "ws", Host: os.Getenv("SOCKET_SERVER"), Path: "/ws", RawQuery: "chan=jobs"}
	c, _, err := dialer.Dial(uri.String(), nil)
	if err != nil {
		log.Fatal(err)
	}
	defer c.Close()
	err = c.WriteMessage(websocket.TextMessage, []byte(status))
	if err != nil {
		log.Fatal(err)
	}
}
