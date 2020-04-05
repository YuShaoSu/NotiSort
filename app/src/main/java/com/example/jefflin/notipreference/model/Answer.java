package com.example.jefflin.notipreference.model;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.CellSignalStrength;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.jefflin.notipreference.manager.ContextManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Answer implements Serializable {

    public String deviceID;
    private String id;
    private long surveyPostTime;
    private long surveyFinishTime;
    private int interval;
    private ArrayList<NotiItem> notifications;
    private String esm_q1;
    private String esm_q2;
    private String esm_q3;
    private String esm_q4;
    private String esm_q5;
    private String esm_q6;
    private String esm_q7;

    // location
    public double longitude;
    public double latitude;
    public float locationAccuracy;

    // status
    public boolean isCharging;
    public int battery;
    public int ringerMode;
    public boolean isScreenOn;
    public boolean isDeviceIdle;
    public boolean isPowerSave;
    public String recentApp;
    public int callState;


    // connectivity
    public String network;
    public String signalType;
    public int signalDbm;

    // sensor
//    public float accelerometerX, accelerometerY, accelerometerZ;
//    public float gyroscopeX, gyroscopeY, gyroscopeZ;
//    public float gravityX, gravityY, gravityZ;
//    public float linearAccelerationX, linearAccelerationY, linearAccelerationZ;
//    public float rotationVectorX, rotationVectorY, rotationVectorZ, rotationVectorCos, rotationVectorAccu;
//    public float proximity;
//    public float magneticFieldX, magneticFieldY, magneticFieldZ;
//    public float light, pressure, relativeHumidity, ambientTemperature;
    public float light;

    public Answer(long surveyPostTime, int interval) {
        this.surveyPostTime = surveyPostTime;
        this.interval = interval;
    }

    public void setContext(Context mContext) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        ContextManager contextManager = ContextManager.getInstance();

        // location
        setLocation(contextManager.locatoinLongtitude,
                contextManager.locatoinLatitude, contextManager.locatoinAccuracy);

//                    // status
        isCharging = batteryManager.isCharging();
        battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.isScreenOn = powerManager.isInteractive();
        this.isDeviceIdle = powerManager.isDeviceIdleMode();
        this.isPowerSave = powerManager.isPowerSaveMode();
        this.ringerMode = audioManager.getRingerMode();
        this.callState = telephonyManager.getCallState();
//                    // recent app
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
        this.recentApp = rappSB.toString();

        Network[] networks = connectivityManager.getAllNetworks();
        StringBuilder ntwSB = new StringBuilder();
        for (Network n : networks) {
            ntwSB.append(connectivityManager.getNetworkInfo(n));
        }
        this.network = ntwSB.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SignalStrength signalStrength = telephonyManager.getSignalStrength();
            try {
                List<CellSignalStrength> ss = signalStrength.getCellSignalStrengths();
                for (CellSignalStrength s : ss) {
                    this.signalType = s.toString();
                    this.signalDbm = s.getDbm();
                }
            } catch (NullPointerException e) {
                Log.e("signal strength", "getCellSignalStrength null pt", e);
            }
        } else {
            this.signalType = contextManager.phoneSignalType;
            this.signalDbm = contextManager.phoneSignalDbm;
        }

        // Sensors
        this.light = contextManager.light;
    }

    public Answer(String id, long surveyPostTime, long surveyFinishTime, int interval) {
        this.id = id;
        this.surveyPostTime = surveyPostTime;
        this.surveyFinishTime = surveyFinishTime;
        this.interval = interval;
    }

    public void setLocation(double longitude, double latitude, float locationAccuracy) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationAccuracy = locationAccuracy;
    }

    public void setStatus(boolean isCharging, int battery,
                          boolean isScreenOn, boolean isDeviceIdle, boolean isPowerSave,
                          int ringerMode, int callState,
                          String recentApp) {
        this.isCharging = isCharging;
        this.battery = battery;
        this.isScreenOn = isScreenOn;
        this.isDeviceIdle = isDeviceIdle;
        this.isPowerSave = isPowerSave;
        this.ringerMode = ringerMode;
        this.callState = callState;
        this.recentApp = recentApp;
    }

//    public void setSensor(float[] accelerometer,
//                          float[] gyroscope,
//                          float[] gravity,
//                          float[] linearAcceleration,
//                          float[] rotationVector,
//                          float proximity,
//                          float[] magneticField,
//                          float light, float pressure, float relativeHumidity, float ambientTemperature) {
//        this.accelerometerX = accelerometer[0];
//        this.accelerometerY = accelerometer[1];
//        this.accelerometerZ = accelerometer[2];
//        this.gyroscopeX = gyroscope[0];
//        this.gyroscopeY = gyroscope[1];
//        this.gyroscopeZ = gyroscope[2];
//        this.gravityX = gravity[0];
//        this.gravityY = gravity[1];
//        this.gravityZ = gravity[2];
//        this.linearAccelerationX = linearAcceleration[0];
//        this.linearAccelerationY = linearAcceleration[1];
//        this.linearAccelerationZ = linearAcceleration[2];
//        this.rotationVectorX = rotationVector[0];
//        this.rotationVectorY = rotationVector[1];
//        this.rotationVectorZ = rotationVector[2];
//        this.rotationVectorCos = rotationVector[3];
//        this.rotationVectorAccu = rotationVector[4];
//        this.proximity = proximity;
//        this.magneticFieldX = magneticField[0];
//        this.magneticFieldY = magneticField[1];
//        this.magneticFieldZ = magneticField[2];
//        this.light = light;
//        this.pressure = pressure;
//        this.relativeHumidity = relativeHumidity;
//        this.ambientTemperature = ambientTemperature;
//    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSurveyFinishTime(long t) {this.surveyFinishTime = t;}

    public void setInterval(int interval) {
        this.interval = interval;
    }


    public void setEsmQ1(String esm_q1) {
        this.esm_q1 = esm_q1;
    }

    public void setEsmQ2(String esm_q2) {
        this.esm_q2 = esm_q2;
    }

    public void setEsmQ3(String esm_q3) {
        this.esm_q3 = esm_q3;
    }

    public void setEsmQ4(String esm_q4) {
        this.esm_q4 = esm_q4;
    }

    public void setEsmQ5(String esm_q5) {
        this.esm_q5 = esm_q5;
    }

    public void setEsmQ6(String esm_q6) {
        this.esm_q6 = esm_q6;
    }

    public void setEsmQ7(String esm_q7) {
        this.esm_q7 = esm_q7;
    }

    // false for scale check fails
    public boolean answerHandler(ArrayList<NotiItem> notiItems, ArrayList<NotiItem> notiItemsD) {

        // scale check and set click_order / display_order
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
            if(item.checkScale()) {
                item.setClickOrder(i);
                itemD.setDisplayOrder(i);
            }
            else return false;
        }

        Collections.sort(notiItems);
        Collections.sort(notiItemsD);

        // fill display_order to first arraylist of notis
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
            if (itemD.isNotDisplay()) {
                item.setNotDisplay();
                item.setNotDisplayReason(itemD.not_display_dup, itemD.not_display_not_relate, itemD.not_display_other);
            } else {
                item.setDisplayOrder(itemD.getDisplayOrder());
            }
        }

        this.notifications = notiItems;

        return true;
    }


}
