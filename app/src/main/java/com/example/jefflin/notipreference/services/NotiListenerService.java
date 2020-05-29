package com.example.jefflin.notipreference.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.Listener.SensorListener;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.model.NotiModel;
import com.example.jefflin.notipreference.receiver.ActivityRecognitionReceiver;
import com.example.jefflin.notipreference.receiver.LocationUpdateReceiver;
import com.example.jefflin.notipreference.receiver.NotificationDisMissReceiver;
import com.example.jefflin.notipreference.receiver.SampleReceiver;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.Listener.TelephonyListener;
import com.example.jefflin.notipreference.widgets.PushNotification;
import com.example.jefflin.notipreference.widgets.PushTask;
import com.example.jefflin.notipreference.widgets.SampleTask;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class NotiListenerService extends NotificationListenerService {
    private static final String TAG = "MyNotificationService";
    static PackageManager packageManager;
    static NotiListenerService _this;
    static Semaphore sem = new Semaphore(0);
    Executor mExecutor = Executors.newSingleThreadExecutor();
    private FusedLocationProviderClient fusedLocationClient;
    private AudioManager audioManager;
    private BatteryManager batteryManager;
    private PowerManager powerManager;
    private UsageStatsManager usageStatsManager;
    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;
    private SensorManager sensorManager;
    private SensorListener sensorListener = new SensorListener();
    static ContextManager contextManager = ContextManager.getInstance();
    private PushNotification pushNotification;
    private SharedPreferences sharedPreferences;

    private static Map<String, ArrayList<NotiItem>> itemMap;

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Connected");
        //_this = this;
        //sem.release();

        pushNotification = new PushNotification(this);
        pushNotification.push(true);
    }

    @Override
    public void onListenerDisconnected() {
        Log.i(TAG, "Disconnected");
        //sem.acquireUninterruptibly();
        //_this = null;
        pushNotification.cancel(true);
    }

    private void startForeground() {
        Intent intent = new Intent(this, ActivityMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 55, intent, 0);

        Notification onGoing = new NotificationCompat.Builder(this, String.valueOf(R.string.channelOngoingID))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("NotiSort")
                .setContentText("Notisort 正在運行中")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setDefaults(0)
                .setSound(null)
                .build();


//        startForeground(1, onGoing);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "bind");
        packageManager = getPackageManager();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setSchedule();
        requestActivityTransitionUpdates(this);
        requestLocationUpdates(this);
        requestPhoneStateUpdates(this);
        registerSensorUpdates(this);

        sharedPreferences = this.getSharedPreferences("survey", MODE_PRIVATE);
        // sharePref init
        sharedPreferences.edit().putBoolean("done", false)
                .putBoolean("block", false)
                .putBoolean("dontDisturb", false)
                .putBoolean("doing", false)
                .apply();


        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final NotiDatabase db = NotiDatabase.getInstance(this);
        final NotiModel noti;
        noti = setNotiModel(sbn);


        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.notiDao().insertNoti(noti);
            }
        });

        if (sharedPreferences.getBoolean("block", false) || sharedPreferences.getBoolean("done", false))
            return;

        if (getActiveNotis()) {
            Log.d("Survey", "10分鐘後發");
            // block
            Timer timer = new Timer();
            timer.schedule(new PushTask(this), 600000);

            // first answer
            Answer answer = new Answer(Calendar.getInstance().getTimeInMillis(), SurveyManager.getInstance().getInterval());
            answer.setContext(this);
            SurveyManager.getInstance().pushAnswerList(answer);
            Timer timer1 = new Timer();
            timer1.schedule(new SampleTask(this, 1, 2), 1 * 60000);

            SurveyManager.getInstance().setMap(itemMap);
            sharedPreferences.edit().putBoolean("block", true).apply();
        }
    }


    private NotiModel setContext(NotiModel notiModel) {
        notiModel.setLocation(contextManager.locatoinLongtitude,
                contextManager.locatoinLatitude, contextManager.locatoinAccuracy);
        notiModel.setBattery(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY), batteryManager.isCharging());
        notiModel.setRingerTone(audioManager.getRingerMode());
        notiModel.setScreenOn(powerManager.isInteractive());
        notiModel.setDeviceIdle(powerManager.isDeviceIdleMode());
        notiModel.setPowerSave(powerManager.isPowerSaveMode());
        notiModel.callState = telephonyManager.getCallState();

        Network[] networks = connectivityManager.getAllNetworks();
        StringBuilder ntwSB = new StringBuilder();
        for (Network n : networks) {
            ntwSB.append(connectivityManager.getNetworkInfo(n));
        }
        notiModel.network = ntwSB.toString();


        // recent app
        StringBuilder rappSB = new StringBuilder();
        List<UsageStats> recentApp;
        recentApp = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                System.currentTimeMillis() - 5000,
                System.currentTimeMillis());
        for (UsageStats u : recentApp) {
            if (u.getLastTimeUsed() == 0) continue;
            rappSB.append(u.getPackageName());
            rappSB.append(" : ");
            rappSB.append(u.getLastTimeUsed() + "; ");
        }
        notiModel.recentApp = rappSB.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SignalStrength signalStrength = telephonyManager.getSignalStrength();
            try {
                List<CellSignalStrength> ss = signalStrength.getCellSignalStrengths();
                for (CellSignalStrength s : ss) {
                    notiModel.signalType = s.toString();
                    notiModel.signalDbm = s.getDbm();
                }
            } catch (NullPointerException e) {
                Log.e("signal strength", "getCellSignalStrength null pt", e);
            }
        } else {
            notiModel.signalType = contextManager.phoneSignalType;
            notiModel.signalDbm = contextManager.phoneSignalDbm;
        }

        // Sensors
        notiModel.light = contextManager.light;


        return notiModel;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private boolean getActiveNotis() {
        //NotiListenerService notiListenerService = NotiListenerService.get();
        ArrayList<NotiItem> click = new ArrayList<NotiItem>();
        ArrayList<NotiItem> display = new ArrayList<NotiItem>();
        int order = 0;
        itemMap = new HashMap();
        for (StatusBarNotification notification : getActiveNotifications()) {
            click.add(setNotiItem(notification, order));
            display.add(setNotiItem(notification, order));
            order++;
        }

        // check for requirement
        if (order < 5 || getNumberOfPackage(click) < 3)
            return false;
        // get without duplicate and null ones
        click = getNotisWithoutDuplicateNull(click);
        display = getNotisWithoutDuplicateNull(display);
        // check for number again
        if (click.size() < 5 || getNumberOfPackage(click) < 3)
            return false;

        itemMap = getNotificationsWithOrder(click, display, 6, 3);

        return true;
    }

    private static int getNumberOfCategory(ArrayList<NotiItem> data) {
        ArrayList<String> categories = new ArrayList<String>();

        for (NotiItem element : data) {
            categories.add(element.category);
        }

        Set<String> distinct = new HashSet<>(categories);
        return distinct.size();
    }

    private static int getNumberOfPackage(ArrayList<NotiItem> data) {
        ArrayList<String> packages = new ArrayList<>();

        for (NotiItem item : data) {
            packages.add(item.appName);
        }

        Set<String> distinct = new HashSet<>(packages);
        return distinct.size();
    }

    private static Map<String, ArrayList<NotiItem>> getNotificationsWithOrder(ArrayList<NotiItem> click, ArrayList<NotiItem> display, int numNoti, int numPart) {
        Map<String, ArrayList<NotiItem>> map = new HashMap<>();
        ArrayList<NotiItem> newClick = new ArrayList<NotiItem>();
        ArrayList<NotiItem> newDisplay = new ArrayList<NotiItem>();
        ArrayList<Integer> bound = new ArrayList<>();

        // if   rmd == 0 -> 0 ~ quo-1, quo ~ 2*quo - 1 , 2*quo ~ size - 1
        // elif rmd == 1 -> 0 ~ quo-1, quo ~ 2*quo, 2*quo+1 ~ size - 1
        // elif rmd == 2 -> 0 ~ quo-1, quo ~ 2*quo, 2*quo+1 ~ size - 1

        // int head = quo - 1;
        // int mid = (2 * quo - 1) + rmd;
        // int tail = data.size() - 1;

        int quo = click.size() / 3;
        int rmd = (click.size() % 3 == 0 ? 0 : 1);
        bound.add(0);
        bound.add(quo);
        bound.add((2 * quo) + rmd);
        bound.add(click.size());


        for (int i = 0; i < 3; ++i) {
            int a = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
            int b = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
            while (a == b) b = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
            newClick.add(click.get(a));
            newClick.add(click.get(b));
            newDisplay.add(display.get(a));
            newDisplay.add(display.get(b));
        }

        map.put("click", newClick);
        map.put("display", newDisplay);

        return map;
    }


    private static ArrayList<NotiItem> getNotificationsWithCategories(ArrayList<NotiItem> data, int numNoti, int numCategory) {
        ArrayList<NotiItem> subData = new ArrayList<NotiItem>();
        ArrayList<String> categories = new ArrayList<String>();

        // Example
        // numNoti = 6, numCategory = 4
        // input = [a,a,a,a,a,a,b,b,b,c,c,c,d,d,d]
        // output = [a,a,a,b,c,d]

        for (NotiItem element : data) {
            // add to categories if its a new category
            if (!categories.contains(element.category)) {
                categories.add(element.category);
            }
            // add to subData if space is enough
            if (subData.size() - categories.size() < numNoti - numCategory) {
                subData.add(element);
            }
            if (subData.size() == numNoti) {
                break;
            }
        }

        return subData;
    }


    private static ArrayList<NotiItem> getNotisWithoutDuplicateNull(ArrayList<NotiItem> activeData) {
        ArrayList<NotiItem> newList = new ArrayList<NotiItem>();

        for (NotiItem element : activeData) {
            boolean shouldAdd = true;
            for (NotiItem newElement : newList) {
                if (element.content.equals(newElement.content) && element.title.equals(newElement.title)) {
                    shouldAdd = false;
                }
            }
            if (shouldAdd && !isNull(element) && !isNotiSort(element)) {
                newList.add(element);
            }
        }

        return newList;
    }

    private static boolean isNotiSort(NotiItem item) {
        return item.appName.equals("NotiSort");
    }

    private static boolean isNull(NotiItem item) {
        return item.content.equals("null") || item.title.equals("null");
    }

    private NotiModel setNotiModel(StatusBarNotification notification) {
        String packageName = "null";
        String title = "";
        String content = "null";
        String appName = "null";
        String category = "null";

        Long postTime = notification.getPostTime();

        try {
            packageName = notification.getPackageName();
        } catch (Exception e) {
            Log.e("NotiListenerService", "package name failed", e);
        }
        try {
            title = notification.getNotification().extras.get("android.title").toString();

        } catch (Exception e) {
            Log.d("NotiListenerService", "title failed");
        }
        try {
            content = notification.getNotification().extras.get("android.text").toString();
        } catch (Exception e) {
            Log.d("NotiListenerService", "content failed");
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            appName = (String) (applicationInfo != null ?
                    packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
        } catch (Exception e) {
            Log.e("NotiListenerService", "appName failed", e);
        }
        try {
            category = (notification.getNotification().category == null) ? " " : notification.getNotification().category;
        } catch (Exception e) {
            Log.e("NotiListenerService", "category failed", e);
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE);
        NotiModel notiModel = new NotiModel(appName, title, content, postTime, category, sharedPreferences.getString("ID", "user id fail"));

        return setContext(notiModel);
    }

    public static NotiItem setNotiItem(StatusBarNotification notification, int order) {
        String icon = "null";
        String packageName = "null";
        String title = "null";
        String content = "null";
        String appName = "null";
        String category = "null";

        Long postTime = notification.getPostTime();

        try {
            packageName = notification.getPackageName();
        } catch (Exception e) {
            Log.e("NotiListenerService", "package name failed", e);
        }
        try {
            title = notification.getNotification().extras.get("android.title").toString();

        } catch (Exception e) {
            Log.d("NotiListenerService", "title failed");
        }
        try {
            content = notification.getNotification().extras.get("android.text").toString();
        } catch (Exception e) {
            Log.d("NotiListenerService", "content failed");
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            appName = (String) (applicationInfo != null ?
                    packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
        } catch (Exception e) {
            Log.e("NotiListenerService", "appName failed", e);
        }
        try {
            category = (notification.getNotification().category == null) ? " " : notification.getNotification().category;
        } catch (Exception e) {
            Log.e("NotiListenerService", "category failed", e);
        }
        try {
            IconHandler iconHandler = new IconHandler();
            icon = iconHandler.saveToInternalStorage(packageManager.getApplicationIcon(packageName), GlobalClass.getDirPath(), appName);
        } catch (Exception e) {
            Log.e("NotiListenerService", "icon failed", e);
        }

        NotiItem notiItem = new NotiItem(appName, title, content, postTime, category, order);
        notiItem.setIcon(icon);

        return notiItem;
    }

    private void setSchedule() {
//        for (int i = 0; i < 5; ++i) {
//            Intent myIntent = new Intent(this, SampleReceiver.class);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            myIntent.putExtra("interval", i);
//            myIntent.setAction("com.example.jefflin.notipreference.next_interval");
//
//            PendingIntent pi = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            Calendar c = Calendar.getInstance();
//            c.set(Calendar.HOUR_OF_DAY, GlobalClass.getIntervalTime()[i]);
//            c.set(Calendar.MINUTE, GlobalClass.getIntervalMinute());
//            c.set(Calendar.SECOND, 0);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//            Log.d("interval time", String.valueOf(c.getTime()));
//        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // end 0 o'clock
        Intent finish = new Intent(this, SampleReceiver.class);
        finish.putExtra("interval", 0);
        finish.setAction("com.example.jefflin.notipreference.next_interval");
        PendingIntent pii = PendingIntent.getBroadcast(this, 0, finish, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar ci = Calendar.getInstance();
        ci.set(Calendar.HOUR_OF_DAY, 0);
        ci.set(Calendar.MINUTE, 0);
        ci.set(Calendar.SECOND, 0);
        ci.add(Calendar.HOUR_OF_DAY, 24);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, ci.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pii);
        Log.d("interval time", String.valueOf(ci.getTime()));

        // initial 8 o'clock
        Intent init = new Intent(this, SampleReceiver.class);
        init.putExtra("interval", 1);
        init.setAction("com.example.jefflin.notipreference.next_interval");
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, init, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cf = Calendar.getInstance();
        cf.set(Calendar.HOUR_OF_DAY, 8);
        cf.set(Calendar.MINUTE, 0);
        cf.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cf.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        Log.d("interval time", String.valueOf(cf.getTime()));


    }

    void registerSensorUpdates(final Context context) {
        Sensor mLight;
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(sensorListener, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void requestPhoneStateUpdates(final Context context) {
        // if API level >= 29, don't use callback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        } else {
            TelephonyListener telephonyListener = new TelephonyListener(context);
            telephonyManager.listen(telephonyListener, PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
    }

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
                        Log.d("request AR task", "success");
                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("request AR task", "fail");
                    }
                });
    }


    private ActivityTransitionRequest buildActivityRecogRequest() {
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
