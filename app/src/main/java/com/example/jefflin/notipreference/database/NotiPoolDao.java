package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.NotiPool;

import java.util.List;

@Dao
public interface NotiPoolDao {
    @Query("SELECT * FROM noti_pool")
    List<NotiPool> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotiPool(NotiPool notiPool);

    @Delete
    void deleteNotiPool(NotiPool... notiPools);

    @Query("DELETE FROM noti_pool")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<NotiPool> notiPools);

}
