package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.ActivityESM;
import com.example.jefflin.notipreference.model.ESMQuestion;

import java.util.ArrayList;
import java.util.Random;

public class FragmentSort extends Fragment implements NotiItemMoveCallback.OnStartDragListener{

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;

    private RecyclerView mRecyclerView;
    private NotiItemAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private NotiItemMoveCallback.OnStartDragListener onStartDragListener = this;
    private ImageView iv_back;


    ArrayList<NotiItem> mData;
    ArrayList<NotiItem> mData_6 = new ArrayList<NotiItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sort, container, false);

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
                //ESMAnswer.getInstance().put_answer(textview_q_title.getText().toString(), editText_answer.getText().toString().trim());
                ((ActivityESM) mContext).go_to_next();
            }
        });
        setRecyclerView(rootView);
        setItemTouchHelper();

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

        //textview_q_title.setText(Html.fromHtml(q_data.getQuestionTitle()));
        //editText_answer.requestFocus();
        //InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(editText_answer, 0);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void setRecyclerView(ViewGroup rootView) {
        mData = NotiListenerService.getData();
        mData_6 = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");
        mAdapter = new NotiItemAdapter(getActivity(), mData_6, onStartDragListener);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_rank);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setItemTouchHelper() {
        mItemTouchHelper = new ItemTouchHelper(new NotiItemMoveCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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