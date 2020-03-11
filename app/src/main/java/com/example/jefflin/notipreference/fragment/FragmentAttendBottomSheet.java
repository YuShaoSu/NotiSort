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

public class FragmentAttendBottomSheet extends BottomSheetDialogFragment {

    public boolean not_attend_need_no_info = false;
    public boolean not_attend_no_use = false;
    public boolean not_attend_other = false;

    public FragmentAttendBottomSheet() {
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

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("info", "check");
                if(!not_attend_need_no_info){
                    info.setBackgroundResource(R.drawable.check_small_round_button);
                    info.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    info.setBackgroundResource(R.drawable.not_check_small_round_button);
                    info.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_need_no_info = !not_attend_need_no_info;
            }
        });

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("use", "check");
                if(!not_attend_no_use){
                    use.setBackgroundResource(R.drawable.check_small_round_button);
                    use.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    use.setBackgroundResource(R.drawable.not_check_small_round_button);
                    use.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_no_use = !not_attend_no_use;
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("other", "check");
                if(!not_attend_other){
                    other.setBackgroundResource(R.drawable.check_small_round_button);
                    other.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    other.setBackgroundResource(R.drawable.not_check_small_round_button);
                    other.setTextColor(Color.parseColor("#000000"));
                }
                not_attend_other = !not_attend_other;
            }
        });

        return view;
    }
}
