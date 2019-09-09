package com.example.jefflin.notipreference;

import android.graphics.drawable.Drawable;

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
@Entity(tableName = "noti_table")
public class NotiItem {
    @NonNull
    public String appName;
    @NonNull
    public String title;
    @NonNull
    public String content;
    @NonNull
    public Long postTime;
    @NonNull

    public String category;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public NotiItem(String appName, String title, String content, Long postTime, String category) {
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }

    @Dao
    public interface NotiItemDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(NotiItem notiItem);

        @Query("DELETE FROM noti_table")
        void deleteAll();


        @Query("SELECT * FROM noti_table")
        LiveData<List<NotiItem>> getAllNoti();

    }

}
