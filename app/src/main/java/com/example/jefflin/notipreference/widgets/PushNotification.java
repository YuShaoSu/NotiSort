package com.example.jefflin.notipreference.widgets;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.receiver.NotificationDisMissReceiver;

public class PushNotification {

    public PushNotification(Context context) {
        Intent intent = new Intent(context, ActivityMain.class);
        //如果正在app裡就開一個新的task
        //在外面就用exist的
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent dIntent = new Intent(context, NotificationDisMissReceiver.class);
        dIntent.setAction("com.example.jefflin.notipreference.dismiss");
        PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 20, dIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(R.string.channelID))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("NotiSort")
                .setContentText("請填寫問卷！")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setDeleteIntent(deleteIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(20, builder.build());
    }

}
