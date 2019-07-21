package com.example.jefflin.notipreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.jefflin.notipreference.services.NotiListenerService;
import com.example.jefflin.notipreference.adapter.FragmentAdapter;
import com.example.jefflin.notipreference.fragment.FragmentCheckboxes;
import com.example.jefflin.notipreference.fragment.FragmentEnd;
import com.example.jefflin.notipreference.fragment.FragmentMultiline;
import com.example.jefflin.notipreference.fragment.FragmentRadioboxes;
import com.example.jefflin.notipreference.fragment.FragmentSort;
import com.example.jefflin.notipreference.fragment.FragmentStart;
import com.example.jefflin.notipreference.fragment.FragmentTextSimple;
import com.example.jefflin.notipreference.model.ESMQuestion;
import com.example.jefflin.notipreference.model.ESMPojo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class ActivityESM extends AppCompatActivity {

    private ESMPojo mESMPojo;
    private ViewPager mPager;
    private String style_string = null;

    ArrayList<NotiItem> mData;
    ArrayList<NotiItem> mData_6 = new ArrayList<NotiItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_survey);

        mData = NotiListenerService.getData();
        mData_6 = getRandom6Element(mData);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mESMPojo = new Gson().fromJson(bundle.getString("json_survey"), ESMPojo.class);
            if (bundle.containsKey("style")) {
                style_string = bundle.getString("style");
            }
        }


        Log.i("json Object = ", String.valueOf(mESMPojo.getESMQuestions()));

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
        FragmentSort fragsort = new FragmentSort();

        //- FILL -
        for (ESMQuestion mESMQuestion : mESMPojo.getESMQuestions()) {

            if (mESMQuestion.getQuestionType().equals("Sort")) {
                fragsort = new FragmentSort();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                xBundle.putSerializable("arrayList", mData_6);
                fragsort.setArguments(xBundle);
                arraylist_fragments.add(fragsort);
            }

            if (mESMQuestion.getQuestionType().equals("String")) {
                FragmentTextSimple frag = new FragmentTextSimple();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mESMQuestion.getQuestionType().equals("Checkboxes")) {
                FragmentCheckboxes frag = new FragmentCheckboxes();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mESMQuestion.getQuestionType().equals("Radioboxes")) {
                FragmentRadioboxes frag = new FragmentRadioboxes();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mESMQuestion.getQuestionType().equals("StringMultiline")) {
                FragmentMultiline frag = new FragmentMultiline();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mESMQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
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
    protected void onDestroy() {
        super.onDestroy();
        // put back 6 notis
        NotiListenerService.putData(mData_6);
    }

    public void go_to_next() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void event_survey_completed(ESMAnswer instance) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("answers", instance.get_json_object());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public ArrayList<NotiItem> getRandom6Element(ArrayList<NotiItem> list) {
        // randomly select 6 notifications
        Random rand = new Random();
        ArrayList<NotiItem> newList = new ArrayList<>();
        ArrayList<NotiItem> list2 = list;

        for (int i = 0; i < 6; i++) {
            try {
                int randomIndex = rand.nextInt(list2.size());
                newList.add(list2.get(randomIndex));
                list2.remove(randomIndex);
            } catch (Exception e) {
                Log.d("d","not enough notifications here");
            }
        }
        return newList;
    }
}