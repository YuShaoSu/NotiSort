package com.example.jefflin.notipreference;

import android.Manifest;
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
import com.example.jefflin.notipreference.database.ActivityRecognitionDao;
import com.example.jefflin.notipreference.database.LocationUpdateDao;
import com.example.jefflin.notipreference.receiver.SampleReceiver;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.database.NotiDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    final private String SURVEY_POST = "survey";
    final private String ITEM_POST = "notification";
    final private String AR_POST = "activity_recognition";
    final private String LU_POST = "location_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NotiListenerService.Arrival");

        Log.d("activity main", "on create");

        if (!isNotiListenerEnabled()) {
            final Intent setting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(setting);
        }

        if (isNotiListenerEnabled()) {
            final Intent survey = new Intent(ActivityMain.this, ActivitySurvey.class);
            survey.putExtra("json_survey", loadSurveyJson("example_survey_1.json"));
            startActivityForResult(survey, SURVEY_REQUEST);
        }

        GlobalClass.setDirPath(getApplicationContext(), "iconDir");
        notiDao = NotiDatabase.getInstance(getApplicationContext()).notiDao();
        activityRecognitionDao = NotiDatabase.getInstance(getApplicationContext()).activityRecognitionDao();
        locationUpdateDao = NotiDatabase.getInstance(getApplicationContext()).locationUpdateDao();

        setPermission();
        setBotNavView();

//      setSchedule();

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

                String jsonPost = SurveyManager.getInstance().getPostJson();
                postRequest(jsonPost, SURVEY_POST);
                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", jsonPost);
                Log.d("****", "*****************************************************");

                String notiPost = SurveyManager.getItemJson(notiDao.getAll());
                postRequest(notiPost, ITEM_POST);

                String ARPost = SurveyManager.getARJson(activityRecognitionDao.getAll());
                postRequest(ARPost, AR_POST);

                String LUPost = SurveyManager.getLUJson(locationUpdateDao.getAll());
                postRequest(LUPost, LU_POST);


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

        for (int i = 0; i < 9; ++i){
            Intent myIntent = new Intent(ActivityMain.this , SampleReceiver.class);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            myIntent.putExtra("interval", i);
            myIntent.setAction("com.example.jefflin.notipreference.next_interval");

            PendingIntent pi = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, GlobalClass.getIntervalTime()[i]);
            c.set(Calendar.MINUTE, 40);
            c.set(Calendar.SECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            Log.d("interval time", String.valueOf(c.getTime()));
        }
    }

    private void setPermission() {

        List<String> permissions = new ArrayList<String>();
        //String[] permissions = new String[];

        // set permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            GlobalClass.setDeviceID(tm.getDeviceId());
            Log.d("Permission", "device read granted");
        }
        else {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if(chkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            Log.d("Permission", "location granted");
        }
        else {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_PHONE_STATE)) {
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//        }
//        else {
            // No explanation needed; request the permission

        String[] permissionsArray = new String[permissions.size()];
        permissionsArray = permissions.toArray(permissionsArray);
        if(!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsArray,
                    MY_PERMISSION_REQUEST_CODE);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//        }
        }

    }

    private boolean chkPermission(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
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
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                                == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        GlobalClass.setDeviceID(tm.getDeviceId());
                        Log.d("Permission device", tm.getDeviceId());
                    }
                    else {
                        GlobalClass.setUUID();
                        Log.d("Permission device", GlobalClass.getDeviceID());
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void postRequest(String jsonPost, final String url) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://neighborbob.nctu.me:5000/" + url;
            final String requestBody = jsonPost;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    if(response.equals("200")) {
                        Log.d("post", "succeed");
                        Toast.makeText(getApplicationContext(),"notisort server received!", Toast.LENGTH_LONG);
                        SurveyManager.getInstance().surveyDone();
                        if(url.equals("notifications")) notiDao.deleteAll();
                        else if(url.equals(AR_POST)) activityRecognitionDao.deleteAll();
                        else if(url.equals(LU_POST)) locationUpdateDao.deleteAll();
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

}