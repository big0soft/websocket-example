package com.big0soft.websocket.sock;

import static com.big0soft.websocket.echo.RestClient.ANDROID_EMULATOR_LOCALHOST;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.big0soft.websocket.AppClass;
import com.big0soft.websocket.NotificationReceiver;
import com.big0soft.websocket.R;
import com.big0soft.websocket.databinding.ActivityEcho2Binding;
import com.big0soft.websocket.echo.RestClient;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class EchoActivity2 extends AppCompatActivity {
    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";
    private static final String TAG = "EchoActivity2";
    private EchoViewModel echoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEcho2Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_echo2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intViewModel();

        binding.button.setOnClickListener(v -> {
            List<StompHeader> headers = new ArrayList<>();
            headers.add(new StompHeader(LOGIN, "guest"));
            headers.add(new StompHeader(PASSCODE, "guest"));
            echoViewModel.connect(headers);
            echoViewModel.startReceivingMessages("/topic/greetings");
        });

        binding.button2.setOnClickListener(v -> {
            echoViewModel.sendMessage("/topic/hello-msg-mapping", "Echo STOMP ");
        });

        echoViewModel.getMessages().observe(this, s -> {
            Log.i(TAG, "websocket receive message: " + s);
            pushNotification();
        });

        requestNotificationPermission();
    }

    public void intViewModel() {
        // إعداد التبعيات يدويًا
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ANDROID_EMULATOR_LOCALHOST
                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");
        WebSocketDataSource dataSource = new WebSocketDataSource(stompClient);
        WebSocketRepository repository = new WebSocketRepository(dataSource);

        echoViewModel = new EchoViewModel(
                new ConnectToWebSocketUseCase(repository),
                new SendMessageUseCase(repository),
                new ReceiveMessagesUseCase(repository)
        );

        List<StompHeader> headers = new ArrayList<>();

    }

    public void pushNotification() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Notification Title");
        intent.putExtra("message", "Notification Message");
        sendBroadcast(intent);
    }

    private void requestNotificationPermission() {
        // Check if notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        AppClass.REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }
}