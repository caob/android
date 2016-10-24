package com.ergo.poc.util;

import com.ergo.poc.data.model.Centers;

/*
 * Created by mariano on 10/24/16.
 */
public class CentersUtils {

    private static CentersUtils instance = null;

    private Centers centers;

    public static CentersUtils getInstance() {
        if (instance == null) {
            instance = new CentersUtils();
        }
        return instance;
    }

    public Centers getCenters() {
        return centers;
    }

    public void setCenters(Centers centers) {
        this.centers = centers;
    }
}
