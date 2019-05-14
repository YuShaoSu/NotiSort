package com.example.jefflin.notipreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotiItemAdapter adapter;
    private NotiListenerService notiListenerService;
    private ArrayList<NotiItem> data;
    private ArrayList<NotiItem> data2;
    private NotiBroadcastReceiver notiBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        notiBroadcastReceiver = new NotiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("NotiListenerService.Arrival");
        registerReceiver(notiBroadcastReceiver,intentFilter);

        go();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notiBroadcastReceiver);
    }

    private void go() {
        data = new ArrayList<>();
        adapter = new NotiItemAdapter(this, data);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public class NotiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String appname = intent.getStringExtra("appname");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");

            Toast.makeText(context, "Notification Received", Toast.LENGTH_LONG).show();
            Log.d("Noti", "received");

            try {
                PackageManager packageManager = context.getPackageManager();
                //ApplicationInfo applicationInfo = packageManager.getApplicationInfo( appname, 0);
                //appname = (String) (applicationInfo != null ?
                //        packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
                Drawable icon = packageManager.getApplicationIcon(appname);
                data.add(new NotiItem(icon, appname,title,content));
            } catch (Exception e) {
                Log.d("e","eee");
            }

            //data.add(new NotiItem(icon, appname,title,content));
            recyclerView.setAdapter(adapter);
        }
    }
}
