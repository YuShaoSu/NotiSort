package com.example.jefflin.notipreference;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

public class NotiListenerService extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Noti","bind");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Log.d("Noti","posted");
        Notification notification = sbn.getNotification();

        Intent intent = new  Intent("NotiListenerService.Arrival");
        intent.putExtra("appname", sbn.getPackageName());
        intent.putExtra("title", notification.extras.get("android.title").toString());
        intent.putExtra("content", notification.extras.get("android.text").toString());

        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("Noti","removed");
        // Implement what you want here
    }
}
