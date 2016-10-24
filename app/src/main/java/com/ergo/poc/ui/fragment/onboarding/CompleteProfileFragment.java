package com.ergo.poc.ui.fragment.onboarding;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.ergo.poc.R;
import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.controller.UserController;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.data.model.User;
import com.ergo.poc.ui.activity.HomeActivity;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.CurrentUser;
import com.ergo.poc.util.KeySaver;
import com.ergo.poc.util.NotificationPreference;
import com.ergo.poc.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by mariano on 10/21/16.
 */
public class CompleteProfileFragment extends Fragment {

    @BindString(R.string.login_progress_title)
    String progressTitle;

    @BindString(R.string.login_progress_message)
    String progressMessage;

    @BindView(R.id.complete_check_male)
    CheckBox checkMale;

    @BindView(R.id.complete_check_female)
    CheckBox checkFemale;

    @BindView(R.id.complete_edit_name)
    EditText name;

    @BindView(R.id.complete_edit_last_name)
    EditText lastName;

    @BindView(R.id.complete_edit_day)
    EditText day;

    @BindView(R.id.complete_edit_month)
    EditText month;

    @BindView(R.id.complete_edit_year)
    EditText year;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_profile, container, false);
        ButterKnife.bind(this, view);

        if (KeySaver.isExist(getActivity(), Constant.PROFILE_OK)) {
            Utils.startActivityWithFinish(getActivity(), HomeActivity.class);
        }

        initViews();

        return view;
    }

    @OnClick(R.id.complete_button)
    public void complete() {
        if (Utils.validate(new EditText[]{name, lastName, day, month, year}) && (checkMale.isChecked() || checkFemale.isChecked())) {

            putUser(checkMale, lastName, name, day, month, year);

            UserController userController = new UserController();
            userController.updateUser(new ResultListener<User>() {
                @Override
                public void loading() {
                    progressDialog = Utils.showProgressDialog(getActivity(), progressTitle, progressMessage);
                }

                @Override
                public void finish(User result) {
                    progressDialog.cancel();
                    CurrentUser.getInstance().setUser(result);
                    KeySaver.saveShare(getActivity(), Constant.PROFILE_OK, true);
                    Utils.startActivityWithFinishWithFadeAnimation(getActivity(), HomeActivity.class);
                }

                @Override
                public void error(Throwable error) {
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void putUser(CheckBox checkBox, EditText... editTexts) {
        Map<String, String> jsonUpdate = new HashMap<>();
        jsonUpdate.put(Constant.UPDATE_USER_LAST_NAME, editTexts[0].getText().toString());
        jsonUpdate.put(Constant.UPDATE_USER_NAME, editTexts[1].getText().toString());
        jsonUpdate.put(Constant.UPDATE_USER_BIRTHDAY, editTexts[2].getText().toString()
                + "/" + editTexts[3].getText().toString()
                + "/" + editTexts[4].getText().toString());
        jsonUpdate.put(Constant.UPDATE_USER_GENDER, checkBox.isChecked()
                ? Constant.UPDATE_USER_GENDER_MALE
                : Constant.UPDATE_USER_GENDER_FEMALE);

        NotificationPreference.getInstance().setNotificationList(NotificationPreference.getInstance().generateData());

        KeySaver.saveShare(getActivity(), Constant.UPDATE_USER, new Gson().toJson(jsonUpdate));
    }

    private void initViews() {
        checkMale.setOnCheckedChangeListener(checkChange);
        checkFemale.setOnCheckedChangeListener(checkChange);
    }

    private CompoundButton.OnCheckedChangeListener checkChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == checkMale) {
                checkFemale.setChecked(!isChecked);
            } else {
                checkMale.setChecked(!isChecked);
            }
        }
    };
}
