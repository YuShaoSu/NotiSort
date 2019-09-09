package com.example.jefflin.notipreference;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NotiItem.class}, version = 1)
public abstract class NotiRoomDatabase extends RoomDatabase {

    public abstract NotiItem.NotiItemDao notiItemDao();

    private static volatile NotiRoomDatabase INSTANCE;

    static NotiRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (NotiRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotiRoomDatabase.class, "noti_database")
//                            .addMigrations(MIGRATION_1_2)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE noti_table.title STRING NULL) ");
//            database.execSQL("ALTER TABLE noti_table ALTER COLUMN (content STRING NULL) ");
//            database.execSQL("ALTER TABLE noti_table ALTER COLUMN (category STRING NULL) ");
//        }
//    };


    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }
        };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NotiItem.NotiItemDao mDao;

        PopulateDbAsync(NotiRoomDatabase db){
            mDao = db.notiItemDao();
        }

        @Override
        protected Void doInBackground(final Void... params){
            mDao.deleteAll();
            NotiItem notiItem = new NotiItem("Test", "Test", "Test", Long.valueOf(1000), "Test");
            mDao.insert(notiItem);
            Log.d("DB on create", " doInBackground function");
            return null;
        }
    }
}
