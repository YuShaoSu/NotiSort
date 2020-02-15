package com.example.jefflin.notipreference.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.NotiItem;

import java.util.ArrayList;
import java.util.List;

public class TwoListItemsAdapter extends RecyclerView.Adapter<TwoListItemsAdapter.ViewHolder> {

    private List<NotiItem> mActiveDataAttend;
    private List<NotiItem> mActiveDataDisplay;
    private List<NotiItem> combine = new ArrayList<NotiItem>();
    private Context mContext;
//    private boolean show = false;


    public TwoListItemsAdapter(Context context, ArrayList<NotiItem> dataAttend, ArrayList<NotiItem> dataDisplay) {
        this.mContext = context;
        this.mActiveDataAttend = dataAttend;
        this.mActiveDataDisplay = dataDisplay;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_appname;
        private TextView tv_title;
        private TextView tv_content;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.icon);
            tv_appname = (TextView) itemView.findViewById(R.id.appname);
            tv_title = (TextView) itemView.findViewById(R.id.title);
            tv_content = (TextView) itemView.findViewById(R.id.content);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.esm_item_backgound);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.esm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        combineTwoList();
//        if (!show) return;
        NotiItem notiItem = combine.get(position);

        IconHandler iconHandler = new IconHandler();
        Bitmap icon_ = iconHandler.loadImageFromStorage(notiItem.icon, notiItem.appName);
        Drawable icon = new BitmapDrawable(mContext.getResources(), icon_);

        holder.iv_icon.setImageDrawable(icon);
        holder.tv_appname.setText(notiItem.appName);
        holder.tv_title.setText(notiItem.title);
        holder.tv_content.setText(notiItem.content);

        if(notiItem.getClickOrder() == -9999) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
        }
        else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return mActiveDataAttend.size() + mActiveDataDisplay.size();
    }

    private void combineTwoList() {
        combine.clear();
//        show = SurveyManager.getInstance().twoListDiff((ArrayList<NotiItem>) mActiveDataAttend, (ArrayList<NotiItem>) mActiveDataDisplay);
//        if (!show) return;
        for (int i = 0; i < mActiveDataDisplay.size(); i++) {
            combine.add(mActiveDataAttend.get(i));
            combine.add(mActiveDataDisplay.get(i));
        }
    }
}
