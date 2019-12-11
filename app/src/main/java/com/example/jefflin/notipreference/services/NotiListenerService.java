package com.example.jefflin.notipreference.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.receiver.ActivityRecognitionReceiver;
import com.example.jefflin.notipreference.receiver.LocationUpdateReceiver;
import com.example.jefflin.notipreference.receiver.SampleReceiver;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.widgets.BlockTask;
import com.example.jefflin.notipreference.widgets.PushNotification;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
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
        requestActivityTransitionUpdates(this);
//        requestLocationUpdates(this);

        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
//        Log.d("post id", String.valueOf(sbn.getId()));
//        DatabaseManager db = new DatabaseManager(getApplicationContext());
//        db.getNotiDao().insertNoti(setNotiItem(sbn, -1));
        final NotiDatabase db = NotiDatabase.getInstance(this);
        db.notiDao().deleteAll();
//        db.notiDao().insertNoti(setNotiItem(sbn, -1));
        // Contextual Data
        // location
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d("onPost location", String.valueOf(location));
                    db.locationUpdateDao().insert(
                                new LocationUpdateModel(location.getLongitude(),
                                location.getLatitude(), location.getAccuracy(),
                                location.getTime()
                            ));
                }
                else {
                    Log.d("Survey Post location", "Failed");
                }
            }
        });

        Log.d("onPost AR DB", String.valueOf(db.activityRecognitionDao().getAll().size()));
        Log.d("onPost LU DB", String.valueOf(db.locationUpdateDao().getAll().size()));
        if(SurveyManager.getInstance().isSurveyBlock() || SurveyManager.getInstance().isSurveyDone()) return;
        ArrayList<NotiItem> mActiveData;
        Map<String, ArrayList<NotiItem>> map = getActiveNotis();
        mActiveData = map.get("click");

        if(!sbn.isOngoing()) {
            Log.d("done", String.valueOf(SurveyManager.getInstance().isSurveyDone()));
            Log.d("block", String.valueOf(SurveyManager.getInstance().isSurveyBlock()));
        }

         if(mActiveData.size() > 5) {

             Log.d("query test", String.valueOf(db.notiDao().getAll()));

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
//        Log.d("NotiListenerService","removed");
//        Log.d("remove id",String.valueOf(sbn.getId()));
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
            activeData.add(setNotiItem(notification, order));
            activeDataDisplay.add(setNotiItem(notification, order));
            order++;
            map.put("click", getNotisWithoutDuplicate(activeData));
            map.put("display", getNotisWithoutDuplicate(activeDataDisplay));
        }
        return map;
    }

    public static NotiItem setNotiItem(StatusBarNotification notification, int order) {
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

        NotiItem notiItem = new NotiItem(appName, title, content, postTime, category, order);
        notiItem.setIcon(icon);

        return notiItem;
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
            Intent myIntent = new Intent(this , SampleReceiver.class);
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

    // receive too much!
    void requestLocationUpdates(final Context context) {
        LocationRequest request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5);
        Intent intent = new Intent(this, LocationUpdateReceiver.class);
        intent.setAction("com.example.jefflin.notipreference.location_update");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Task task = fusedLocationClient.requestLocationUpdates(request, pendingIntent);
        task.addOnCompleteListener(
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("request Location task", "success");
                        } else {
                            Log.d("request Location task", "fail");
                        }
                    }
                }
        );

    }

    void requestActivityTransitionUpdates(final Context context) {
        ActivityTransitionRequest request = buildActivityRecogRequest();
        Intent intent = new Intent(this, ActivityRecognitionReceiver.class);
        intent.setAction("com.example.jefflin.notipreference.activity_recog_transition");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Task task = ActivityRecognition.getClient(context)
                .requestActivityTransitionUpdates(request, pendingIntent);
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(context,
                                "Successfully requested activity updates",
                                Toast.LENGTH_SHORT)
                                .show();
                        Log.d("request AR task", "success");
                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context,
                                "Requesting activity updates failed to start",
                                Toast.LENGTH_SHORT)
                                .show();
                        Log.d("request AR task", "fail");
                    }
                });
    }


    private ActivityTransitionRequest buildActivityRecogRequest(){
        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.WALKING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.RUNNING)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());


        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_BICYCLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_BICYCLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        return new ActivityTransitionRequest(transitions);

    }

}
