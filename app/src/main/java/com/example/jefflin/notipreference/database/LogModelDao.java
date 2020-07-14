package com.example.jefflin.notipreference.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.LogModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LogModelDao {
    @Query("SELECT * from log")
    LiveData<List<LogModel>> getAll();

    @Query("select * from log where id = :id")
    LogModel getItembyId(String id);

    @Insert(onConflict = REPLACE)
    void insert(LogModel logModel);

    @Query("DELETE FROM log")
    void deleteAll();

    @Delete
    void delete(LogModel logModel);
}
