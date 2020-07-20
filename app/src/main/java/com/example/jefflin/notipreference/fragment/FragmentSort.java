package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jefflin.notipreference.helper.OnSheetDismissCallBack;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.model.ESMQuestion;

import java.util.ArrayList;

public class FragmentSort extends Fragment implements NotiItemMoveCallback.OnStartDragListener {

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private TextView textview_q_discription;

    private RecyclerView mRecyclerView;
    private NotiItemAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private NotiItemMoveCallback.OnStartDragListener onStartDragListener = this;
//    private OnSheetDismissCallBack onSheetDismissCallBack = this;
    private ImageView iv_back;

    private int sortType;

    ArrayList<NotiItem> mData;
    ArrayList<NotiItem> mData_6 = new ArrayList<NotiItem>();
    ArrayList<NotiItem> mActiveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sort, container, false);

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

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SurveyAnswer.getInstance().put_answer(textview_q_title.getText().toString(), editText_answer.getText().toString().trim());
                ((ActivitySurvey) mContext).putCurrentData(mActiveData);
                if (getArguments().getInt("sortType") == 1)
                    ((ActivitySurvey) mContext).refreshFragsurvey();
                ((ActivitySurvey) mContext).go_to_next();
            }
        });
        setRecyclerView(rootView);
        setItemTouchHelper();

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
        sortType = getArguments().getInt("sortType");
        mActiveData = (ArrayList<NotiItem>) getArguments().getSerializable("arrayList");
        mAdapter = new NotiItemAdapter(mContext, mActiveData, onStartDragListener, sortType);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_rank);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setItemTouchHelper() {
        mItemTouchHelper = new ItemTouchHelper(new NotiItemMoveCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void refreshAttendAdapter(boolean info, boolean use, boolean relate, boolean other) {
        mAdapter.setNotAttend(info, use, relate, other);
    }
    public void refreshAttendAdapterReason(String reason) {
        mAdapter.setNotAttendReason(reason);
    }
    public void refreshDisplayAdapter(boolean dup, boolean relate, boolean known, boolean other) {
        mAdapter.setNotDisplay(dup, relate, known, other);
    }
    public void refreshDisplayAdapterReason(String reason) {
        mAdapter.setNotDisplayReason(reason);
    }
}