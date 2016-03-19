package com.example.wsmm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by abubaker on 12/03/2016.
 */
public class Wsmm extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

       // RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
       // Realm.setDefaultConfiguration(realmConfig);
    }
}
