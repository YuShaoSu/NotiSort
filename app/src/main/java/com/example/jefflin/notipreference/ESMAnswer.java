package com.example.jefflin.notipreference;

import com.google.gson.Gson;


import java.util.LinkedHashMap;

//Singleton ESMAnswer ........

public class ESMAnswer {
    private volatile static ESMAnswer uniqueInstance;
    private final LinkedHashMap<String, String> answered_hashmap = new LinkedHashMap<>();


    private ESMAnswer() {
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

    public static ESMAnswer getInstance() {
        if (uniqueInstance == null) {
            synchronized (ESMAnswer.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ESMAnswer();
                }
            }
        }
        return uniqueInstance;
    }
}
