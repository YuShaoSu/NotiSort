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
import android.widget.Toast;

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
import java.util.Calendar;
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
    private TextView additional_title;
    private EditText additional_text;
    private boolean twoList;

    ArrayList<NotiItem> mActiveData;
    ArrayList<NotiItem> mActiveDataDisplay;

    Answer answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_compare, container, false);

        if (isAdded()) {
            mContext = getActivity();
        }

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
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        twoListItemsAdapter = new TwoListItemsAdapter(mContext, mActiveData, mActiveDataDisplay);
        recyclerView.setAdapter(twoListItemsAdapter);

        compare_title = rootView.findViewById(R.id.comapre_title);
        compare_text = rootView.findViewById(R.id.comapre_text);
        additional_title = rootView.findViewById(R.id.additional_title);
        additional_text = rootView.findViewById(R.id.additional_text);

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

                String additionalText = "null";
                additionalText = additional_text.getText().toString();

                if (answer.answerHandler(mActiveData, mActiveDataDisplay)) {
                    // scale check passed and done put notifications into answer
                    // now put ESM answer

                    answer.setSurveyFinishTime(Calendar.getInstance().getTimeInMillis());

                    answer.setEsmQ7(text);
                    answer.setEsmQ8(additionalText);

                    // contextual data


                    String json = SurveyManager.getInstance().getAnswer(answer);

                    Log.i("ans in json:", json);

//                    SurveyManager.getInstance().surveyDone();
                    ((ActivitySurvey) mContext).go_to_next();

                } else {  // scale not done or other err(not set on current)
//                    Log.d("answer handler", "scale not done");
                    Toast toast = Toast.makeText(mContext, "問卷尚未完成", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isAdded()){
            mContext = getActivity();
        }

        ESMQuestion q_data = (ESMQuestion) getArguments().getSerializable("data");
        textview_q_title.setText(q_data.getQuestionTitle());
        textview_q_discription.setText(q_data.getDescription());

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }
    }

    public void refreshAdapter() {
        twoList = twoListDiff();
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

    public boolean twoListDiff() {
        boolean diff = false;
        for (int i = 0; i < mActiveData.size(); i++) {
            if(mActiveDataDisplay.get(i).getDisplayOrder() == -9999 && mActiveData.get(i).getClickOrder() == -9999) {
                break;
            }
            if (mActiveData.get(i).origin_order != mActiveDataDisplay.get(i).origin_order) {
                diff = true;
                break;
            }
            if (mActiveData.get(i).getClickOrder() == -9999 || mActiveDataDisplay.get(i).getDisplayOrder() == -9999) {
                diff = true;
                break;
            }
        }
        return diff;
    }

}
