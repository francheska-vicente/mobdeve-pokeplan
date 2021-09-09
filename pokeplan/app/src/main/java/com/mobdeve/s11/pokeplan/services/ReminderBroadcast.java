package com.mobdeve.s11.pokeplan.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mobdeve.s11.pokeplan.R;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "pokeplanNotify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(intent.getStringExtra("TASKNAME"))
                .setContentText(intent.getStringExtra("NOTIF_MESSAGE"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notifBuilder.setAutoCancel(true);
        notifBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        int requestCode = intent.getIntExtra("NOTIF_CODE", 1);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify(requestCode, notifBuilder.build());
    }

}
