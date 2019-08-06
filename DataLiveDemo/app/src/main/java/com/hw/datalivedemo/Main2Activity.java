package com.hw.datalivedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.hw.datalivedemo.datalive.LiveDataBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * @author huangqj
 */
public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getName();
    public static final String SEND_KEY = "key_send_msg";
    public static final String SEND_KEY1 = "key_send_msg1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Timber.tag(TAG);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_send_msg) {
            String msg = ((EditText) findViewById(R.id.et_input_msg)).getText().toString();
            String format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.CHINA).format(new Date());
            if (msg.isEmpty()) {
                msg = "默认消息";
            }
            msg = msg + "：" + format;
            Timber.e("onClick: 发送消息：%s", msg);
            LiveDataBus.getInstance().with(SEND_KEY).postValue(msg);
            LiveDataBus.getInstance().with(SEND_KEY1).postValue("第二条消息：123--" + format);
        }
    }
}
