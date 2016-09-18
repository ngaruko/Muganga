package org.muganga;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

public class MainApplication extends Application {


    private static MainApplication sInstance;

//
    public static MainApplication getInstance() {
        return sInstance;
    }
//
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Firebase.setAndroidContext(this);


    }

}