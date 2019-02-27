package szu.wifichat.android.finger;

import android.os.Handler;
import android.os.Message;

import cn.pda.fingerprint.FingerPrintCommandManager;

/**
 * Created by caigh on 2017/6/16.
 */

public class PdaFingerUtil {

    private static final int WHAT_SCAN = 3;
    /**
     * 扫描指纹的时间间隔(ms)
     */
    private static final int SCAN_SPAN = 200;

    private static Callback mCallback;
    private static FingerPrintCommandManager fpManager; //指纹操作句柄
//    private static DBServer dbServer;  //数据库操作
//    private static List<FingerData> listFinger; // 数据库指纹数据
    private static byte[] templet = null; // 指纹模板

    public static void init(Callback callback) {
        mCallback = callback;
        fpManager = new FingerPrintCommandManager(fpHandler);
//        dbServer = new DBServer();
        //先将数据库中的数据查询出来
//        listFinger = dbServer.queryAllData();
    }

    public static boolean openDevice() {
        return fpManager.open();
    }

    public static void closeDevice() {
        fpManager.close();
    }

    /**
     * @param waitTime 最大等待时间(ms)
     */
    public static void startScan(final int waitTime) {
        Message message = scanHandler.obtainMessage(WHAT_SCAN);
        message.arg1 = waitTime;
        scanHandler.sendMessage(message);
    }

    private static Handler fpHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String data = msg.getData().getString("msg");
            if (data != null) {
                mCallback.onGetMessage(data);
            }
        }

        ;
    };

    private static Handler scanHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SCAN:
                    int remain = msg.arg1;//剩余时间ms

                    if (remain < 0) {
                        mCallback.timeOut();
                        return;
                    }
                    //将指纹模板生成在BUFFER_A
                    int buffid = FingerPrintCommandManager.BUFFER_A;
                    //生成模板
                    if (fpManager.genChara(buffid, false)) {
                        templet = fpManager.getChara(buffid);
                        mCallback.onGetFinger(templet);
                    } else {
                        Message message = scanHandler.obtainMessage(WHAT_SCAN);
                        message.arg1 = remain - SCAN_SPAN;
                        scanHandler.sendMessageDelayed(message, SCAN_SPAN);
                        mCallback.onScanning(message.arg1);
                    }
                    break;
            }
        }
    };

    public static interface Callback {

        void onGetMessage(String message);

        void timeOut();

        void onGetFinger(Object fingerData);

        void onScanning(int remainTime);
    }

}
