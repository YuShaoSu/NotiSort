package com.example.jefflin.notipreference;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/*
    Main activity, also the landing page
    has 1 bottom navigation bar and 1 top bar
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBotNavView();

        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        // this line open the setting of the phone
        // startActivity(intent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NotiListenerService.Arrival");
    }

    private void setBotNavView(){
        final Intent intent = new Intent(this, SortActivity.class);
        final Intent intent_history = new Intent(this, HistoryActivity.class);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bot_navigation_home);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_help:
                        break;
                    case R.id.action_sort:
                        startActivity(intent);
                        break;
                    case R.id.action_profile:
                        startActivity(intent_history);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}