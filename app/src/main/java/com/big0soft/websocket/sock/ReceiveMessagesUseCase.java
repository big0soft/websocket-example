package com.big0soft.websocket.sock;

import io.reactivex.Flowable;

public class ReceiveMessagesUseCase {
    private final WebSocketRepository repository;

    public ReceiveMessagesUseCase(WebSocketRepository repository) {
        this.repository = repository;
    }

    public Flowable<String> execute(String topic) {
        return repository.receiveMessages(topic);
    }
}
