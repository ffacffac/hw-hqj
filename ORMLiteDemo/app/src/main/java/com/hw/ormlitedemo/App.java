package com.hw.ormlitedemo;

import android.app.Application;

import com.hw.ormlitedemo.database.DBHelper;

/**
 * Created by huangqj on 2019-07-25.
 */

public class App extends Application {

    private static DBHelper dbHelper;
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = DBHelper.getInstance(this);
        app = this;
    }

    public static App getInstance() {
        return app;
    }

    public static DBHelper getDBHelper() {
        return dbHelper;
    }
}
