package com.hw.ourlife.component;

import android.content.SharedPreferences;

import com.hw.ourlife.module.AppModule;
import com.hw.ourlife.MyApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    SharedPreferences sharedPreferences();

    MyApplication myApplication();
}
