package com.example.jefflin.notipreference;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

        setSchedule();
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

                String jsonPost = SurveyAnswer.getInstance().getPostJson();
                postAnswer(jsonPost);
                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", jsonPost);
                Log.d("****", "*****************************************************");

                Log.d("****", jsonPost);
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

    private void setSchedule() {
        Intent myIntent = new Intent(ActivityMain.this , SurveyManager.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        PendingIntent pi0 = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar c0 = Calendar.getInstance();
        c0.set(Calendar.HOUR_OF_DAY, 8);
        c0.set(Calendar.MINUTE, 0);
        c0.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c0.getTimeInMillis(), TimeUnit.DAYS.toMillis(1), pi0);

        PendingIntent pi1 = PendingIntent.getService(this, 1, myIntent, 0);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 12);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c1.getTimeInMillis(), TimeUnit.DAYS.toMillis(1) , pi1);

        PendingIntent pi2 = PendingIntent.getService(this, 2, myIntent, 0);
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, 16);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c2.getTimeInMillis(), TimeUnit.DAYS.toMillis(1) , pi2);

        PendingIntent pi3 = PendingIntent.getService(this, 3, myIntent, 0);
        Calendar c3 = Calendar.getInstance();
        c3.set(Calendar.HOUR_OF_DAY, 20);
        c3.set(Calendar.MINUTE, 0);
        c3.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c3.getTimeInMillis(), TimeUnit.DAYS.toMillis(1) , pi3);
    }

    private void postAnswer(String jsonPost) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://neighborbob.nctu.me:5000/survey";
            final String requestBody = jsonPost;

            Log.d("postAnswer", "postAnswer function in activityMain");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    if(response == "200") Toast.makeText(getApplicationContext(),"notisort server received!", Toast.LENGTH_LONG);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
    }

}