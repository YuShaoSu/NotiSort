package com.example.jefflin.notipreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScaleActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);

        iv_back = (ImageView) findViewById(R.id.back);
        tv_next = (TextView) findViewById(R.id.next);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleActivity.super.onBackPressed();
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EsmActivity.class);
                startActivity(intent);
            }
        });
    }
}