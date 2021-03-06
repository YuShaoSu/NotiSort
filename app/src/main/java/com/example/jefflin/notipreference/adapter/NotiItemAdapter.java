package com.example.jefflin.notipreference.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jefflin.notipreference.ActivitySurvey;
import com.example.jefflin.notipreference.fragment.FragmentAttendBottomSheet;
import com.example.jefflin.notipreference.fragment.FragmentDisplayBottomSheet;
import com.example.jefflin.notipreference.helper.OnSheetDismissCallBack;
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
        NotiItemMoveCallback.ItemTouchHelperContract{
    private Context mContext;
    private List<NotiItem> mData;
    private NotiItemMoveCallback.OnStartDragListener mDragListener;
    private int sortType;
    private FragmentAttendBottomSheet fragmentAttendBottomSheet;
    private FragmentDisplayBottomSheet fragmentDisplayBottomSheet;

    public NotiItemAdapter(Context context, ArrayList<NotiItem> data, NotiItemMoveCallback.OnStartDragListener dragListener,  int sortType) {
        this.mContext = context;
        this.mData = data;
        this.mDragListener = dragListener;
        this.sortType = sortType;
    }

    public NotiItemAdapter(Context context, ArrayList<NotiItem> data, NotiItemMoveCallback.OnStartDragListener dragListener) {
        this.mContext = context;
        this.mData = data;
        this.mDragListener = dragListener;
    }

    public void setNoti(List<NotiItem> notiItems) {
        mData = notiItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        NotiItem notiItem = mData.get(position);

        IconHandler iconHandler = new IconHandler();
        Bitmap icon_ = iconHandler.loadImageFromStorage(notiItem.icon, notiItem.appName);
        if(icon_ != null) {
            Drawable icon = new BitmapDrawable(mContext.getResources(), icon_);
            holder.iv_icon.setImageDrawable(icon);
        }

        holder.tv_appname.setText(notiItem.appName);
        holder.tv_title.setText(notiItem.title);
        holder.tv_content.setText(notiItem.content);

        // check if won't click
        if (sortType == 0) {
            if (notiItem.isNotClick()) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
            } else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                notiItem.not_attend_no_info = -1;
                notiItem.not_attend_no_use = -1;
                notiItem.not_attend_not_relate = -1;
                notiItem.not_attend_other = -1;
            }
        } else if (sortType == 1) {
            if (notiItem.isNotDisplay()) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
            } else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                notiItem.not_display_dup = -1;
                notiItem.not_display_not_relate = -1;
                notiItem.not_display_known = -1;
                notiItem.not_display_other = -1;
            }
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
            iv_icon = (ImageView) itemView.findViewById(R.id.icon);
            tv_appname = (TextView) itemView.findViewById(R.id.appname);
            tv_title = (TextView) itemView.findViewById(R.id.title);
            tv_content = (TextView) itemView.findViewById(R.id.content);
            iv_drag = (ImageView) itemView.findViewById(R.id.drag);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.noti_item);
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
    public void onRowMoved(int fromPosition, int toPosition) {
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
        if (sortType == 0) item.setNotClick();
        else if (sortType == 1) item.setNotDisplay();
        mData.add(item);
        mData.remove(pos);
        notifyItemRemoved(pos);
        notifyItemInserted(mData.size() - 1);
        if (sortType == 0 && item.isNotClick()) {
            fragmentAttendBottomSheet = new FragmentAttendBottomSheet();
            fragmentAttendBottomSheet.show(((ActivitySurvey) mContext).getSupportFragmentManager(), "");
        } else if (sortType == 1 && item.isNotDisplay()) {
            fragmentDisplayBottomSheet = new FragmentDisplayBottomSheet();
            fragmentDisplayBottomSheet.show(((ActivitySurvey) mContext).getSupportFragmentManager(), "");
        }
    }

    public void setNotAttend(boolean need_no_info, boolean no_use, boolean not_relate, boolean other) {
        NotiItem item = mData.get(mData.size() - 1);
//        Log.d("not attend", "factor " + " " + item.appName);
        item.not_attend_no_info = need_no_info ? 1 : 0;
        item.not_attend_no_use = no_use ? 1 : 0;
        item.not_attend_other = other ? 1 : 0;
        item.not_attend_not_relate = not_relate ? 1 : 0;
    }

    public void setNotAttendReason(String reason) {
        NotiItem item = mData.get(mData.size() - 1);
//        Log.d("not attend", "other reason " + reason + " " + item.appName);
        item.not_attend_other_reason = reason;
    }

    public void setNotDisplay(boolean duplicate, boolean not_relate, boolean known, boolean other) {
        NotiItem item = mData.get(mData.size() - 1);
//        Log.d("not display", "factor " + " " + item.appName);
        item.not_display_dup = duplicate ? 1 : 0;
        item.not_display_not_relate = not_relate ? 1 : 0;
        item.not_display_known = known ? 1 : 0;
        item.not_display_other = other ? 1 : 0;
    }

    public void setNotDisplayReason(String reason) {
        NotiItem item = mData.get(mData.size() - 1);
//        Log.d("not display", "other reason " + reason + " " + item.appName + " size " + mData.size());
        item.not_display_other_reason = reason;
    }

}