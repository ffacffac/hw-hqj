package szu.wifichat.android.bluetooth;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import android_serialport_api.M100_RFID_API;

public class M100BTManager extends BTCommon {

    public M100_RFID_API mRfid;

    private static M100BTManager mM100BTManager;

    public static M100BTManager getInstance(Context context) {
        if (mM100BTManager == null) {
            mM100BTManager = new M100BTManager(context);
        }
        return mM100BTManager;
    }

    public M100BTManager(Context context) {
        super(context);
        this.mHandler = this.handler;
        this.mContext = context;
        mRfid = new M100_RFID_API();// M100类型的
        openBluetooth();
    }

    /**
     * 连接
     *
     * @throws IOException
     */
    public void onConnect() throws IOException {
        // 蓝牙还没打开
        if (mBluetooth.isEnabled() == false)// 如果蓝牙没打开，则打开蓝牙
        {
            if (openBluetooth())// 打开蓝牙成功
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 如未连接设备则打开DeviceListActivity进行设备搜索
                        if (mSocket == null) {
                            if (!isNullIBTC()) {
                                mIBTClientListener.onSearchListener(true);// 进入搜索蓝牙界面
                            }
                        }
                    }
                }, 1800);
            } else {
                if (!isNullIBTC()) {
                    mIBTClientListener.onSearchListener(false);// 蓝牙服务不可用则提示
                }
            }
            return;
        }
        // 如未连接设备则打开DeviceListActivity进行设备搜索
        if (mSocket == null) {
            if (!isNullIBTC()) {
                mIBTClientListener.onSearchListener(true);// 进入搜索蓝牙界面
            }
        }
    }

    /**
     * 盘点
     *
     * @param infTimeinterval //盘点间隔时间
     * @throws IOException
     */
    public void onInfCyc(int infTimeinterval) throws IOException {
        // 如果蓝牙未连接则提示
        if (mSocket == null) {
            if (!isNullIBTC()) {
                mIBTClientListener.onInfCycListener(false);// 蓝牙未连接，盘点失败
            }
            return;
        }
        mRfid.M100SetMessageHandler(mHandler);
        mRfid.M100StartInvTag(infTimeinterval);// 盘点频率，盘点间隔时间，如：100ms，时间越小，盘点频率越快
        mIBTClientListener.onInfCycListener(true);// 开始盘点
    }

    /**
     * 停止盘点
     *
     * @throws IOException
     */
    public void onStopInfCyc() throws IOException {
        // 如果蓝牙未连接则提示
        if (mSocket == null) {
            if (!isNullIBTC()) {
                mIBTClientListener.onStopInfCycListener(false);
            }
            return;
        }
        mRfid.M100SetMessageHandler(mHandler);
        mRfid.M100StopInvTag();
        if (!isNullIBTC()) {
            mIBTClientListener.onStopInfCycListener(true);// 停止盘点成功
        }
    }

    /**
     * 停止盘点,不需要监听
     *
     * @throws IOException
     */
    public void onStopInfCycNoListener() throws IOException {
        // 如果蓝牙未连接则提示
        if (mSocket == null) {
            return;
        }
        mRfid.M100SetMessageHandler(mHandler);
        mRfid.M100StopInvTag();
    }

    /**
     * 断开连接
     *
     * @throws IOException
     */
    public void disconnectM100() throws IOException {
        mRfid.M100_CloseBluetoothSocket();// 断开与蓝牙设备的连接
        if (mSocket == null) {
            return;
        }
        mSocket.close();
        mSocket = null;
    }

    /**
     * 断开与disconnectM100的连接
     *
     * @throws IOException
     */
    public void disconnectM100NoListener() throws IOException {
        onStopInfCycNoListener();// 停止扫描（读标签）
        mRfid.M100_CloseBluetoothSocket();// DisConnected
        if (mSocket == null) {
            return;
        }
        mSocket.close();
        mSocket = null;
    }

    /**
     * 当M100蓝牙扫描器被关闭时，要断开与手机的连接
     *
     * @throws IOException
     */
    public void disconnectByM100Off() throws IOException {
        if (mSocket == null)// 如果这时mSocket==null,则说明已经从手机上关闭了，不需要再去关闭
        {
            return;
        } else {
            onStopInfCycNoListener();// 停止扫描（读标签）
            mRfid.M100_CloseBluetoothSocket();// DisConnected
            mSocket.close();
            mSocket = null;
        }
    }

    /**
     * 打开接收线程
     *
     * @throws IOException
     */
    public void startMessageThread() throws IOException {
        mRfid.M100_SetBluetoothSocket(mSocket, 1);// 设置蓝牙Socket 1
        mRfid.M100SetMessageHandler(mHandler);
    }

    /**
     * 出来扫描到的标签数据
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_M100DATARESP_INV:
                    Bundle x6b4 = msg.getData();
                    String x6EPCstr4 = x6b4.getString("InvData");
                    byte[] x6invdatabyte = x6b4.getByteArray("InvDatabyte");
                    String x6invtt = "";
                    for (int i = 0; i < x6invdatabyte.length; i++) {
                        x6invtt += String.format("%1$02x", x6invdatabyte[i]);// 格式化
                    }
                    if (!isNullIBTC()) {
                        mIBTClientListener
                                .getEPCData(MSG_M100DATARESP_INV, x6invtt.trim().toUpperCase());// 回调扫描到的数据，统一转换成大写
                    }
                    break;
                case MSG_M100CMDRESP:
                    Bundle x6ver = msg.getData();
                    String x6HWver = x6ver.getString("HWver");
                    String x6SWver = x6ver.getString("SWver");
                    System.out.println("x6HWver: " + x6HWver);
                    System.out.println("x6SWver: " + x6SWver);
                    break;
                case CONNECT_FAIL:
                    mIBTClientListener.onConnectListener(false);//连接失败
                    break;
                case CONNECT_SUCCESS:
                    mIBTClientListener.onConnectListener(true);//连接成功
                    try {
                        startMessageThread(); // 打开接收线程
                    } catch (IOException e) {
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static final int CONNECT_SUCCESS = 14;
    public static final int CONNECT_FAIL = 13;

    private void sendMsg(int what) {
        handler.sendMessage(handler.obtainMessage(what));
    }

    private class ConnectBTRun implements Runnable {
        String address;

        public ConnectBTRun(String address) {
            this.address = address;
        }

        @Override
        public void run() {
            try {
                // 连接成功，由DeviceListActivity设置返回
                // MAC地址，由DeviceListActivity设置返回
                getRemoteDevice(address); // 得到蓝牙设备句柄
                createRfcommSocketToServiceRecord();// 得到socket
            } catch (IOException e) {
                sendMsg(CONNECT_FAIL);//连接失败
            } catch (NoSuchMethodException e) {
                sendMsg(CONNECT_FAIL);
            } catch (IllegalAccessException e) {
                sendMsg(CONNECT_FAIL);
            } catch (IllegalArgumentException e) {
                sendMsg(CONNECT_FAIL);
            } catch (InvocationTargetException e) {
                sendMsg(CONNECT_FAIL);
            }
            try {
                // 连接成功
                String deviceName = socketConnect();// 连接socket
                sendMsg(CONNECT_SUCCESS);//连接成功
            } catch (IOException e) {
                try {
                    closeSocket();// 关闭socket
                    sendMsg(CONNECT_FAIL);
                } catch (IOException ee) {
                    sendMsg(CONNECT_FAIL);
                }
                return;
            }
        }
    }


    /**
     * 连接蓝牙设备
     */
    public void connectBT(final String address) {
        new Thread(new ConnectBTRun(address)).start();
        // try {
        //     // 连接成功，由DeviceListActivity设置返回
        //     // MAC地址，由DeviceListActivity设置返回
        //     getRemoteDevice(address); // 得到蓝牙设备句柄
        //     createRfcommSocketToServiceRecord();// 得到socket
        // } catch (IOException e) {
        //     mIBTClientListener.onConnectListener(false);//连接失败
        // } catch (NoSuchMethodException e) {
        //     mIBTClientListener.onConnectListener(false);//连接失败
        // } catch (IllegalAccessException e) {
        //     mIBTClientListener.onConnectListener(false);//连接失败
        // } catch (IllegalArgumentException e) {
        //     mIBTClientListener.onConnectListener(false);//连接失败
        // } catch (InvocationTargetException e) {
        //     mIBTClientListener.onConnectListener(false);//连接失败
        // }
        // try {
        //     // 连接成功
        //     String deviceName = socketConnect();// 连接socket
        //     mIBTClientListener.onConnectListener(true);//连接成功
        // } catch (IOException e) {
        //     try {
        //         closeSocket();// 关闭socket
        //         mIBTClientListener.onConnectListener(false);//连接失败
        //     } catch (IOException ee) {
        //         mIBTClientListener.onConnectListener(false);//连接失败
        //     }
        //     return;
        // }
        // try {
        //     startMessageThread(); // 打开接收线程
        // } catch (IOException e) {
        //     // Toast.makeText(this, getResources().getString(R.string.info_revfail), Toast.LENGTH_SHORT).show();
        //     return;
        // }
    }
}
