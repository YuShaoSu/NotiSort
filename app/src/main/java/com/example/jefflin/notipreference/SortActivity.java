package com.example.jefflin.notipreference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SortActivity extends AppCompatActivity implements NotiItemMoveCallback.OnStartDragListener{
    private RecyclerView recyclerView;
    private NotiItemAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private NotiItemMoveCallback.OnStartDragListener onStartDragListener = this;

    private ImageView iv_back;
    private TextView tv_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        iv_back = (ImageView) findViewById(R.id.back);
        tv_next = (TextView) findViewById(R.id.next);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortActivity.super.onBackPressed();
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortActivity.super.onBackPressed();
            }
        });

        setRecyclerView();
        setItemTouchHelper();
    }

    private void setRecyclerView() {
        ArrayList<NotiItem> data;
        data = MainActivity.getData();
        adapter = new NotiItemAdapter(this, data, onStartDragListener);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_rank);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setItemTouchHelper() {
        mItemTouchHelper = new ItemTouchHelper(new NotiItemMoveCallback(adapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
