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

    //

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


    public NotiModel(String appName, String title, String content, Long postTime, String category) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }

}
