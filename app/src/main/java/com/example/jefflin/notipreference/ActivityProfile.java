package com.example.jefflin.notipreference;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.widgets.PushNotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityProfile extends AppCompatActivity {

    private ImageView iv_back;
    private TextView show_id;
    private EditText edit_id;
    private Button done;
    private Button test;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE);

        iv_back = (ImageView) findViewById(R.id.back);
        show_id = (TextView) findViewById(R.id.profile_show_id);
        edit_id = (EditText) findViewById(R.id.profile_edit_id);
        done = (Button) findViewById(R.id.profile_id_button);
        test = (Button) findViewById(R.id.profile_test);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.super.onBackPressed();
            }
        });

        show_id.setText(sharedPreferences.getString("ID", ""));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = edit_id.getText().toString();
                sharedPreferences.edit()
                        .putString("ID", user_id)
                        .commit();
                show_id.setText(sharedPreferences.getString("ID", ""));
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setExample();
                new PushNotification(ActivityProfile.this);
            }
        });


    }

    private void setExample() {
        Map<String, ArrayList<NotiItem>> map = new HashMap<>();
        ArrayList<NotiItem> listA = new ArrayList<>();
        ArrayList<NotiItem> listB = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            listA.add(new NotiItem("test", "test", "test", Calendar.getInstance().getTimeInMillis(), "test", i));
            listB.add(new NotiItem("test", "test", "test", Calendar.getInstance().getTimeInMillis(), "test", i));
        }
        map.put("click", listA);
        map.put("display", listB);

        SurveyManager.getInstance().setMap(map);
    }


}
