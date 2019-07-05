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

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }
}
