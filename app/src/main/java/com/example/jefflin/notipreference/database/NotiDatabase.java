package com.example.jefflin.notipreference.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;

@Database(entities = {NotiItem.class, ActivityRecognitionModel.class}, version = 2)
public abstract class NotiDatabase extends RoomDatabase {
    private static volatile NotiDatabase uniqueInstance;
    public abstract NotiDao notiDao();
    public abstract ActivityRecognitionDao activityRecognitionDao();

    public static NotiDatabase getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (SurveyManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = Room.inMemoryDatabaseBuilder(context, NotiDatabase.class).
                            allowMainThreadQueries().addMigrations(MIGRATION1_2).build();
                }
            }
        }
        return uniqueInstance;
    }

    static final Migration MIGRATION1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `activity_recognition` (`id` INTEGER, "
                    + " `activity_type` INTEGER, `transition_type` INTEGER, "
                    + " `timestamp` LONG, PRIMARY KEY(`id`))");
        }
    };

}
