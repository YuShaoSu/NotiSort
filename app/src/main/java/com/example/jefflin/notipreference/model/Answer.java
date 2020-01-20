package com.example.jefflin.notipreference.model;

import java.util.ArrayList;
import java.util.Collections;

public class Answer {

    private String id;
    private long surveyPostTime;
    private long surveyFinishTime;
    private int interval;
    private ArrayList<NotiItem> notifications;
    private String esm_q1;
    private String esm_q2;
    private String esm_q3;
    private String esm_q4;
    private String esm_q5;
    private String esm_q6;
    private double latitude;
    private double longtitude;
    public float locationAccuracy;
    public boolean isCharging;
    public int battery;
    public boolean isScreenOn;
    public boolean isDeviceIdle;
    public boolean isPowerSave;
    private int ringerMode;

    public Answer(String id, long surveyPostTime, long surveyFinishTime, int interval) {
        this.id = id;
        this.surveyPostTime = surveyPostTime;
        this.surveyFinishTime = surveyFinishTime;
        this.interval = interval;
    }

    public void setContext(double[] location, float accuracy, int mRingerMode) {
        latitude = location[0];
        longtitude = location[1];
        locationAccuracy = accuracy;
        ringerMode = mRingerMode;
    }

    public void setBattery(int mBattery, boolean is_charging){
        battery = mBattery;
        isCharging = is_charging;
    }

    public void setStatus(boolean screen, boolean idle, boolean powerSave){
        isScreenOn = screen;
        isDeviceIdle = idle;
        isPowerSave = powerSave;
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setNotifications(ArrayList<NotiItem> notifications) {
        this.notifications = notifications;
    }

    public void setEsmQ1(String esm_q1) {
        this.esm_q1 = esm_q1;
    }
    public void setEsmQ2(String esm_q2) { this.esm_q2 = esm_q2; }
    public void setEsmQ3(String esm_q3) { this.esm_q3 = esm_q3; }
    public void setEsmQ4(String esm_q4) { this.esm_q4 = esm_q4; }
    public void setEsmQ5(String esm_q5) { this.esm_q5 = esm_q5; }
    public void setEsmQ6(String esm_q6) { this.esm_q6 = esm_q6; }

    // false for scale check fails
    public boolean answerHandler(ArrayList<NotiItem> notiItems, ArrayList<NotiItem> notiItemsD) {

        // scale check and set click_order / display_order
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
//            if(item.checkScale()) {
                item.setClickOrder(i);
                itemD.setDisplayOrder(i);
//            }
//            else return false;
        }

        Collections.sort(notiItems);
        Collections.sort(notiItemsD);

        // fill display_order to first arraylist of notis
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
            item.setDisplayOrder(itemD.getDisplayOrder());
        }

        this.notifications = notiItems;

        return true;
    }


}
