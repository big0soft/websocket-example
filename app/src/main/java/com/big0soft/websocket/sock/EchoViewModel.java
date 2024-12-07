package com.big0soft.websocket.sock;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.dto.StompHeader;

public class EchoViewModel extends ViewModel {
    private final ConnectToWebSocketUseCase connectToWebSocketUseCase;
    private final SendMessageUseCase sendMessageUseCase;
    private final ReceiveMessagesUseCase receiveMessagesUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> messages = new MutableLiveData<>();

    public EchoViewModel(ConnectToWebSocketUseCase connectToWebSocketUseCase,
                         SendMessageUseCase sendMessageUseCase,
                         ReceiveMessagesUseCase receiveMessagesUseCase) {
        this.connectToWebSocketUseCase = connectToWebSocketUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.receiveMessagesUseCase = receiveMessagesUseCase;
    }

    public void connect(List<StompHeader> headers) {
        disposables.add(connectToWebSocketUseCase.execute(headers).subscribe());
    }

    public void connect() {

        disposables.add(connectToWebSocketUseCase.execute()
                .subscribe(() -> {
                    Log.i(TAG, "connected: ");
                },throwable -> {
                    Log.e(TAG, "sendMessage: ", throwable);
                }));
    }

    private static final String TAG = "EchoViewModel";
    public void sendMessage(String destination, String message) {
        disposables.add(sendMessageUseCase.execute(destination, message)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {

                },throwable -> {
                    Log.e(TAG, "sendMessage: ", throwable);
                }));
    }

    public LiveData<String> getMessages() {
        return messages;
    }

    public void startReceivingMessages(String topic) {
        disposables.add(receiveMessagesUseCase.execute(topic)
                .subscribe(messages::postValue,throwable -> {
                    Log.e(TAG, "startReceivingMessages: ", throwable);
                }));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}