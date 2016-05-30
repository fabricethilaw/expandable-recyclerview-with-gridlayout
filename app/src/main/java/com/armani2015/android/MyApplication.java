package com.armani2015.android;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Thilaw Fabrice on 2016-04-19.
 */
public class MyApplication extends Application {
    public static String sDefSystemLanguage;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}