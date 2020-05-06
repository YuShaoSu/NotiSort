package com.example.jefflin.notipreference.manager;

import android.util.Log;

import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.example.jefflin.notipreference.model.AnswerJson;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.NotiModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyManager {
    private static volatile SurveyManager uniqueInstance;
    ArrayList<Answer> answerArrayList = new ArrayList<>();
    Map<String, ArrayList<NotiItem>> mMap = new HashMap<>();
    private static boolean isSurveyDone = false;
    private static boolean isSurveyBlock = false;
    private static boolean isSurveyDoing = false;
    private static boolean dontDisturb = false;
    private  int interval;
    private  Long surveyPostTime;

    public boolean isDontDisturb() {
        return dontDisturb;
    }

    public void setDontDisturb(boolean dontDisturb) {
        this.dontDisturb = dontDisturb;
    }

    public void pushAnswerList(Answer answer) {
        answerArrayList.add(answer);
    }

    public Answer getFromAnswerList(int i) {
        if(i >= answerArrayList.size()) i = answerArrayList.size() - 1;
        return answerArrayList.get(i);
    }

    public void clearAnswerList() {
        answerArrayList.clear();
    }

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

    public boolean isSurveyBlock() {
        return isSurveyBlock;
    }

    public void setSurveyBlock(boolean surveyBlock) {
        isSurveyBlock = surveyBlock;
    }

    public boolean isSurveyDone() {
        return isSurveyDone;
    }

    public void setSurveyDone(boolean surveyDone) {
        isSurveyDone = surveyDone;
    }

    public boolean isSurveyDoing() {
        return isSurveyDoing;
    }

    public void setSurveyDoing(boolean surveyDoing) {
        isSurveyDoing = surveyDoing;
    }

    public long getSurveyPostTime() {
        return surveyPostTime;
    }

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

    public void surveyBlock() {
        isSurveyBlock = !isNotiNull();
    }

    public void surveyUnblock() {
        clearAnswerList();
        mMap.clear();
        isSurveyBlock = false;
        isSurveyDoing = false;
    }

    public void surveyInit() {
//        isSurveyBlock = false;
//        isSurveyDone = false;
//        isSurveyDoing = false;
        clearAnswerList();
        mMap.clear();
    }

    public void surveyDone() {
        Log.d("SuveyManager", "survey done");
//        isSurveyDone = true;
//        isSurveyBlock = false;
//        isSurveyDoing = false;
        clearAnswerList();
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

    public void setCurrentLocation(double mLatitude, double mLongtitude, float mAccuracy) {
        longtitude = mLongtitude;
        latitude = mLatitude;
        accuracy = mAccuracy;
    }

    public double[] getLocation() {
        return new double[]{latitude, longtitude, accuracy};
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    private int ringerMode;

    public void setRingerMode(int ringerMode) {
        this.ringerMode = ringerMode;
    }

    public int getRingerMode() {
        return ringerMode;
    }

    // answer json
    private String postJson;

    public void setPostJson(Answer answer) {
        Gson gson = new Gson();
        this.postJson = gson.toJson(answer);
    }

    public String getAnswer(Answer answer) {
        Gson gson = new Gson();
        postJson = gson.toJson(answer);
        return postJson;
    }

    public static String getAnswerJson(List<AnswerJson> answerJsons, String now) {
        Gson gson = new Gson();
        List<Answer> answer = new ArrayList<>();
        if (!answerJsons.isEmpty()) {
            for (AnswerJson a : answerJsons) {
                answer.add(gson.fromJson(a.getJsonString(), Answer.class));
            }
        }
        if (!now.equals("")) answer.add(gson.fromJson(now, Answer.class));
        return gson.toJson(answer);
    }

    public static String getItemJson(List<NotiModel> notiModels) {
        Gson gson = new Gson();
        return gson.toJson(notiModels);
    }

    public static String getARJson(List<ActivityRecognitionModel> activityRecognitionModels) {
        Gson gson = new Gson();
        return gson.toJson(activityRecognitionModels);
    }

    public String getPostJson() {
        return postJson;
    }


}
