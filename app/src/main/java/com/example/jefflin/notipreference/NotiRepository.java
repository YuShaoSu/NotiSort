package com.example.jefflin.notipreference;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotiRepository {
    private NotiItem.NotiItemDao mNotiDao;
    private LiveData<List<NotiItem>> mAllNotis;

    NotiRepository(Application application) {
        NotiRoomDatabase db = NotiRoomDatabase.getDatabase(application);
        mNotiDao = db.notiItemDao();
        mAllNotis =  mNotiDao.getAllNoti();
    }

    LiveData<List<NotiItem>> getAllNotis() {
        return mAllNotis;
    }

    public void insert(NotiItem notiItem) {
        new insertAsyncTask(mNotiDao).execute(notiItem);
    }

    private static class insertAsyncTask extends AsyncTask<NotiItem, Void, Void> {
        private NotiItem.NotiItemDao mAsyncTaskDao;

        insertAsyncTask(NotiItem.NotiItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NotiItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }
}
