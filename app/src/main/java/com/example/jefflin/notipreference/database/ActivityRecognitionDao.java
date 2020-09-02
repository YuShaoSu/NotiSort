package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.example.jefflin.notipreference.model.NotiModel;

import java.util.List;

@Dao
public interface ActivityRecognitionDao {

    @Insert
    void insertActivityRecognition(ActivityRecognitionModel activityRecognitionModel);

    @Query("SELECT * FROM activity_recognition")
    List<ActivityRecognitionModel> getAll();

    @Query("DELETE FROM activity_recognition")
    void deleteAll();

    @Query("SELECT * FROM activity_recognition ORDER BY id LIMIT :limit")
    List<ActivityRecognitionModel> getPartial(int limit);

    @Query("SELECT count(id) FROM activity_recognition")
    int getCount();

    @Query("SELECT id FROM activity_recognition ORDER BY id DESC LIMIT 1")
    int getLargestID();

    @Query("SELECT id FROM activity_recognition ORDER BY id LIMIT 1")
    int getSmallestID();

    @Delete
    void deleteARList(List<ActivityRecognitionModel> activityRecognitionModels);

}
