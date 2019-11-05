package com.example.jefflin.notipreference.manager;

import android.util.Log;

import com.example.jefflin.notipreference.NotiItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SurveyManager {
    private static volatile SurveyManager uniqueInstance;
    Map<String, ArrayList<NotiItem>> mMap = new HashMap<>();
    private boolean isSurveyDone;
    private boolean isSurveyBlock;
    private int interval;

    private SurveyManager() {
        setSurveyDone(false);
        setSurveyBlock(false);
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
    public void setSurveyDone(boolean surveyDone) { isSurveyDone = surveyDone; }

    public void setMap(Map<String, ArrayList<NotiItem>> map) {
        mMap = map;
    }
    public Map<String, ArrayList<NotiItem>> getMap() {
        return mMap;
    }

    public boolean isNotiNull() {
        return mMap.isEmpty();
    }

    public void surveyInit() {
        isSurveyBlock = false;
        isSurveyDone = false;
    }

    public void surveyDone() {
        Log.d("SuveyManager", "survey done");
        isSurveyDone = true;
        isSurveyBlock = false;
        mMap.clear();
    }
}
