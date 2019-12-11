package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.ActivityRecognitionModel;

import java.util.List;

@Dao
public interface ActivityRecognitionDao {

    @Insert
    void insertActivityRecognition(ActivityRecognitionModel activityRecognitionModel);

    @Query("SELECT * FROM activity_recognition")
    List<ActivityRecognitionModel> getAll();

    @Query("DELETE FROM activity_recognition")
    void deleteAll();

}
