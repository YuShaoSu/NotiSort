package com.example.jefflin.notipreference;

import com.google.gson.Gson;


import java.util.LinkedHashMap;

//Singleton ESMAnswers ........

public class ESMAnswers {
    private volatile static ESMAnswers uniqueInstance;
    private final LinkedHashMap<String, String> answered_hashmap = new LinkedHashMap<>();


    private ESMAnswers() {
    }

    public void put_answer(String key, String value) {
        answered_hashmap.put(key, value);
    }

    public String get_json_object() {
        Gson gson = new Gson();
        return gson.toJson(answered_hashmap,LinkedHashMap.class);
    }

    @Override
    public String toString() {
        return String.valueOf(answered_hashmap);
    }

    public static ESMAnswers getInstance() {
        if (uniqueInstance == null) {
            synchronized (ESMAnswers.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ESMAnswers();
                }
            }
        }
        return uniqueInstance;
    }
}
