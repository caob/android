package com.ergo.poc.data.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.model.Notifications;
import com.ergo.poc.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Created by mariano on 10/21/16.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Activity activity;

    private List<Notifications> notificationsList;

    private OnItemClickListener onItemClickListener;

    public NotificationsAdapter(Activity activity) {
        this.activity = activity;
        this.notificationsList = new ArrayList<>();
    }

    public void setNotificationsList(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notifications notifications = notificationsList.get(position);

        int color = 0;
        int category = 0;

        switch (notifications.getCategory()) {
            case Constant.NOTIFICATION_ABOUT_APP:
                color = R.color.notifications_about_app;
                category = R.string.notification_about_app;
                break;
            case Constant.NOTIFICATION_PROMOTION:
                color = R.color.notifications_promotion;
                category = R.string.notification_promotion;
                break;
            case Constant.NOTIFICATION_NEWS:
                color = R.color.notifications_news;
                category = R.string.notification_news;
                break;
        }

        holder.title.setText(category);
        holder.category.setImageDrawable(new ColorDrawable(ContextCompat.getColor(activity, color)));
        holder.description.setText(notifications.getDescription());
        holder.date.setText(notifications.getDateFormat());

        if (notifications.isRead()) {
            holder.description.setTypeface(Typeface.DEFAULT);
        } else {
            holder.description.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.notifications_item_title)
        TextView title;

        @BindView(R.id.notifications_item_description)
        TextView description;

        @BindView(R.id.notifications_item_date)
        TextView date;

        @BindView(R.id.notifications_item_color)
        CircleImageView category;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, notificationsList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Notifications value);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
