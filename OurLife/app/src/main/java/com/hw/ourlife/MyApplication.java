package com.hw.ourlife;

import android.app.Application;

import com.hw.baselibrary.db.DbUtils;
import com.hw.baselibrary.util.Toast;
import com.hw.ourlife.component.AppComponent;
import com.hw.ourlife.component.DaggerAppComponent;
import com.hw.ourlife.module.AppModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by huangqj on 2019-04-09.
 *
 * @author huangqj
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;
    private static AppComponent appComponent;
    private RefWatcher refWatcher;
    public Toast toast;
    public DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        toast = Toast.get(myApplication);
        initLeakCanary();
        inject();
        initDb();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(myApplication)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(myApplication);
    }

    public static RefWatcher getRefWatcher() {
        return myApplication.refWatcher;
    }

    /**
     * 初始化xutils
     */
    private void initDb() {
        x.Ext.init(myApplication);
        db = DbUtils.getDbManager(DbUtils.getDaoConfig());
    }

    // public static MyApplication getApp() {
    //     return myApplication;
    // }

    public static synchronized AppComponent inject() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().appModule(new AppModule(myApplication)).build();
            ComponentHolder.setAppComponent(appComponent);
        }
        return appComponent;
    }
}
