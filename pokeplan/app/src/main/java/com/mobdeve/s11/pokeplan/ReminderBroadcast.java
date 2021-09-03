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
                .setContentText(intent.getStringExtra("NOTIF_MESSAGE"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notifBuilder.setAutoCancel(true);
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify((int) System.currentTimeMillis(), notifBuilder.build());
    }

}
