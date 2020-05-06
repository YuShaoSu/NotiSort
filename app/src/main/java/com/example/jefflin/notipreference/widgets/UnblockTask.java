package com.example.jefflin.notipreference.widgets;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.TimerTask;

public class UnblockTask extends TimerTask {

    private SharedPreferences sharedPreferences;

    public UnblockTask(Context context){
        sharedPreferences = context.getSharedPreferences("survey", Context.MODE_PRIVATE);
    }

    @Override
    public void run() {
        sharedPreferences.edit().putBoolean("block", false)
                            .putBoolean("doing", false)
                            .apply();
    }

}
