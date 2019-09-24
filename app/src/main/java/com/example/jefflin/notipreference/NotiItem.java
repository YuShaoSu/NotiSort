package com.example.jefflin.notipreference;

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

import java.util.Date;
import java.util.List;

/*
    A class that store notification object's attributes
 */
public class NotiItem {
    public String appName;
    public String title;
    public String content;
    public Long postTime;
    public String category;
    public Drawable icon;

    public int id;

    public NotiItem(Drawable icon, String appName, String title, String content, Long postTime, String category) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }


}
