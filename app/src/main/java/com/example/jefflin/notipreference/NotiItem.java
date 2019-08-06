package com.example.jefflin.notipreference;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.util.Date;

/*
    A class that store notification object's attributes
 */
@Entity
public class NotiItem {
    public Drawable icon;

    @NonNull
    public String appName;
    @NonNull
    public String title;
    @NonNull
    public String content;
    @NonNull
    public Date postTime;
    @NonNull
    public String category;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public NotiItem(Drawable icon, String appName, String title, String content, Date postTime, String category) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }

    @Dao
    public interface NotiItemDao {

        @Insert
        void insert(NotiItem notiItem);



    }

}
