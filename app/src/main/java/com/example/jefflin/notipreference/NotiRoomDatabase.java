package com.example.jefflin.notipreference;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NotiItem.class}, version = 1)
public abstract class NotiRoomDatabase extends RoomDatabase {

    public abstract NotiItem.NotiItemDao notiItemDao();

    private static volatile NotiRoomDatabase INSTANCE;

    static NotiRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (NotiRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotiRoomDatabase.class, "noti_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
