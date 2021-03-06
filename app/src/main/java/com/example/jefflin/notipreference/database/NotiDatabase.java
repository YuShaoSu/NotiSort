package com.example.jefflin.notipreference.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jefflin.notipreference.model.Accessibility;
import com.example.jefflin.notipreference.model.AnswerJson;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.example.jefflin.notipreference.model.LogModel;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.example.jefflin.notipreference.model.NotiModel;
import com.example.jefflin.notipreference.model.NotiModelRemove;
import com.example.jefflin.notipreference.model.NotiPool;
import com.example.jefflin.notipreference.model.SampleCombination;
import com.example.jefflin.notipreference.model.SampleRecord;

@Database(entities = {NotiModel.class, NotiItem.class, ActivityRecognitionModel.class, LocationUpdateModel.class,
        Accessibility.class, AnswerJson.class, SampleRecord.class, SampleCombination.class,
        NotiModelRemove.class, NotiPool.class, LogModel.class}, version = 13)
public abstract class NotiDatabase extends RoomDatabase {
    private static volatile NotiDatabase uniqueInstance;

    public abstract NotiDao notiDao();
    public abstract ActivityRecognitionDao activityRecognitionDao();
    public abstract AccessibilityDao accessibilityDao();
    public abstract AnswerJsonDao answerJsonDao();
    public abstract SampleRecordDao sampleRecordDao();
    public abstract SampleCombinationDao sampleCombinationDao();
    public abstract NotiModelRemoveDao notiModelRemoveDao();
    public abstract NotiPoolDao notiPoolDao();
    public abstract LogModelDao logModelDao();


    public static NotiDatabase getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (SurveyManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = Room.databaseBuilder(context, NotiDatabase.class, "NotiSort.db").
                            addMigrations(MIGRATION1_2, MIGRATION2_3, MIGRATION3_4, MIGRATION4_5,
                                    MIGRATION5_6, MIGRATION6_7, MIGRATION7_8, MIGRATION8_9, MIGRATION9_10,
                                    MIGRATION10_11, MIGRATION11_12, MIGRATION12_13).build();
                }
            }
        }
        return uniqueInstance;
    }

    private static final Migration MIGRATION1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `activity_recognition` (`id` INTEGER, "
                    + " `activity_type` INTEGER, `transition_type` INTEGER, "
                    + " `timestamp` LONG, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION2_3 = new Migration(2, 3) {
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

    private static final Migration MIGRATION3_4 = new Migration(3, 4) {
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

    private static final Migration MIGRATION4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `noti_model` (`id` INTEGER, `app_name` TEXT, "
                    + "`title` TEXT, `content` TEXT, `post_time` LONG, `category` TEXT, "
                    + "`longtitude` DOUBLE, `latitude` DOUBLE, `location_accuracy` FLOAT, "
                    + "`is_charging` BOOLEAN, `battery` INTEGER, `ringer_tone` INTEGER, "
                    + "`is_screen_on` BOOLEAN, `is_device_idle` BOOLEAN, `is_power_save` BOOLEAN)");
        }
    };

    private static final Migration MIGRATION5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE `noti_items`");
            database.execSQL("ALTER TABLE noti_model ADD did TEXT");
            database.execSQL("ALTER TABLE accessbility ADD did TEXT");
            database.execSQL("ALTER TABLE activity_recognition ADD did TEXT");
        }
    };

    private static final Migration MIGRATION6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `answer` (`id` INTEGER, `json_string` TEXT, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `sample_record` (`id` INTEGER, `app_name` TEXT, `title` TEXT, `content` TEXT, " +
                    " `post_time` LONG, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `sample_combination` (`id` INTEGER NOT NULL, `app_name_comb` TEXT unique NOT NULL, " +
                    "`count` INTEGER NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE UNIQUE INDEX index_sample_combination_app_name_comb ON sample_combination(app_name_comb)");
        }
    };

    private static final Migration MIGRATION9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `noti_model_remove` (`id` INTEGER NOT NULL, `app_name` TEXT, " +
                    "`title` TEXT, `content` TEXT, `post_time` INTEGER NOT NULL, `remove_time` INTEGER NOT NULL, " +
                    "`reason` INTEGER NOT NULL, `did` TEXT NOT NULL, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `noti_pool` (`id` INTEGER NOT NULL, `app_name` TEXT, " +
                    "`title` TEXT, `content` TEXT, `post_time` INTEGER, " +
                    "`category` TEXT, `icon` TEXT, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `log` (`id` INTEGER NOT NULL, " +
                    "`event` TEXT, `timestamp` INTEGER, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION12_13 = new Migration(12, 13) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `noti_model` ADD is_ongoing INTEGER not null DEFAULT 0");
            database.execSQL("ALTER TABLE `noti_model` ADD is_clearable INTEGER not null DEFAULT 0");
            database.execSQL("ALTER TABLE `noti_model` ADD is_group INTEGER not null DEFAULT 0");
        }
    };

}
