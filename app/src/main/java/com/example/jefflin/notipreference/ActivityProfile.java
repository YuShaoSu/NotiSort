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

import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.adapter.NotiItemAdapter;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;

import java.util.ArrayList;

public class ActivityProfile extends AppCompatActivity {

    private ImageView iv_back;
    private TextView show_id;
    private EditText edit_id;
    private Button done;
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


    }


}
