package com.big0soft.websocket;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

public class AppClass extends Application {
    public static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    public static final String CHANNEL_ID = "my_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.

// The user-visible name of the channel.
        CharSequence name = "asdasd";

// The user-visible description of the channel.
        String description = "channel_description";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

// Configure the notification channel.
        mChannel.setDescription(description);

        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        mNotificationManager.createNotificationChannel(mChannel);
    }


}
