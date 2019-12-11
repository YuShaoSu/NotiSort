package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.NotiItem;

import java.util.List;

@Dao
public interface NotiDao {
    @Query("SELECT * FROM noti_items")
    List<NotiItem> getAll();

    @Insert
    void insertNoti(NotiItem notiItem);

    @Delete
    void deleteNoti(NotiItem... notiItems);

    @Query("DELETE FROM noti_items")
    void deleteAll();
}
