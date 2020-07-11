package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "noti_pool")
public class NotiPool implements Comparable<NotiPool>{
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

    @ColumnInfo(name = "icon")
    public String icon;

    public NotiPool(String appName, String title, String content, Long postTime, String category) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }


    public void setIcon(String mIcon) {
        this.icon = mIcon;
    }


    @Override
    public int compareTo(NotiPool compare) {
        return (int) (this.postTime - compare.postTime);
    }

}
