package com.hw.ourlife.mvp.presenter;

import android.content.SharedPreferences;

import com.hw.baselibrary.constant.Constant;
import com.hw.baselibrary.constant.Key;
import com.hw.baselibrary.util.EncryptUtil;
import com.hw.baselibrary.util.FileUtil;
import com.hw.baselibrary.util.LogUtils;
import com.hw.baselibrary.util.RxUtils;
import com.hw.ourlife.ComponentHolder;
import com.hw.ourlife.mvp.view.ILoginContract;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public class LoginPresenter extends BasePresenter<ILoginContract.ILoginView> implements ILoginContract.ILoginPresenter {

    private static final String TAG = LoginPresenter.class.getName();
    private SharedPreferences preferences;

    @Inject
    protected LoginPresenter() {
        super();
        preferences = ComponentHolder.getAppComponent().sharedPreferences();
    }

    @Override
    public boolean firstOpen() {
        return preferences.getBoolean(Key.KEY_SPARE_FIRST_OPEN, true);
    }

    @Override
    public void initPassword() {
        if (!firstOpen()) {
            return;
        }
        //首次安装，首次打开APP
        preferences.edit().putBoolean(Key.KEY_SPARE_FIRST_OPEN, false).apply();
        try {
            FileUtil.writeStrToFile(Constant.CONFIG_FILE_DIR_PATH, Constant.CONFIG_FILE_NAME, Constant.PASSWORD);
            LogUtils.e(TAG, "initPassword", "首次安装，写入密码成功---->");
        } catch (Exception e) {
            LogUtils.ex(TAG, "initPassword", e);
        }
    }

    @Override
    public void login(String password) {
        view.showLoading();
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            String encryptPwd = EncryptUtil.encrypt(password);
            String configPwd = FileUtil.readFile(Constant.CONFIG_FILE_PATH);
            LogUtils.e(TAG, "login", "加密成功-->" + encryptPwd);
            LogUtils.e(TAG, "login ", "文件加密内容-->" + configPwd);
            emitter.onNext(encryptPwd.equals(configPwd));
            emitter.onComplete();
        }).compose(RxUtils.rxSchedulerHelper()).subscribe(aBoolean -> {
            //登录成功
            view.dismissLoading();
            view.onLogin(aBoolean);
        }, e -> {
            view.dismissLoading();
            LogUtils.ex(TAG, "login: ", e);
            view.showErrorMsg(new Exception("登录出现异常", e), "登录出现异常");
        });
        addSubscribe(disposable);
    }
}
