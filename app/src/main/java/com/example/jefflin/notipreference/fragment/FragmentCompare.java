package com.example.jefflin.notipreference.fragment;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.CellSignalStrength;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.adapter.TwoListItemsAdapter;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.ESMQuestion;
import com.example.jefflin.notipreference.model.NotiItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentCompare extends Fragment {
    private TwoListItemsAdapter twoListItemsAdapter;
    private FragmentActivity mContext;

    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;
    private ImageView iv_back;
    private RecyclerView recyclerView;

    private TextView compare_title;
    private EditText compare_text;
    private boolean twoList;

    ArrayList<NotiItem> mActiveData;
    ArrayList<NotiItem> mActiveDataDisplay;

    Answer answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_compare, container, false);

        // fragment init
        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.title);
        textview_q_discription = (TextView) rootView.findViewById(R.id.discription);
        iv_back = (ImageView) rootView.findViewById(R.id.back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivitySurvey) mContext).onBackPressed();
            }
        });

        mActiveData = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");
        mActiveDataDisplay = (ArrayList<NotiItem>) getArguments().getSerializable("arrayListDisplay");
        answer = (Answer) getArguments().getSerializable("answer");
        recyclerView = rootView.findViewById(R.id.recycler_compare);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        twoListItemsAdapter = new TwoListItemsAdapter(getActivity(), mActiveData, mActiveDataDisplay);
        recyclerView.setAdapter(twoListItemsAdapter);

        compare_title = rootView.findViewById(R.id.comapre_title);
        compare_text = rootView.findViewById(R.id.comapre_text);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text;
                if(twoList) {
                    text = compare_text.getText().toString();
                }
                else {
                    text = "null";
                }

                if (answer.answerHandler(mActiveData, mActiveDataDisplay)) {
                    // scale check passed and done put notifications into answer
                    // now put ESM answer

                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
                    PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                    UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
                    ContextManager contextManager = ContextManager.getInstance();

                    answer.setEsmQ7(text);

                    // contextual data

                    // location
                    answer.setLocation(contextManager.locatoinLongtitude,
                            contextManager.locatoinLatitude, contextManager.locatoinAccuracy);

//                    // status
                    answer.isCharging = batteryManager.isCharging();
                    answer.battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    answer.isScreenOn = powerManager.isInteractive();
                    answer.isDeviceIdle = powerManager.isDeviceIdleMode();
                    answer.isPowerSave = powerManager.isPowerSaveMode();
                    answer.ringerMode = audioManager.getRingerMode();
                    answer.callState = telephonyManager.getCallState();
//                    // recent app
                    StringBuilder rappSB = new StringBuilder();
                    List<UsageStats> recentApp;
                    recentApp = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                            System.currentTimeMillis() - 5000,
                            System.currentTimeMillis());
                    for (UsageStats u : recentApp) {
                        if (u.getLastTimeUsed() == 0) continue;
                        rappSB.append(u.getPackageName());
                        rappSB.append(" : ");
                        rappSB.append(u.getLastTimeUsed() + "; ");
                    }
                    answer.recentApp = rappSB.toString();

                    Network[] networks = connectivityManager.getAllNetworks();
                    StringBuilder ntwSB = new StringBuilder();
                    for (Network n : networks) {
                        ntwSB.append(connectivityManager.getNetworkInfo(n));
                    }
                    answer.network = ntwSB.toString();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        SignalStrength signalStrength = telephonyManager.getSignalStrength();
                        try {
                            List<CellSignalStrength> ss = signalStrength.getCellSignalStrengths();
                            for (CellSignalStrength s : ss) {
                                answer.signalType = s.toString();
                                answer.signalDbm = s.getDbm();
                            }
                        } catch (NullPointerException e) {
                            Log.e("signal strength", "getCellSignalStrength null pt", e);
                        }
                    } else {
                        answer.signalType = contextManager.phoneSignalType;
                        answer.signalDbm = contextManager.phoneSignalDbm;
                    }

                    // Sensors
                    answer.light = contextManager.light;

                    String json = SurveyManager.getInstance().getAnswer(answer);

                    Log.i("ans in json:", json);

                } else {  // scale not done or other err(not set on current)
                    Log.d("answer handler", "scale not done");
                }

                ((ActivitySurvey) mContext).go_to_next();

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        ESMQuestion q_data = (ESMQuestion) getArguments().getSerializable("data");
        textview_q_title.setText(q_data.getQuestionTitle());
        textview_q_discription.setText(q_data.getDescription());

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }
    }

    public void refreshAdapter() {
        twoList = SurveyManager.getInstance().twoListDiff(mActiveData, mActiveDataDisplay);
        twoListItemsAdapter.notifyDataSetChanged();
        // whether to add q7
        if (twoList) {
            compare_title.setVisibility(View.VISIBLE);
            compare_text.setVisibility(View.VISIBLE);
        } else {
            compare_title.setVisibility(View.GONE);
            compare_text.setVisibility(View.GONE);
        }
    }

}
