package com.big0soft.websocket.socket;

import android.util.Log;

import com.big0soft.websocket.TAGs;

import okhttp3.Response;
import okhttp3.WebSocketListener;

public class WebSocket extends WebSocketListener {

    @Override
    public void onOpen(okhttp3.WebSocket webSocket, Response response) {
        String message = "hello i want connect";
        webSocket.send(message);
        Log.i(TAGs.TAG(getClass()), "onOpen: "+message);
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, String text) {
        outPut(text);
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAGs.TAG(getClass()), "onFailure: ",t );
    }

    public static final int NORMAL_CLOSER_STATE = 1000;
    @Override
    public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSER_STATE,null);
        outPut("The connection is closed the reason: "+reason + " code: "+code);
    }

    private void outPut(String text) {
        Log.i(TAGs.TAG(getClass()), "outPut: "+text);
    }
}
