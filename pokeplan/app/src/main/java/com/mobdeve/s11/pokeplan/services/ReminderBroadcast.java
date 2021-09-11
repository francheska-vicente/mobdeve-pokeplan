package com.mobdeve.s11.pokeplan.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.activities.MainActivity;
import com.mobdeve.s11.pokeplan.fragments.TaskListFragment;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("NOTIF_CODE", 1);
        String title = intent.getStringExtra("TASKNAME");
        String description = intent.getStringExtra("NOTIF_MESSAGE");

        Log.d("hello let's", title + " " + description);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "pokeplanNotify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notifBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
        notifBuilder.setAutoCancel(true);
        notifBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify(requestCode, notifBuilder.build());
    }

}
