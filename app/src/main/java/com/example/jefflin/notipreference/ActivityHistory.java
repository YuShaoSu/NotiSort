package com.example.jefflin.notipreference;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.services.NotiListenerService;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;

import java.util.ArrayList;

public class ActivityHistory extends AppCompatActivity implements NotiItemMoveCallback.OnStartDragListener{
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
                ActivityHistory.super.onBackPressed();
            }
        });

        setRecyclerView();
        setItemTouchHelper();
    }

    private void setRecyclerView() {
//        mData = NotiListenerService.getActiveNotis().get("click");
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
