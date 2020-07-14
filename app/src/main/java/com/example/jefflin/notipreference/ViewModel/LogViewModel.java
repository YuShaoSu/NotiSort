package com.example.jefflin.notipreference.ViewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.LogModel;

import java.util.List;

public class LogViewModel extends AndroidViewModel {
    private final LiveData<List<LogModel>> logList;

    private NotiDatabase appDatabase;

    public LogViewModel(Application application) {
        super(application);

        appDatabase = NotiDatabase.getInstance(application);

        logList = appDatabase.logModelDao().getAll();
    }


    public LiveData<List<LogModel>> getLogList() {
        return logList;
    }

    public void deleteItem() {
        new deleteAsyncTask(appDatabase).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<LogModel, Void, Void> {

        private NotiDatabase db;

        deleteAsyncTask(NotiDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final LogModel... params) {
            db.logModelDao().deleteAll();
            return null;
        }

    }

}
