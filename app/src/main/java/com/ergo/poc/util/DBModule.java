package com.ergo.poc.util;

import com.ergo.poc.data.model.Centers;
import com.ergo.poc.data.model.Notifications;

import io.realm.annotations.RealmModule;

/*
 * Created by mariano on 10/23/16.
 */
@RealmModule(classes = {Centers.class, Notifications.class})
public class DBModule {
}
