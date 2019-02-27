package szu.wifichat.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import szu.wifichat.android.conf.Key;

public class BTReceiver extends BroadcastReceiver {
    public static final String TAG = "BTReceiver";
    public IBTClientListener mIBTClientListener;// 蓝牙服务接口监听

    public BTReceiver() {
    }

    public void setIBTClientListener(IBTClientListener iBTClientListener) {
        this.mIBTClientListener = iBTClientListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))// 蓝牙开关状态的监听
        {
            int state = bundle.getInt(BluetoothAdapter.EXTRA_STATE);
            if (state == BluetoothAdapter.STATE_OFF) {
                mIBTClientListener.onBTCloseListener();// 蓝牙已被关闭
            } else if (state == BluetoothAdapter.STATE_ON) {
                mIBTClientListener.onBTOpenListener();// 蓝牙打开
            }
        }
        // 蓝牙断开
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            mIBTClientListener.onDisconnectListener(true);// 蓝牙断开监听
        }
        // 蓝牙连接
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            // Log.e(TAG, "蓝牙连接成功");
            // mIBTClientListener.onConnectListener(true);
        }
        // 获取蓝牙地址
        if (action.equals(Key.BT_GET_ADDRESS)) {
            String address = intent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            mIBTClientListener.onGetAddressListener(address);
        }
        // 通过广播发送标签数据
        if (action.equals(Key.BT_SEND_EPC_DATA_BY_BROADCAS)) {
            String epc = intent.getStringExtra(Key.BT_EPC_DATA_BY_BROADCAST);
            mIBTClientListener.getEPCDataByBroadcast(0, epc);
        }
    }
}
