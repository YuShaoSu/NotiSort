package com.example.jefflin.notipreference;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AppOpsManager;
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
import com.example.jefflin.notipreference.database.AccessibilityDao;
import com.example.jefflin.notipreference.database.ActivityRecognitionDao;
import com.example.jefflin.notipreference.database.AnswerJsonDao;
import com.example.jefflin.notipreference.database.LocationUpdateDao;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.database.NotiDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.AnswerJson;
import com.example.jefflin.notipreference.receiver.SampleReceiver;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

/*
    Main activity, also the landing page
    has 1 bottom navigation bar and 1 top bar
*/

public class ActivityMain extends AppCompatActivity {
    final private int SURVEY_REQUEST = 1337;
    final private int MY_PERMISSION_REQUEST_CODE = 55;
    private NotiDao notiDao;
    private ActivityRecognitionDao activityRecognitionDao;
    private LocationUpdateDao locationUpdateDao;
    private AccessibilityDao accessibilityDao;
    private AnswerJsonDao answerJsonDao;
    final private String SURVEY_POST = "survey";
    final private String ITEM_POST = "notification";
    final private String AR_POST = "activity_recognition";
    final private String LU_POST = "location_update";
    final private String AC_POST = "accessibility";
    final private String DATA_FORMAT = "MMMM d, yyyy 'at' h:mm a";
    Executor mExecutor = Executors.newSingleThreadExecutor();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NotiListenerService.Arrival");

        sharedPreferences = this.getSharedPreferences("survey", MODE_PRIVATE);

        Log.d("activity main", "on create");

        if (!isNotiListenerEnabled()) {
            final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(setting);
        }


//        if (sharedPreferences.getBoolean("block", false)) {
//            final Intent survey = new Intent(ActivityMain.this, ActivitySurvey.class);
//            survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
//            startActivityForResult(survey, SURVEY_REQUEST);
//        }

        GlobalClass.setDirPath(getApplicationContext().getDir("iconDir", Context.MODE_PRIVATE));
        notiDao = NotiDatabase.getInstance(getApplicationContext()).notiDao();
        activityRecognitionDao = NotiDatabase.getInstance(getApplicationContext()).activityRecognitionDao();
        locationUpdateDao = NotiDatabase.getInstance(getApplicationContext()).locationUpdateDao();
        accessibilityDao = NotiDatabase.getInstance(getApplicationContext()).accessibilityDao();
        answerJsonDao = NotiDatabase.getInstance(getApplicationContext()).answerJsonDao();

        setPermission();
        setBotNavView();
        setSyncTime();
        setSurveyButton();
        //setDoneBool();
    }

    private void setSyncTime() {
        TextView sync_time = (TextView) findViewById(R.id.sync_time);
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        sync_time.setText(sharedPreferences.getString("SYNC_TIME", getString(R.string.sync_time_value)));
    }

    private void setDoneBool() {
        TextView done_bool = (TextView) findViewById(R.id.done_bool);
        StringBuilder sb = new StringBuilder();
        sb.append("done: ");
        sb.append(sharedPreferences.getBoolean("done", true) ? "是; " : "否; ");
        sb.append("block: ");
        sb.append(sharedPreferences.getBoolean("block", true) ? "是; " : "否; ");
        sb.append("dont disturb: ");
        sb.append(sharedPreferences.getBoolean("dontDisturb", true) ? "是; " : "否; ");
        done_bool.setText(sb.toString());
    }

    private void setSurveyButton() {
        Button survey_button = (Button) findViewById(R.id.survey);
        if (!sharedPreferences.getBoolean("done", false) &&
                !sharedPreferences.getBoolean("dontDisturb", false) &&
                sharedPreferences.getBoolean("block", false)) {
            survey_button.setVisibility(View.VISIBLE);
        } else {
            survey_button.setVisibility(View.GONE);
        }

        survey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sync(false);
                final Intent survey = new Intent(ActivityMain.this, ActivitySurvey.class);
                survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
                startActivityForResult(survey, SURVEY_REQUEST);
            }
        });
    }

    private void setBotNavView() {
        final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        final Intent intent_history = new Intent(this, ActivityProfile.class);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_navigation_home);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        startActivity(setting);
                        break;
                    case R.id.action_sync:
                        sync(false);
                        Toast toast = Toast.makeText(ActivityMain.this,"成功回傳！", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case R.id.action_profile:
                        startActivity(intent_history);
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
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SURVEY_REQUEST) {

            SurveyManager.getInstance().surveyDone();
            sharedPreferences.edit().putBoolean("done", true)
                    .putBoolean("block", false)
                    .putBoolean("doing", false)
                    .commit();

            //setDoneBool();
            setBotNavView();


            Log.d("ActivityMainResult", "SURVEY_REQUEST " + resultCode);
            switch (resultCode) {
                case RESULT_OK:
                    sync(true);
                    setNextSurvey();
                    break;
                case RESULT_FIRST_USER: // not sync now
                    final String ans = SurveyManager.getInstance().getPostJson();
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            AnswerJson answerJson = new AnswerJson(ans);
                            answerJsonDao.insert(answerJson);
                        }
                    });
                    setNextSurvey();
                    break;
                case 0:
                    break;
            }
        } else {
            Log.d("ActivityMainResult", "not SURVEY_REQUEST " + requestCode);
        }

    }

    private void sync(final boolean now) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String jsonPost = SurveyManager.getAnswerJson(answerJsonDao.getAll(), now ? SurveyManager.getInstance().getPostJson() : "");
                postRequest(jsonPost, SURVEY_POST, now);
                Log.d("answer json", jsonPost);

                String notiPost = SurveyManager.getItemJson(notiDao.getAll());
                postRequest(notiPost, ITEM_POST, now);
                Log.d("noti json", notiPost);

                String ARPost = SurveyManager.getARJson(activityRecognitionDao.getAll());
                postRequest(ARPost, AR_POST, now);

            }
        });
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
            NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.channelID), getString(R.string.channelName), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.channelDescription));
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            try {
                notificationManager.createNotificationChannel(channel);
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            NotificationChannel channelOngoing = new NotificationChannel(String.valueOf(R.string.channelOngoingID), getString(R.string.channelNameOngoing), NotificationManager.IMPORTANCE_MIN);
            channelOngoing.setDescription(getString(R.string.channelDescriptionOngoing));
            notificationManager.createNotificationChannel(channelOngoing);
        }
    }

    private boolean isNotiListenerEnabled() {
        ComponentName cn = new ComponentName(this, NotiListenerService.class);
        String flat = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return enabled;
    }

    private void setPermission() {

        List<String> permissions = new ArrayList<String>();

        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION);

        Iterator<String> p = permissions.iterator();
        while (p.hasNext()) {
            if (chkPermission(p.next())) {
                p.remove();
            }
        }

        if (chkPermissionOps(OPSTR_GET_USAGE_STATS, Manifest.permission.PACKAGE_USAGE_STATS)) {
            Log.d("Permission", "usage stats granted");
        } else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        String[] permissionsArray = new String[permissions.size()];
        permissionsArray = permissions.toArray(permissionsArray);
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsArray,
                    MY_PERMISSION_REQUEST_CODE);
        }

    }

    private boolean chkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean chkPermissionOps(String opString, String permission) {
        AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(opString, android.os.Process.myUid(), this.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            return (this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return mode == AppOpsManager.MODE_ALLOWED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void postRequest(String jsonPost, final String url, final boolean now) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://neighborbob.ninja/" + url;
        final String requestBody = jsonPost;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")) {
                    SimpleDateFormat format = new SimpleDateFormat(DATA_FORMAT, Locale.getDefault());
                    String sync_time_value = format.format(Calendar.getInstance().getTime());
                    SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("SYNC_TIME", sync_time_value)
                            .apply();
                    setSyncTime();
                    Log.d("post", "succeed");
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (url.equals("notifications")) notiDao.deleteAll();
                            else if (url.equals(SURVEY_POST)) answerJsonDao.deleteAll();
                            else if (url.equals(AR_POST)) activityRecognitionDao.deleteAll();
//                            else if (url.equals(LU_POST)) locationUpdateDao.deleteAll();
//                            else if (url.equals(AC_POST)) accessibilityDao.deleteAll();
                        }
                    });
                } else {
                    if (now && url.equals(SURVEY_POST)) {
                        mExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                answerJsonDao.insert(new AnswerJson(SurveyManager.getInstance().getPostJson()));
                            }
                        });
                    }
                }
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

    private void setNextSurvey() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SampleReceiver.class);
        intent.putExtra("interval", 2);
        intent.setAction("com.example.jefflin.notipreference.next_interval");
        PendingIntent pii = PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 3);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pii);
        Log.d("interval time", String.valueOf(c.getTime()));
    }

}