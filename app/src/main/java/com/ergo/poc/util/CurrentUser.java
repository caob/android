package com.ergo.poc.util;

import android.app.Activity;

import com.ergo.poc.PocApplication;
import com.ergo.poc.data.model.User;
import com.google.gson.Gson;

/*
 * Created by mariano on 10/22/16.
 */
public class CurrentUser {

    private static CurrentUser instance = null;

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public User getUser() {
        return new Gson().fromJson(KeySaver.getStringSavedShare(PocApplication.getContext(), Constant.CURRENT_USER), User.class);
    }

    public void setUser(User user) {
        KeySaver.saveShare(PocApplication.getContext(), Constant.CURRENT_USER, new Gson().toJson(user));
    }
}
