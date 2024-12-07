package com.big0soft.websocket.echo;

import static com.big0soft.websocket.echo.RestClient.ANDROID_EMULATOR_LOCALHOST;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.big0soft.websocket.NotificationReceiver;
import com.big0soft.websocket.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class EchoActivity extends AppCompatActivity {

    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";
    private static final String TAG = "MainActivity";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final List<String> mDataSet = new ArrayList<>();
    private final Gson mGson = new GsonBuilder().create();
    private SimpleAdapter mAdapter;
    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private RecyclerView mRecyclerView;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);

        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new SimpleAdapter(mDataSet);
        mAdapter.setHasStableIds(true);
        Log.i(TAG, "onCreate: " + mAdapter);
        Log.i(TAG, "onCreate: " + mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ANDROID_EMULATOR_LOCALHOST
                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");

        resetSubscriptions();

    }



    public void disconnectStomp(View view) {
        compositeDisposable.add(mStompClient.disconnectCompletable()
                .subscribe(() -> {
                    addItem(new EchoModel("connected: " + mTimeFormat.format(new Date())));
                }));
    }

    public void clearStompLog(View view) {
        mDataSet.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void connectStomp(View view) {

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            addItem(new EchoModel("connected: " + mTimeFormat.format(new Date())));
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            toast("Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        receiveStomp();

        mStompClient.connect(headers);
    }

    public void receiveStomp() {
        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/topic/greetings")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                    pushNotification();
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

    }

    public void sendEchoViaStomp(View v) {
        compositeDisposable.add(mStompClient.send("/topic/hello-msg-mapping", "Echo STOMP " + mTimeFormat.format(new Date()))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                }));
    }

    public void pushNotification() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Notification Title");
        intent.putExtra("message", "Notification Message");
        sendBroadcast(intent);
    }

    public void sendEchoViaRest(View v) {
        mRestPingDisposable = RestClient.getInstance().getExampleRepository()
                .sendRestEcho("Echo REST " + mTimeFormat.format(new Date()))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "REST echo send successfully");
                }, throwable -> {
                    Log.e(TAG, "Error send REST echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    private void addItem(EchoModel echoModel) {
        mDataSet.add(echoModel.getEcho() + " - " + mTimeFormat.format(new Date()));
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mDataSet.size() - 1);
    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }
}

