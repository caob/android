package com.ergo.poc.data.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/*
 * Created by mariano on 10/22/16.
 */
public interface LoginAPI {

    @POST("auth")
    Call<ResponseBody> loginUser(@Body RequestBody data);

    @PUT("usuarios/{user_id}")
    Call<ResponseBody> updateUser(@Path("user_id") String id, @Body RequestBody data);

}
