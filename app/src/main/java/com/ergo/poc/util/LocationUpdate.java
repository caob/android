package com.ergo.poc.util;

import android.location.Location;

/*
 * Created by mariano on 10/22/16.
 */
public class LocationUpdate {

    private static LocationUpdate instance = null;

    private android.location.Location location;

    public static LocationUpdate getInstance() {
        if (instance == null) {
            instance = new LocationUpdate();
        }
        return instance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
