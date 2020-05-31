package com.example.jefflin.notipreference.receiver;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jefflin.notipreference.database.ActivityRecognitionDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ActivityRecognitionReceiver extends BroadcastReceiver {

    public ActivityRecognitionReceiver() {
    }

//    Executor mExecutor = Executors.newSingleThreadExecutor();
    Executor mExecutor = Executors.newCachedThreadPool();


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                Log.d("ActivityTransitionEvent", event.toString());
                SharedPreferences sharedPreferences =  context.getSharedPreferences("USER", Context.MODE_PRIVATE);
                final ActivityRecognitionModel activityRecognitionModel = new ActivityRecognitionModel(
                        event.getActivityType(), event.getTransitionType(),
                        System.currentTimeMillis(), sharedPreferences.getString("ID", "user id fail"));
                final ActivityRecognitionDao mDao = NotiDatabase.getInstance(context).activityRecognitionDao();
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mDao.insertActivityRecognition(activityRecognitionModel);
                    }
                });
            }
        }
    }
}
