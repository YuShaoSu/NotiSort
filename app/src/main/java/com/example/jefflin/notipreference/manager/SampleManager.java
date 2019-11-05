package com.example.jefflin.notipreference.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SampleManager extends BroadcastReceiver {

    public SampleManager() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SampleManager", "broadcast received");
        Log.d("SampleManager", String.valueOf(intent.getExtras().get("interval")));
//        if(intent.getExtras().get("schedule").equals("next_interval"))  SurveyManager.getInstance().surveyInit();
//        else Log.d("SampleManager", "not me!");
        SurveyManager.getInstance().surveyInit();
    }

    public void surveyDone() {
        SurveyManager.getInstance().surveyDone();
    }

}
