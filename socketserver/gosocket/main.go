package main

import (
	"net/http"
	"socketserver/manager"
) 


func main() {
    http.HandleFunc("/ws", manager.Handler)
    http.ListenAndServe(":8080", nil)
}
