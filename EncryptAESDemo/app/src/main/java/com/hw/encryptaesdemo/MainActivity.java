package com.hw.encryptaesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private Button btnEncrypt;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEncrypt = findViewById(R.id.btn_exc_encrypt);
        tvInfo = findViewById(R.id.tv_encrypt);
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String encryptCS5 = EncryptUtils.Encrypt("测试5");
                    String encrypt1 = EncryptUtils.Encrypt("1");
                    String decryptCS5 = EncryptUtils.Decrypt(encryptCS5);
                    String decrypt1 = EncryptUtils.Decrypt(encrypt1);
                    tvInfo.setText("");
                    tvInfo.append("测试5--加密：" + encryptCS5 + "\n");
                    tvInfo.append("1--加密：" + encrypt1 + "\n");
                    tvInfo.append("---------------------------------\n");
                    tvInfo.append("测试5--解密：" + decryptCS5 + "\n");
                    tvInfo.append("1--解密：" + decrypt1 + "\n");

                    Log.e(TAG, "onClick: " + "测试5--加密：" + encryptCS5 + "\n");
                    Log.e(TAG, "onClick: " + "1--加密：" + encrypt1 + "\n");
                    Log.e(TAG, "onClick: " + "---------------------------------");
                    Log.e(TAG, "onClick: " + "测试5--解密：" + decryptCS5 + "\n");
                    Log.e(TAG, "onClick: " + "1--解密：" + decrypt1 + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }
}
