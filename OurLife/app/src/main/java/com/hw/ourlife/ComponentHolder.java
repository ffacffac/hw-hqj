package com.hw.ourlife;

import com.hw.ourlife.component.AppComponent;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public class ComponentHolder {

    private static AppComponent myAppComponent;

    public static void setAppComponent(AppComponent component) {
        myAppComponent = component;
    }

    public static AppComponent getAppComponent() {
        return myAppComponent;
    }
}
