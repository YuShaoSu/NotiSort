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

    public SampleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int interval = intent.getIntExtra("interval", 0);
        Log.d("SampleReceiver", "broadcast received");
        Log.d("SampleReceiver", String.valueOf(interval));

//        SurveyManager.getInstance().setInterval(interval);
        if (interval == 1) {
            SurveyManager.getInstance().surveyInit();
            SurveyManager.getInstance().setDontDisturb(false);
        } else if (interval == 0) {
            SurveyManager.getInstance().setSurveyDone(true);
            SurveyManager.getInstance().setDontDisturb(true);
        } else if (interval == 2) {
            if (!SurveyManager.getInstance().isDontDisturb())
                SurveyManager.getInstance().surveyInit();
        }

//        Intent myIntent = new Intent(context , SampleReceiver.class);
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        myIntent.putExtra("interval", interval);
//        myIntent.setAction("com.example.jefflin.notipreference.next_interval");
//
//
//        PendingIntent pi = PendingIntent.getBroadcast(context, interval + 10, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, GlobalClass.getIntervalTime()[interval]);
//        c.add(Calendar.DAY_OF_YEAR, 1);
//        c.set(Calendar.MINUTE, GlobalClass.getIntervalMinute());
//        c.set(Calendar.SECOND, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
//        Log.d("SampleReceiver interval", String.valueOf(c.getTime()));
    }


}
