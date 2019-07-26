package com.hw.ourlife.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */
@Module
public class ActivityModule {

    private Activity activity;

    /**
     * 将目标类传过来
     *
     * @param activity 目标activity
     */
    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * 对应ActivityComponent接口的getActivity()
     *
     * @return Activity 返回值
     */
    @Provides
    Activity provideActivity() {
        return activity;
    }
}
