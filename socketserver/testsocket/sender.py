import websocket

# Create a connection to the server
ws = websocket.WebSocket()

# Connect to the WebSocket server
ws.connect("ws://localhost:8080/ws?chan=test")

# Send a message
ws.send("Hello, WebSocket!")

# Receive a message
# print(ws.recv())

# Close the connection
ws.close()
