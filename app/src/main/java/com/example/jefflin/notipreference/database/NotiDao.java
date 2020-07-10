package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.model.NotiModel;

import java.util.List;

@Dao
public interface NotiDao {
    @Query("SELECT * FROM noti_model")
    List<NotiModel> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNoti(NotiModel notiModel);

    @Delete
    void deleteNoti(NotiModel... notiModels);

    @Query("DELETE FROM noti_model")
    void deleteAll();
}
