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

public class FragmentAttendBottomSheet extends BottomSheetDialogFragment {

    public boolean not_attend_need_no_info = false;
    public boolean not_attend_no_use = false;
    public boolean not_attend_not_relate = false;
    public boolean not_attend_other = false;
    private OnSheetDismissCallBack onSheetDismissCallBack;
    private Context context;

    public FragmentAttendBottomSheet(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attend_bottom_sheet, container, false);

        final Button info = view.findViewById(R.id.not_attend_0);
        final Button use = view.findViewById(R.id.not_attend_1);
        final Button other = view.findViewById(R.id.not_attend_2);
        final Button relate = view.findViewById(R.id.not_attend_3);
        final EditText other_reason = view.findViewById(R.id.not_attend_2_other);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_attend_need_no_info){
                    info.setBackgroundResource(R.drawable.check_small_round_button);
                    info.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    info.setBackgroundResource(R.drawable.not_check_small_round_button);
                    info.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_need_no_info = !not_attend_need_no_info;
//                onSheetDismissCallBack.setNotAttend(not_attend_need_no_info, not_attend_no_use, not_attend_other);
                ((ActivitySurvey) context).refreshAttendSort(not_attend_need_no_info, not_attend_no_use, not_attend_not_relate, not_attend_other);
            }
        });

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_attend_no_use){
                    use.setBackgroundResource(R.drawable.check_small_round_button);
                    use.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    use.setBackgroundResource(R.drawable.not_check_small_round_button);
                    use.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_no_use = !not_attend_no_use;
//                onSheetDismissCallBack.setNotAttend(not_attend_need_no_info, not_attend_no_use, not_attend_other);
                ((ActivitySurvey) context).refreshAttendSort(not_attend_need_no_info, not_attend_no_use, not_attend_not_relate, not_attend_other);
            }
        });

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_attend_not_relate){
                    relate.setBackgroundResource(R.drawable.check_small_round_button);
                    relate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    relate.setBackgroundResource(R.drawable.not_check_small_round_button);
                    relate.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_not_relate = !not_attend_not_relate;
//                onSheetDismissCallBack.setNotAttend(not_attend_need_no_info, not_attend_no_use, not_attend_other);
                ((ActivitySurvey) context).refreshAttendSort(not_attend_need_no_info, not_attend_no_use, not_attend_not_relate, not_attend_other);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!not_attend_other){
                    other.setBackgroundResource(R.drawable.check_small_round_button);
                    other.setTextColor(Color.parseColor("#FFFFFF"));
                    other_reason.setVisibility(View.VISIBLE);
                } else {
                    other.setBackgroundResource(R.drawable.not_check_small_round_button);
                    other.setTextColor(Color.parseColor("#000000"));
                    other_reason.setVisibility(View.INVISIBLE);
                }
                not_attend_other = !not_attend_other;
//                onSheetDismissCallBack.setNotAttend(not_attend_need_no_info, not_attend_no_use, not_attend_other);
                ((ActivitySurvey) context).refreshAttendSort(not_attend_need_no_info, not_attend_no_use, not_attend_not_relate, not_attend_other);
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
                    ((ActivitySurvey) context).refreshAttendSortReason(editable.toString());
                }
            }
        });

        return view;
    }
}
