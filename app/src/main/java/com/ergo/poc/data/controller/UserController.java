package com.ergo.poc.data.controller;

import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.dao.PocDAO;
import com.ergo.poc.data.model.User;

/*
 * Created by mariano on 10/22/16.
 */
public class UserController {

    public void loginUser(final ResultListener<User> resultListener) {
        PocDAO pocDAO = new PocDAO();
        pocDAO.loginUser(new ResultListener<User>() {
            @Override
            public void loading() {
                resultListener.loading();
            }

            @Override
            public void finish(User result) {
                resultListener.finish(result);
            }

            @Override
            public void error(Throwable error) {
                resultListener.error(error);
            }
        });
    }

    public void updateUser(final ResultListener<User> resultListener) {
        PocDAO pocDAO = new PocDAO();
        pocDAO.updateUser(new ResultListener<User>() {
            @Override
            public void loading() {
                resultListener.loading();
            }

            @Override
            public void finish(User result) {
                resultListener.finish(result);
            }

            @Override
            public void error(Throwable error) {
                resultListener.error(error);
            }
        });
    }
}
