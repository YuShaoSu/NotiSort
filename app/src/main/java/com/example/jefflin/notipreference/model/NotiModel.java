package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "noti_model")
public class NotiModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "app_name")
    public String appName;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "post_time")
    public Long postTime;

    @ColumnInfo(name = "category")
    public String category;


    // location
    @ColumnInfo
    public double longtitude;
    @ColumnInfo
    public double latitude;
    @ColumnInfo(name = "location_accuracy")
    public float locationAccuracy;

    // battery
    @ColumnInfo(name = "is_charging")
    public boolean isCharging;
    @ColumnInfo
    public int battery;

    // ringer
    @ColumnInfo(name = "ringer_tone")
    public int ringerTone;

    // screen & status
    @ColumnInfo(name = "is_screen_on")
    public boolean isScreenOn;
    @ColumnInfo(name = "is_device_idle")
    public boolean isDeviceIdle;
    @ColumnInfo(name = "is_power_save")
    public boolean isPowerSave;

    @ColumnInfo(name = "did")
    public String deviceID;

    // need migration
    public String network;
    public int callState;
    public String recentApp;
    public String signalType;
    public int signalDbm;

    @ColumnInfo(name = "is_ongoing", defaultValue = "0")
    public int isOngoing = -1;
    @ColumnInfo(name = "is_clearable", defaultValue = "0")
    public int isClearable;
    @ColumnInfo(name = "is_group", defaultValue = "0")
    public int isGroup;


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


    //
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


    public void setLocation(double longtitude, double latitude, float locationAccuracy) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.locationAccuracy = locationAccuracy;
    }

    public void setBattery(int battery, boolean charging) {
        this.battery = battery;
        isCharging = charging;
    }

    public void setRingerTone(int ringerTone) {
        this.ringerTone = ringerTone;
    }

    public void setScreenOn(boolean screenOn) {
        isScreenOn = screenOn;
    }

    public void setDeviceIdle(boolean deviceIdle) {
        isDeviceIdle = deviceIdle;
    }

    public void setPowerSave(boolean powerSave) {
        isPowerSave = powerSave;
    }


    public NotiModel() {}

    public NotiModel(String appName, String title, String content, Long postTime, String category, String deviceID, boolean isOngoing, boolean isClearable) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
        this.deviceID = deviceID;
        this.isOngoing = isOngoing ? 1 : 0;
        this.isClearable = isClearable ? 1 : 0;
    }

}
