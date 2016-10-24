package com.ergo.poc.util;

import com.ergo.poc.PocApplication;
import com.ergo.poc.data.model.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by mariano on 10/24/16.
 */
public class NotificationPreference {

    private static NotificationPreference instance = null;

    public static NotificationPreference getInstance() {
        if (instance == null) {
            instance = new NotificationPreference();
        }
        return instance;
    }

    public void setNotificationList(List<Notification> notificationList) {
        KeySaver.saveShare(PocApplication.getContext(), Constant.NOTIFICATION_PREFERENCE
                , new Gson().toJson(notificationList));
    }

    public List<Notification> getNotificationList() {
        Type listType = new TypeToken<List<Notification>>() {
        }.getType();

        return new Gson().fromJson(KeySaver.getStringSavedShare(PocApplication.getContext()
                , Constant.NOTIFICATION_PREFERENCE), listType);
    }

    public List<Notification> generateData() {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification(Constant.NOTIFICATION_ABOUT_APP_NAME
                , Constant.NOTIFICATION_ABOUT_APP, true));
        notificationList.add(new Notification(Constant.NOTIFICATION_PROMOTION_NAME
                , Constant.NOTIFICATION_PROMOTION, true));
        notificationList.add(new Notification(Constant.NOTIFICATION_NEWS_NAME
                , Constant.NOTIFICATION_NEWS, true));
        return notificationList;
    }
}
