package com.example.jefflin.notipreference.widgets;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.receiver.NotificationDisMissReceiver;

public class PushNotification {

    NotificationManagerCompat notificationManager;
    Context context;

    public PushNotification(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    public void push(boolean onGoing) {
        Intent intent = new Intent(context, ActivityMain.class);
        //如果正在app裡就開一個新的task
        //在外面就用exist的
        if (!onGoing)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//        Intent dIntent = new Intent(context, NotificationDisMissReceiver.class);
//        dIntent.setAction("com.example.jefflin.notipreference.dismiss");
//        PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 20, dIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, onGoing ? String.valueOf(R.string.channelOngoingID) : String.valueOf(R.string.channelID))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("NotiSort")
                .setContentText(onGoing ? "NotiSort正在運行中" : "請填寫問卷！")
                .setContentIntent(pendingIntent)
                .setPriority(onGoing ? NotificationCompat.PRIORITY_MIN : NotificationCompat.PRIORITY_MAX)
                .setDefaults(onGoing ? 0 : Notification.DEFAULT_ALL)
                .setOngoing(onGoing);
        if (!onGoing) {
            builder.setTimeoutAfter(600000)
                .setAutoCancel(true);
        }
//                .setDeleteIntent(deleteIntent);
        notificationManager.notify(onGoing ? 22 : 20, builder.build());
    }

    public void cancel(boolean onGoing) {
        notificationManager.cancel(onGoing ? 22 : 20);
    }

}
