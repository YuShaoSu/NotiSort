package com.example.jefflin.notipreference.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Database(entities = {NotiItem.class}, version = 1)
public abstract class NotiDatabase extends RoomDatabase {
    private static volatile NotiDatabase uniqueInstance;
    public abstract NotiDao notiDao();

    public static NotiDatabase getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (SurveyManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = Room.inMemoryDatabaseBuilder(context, NotiDatabase.class).allowMainThreadQueries().build();
                }
            }
        }
        return uniqueInstance;
    }

}
