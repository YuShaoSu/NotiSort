package com.example.jefflin.notipreference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
    A class that store notification object's attributes
 */
public class NotiItem implements Serializable, Comparable<NotiItem> {
    public String appName;
    public String title;
    public String content;
    public Long postTime;
    public String category;
    public String icon;

    public int factor0;
    public int factor1;
    public int factor2;
    public int factor3;

    private int origin_order;
    private int click_order = -1;
    private int display_order = -1;

    public NotiItem(String icon, String appName, String title, String content, Long postTime, String category, int origin_order) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
        this.factor0 = -1;
        this.factor1 = -1;
        this.factor2 = -1;
        this.factor3 = -1;
        this.origin_order = origin_order;
    }

    @Override
    public int compareTo(NotiItem compare) {
        int compare_order = compare.getOriginOrder();
        return this.origin_order - compare_order;
    }

    // false for not set correctly
    public boolean checkScale() {
        return (factor0 != -1 && factor1 != -1 && factor2 != -1 && factor3 != -1);
    }

    public int setFactor(int qN, int value) {
        switch(qN) {
            case 0: this.factor0 = value; break;
            case 1: this.factor1 = value; break;
            case 2: this.factor2 = value; break;
            case 3: this.factor3 = value; break;
            default: return -1;
        }
        return value;
    }

    public void setClickOrder(int click_order) {
        this.click_order = click_order;
    }

    public int getClickOrder() {
        return click_order;
    }

    public int getDisplayOrder() {
        return display_order;
    }

    public void setDisplayOrder(int display_order) {
        this.display_order = display_order;
    }

    public int getOriginOrder() {
        return origin_order;
    }
}
