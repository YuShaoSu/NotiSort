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

    @Query("SELECT * FROM noti_model ORDER BY id LIMIT :limit")
    List<NotiModel> getPartial(int limit);

    @Query("SELECT count(id) FROM noti_model")
    int getCount();

    @Query("SELECT id FROM noti_model ORDER BY id DESC LIMIT 1")
    int getLargestID();

    @Query("SELECT id FROM noti_model ORDER BY id LIMIT 1")
    int getSmallestID();

    @Delete
    void deleteNoti(NotiModel... notiModels);

    @Delete
    void deleteNotiList(List<NotiModel> notiModels);

    @Query("DELETE FROM noti_model")
    void deleteAll();
}
