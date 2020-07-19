package com.example.jefflin.notipreference.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SampleReceiver extends BroadcastReceiver {

    public SampleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int interval = intent.getIntExtra("interval", 0);
        Log.d("SampleReceiver", "broadcast received");
        Log.d("SampleReceiver", String.valueOf(interval));
        SharedPreferences sharedPreferences = context.getSharedPreferences("survey", Context.MODE_PRIVATE);


        // 0:00
        if (interval == 0) {
            sharedPreferences.edit().putBoolean("done", true)
                    .putBoolean("block", false)
                    .putBoolean("dontDisturb", true)
                    .apply();
        // 8:00
        } else if (interval == 1) {
//            SurveyManager.getInstance().surveyInit();
            sharedPreferences.edit().putBoolean("dontDisturb", false)
                    .putBoolean("block", false)
                    .putBoolean("done", false)
                    .putInt("stage", 1)
                    .apply();
            stageDescent(context);
        // new interval
        } else if (interval == 2) {
            if (!sharedPreferences.getBoolean("dontDisturb", false)) {
//                SurveyManager.getInstance().surveyInit();
                sharedPreferences.edit().putBoolean("done", false)
                        .putBoolean("block", false)
                        .putBoolean("doing", false)
                        .putInt("stage", 1)
                        .apply();
                stageDescent(context);
            }
        // loosen the condition
        } else if (interval == 3) {
            if (!sharedPreferences.getBoolean("dontDisturb", true) && !sharedPreferences.getBoolean("done", true)) {
                sharedPreferences.edit().putInt("stage", sharedPreferences.getInt("stage", 1) + 1).apply();
                stageDescent(context);
            }
        }

    }

    private void stageDescent(Context context) {
        Intent myIntent = new Intent(context , SampleReceiver.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        myIntent.putExtra("interval", 3);
        myIntent.setAction("com.example.jefflin.notipreference.next_interval");

        PendingIntent pi = PendingIntent.getBroadcast(context, 3 + 10, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        date.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        c.setTime(date);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//        Log.d("SampleReceiver interval", String.valueOf(c.getTime()));
    }


}
