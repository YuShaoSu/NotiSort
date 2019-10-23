package com.example.jefflin.notipreference.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
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
import com.example.jefflin.notipreference.helper.BitmapConverter;
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
        if(mActiveData.size() > 5) {
            mData = mActiveData;
            Notification notification = sbn.getNotification();
            //check if the new post notification is ongoing
            if (notification.category != null && !(
                    notification.category.equals("alarm") || notification.category.equals("call") ||
                    notification.category.equals("navigation") || notification.category.equals("progress") ||
                    notification.category.equals("service") || notification.category.equals("transport"))) {
                PushNotification pushNotification = new PushNotification(this);
            }
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
            byte[] icon = null;
            String packageName = "null";
            String title = "null";
            String content = "null";
            String appName = "null";
            String category = "null";
            int icon_height = 1;
            int icon_width = 1;
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
                BitmapConverter converter = new BitmapConverter();
//                BitmapDrawable tmpDraw = (BitmapDrawable)packageManager.getApplicationIcon(packageName);
                Bitmap Bmp = converter.getBitmapFromDrawable(packageManager.getApplicationIcon(packageName));
                icon = converter.toByte(Bmp);
                icon_height = Bmp.getHeight();
                icon_width = Bmp.getWidth();

            } catch (Exception e) {
                Log.e("Rank","icon failed", e);
            }

//            Log.d("Notification Info:", "   App name: " + appName + "  Title: " + title + "  Content: " + content + "   Category: " + category);

            activeData.add(new NotiItem(icon, appName, title, content, postTime, category, icon_height, icon_width));
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
