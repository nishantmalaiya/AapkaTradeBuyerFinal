package com.aapkatrade.buyer.general;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by PPC15 on 8/29/2017.
 */

public class ATApplication extends Application {
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = ATApplication.this;
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(config);
    }
}