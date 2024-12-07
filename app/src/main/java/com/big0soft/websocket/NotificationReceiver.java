package com.big0soft.websocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the notification service
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}