package com.hw.datalivedemo;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hw.datalivedemo.datalive.LiveDataBus;

import timber.log.Timber;

/**
 * @author huangqj
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.tag(TAG);
        LiveDataBus.getInstance().with(Main2Activity.SEND_KEY, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Timber.e("onChanged: 接收到消息：%s", s);
                ((TextView) findViewById(R.id.tv_rev_msg)).setText(s);
            }
        });
        LiveDataBus.getInstance().with(Main2Activity.SEND_KEY1, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Timber.e("onChanged: 接收到消息：%s", s);
                ((TextView) findViewById(R.id.tv_rev_msg)).append("\n" + s);
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_to) {
            startActivity(new Intent(this, Main2Activity.class));
        }
    }
}
