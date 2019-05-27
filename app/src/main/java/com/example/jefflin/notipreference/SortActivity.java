package com.example.jefflin.notipreference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

public class SortActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotiItemAdapter adapter;


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
        adapter = new NotiItemAdapter(this, data);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_rank);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setItemTouchHelper() {
        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

}
