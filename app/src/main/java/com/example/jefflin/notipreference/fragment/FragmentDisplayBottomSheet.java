package com.example.jefflin.notipreference.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.helper.OnSheetDismissCallBack;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentDisplayBottomSheet extends BottomSheetDialogFragment {

    public boolean not_display_duplicate = false;
    public boolean not_display_not_relate = false;
    public boolean not_display_known = false;
    public boolean not_display_other = false;
    private OnSheetDismissCallBack onSheetDismissCallBack;
    private Context context;

    public FragmentDisplayBottomSheet(Context context) {
        this.context = context;
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
        final Button known = view.findViewById(R.id.not_display_1);
        final Button relate = view.findViewById(R.id.not_display_2);
        final Button other = view.findViewById(R.id.not_display_3);
        final EditText other_reason = view.findViewById(R.id.not_display_3_other);

        duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_display_duplicate){
                    duplicate.setBackgroundResource(R.drawable.check_small_round_button);
                    duplicate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    duplicate.setBackgroundResource(R.drawable.not_check_small_round_button);
                    duplicate.setTextColor(Color.parseColor("#000000"));
                }
                not_display_duplicate = !not_display_duplicate;
//                onSheetDismissCallBack.setNotDisplay(not_display_duplicate, not_display_not_relate, not_display_other);
                ((ActivitySurvey) context).refreshDisplaySort(not_display_duplicate, not_display_not_relate, not_display_known, not_display_other);
            }
        });

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_display_not_relate){
                    relate.setBackgroundResource(R.drawable.check_small_round_button);
                    relate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    relate.setBackgroundResource(R.drawable.not_check_small_round_button);
                    relate.setTextColor(Color.parseColor("#000000"));
                }
                not_display_not_relate = !not_display_not_relate;
//                onSheetDismissCallBack.setNotDisplay(not_display_duplicate, not_display_not_relate, not_display_other);
                ((ActivitySurvey) context).refreshDisplaySort(not_display_duplicate, not_display_not_relate, not_display_known, not_display_other);
            }
        });

        known.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_display_known){
                    known.setBackgroundResource(R.drawable.check_small_round_button);
                    known.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    known.setBackgroundResource(R.drawable.not_check_small_round_button);
                    known.setTextColor(Color.parseColor("#000000"));
                }
                not_display_known = !not_display_known;
//                onSheetDismissCallBack.setNotDisplay(not_display_duplicate, not_display_not_relate, not_display_other);
                ((ActivitySurvey) context).refreshDisplaySort(not_display_duplicate, not_display_not_relate, not_display_known, not_display_other);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_display_other){
                    other.setBackgroundResource(R.drawable.check_small_round_button);
                    other.setTextColor(Color.parseColor("#FFFFFF"));
                    other_reason.setVisibility(View.VISIBLE);
                } else {
                    other.setBackgroundResource(R.drawable.not_check_small_round_button);
                    other.setTextColor(Color.parseColor("#000000"));
                    other_reason.setVisibility(View.INVISIBLE);
                }
                not_display_other = !not_display_other;
//                onSheetDismissCallBack.setNotDisplay(not_display_duplicate, not_display_not_relate, not_display_other);
                ((ActivitySurvey) context).refreshDisplaySort(not_display_duplicate, not_display_not_relate, not_display_known, not_display_other);
            }
        });

        other_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    ((ActivitySurvey) context).refreshDisplaySortReason(editable.toString());
                }
            }
        });

        return view;
    }
}
