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
import com.example.jefflin.notipreference.widgets.PushNotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotiListenerService extends NotificationListenerService {
    private static ArrayList<NotiItem> mData = new ArrayList<NotiItem>();
    PackageManager packageManager;
    private int notiNum = 0;
    private boolean spotifyRepeat = false;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotiListenerService","bind");
        packageManager = getPackageManager();
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        String packageName = "null";
        String title = "null";
        String content = "null";
        String appName = "null";
        String category = "null";
        Long postTime = sbn.getPostTime();

        Notification notification = sbn.getNotification();

        Log.d("NotiListenerService","posted");
        Log.d("is ongoing", Boolean.toString(sbn.isOngoing()));
        Log.d("app name", sbn.getPackageName());

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
            category = (notification.category == null) ? "null" : notification.category;
        } catch (Exception e){
            Log.d("NotiListenerService",category);
        }

        //check if spotify repeat, 避免按暫停時存到一堆不是ongoing 的 noti
        if(appName.equals("Spotify")) {
            if(!spotifyRepeat) {
                spotifyRepeat = true;
                pushNotification();
                mData.add(new NotiItem(appName, title, content, postTime, category));
            }
        }
        else if(!sbn.isOngoing()){
            pushNotification();
            mData.add(new NotiItem(appName, title, content, postTime, category));
        } else if (mData.isEmpty() || !isOngoingAppNameRepeat(mData, new NotiItem(appName, title, content, postTime, category))){
            pushNotification();
            mData.add(new NotiItem(appName, title, content, postTime, category));
        }

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

    private static boolean isOngoingAppNameRepeat(ArrayList<NotiItem> list, NotiItem item) {
        Log.d("inside function ", "ongoing repeat appName checking");
        int j;
        boolean repeat = false;
        for (j=0; j<list.size(); j++) {
            Log.d("item[" + j + "].appname:", list.get(j).appName);
            if (item.appName.equals(list.get(j).appName)) {
                Log.d("repeat app name",item.appName);
                Log.d("repeat app category",item.category);
                repeat = true;
                break;
            }
        }
        return repeat;
    }

    private void pushNotification(){
        if(notiNum >= 10){
            PushNotification pushNotification = new PushNotification(this);
            notiNum = 0;
        }
        else {
            notiNum++;
        }
    }
}
