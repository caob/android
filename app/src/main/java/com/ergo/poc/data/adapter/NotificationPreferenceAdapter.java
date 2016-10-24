package com.ergo.poc.data.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.model.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/24/16.
 */
public class NotificationPreferenceAdapter extends RecyclerView.Adapter<NotificationPreferenceAdapter.ViewHolder> {

    private List<Notification> notificationList;

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public NotificationPreferenceAdapter(List<Notification> notificationList, @NonNull OnItemCheckListener onItemCheckListener) {
        this.notificationList = notificationList;
        this.onItemCheckListener = onItemCheckListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preference, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.name.setText(notification.getName());
        holder.state.setChecked(notification.isState());

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.state.setChecked(!holder.state.isChecked());
                if (holder.state.isChecked()) {
                    onItemCheckListener.onItemCheck(holder.getAdapterPosition());
                } else {
                    onItemCheckListener.onItemUnCheck(holder.getAdapterPosition());
                }
                onItemCheckListener.onFinally();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.preference_name)
        TextView name;

        @BindView(R.id.preference_state)
        CheckBox state;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);

            state.setClickable(false);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }

    public interface OnItemCheckListener {
        void onItemCheck(int position);
        void onItemUnCheck(int position);
        void onFinally();
    }
}
