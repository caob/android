package com.ergo.poc.data.controller;

import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.dao.PocDAO;
import com.ergo.poc.data.model.Centers;

import java.util.List;

/*
 * Created by mariano on 10/21/16.
 */
public class CentersController {

    public void getCenters(final ResultListener<List<Centers>> resultListener) {
        PocDAO pocDAO = new PocDAO();
        pocDAO.getCenters(new ResultListener<List<Centers>>() {
            @Override
            public void loading() {
                resultListener.loading();
            }

            @Override
            public void finish(List<Centers> result) {
                resultListener.finish(result);
            }

            @Override
            public void error(Throwable error) {
                resultListener.error(error);
            }
        });
    }
}
