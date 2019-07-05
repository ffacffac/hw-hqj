package com.hw.ourlife.component;

import android.app.Activity;

import com.hw.ourlife.activity.MainActivity;
import com.hw.ourlife.module.ActivityModule;

import dagger.Component;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    /**
     * 获取act
     *
     * @return act
     */
    Activity getActivity();

    /**
     * 注入MainActivity所需的依赖
     *
     * @param mainActivity 目标act
     */
    void inject(MainActivity mainActivity);
}
