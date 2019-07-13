package com.example.jefflin.notipreference.fragment;

import android.app.Service;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jefflin.notipreference.Answers;
import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.NotiItemAdapter;
import com.example.jefflin.notipreference.NotiItemMoveCallback;
import com.example.jefflin.notipreference.NotiListenerService;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.SurveyActivity;
import com.example.jefflin.notipreference.models.Question;

import java.util.ArrayList;
import java.util.Random;

public class FragmentSort extends Fragment implements NotiItemMoveCallback.OnStartDragListener{

    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private EditText editText_answer;

    private RecyclerView mRecyclerView;
    private NotiItemAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private NotiItemMoveCallback.OnStartDragListener onStartDragListener = this;
    private ImageView iv_back;
    private TextView tv_next;


    ArrayList<NotiItem> mData;
    ArrayList<NotiItem> mData_6 = new ArrayList<NotiItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sort, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        editText_answer = (EditText) rootView.findViewById(R.id.editText_answer);

        iv_back = (ImageView) rootView.findViewById(R.id.back);

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Answers.getInstance().put_answer(textview_q_title.getText().toString(), editText_answer.getText().toString().trim());
                ((SurveyActivity) mContext).go_to_next();
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
        Question q_data = (Question) getArguments().getSerializable("data");

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
            editText_answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 3) {
                        button_continue.setVisibility(View.VISIBLE);
                    } else {
                        button_continue.setVisibility(View.GONE);
                    }
                }
            });
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
        mData_6 = getRandom6Element(mData);
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