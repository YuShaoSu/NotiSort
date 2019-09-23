package com.example.jefflin.notipreference.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
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

        private TextView appname;
        private TextView title;
        private TextView content;
        private RadioGroup scaleGroup;

        ViewHolder(View view) {
            super(view);
            appname = (TextView) view.findViewById(R.id.appname);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);


//            scaleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                }
//            });
        }
    }
}
