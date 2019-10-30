package com.example.jefflin.notipreference;

import com.google.gson.Gson;


import java.util.LinkedHashMap;

//Singleton SurveyAnswer ........

public class SurveyAnswer {
    private volatile static SurveyAnswer uniqueInstance;
    private final LinkedHashMap<String, String> answered_hashmap = new LinkedHashMap<>();

    private SurveyAnswer() {
    }

    public String getAnswer(Answer answer) {
        Gson gson = new Gson();
        return gson.toJson(answer);
    }

    public void put_answer(String key, String value) {
        answered_hashmap.put(key, value);
    }

    public String get_json_object() {
        Gson gson = new Gson();
        return gson.toJson(answered_hashmap, LinkedHashMap.class);
    }

    @Override
    public String toString() {
        return String.valueOf(answered_hashmap);
    }

    public static SurveyAnswer getInstance() {
        if (uniqueInstance == null) {
            synchronized (SurveyAnswer.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SurveyAnswer();
                }
            }
        }
        return uniqueInstance;
    }
}
