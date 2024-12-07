package com.big0soft.websocket.sock;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebSocketRepository {
    private final WebSocketDataSource dataSource;

    public WebSocketRepository(WebSocketDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Completable connect(List<StompHeader> headers) {
        return dataSource.connect(headers);
    }

    public Completable connect() {
        return dataSource.connect();
    }

    public Flowable<String> receiveMessages(String topic) {
        return dataSource.receiveMessages(topic);
    }

    public Completable sendMessage(String destination, String message) {
        return dataSource.sendMessage(destination, message);
    }

    public Completable disconnect() {
        return dataSource.disconnect();
    }
}
