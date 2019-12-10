package com.example.jefflin.notipreference.manager;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.jefflin.notipreference.model.NotiDao;
import com.example.jefflin.notipreference.model.NotiDatabase;

public class DatabaseManager {
    private NotiDao notiDao;
    private NotiDatabase db;

    public NotiDao getNotiDao() { return notiDao; }

    // database
    public DatabaseManager() {

    }
}
