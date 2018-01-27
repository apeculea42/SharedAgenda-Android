package com.myherobots;

import android.app.Application;
import android.content.Context;

/**
 * Created by ale on 26/01/2018.
 */

public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
