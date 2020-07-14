package com.example.jefflin.notipreference.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.model.LogModel;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private List<LogModel> logModels;
    private Context mContext;

    public LogAdapter(Context context, List<LogModel> logModels) {
        this.mContext = context;
        this.logModels = logModels;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timestamp;
        private TextView event;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            timestamp = (TextView) itemView.findViewById(R.id.log_time);
            event = (TextView) itemView.findViewById(R.id.log_event);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.log_item);
        }
    }

    @Override
    public int getItemCount() {
        return logModels.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final LogAdapter.ViewHolder holder, int position) {
        LogModel logModel = logModels.get(position);
        holder.timestamp.setText(logModel.timestamp.toString());
        holder.event.setText(logModel.event);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.log_item, parent, false);
        return new ViewHolder(view);
    }

    public void addItems(List<LogModel> logModels) {
        this.logModels = logModels;
        notifyDataSetChanged();
    }



}
