package szu.wifichat.android.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 蓝牙管理类，由于可能有不同的蓝牙扫描器，因此建立一个工厂类来选择不同的蓝牙服务
 *
 * @author huangqj
 */
public class BTFactoryManager {

    private M100BTManager mM100btManager;

    private static BTFactoryManager mBTBtFactoryManager;

    public static BTFactoryManager getInstance(Context context, IBTClientListener iBTClientListener) {
        if (mBTBtFactoryManager == null) {
            synchronized (BTFactoryManager.class) {
                if (mBTBtFactoryManager == null) {
                    mBTBtFactoryManager = new BTFactoryManager(context, iBTClientListener);
                }
            }
        }
        return mBTBtFactoryManager;
    }

    /**
     * 创建对应蓝牙实例
     */
    public BTFactoryManager(Context context, IBTClientListener iBTClientListener) {
        mM100btManager = M100BTManager.getInstance(context);
        setIBTCListener(iBTClientListener);
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        mM100btManager.openBluetooth();
    }

    /**
     * 断开连接
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        mM100btManager.disconnectM100();
    }

    /**
     * 从手机直接断开连接
     *
     * @throws IOException
     */
    public void disconnectM100NoListener() throws IOException {
        mM100btManager.disconnectM100NoListener();
    }

    /**
     * 当M100蓝牙扫描器被关闭时，要断开与手机的连接
     *
     * @throws IOException
     */
    public void disconnectByM100Off() throws IOException {
        mM100btManager.disconnectByM100Off();
    }

    /**
     * 设置蓝牙监听
     */
    public void setIBTCListener(IBTClientListener iBTClientListener) {
        mM100btManager.setIBTCListener(iBTClientListener);
    }

    /**
     * 连接
     *
     * @throws IOException
     */
    public void onConnect() throws IOException {
        mM100btManager.onConnect();
    }

    /**
     * 盘点
     *
     * @throws IOException
     */
    public void onInfCyc(int infTimeinterval) throws IOException {
        mM100btManager.onInfCyc(infTimeinterval);
    }

    /**
     * 盘点
     *
     * @throws IOException
     */
    public void onInfCyc() throws IOException {
        mM100btManager.onInfCyc(500);// 数值为盘点频率
    }

    /**
     * 停止盘点
     *
     * @throws IOException
     */
    public void onStopInfCyc() throws IOException {
        mM100btManager.onStopInfCyc();
    }

    /**
     * 停止盘点,不需要监听
     *
     * @throws IOException
     */
    public void onStopInfCycNoListener() throws IOException {
        mM100btManager.onStopInfCycNoListener();
    }

    /**
     * 得到蓝牙句柄
     *
     * @param address
     */
    public void getRemoteDevice(String address) {
        mM100btManager.getRemoteDevice(address);
    }

    /**
     * 用服务号得到socket
     *
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public BluetoothSocket createRfcommSocketToServiceRecord() throws IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return mM100btManager.createRfcommSocketToServiceRecord();
    }

    /**
     * 连接socket
     *
     * @return
     * @throws IOException
     */
    public String socketConnect() throws IOException {
        return mM100btManager.socketConnect();
    }

    /**
     * 关闭socket
     *
     * @return
     * @throws IOException
     */
    public void closeSocket() throws IOException {
        mM100btManager.closeSocket();
    }

    /**
     * 设置socket为null
     *
     * @throws IOException
     */
    public void setSocketNull() throws IOException {
        mM100btManager.setSocketNull();
    }

    /**
     * 打开接收线程
     *
     * @throws IOException
     */
    public void startMessageThread() throws IOException {
        mM100btManager.startMessageThread();
    }

    /**
     * 关闭整个蓝牙服务
     *
     * @throws IOException
     */
    public void closeBluetooth() throws IOException {
        mM100btManager.closeBluetooth();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) throws IOException {
        mM100btManager.sendMsg(msg);
    }

    /**
     * 连接蓝牙设备
     */
    public void connectBT(String address) {
        mM100btManager.connectBT(address);
    }
}
