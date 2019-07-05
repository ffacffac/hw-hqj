package com.hw.ourlife.mvp.presenter;

import com.hw.ourlife.mvp.view.BaseView;

/**
 * Presenter 基类
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public interface AbstractPresenter<T extends BaseView> {

    /**
     * 注入View
     *
     * @param view view
     */
    void attachView(T view);

    /**
     * 回收View
     */
    void detachView();
}
