package com.example.jefflin.notipreference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jefflin.notipreference.manager.SurveyManager;

public class NotificationDisMissReceiver extends BroadcastReceiver {

    public NotificationDisMissReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("survey noti", "dismiss receive");
//        SurveyManager.getInstance().surveyUnblock();
    }
}
