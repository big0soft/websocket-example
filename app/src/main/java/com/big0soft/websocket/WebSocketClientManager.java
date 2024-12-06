package com.big0soft.websocket;

// WebSocketClient.java

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSocketClientManager extends WebSocketClient {

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    private MessageListener messageListener;

    public WebSocketClientManager(URI serverUri) {
        super(serverUri);
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket Opened");
    }

    @Override
    public void onMessage(String message) {
        if (messageListener != null) {
            messageListener.onMessageReceived(message);
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("WebSocket Error: " + ex.getMessage());
    }
}
