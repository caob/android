package com.ergo.poc;

import android.app.Application;
import android.content.Context;

import com.digits.sdk.android.Digits;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.DBModule;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/*
 * Created by mariano on 10/21/16.
 */
public class PocApplication extends Application {

    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT =  getApplicationContext();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constant.TWITTER_KEY, Constant.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .modules(new DBModule())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
