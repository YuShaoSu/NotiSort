package com.example.jefflin.notipreference.model;

public class SurveyInfo {
    public String deviceID;
    public int surveySendCount;
    public int surveyRespondedCount;
    public int surveyFinishedCount;
    public Long timestamp;

    public SurveyInfo(String id, int send, int response, int finish, Long timestamp) {
        this.deviceID = id;
        this.surveySendCount = send;
        this.surveyRespondedCount = response;
        this.surveyFinishedCount = finish;
        this.timestamp = timestamp;
    }

}
