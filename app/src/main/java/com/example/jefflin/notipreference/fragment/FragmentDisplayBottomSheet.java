package com.example.jefflin.notipreference.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jefflin.notipreference.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentDisplayBottomSheet extends BottomSheetDialogFragment {

    public boolean not_display_duplicate = false;
    public boolean not_display_not_relate = false;
    public boolean not_display_other = false;


    public FragmentDisplayBottomSheet() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_bottom_sheet, container, false);

        final Button duplicate = view.findViewById(R.id.not_display_0);
        final Button relate = view.findViewById(R.id.not_display_1);
        final Button other = view.findViewById(R.id.not_display_2);

        duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("duplicate", "check");
                if(!not_display_duplicate){
                    duplicate.setBackgroundResource(R.drawable.check_small_round_button);
                    duplicate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    duplicate.setBackgroundResource(R.drawable.not_check_small_round_button);
                    duplicate.setTextColor(Color.parseColor("#000000"));
                }
                not_display_duplicate = !not_display_duplicate;
            }
        });

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("relate", "check");
                if(!not_display_not_relate){
                    relate.setBackgroundResource(R.drawable.check_small_round_button);
                    relate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    relate.setBackgroundResource(R.drawable.not_check_small_round_button);
                    relate.setTextColor(Color.parseColor("#000000"));
                }
                not_display_not_relate = !not_display_not_relate;
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("other", "check");
                if(!not_display_other){
                    other.setBackgroundResource(R.drawable.check_small_round_button);
                    other.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    other.setBackgroundResource(R.drawable.not_check_small_round_button);
                    other.setTextColor(Color.parseColor("#000000"));
                }
                not_display_other = !not_display_other;
            }
        });

        return view;
    }
}
