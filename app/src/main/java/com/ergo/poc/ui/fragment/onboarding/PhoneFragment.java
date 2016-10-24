package com.ergo.poc.ui.fragment.onboarding;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.ergo.poc.R;
import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.controller.UserController;
import com.ergo.poc.data.model.User;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.CurrentUser;
import com.ergo.poc.util.FragmentUtils;
import com.ergo.poc.util.KeySaver;
import com.ergo.poc.util.Utils;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterCore;

import butterknife.BindString;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/21/16.
 */
public class PhoneFragment extends Fragment {

    @BindString(R.string.login_progress_title)
    String progressTitle;

    @BindString(R.string.login_progress_message)
    String progressMessage;

    @BindString(R.string.button_login_phone)
    String loginPhone;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        ButterKnife.bind(this, view);

        if (KeySaver.isExist(getActivity(), Constant.LOGIN_OK)) {
            FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.on_boarding_container
                    , false, new CompleteProfileFragment());
        }

        initPhoneLogin(view);

        return view;
    }

    private void initPhoneLogin(View view) {

        int padding = Utils.dpToPx(getActivity(), 50);

        DigitsAuthButton digitsButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        digitsButton.setText(loginPhone);
        digitsButton.setTextSize(14);
        digitsButton.setPadding(padding, 0, padding, 0);
        digitsButton.setTypeface(Typeface.DEFAULT_BOLD);
        digitsButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_shape));
        digitsButton.setCallback(authCallback);
    }

    private AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {

            DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(TwitterCore.getInstance().getAuthConfig()
                    , session.getAuthToken());

            KeySaver.saveShare(getActivity(), Constant.AUTH_USER, new Gson().toJson(oauthSigning
                    .getOAuthEchoHeadersForVerifyCredentials()));

            UserController loginController = new UserController();
            loginController.loginUser(new ResultListener<User>() {
                @Override
                public void loading() {
                    progressDialog = Utils.showProgressDialog(getActivity(), progressTitle, progressMessage);
                }

                @Override
                public void finish(User user) {
                    progressDialog.cancel();
                    CurrentUser.getInstance().setUser(user);
                    KeySaver.saveShare(getActivity(), Constant.LOGIN_OK, true);
                    FragmentUtils.getInstance().replaceFragment(getActivity(), R.id.on_boarding_container
                            , true, new CompleteProfileFragment());
                }

                @Override
                public void error(Throwable error) {
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void failure(DigitsException exception) {
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
