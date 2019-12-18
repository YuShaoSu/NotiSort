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
    public String icon;
    @Ignore
    public int factor0;
    @Ignore
    public int factor1;
    @Ignore
    public int factor2;
    @Ignore
    public int factor3;

    @ColumnInfo(name = "origin_order")
    public int origin_order;
    @Ignore
    private int click_order = -1;
    @Ignore
    private int display_order = -1;

    public NotiItem(String appName, String title, String content, Long postTime, String category, int origin_order) {
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
        this.sortReason = "";
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

    public void setIcon(String mIcon) {
        this.icon = mIcon;
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

    public void setSortReason(String reason) { this.sortReason = reason; }
}
