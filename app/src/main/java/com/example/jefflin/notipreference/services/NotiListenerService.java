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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class NotiListenerService extends NotificationListenerService {
    private static ArrayList<NotiItem> mData = new ArrayList<NotiItem>();
    static PackageManager packageManager;
    private int notiNum = 0;
    private static boolean spotifyRepeat = false;

    private static final String TAG = "MyNotificationService";
    static NotiListenerService _this;
    static Semaphore sem = new Semaphore(0);

    public static NotiListenerService get() {
        sem.acquireUninterruptibly();
        NotiListenerService ret = _this;
        sem.release();
        return ret;
    }

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Connected");
        _this = this;
        sem.release();
    }

    @Override
    public void onListenerDisconnected() {
        Log.i(TAG, "Disconnected");
        sem.acquireUninterruptibly();
        _this = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotiListenerService","bind");
        packageManager = getPackageManager();
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){

        ArrayList<NotiItem> mActiveData;

        mActiveData = getActiveNotis();

        int i, category_count = 0;
        if(mActiveData.size() > 7) {
            for (i = 0; i < mActiveData.size(); ++i) {
            }
            mData = mActiveData;
            PushNotification pushNotification = new PushNotification(this);
            return;
        }


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("NotiListenerService","removed");
    }

    public static ArrayList<NotiItem> getData() {
        return mData;
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

    public static ArrayList<NotiItem> getActiveNotis() {
        NotiListenerService notiListenerService = NotiListenerService.get();
        ArrayList<NotiItem> activeData = new ArrayList<NotiItem>();

        for (StatusBarNotification notification : notiListenerService.getActiveNotifications()) {
            Drawable icon = null;
            String packageName = " ";
            String title = " ";
            String content = " ";
            String appName = " ";
            String category = " ";
            Long postTime = notification.getPostTime();

            try{
                packageName = notification.getPackageName();
            } catch (Exception e) {
                Log.d("d", "no packageName");
            }
            try{
                title = notification.getNotification().extras.get("android.title").toString();

            } catch (Exception e) {
                Log.d("d", "no title");
            }
            try{
                content = notification.getNotification().extras.get("android.text").toString();
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
                category = (notification.getNotification().category == null) ? " " : notification.getNotification().category;
            } catch (Exception e){
                Log.d("NotiListenerService",category);
            }
            try {
                icon = packageManager.getApplicationIcon(packageName);
            } catch (Exception e) {
                Log.d("Rank","icon failed");
            }

//            Log.d("Notification Info:", "   App name: " + appName + "  Title: " + title + "  Content: " + content + "   Category: " + category);

            activeData.add(new NotiItem(icon, appName, title, content, postTime, category));
        }
        return getNotisWithoutDuplicate(activeData);
    }

    private static ArrayList<NotiItem> getNotisWithoutDuplicate(ArrayList<NotiItem> activeData) {
        ArrayList<NotiItem> newList = new ArrayList<NotiItem>();

        for (NotiItem element : activeData) {
            boolean shouldAdd = true;
            for (NotiItem newElement : newList) {
                if (element.content.equals(newElement.content) && element.title.equals(newElement.title)) {
                    shouldAdd = false;
                }
            }
            if (shouldAdd && !isNotiSort(element)) {
                newList.add(element);
            }
        }

        return newList;
    }

    private static boolean isNotiSort(NotiItem item) {
        return item.appName.equals("NotiSort");
    }
}
