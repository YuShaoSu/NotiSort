package com.example.jefflin.notipreference.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.jefflin.notipreference.widgets.DateConverter;

import java.util.Date;

@Entity(tableName = "log")
@TypeConverters(DateConverter.class)
public class LogModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters(DateConverter.class)
    public Date timestamp;

    public String event;

    public LogModel(Date date, String log) {
        this.timestamp = date;
        this.event = log;
    }

    public LogModel(){}
}
