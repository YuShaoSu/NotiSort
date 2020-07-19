package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.jefflin.notipreference.model.NotiPool;

import java.util.List;

@Dao
public abstract class NotiPoolDao {
    @Query("SELECT * FROM noti_pool")
    public abstract List<NotiPool> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertNotiPool(NotiPool notiPool);

    @Delete
    public abstract void deleteNotiPool(NotiPool... notiPools);

    @Transaction
    @Query("DELETE FROM noti_pool")
    public abstract void deleteAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<NotiPool> notiPools);

    @Transaction
    public void updateWhole(List<NotiPool> notiPools) {
        deleteAll();
        insertAll(notiPools);
    }

}
