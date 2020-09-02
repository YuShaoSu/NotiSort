package com.example.jefflin.notipreference;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.jefflin.notipreference.adapter.FragmentAdapter;
import com.example.jefflin.notipreference.database.ActivityRecognitionDao;
import com.example.jefflin.notipreference.database.NotiDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.database.NotiModelRemoveDao;
import com.example.jefflin.notipreference.fragment.FragmentLog;
import com.example.jefflin.notipreference.fragment.FragmentStart;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.ActivityRecognitionModel;
import com.example.jefflin.notipreference.model.AnswerJson;
import com.example.jefflin.notipreference.model.ESMPojo;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.model.NotiModel;
import com.example.jefflin.notipreference.model.NotiModelRemove;
import com.example.jefflin.notipreference.widgets.PushNotification;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.sql.StatementEvent;

public class ActivityProfile extends AppCompatActivity {

    private ImageView iv_back;
    private TextView show_id;
    private EditText edit_id;
    private EditText notification_sync_limit;
    private EditText ar_sync_limit;
    private EditText nr_sync_limit;
    private Button done;
    private Button test;
    private Button log;
    private Button noti_sync;
    private Button ar_sync;
    private Button nr_sync;
    private Button refresh;
    private SharedPreferences sharedPreferences;
    private NotiDao notiDao;
    private ActivityRecognitionDao activityRecognitionDao;
    private NotiModelRemoveDao notiModelRemoveDao;
    private TextView noti_db_count;
    private TextView noti_db_lid;
    private TextView noti_db_sid;
    private TextView noti_sync_status;

    private TextView ar_count;
    private TextView nr_count;
    final private String DATA_FORMAT = "MMMM d, yyyy 'at' h:mm a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE);
        notiDao = NotiDatabase.getInstance(this).notiDao();
        activityRecognitionDao = NotiDatabase.getInstance(this).activityRecognitionDao();
        notiModelRemoveDao = NotiDatabase.getInstance(this).notiModelRemoveDao();

        iv_back = (ImageView) findViewById(R.id.back);
        show_id = (TextView) findViewById(R.id.profile_show_id);
        edit_id = (EditText) findViewById(R.id.profile_edit_id);
        notification_sync_limit = (EditText) findViewById(R.id.notification_sync_limit);
        done = (Button) findViewById(R.id.profile_id_button);
        test = (Button) findViewById(R.id.profile_test);
        log = (Button) findViewById(R.id.log);
        noti_db_count = (TextView) findViewById(R.id.notification_db_count);
        noti_db_lid = (TextView) findViewById(R.id.notification_db_lid);
        noti_db_sid = (TextView) findViewById(R.id.notification_db_sid);
        noti_sync_status = (TextView) findViewById(R.id.notification_sync_status);

        ar_count = (TextView) findViewById(R.id.ar_count);
        nr_count = (TextView) findViewById(R.id.nr_count);

        ar_sync = (Button) findViewById(R.id.ar_sync);
        nr_sync = (Button) findViewById(R.id.nr_sync);

        noti_sync = (Button) findViewById(R.id.notification_sync);
        refresh = (Button) findViewById(R.id.notification_refresh);

        ar_sync_limit = (EditText) findViewById(R.id.ar_sync_limit);
        nr_sync_limit = (EditText) findViewById(R.id.nr_sync_limit);


        refreshUI();

        ar_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ar_sync(Integer.valueOf(ar_sync_limit.getText().toString()));
            }
        });

        nr_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nr_sync(Integer.valueOf(nr_sync_limit.getText().toString()));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti_sync_status.setText("no sync event");
                refreshUI();
            }
        });

        noti_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = notification_sync_limit.getText().toString();
                noti_sync_status.setText("start to sync");
                sync(Integer.valueOf(l));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //- START -

//                FragmentStart frag_start = new FragmentStart();
//                Bundle sBundle = new Bundle();
//                sBundle.putSerializable("survery_properties", mESMPojo.getESMProperties());
//                sBundle.putString("style", style_string);
//                frag_start.setArguments(sBundle);
//                arraylist_fragments.add(frag_start);
                setContentView(R.layout.activity_main_survey);
                ArrayList<Fragment> fragments = new ArrayList<>();
                FragmentLog fragmentLog = new FragmentLog();
                fragments.add(fragmentLog);
                ViewPager mPager = (ViewPager) findViewById(R.id.pager);
                FragmentAdapter mPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
                mPager.setAdapter(mPagerAdapter);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.super.onBackPressed();
            }
        });

        show_id.setText(sharedPreferences.getString("ID", ""));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = edit_id.getText().toString();
                sharedPreferences.edit()
                        .putString("ID", user_id)
                        .commit();
                show_id.setText(sharedPreferences.getString("ID", ""));
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setExample();
                new PushNotification(ActivityProfile.this).push(false);
            }
        });


    }

    private void setExample() {
        Map<String, ArrayList<NotiItem>> map = new HashMap<>();
        ArrayList<NotiItem> listA = new ArrayList<>();
        ArrayList<NotiItem> listB = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            listA.add(new NotiItem("test", "test", "test", Calendar.getInstance().getTimeInMillis(), "test", i));
            listB.add(new NotiItem("test", "test", "test", Calendar.getInstance().getTimeInMillis(), "test", i));
        }
        map.put("click", listA);
        map.put("display", listB);

        SurveyManager.getInstance().setMap(map);
    }

    private List<NotiModel> notiInDB(final int limit) {
        List<NotiModel> results = new ArrayList<NotiModel>();
        FutureTask<List<NotiModel>> mFuture = new FutureTask<List<NotiModel>>(new Callable<List<NotiModel>>() {
            @Override
            public List<NotiModel> call() throws Exception {
                return notiDao.getPartial(limit);
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            results = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private List<ActivityRecognitionModel> arInDB(final int limit) {
        List<ActivityRecognitionModel> results = new ArrayList<ActivityRecognitionModel>();
        FutureTask<List<ActivityRecognitionModel>> mFuture = new FutureTask<List<ActivityRecognitionModel>>(new Callable<List<ActivityRecognitionModel>>() {
            @Override
            public List<ActivityRecognitionModel> call() throws Exception {
                return activityRecognitionDao.getPartial(limit);
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            results = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }


    private List<NotiModelRemove> nrInDB(final int limit) {
        List<NotiModelRemove> results = new ArrayList<NotiModelRemove>();
        FutureTask<List<NotiModelRemove>> mFuture = new FutureTask<List<NotiModelRemove>>(new Callable<List<NotiModelRemove>>() {
            @Override
            public List<NotiModelRemove> call() throws Exception {
                return notiModelRemoveDao.getPartial(limit);
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(mFuture);
        try {
            results = mFuture.get();
            Log.d("nr sync", results.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }


    private void refreshUI() {
        HashMap<String, Integer> r = notiInDBCount();
        Log.d("notiDBStatistic", "Q" + r.size() + r.toString());
        noti_db_count.setText(String.valueOf(r.get("dbCount")));
        noti_db_lid.setText(String.valueOf(r.get("largestID")));
        noti_db_sid.setText(String.valueOf(r.get("smallestID")));
        ar_count.setText(String.valueOf(r.get("ar")));
        nr_count.setText(String.valueOf(r.get("nr")));
    }

    private void notiInDBDelete(final List<NotiModel> notiModels) {
        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notiDao.deleteNotiList(notiModels);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                    }
                });

            }
        });
    }

    private void arInDBDelete(final List<ActivityRecognitionModel> activityRecognitionModels) {
        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                activityRecognitionDao.deleteARList(activityRecognitionModels);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                    }
                });

            }
        });
    }

    private void nrInDBDelete(final List<NotiModelRemove> notiModelRemoves) {
        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notiModelRemoveDao.deleteNotiRemoveList(notiModelRemoves);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                    }
                });

            }
        });
    }

    private HashMap<String, Integer> notiInDBCount() {
        HashMap<String, Integer> result = new HashMap<>();
        FutureTask<Integer> countFuture = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return notiDao.getCount();
            }
        });
        FutureTask<Integer> lIDFuture = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return notiDao.getLargestID();
            }
        });
        FutureTask<Integer> sIDFuture = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return notiDao.getSmallestID();
            }
        });
        FutureTask<Integer> ARFuture = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return activityRecognitionDao.getCount();
            }
        });
        FutureTask<Integer> NRFuture = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return notiModelRemoveDao.getCount();
            }
        });
        ExecutorService sExecutor = Executors.newSingleThreadExecutor();
        sExecutor.execute(countFuture);
        try {
            result.put("dbCount", countFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sExecutor.execute(lIDFuture);
        try {
            result.put("largestID", lIDFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sExecutor.execute(sIDFuture);
        try {
            result.put("smallestID", sIDFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sExecutor.execute(ARFuture);
        try {
            result.put("ar", ARFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sExecutor.execute(NRFuture);
        try {
            result.put("nr", NRFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    private void sync(int limit) {
        final List<NotiModel> to_sync = notiInDB(limit);
        String jsonPost = SurveyManager.getItemJson(to_sync);

        final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://notisort.neighborbob.me/notification" + "?did=" + sharedPreferences.getString("ID", "user id fail") + "&ver=noti_sync";
        final String requestBody = jsonPost;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")) {
                    noti_sync_status.setText("sync success");
                    notiInDBDelete(to_sync);
                } else {
                    noti_sync_status.setText("sync fail");
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

    private void ar_sync(int limit) {
        final List<ActivityRecognitionModel> to_sync = arInDB(limit);
        String jsonPost = SurveyManager.getARJson(to_sync);

        final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://notisort.neighborbob.me/activity_recognition" + "?did=" + sharedPreferences.getString("ID", "user id fail") + "&ver=sync";
        final String requestBody = jsonPost;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")) {
                    arInDBDelete(to_sync);
                } else {
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

    private void nr_sync(int limit) {
        final List<NotiModelRemove> to_sync = nrInDB(limit);
        String jsonPost = SurveyManager.getRemoveJson(to_sync);

        final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://notisort.neighborbob.me/notification_remove" + "?did=" + sharedPreferences.getString("ID", "user id fail") + "&ver=sync";
        final String requestBody = jsonPost;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                if (response.equals("200")) {
                    nrInDBDelete(to_sync);
                } else {
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


}
