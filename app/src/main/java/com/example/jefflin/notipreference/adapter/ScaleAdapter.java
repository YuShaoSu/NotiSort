package com.example.jefflin.notipreference.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.helper.IconHandler;

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
    static final int not_apply = 5;

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

        Resources resources = mContext.getResources();
        IconHandler iconHandler = new IconHandler();
        Bitmap icon_ = iconHandler.loadImageFromStorage(notiItem.icon, notiItem.appName);
        Drawable icon = new BitmapDrawable(resources, icon_);

        holder.icon.setImageDrawable(icon);
        holder.appName.setText(notiItem.appName);
        holder.title.setText(notiItem.title);
        holder.content.setText(notiItem.content);
        holder.position.setText(String.valueOf(position + 1));

        if(!notiItem.category.equals("msg")) {
            holder.scaleGroup3.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView appName;
        private TextView title;
        private TextView content;
        private TextView position;
        private EditText reason_box;
        private RadioGroup scaleGroup0;
        private RadioGroup scaleGroup1;
        private RadioGroup scaleGroup2;
        private RadioGroup scaleGroup3;


        ViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            appName = (TextView) view.findViewById(R.id.appname);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            position = view.findViewById(R.id.scale_item_position);
            reason_box = view.findViewById(R.id.likert_scale_reason_box);


            reason_box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        Log.d("text", "done");
                        return false;   // return false to hide the keyboard
                    }
                    else    return true;
                }
            });


            reason_box.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    reason_done.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    Log.d("text onChange", getAdapterPosition() + " " + s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d("text afterChange", getAdapterPosition() + " " + s.toString());
                    if(s.toString().trim().length() > 0) {
                        mData.get(getAdapterPosition()).setSortReason(s.toString());
                    }
                }
            });

            scaleGroup0 = (RadioGroup) view.findViewById(R.id.likert_scale0);
            scaleGroup0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree0:
                            mData.get(getAdapterPosition()).factor0 = strong_agree;
                            Log.d("edit text", reason_box.getText().toString());
                            break;
                        case R.id.agree0:
                            mData.get(getAdapterPosition()).factor0 = agree;
                            break;
                        case R.id.neutral0:
                            mData.get(getAdapterPosition()).factor0 = neutral;
                            break;
                        case R.id.disagree0:
                            mData.get(getAdapterPosition()).factor0 = disagree;
                            break;
                        case R.id.strong_disagree0:
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

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree1:
                            mData.get(getAdapterPosition()).factor1 = strong_agree;
                            break;
                        case R.id.agree1:
                            mData.get(getAdapterPosition()).factor1 = agree;
                            break;
                        case R.id.neutral1:
                            mData.get(getAdapterPosition()).factor1 = neutral;
                            break;
                        case R.id.disagree1:
                            mData.get(getAdapterPosition()).factor1 = disagree;
                            break;
                        case R.id.strong_disagree1:
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

                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree2:
                            mData.get(getAdapterPosition()).factor2 = strong_agree;
                            break;
                        case R.id.agree2:
                            mData.get(getAdapterPosition()).factor2 = agree;
                            break;
                        case R.id.neutral2:
                            mData.get(getAdapterPosition()).factor2 = neutral;
                            break;
                        case R.id.disagree2:
                            mData.get(getAdapterPosition()).factor2 = disagree;
                            break;
                        case R.id.strong_disagree2:
                            mData.get(getAdapterPosition()).factor2 = strong_disagree;
                            break;
                        case R.id.not_apply_2:
                            mData.get(getAdapterPosition()).factor2 = not_apply
                        default:
                            mData.get(getAdapterPosition()).factor2 = -1;
                    }

                }



            });

            scaleGroup3 = (RadioGroup) view.findViewById(R.id.likert_scale3);
            scaleGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (group.getCheckedRadioButtonId()){
                        case R.id.strong_agree3:
                            mData.get(getAdapterPosition()).factor3 = strong_agree;
                            break;
                        case R.id.agree3:
                            mData.get(getAdapterPosition()).factor3 = agree;
                            break;
                        case R.id.neutral3:
                            mData.get(getAdapterPosition()).factor3 = neutral;
                            break;
                        case R.id.disagree3:
                            mData.get(getAdapterPosition()).factor3 = disagree;
                            break;
                        case R.id.strong_disagree3:
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
