package com.example.jefflin.notipreference.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jefflin.notipreference.model.Accessibility;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;

@Database(entities = {NotiItem.class, ActivityRecognitionModel.class, LocationUpdateModel.class, Accessibility.class}, version = 4)
public abstract class NotiDatabase extends RoomDatabase {
    private static volatile NotiDatabase uniqueInstance;

    public abstract NotiDao notiDao();

    public abstract ActivityRecognitionDao activityRecognitionDao();

    public abstract LocationUpdateDao locationUpdateDao();

    public abstract AccessibilityDao accessibilityDao();

    public static NotiDatabase getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (SurveyManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = Room.inMemoryDatabaseBuilder(context, NotiDatabase.class).
                            addMigrations(MIGRATION1_2, MIGRATION2_3, MIGRATION3_4).build();
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

    static final Migration MIGRATION2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE activity_recognition "
                    + " ADD COLUMN device_id TEXT");
            database.execSQL("CREATE TABLE `location_update` (`id` INTEGER, "
                    + " `longitude` DOUBLE, `latitude` DOUBLE, "
                    + " `accuracy` FLOAT, `timestamp` LONG, "
                    + " `device_id` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `accessbility` (`id` INTEGER, "
                    + " `type` INTEGER, `pack` TEXT, "
                    + " `text` TEXT, `extra` TEXT, "
                    + " `timestamp` LONG, `device_id` TEXT, PRIMARY KEY(`id`))");
            database.execSQL("ALTER TABLE noti_item ADD longitude DOUBLE");
            database.execSQL("ALTER TABLE noti_item ADD latitude DOUBLE");
            database.execSQL("ALTER TABLE noti_item ADD location_accuracy FLOAT");
            database.execSQL("ALTER TABLE noti_item ADD is_charging BOOLEAN");
            database.execSQL("ALTER TABLE noti_item ADD battery INTEGER");
            database.execSQL("ALTER TABLE noti_item ADD ringer_tone INTEGER");
            database.execSQL("ALTER TABLE noti_item ADD is_screen_on BOOLEAN");
            database.execSQL("ALTER TABLE noti_item ADD is_device_idle BOOLEAN");
            database.execSQL("ALTER TABLE noti_item ADD is_power_save BOOLEAN");
        }
    };

}
