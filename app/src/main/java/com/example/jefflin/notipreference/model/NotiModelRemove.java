package com.example.jefflin.notipreference.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "noti_model_remove")
public class NotiModelRemove {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "app_name")
    public String appName;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @NonNull
    @ColumnInfo(name = "post_time")
    public long postTime;

    @NonNull
    @ColumnInfo(name = "remove_time")
    public long removeTime;

    @NonNull
    @ColumnInfo(name = "reason")
    public int reason = -1;

    @NonNull
    @ColumnInfo(name = "did")
    public String deviceID;

    public NotiModelRemove(String appName, String title, String content, long postTime, long removeTime, int reason, String deviceID) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.removeTime = removeTime;
        this.reason = reason;
        this.deviceID = deviceID;
    }

}
