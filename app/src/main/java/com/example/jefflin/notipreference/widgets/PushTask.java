package com.example.jefflin.notipreference.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.Timer;
import java.util.TimerTask;

public class PushTask extends TimerTask {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PushTask(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("survey", Context.MODE_PRIVATE);
    }

    @Override
    public void run() {
        if (!sharedPreferences.getBoolean("block", true))
            return;
        new PushNotification(context).push(false);
        Timer timer = new Timer();
        timer.schedule(new UnblockTask(context), 900000);
    }

}
