package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.jefflin.notipreference.model.SampleRecord;

import java.util.List;

@Dao
public interface SampleRecordDao {
    @Query("SELECT * FROM sample_record")
    List<SampleRecord> getAll();

    @Query("SELECT * FROM sample_record ORDER BY id DESC LIMIT 12")
    List<SampleRecord> getLast();

    @Query("DELETE FROM sample_record")
    void deleteAll();

    @Query("SELECT app_name FROM sample_record GROUP BY app_name ORDER BY count(*) DESC")
    List<String> getAppNameInOrder();

    @Query("SELECT count(*) FROM sample_record GROUP BY app_name ORDER BY count(*) DESC")
    List<Integer> getAppNameCountInOrder();

    @Insert
    void insert(SampleRecord... sampleRecord);

    @Insert
    void insertAll(List<SampleRecord> sampleRecords);

    @Delete
    void delete(SampleRecord... sampleRecords);
}
