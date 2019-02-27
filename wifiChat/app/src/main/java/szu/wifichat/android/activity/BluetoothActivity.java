package szu.wifichat.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import szu.wifichat.android.R;
import szu.wifichat.android.bluetooth.BTBaseActivity;
import szu.wifichat.android.bluetooth.BTFactoryManager;
import szu.wifichat.android.bluetooth.DeviceListActivity;
import szu.wifichat.android.bluetooth.IBTClientListener;
import szu.wifichat.android.conf.Key;
import szu.wifichat.android.view.DialogHelper;

public class BluetoothActivity extends BTBaseActivity implements IBTClientListener {

    @BindView(R.id.iv_bluetooth_connect)
    ImageView ivConnect;
    @BindView(R.id.tv_scan_label)
    TextView tvScanLabel;
    @BindView(R.id.btn_scan)
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        mBTFactoryManager = BTFactoryManager.getInstance(this, this);
        mBTReceiver.setIBTClientListener(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void processMessage(Message msg) {

    }

    @OnClick({R.id.iv_bluetooth_connect, R.id.btn_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bluetooth_connect:
                try {
                    if (!isConnecting) {
                        mBTFactoryManager.onConnect();// 打开蓝牙连接,在按一次就是断开连接
                    } else {
                        mBTFactoryManager.disconnect();//断开连接
                    }
                } catch (IOException e) {
                    DialogHelper.getInstance(this).showDialogError("连接蓝牙出现异常！");
                }
                break;
            case R.id.btn_scan:
                try {
                    if (!isConnecting) return;
                    if (isScanning) {
                        stopInv(true);
                        btnScan.setText("开始扫描");
                    } else {
                        startInv();
                        btnScan.setText("停止扫描");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSearchListener(boolean isSearch) {
        if (isSearch) {
            Intent intent = new Intent(this, DeviceListActivity.class); // 跳转程序设置
            startActivityForResult(intent, REQUEST_CONNECT_DEVICE); // 设置返回宏定义
        } else {
            showBTToast(getResources().getString(R.string.info_nobt), R.drawable.icon_r);// 如果蓝牙服务不可用则提示
        }
    }

    @Override
    public void onConnectListener(boolean isConnect) {
        DialogHelper.getInstance(BluetoothActivity.this).cancelProgressDialog();
        if (isConnect) {
            this.isConnecting = true;
            mSpeechOffLine.play("蓝牙连接成功");
            ivConnect.setImageResource(R.drawable.bluetooth_connected);// 更换连接成功的背景图
            showBTToast(getResources().getString(R.string.act_btconn) + getResources()
                    .getString(R.string.act_btconngood), R.drawable.icon_bt);// 提示连接成功
        } else {
            mSpeechOffLine.play("蓝牙连接失败");
            btConnFail();
        }
    }

    @Override
    public void onInfCycListener(boolean isInfCyc) {

    }

    @Override
    public void onStopInfCycListener(boolean isStopInfCyc) {

    }

    @Override
    public void onDisconnectListener(boolean isDisconnect) {
        isBTOnOrOff = false;// 蓝牙断开
        if (isDisconnect) {
            isConnecting = false;
            mSpeechOffLine.play("蓝牙已断开");
            ivConnect.setImageResource(R.drawable.bluetooth_disconnect);
            DialogHelper.getInstance(this).showToastShort("蓝牙已断开！");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 这里是为了：当M100蓝牙扫描器被关闭时，要断开与手机的连接,要设置延迟时间，不然程序会卡死
                        mBTFactoryManager.disconnectByM100Off();
                    } catch (IOException e) {
                        DialogHelper.getInstance(BluetoothActivity.this).showDialogError("断开连接出现异常");
                    }
                }
            }, 1500);
        }
    }

    @Override
    public void onBTCloseListener() {

    }

    @Override
    public void onBTOpenListener() {

    }

    @Override
    public void onGetAddressListener(String address) {

    }

    @Override
    public void getEPCData(int MSG_INV_OR_CMDRESP, String epc) {
        tvScanLabel.append(epc);
    }

    @Override
    public void getEPCDataByBroadcast(int MSG_INV_OR_CMDRESP, String epc) {

    }

    /**
     * 接收活动结果，响应startActivityForResult()
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: // 连接结果，由DeviceListActivity设置返回
                if (resultCode == Key.GET_BT_ADDRESS_RESULT_OK)// 响应返回结果
                {
                    DialogHelper.getInstance(BluetoothActivity.this).showProgressDialog("", "正在连接蓝牙设备...", null);
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);// 得到蓝牙设备地址
                    mBTFactoryManager.connectBT(address);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 连接蓝牙设备
     */
    // public void connectBT(String address) {
    //     try {
    //         // 连接成功，由DeviceListActivity设置返回
    //         // MAC地址，由DeviceListActivity设置返回
    //         mBTFactoryManager.getRemoteDevice(address); // 得到蓝牙设备句柄
    //         mBTFactoryManager.createRfcommSocketToServiceRecord();// 得到socket
    //     } catch (IOException e) {
    //         btConnFail();
    //     } catch (NoSuchMethodException e) {
    //         btConnFail();
    //     } catch (IllegalAccessException e) {
    //         btConnFail();
    //     } catch (IllegalArgumentException e) {
    //         btConnFail();
    //     } catch (InvocationTargetException e) {
    //         btConnFail();
    //     }
    //     try {
    //         // 连接成功
    //         String deviceName = mBTFactoryManager.socketConnect();// 连接socket
    //         showBTToast(getResources().getString(R.string.act_btconn) + deviceName + getResources()
    //                 .getString(R.string.act_btconngood), R.drawable.icon_bt);// 提示连接成功
    //     } catch (IOException e) {
    //         try {
    //             mBTFactoryManager.closeSocket();// 关闭socket
    //             btConnFail();// 连接失败
    //         } catch (IOException ee) {
    //             btConnFail();
    //         }
    //         return;
    //     }
    //     try {
    //         mBTFactoryManager.getMessageThread(); // 打开接收线程
    //     } catch (IOException e) {
    //         Toast.makeText(this, getResources().getString(R.string.info_revfail), Toast.LENGTH_SHORT).show();
    //         return;
    //     }
    // }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
