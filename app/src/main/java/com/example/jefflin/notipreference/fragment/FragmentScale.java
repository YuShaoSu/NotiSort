package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivityESM;
import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.adapter.ScaleAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.model.ESMQuestion;
import com.example.jefflin.notipreference.services.NotiListenerService;

import java.util.ArrayList;

public class FragmentScale extends Fragment  {

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;

    private RecyclerView mRecyclerView;
    private ScaleAdapter mAdapter;
    private ImageView iv_back;


    ArrayList<NotiItem> mData;
    ArrayList<NotiItem> mData_6 = new ArrayList<NotiItem>();
    ArrayList<NotiItem> mActiveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_scale, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.title);
        textview_q_discription = (TextView) rootView.findViewById(R.id.discription);

        iv_back = (ImageView) rootView.findViewById(R.id.back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityESM) mContext).go_to_next();
            }
        });
        setRecyclerView(rootView);
//        setItemTouchHelper();

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

    private void setRecyclerView(ViewGroup rootView) {
        mData = NotiListenerService.getData();
        mData_6 = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");
        mActiveData = NotiListenerService.getActiveNotis();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_scale);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new ScaleAdapter(getActivity(), mActiveData);
        mRecyclerView.setAdapter(mAdapter);
    }



}
