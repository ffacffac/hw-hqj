package com.hw.datalivedemo;

import android.app.Application;

import timber.log.Timber;

/**
 * @author huangqj
 * @date 2019-08-05
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置只有在调试的时候输出日志
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
