package com.hw.ourlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hw.baselibrary.util.Toast;
import com.hw.ourlife.MyApplication;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public abstract class BaseActivity extends AbstractSimpleActivity {

    protected Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = MyApplication.inject().myApplication().toast;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }

    protected void startActivity(Bundle bundle, Class cls, boolean finish) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(com.hw.baselibrary.R.anim.push_right_in, com.hw.baselibrary.R.anim.push_right_out);
        if (finish) {
            this.finishActivity();
        }
    }

    protected void startActivity(Bundle bundle, Class cls) {
        this.startActivity(bundle, cls, false);
    }

    protected void startActivityForResult(Bundle bundle, Class cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(com.hw.baselibrary.R.anim.push_right_in, com.hw.baselibrary.R.anim.push_left_out);
    }

    protected void finishActivity() {
        finish();
        overridePendingTransition(com.hw.baselibrary.R.anim.push_left_in, com.hw.baselibrary.R.anim.push_right_out);
    }
}
