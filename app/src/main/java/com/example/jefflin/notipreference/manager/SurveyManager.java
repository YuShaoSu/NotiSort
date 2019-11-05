package com.example.jefflin.notipreference.manager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jefflin.notipreference.NotiItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SurveyManager extends BroadcastReceiver {
    private static volatile SurveyManager uniqueInstance;
    Map<String, ArrayList<NotiItem>> mMap = new HashMap<>();
    private boolean isSurveyDone;
    private boolean isSurveyBlock;
    private int interval;

    private SurveyManager() {
        setSurveyDone(false);
        setSurveyBlock(false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("","broadcast in surveyManager");
        setSurveyBlock(false);
        setSurveyDone(false);
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

    public boolean isSurveyBlock() { return isSurveyBlock; }
    public void setSurveyBlock(boolean surveyBlock) { isSurveyBlock = surveyBlock; }

    public boolean isSurveyDone() { return isSurveyDone; }
    public void setSurveyDone(boolean surveyDone) {
        isSurveyDone = surveyDone;
        if(surveyDone)  mMap.clear();
    }

    public void setMap(Map<String, ArrayList<NotiItem>> map) {
        mMap = map;
    }
    public Map<String, ArrayList<NotiItem>> getMap() {
        return mMap;
    }

    public boolean isNotiNull() {
        return mMap.isEmpty();
    }
}
