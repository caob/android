package com.ergo.poc.ui.fragment.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.model.Notifications;
import com.ergo.poc.ui.activity.HomeActivity;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/*
 * Created by mariano on 10/21/16.
 */
public class DashboardFragment extends Fragment {

    @BindView(R.id.dashboard_home_message)
    View messages;

    @BindView(R.id.dashboard_home_centers)
    View centers;

    @BindView(R.id.badge_holder)
    RelativeLayout badge;

    @BindView(R.id.badge_count)
    TextView badgeCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        initViews();

        return view;
    }

    private void initViews() {
        RealmResults<Notifications> query = Realm.getDefaultInstance().where(Notifications.class)
                .equalTo(Constant.READ, false)
                .findAll();

        badgeCount.setText(String.valueOf(query.size()));
        badge.setVisibility(query.size() > 0 ? View.VISIBLE : View.GONE);

        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getActivity().getResources().getString(R.string.home));

    }

    @OnClick(R.id.dashboard_home_message)
    public void messages() {
        ((HomeActivity)getActivity()).checkItem(R.id.nav_inbox);
        FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.home_fragment_container
                , false, new InboxFragment());
    }

    @OnClick(R.id.dashboard_home_centers)
    public void centers() {
        ((HomeActivity)getActivity()).checkItem(R.id.nav_centers);
        FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.home_fragment_container
                , false, new CentersFragment());
    }
}
