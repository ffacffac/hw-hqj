package com.hw.ourlife.mvp.view;

/**
 * view 基类
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */
public interface BaseView {

    /**
     * Show error message
     *
     * @param errorMsg error message
     */
    void showErrorMsg(Exception e, String errorMsg);

    /**
     * Show loading
     */
    void showLoading();

    /**
     * dismiss loading
     */
    void dismissLoading();
}
