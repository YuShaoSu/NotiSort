package com.example.jefflin.notipreference.manager;

import android.util.Log;

import com.example.jefflin.notipreference.model.Accessibility;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.NotiModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SurveyManager {
    private static volatile SurveyManager uniqueInstance;
    Map<String, ArrayList<NotiItem>> mMap = new HashMap<>();
    private final LinkedHashMap<String, String> answered_hashmap = new LinkedHashMap<>();
    private boolean isSurveyDone;
    private boolean isSurveyBlock;
    private int interval;
    private Long surveyPostTime;

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

    public long getSurveyPostTime() { return surveyPostTime; }

    public void setMap(Map<String, ArrayList<NotiItem>> map) {
        surveyPostTime = Calendar.getInstance().getTimeInMillis();
        mMap.clear();
        mMap = map;
    }

    public Map<String, ArrayList<NotiItem>> getMap() {
        return mMap;
    }

    public boolean isNotiNull() {
        return mMap.isEmpty();
    }

    public boolean twoListDiff(ArrayList<NotiItem> attend, ArrayList<NotiItem> display) {
        boolean diff = false;
        for (int i = 0; i < attend.size(); i++) {
            if (attend.get(i).origin_order != display.get(i).origin_order) {
                diff = true;
                break;
            }
        }
        return diff;
    }

    public void surveyBlock() {
        isSurveyBlock = !isNotiNull();
    }

    public void surveyUnblock() {
        isSurveyBlock = false;
    }

    public void surveyInit() {
        isSurveyBlock = false;
        isSurveyDone = false;
        mMap.clear();
    }

    public void surveyDone() {
        Log.d("SuveyManager", "survey done");
        isSurveyDone = true;
        isSurveyBlock = false;
        mMap.clear();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    // contextual data
    private double longtitude;
    private double latitude;
    private float accuracy;
    public void setCurrentLocation(double mLatitude, double mLongtitude, float mAccuracy) { longtitude = mLongtitude; latitude = mLatitude; accuracy = mAccuracy;}
    public double[] getLocation() { return new double[] { latitude, longtitude, accuracy }; }
    public double getLatitude() { return latitude; }
    public double getLongtitude() { return longtitude; }
    public float getAccuracy() { return accuracy; }

    private int ringerMode;
    public void setRingerMode(int ringerMode) { this.ringerMode = ringerMode; }
    public int getRingerMode() { return ringerMode; }

    // answer json
    private String postJson;

    public String getAnswer(Answer answer) {
        Gson gson = new Gson();
        postJson = gson.toJson(answer);
        return postJson;
    }

    public String get_json_object() {
        Gson gson = new Gson();
        return gson.toJson(answered_hashmap, LinkedHashMap.class);
    }

    public static String getItemJson(List<NotiModel> notiModels) {
        Gson gson = new Gson();
        return gson.toJson(notiModels);
    }

    public static String getARJson(List<ActivityRecognitionModel> activityRecognitionModels){
        Gson gson = new Gson();
        return gson.toJson(activityRecognitionModels);
    }

    public static String getACJson(List<Accessibility> accessibilities){
        Gson gson = new Gson();
        return gson.toJson(accessibilities);
    }

    public static String getLUJson(List<LocationUpdateModel> locationUpdateModels){
        Gson gson = new Gson();
        return gson.toJson(locationUpdateModels);
    }

    public String getPostJson() {
        return postJson;
    }

//    public NotiDao getNotiDao() { return notiDao; }


}
