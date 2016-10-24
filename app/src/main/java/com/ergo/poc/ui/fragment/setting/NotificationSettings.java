package com.ergo.poc.ui.fragment.setting;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergo.poc.R;
import com.ergo.poc.data.adapter.NotificationPreferenceAdapter;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.util.NotificationPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/24/16.
 */
public class NotificationSettings extends DialogFragment {

    @BindView(R.id.notification_settings_recycler)
    RecyclerView recyclerView;

    private List<Notification> notificationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_settings, container, false);

        ButterKnife.bind(this, view);

        notificationList = NotificationPreference.getInstance().getNotificationList();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new NotificationPreferenceAdapter(notificationList, itemCheckListener));

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private NotificationPreferenceAdapter.OnItemCheckListener itemCheckListener =
            new NotificationPreferenceAdapter.OnItemCheckListener() {
                @Override
                public void onItemCheck(int position) {
                    notificationList.get(position).setState(true);
                }

                @Override
                public void onItemUnCheck(int position) {
                    notificationList.get(position).setState(false);
                }

                @Override
                public void onFinally() {
                    NotificationPreference.getInstance().setNotificationList(notificationList);
                }
            };
}
