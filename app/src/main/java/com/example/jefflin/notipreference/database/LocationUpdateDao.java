package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.LocationUpdateModel;

import java.util.List;

@Dao
public interface LocationUpdateDao {
    @Query("SELECT * FROM location_update")
    List<LocationUpdateModel> getAll();

    @Insert
    void insert(LocationUpdateModel locationUpdateModel);

    @Delete
    void delete(LocationUpdateModel... locationUpdateModels);

    @Query("DELETE FROM location_update")
    void deleteAll();
}
