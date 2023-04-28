package com.example.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class EventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        String title = intent.getStringExtra("eventtitle"),description = intent.getStringExtra("eventDescription");
        int id = intent.getIntExtra("eventId",0);
        NotificationUtils notificationUtils = new NotificationUtils();
        notificationUtils.show(context,title,description,id);
    }
}
