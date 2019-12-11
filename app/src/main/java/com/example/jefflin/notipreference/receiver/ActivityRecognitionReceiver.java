package com.example.jefflin.notipreference.receiver;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jefflin.notipreference.database.ActivityRecognitionDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

public class ActivityRecognitionReceiver extends BroadcastReceiver {

    public ActivityRecognitionReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                Log.d("ActivityTransitionEvent", event.toString());
                ActivityRecognitionModel activityRecognitionModel = new ActivityRecognitionModel(
                        event.getActivityType(), event.getTransitionType(),
                        System.currentTimeMillis());
                ActivityRecognitionDao mDao = NotiDatabase.getInstance(context).activityRecognitionDao();
                mDao.insertActivityRecognition(activityRecognitionModel);
            }
        }
    }
}
