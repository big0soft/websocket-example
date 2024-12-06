package com.big0soft.websocket;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity {

    private StompClient stompClient;
    private TextView messageView;
    private EditText inputMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = findViewById(R.id.messageView);
        inputMessage = findViewById(R.id.inputMessage);
        Button sendMessage = findViewById(R.id.sendMessage);

        setupWebSocket();

        sendMessage.setOnClickListener(v -> {
            String text = inputMessage.getText().toString();
            if (!text.isEmpty()) {
                Message message = new Message(text);
                Gson gson = new Gson();
                String json = gson.toJson(message);

                // Send to "/app/application"
                stompClient.send("/app/application", json).subscribe();
            }
        });

        // Subscribe to "/all/messages"
        Disposable disposable = stompClient.topic("/all/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    messageView.append("\n" + topicMessage.getPayload());
                    Log.i(TAG, "Received message: " + topicMessage.getPayload());
                }, throwable -> {
                    Log.e(TAG, "Error on subscription", throwable);
                });

    }

    private static final String TAG = "MainActivity";


    private void setupWebSocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.1.2:9901/ws/websocket");
        stompClient.connect();
//        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        // Add the disposable to cleanup when the activity is destroyed
        Disposable subscribe = stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i(TAG, "WebSocket connection opened");
                    break;
                case ERROR:
                    Log.e(TAG, "Error: " + lifecycleEvent.getException().getMessage());
                    break;
                case CLOSED:
                    Log.i(TAG, "WebSocket connection closed");
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }
}
