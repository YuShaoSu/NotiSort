package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sample_record")
public class SampleRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "post_time")
    private long timestamp;

    @ColumnInfo(name = "app_name")
    private String appName;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    public SampleRecord(String appName, String title, String content, long timestamp) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAppName() {
        return appName;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
