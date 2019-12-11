package com.example.jefflin.notipreference.manager;

import com.example.jefflin.notipreference.database.NotiDao;
import com.example.jefflin.notipreference.database.NotiDatabase;

public class DatabaseManager {
    private NotiDao notiDao;
    private NotiDatabase db;

    public NotiDao getNotiDao() { return notiDao; }

    // database
    public DatabaseManager() {

    }
}
