package com.example.jefflin.notipreference.services;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.example.jefflin.notipreference.NotiItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotiListenerService extends NotificationListenerService {
    private static ArrayList<NotiItem> mData = new ArrayList<NotiItem>();
    PackageManager packageManager;

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
        Date postTime = Calendar.getInstance().getTime();
        Drawable icon = null;
        Notification notification = sbn.getNotification();

        Log.d("NotiListenerService","posted");

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
            icon = packageManager.getApplicationIcon(packageName);
        } catch (Exception e) {
            Log.d("d","icon failed");
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

        mData.add(new NotiItem(icon, appName, title, content, postTime, category));

        Toast.makeText(this.getBaseContext(),"Notification Received",Toast.LENGTH_LONG).show();
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
}
