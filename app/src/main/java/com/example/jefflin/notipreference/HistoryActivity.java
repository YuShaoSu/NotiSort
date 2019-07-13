package com.example.jefflin.notipreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements NotiItemMoveCallback.OnStartDragListener{
    private RecyclerView mRecyclerView;
    private NotiItemAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private NotiItemMoveCallback.OnStartDragListener onStartDragListener = this;

    ArrayList<NotiItem> mData;

    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        iv_back = (ImageView) findViewById(R.id.back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.super.onBackPressed();
            }
        });

        setRecyclerView();
        setItemTouchHelper();
    }

    private void setRecyclerView() {
        mData = NotiListenerService.getData();
        mAdapter = new NotiItemAdapter(this, mData, onStartDragListener);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_rank);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setItemTouchHelper() {
        mItemTouchHelper = new ItemTouchHelper(new NotiItemMoveCallback(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
