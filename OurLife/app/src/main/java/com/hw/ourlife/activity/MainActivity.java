package com.hw.ourlife.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hw.baselibrary.loading.ProgressDialog;
import com.hw.baselibrary.util.LogUtils;
import com.hw.baselibrary.util.RxUtils;
import com.hw.ourlife.R;
import com.hw.ourlife.mvp.presenter.LoginPresenter;
import com.hw.ourlife.mvp.view.ILoginContract;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author huangqj
 */
public class MainActivity extends BasePresenterActivity<LoginPresenter> {

    private static final String TAG = MainActivity.class.getName();
    private ProgressDialog pDialog;
    private RxPermissions rxPermissions;

    @BindView(R.id.et_input_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenterView(new LoginCallback());
        pDialog = new ProgressDialog(this);
        presenter.initPassword();
        rxPermissions = new RxPermissions(this);
        requestPermissions();
        // useMap();
    }

    private Observable<String> obcreate() {
        return Observable.create(emitter -> {
            emitter.onNext("请求结果的数据。。。。");
            emitter.onComplete();
        });
    }

    private Observable<UserRep> map() {
        return obcreate().map(new UserRepFun()).compose(RxUtils.rxSchedulerHelper());
    }

    private void useMap() {
        map().subscribe(userRep -> Log.e(TAG, "useMap: " + userRep.toString()));
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        //所有的权限都已经授予
                        LogUtils.e(TAG, "requestPro", "所有权限被授予----->");
                    } else {
                        //有某一个权限被拒绝
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
    }

    @Override
    protected void initInject() {
        getActivityComponent(MainActivity.this).inject(MainActivity.this);
    }

    @OnClick({R.id.btn_login})
    void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String pwd = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    toast.showToastCenter("请输入密码");
                    break;
                }
                presenter.login(pwd);
                break;
            default:
                break;
        }
    }

    private class LoginCallback implements ILoginContract.ILoginView {
        @Override
        public void showErrorMsg(Exception e, String errorMsg) {
        }

        @Override
        public void showLoading() {
            pDialog.show();
        }

        @Override
        public void dismissLoading() {
            pDialog.dismiss();
        }

        @Override
        public void onLogin(boolean result) {
            toast.showToastCenter(result ? "登录成功" : "登录失败，密码不正确");
            if (result) {
                etPassword.setText("");
                startActivity(null, OurLifeActivity.class, true);
            }
        }
    }
}
