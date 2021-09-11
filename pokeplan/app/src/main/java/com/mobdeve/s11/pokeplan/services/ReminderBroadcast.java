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

/**
 * This class creates the broadcast receiver for the notifications for the task.
 */
public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("NOTIF_CODE", 1); // request code of the notification
        String title = intent.getStringExtra("TASKNAME"); // the name of the task that would be displayed as the notification title
        String description = intent.getStringExtra("NOTIF_MESSAGE"); // the message to be displayed in the task

        // creation of the notification builder
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "pokeplanNotify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // setting the intent that when the notification is clicked, it would open the main activity
        notifBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
        notifBuilder.setAutoCancel(true);
        notifBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        notificationCompat.notify(requestCode, notifBuilder.build());
    }

}
