package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/*
    A class that store notification object's attributes
*/
@Entity(tableName = "noti_items")
public class NotiItem implements Serializable, Comparable<NotiItem> {
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


    @Ignore
    public String sortReason;

    @Ignore
    public transient String icon;
    @Ignore
    public int sender_attractiveness;
    @Ignore
    public int content_attractiveness;
    @Ignore
    public int importance;
    @Ignore
    public int urgency;

    @Ignore
    @ColumnInfo(name = "origin_order")
    public int origin_order;
    @Ignore
    private int click_order = -1;
    @Ignore
    private int display_order = -1;

    public int not_attend_no_info = -1;
    public int not_attend_no_use = -1;
    public int not_attend_other = -1;

    public int not_display_dup = -1;
    public int not_display_not_relate = -1;
    public int not_display_other = -1;

    public NotiItem() {
    }

    public NotiItem(String appName, String title, String content, Long postTime, String category, int origin_order) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
        this.sender_attractiveness = -1;
        this.content_attractiveness = -1;
        this.importance = -1;
        this.urgency = -1;
        this.origin_order = origin_order;
        this.sortReason = "";
    }

    @Override
    public int compareTo(NotiItem compare) {
        int compare_order = compare.getOriginOrder();
        return this.origin_order - compare_order;
    }

    // false for not set correctly
    public boolean checkScale() {
        return (sender_attractiveness != -1 && content_attractiveness != -1 && importance != -1 && urgency != -1);
    }

    public void setIcon(String mIcon) {
        this.icon = mIcon;
    }

    public int setFactor(int qN, int value) {
        switch (qN) {
            case 0:
                this.sender_attractiveness = value;
                break;
            case 1:
                this.content_attractiveness = value;
                break;
            case 2:
                this.importance = value;
                break;
            case 3:
                this.urgency = value;
                break;
            default:
                return -1;
        }
        return value;
    }


    public void setNotClick() {
        this.click_order = this.isNotClick() ? -1 : -9999;
    }

    public void setNotDisplay() {
        this.display_order = this.isNotDisplay() ? -1 : -9999;
    }

    public void setNotDisplayReason(int dup, int relate, int other) {
        this.not_display_dup = dup;
        this.not_display_not_relate = relate;
        this.not_display_other = other;
    }

    public int getClickOrder() {
        return click_order;
    }

    public void setClickOrder(int click_order) {
        if(!this.isNotClick())   this.click_order = click_order;
    }


    public int getDisplayOrder() {
        return display_order;
    }

    public void setDisplayOrder(int display_order) {
        if(!this.isNotDisplay())    this.display_order = display_order;
    }

    public int getOriginOrder() {
        return origin_order;
    }

    public void setSortReason(String reason) {
        this.sortReason = reason;
    }

    public boolean isNotClick() { return click_order == -9999; }
    public boolean isNotDisplay() { return display_order == -9999; }

    //
    // location
//    @ColumnInfo
//    public double longtitude;
//    @ColumnInfo
//    public double latitude;
//    @ColumnInfo(name = "location_accuracy")
//    public float locationAccuracy;
//
//    // battery
//    @ColumnInfo(name = "is_charging")
//    public boolean isCharging;
//    @ColumnInfo
//    public int battery;
//
//    // ringer
//    @ColumnInfo(name = "ringer_tone")
//    public int ringerTone;
//
//    // screen & status
//    @ColumnInfo(name = "is_screen_on")
//    public boolean isScreenOn;
//    @ColumnInfo(name = "is_device_idle")
//    public boolean isDeviceIdle;
//    @ColumnInfo(name = "is_power_save")
//    public boolean isPowerSave;
//
//
//    //
//
//    public void setLocation(double longtitude, double latitude, float locationAccuracy){
//        this.longtitude = longtitude;
//        this.latitude = latitude;
//        this.locationAccuracy = locationAccuracy;
//    }
//
//    public void setBattery(int battery, boolean charging){
//        this.battery = battery;
//        isCharging = charging;
//    }
//
//    public void setRingerTone(int ringerTone) { this.ringerTone = ringerTone; }
//
//    public void setScreenOn(boolean screenOn) { isScreenOn = screenOn; }
//    public void setDeviceIdle(boolean deviceIdle) { isDeviceIdle = deviceIdle; }
//    public void setPowerSave(boolean powerSave) { isPowerSave = powerSave; }

}
