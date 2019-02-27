package szu.wifichat.android.httputils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import szu.wifichat.android.conf.Configs;
import szu.wifichat.android.interfaces.IGroundRequest;
import szu.wifichat.android.util.JsonUtils;

/**
 * Created by huangqj on 2017-06-19.
 */

public class GrounderRequest extends Thread {

    private final String TAG_GrounderRequest = "GrounderRequest";
    private IGroundRequest mIGroundRequest;
    private boolean isRequest = true;//请求或者结束请求线程
    public final int MSG_WHAT = 10;
    private int sleepTime = 4000;

    public GrounderRequest(IGroundRequest iGroundRequest) {
        this.mIGroundRequest = iGroundRequest;
    }

    @Override
    public void run() {
        super.run();
        while (isRequest) {
            try {
                String json = OkHttpClientManager.getAsString(Configs.GET_GROUND_STATE);
                if (json != null && !json.isEmpty()) {
                    GroundState groundState = JsonUtils.getObject(json, GroundState.class);
                    sendMessage(groundState);
                }
                sleep(sleepTime);//5秒钟去轮询一次
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始请求
     */
    public void startRequest() {
        isRequest = true;
        Log.e(TAG_GrounderRequest, "地线端开始轮询。。。");
    }

    /**
     * 开始请求
     */
    public void stopRequest() {
        isRequest = false;
        mIGroundRequest = null;
        Log.e(TAG_GrounderRequest, "地线端停止轮询。。。");

    }

    /**
     * 判断线程是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return this.isRequest;
    }

    private void sendMessage(GroundState groundState) {
        Message msg = new Message();
        msg.what = MSG_WHAT;
        msg.obj = groundState;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    if (mIGroundRequest != null) {
                        mIGroundRequest.response((GroundState) msg.obj);
                    }
                    break;
            }
        }
    };
}
