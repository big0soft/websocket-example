package com.big0soft.websocket.sock;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebSocketDataSource {
    private static final String TAG = "WebSocketDataSource";
    private final StompClient stompClient;

    public WebSocketDataSource(StompClient stompClient) {
        this.stompClient = stompClient;
    }

    public Completable connect(List<StompHeader> headers) {
        return Completable.fromAction(() -> stompClient.connect(headers));
    }

    public Completable connect() {
        return Completable.fromAction(stompClient::connect)
                .doOnError(throwable -> {
                    Log.e(TAG, "connect: ", throwable);
                });
    }


    public Flowable<String> receiveMessages(String topic) {
        return stompClient.topic(topic).map(StompMessage::getPayload);
    }

    public Completable sendMessage(String destination, String message) {
        return stompClient.send(destination, message);
    }

    public Completable disconnect() {
        return stompClient.disconnectCompletable();
    }
}
