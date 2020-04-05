package com.example.jefflin.notipreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jefflin.notipreference.fragment.FragmentCompare;
import com.example.jefflin.notipreference.fragment.FragmentScale;
import com.example.jefflin.notipreference.fragment.FragmentESM;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.Answer;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.adapter.FragmentAdapter;
import com.example.jefflin.notipreference.fragment.FragmentEnd;
import com.example.jefflin.notipreference.fragment.FragmentSort;
import com.example.jefflin.notipreference.fragment.FragmentStart;
import com.example.jefflin.notipreference.model.ESMQuestion;
import com.example.jefflin.notipreference.model.ESMPojo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivitySurvey extends AppCompatActivity {

    private ESMPojo mESMPojo;
    private ViewPager mPager;
    private String style_string = null;

    ArrayList<NotiItem> mActiveData = new ArrayList<NotiItem>();
    ArrayList<NotiItem> mCurrentData = new ArrayList<NotiItem>();
    ArrayList<NotiItem> mActiveDataDisplay = new ArrayList<NotiItem>();
    Answer answer;

    FragmentScale fragscale = new FragmentScale();
    FragmentESM fragsurvey;
    FragmentCompare fragCompare;
    FragmentSort fragsort;
    FragmentSort fragsort_display;

    public ArrayList<NotiItem> getCurrentData() {
        return mCurrentData;
    }

    public void putCurrentData(ArrayList<NotiItem> currentData) {
        mCurrentData = currentData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivitySurvey", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_survey);
        answer = SurveyManager.getInstance().getFromAnswerList(selectAnswer());

        if (!SurveyManager.getInstance().isNotiNull()) {
            mActiveData = SurveyManager.getInstance().getMap().get("click");
            mActiveDataDisplay = SurveyManager.getInstance().getMap().get("display");
        }
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mESMPojo = new Gson().fromJson(bundle.getString("json_survey"), ESMPojo.class);
            if (bundle.containsKey("style")) {
                style_string = bundle.getString("style");
            }
        }

        final ArrayList<Fragment> arraylist_fragments = new ArrayList<>();

        //- START -
        if (!mESMPojo.getESMProperties().getSkipIntro()) {
            FragmentStart frag_start = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable("survery_properties", mESMPojo.getESMProperties());
            sBundle.putString("style", style_string);
            frag_start.setArguments(sBundle);
            arraylist_fragments.add(frag_start);
        }

        //- FILL -
        for (ESMQuestion mESMQuestion : mESMPojo.getESMQuestions()) {

            if (mESMQuestion.getQuestionType().equals("Sort")) {
                fragsort = new FragmentSort();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                xBundle.putSerializable("arrayList", mActiveData);
                xBundle.putInt("sortType", 0);
                fragsort.setArguments(xBundle);
                arraylist_fragments.add(fragsort);
            }

            if (mESMQuestion.getQuestionType().equals("displaySort")) {
                fragsort_display = new FragmentSort();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                xBundle.putSerializable("arrayList", mActiveDataDisplay);
                xBundle.putInt("sortType", 1);
                fragsort_display.setArguments(xBundle);
                arraylist_fragments.add(fragsort_display);
            }

            if (mESMQuestion.getQuestionType().equals("Scale")) {
                fragscale = new FragmentScale();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                xBundle.putSerializable("arrayList", mActiveData);
                fragscale.setArguments(xBundle);
                arraylist_fragments.add(fragscale);
            }

            if (mESMQuestion.getQuestionType().equals("Survey")) {
                fragsurvey = new FragmentESM();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putSerializable("answer", answer);
                xBundle.putString("style", style_string);
                fragsurvey.setArguments(xBundle);
                arraylist_fragments.add(fragsurvey);
            }

            if (mESMQuestion.getQuestionType().equals("Compare")) {
                fragCompare = new FragmentCompare();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putSerializable("arrayList", mActiveData);
                xBundle.putSerializable("arrayListDisplay", mActiveDataDisplay);
                xBundle.putSerializable("answer", answer);
                xBundle.putString("style", style_string);
                fragCompare.setArguments(xBundle);
                arraylist_fragments.add(fragCompare);
            }

        }

        //- END -
        FragmentEnd frag_end = new FragmentEnd();
        Bundle eBundle = new Bundle();
        eBundle.putSerializable("survery_properties", mESMPojo.getESMProperties());
        eBundle.putString("style", style_string);
        frag_end.setArguments(eBundle);
        arraylist_fragments.add(frag_end);

        mPager = (ViewPager) findViewById(R.id.pager);
        FragmentAdapter mPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), arraylist_fragments);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ActivitySurvey", "onResume");
        SurveyManager.getInstance().setSurveyDoing(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivitySurvey", "onPause");
//        SurveyManager.getInstance().surveyUnblock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void go_to_next() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        fragscale.changeActiveData();
    }

    public void refreshAttendSort(boolean info, boolean use, boolean other) { fragsort.refreshAttendAdapter(info, use, other); }

    public void refreshDisplaySort(boolean dup, boolean relate, boolean other) { fragsort_display.refreshDisplayAdapter(dup, relate, other); }

    public void refreshFragsurvey() {
        fragCompare.refreshAdapter();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void event_survey_completed(boolean sync_now) {
        Intent returnIntent = new Intent();
        setResult(sync_now ? Activity.RESULT_OK : Activity.RESULT_FIRST_USER, returnIntent);
        finish();
    }

    private int hourDifference(Long d1, Long d2) {
        long diff = Math.abs(d1 - d2);
        return (int) (diff / (60 * 60 * 1000));
    }

    private int selectAnswer() {
        long diff = Calendar.getInstance().getTimeInMillis() - SurveyManager.getInstance().getSurveyPostTime();
        Log.d("answer list", "choose" + Math.floor(diff / (60 * 1000)));
        return (int) Math.floor(diff / (60 * 1000));
    }
}
