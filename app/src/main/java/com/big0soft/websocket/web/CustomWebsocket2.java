package com.big0soft.websocket.web;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.big0soft.websocket.TAGs;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class CustomWebsocket2 extends WebSocketListener {
    public static final String TAG = "CustomWebSocketListener";
    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        Log.i(TAG, "WebSocket connection opened.");
        webSocket.send("hello");
        webSocket.send("world");
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        Log.i(TAG, "onMessage: " + new String(bytes.toByteArray()));
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.i(TAG, "onMessage: " + text);
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        Log.i(TAG, "WebSocket closing: " + reason);
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
        Log.e(TAG, "WebSocket failure: " + t.getMessage(), t);
    }
}