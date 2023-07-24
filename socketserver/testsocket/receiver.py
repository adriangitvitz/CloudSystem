import websocket


def on_message(ws, message):
    print(f"Message: {message}")


def on_error(ws, error):
    print(error)


def on_close(ws, close_status_code, close_msg):
    print(f"Connection closed {close_status_code} - {close_msg}")


def on_open(ws):
    print("Connection opened")


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp(
        "ws://localhost:8080/ws?chan=jobs",
        on_message=on_message,
        on_error=on_error,
        on_close=on_close,
    )
    ws.on_open = on_open
    ws.run_forever()
