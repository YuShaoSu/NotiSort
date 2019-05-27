package com.example.jefflin.notipreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

/*
    Main activity, also the landing page
    has 1 bottom navigation bar and 1 top bar
 */

public class MainActivity extends AppCompatActivity {

    private NotiBroadcastReceiver notiBroadcastReceiver;

    // Fix: change this to private
    private static ArrayList<NotiItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBotNavView();

        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        //startActivity(intent);

        data = new ArrayList<>();

        notiBroadcastReceiver = new NotiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NotiListenerService.Arrival");
        registerReceiver(notiBroadcastReceiver,intentFilter);
    }

    private void setBotNavView(){
        final Intent intent = new Intent(this, SortActivity.class);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_navigation_home);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        break;
                    case R.id.action_sort:
                        //intent.putExtra("data", data);
                        startActivity(intent);
                        break;
                    case R.id.action_profile:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notiBroadcastReceiver);
    }

    public static ArrayList<NotiItem> getData(){
        return data;
    }

    // Fix: Change this to another class
    public class NotiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packagename = intent.getStringExtra("appname");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            Drawable icon = null;
            String appname = "";

            Toast.makeText(context, "Notification Received", Toast.LENGTH_LONG).show();
            Log.d("Noti", "received");

            PackageManager packageManager = context.getPackageManager();
            try {
                icon = packageManager.getApplicationIcon(packagename);
            } catch (Exception e) {
                Log.d("Rank","icon failed");
            }
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo( packagename, 0);
                appname = (String) (applicationInfo != null ?
                        packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
            } catch (Exception e) {
                Log.d("Rank","app name failed");
            }
            data.add(new NotiItem(icon, appname,title,content));
        }
    }
}