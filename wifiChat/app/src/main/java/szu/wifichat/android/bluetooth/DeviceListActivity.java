package szu.wifichat.android.bluetooth;

/*
 * Copyright (C) 2009 The Android Open Source Project Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import szu.wifichat.android.BaseApplication;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.Key;

public class DeviceListActivity extends Activity
{
    public static String EXTRA_DEVICE_ADDRESS = "address"; // 返回时数据标签
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private BaseApplication mApplicationMember;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // 创建并显示窗口
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // 设置窗口显示模式为窗口方式
        setContentView(R.layout.device_list);

        // 设定默认返回值为取消
        setResult(Activity.RESULT_CANCELED);

        // 设定扫描按键响应
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                doDiscovery();// 查找设备
                v.setVisibility(View.GONE);
            }
        });

        // 初使化设备存储数组
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // 设置已配队设备列表
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // 设置新查找设备列表
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // 注册接收查找到设备action接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // this.registerReceiver(mReceiver, filter);
        // 注册查找结束action接收器
        // filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mApplicationMember = (BaseApplication) getApplicationContext();
        if (mApplicationMember.mBluetooth != null)
        {
            this.mBtAdapter = mApplicationMember.mBluetooth;
        }
        else
        {
            mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // 得到本地蓝牙句柄
        }
        // 得到已配对蓝牙设备列表
        // Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // 添加已配对设备到列表并显示
        // if (pairedDevices.size() > 0) {
        // findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
        // for (BluetoothDevice device : pairedDevices) {
        // mPairedDevicesArrayAdapter.add(device.getName() + "\n" +
        // device.getAddress());
        // }
        // } else {
        // String noDevices = "No devices have been paired";
        // mPairedDevicesArrayAdapter.add(noDevices);
        // }

        List<String> pairedDevices = getPairedDeviceAddress();// 获取已配对的蓝牙设备地址
        if (pairedDevices != null && pairedDevices.size() > 0)
        {
            setTitle("已连接设备");
            mPairedDevicesArrayAdapter.addAll(pairedDevices);
        }

        // if (pairedDevices != null && pairedDevices.size() == 1)
        // {
        // selectBluetoothDevice(pairedDevices.get(0));
        // }

        if (pairedDevices == null)
        {
            doDiscovery();// 如果设备未连接过蓝牙，一进入就自动去查找
        }
    }

    /**
     * 获取已配对的蓝牙设备地址
     *
     * @return
     */
    private List<String> getPairedDeviceAddress()
    {
        List<String> pairedDevices = null;
        Set<BluetoothDevice> bondeds = mBtAdapter.getBondedDevices();// 获取已配对的蓝牙设备地址
        if (bondeds != null && !bondeds.isEmpty())
        {
            pairedDevices = new ArrayList<String>();
            for (BluetoothDevice bluetoothDevice : bondeds)
            {
                String strNameAndAddress = bluetoothDevice.getName() + "\n"
                        + bluetoothDevice.getAddress();
                pairedDevices.add(strNameAndAddress);
                // boolean ischeck = BluetoothAdapter.checkBluetoothAddress(bluetoothDevice
                // .getAddress());//检查地址是否有效
            }
        }
        return pairedDevices;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mBtAdapter != null)
        {
            mBtAdapter.cancelDiscovery();// 关闭服务查找
        }
        this.unregisterReceiver(mReceiver);// 注销action接收器
    }

    public void OnCancel(View v)
    {
        finish();
    }

    /**
     * 开始服务和设备查找
     */
    private void doDiscovery()
    {
        mPairedDevicesArrayAdapter.clear();
        mNewDevicesArrayAdapter.clear();
        mPairedDevicesArrayAdapter.notifyDataSetChanged();
        mNewDevicesArrayAdapter.notifyDataSetChanged();
        // 在窗口显示查找中信息
        setProgressBarIndeterminateVisibility(true);// 显示加载动画
        setTitle(R.string.listsearching);// 这是Activity的头部
        // 显示其它设备（未配对设备）列表
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // 关闭再进行的服务查找
        if (mBtAdapter.isDiscovering())
        {
            mBtAdapter.cancelDiscovery();
        }
        // 并重新开始
        mBtAdapter.startDiscovery();
    }

    // 选择设备响应函数
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
        {
            selectBluetoothDevice(((TextView) v).getText().toString());
        }
    };

    /**
     * 选择设备
     *
     * @param bluettoothAddress
     */
    private void selectBluetoothDevice(String bluettoothAddress)
    {
        mBtAdapter.cancelDiscovery();// 准备连接设备，关闭服务查找
        String info = bluettoothAddress;// 得到mac地址
        if (info.length() < 17)
        {
            finish();
            return;
        }
        String address = info.substring(info.length() - 17);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);// 返回地址数据
        setResult(Key.GET_BT_ADDRESS_RESULT_OK, intent);// 设置返回值并结束程序

        // Intent intent2 = new Intent(Key.BT_GET_ADDRESS);
        // intent2.putExtra(EXTRA_DEVICE_ADDRESS, address);
        // sendBroadcast(intent2);// 发送广播
        this.finish();
    }

    // 查找到设备和搜索完成action监听器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            // 查找到设备action
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // 得到蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果是已配对的则略过，已得到显示，其余的在添加到新设备列表中进行显示
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                else
                { // 添加到已配对设备列表
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    // 自动连接
//                     selectBluetoothDevice(device.getName() + "\n" + device.getAddress());// 自动选择连接
                }
            }
            // 完成设备搜索，搜索完成action
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.listselectdevtoconn);
                if (mNewDevicesArrayAdapter.getCount() == 0)
                {
                    String noDevices = context.getResources().getString(R.string.listnotgetdev);// 没有查找到设备
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                // if(mPairedDevicesArrayAdapter.getCount() > 0)
                // findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            }
        }
    };

}
