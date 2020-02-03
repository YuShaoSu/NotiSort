package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.jefflin.notipreference.GlobalClass;

import java.util.Calendar;

@Entity(tableName = "accessibility")
public class Accessibility {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "type")
    public int accessibilityType;

    @ColumnInfo(name = "pack")
    public String accessibilityPack;

    @ColumnInfo(name = "text")
    public String accessibilityText;

    @ColumnInfo(name = "extra")
    public String accessibilityExtra;

    @ColumnInfo
    public Long timestamp;

    @ColumnInfo(name = "did")
    public String deviceID;

    public Accessibility(){}

    public Accessibility(int type, String pack, String text, String Extra){
        this.accessibilityExtra = Extra;
        this.accessibilityType = type;
        this.accessibilityPack = pack;
        this.accessibilityText = text;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.deviceID = GlobalClass.getDeviceID();
    }

}
