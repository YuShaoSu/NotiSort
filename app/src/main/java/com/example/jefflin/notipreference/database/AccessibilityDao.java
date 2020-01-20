package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.Accessibility;

import java.util.List;

@Dao
public interface AccessibilityDao {
    @Query("SELECT * FROM accessibility")
    List<Accessibility> getAll();

    @Insert
    void insert(Accessibility locationUpdateModel);

    @Delete
    void delete(Accessibility... locationUpdateModels);

    @Query("DELETE FROM accessibility")
    void deleteAll();
}
