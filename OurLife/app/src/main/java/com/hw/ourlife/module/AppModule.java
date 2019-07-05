package com.hw.ourlife.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.hw.baselibrary.constant.Key;
import com.hw.ourlife.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */
@Module
public class AppModule {

    private MyApplication myApplication;

    public AppModule(MyApplication application) {
        this.myApplication = application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        //提供全局唯一SharedPreferences
        return myApplication.getSharedPreferences(Key.KEY_APP_SPARE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication() {
        return myApplication;
    }
}
