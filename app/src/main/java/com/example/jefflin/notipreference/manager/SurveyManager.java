package com.example.jefflin.notipreference.manager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jefflin.notipreference.NotiItem;

import java.util.ArrayList;
import java.util.Map;

public class SurveyManager extends BroadcastReceiver {
    private static volatile SurveyManager uniqueInstance;
    Map<String, ArrayList<NotiItem>> mMap;
    private boolean isSurveDone;
    private boolean surveBlock;

    private SurveyManager() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("","broadcast in surveyManager");
    }

    public static SurveyManager getInstance() {
        if (uniqueInstance == null) {
            synchronized (SurveyManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SurveyManager();
                }
            }
        }
        return uniqueInstance;
    }

    public void setMap(Map<String, ArrayList<NotiItem>> map) {
        mMap = map;
    }

    public Map<String, ArrayList<NotiItem>> getMap() {
        return mMap;
    }
}
