package com.example.jefflin.notipreference.fragment;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.adapter.TwoListItemsAdapter;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.model.ESMQuestion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentESM extends Fragment {

    private TwoListItemsAdapter twoListItemsAdapter;

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;
//    private ImageView iv_back;

    private RadioGroup radioGroupQ1;
    //private EditText editTextQ1;
    private RadioGroup radioGroupQ2;
    private EditText editTextQ2;
    private RadioGroup radioGroupQ3;
    private EditText editTextQ3;
    private RadioGroup radioGroupQ4;
    private RadioGroup radioGroupQ5;
    private RadioGroup radioGroupQ6;

    private Boolean q1_selected;
    private Boolean q2_selected;
    private Boolean q3_selected;
    private Boolean q4_selected;
    private Boolean q5_selected;
    private Boolean q6_selected;


    Answer answer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_esm, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.title);
        textview_q_discription = (TextView) rootView.findViewById(R.id.discription);


//        iv_back = (ImageView) rootView.findViewById(R.id.back);
//
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ActivitySurvey) mContext).onBackPressed();
//            }
//        });


        radioGroupQ1 = rootView.findViewById(R.id.q1);
        //editTextQ1 = rootView.findViewById(R.id.q2_other);
        radioGroupQ2 = rootView.findViewById(R.id.q2);
        editTextQ2 = rootView.findViewById(R.id.q2_other);
        radioGroupQ3 = rootView.findViewById(R.id.q3);
        editTextQ3 = rootView.findViewById(R.id.q3_other);
        radioGroupQ4 = rootView.findViewById(R.id.q4);
        radioGroupQ5 = rootView.findViewById(R.id.q5);
        radioGroupQ6 = rootView.findViewById(R.id.q6);

        q1_selected = false;
        q2_selected = false;
        q3_selected = false;
        q4_selected = false;
        q5_selected = false;
        q6_selected = false;


        radioGroupQ1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q1_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });

        radioGroupQ2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q2_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        radioGroupQ3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        radioGroupQ4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q4_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        radioGroupQ5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q5_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        radioGroupQ6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q6_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });

        button_continue.setBackgroundColor(getResources().getColor(R.color.grey_medium));
        button_continue.setEnabled(false);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton selectedRadioButton1 = (RadioButton) rootView.findViewById(radioGroupQ1.getCheckedRadioButtonId());
                String selectedRadioButtonText1 = selectedRadioButton1.getText().toString();
                //String selectedRadioButtonText1 = editTextQ1.getText().toString();
                RadioButton selectedRadioButton2 = (RadioButton) rootView.findViewById(radioGroupQ2.getCheckedRadioButtonId());
                String selectedRadioButtonText2 = selectedRadioButton2.getText().toString();
                RadioButton selectedRadioButton3 = (RadioButton) rootView.findViewById(radioGroupQ3.getCheckedRadioButtonId());
                String selectedRadioButtonText3 = selectedRadioButton3.getText().toString();

                if (selectedRadioButtonText2.equals("其他")) {
                    selectedRadioButtonText2 = selectedRadioButtonText2 + ":" + editTextQ2.getText().toString();
                }
                if (selectedRadioButtonText3.equals("其他")) {
                    selectedRadioButtonText3 = selectedRadioButtonText3 + ":" + editTextQ3.getText().toString();
                }

                RadioButton selectedRadioButton4 = (RadioButton) rootView.findViewById(radioGroupQ4.getCheckedRadioButtonId());
                String selectedRadioButtonText4 = selectedRadioButton4.getTag().toString();
                RadioButton selectedRadioButton5 = (RadioButton) rootView.findViewById(radioGroupQ5.getCheckedRadioButtonId());
                String selectedRadioButtonText5 = selectedRadioButton5.getTag().toString();
                RadioButton selectedRadioButton6 = (RadioButton) rootView.findViewById(radioGroupQ6.getCheckedRadioButtonId());
                String selectedRadioButtonText6 = selectedRadioButton6.getTag().toString();


                // handle the answers of previous
                answer = (Answer) getArguments().getSerializable("answer");
                SharedPreferences pref = mContext.getSharedPreferences("USER", Context.MODE_PRIVATE);
                answer.setId(pref.getString("ID", "id failed"));
//                if (answer.answerHandler(mActiveData, mActiveDataDisplay)) {
//                    // scale check passed and done put notifications into answer
//                    // now put ESM answer
//
//                    AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//                    BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
//                    PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//                    UsageStatsManager usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
//                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//                    TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
////                    SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
//                    ContextManager contextManager = ContextManager.getInstance();
//
                    answer.setEsmQ1(selectedRadioButtonText1);
                    answer.setEsmQ2(selectedRadioButtonText2);
                    answer.setEsmQ3(selectedRadioButtonText3);
                    answer.setEsmQ4(selectedRadioButtonText4);
                    answer.setEsmQ5(selectedRadioButtonText5);
                    answer.setEsmQ6(selectedRadioButtonText6);
//                    Log.d("d",selectedRadioButtonText3);
//
//                    // contextual data
//
//                    // location
//                    answer.setLocation(contextManager.locatoinLongtitude,
//                            contextManager.locatoinLatitude, contextManager.locatoinAccuracy);
//
//                    // status
//                    answer.isCharging = batteryManager.isCharging();
//                    answer.battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//                    answer.isScreenOn = powerManager.isInteractive();
//                    answer.isDeviceIdle = powerManager.isDeviceIdleMode();
//                    answer.isPowerSave = powerManager.isPowerSaveMode();
//                    answer.ringerMode = audioManager.getRingerMode();
//                    answer.callState = telephonyManager.getCallState();
//                    // recent app
//                    StringBuilder rappSB = new StringBuilder();
//                    List<UsageStats> recentApp;
//                    recentApp = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
//                            System.currentTimeMillis() - 5000,
//                            System.currentTimeMillis());
//                    for (UsageStats u : recentApp) {
//                        if (u.getLastTimeUsed() == 0) continue;
//                        rappSB.append(u.getPackageName());
//                        rappSB.append(" : ");
//                        rappSB.append(u.getLastTimeUsed() + "; ");
//                    }
//                    answer.recentApp = rappSB.toString();
//
//                    Network[] networks = connectivityManager.getAllNetworks();
//                    StringBuilder ntwSB = new StringBuilder();
//                    for (Network n : networks) {
//                        ntwSB.append(connectivityManager.getNetworkInfo(n));
//                    }
//                    answer.network = ntwSB.toString();
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        SignalStrength signalStrength = telephonyManager.getSignalStrength();
//                        try {
//                            List<CellSignalStrength> ss = signalStrength.getCellSignalStrengths();
//                            for (CellSignalStrength s : ss) {
//                                answer.signalType = s.toString();
//                                answer.signalDbm = s.getDbm();
//                            }
//                        } catch (NullPointerException e) {
//                            Log.e("signal strength", "getCellSignalStrength null pt", e);
//                        }
//                    } else {
//                        answer.signalType = contextManager.phoneSignalType;
//                        answer.signalDbm = contextManager.phoneSignalDbm;
//                    }
//
//                    // Sensors
////                    answer.setSensor(contextManager.accelerometer,
////                            contextManager.gyroscope,
////                            contextManager.gravity,
////                            contextManager.linearAcceleration,
////                            contextManager.rotationVector,
////                            contextManager.proximity,
////                            contextManager.magneticField,
////                            contextManager.light,
////                            contextManager.pressure,
////                            contextManager.relativeHumidity,
////                            contextManager.ambientTemperature);
//                    answer.light = contextManager.light;
//
//                    String json = SurveyManager.getInstance().getAnswer(answer);
//
//                    Log.i("ans in json:", json);
//
//                } else {  // scale not done or other err(not set on current)
//                    Log.d("answer handler", "scale not done");
//                }


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


}
