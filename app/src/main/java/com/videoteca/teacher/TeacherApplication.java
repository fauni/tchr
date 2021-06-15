package com.videoteca.teacher;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TeacherApplication extends Application {
    private static TeacherApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // Call realm configuration
        setUpRealmConfiguration();
    }

    //Realm Configuration
    private void setUpRealmConfiguration() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .name("teacheapp.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public static synchronized TeacherApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

}
