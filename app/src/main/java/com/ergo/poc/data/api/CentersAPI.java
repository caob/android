package com.ergo.poc.data.api;

import com.ergo.poc.data.model.Centers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/*
 * Created by mariano on 10/21/16.
 */
public interface CentersAPI {

    @GET("getCentros")
    Call<List<Centers>> loadCenters();
}
