package com.big0soft.websocket.socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

public class WebSocketConnection {
    private static WebSocketConnection instance;
    private final WebSocketListener webSocketListener;


    private WebSocketConnection(WebSocketListener webSocketListener) {
        this.webSocketListener = webSocketListener;
        String url = "wss://free.blr2.piesocket.com/v3/1?api_key=uvRRAoeRT59wgb2JC8FB0sePBlsnFx5AWckU0SgZ&notify_self=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newWebSocket(request,webSocketListener);
    }
//    https://piehost.com/

    public static WebSocketConnection getInstance() {
        return instance;
    }
}
