package com.big0soft.websocket;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.big0soft.websocket.echo.EchoActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationService extends IntentService {

    private static final String TAG = "NotificationService";

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Handle incoming notification
        Log.d(TAG, "Received notification");

        // Get the notification data from the intent
        Bundle extras = intent.getExtras();
        String title = extras.getString("title");
        String message = extras.getString("message");

        // Create and show the notification
        sendNotification(title, message);
    }

    public static   int NOTIFICATION_ID = 1;
    private void sendNotification(String title, String message) {

        Intent intent = new Intent(this, EchoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"my_channel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID++, builder.build());
    }
}


