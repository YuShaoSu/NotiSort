package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.adapter.ScaleAdapter;
import com.example.jefflin.notipreference.model.ESMQuestion;

import java.util.ArrayList;

public class FragmentScale extends Fragment  {

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;
    private EditText likert_scale_reason;

    private RecyclerView mRecyclerView;
    private ScaleAdapter mAdapter;
    private ImageView iv_back;


    ArrayList<NotiItem> mActiveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_scale, container, false);

        if (isAdded()) {
            mContext = getActivity();
        }

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

        likert_scale_reason = rootView.findViewById(R.id.likert_scale_reason_box);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivitySurvey) mContext).go_to_next();
            }
        });
        setRecyclerView(rootView);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isAdded()) {
            mContext = getActivity();
        }
        ESMQuestion q_data = (ESMQuestion) getArguments().getSerializable("data");
        textview_q_title.setText(q_data.getQuestionTitle());
        textview_q_discription.setText(q_data.getDescription());

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }

    }

    private void setRecyclerView(ViewGroup rootView) {
        mActiveData = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_scale);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new ScaleAdapter(mContext, mActiveData);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void changeActiveData() {
        mActiveData = ((ActivitySurvey) mContext).getCurrentData();
        mAdapter.notifyDataSetChanged();
    }
}
