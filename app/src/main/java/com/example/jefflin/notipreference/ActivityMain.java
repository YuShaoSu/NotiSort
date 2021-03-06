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
import com.android.volley.DefaultRetryPolicy;
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
import com.example.jefflin.notipreference.database.NotiModelRemoveDao;
import com.example.jefflin.notipreference.database.NotiPoolDao;
import com.example.jefflin.notipreference.database.SampleCombinationDao;
import com.example.jefflin.notipreference.database.SampleRecordDao;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.database.NotiDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.AnswerJson;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.model.NotiModelRemove;
import com.example.jefflin.notipreference.model.SampleCombination;
import com.example.jefflin.notipreference.model.SampleRecord;
import com.example.jefflin.notipreference.model.SurveyInfo;
import com.example.jefflin.notipreference.receiver.SampleReceiver;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.example.jefflin.notipreference.widgets.Utils;
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
    final private int RESULT_SYNC_NOW = 1338;
    final private int RESULT_NOT_SYNC_NOW = 1340;
    private NotiDao notiDao;
    private ActivityRecognitionDao activityRecognitionDao;
    private AnswerJsonDao answerJsonDao;
    private SampleRecordDao sampleRecordDao;
    private SampleCombinationDao sampleCombinationDao;
    private NotiModelRemoveDao notiModelRemoveDao;
    final private String SURVEY_POST = "survey";
    final private String ITEM_POST = "notification";
    final private String AR_POST = "activity_recognition";
    final private String LU_POST = "location_update";
    final private String AC_POST = "accessibility";
    final private String INFO_POST = "survey_info";
    final private String REMOVE_POST = "notification_remove";
    final private String DATA_FORMAT = "MMMM d, yyyy 'at' h:mm a";

    Executor mExecutor = Executors.newCachedThreadPool();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        sharedPreferences = this.getSharedPreferences("survey", MODE_PRIVATE);

        // check if the user grant the permission for notification listener service
        if (!isNotiListenerEnabled()) {
            final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(setting);
        }


        // this set the path where to store the app icon
        GlobalClass.setDirPath(getApplicationContext().getDir("iconDir", Context.MODE_PRIVATE));


        notiDao = NotiDatabase.getInstance(getApplicationContext()).notiDao();
        activityRecognitionDao = NotiDatabase.getInstance(getApplicationContext()).activityRecognitionDao();
        sampleRecordDao = NotiDatabase.getInstance(getApplicationContext()).sampleRecordDao();
        sampleCombinationDao = NotiDatabase.getInstance(getApplicationContext()).sampleCombinationDao();
        answerJsonDao = NotiDatabase.getInstance(getApplicationContext()).answerJsonDao();
        notiModelRemoveDao = NotiDatabase.getInstance(getApplicationContext()).notiModelRemoveDao();

        setPermission();
        setBotNavView();
        setSyncTime();
        setSurveyButton();
        setSurveyCount();
        //setDoneBool();
    }

    private void setSyncTime() {
        // the last sync time to show in the homepage
        TextView sync_time = (TextView) findViewById(R.id.sync_time);
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        sync_time.setText(sharedPreferences.getString("SYNC_TIME", getString(R.string.sync_time_value)));
    }

    private void setSurveyCount() {
        // questionnaire send, open(response), done statistic
        SharedPreferences sharedPreferences = getSharedPreferences("survey", MODE_PRIVATE);
//        TextView sendCount = (TextView) findViewById(R.id.sendCount);
//        TextView respondedCount = (TextView) findViewById(R.id.respondedCount);
        TextView finishedCount = (TextView) findViewById(R.id.finishedCount);
//        String sendString = getResources().getString(R.string.sendCount) + sharedPreferences.getInt("surveySendCount", 0);
//        String respondedString = getResources().getString(R.string.respondedCount) + sharedPreferences.getInt("surveyRespondedCount", 0);
        String finishedString = getResources().getString(R.string.finishedCount) + sharedPreferences.getInt("surveyFinishedCount", 0);
//        sendCount.setText(sendString);
//        respondedCount.setText(respondedString);
        finishedCount.setText(finishedString);
    }

    private void setDoneBool() {
        // for debug only, to make sure the state of questionnaire correct
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
        // whether to display the button to enter the questionnaire
        Button survey_button = (Button) findViewById(R.id.survey);
        if (!sharedPreferences.getBoolean("dontDisturb", false) &&
                sharedPreferences.getBoolean("block", false) &&
                !sharedPreferences.getBoolean("done", false))
        {
            survey_button.setVisibility(View.VISIBLE);
        } else {
            survey_button.setVisibility(View.GONE);
        }

        survey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add response count if the user open it
                int count = sharedPreferences.getInt("surveyRespondedCount", 0);
                sharedPreferences.edit().putInt("surveyRespondedCount", count + 1).apply();

                // start the questionnaire
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
        // control the behavior when click the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        startActivity(setting);
                        break;
                    case R.id.action_sync:
                        sync(false);
                        Toast toast = Toast.makeText(ActivityMain.this, "成功回傳！", Toast.LENGTH_SHORT);
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
    protected void onResume() {
        super.onResume();
        setSurveyButton();
        setSurveyCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void surveyDoneSuccess(List<NotiItem> notiItems) {
        final ArrayList<SampleCombination> sampleCombinations = getSampleCombination(notiItems);

        // upsert the sample combination to db
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sampleCombinationDao.upsert(sampleCombinations);
            }
        });


        int count = sharedPreferences.getInt("surveyFinishedCount", 0);
        sharedPreferences.edit().putInt("surveyFinishedCount", count + 1).apply();

        long lastFinish = Calendar.getInstance().getTimeInMillis();
        sharedPreferences.edit().putBoolean("done", true)
                .putBoolean("block", false)
                .putBoolean("doing", false)
                .putLong("lastFinishTime", lastFinish)
                .apply();

        SurveyManager.getInstance().surveyDone();
        setBotNavView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<NotiItem> arrayList;

        // it can be other reason to reach this state other than simply finish the survey,
        // as a result we have to make sure the requestCode is the one ActivitySurvey pass back.
        if (requestCode == SURVEY_REQUEST) {

            switch (resultCode) {
                case RESULT_SYNC_NOW:
                    // meaning the user press 是 when asking 是否要現在回傳資料? in the last page of the questionnaire.
                    setNextSurvey();
                    arrayList = (List<NotiItem>) data.getExtras().getSerializable("arrayList");
                    surveyDoneSuccess(arrayList);
                    sync(true);
                    break;

                case RESULT_NOT_SYNC_NOW:
                    // meaning the user press 否
                    setNextSurvey();
                    arrayList = (List<NotiItem>) data.getExtras().getSerializable("arrayList");
                    surveyDoneSuccess(arrayList);
                    final String ans = SurveyManager.getInstance().getPostJson();
                    // not sync with server now, store it to database
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            AnswerJson answerJson = new AnswerJson(ans);
                            answerJsonDao.insert(answerJson);
                        }
                    });
                    break;
                case 0:
                    break;
            }

        }
    }

    private void sync(final boolean now) {
        // We sync five type of data with server here,
        // survey statistic(infoPost), questionnaire(jsonPost), notifications(notiPost), activity recognition(ARPost), notification remove(removePost)
        int surveySendCount = sharedPreferences.getInt("surveySendCount", 0);
        int surveyRespondedCount = sharedPreferences.getInt("surveyRespondedCount", 0);
        int surveyFinishedCount = sharedPreferences.getInt("surveyFinishedCount", 0);
        SharedPreferences sharedPreferencesUser = getSharedPreferences("USER", MODE_PRIVATE);
        String did = sharedPreferencesUser.getString("ID", "user id fail");
        final SurveyInfo surveyInfo = new SurveyInfo(did, surveySendCount, surveyRespondedCount, surveyFinishedCount, Calendar.getInstance().getTimeInMillis());
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String jsonPost = SurveyManager.getAnswerJson(answerJsonDao.getAll(), now ? SurveyManager.getInstance().getPostJson() : "");
                postRequest(jsonPost, SURVEY_POST, now);

                String notiPost = SurveyManager.getItemJson(notiDao.getAll());
                postRequest(notiPost, ITEM_POST, now);

                String ARPost = SurveyManager.getARJson(activityRecognitionDao.getAll());
                postRequest(ARPost, AR_POST, now);

                String infoPost = SurveyManager.getInfoJson(surveyInfo);
                postRequest(infoPost, INFO_POST, now);

                String removePost = SurveyManager.getRemoveJson(notiModelRemoveDao.getAll());
                postRequest(removePost, REMOVE_POST, now);

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
            } catch (NullPointerException e) {
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
        return flat != null && flat.contains(cn.flattenToString());
    }

    private void setPermission() {
        // check and ask for permission if the user haven't granted.

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
            }
        }
    }

    private void getRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    // use VOLLEY package to handle requests.
    private void postRequest(String jsonPost, final String url, final boolean now) {
        // the 'now' parameter is to denote whether the user sync right after the survey
        // need this info since we may have to store it to database if the post request not succeed.
        final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // you have to change this url to yours
        String URL = "https://notisort.neighborbob.me/" + url + "?did=" + sharedPreferences.getString("ID", "user id fail") + "&ver=5";
        final String requestBody = jsonPost;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")) {
                    SimpleDateFormat format = new SimpleDateFormat(DATA_FORMAT, Locale.getDefault());
                    String sync_time_value = format.format(Calendar.getInstance().getTime());
                    sharedPreferences.edit()
                            .putString("SYNC_TIME", sync_time_value)
                            .apply();
                    setSyncTime();
//                    Log.d("post", "succeed");
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (url.equals(ITEM_POST)) notiDao.deleteAll();
                            else if (url.equals(SURVEY_POST)) answerJsonDao.deleteAll();
                            else if (url.equals(AR_POST)) activityRecognitionDao.deleteAll();
                            else if (url.equals(REMOVE_POST)) notiModelRemoveDao.deleteAll();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        Log.d("retry policy ", stringRequest.getRetryPolicy().getCurrentTimeout() + " max_retry " + stringRequest.getRetryPolicy().getCurrentRetryCount());

        requestQueue.add(stringRequest);
    }

    private ArrayList<SampleRecord> getSurveyRecord() {
        ArrayList<SampleRecord> l = new ArrayList<SampleRecord>();
        ArrayList<NotiItem> notiItems = SurveyManager.getInstance().getMap().get("click");
        for (int i = 0; i < notiItems.size(); ++i) {
            l.add(new SampleRecord(notiItems.get(i).appName, notiItems.get(i).title, notiItems.get(i).content, notiItems.get(i).postTime));
        }

        return l;
    }

    private ArrayList<SampleCombination> getSampleCombination(List<NotiItem> notiItems) {
        // get app combination from the sampled list of notifications
        ArrayList<SampleCombination> l = new ArrayList<>();
        List<String> appCombs = new Utils().getTwoAppList(notiItems);
        for (String s : appCombs) {
            l.add(new SampleCombination(s));
        }

        return l;
    }

    private void setNextSurvey() {
        // set next alarmManager to schedule next survey
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SampleReceiver.class);
        intent.putExtra("interval", 2);
        intent.setAction("com.example.jefflin.notipreference.next_interval");
        PendingIntent pii = PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 3);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pii);
    }

}