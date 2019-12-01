package com.example.jefflin.notipreference.widgets;

import android.util.Log;

import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.TimerTask;

public class BlockTask extends TimerTask {
    @Override
    public void run() {
        Log.d("Block Task", "run");
        if(SurveyManager.getInstance().isSurveyDone())
            return;
        else if(SurveyManager.getInstance().isSurveyBlock()) {
            SurveyManager.getInstance().surveyUnblock();
        }
    }
}
