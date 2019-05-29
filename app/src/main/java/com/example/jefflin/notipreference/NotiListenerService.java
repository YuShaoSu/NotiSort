package com.example.jefflin.notipreference;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotiListenerService extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotiListenerService","bind");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Log.d("NotiListenerService","posted");
        Notification notification = sbn.getNotification();

        Intent intent = new  Intent("NotiListenerService.Arrival");
        intent.putExtra("appname", sbn.getPackageName());
        intent.putExtra("title", notification.extras.get("android.title").toString());
        try{
            intent.putExtra("content", notification.extras.get("android.text").toString());
        } catch (Exception e) {

        }

        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("NotiListenerService","removed");
    }
}
