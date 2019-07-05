package com.hw.ourlife.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.hw.ourlife.component.DaggerFragmentComponent;
import com.hw.ourlife.component.FragmentComponent;
import com.hw.ourlife.module.FragmentModule;
import com.hw.ourlife.mvp.presenter.AbstractPresenter;
import com.hw.ourlife.mvp.view.BaseView;

import javax.inject.Inject;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public abstract class BasePresenterFragment<T extends AbstractPresenter> extends BaseFragment implements BaseView {

    @Inject
    T presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initInject();
        setPresenterView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    protected void setPresenterView(BaseView view) {
        if (presenter != null) {
            presenter.attachView(view);
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDestroyView();
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder().fragmentModule(new FragmentModule(this)).build();
    }

    /**
     * 注入当前Fragment所需的依赖
     */
    protected abstract void initInject();

    @Override
    public void showErrorMsg(Exception e, String errorMsg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }
}
