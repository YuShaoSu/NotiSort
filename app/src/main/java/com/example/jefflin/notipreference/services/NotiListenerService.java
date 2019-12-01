package com.example.jefflin.notipreference.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.AudioManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.manager.SampleManager;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.widgets.BlockTask;
import com.example.jefflin.notipreference.widgets.PushNotification;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class NotiListenerService extends NotificationListenerService {
    static PackageManager packageManager;
    private int notiNum = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private AudioManager mAudioManager;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        setSchedule();

        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        if(SurveyManager.getInstance().isSurveyBlock() || SurveyManager.getInstance().isSurveyDone()) return;
        ArrayList<NotiItem> mActiveData;
        Map<String, ArrayList<NotiItem>> map = getActiveNotis();
        mActiveData = map.get("click");

        if(!sbn.isOngoing()) {
            Log.d("done", String.valueOf(SurveyManager.getInstance().isSurveyDone()));
            Log.d("block", String.valueOf(SurveyManager.getInstance().isSurveyBlock()));
        }

         if(mActiveData.size() > 5) {

             // Contextual Data
             // location
             fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                 @Override
                 public void onSuccess(Location location) {
                     // Got last known location. In some rare situations this can be null.
                     if (location != null) {
                         // Logic to handle location object
                         Log.d("Survey Post location", String.valueOf(location));
                         SurveyManager.getInstance().setCurrentLocation(location.getLatitude(), location.getLongitude());
                     }
                     else {
                         Log.d("Survey Post location", "Failed");
                     }
                 }
             });
             // ringer mode
             SurveyManager.getInstance().setRingerMode(mAudioManager.getRingerMode());

             // block
             Timer timer = new Timer();
             timer.schedule(new BlockTask(), 600000);

             Notification notification = sbn.getNotification();
             //check if the new post notification is ongoing
             SurveyManager.getInstance().setMap(map);
             SurveyManager.getInstance().setSurveyBlock(true);
             new PushNotification(this);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("NotiListenerService","removed");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public static Map<String, ArrayList<NotiItem>> getActiveNotis() {
        NotiListenerService notiListenerService = NotiListenerService.get();
        ArrayList<NotiItem> activeData = new ArrayList<NotiItem>();
        ArrayList<NotiItem> activeDataDisplay = new ArrayList<NotiItem>();
        int order = 0;
        Map<String, ArrayList<NotiItem>> map = new HashMap();
        for (StatusBarNotification notification : notiListenerService.getActiveNotifications()) {

            String icon = "null";
            String packageName = "null";
            String title = "";
            String content = "null";
            String appName = "null";
            String category = "null";

            Long postTime = notification.getPostTime();

            try{
                packageName = notification.getPackageName();
            } catch (Exception e) {
                Log.e("NotiListenerService", "package name failed", e);
            }
            try{
                title = notification.getNotification().extras.get("android.title").toString();

            } catch (Exception e) {
                Log.d("NotiListenerService", "title failed");
//                continue;
            }
            try{
                content = notification.getNotification().extras.get("android.text").toString();
            } catch (Exception e) {
                Log.d("NotiListenerService", "content failed");
//                continue;
            }
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo( packageName, 0);
                appName = (String) (applicationInfo != null ?
                        packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
            } catch (Exception e) {
                Log.e("NotiListenerService", "appName failed", e);
//                continue;
            }
            try {
                category = (notification.getNotification().category == null) ? " " : notification.getNotification().category;
            } catch (Exception e){
                Log.e("NotiListenerService", "category failed", e);
            }
            try {
                IconHandler iconHandler = new IconHandler();
                icon = iconHandler.saveToInternalStorage(packageManager.getApplicationIcon(packageName), GlobalClass.getDirPath(), appName);
            } catch (Exception e) {
                Log.e("Rank","icon failed", e);
            }

//            Log.d("Notification Info:", "   App name: " + appName + "  Title: " + title + "  Content: " + content + "   Category: " + category);

            activeData.add(new NotiItem(icon, appName, title, content, postTime, category, order));
            activeDataDisplay.add(new NotiItem(icon, appName, title, content, postTime, category, order));
            order++;
            map.put("click", getNotisWithoutDuplicate(activeData));
            map.put("display", getNotisWithoutDuplicate(activeDataDisplay));
        }
        return map;
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

    private void setSchedule(){
        for (int i = 0; i < 5; ++i){
            Intent myIntent = new Intent(this , SampleManager.class);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            myIntent.putExtra("interval", i);
            myIntent.setAction("com.example.jefflin.notipreference.next_interval");

            PendingIntent pi = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, GlobalClass.getIntervalTime()[i]);
            c.set(Calendar.MINUTE, GlobalClass.getIntervalMinute());
            c.set(Calendar.SECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            Log.d("interval time", String.valueOf(c.getTime()));
        }
    }

}
