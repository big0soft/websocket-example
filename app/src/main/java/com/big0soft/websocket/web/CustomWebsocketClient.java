package com.big0soft.websocket.web;

import android.util.Log;

import com.big0soft.websocket.TAGs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class CustomWebsocketClient extends WebSocketListener {

    private OkHttpClient client;
    private WebSocket webSocket;

    public void connectWebSocket() {
        // Replace "YOUR_WEBSOCKET_SERVER_URL" with the actual URL of your Spring Boot WebSocket server
//        String url = "ws://192.168.1.2:9191/websocket";
//        String url = "ws://192.168.1.2:9191/websocket";
//
//        // Create an instance of OkHttpClient
//        okHttpClient = new OkHttpClient();
//
//        // Build the WebSocket request
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        // Establish the WebSocket connection
//        webSocket = okHttpClient.newWebSocket(request, this);

        client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
//ws://192.168.1.2:9191/gs-guide-websocket
//        http://1564-176-29-232-102.ngrok-free.app/test
        String webSocketUrl = "ws://1564-176-29-232-102.ngrok-free.app/gs-guide-websocket";

        Request request = new Request.Builder()
//                .url("ws://192.168.77.47:9191/gs-guide-websocket")
                .url(webSocketUrl)
                .build();
        webSocket = client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.i(TAGs.TAG(getClass()), "onOpen: ");
        String message = "{\"destination\": \"/app/greetings\", \"message\": \"plain\" }";
        Log.i(TAGs.TAG(getClass()), "onOpen: " + message);
        boolean isSend = webSocket.send(message);
        Log.i(TAGs.TAG(getClass()), "onOpen: " + isSend);
//          webSocket.send("Hello...");
//        webSocket.send("...World!");
//        webSocket.send(ByteString.decodeHex("deadbeef"));
//        webSocket.close(1000, "Goodbye, World!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.i(TAGs.TAG(getClass()), "onMessage: " + text);

    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }


//
//    @Override
//    public void onMessage(WebSocket webSocket, String text) {
//        Log.i(TAGs.TAG(getClass()), "onMessage: "+text);
//        webSocket.send(text);
//        // Called when a message is received from the server
//        // Handle the received message here
//    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        // Called when the WebSocket connection is closed
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAGs.TAG(getClass()), "onFailure: ", t);
        // Called when an error occurs
    }

    // Method to send a message through the WebSocket connection
    public void sendMessage(String destination, String message) {
        String jsonMessage = "{\"destination\":\"" + destination + "\", \"message\":\"" + message + "\"}";
        boolean send = webSocket.send(jsonMessage);
        Log.i(TAGs.TAG(getClass()), "sendMessage: " + send);
    }

    // Method to send a message through the WebSocket connection
    public void sendMessage2(String destination, String message) {
        String jsonMessage = "{" +
                "\"destination\": \"" + destination + "\"," +
                "\"content-type\": \"text/plain\"," +
                "\"subscription\": \"sub-0\"," +
                "\"message\": \"" + message + "\"" +
                "}";
        Log.i(TAGs.TAG(getClass()), "sendMessage2: " + jsonMessage);
        webSocket.send(jsonMessage);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

}