package com.example.jefflin.notipreference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

public class SortActivity extends AppCompatActivity implements ItemMoveCallback.OnStartDragListener{
    private RecyclerView recyclerView;
    private NotiItemAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemMoveCallback.OnStartDragListener onStartDragListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
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
        mItemTouchHelper = new ItemTouchHelper(new ItemMoveCallback(adapter));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
