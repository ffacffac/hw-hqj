package com.hw.ourlife.activity;

import android.app.Activity;

import com.hw.ourlife.component.ActivityComponent;
import com.hw.ourlife.component.DaggerActivityComponent;
import com.hw.ourlife.module.ActivityModule;
import com.hw.ourlife.mvp.presenter.AbstractPresenter;
import com.hw.ourlife.mvp.view.BaseView;

import javax.inject.Inject;

/**
 * mvp设计模式继承此act
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public abstract class BasePresenterActivity<T extends AbstractPresenter> extends BaseActivity implements BaseView {

    @Inject
    T presenter;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        setPresenterView(this);
    }

    protected void setPresenterView(BaseView baseView) {
        if (presenter != null) {
            presenter.attachView(baseView);
        }
    }

    /**
     * 注入当前Activity所需的依赖
     */
    protected abstract void initInject();

    /**
     * @param activity act
     * @return ActivityComponent
     */
    protected ActivityComponent getActivityComponent(Activity activity) {
        // return DaggerActivityComponent.builder().appComponent(MyApplication.inject())
        //                               .activityModule(new ActivityModule(this)).build();
        return DaggerActivityComponent.builder().activityModule(new ActivityModule(activity)).build();
    }


    @Override
    public void showErrorMsg(Exception e, String errorMsg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroy();
    }
}
