package com.ergo.poc.ui.fragment.home;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ergo.poc.R;
import com.ergo.poc.data.adapter.NotificationsAdapter;
import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.controller.NotificationController;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.data.model.Notifications;
import com.ergo.poc.ui.fragment.setting.NotificationSettings;
import com.ergo.poc.util.NotificationPreference;
import com.ergo.poc.util.Utils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by mariano on 10/21/16.
 */
public class InboxFragment extends Fragment {

    @BindString(R.string.notifications_progress_title)
    String progressTitle;

    @BindString(R.string.notifications_progress_message)
    String progressMessage;

    @BindView(R.id.inbox_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.inbox_recycler)
    RecyclerView recyclerView;

    private NotificationsAdapter adapter;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, view);

        initViews();
        initData(false, null, true, NotificationPreference.getInstance().getNotificationList());

        return view;
    }

    private void initViews() {
        adapter = new NotificationsAdapter(getActivity());
        adapter.setOnItemClickListener(itemClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(refreshListener);

        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getActivity().getResources().getString(R.string.inbox));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefresh.setRefreshing(true);
            initData(true, null, true, NotificationPreference.getInstance().getNotificationList());
        }
    };

    private void initData(final boolean updateValues, Boolean read, boolean order, List<Notification> view) {
        NotificationController notificationController = new NotificationController();
        notificationController.getNotifications(new ResultListener<List<Notifications>>() {
            @Override
            public void loading() {
                progressDialog = Utils.showProgressDialog(getActivity(), progressTitle, progressMessage);
            }

            @Override
            public void finish(List<Notifications> result) {
                if (updateValues) swipeRefresh.setRefreshing(false);
                progressDialog.cancel();
                adapter.setNotificationsList(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(Throwable error) {
                if (updateValues) swipeRefresh.setRefreshing(false);
                progressDialog.cancel();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, updateValues, read, order, view);
    }

    private NotificationsAdapter.OnItemClickListener itemClickListener = new NotificationsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, Notifications value) {

        }
    };

    @OnClick(R.id.inbox_header_button)
    public void settings() {
        NotificationSettings notificationDialog = new NotificationSettings();
        notificationDialog.show(getFragmentManager(), null);
    }
}
