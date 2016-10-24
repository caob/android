package com.ergo.poc.ui.fragment.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.adapter.NotificationPreferenceAdapter;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.data.model.User;
import com.ergo.poc.util.CurrentUser;
import com.ergo.poc.util.NotificationPreference;
import com.ergo.poc.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/21/16.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_text_name)
    TextView name;

    @BindView(R.id.profile_text_last_name)
    TextView lastName;

    @BindView(R.id.profile_text_day)
    TextView day;

    @BindView(R.id.profile_text_month)
    TextView month;

    @BindView(R.id.profile_text_year)
    TextView year;

    @BindView(R.id.profile_preference_recycler)
    RecyclerView recyclerView;

    private List<Notification> notificationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        initViews();

        return view;
    }

    private void initViews() {

        User user = CurrentUser.getInstance().getUser();

        name.setText(user.getName());
        lastName.setText(user.getLastName());
        day.setText(user.getBirthday().split("/")[0]);
        month.setText(Utils.getMonthName(Integer.parseInt(user.getBirthday().split("/")[1])));
        year.setText(user.getBirthday().split("/")[2]);

        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(getActivity().getResources().getString(R.string.profile));

        notificationList = NotificationPreference.getInstance().getNotificationList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new NotificationPreferenceAdapter(notificationList, itemCheckListener));

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
