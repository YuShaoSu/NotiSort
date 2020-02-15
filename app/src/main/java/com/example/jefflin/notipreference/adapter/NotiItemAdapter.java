package com.example.jefflin.notipreference.adapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jefflin.notipreference.model.NotiItem;
import com.example.jefflin.notipreference.helper.IconHandler;
import com.example.jefflin.notipreference.helper.NotiItemMoveCallback;
import com.example.jefflin.notipreference.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    The adaptor of the sorting page recycler view.
 */
public class NotiItemAdapter extends RecyclerView.Adapter<NotiItemAdapter.ViewHolder> implements
        NotiItemMoveCallback.ItemTouchHelperContract {
    private Context mContext;
    private List<NotiItem> mData;
    private NotiItemMoveCallback.OnStartDragListener mDragListener;


    public NotiItemAdapter(Context context, ArrayList<NotiItem> data, NotiItemMoveCallback.OnStartDragListener dragListener) {
        this.mContext = context;
        this.mData = data;
        this.mDragListener = dragListener;
    }

    public void setNoti(List<NotiItem> notiItems){
        mData = notiItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        NotiItem notiItem = mData.get(position);

        IconHandler iconHandler = new IconHandler();
        Bitmap icon_ = iconHandler.loadImageFromStorage(notiItem.icon, notiItem.appName);
        Drawable icon = new BitmapDrawable(mContext.getResources(), icon_);

        holder.iv_icon.setImageDrawable(icon);
        holder.tv_appname.setText(notiItem.appName);
        holder.tv_title.setText(notiItem.title);
        holder.tv_content.setText(notiItem.content);

        // check if won't click
        if(notiItem.getClickOrder() == -9999) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
        }
        else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()
                        == MotionEvent.ACTION_DOWN) {
                    // Notify ItemTouchHelper to start dragging
                    mDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_appname;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_rank;
        private ImageView iv_drag;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView)itemView.findViewById(R.id.icon);
            tv_appname = (TextView)itemView.findViewById(R.id.appname);
            tv_title = (TextView)itemView.findViewById(R.id.title);
            tv_content = (TextView)itemView.findViewById(R.id.content);
            iv_drag = (ImageView)itemView.findViewById(R.id.drag);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.noti_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_noti_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onRowMoved (int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {

    }

    @Override
    public void onRowDelete(int pos) {
        NotiItem item;
        item = mData.get(pos);
        item.setClickOrder(item.getClickOrder() == -9999 ? -1 : -9999);
        mData.add(item);
        mData.remove(pos);
        notifyItemRemoved(pos);
        notifyItemInserted(mData.size() - 1);
    }

}