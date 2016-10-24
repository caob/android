package com.ergo.poc.data.api;

import com.ergo.poc.data.model.Notifications;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * Created by mariano on 10/23/16.
 */
public interface NotificationsAPI {

    @GET("json/get/Vki5ux8kG")
    Call<List<Notifications>> loadNotifications(@Query("read") Boolean read);
}
