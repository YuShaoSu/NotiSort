package com.example.jefflin.notipreference.fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivityESM;
import com.example.jefflin.notipreference.ESMAnswer;
import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.adapter.ScaleAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.model.ESMQuestion;
import com.example.jefflin.notipreference.services.NotiListenerService;

import java.util.ArrayList;

public class FragmentSurvey extends Fragment  {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_survey, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.title);
        textview_q_discription = (TextView) rootView.findViewById(R.id.discription);

        iv_back = (ImageView) rootView.findViewById(R.id.back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                ESMAnswer.getInstance().put_answer("1. 身邊有人嗎？", selectedRadioButtonText1);
                ESMAnswer.getInstance().put_answer("2. 你在哪裡？", selectedRadioButtonText2);
                ESMAnswer.getInstance().put_answer("3. 你在進行什麼活動?", checkedText3);
                ESMAnswer.getInstance().put_answer("4. 你很投入活動?", selectedRadioButtonText4);
                ESMAnswer.getInstance().put_answer("5. 你很忙碌?", selectedRadioButtonText5);
                ESMAnswer.getInstance().put_answer("6. 你的心情?", selectedRadioButtonText6);

                ((ActivityESM) mContext).go_to_next();
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
