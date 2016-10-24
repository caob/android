package com.ergo.poc.data.controller;

import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.dao.PocDAO;
import com.ergo.poc.data.model.Notification;
import com.ergo.poc.data.model.Notifications;

import java.util.List;

/*
 * Created by mariano on 10/21/16.
 */
public class NotificationController {

    public void getNotifications(final ResultListener<List<Notifications>> resultListener
            , boolean updateValues
            , Boolean read, boolean order, List<Notification> view) {
        PocDAO pocDAO = new PocDAO();
        pocDAO.getNotifications(new ResultListener<List<Notifications>>() {
            @Override
            public void loading() {
                resultListener.loading();
            }

            @Override
            public void finish(List<Notifications> result) {
                resultListener.finish(result);
            }

            @Override
            public void error(Throwable error) {
                resultListener.error(error);
            }
        }, updateValues, read, order, view);
    }
}
