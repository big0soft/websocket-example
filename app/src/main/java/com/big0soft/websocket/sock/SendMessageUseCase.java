package com.big0soft.websocket.sock;

import io.reactivex.Completable;

public class SendMessageUseCase {
    private final WebSocketRepository repository;

    public SendMessageUseCase(WebSocketRepository repository) {
        this.repository = repository;
    }

    public Completable execute(String destination, String message) {
        return repository.sendMessage(destination, message);
    }
}
