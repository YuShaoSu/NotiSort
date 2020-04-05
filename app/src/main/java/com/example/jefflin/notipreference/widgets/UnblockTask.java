package com.example.jefflin.notipreference.widgets;

import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.TimerTask;

public class UnblockTask extends TimerTask {

    @Override
    public void run() {
        SurveyManager.getInstance().surveyUnblock();
    }

}
