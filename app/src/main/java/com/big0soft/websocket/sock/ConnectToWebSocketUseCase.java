package com.big0soft.websocket.sock;

import java.util.List;

import io.reactivex.Completable;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ConnectToWebSocketUseCase {
    private final WebSocketRepository repository;

    public ConnectToWebSocketUseCase(WebSocketRepository repository) {
        this.repository = repository;
    }

    public Completable execute(List<StompHeader> headers) {
        return repository.connect(headers);
    }
    public Completable execute() {
        return repository.connect();
    }
}
