package com.example.jefflin.notipreference.widgets;

import android.content.Context;

import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.Answer;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SampleTask extends TimerTask {

    private Context context;
    private int min;
    private int i;

    public SampleTask(Context context, int min, int i) {
        this.context = context;
        this.min = min;
        this.i = i;
    }

    @Override
    public void run() {
        if(SurveyManager.getInstance().isSurveyDoing() || !SurveyManager.getInstance().isSurveyBlock()) return;

        Answer answer = new Answer(Calendar.getInstance().getTimeInMillis(), SurveyManager.getInstance().getInterval());
        answer.setContext(context);
        SurveyManager.getInstance().pushAnswerList(answer);

        if(i == 10)  return;
        Timer timer = new Timer();
        timer.schedule(new SampleTask(context, min, i+1), min * 60000);
    }

}
