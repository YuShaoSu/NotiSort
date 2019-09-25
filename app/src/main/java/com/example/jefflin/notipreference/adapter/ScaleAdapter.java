package com.example.jefflin.notipreference.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.NotiItem;
import com.example.jefflin.notipreference.R;

import java.util.ArrayList;
import java.util.List;

public class ScaleAdapter extends RecyclerView.Adapter<ScaleAdapter.ViewHolder>  {

    private Context mContext;
    private List<NotiItem> mData;

    static final int strong_disagree = 0;
    static final int disagree = 1;
    static final int neutral = 2;
    static final int agree = 3;
    static final int strong_agree = 4;

//    private static final int strong_disagree0 = R.id.strong_disagree0;
//    private static final int strong_disagree1 = R.id.strong_disagree1;
//    private static final int strong_disagree2 = R.id.strong_disagree2;
//    private static final int strong_disagree3 = R.id.strong_disagree3;
//
//    private static final int disagree0 = R.id.disagree0;
//    private static final int disagree1 = R.id.disagree1;
//    private static final int disagree2 = R.id.disagree2;
//    private static final int disagree3 = R.id.disagree3;
//
//    private static final int neutral0 = R.id.neutral0;
//    private static final int neutral1 = R.id.neutral1;
//    private static final int neutral2 = R.id.neutral2;
//    private static final int neutral3 = R.id.neutral3;
//
//    private static final int agree0 = R.id.agree0;
//    private static final int agree1 = R.id.agree1;
//    private static final int agree2 = R.id.agree2;
//    private static final int agree3 = R.id.agree3;
//
//    private static final int strong_agree0 = R.id.strong_agree0;
//    private static final int strong_agree1 = R.id.strong_agree1;
//    private static final int strong_agree2 = R.id.strong_agree2;
//    private static final int strong_agree3 = R.id.strong_agree3;


    private RadioGroup lastCheckedRadioGroup = null;

    public ScaleAdapter(Context context, ArrayList<NotiItem> data){
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ScaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType){
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.scale_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScaleAdapter.ViewHolder holder, int position) {

        NotiItem notiItem = mData.get(position);

        holder.icon.setImageDrawable(notiItem.icon);
        holder.appname.setText(notiItem.appName);
        holder.title.setText(notiItem.title);
        holder.content.setText(notiItem.content);

        int id =(position+1)*100;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView appname;
        private TextView title;
        private TextView content;
        private RadioGroup scaleGroup0;
        private RadioGroup scaleGroup1;
        private RadioGroup scaleGroup2;
        private RadioGroup scaleGroup3;


        ViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            appname = (TextView) view.findViewById(R.id.appname);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);

            scaleGroup0 = (RadioGroup) view.findViewById(R.id.likert_scale0);
            scaleGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("checked button id", String.valueOf(group.getCheckedRadioButtonId()));
                    Log.d("get adapter position: ", String.valueOf(getAdapterPosition()));

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree0:
                            Log.d("strong agree in f0", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor0 = strong_agree;
                            break;
                        case R.id.agree0:
                            Log.d("agree in f0", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor0 = agree;
                            break;
                        case R.id.neutral0:
                            Log.d("neutral in f0", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor0 = neutral;
                            break;
                        case R.id.disagree0:
                            Log.d("disagree in f0", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor0 = disagree;
                            break;
                        case R.id.strong_disagree0:
                            Log.d("strong disagree in f0", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor0 = strong_disagree;
                            break;
                        default:
                            mData.get(getAdapterPosition()).factor0 = -1;
                    }

                }
            });

            scaleGroup1 = (RadioGroup) view.findViewById(R.id.likert_scale1);
            scaleGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("checked button id", String.valueOf(group.getCheckedRadioButtonId()));
                    Log.d("get adapter position: ", String.valueOf(getAdapterPosition()));

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree1:
                            Log.d("strong agree in f1", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor1 = strong_agree;
                            break;
                        case R.id.agree1:
                            Log.d("agree in f1", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor1 = agree;
                            break;
                        case R.id.neutral1:
                            Log.d("neutral in f1", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor1 = neutral;
                            break;
                        case R.id.disagree1:
                            Log.d("disagree in f1", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor1 = disagree;
                            break;
                        case R.id.strong_disagree1:
                            Log.d("strong disagree in f1", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor1 = strong_disagree;
                            break;
                        default:
                            mData.get(getAdapterPosition()).factor1 = -1;
                    }

                }

            });

            scaleGroup2 = (RadioGroup) view.findViewById(R.id.likert_scale2);
            scaleGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("checked button id", String.valueOf(group.getCheckedRadioButtonId()));
                    Log.d("get adapter position: ", String.valueOf(getAdapterPosition()));

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree2:
                            Log.d("strong agree in f2", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor2 = strong_agree;
                            break;
                        case R.id.agree2:
                            Log.d("agree in f2", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor2 = agree;
                            break;
                        case R.id.neutral2:
                            Log.d("neutral in f2", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor2 = neutral;
                            break;
                        case R.id.disagree2:
                            Log.d("disagree in f2", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor2 = disagree;
                            break;
                        case R.id.strong_disagree2:
                            Log.d("strong disagree in f2", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor2 = strong_disagree;
                            break;
                        default:
                            mData.get(getAdapterPosition()).factor2 = -1;
                    }

                }



            });

            scaleGroup3 = (RadioGroup) view.findViewById(R.id.likert_scale3);
            scaleGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d("checked button id", String.valueOf(group.getCheckedRadioButtonId()));
                    Log.d("get adapter position: ", String.valueOf(getAdapterPosition()));

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree3:
                            Log.d("strong agree in f3", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor3 = strong_agree;
                            break;
                        case R.id.agree3:
                            Log.d("agree in f3", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor3 = agree;
                            break;
                        case R.id.neutral3:
                            Log.d("neutral in f3", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor3 = neutral;
                            break;
                        case R.id.disagree3:
                            Log.d("disagree in f3", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor3 = disagree;
                            break;
                        case R.id.strong_disagree3:
                            Log.d("strong disagree in f3", String.valueOf(getAdapterPosition()));
                            mData.get(getAdapterPosition()).factor3 = strong_disagree;
                            break;
                        default:
                            mData.get(getAdapterPosition()).factor3 = -1;
                    }

                }
            });
        }
    }
}
