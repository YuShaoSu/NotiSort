package com.example.jefflin.notipreference;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;

import com.example.jefflin.notipreference.services.NotiListenerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;

/*
    Main activity, also the landing page
    has 1 bottom navigation bar and 1 top bar
 */

public class ActivityMain extends AppCompatActivity {
    final private int SURVEY_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setDirPath(getApplicationContext(), "iconDir");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NotiListenerService.Arrival");

        if (!isNotiListenerEnabled()) {
            final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(setting);
        }

        if (isNotiListenerEnabled()) {
            final Intent survey = new Intent(ActivityMain.this, ActivitySurvey.class);
            survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
            startActivityForResult(survey, SURVEY_REQUEST);
        }


        setBotNavView();
    }

    private void setBotNavView(){
        final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        final Intent survey = new Intent(ActivityMain.this, ActivitySurvey.class);
        final Intent intent_history = new Intent(this, ActivityHistory.class);

        survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_navigation_home);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        startActivity(setting);
                        break;
                    case R.id.action_sort:
                        startActivityForResult(survey, SURVEY_REQUEST);
                        break;
                    case R.id.action_profile:
                        startActivity(intent_history);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SURVEY_REQUEST) {
            if (resultCode == RESULT_OK) {

                String answers_json = data.getExtras().getString("answers");
                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", answers_json);
                Log.d("****", "*****************************************************");

                Log.d("****", answers_json);
            }
        }
    }

    private String loadSurveyJson(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channelName);
            String description = getString(R.string.channelDescription);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.channelID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isNotiListenerEnabled() {
        ComponentName cn = new ComponentName(this, NotiListenerService.class);
        String flat = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return enabled;
    }
}