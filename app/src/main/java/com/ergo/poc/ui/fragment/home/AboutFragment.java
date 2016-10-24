package com.ergo.poc.ui.fragment.home;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by mariano on 10/21/16.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.about_version)
    TextView version;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        try {
            initViews();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initViews() throws PackageManager.NameNotFoundException {
        version.setText(String.format(getActivity().getResources().getString(R.string.version_name), Utils.getVersionName()));
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getActivity().getResources().getString(R.string.about));
    }

    @OnClick(R.id.about_call)
    public void call() {
        Utils.openDial(getActivity(), getActivity().getResources().getString(R.string.call_111));
    }
}
