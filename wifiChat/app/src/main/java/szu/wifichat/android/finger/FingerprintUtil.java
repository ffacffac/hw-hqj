package szu.wifichat.android.finger;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.za.finger.FingerHelper;
import com.za.finger.IUsbConnState;

import cn.pda.serialport.Tools;

/**
 * Created by caigh on 2017/6/15.
 */

public class FingerprintUtil {
    private static final String TAG = FingerprintUtil.class.getSimpleName();

    private static final int WHAT_GET_FEATURE = 3;

    /** 扫描指纹的时间间隔(ms) */
    private static final int SCAN_SPAN = 200;

    private static FingerHelper mFingerHelper;  //option finger
    private static Callback mCallback;

    private static Handler mHandler = new MyHandler();

    /**
     * @param activity 用于activity.getApplicationContext();
     * @param callback 用于activity.getApplicationContext();
     * @param needOpen 马上打开设备
     */
    public static void init(Activity activity, Callback callback, boolean needOpen) {
        mFingerHelper = new FingerHelper(activity, new MyUsbCallback());
        mCallback = callback;
        if (needOpen)
        openDevice();
    }

    public static void openDevice() {
        if(mFingerHelper == null) {
            Log.e(TAG, "必须先初始化工具类");
            return;
        }
        mFingerHelper.init();
    }

    public static void closeDevice() {
        if(mFingerHelper == null) {
            Log.e(TAG, "必须先初始化工具类");
            return;
        }
        mFingerHelper.close();
    }

    /**
     * @param waitTime 最大等待时间(ms)
     * */
    public static void getFeature(int waitTime) {
        Message message = mHandler.obtainMessage(WHAT_GET_FEATURE);
        message.arg1 = waitTime;
        mHandler.sendMessage(message);
    }


    private static class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_FEATURE:
                    int remain = msg.arg1;//剩余时间ms
                    log("WHAT_GET_FEATURE", remain);

                    if(remain < 0) {
                        mCallback.timeOut();
                        return;
                    }

                    int state = mFingerHelper.getImage();
                    //find finger
                    if (state == mFingerHelper.PS_OK) {
                        //gen char to bufferA
                        state = mFingerHelper.genChar(mFingerHelper.CHAR_BUFFER_A);
                        if (state == mFingerHelper.PS_OK) {
                            int[] iCharLen = {0, 0} ;
                            byte[] charBytes = new byte[512];
                            //upload char
                            state = mFingerHelper.upCharFromBufferID(mFingerHelper.CHAR_BUFFER_A, charBytes, iCharLen);
                            if (state == mFingerHelper.PS_OK) {
                                //upload success
                                mCallback.onGetFeature(Tools.Bytes2HexString(charBytes, 512));
                            }
                        }else{
                            //char is bad quickly
                            mCallback.onGetFeatureBad();
                            return ;
                        }
                    } else if (state == mFingerHelper.PS_NO_FINGER) {
                        Message message = mHandler.obtainMessage(WHAT_GET_FEATURE);
                        message.arg1 = remain - SCAN_SPAN;
                        mHandler.sendMessageDelayed(message, SCAN_SPAN);
                        mCallback.onScanning(message.arg1);
                    } else if (state == mFingerHelper.PS_GET_IMG_ERR) {
                        mCallback.onGetImageError();
                        return ;
                    }else{
                        mCallback.onGetDeviceError();
                        return ;
                    }
                    break;
            }
        }
    }

    private static class MyUsbCallback implements IUsbConnState {
        public void onUsbConnected() {
            int state = mFingerHelper.connectFingerDev();
            if (state == mFingerHelper.CONNECT_OK) {
                mCallback.onConnectDevice(true, state);
            } else {
                mCallback.onConnectDevice(false, state);
            }
        }

        public void onUsbPermissionDenied() {
            if(mCallback.onUsbPermissionDenied())
                mFingerHelper.init();
        }

        public void onDeviceNotFound() {
            mCallback.onDeviceNotFound();
        }
    }

    public static interface Callback {
        void onScanning(int remainTime);

        void onGetFeature(String featureCode);

        void onGetFeatureBad();

        void timeOut();

        void onGetImageError();

        void onGetDeviceError();

        void onConnectDevice(boolean isConnectSuccess, int errorCode);

        /** @return need auto request again */
        boolean onUsbPermissionDenied();

        void onDeviceNotFound();
    }

//    /*
    static int count;
    static String C = ", ";
    static void log(Object... o) {
        count++;
        StringBuilder sb = new StringBuilder();
        sb.append("log."+count+" [");
        for(Object oo:o) {
            sb.append((oo == null? null:oo.toString())+C);
        }
        Log.e(TAG, sb.toString());
    }
    /**/

}
