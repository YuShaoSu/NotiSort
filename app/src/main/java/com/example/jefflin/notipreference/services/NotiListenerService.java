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
import android.provider.CalendarContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.ActivityMain;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.Listener.SensorListener;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.database.LogModelDao;
import com.example.jefflin.notipreference.database.NotiPoolDao;
import com.example.jefflin.notipreference.database.SampleCombinationDao;
import com.example.jefflin.notipreference.database.SampleRecordDao;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.LogModel;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.model.NotiModel;
import com.example.jefflin.notipreference.model.NotiModelRemove;
import com.example.jefflin.notipreference.model.NotiPool;
import com.example.jefflin.notipreference.model.SampleCombination;
import com.example.jefflin.notipreference.model.SampleRecord;
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
import com.example.jefflin.notipreference.widgets.UnblockTask;
import com.example.jefflin.notipreference.widgets.Utils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class NotiListenerService extends NotificationListenerService {
    private static final String TAG = "MyNotificationService";
    static PackageManager packageManager;
    static NotiListenerService _this;
    static Semaphore sem = new Semaphore(0);
    Executor mExecutor = Executors.newSingleThreadExecutor();
    //    Executor mExecutor = Executors.newCachedThreadPool();
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

    private static int sizeLimit = 6;
    private static int appLimit = 4;
    // convert to sec
    private static int hourLimit = 2 * 60 * 60;


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
        GlobalClass.setDirPath(getApplicationContext().getDir("iconDir", Context.MODE_PRIVATE));


        sharedPreferences = this.getSharedPreferences("survey", MODE_PRIVATE);
        // sharePref init
        sharedPreferences.edit().putBoolean("done", false)
                .putBoolean("block", false)
                .putBoolean("dontDisturb", false)
                .putBoolean("doing", false)
                .putInt("stage", 1)
                .apply();


        return super.onBind(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap, int reason) {
//        Log.d("re onRemoved ", sbn.getPackageName() + " " + sbn.getNotification().extras.get("android.title") + " " + sbn.getId() + " " + sbn.getPostTime() + " " + reason);
        if (sbn.isClearable()) {
            final NotiDatabase db = NotiDatabase.getInstance(this);
            Notification notification = sbn.getNotification();
            final NotiModelRemove notiModelRemove = setNotiModelRemove(sbn, Calendar.getInstance().getTimeInMillis(), reason);
            if (!notiModelRemove.appName.equals("NotiSort")) {
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.notiModelRemoveDao().insertNotiRemove(notiModelRemove);
                    }
                });
            }
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        final NotiDatabase db = NotiDatabase.getInstance(this);
        final NotiModel noti;
        noti = setNotiModel(sbn);

        if (!noti.appName.equals("NotiSort")) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.notiDao().insertNoti(noti);
                }
            });
        }

        Log.d("notilistener", "sharedPreference");

        // TODO 先不看 done 以方便 debug
        if (sharedPreferences.getBoolean("block", false) || sharedPreferences.getBoolean("done", false))
            return;

        Log.d("notilistener", "before get activity");

        if (getActiveNotis()) {
            Log.d("Survey", "10分鐘後發");
            // block
            sharedPreferences.edit().putBoolean("block", true).apply();

            Timer timer = new Timer();
            timer.schedule(new PushTask(this), 600000);

            Timer timer2 = new Timer();
            timer2.schedule(new UnblockTask(this), 1500000);

//            // 2020.06.14  add count by jj
//            int count = sharedPreferences.getInt("surveySendCount", 0);
//            sharedPreferences.edit().putInt("surveySendCount", count + 1).apply();

            // first answer
            SurveyManager.getInstance().surveyInit();
            Answer answer = new Answer(Calendar.getInstance().getTimeInMillis(), SurveyManager.getInstance().getInterval());
            answer.setContext(this);
            SurveyManager.getInstance().pushAnswerList(answer);
            Timer timer1 = new Timer();
            timer1.schedule(new SampleTask(this, 1, 2), 1 * 60000);

            SurveyManager.getInstance().setMap(itemMap);
        }

        Log.d("notilistener", "onPost bottom");
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
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private boolean itemRequirement(ArrayList<NotiItem> items) {
        int notiSize = items.size();
        int uniqueApp = getNumberOfPackage(items);
        boolean meet = !(notiSize < sizeLimit || uniqueApp < appLimit);
        if (!meet) {
            basic_requirement_logging(notiSize, uniqueApp);
        }
        return meet;
    }

    private boolean poolRequirement(List<NotiPool> pools) {
        int notiSize = pools.size();
        int uniqueApp = getNumberOfPoolPackage(pools);
        boolean meet = !(notiSize < sizeLimit || uniqueApp < appLimit);
        if (!meet) {
            basic_requirement_logging(notiSize, uniqueApp);
        }
        return meet;
    }

    private void basic_requirement_logging(int notiSize, int uniqueApp) {
        StringBuilder logEvent = new StringBuilder("basic requirement/ 是否發出問卷 false\n");
        logEvent.append("notification pool size: ");
        logEvent.append(notiSize);
        logEvent.append("\n");
        logEvent.append("unique app num: ");
        logEvent.append(uniqueApp);
        logging(logEvent.toString());
    }

    private void logging(String logEvent) {
        final LogModel logModel = new LogModel(Calendar.getInstance().getTime(), logEvent);
        final LogModelDao logModelDao = NotiDatabase.getInstance(this).logModelDao();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                logModelDao.insert(logModel);
            }
        });
    }

    private boolean twoAppRequirement(List<Map.Entry<String, Integer>> sortedDrawerTwoAppMap, List<Map.Entry<String, Integer>> sortedDbTwoAppMap, int stage) {
        Map.Entry<String, Integer> first = sortedDrawerTwoAppMap.get(0);
        Map.Entry<String, Integer> second = sortedDrawerTwoAppMap.get(1);
        int dbSize = sortedDbTwoAppMap.size();
        int firstLimitNu = 0, firstLimitDe = 1, secondLimitNu = 0, secondLimitDe = 1;
        if (stage == 1) {
            firstLimitNu = 3;
            secondLimitNu = 2;
        } else if (stage == 2) {
            firstLimitNu = 2;
            secondLimitNu = 3;
            secondLimitDe = 2;
        }

        boolean meet = (!sortedDbTwoAppMap.contains(first) || sortedDbTwoAppMap.indexOf(first) * firstLimitNu / firstLimitDe <= dbSize);
                //|| sortedDbTwoAppMap.indexOf(second) * secondLimitNu / secondLimitDe <= dbSize);

        // create log
        StringBuilder logEvent = new StringBuilder("sample組成/ 是否發出問卷 ");
        logEvent.append(meet);
        logEvent.append(" ; size ");
        logEvent.append(dbSize);
        logEvent.append(" ; stage ");
        logEvent.append(stage);
        logEvent.append(";\n");
        for(Map.Entry<String, Integer> m: sortedDrawerTwoAppMap) {
            logEvent.append(m.getKey());
            logEvent.append(" fre: ");
            logEvent.append(m.getValue());
            logEvent.append(" rank: ");
            logEvent.append(sortedDbTwoAppMap.contains(m) ? sortedDbTwoAppMap.indexOf(m) : "never");
            logEvent.append("\n");
        }
        Log.d("Final機制", "" + dbSize + "," + sortedDbTwoAppMap.indexOf(first) * firstLimitNu / firstLimitDe
                + "," + sortedDbTwoAppMap.indexOf(second) * secondLimitNu / secondLimitDe + " " + first.getKey() + " : " + first.getValue());
        logging(logEvent.toString());

        return meet;
    }

    private boolean getActiveNotis() {
        ArrayList<NotiItem> click = new ArrayList<NotiItem>();
        ArrayList<NotiItem> display = new ArrayList<NotiItem>();
        int order = 0;
        itemMap = new HashMap();

        // the synthesis pool
        final List<NotiPool> notiPools = getNotiPoolWithoutDuplicateNull(getSynNotiPools());
//        notiPools = getNotiPoolWithoutDuplicateNull(notiPools);

        // deleteAll in notipool db, then insertAll
        final NotiPoolDao notiPoolDao = NotiDatabase.getInstance(this).notiPoolDao();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notiPoolDao.updateWhole(notiPools);
            }
        });

        // use notipool to check requirement
        if (!poolRequirement(notiPools))
            return false;

        // sort by postTime and create NotiItem List
        Collections.sort(notiPools);
        Collections.reverse(notiPools);
        for (NotiPool notiPool : notiPools) {
//            Log.d("NotiPool", notiPool.appName + " " + notiPool.title + " " + notiPool.content + " " + notiPool.postTime);
            click.add(new NotiItem(notiPool, order));
            display.add(new NotiItem(notiPool, order));
            order++;
        }


        Log.d("notilistener", "before getActiveNotisWithOrder " + click.size());


        HashMap<String, Integer> dbTwoAppMap = getSampleCombination();
        List<Map.Entry<String, Integer>> sortedDbTwoAppMap = new ArrayList<>(dbTwoAppMap.entrySet());
        Collections.sort(sortedDbTwoAppMap,
                new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                        return Integer.compare(b.getValue(), a.getValue());
                    }
                }
        );
        Collections.reverse(sortedDbTwoAppMap);

        List<Map.Entry<String, Integer>> sortedDrawerTwoAppMap = getSortedDrawerTwoAppMap(click, display, dbTwoAppMap);

        // check for two app requirement here
        int stage = sharedPreferences.getInt("stage", 1);
        if (!twoAppRequirement(sortedDrawerTwoAppMap, sortedDbTwoAppMap, stage)) {
            return false;
        }

        // final final final
        if (click.size() == 6) {
            itemMap.put("click", click);
            itemMap.put("display", display);
        } else {
            itemMap = getBalancedNotificationsWithOrderV3(click, display, sortedDrawerTwoAppMap);
        }

        Log.d("notilistener", "getActiveNotis after itemMap");

        // check for number final check (因為找不到為何還會<6)
        if (!itemRequirement(itemMap.get("click"))) {
            return false;
        }

        Log.d("notilistener", "getActiveNotis bottom");


        return true;
    }

    private static List<NotiPool> getNotiPoolWithoutDuplicateNull(List<NotiPool> pools) {
        List<NotiPool> newList = new ArrayList<NotiPool>();
        for (NotiPool element : pools) {
            if (element.title.equals("null") || element.content.equals("null") || element.appName.equals("NotiSort")) {
                continue;
            }
            boolean shouldAdd = true;
            for (NotiPool newElement : newList) {
                if (element.content.equals(newElement.content) && element.title.equals(newElement.title)) {
                    shouldAdd = false;
                }
            }
            if (shouldAdd) {
                newList.add(element);
            }
        }

        return newList;
    }

    private List<Map.Entry<String, Integer>> getSortedDrawerTwoAppMap(ArrayList<NotiItem> drawerNotiItems, ArrayList<NotiItem> drawerNotiItems2, HashMap<String, Integer> dbTwoAppMap) {
        // create drawer map: appName -> ArrayList<NotiItem>
        HashMap<String, List<NotiItem>> drawerAppNameMap = new HashMap<>();
        for (NotiItem drawerNotiItem : drawerNotiItems) {
            if (!drawerAppNameMap.containsKey(drawerNotiItem.appName)) {
                drawerAppNameMap.put(drawerNotiItem.appName, new ArrayList<NotiItem>());
            }
            drawerAppNameMap.get(drawerNotiItem.appName).add(drawerNotiItem);
        }

        // create drawer list<appName> with alphabet order
        List<String> drawerAppNameList = new ArrayList<>(drawerAppNameMap.keySet());
        Collections.sort(drawerAppNameList);

        // form 2-app map: app1;app2 -> frequency
        HashMap<String, Integer> drawerTwoAppMap = new HashMap<>();
        for (int i = 0; i < drawerAppNameList.size() - 1; i++) {
            for (int j = i + 1; j < drawerAppNameList.size(); j++) {
                String twoApp = drawerAppNameList.get(i) + ";" + drawerAppNameList.get(j);
                if (dbTwoAppMap.containsKey(twoApp))
                    drawerTwoAppMap.put(twoApp, dbTwoAppMap.get(twoApp));
                else
                    drawerTwoAppMap.put(twoApp, 0);
            }
        }

        // sort 2-app map by frequency
        List<Map.Entry<String, Integer>> sortedDrawerTwoAppMap = new ArrayList<>(drawerTwoAppMap.entrySet());
        Collections.sort(sortedDrawerTwoAppMap,
                new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                        return Integer.compare(b.getValue(), a.getValue());
                    }
                }
        );
        Collections.reverse(sortedDrawerTwoAppMap);

        // final 機制 by jj
        return sortedDrawerTwoAppMap;

    }

    private Map<String, ArrayList<NotiItem>> getBalancedNotificationsWithOrderV3(ArrayList<NotiItem> drawerNotiItems, ArrayList<NotiItem> drawerNotiItems2, List<Map.Entry<String, Integer>> sortedDrawerTwoAppMap) {
        ArrayList<NotiItem> tmpClick = new ArrayList<>();
        ArrayList<NotiItem> newClick = new ArrayList<>();
        ArrayList<NotiItem> newDisplay = new ArrayList<>();
        Map<String, ArrayList<NotiItem>> newMap = new HashMap<>();
        ArrayList<NotiItem> drawer = new ArrayList<>(drawerNotiItems);
        ArrayList<NotiItem> drawer2 = new ArrayList<>(drawerNotiItems2);

        // create drawer map: appName -> ArrayList<NotiItem>
        HashMap<String, List<NotiItem>> drawerAppNameMap = new HashMap<>();
        for (NotiItem drawerNotiItem : drawerNotiItems) {
            if (!drawerAppNameMap.containsKey(drawerNotiItem.appName)) {
                drawerAppNameMap.put(drawerNotiItem.appName, new ArrayList<NotiItem>());
            }
            drawerAppNameMap.get(drawerNotiItem.appName).add(drawerNotiItem);
        }

        // pick 2-app notification into tmpClick
        Set<String> addedAppSet = new HashSet<>();
        for (Map.Entry<String, Integer> twoApp : sortedDrawerTwoAppMap) {
            if (tmpClick.size() == 6) break;
            String[] twoAppList = twoApp.getKey().split(";");
            if (!addedAppSet.contains(twoAppList[0])) {
                int i = ThreadLocalRandom.current().nextInt(0, drawerAppNameMap.get(twoAppList[0]).size());
                NotiItem notiItemToAdd1 = drawerAppNameMap.get(twoAppList[0]).get(i);
                tmpClick.add(notiItemToAdd1);
                drawerNotiItems.remove(notiItemToAdd1);
                addedAppSet.add(twoAppList[0]);
            }
            if (!addedAppSet.contains(twoAppList[1])) {
                int j = ThreadLocalRandom.current().nextInt(0, drawerAppNameMap.get(twoAppList[1]).size());
                NotiItem notiItemToAdd2 = drawerAppNameMap.get(twoAppList[1]).get(j);
                tmpClick.add(notiItemToAdd2);
                drawerNotiItems.remove(notiItemToAdd2);
                addedAppSet.add(twoAppList[1]);
            }
        }

        // if size < 6, fill
        while (tmpClick.size() < 6) {
            if (drawerNotiItems.isEmpty()) break; // 會不滿6個
            int i = ThreadLocalRandom.current().nextInt(0, drawerNotiItems.size());
            tmpClick.add(drawerNotiItems.get(i));
        }

        // reorder
        Set<Integer> originOrder = new HashSet<>();
        for (NotiItem notiItem : tmpClick) {
            originOrder.add(notiItem.getOriginOrder());
        }
        for (int i = 0; i < drawer.size(); ++i) {
            if (originOrder.contains(drawer.get(i).getOriginOrder())) {
                newClick.add(drawer.get(i));
                newDisplay.add(drawer2.get(i));
            }
        }

        // return
        newMap.put("click", newClick);
        newMap.put("display", newDisplay);
        return newMap;
    }

    private HashMap<String, Integer> getSampleCombination() {

        final SampleCombinationDao sampleCombinationDao = NotiDatabase.getInstance(this).sampleCombinationDao();
        List<SampleCombination> results = new ArrayList<>();
        FutureTask<List<SampleCombination>> mFuture = new FutureTask<List<SampleCombination>>(new Callable<List<SampleCombination>>() {
            @Override
            public List<SampleCombination> call() throws Exception {
                return sampleCombinationDao.getAll();
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            results = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> resultMap = new HashMap<>();
        if (!results.isEmpty()) {
            for (SampleCombination s : results) {
                resultMap.put(s.getAppNameComb(), s.getCount());
            }
        }

        return resultMap;
    }

    private NotiItem getMostCommonNotiItem(ArrayList<NotiItem> notiItems, Map<String, Integer> appNameMap) {
        int max = -1;
        ArrayList<NotiItem> commonNotiItem = new ArrayList<>();
        for (NotiItem notiItem : notiItems) {
            int count = 0;
            if (appNameMap.containsKey(notiItem.appName)) count = appNameMap.get(notiItem.appName);
            if (count > max) max = count;
        }
        for (NotiItem notiItem : notiItems) {
            int count = 0;
            if (appNameMap.containsKey(notiItem.appName)) count = appNameMap.get(notiItem.appName);
            if (count == max) commonNotiItem.add(notiItem);
        }
        int i = ThreadLocalRandom.current().nextInt(0, commonNotiItem.size());
        return commonNotiItem.get(i);
    }

    private NotiItem getLeastCommonNotiItem(ArrayList<NotiItem> notiItems, Map<String, Integer> appNameMap) {
        int min = 9999;
        ArrayList<NotiItem> rareNotiItem = new ArrayList<>();
        for (NotiItem notiItem : notiItems) {
            int count = 0;
            if (appNameMap.containsKey(notiItem.appName)) count = appNameMap.get(notiItem.appName);
            if (count < min) min = count;
        }
        for (NotiItem notiItem : notiItems) {
            int count = 0;
            if (appNameMap.containsKey(notiItem.appName)) count = appNameMap.get(notiItem.appName);
            if (count == min) rareNotiItem.add(notiItem);
        }
        int i = ThreadLocalRandom.current().nextInt(0, rareNotiItem.size());
        return rareNotiItem.get(i);
    }


    private ArrayList<NotiItem> getNonRepeatNotification(ArrayList<NotiItem> notiItems, List<SampleRecord> sampleRecords, int maxRepeat) {
        // if maxRepeat = 0 => if repeat once than delete that notification
        ArrayList<NotiItem> newList = new ArrayList<NotiItem>();

        for (NotiItem element : notiItems) {
            int repeatCount = 0;
            for (SampleRecord newElement : sampleRecords) {
                if (element.appName.equals(newElement.getAppName()) && element.content.equals(newElement.getContent()) && element.title.equals(newElement.getTitle())) {
                    repeatCount++;
                }
            }
            if (repeatCount <= maxRepeat) {
                newList.add(element);
            }
        }
        return newList;
    }

    private List<NotiPool> getSynNotiPools() {
        List<NotiPool> newNotiPools = new ArrayList<>();
        HashSet<String> appTable = new HashSet<>();
        for (StatusBarNotification notification : getActiveNotifications()) {
            NotiPool np = setNotiPool(notification);
            newNotiPools.add(np);
            appTable.add(np.appName);
        }
        List<NotiPool> notiPools = getNotiPools();

        long now = System.currentTimeMillis();
        for (NotiPool n : notiPools) {
            int timeDiff = (int) (now - n.postTime) / 1000;
            if (timeDiff > hourLimit) {
//                Log.d("notipool", "delete the noti one hr ago " + n.appName + " " + n.title);
                continue;
            }
            if (!appTable.contains(n.appName)) {
                newNotiPools.add(n);
            }
        }
        return newNotiPools;
    }

    private List<NotiPool> getNotiPools() {
        final NotiPoolDao notiPoolDao = NotiDatabase.getInstance(this).notiPoolDao();
        List<NotiPool> l = new ArrayList<>();
        FutureTask<List<NotiPool>> mFuture = new FutureTask<List<NotiPool>>(new Callable<List<NotiPool>>() {
            @Override
            public List<NotiPool> call() throws Exception {
                return notiPoolDao.getAll();
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            l = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    private Map<String, Integer> getSampleRecordMap() {
        final SampleRecordDao sampleRecordDao = NotiDatabase.getInstance(this).sampleRecordDao();
        Map<String, Integer> appNameMap = new LinkedHashMap<>();
        FutureTask<Map<String, Integer>> mFuture = new FutureTask<Map<String, Integer>>(new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> appNameMap = new LinkedHashMap<>();
                List<String> allAppName = sampleRecordDao.getAppNameInOrder();
                List<Integer> allAppNameCount = sampleRecordDao.getAppNameCountInOrder();
                Iterator<String> i1 = allAppName.iterator();
                Iterator<Integer> i2 = allAppNameCount.iterator();
                while (i1.hasNext() || i2.hasNext()) appNameMap.put(i1.next(), i2.next());
                return appNameMap;
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            appNameMap = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appNameMap;
    }


    private List<SampleRecord> getSampleRecord() {
        final SampleRecordDao sampleRecordDao = NotiDatabase.getInstance(this).sampleRecordDao();
        List<SampleRecord> l = new ArrayList<SampleRecord>();
        FutureTask<List<SampleRecord>> mFuture = new FutureTask<List<SampleRecord>>(new Callable<List<SampleRecord>>() {
            @Override
            public List<SampleRecord> call() throws Exception {
                return sampleRecordDao.getLast();
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            l = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
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

        Log.d("notilistener", "inside getWithOrder");

        for (int i = 0; i <= 3; ++i) {
            Log.d("bound ", i + " :" + bound.get(i));
        }

        for (int i = 0; i < 3; ++i) {
            int a = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
            int b = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
            while (a == b && (bound.get(i) + 1) != bound.get(i + 1)) {
                b = ThreadLocalRandom.current().nextInt(bound.get(i), bound.get(i + 1));
                Log.d("notilistener", "inside getWithOrder random a,b same");
            }
            newClick.add(click.get(a));
            newClick.add(click.get(b));
            newDisplay.add(display.get(a));
            newDisplay.add(display.get(b));
        }

        map.put("click", newClick);
        map.put("display", newDisplay);

        Log.d("notilistener", "getWithOrder bottom");

        return map;
    }


//    private static ArrayList<NotiItem> getNotificationsWithCategories(ArrayList<NotiItem> data, int numNoti, int numCategory) {
//        ArrayList<NotiItem> subData = new ArrayList<NotiItem>();
//        ArrayList<String> categories = new ArrayList<String>();
//
//        // Example
//        // numNoti = 6, numCategory = 4
//        // input = [a,a,a,a,a,a,b,b,b,c,c,c,d,d,d]
//        // output = [a,a,a,b,c,d]
//
//        for (NotiItem element : data) {
//            // add to categories if its a new category
//            if (!categories.contains(element.category)) {
//                categories.add(element.category);
//            }
//            // add to subData if space is enough
//            if (subData.size() - categories.size() < numNoti - numCategory) {
//                subData.add(element);
//            }
//            if (subData.size() == numNoti) {
//                break;
//            }
//        }
//
//        return subData;
//    }

//    private static ArrayList<NotiItem> getNotisWithoutDuplicateNull(ArrayList<NotiItem> activeData) {
//        ArrayList<NotiItem> newList = new ArrayList<NotiItem>();
//
//        for (NotiItem element : activeData) {
//            boolean shouldAdd = true;
//            for (NotiItem newElement : newList) {
//                if (element.content.equals(newElement.content) && element.title.equals(newElement.title)) {
//                    shouldAdd = false;
//                }
//            }
//            if (shouldAdd && !isNull(element) && !isNotiSort(element)) {
//                newList.add(element);
//            }
//        }
//
//        return newList;
//    }

    private static boolean isNotiSort(NotiItem item) {
        return item.appName.equals("NotiSort");
    }

    private static boolean isNull(NotiItem item) {
        return item.content.equals("null") || item.title.equals("null");
    }

    private static int getNumberOfPackage(ArrayList<NotiItem> data) {
        ArrayList<String> packages = new ArrayList<>();

        for (NotiItem item : data) {
            packages.add(item.appName);
        }

        Set<String> distinct = new HashSet<>(packages);
        return distinct.size();
    }

    private static int getNumberOfPoolPackage(List<NotiPool> data) {
        ArrayList<String> packages = new ArrayList<>();

        for (NotiPool item : data) {
            packages.add(item.appName);
        }

        Set<String> distinct = new HashSet<>(packages);
        return distinct.size();
    }

    private NotiModelRemove setNotiModelRemove(StatusBarNotification notification, Long removeTime, int reason) {
        String packageName = "null";
        String title = "";
        String content = "null";
        String appName = "null";

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

        SharedPreferences sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE);
        NotiModelRemove notiModelRemove = new NotiModelRemove(appName, title, content, postTime, removeTime, reason, sharedPreferences.getString("ID", "user id fail"));

        return notiModelRemove;
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
        NotiModel notiModel = new NotiModel(appName, title, content, postTime, category, sharedPreferences.getString("ID", "user id fail"), notification.isOngoing(), notification.isClearable());

        return setContext(notiModel);
    }

    public static NotiPool setNotiPool(StatusBarNotification notification) {
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

        NotiPool notiPool = new NotiPool(appName, title, content, postTime, category);
        notiPool.setIcon(icon);

        return notiPool;
    }

//    public static NotiItem setNotiItem(StatusBarNotification notification, int order) {
//        String icon = "null";
//        String packageName = "null";
//        String title = "null";
//        String content = "null";
//        String appName = "null";
//        String category = "null";
//
//        Long postTime = notification.getPostTime();
//
//        try {
//            packageName = notification.getPackageName();
//        } catch (Exception e) {
//            Log.e("NotiListenerService", "package name failed", e);
//        }
//        try {
//            title = notification.getNotification().extras.get("android.title").toString();
//
//        } catch (Exception e) {
//            Log.d("NotiListenerService", "title failed");
//        }
//        try {
//            content = notification.getNotification().extras.get("android.text").toString();
//        } catch (Exception e) {
//            Log.d("NotiListenerService", "content failed");
//        }
//        try {
//            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
//            appName = (String) (applicationInfo != null ?
//                    packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
//        } catch (Exception e) {
//            Log.e("NotiListenerService", "appName failed", e);
//        }
//        try {
//            category = (notification.getNotification().category == null) ? " " : notification.getNotification().category;
//        } catch (Exception e) {
//            Log.e("NotiListenerService", "category failed", e);
//        }
//        try {
//            IconHandler iconHandler = new IconHandler();
//            icon = iconHandler.saveToInternalStorage(packageManager.getApplicationIcon(packageName), GlobalClass.getDirPath(), appName);
//        } catch (Exception e) {
//            Log.e("NotiListenerService", "icon failed", e);
//        }
//
//        NotiItem notiItem = new NotiItem(appName, title, content, postTime, category, order);
//        notiItem.setIcon(icon);
//
//        return notiItem;
//    }

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
        cf.add(Calendar.HOUR_OF_DAY, 24);
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
