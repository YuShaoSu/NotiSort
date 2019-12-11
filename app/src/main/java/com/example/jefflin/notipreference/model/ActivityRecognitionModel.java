package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.jefflin.notipreference.GlobalClass;

@Entity(tableName = "activity_recognition")
public class ActivityRecognitionModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "activity_type")
    private int activityType;

    @ColumnInfo(name = "transition_type")
    private int transitionType;

//    @ColumnInfo(name = "event_string")
//    private String eventString;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "device_id")
    private String deviceID;

    public ActivityRecognitionModel() {}

    public ActivityRecognitionModel(int mA, int mT, long time) {
        this.activityType = mA;
        this.transitionType = mT;
//        this.eventString = event;
        this.timestamp = time;
        this.deviceID = GlobalClass.getDeviceID();
    }

    public int getId() { return id; }
    public int getActivityType() { return activityType; }
    public int getTransitionType() { return transitionType; }
//    public String getEventString() { return eventString; }
    public long getTimestamp() { return timestamp; }
    public String getDeviceID() { return deviceID; }

    public void setId(int id) { this.id = id; }
    public void setActivityType(int activityType) { this.activityType = activityType; }
    public void setTransitionType(int transitionType) { this.transitionType = transitionType; }
//    public void setEventString(String eventString) { this.eventString = eventString; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setDeviceID(String deviceID) { this.deviceID = deviceID; }
}
