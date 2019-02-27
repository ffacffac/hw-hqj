package szu.wifichat.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import szu.wifichat.android.BaseApplication;

public class BTCommon {
    public static final byte MSG_M100CMDRESP = 0X08;
    public static final byte MSG_M100DATARESP_INV = 0X0B;
    public final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP服务UUID号
    //    public final static String MY_UUID = "fa87c0d0-afac-11de-8a39-0800200c9a66"; // SPP服务UUID号
    public Handler mHandler;
    public Context mContext;
    public BluetoothDevice mDevice = null; // 蓝牙设备
    public BluetoothSocket mSocket = null; // 蓝牙通信socket
    public BluetoothAdapter mBluetooth = null; // 获取本地蓝牙适配器，即蓝牙设备
    // public BluetoothAdapter mBluetooth = BluetoothAdapter.getDefaultAdapter(); // 获取本地蓝牙适配器，即蓝牙设备
    public IBTClientListener mIBTClientListener;// 蓝牙服务接口监听
    public BaseApplication mApplicationMember;

    public BTCommon(Context context) {
        this.mContext = context;
        mApplicationMember = (BaseApplication) context.getApplicationContext();
        this.mBluetooth = mApplicationMember.mBluetooth;
    }

    /**
     * 设置蓝牙监听
     */
    public void setIBTCListener(IBTClientListener iBTClientListener) {
        this.mIBTClientListener = iBTClientListener;
    }

    /**
     * 设置可以被搜索（打开蓝牙）
     */
    public boolean openBluetooth() {
        if (mBluetooth == null) {
            mBluetooth = BluetoothAdapter.getDefaultAdapter();// 如果没有获取本地蓝牙服务，则去获取
            mApplicationMember.mBluetooth = this.mBluetooth;
        }
        if (mBluetooth != null && mBluetooth.isEnabled() == false)// 如果未打开蓝牙，则打开蓝牙
        {
            return mBluetooth.enable();// 打开蓝牙
        }
        return false;
    }

    /**
     * 判断mIBTC是否为空
     *
     * @return
     */
    public boolean isNullIBTC() {
        if (mIBTClientListener != null) {
            return false;
        }
        return true;
    }

    /**
     * 得到蓝牙句柄
     *
     * @param address
     */
    public void getRemoteDevice(String address) {
        mDevice = mBluetooth.getRemoteDevice(address);
    }

    /**
     * 得到socket
     *
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public BluetoothSocket createRfcommSocketToServiceRecord() throws IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // (在PDA的时候)用服务号获取socket的方法，会导致断开连接后，然后第一次连接时会失败，
        // 但是在平板上的时候，这样就没问题，这种方式可以连接M100蓝牙扫描器，但是连接其他的就不行
        return mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));

        // 但是在平板上面在用这种方式连接socket，却连接不上
        // BluetoothSocket temp = null;
        // temp = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
        //以上取得socket方法不能正常使用， 用以下方法代替，但是这种方式不能连接M100蓝牙扫描器
        // BluetoothSocket temp = null;
        // Method m = mDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
        // temp = (BluetoothSocket) m.invoke(mDevice, 1);
        // mSocket = temp; // 怪异错误： 直接赋值给socket,对socket操作可能出现异常， 要通过中间变量temp赋值给socket

        // BluetoothSocket temp = null;
        // // temp
        // // =mTouchObject.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
        // // 以上取得socket方法不能正常使用， 用以下方法代替
        // Method m = mDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
        // temp = (BluetoothSocket) m.invoke(mDevice, 1);
        // mSocket = temp; // 怪异错误： 直接赋值给socket,对socket操作可能出现异常， 要通过中间变量temp赋值给socket
        // return mSocket;
    }

    /**
     * 连接socket
     *
     * @return 返回设备名
     * @throws IOException
     */
    public String socketConnect() throws IOException {
        mSocket.connect();
        return mDevice.getName();
    }

    /**
     * 关闭socket
     *
     * @return
     * @throws IOException
     */
    public void closeSocket() throws IOException {
        if (mSocket == null) {
            return;
        }
        mSocket.close();
        mSocket = null;
    }

    /**
     * 关闭蓝牙
     *
     * @throws IOException
     */
    public void closeBluetooth() throws IOException {
        if (mSocket != null) mSocket.close();// 关闭连接socket
        if (mBluetooth != null) mBluetooth.disable(); // 关闭蓝牙服务
    }

    /**
     * 设置socket为null
     *
     * @throws IOException
     */
    public void setSocketNull() throws IOException {
        mSocket.close();
        mSocket = null;
    }

    public void sendMsg(String msg) throws IOException {
        if (mSocket != null) {
            OutputStream os = mSocket.getOutputStream();//输出流
            if (os != null) {
                //往服务端写信息
                os.write(msg.getBytes("utf-8"));
            }
        }
    }

    //    public DataInputStream dataInputStream;
    //    public DataOutputStream dataOutputStream;
    //    public Handler handlerD = null;

    //    public void getOutputInputStream(Handler handler) {
    //        try {
    //            dataInputStream = new DataInputStream(mSocket.getInputStream());
    //            dataOutputStream = new DataOutputStream(mSocket.getOutputStream());
    //            this.handler = handler;
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }


    //            .fromString("00001101-0000-1000-8000-00805F9B34FB");//和客户端相同的UUID
    private final String NAME = "Bluetooth_Socket";
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;
    private InputStream is;//输入流
    AcceptThread acceptThread;

    //    //服务端监听客户端的线程类
    public class AcceptThread extends Thread {
        Handler handler = null;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public AcceptThread(Handler handler) {
            this.handler = handler;
            try {
                dataInputStream = new DataInputStream(mSocket.getInputStream());
                dataOutputStream = new DataOutputStream(mSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                Log.e("BT-------------.1", "hhhhhhhhhhh");
                Log.e("BT-------------.2", "hhhhhhhhhhh");
                //                while (isScaning) {
                while (isRunByHelm) {
                    Log.e("BT-------------.6", "hhhhhhhhhhh");
                    int type = dataInputStream.readInt();
                    int dataSize = dataInputStream.readInt();
                    byte[] data = new byte[dataSize];
                    int len = 0;
                    while (len < dataSize) {
                        len += dataInputStream.read(data, len, dataSize - len);
                    }
                    String str = new String(data);
                    str = str.replaceAll(" ", "");
                    str = str.toUpperCase();
                    Log.e("BT-------------.数据", str);
                    if (str != null && !str.equals("") && str.length() > 15) {
                        if (str.equals("005032570E02531460845C8E") || str.equals("0041026512003022302E7BA2") || str
                                .equals("005032570E026580A39C1581") || str.equals("0041026512003822602E6C9C")) {
                            isRunByHelm = false;
                            Message msg = new Message();
                            msg.obj = str;
                            handler.sendMessage(msg);
                        }
                    }
                }
                //                }
            } catch (Exception e) {
                Log.e("BT-------------.3", "hhhhhhhhhhh");
                if (e == null) return;
                String s = e.getMessage();
                String dd = s;
            }
            Log.e("BT-------------.4", "hhhhhhhhhhh");
        }
    }

    public boolean isRunByHelm = true;
    public boolean isScaning = true;

    public void stopMessageThreadByHelm(boolean isd) {
        isRunByHelm = false;
    }
}
