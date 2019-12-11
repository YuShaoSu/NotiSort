package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.jefflin.notipreference.GlobalClass;

@Entity(tableName = "location_update")
public class LocationUpdateModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "accuracy")
    private float accuracy;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "device_id")
    private String deviceID;

    public LocationUpdateModel(double longitude, double latitude, float accuracy,
                               long timestamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.deviceID = GlobalClass.getDeviceID();
    }

    public int getId() { return id; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public float getAccuracy() { return accuracy; }
    public long getTimestamp() { return timestamp; }
    public String getDeviceID() { return deviceID; }

    public void setId(int id) { this.id = id; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setAccuracy(float accuracy) { this.accuracy = accuracy; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setDeviceID(String deviceID) { this.deviceID = deviceID; }
}
