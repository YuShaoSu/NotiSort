package com.example.jefflin.notipreference.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
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

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.GlobalClass;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.model.ESMQuestion;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentESM extends Fragment  {

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;
    private ImageView iv_back;

    private RadioGroup radioGroupQ1;
    private RadioGroup radioGroupQ2;
    private EditText editTextQ2;
    private CheckBox checkBoxQ3_1;
    private CheckBox checkBoxQ3_2;
    private CheckBox checkBoxQ3_3;
    private CheckBox checkBoxQ3_4;
    private CheckBox checkBoxQ3_5;
    private CheckBox checkBoxQ3_6;
    private CheckBox checkBoxQ3_7;
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

    ArrayList<NotiItem> mActiveData;
    ArrayList<NotiItem> mActiveDataDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_esm, container, false);

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

        radioGroupQ1 = rootView.findViewById(R.id.q1);
        radioGroupQ2 = rootView.findViewById(R.id.q2);
        editTextQ2 = rootView.findViewById(R.id.q2_other);
        checkBoxQ3_1 = rootView.findViewById(R.id.q3_1);
        checkBoxQ3_2 = rootView.findViewById(R.id.q3_2);
        checkBoxQ3_3 = rootView.findViewById(R.id.q3_3);
        checkBoxQ3_4 = rootView.findViewById(R.id.q3_4);
        checkBoxQ3_5 = rootView.findViewById(R.id.q3_5);
        checkBoxQ3_6 = rootView.findViewById(R.id.q3_6);
        checkBoxQ3_7 = rootView.findViewById(R.id.q3_7);
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
        checkBoxQ3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                q3_selected = true;
                if (q1_selected && q2_selected && q3_selected && q4_selected && q5_selected && q6_selected) {
                    button_continue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button_continue.setEnabled(true);
                }
            }
        });
        checkBoxQ3_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                RadioButton selectedRadioButton2 = (RadioButton) rootView.findViewById(radioGroupQ2.getCheckedRadioButtonId());
                String selectedRadioButtonText2 = selectedRadioButton2.getText().toString();
                if (selectedRadioButtonText2.equals("其他")) {
                    selectedRadioButtonText2 = selectedRadioButtonText2 + ":" + editTextQ2.getText().toString();
                }
                String checkedText3_1 = "";
                String checkedText3_2 = "";
                String checkedText3_3 = "";
                String checkedText3_4 = "";
                String checkedText3_5 = "";
                String checkedText3_6 = "";
                String checkedText3_7 = "";
                String checkedText3 = "";
                if (checkBoxQ3_1.isChecked()) {
                    checkedText3_1 = checkBoxQ3_1.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_1 + ", ";
                } if (checkBoxQ3_2.isChecked()) {
                    checkedText3_2 = checkBoxQ3_2.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_2 + ", ";
                } if (checkBoxQ3_3.isChecked()) {
                    checkedText3_3 = checkBoxQ3_3.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_3 + ", ";
                } if (checkBoxQ3_4.isChecked()) {
                    checkedText3_4 = checkBoxQ3_4.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_4 + ", ";
                } if (checkBoxQ3_5.isChecked()) {
                    checkedText3_5 = checkBoxQ3_5.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_5 + ", ";
                } if (checkBoxQ3_6.isChecked()) {
                    checkedText3_6 = checkBoxQ3_6.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_6 + ", ";
                } if (checkBoxQ3_7.isChecked()) {
                    checkedText3_7 = checkBoxQ3_7.getText().toString();
                    checkedText3 = checkedText3 + checkedText3_7 + ":" + editTextQ3.getText().toString() + ", ";
                }
                checkedText3 = checkedText3.substring(0, checkedText3.length() - 2);

                RadioButton selectedRadioButton4 = (RadioButton) rootView.findViewById(radioGroupQ4.getCheckedRadioButtonId());
                String selectedRadioButtonText4 = selectedRadioButton4.getTag().toString();
                RadioButton selectedRadioButton5 = (RadioButton) rootView.findViewById(radioGroupQ5.getCheckedRadioButtonId());
                String selectedRadioButtonText5 = selectedRadioButton5.getTag().toString();
                RadioButton selectedRadioButton6 = (RadioButton) rootView.findViewById(radioGroupQ6.getCheckedRadioButtonId());
                String selectedRadioButtonText6 = selectedRadioButton6.getTag().toString();


                // handle the answers of previous
                Answer answer = new Answer(GlobalClass.getDeviceID(), SurveyManager.getInstance().getSurveyPostTime(), Calendar.getInstance().getTimeInMillis(), 0);
                if(answer.answerHandler(mActiveData, mActiveDataDisplay)){
                    // scale check passed and done put notifications into answer
                    // now put ESM answer
                    AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
                    BatteryManager batteryManager = (BatteryManager)mContext.getSystemService(Context.BATTERY_SERVICE);
                    PowerManager powerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);


                    answer.setEsmQ1(selectedRadioButtonText1);
                    answer.setEsmQ2(selectedRadioButtonText2);
                    answer.setEsmQ3(checkedText3);
                    answer.setEsmQ4(selectedRadioButtonText4);
                    answer.setEsmQ5(selectedRadioButtonText5);
                    answer.setEsmQ6(selectedRadioButtonText6);

                    // contextual data
                    SurveyManager.getInstance().setCurrentLocation(ContextManager.getInstance().locatoinLatitude,
                            ContextManager.getInstance().locatoinLongtitude, ContextManager.getInstance().locatoinAccuracy);
                    SurveyManager.getInstance().setRingerMode(audioManager.getRingerMode());
                    answer.setContext(SurveyManager.getInstance().getLocation(), ContextManager.getInstance().locatoinAccuracy,
                            SurveyManager.getInstance().getRingerMode());
                    answer.setBattery(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY), batteryManager.isCharging());
                    answer.setStatus(powerManager.isInteractive(), powerManager.isDeviceIdleMode(), powerManager.isPowerSaveMode());

                    String json = SurveyManager.getInstance().getAnswer(answer);

                    Log.i("ans in json:", json);

                }
                else {  // scale not done or other err(not set on current)
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

        // for survey answer handle
        mActiveData = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");
        mActiveDataDisplay = (ArrayList<NotiItem>) getArguments().getSerializable("arrayListDisplay");

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }
    }
}
