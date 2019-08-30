package com.example.jefflin.notipreference.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotiListenerService extends NotificationListenerService {
    private static ArrayList<NotiItem> mData = new ArrayList<NotiItem>();
    PackageManager packageManager;
    private int notiNum = 0;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotiListenerService","bind");
        packageManager = getPackageManager();
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        String packageName = "";
        String title = "";
        String content = "";
        String appName = "";
        String category = "";
        Long postTime = sbn.getPostTime();

        Notification notification = sbn.getNotification();

        Log.d("NotiListenerService","posted");
        Log.d("is ongoing", Boolean.toString(sbn.isOngoing()));
        Log.d("app name", sbn.getPackageName());
        pushNotification();

        try{
            packageName = sbn.getPackageName();
        } catch (Exception e) {
            Log.d("d", "no packageName");
        }
        try{
            title = notification.extras.get("android.title").toString();
        } catch (Exception e) {
            Log.d("d", "no title");
        }
        try{
            content = notification.extras.get("android.text").toString();
        } catch (Exception e) {
            Log.d("d", "no content");
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo( packageName, 0);
            appName = (String) (applicationInfo != null ?
                    packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
        } catch (Exception e) {
            Log.d("d","app name failed");
        }
        try {
            category = notification.category;
        } catch (Exception e){
            Log.d("NotiListenerService",category);
        }

        mData.add(new NotiItem(appName, title, content, postTime, category));

        Toast.makeText(this.getBaseContext(),"Notification Received",Toast.LENGTH_LONG).show();
        Log.d("Notification Info:", "   App name: " + appName + "  Title: " + title + "  Content: " + content + "   Category: " + category);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("NotiListenerService","removed");
    }

    public static ArrayList<NotiItem> getData() {
        return mData;
    }

    public static void putData( ArrayList<NotiItem> mData6) {
        mData.addAll(mData6);
    }

    private void pushNotification(){
        if(notiNum >= 10){
            Intent intent = new Intent(this, ActivityMain.class);
            //如果正在app裡就開一個新的task
            //在外面就用existing的
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Survey Time!")
                    .setContentText("Receive more than ten notifications")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notiNum, builder.build());
            notiNum = 0;
        }
        else {
            notiNum++;
        }
    }
}
