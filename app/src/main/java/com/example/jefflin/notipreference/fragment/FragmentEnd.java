package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.ESMProperties;

public class FragmentEnd extends Fragment {

    private FragmentActivity mContext;
    private TextView textView_end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_end, container, false);

        Button button_finish = (Button) rootView.findViewById(R.id.button_sync_now);
        textView_end = (TextView) rootView.findViewById(R.id.textView_end);


        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ActivitySurvey) mContext).event_survey_completed(SurveyManager.getInstance());

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        ESMProperties survery_properties = (ESMProperties) getArguments().getSerializable("survery_properties");

        assert survery_properties != null;
        textView_end.setText(Html.fromHtml(survery_properties.getEndMessage()));
    }
}