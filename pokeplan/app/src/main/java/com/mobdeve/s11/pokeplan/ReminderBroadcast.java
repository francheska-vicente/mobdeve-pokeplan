package com.mobdeve.s11.pokeplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "pokeplanNotify")
                .setSmallIcon(R.drawable.egg)
                .setContentTitle(intent.getStringExtra("TASKNAME"))
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);

        notificationCompat.notify(0, notifBuilder.build());
    }
}
