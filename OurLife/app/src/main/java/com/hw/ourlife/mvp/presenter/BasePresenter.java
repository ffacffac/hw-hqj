package com.hw.ourlife.mvp.presenter;

import com.hw.ourlife.mvp.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public class BasePresenter<T extends BaseView> implements AbstractPresenter<T> {

    protected T view;
    private CompositeDisposable compositeDisposable;

    protected BasePresenter() {
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void detachView() {
        this.view = null;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
