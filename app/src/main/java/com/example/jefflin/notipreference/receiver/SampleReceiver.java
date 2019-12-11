package com.example.jefflin.notipreference.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.manager.SurveyManager;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class SampleReceiver extends BroadcastReceiver {

    public SampleReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String interval = String.valueOf(intent.getExtras().get("interval"));
        Log.d("SampleReceiver", "broadcast received");
        Log.d("SampleReceiver", interval);

        SurveyManager.getInstance().surveyInit();

        Intent myIntent = new Intent(context , SampleReceiver.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        myIntent.putExtra("interval", interval);
        myIntent.setAction("com.example.jefflin.notipreference.next_interval");


        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.parseInt(interval) + 10, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, GlobalClass.getIntervalTime()[Integer.parseInt(interval)]);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.MINUTE, GlobalClass.getIntervalMinute());
        c.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        Log.d("SampleReceiver interval", String.valueOf(c.getTime()));
    }

    public void surveyDone() {
        SurveyManager.getInstance().surveyDone();
    }

}
