package com.example.jefflin.notipreference;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/*
    The adaptor of the sorting page recycler view.
 */
public class NotiItemAdapter extends RecyclerView.Adapter<NotiItemAdapter.ViewHolder> implements
        ItemMoveCallback.ItemTouchHelperContract {
    private Context mContext;
    private ArrayList<NotiItem> mData;
    private ItemMoveCallback.OnStartDragListener mDragListener;

    public NotiItemAdapter(Context context, ArrayList<NotiItem> data, ItemMoveCallback.OnStartDragListener dragListener) {
        this.mContext = context;
        this.mData = data;
        this.mDragListener = dragListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NotiItem notiitem = mData.get(position);
        holder.iv_icon.setImageDrawable(notiitem.icon);
        holder.tv_appname.setText(notiitem.appname);
        holder.tv_title.setText(notiitem.title);
        holder.tv_content.setText(notiitem.content);

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

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_appname;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_rank;
        private ImageView iv_drag;

        ViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView)itemView.findViewById(R.id.icon);
            tv_appname = (TextView)itemView.findViewById(R.id.appname);
            tv_title = (TextView)itemView.findViewById(R.id.title);
            tv_content = (TextView)itemView.findViewById(R.id.content);
            iv_drag = (ImageView)itemView.findViewById(R.id.drag);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
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
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

}