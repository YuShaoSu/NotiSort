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
public class NotiItem implements Serializable {
    public String appName;
    public String title;
    public String content;
    public Long postTime;
    public String category;
    public byte[] icon;
    public int icon_height;
    public int icon_width;

    public int factor0;
    public int factor1;
    public int factor2;
    public int factor3;

    public int id;
//Drawable icon,
    public NotiItem(byte[] icon, String appName, String title, String content, Long postTime, String category, int icon_height, int icon_width) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
        this.icon_height = icon_height;
        this.icon_width = icon_width;
        this.factor0 = -1;
        this.factor1 = -1;
        this.factor2 = -1;
        this.factor3 = -1;
    }


}
