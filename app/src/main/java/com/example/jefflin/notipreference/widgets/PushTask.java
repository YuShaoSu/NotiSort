package com.example.jefflin.notipreference.widgets;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.Timer;
import java.util.TimerTask;

public class PushTask extends TimerTask {

    private Context context;

    public PushTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        new PushNotification(context).push(false);
        Timer timer = new Timer();
        timer.schedule(new UnblockTask(), 900000);
    }

}