package szu.wifichat.android.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import szu.wifichat.android.BaseActivity;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.Configs;
import szu.wifichat.android.conf.ConstData;
import szu.wifichat.android.httputils.GroundState;
import szu.wifichat.android.httputils.OkHttpClientManager;
import szu.wifichat.android.interfaces.IGroundUpload;
import szu.wifichat.android.util.JsonUtils;

public class HttpTestActivity extends BaseActivity {

    @BindView(R.id.btn_send_data_asyn)
    Button btnSendDataSncy;//异步
    @BindView(R.id.btn_send_data_sync)
    Button btnSendDataSync;//同步
    @BindView(R.id.tv_data)
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.btn_send_data_asyn, R.id.btn_send_data_sync})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_send_data_asyn:
//                String url = new StringBuffer().append(Configs.GROUND_UPLOAD).append(1).append
//                        ("L101#").append(WorkEnum.DeviceState
//                        .WnderWay.getState()).toString();
                OkHttpClientManager.getInstance().getAsyn1(ConstData.beiJing, new IGroundUpload() {
//                OkHttpClientManager.getInstance().getAsyn1(url, new IGroundUpload() {

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e("HTTP_TEST请求失败----", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) throws IOException {
                        Log.e("HTTP_TEST请求成功----", response);
                    }
                });
                break;
            case R.id.btn_send_data_sync:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //同步请求
//                            String str = OkHttpClientManager.getAsString(ConstData.beiJing);
                            String str = OkHttpClientManager.getAsString(Configs.GET_GROUND_STATE);
                            Log.e("请求到数据：====str", str);
                            if (str != null) {
                                GroundState groundState = JsonUtils.getObject(str, GroundState
                                        .class);
                                Log.e("请求到数据：====", groundState.getWorkContent());
                                Log.e("请求到数据：====", groundState.getWorkers());
                                Log.e("请求到数据：====", groundState.getWorkPlace());
                            }
//                            tvData.setText(str);
//                            Log.e("请求到数据：====", str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }


    @Override
    public void processMessage(Message msg) {

    }
}
