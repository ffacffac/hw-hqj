package com.hw.ourlife.mvp.view;

import com.hw.ourlife.mvp.presenter.AbstractPresenter;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public interface ILoginContract {

    interface ILoginView extends BaseView {
        /**
         * 登录返回结果
         *
         * @param result true--登录成功
         */
        void onLogin(boolean result);
    }

    interface ILoginPresenter extends AbstractPresenter<ILoginContract.ILoginView> {

        /**
         * 是否首次安装，打开APP
         *
         * @return true--是
         */
        boolean firstOpen();

        /**
         * 首次安装，打开APP时，将密码写入文件
         */
        void initPassword();

        /**
         * 登录
         *
         * @param password 密码
         */
        void login(String password);
    }
}
