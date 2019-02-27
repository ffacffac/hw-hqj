package szu.wifichat.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import szu.wifichat.android.BaseActivity;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.Key;
import szu.wifichat.android.view.DialogHelper;

/**
 * Created by huangqj on 2017-07-05.
 */

public abstract class BTBaseActivity extends BaseActivity {

    public final static int REQUEST_CONNECT_DEVICE = 3; // 宏定义查询设备句柄
    public BTFactoryManager mBTFactoryManager;
    public BTReceiver mBTReceiver;
    public boolean isBTOnOrOff = false;
    public boolean isConnecting = false;//是否正在连接
    public boolean isScanning;//是否在扫描

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBTReceiver();
        /**
         * 注册广播
         */
    }

    public void registerBTReceiver() {
        mBTReceiver = new BTReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);// 蓝牙开关的状态监听
        filter.addAction(Key.BT_GET_ADDRESS);// 获取蓝牙地址
        filter.addAction(Key.BT_SEND_EPC_DATA_BY_BROADCAS);// 通过广播来传标签（EPC）数据
        this.registerReceiver(mBTReceiver, filter);
    }

    /**
     * 开始扫描
     */
    public void startInv() throws IOException {
        if (!isConnecting) {
            mSpeechOffLine.play("蓝牙未连接");
            showBTToast(getResources().getString(R.string.info_btconn), R.drawable.icon_r);//
            mBTFactoryManager.onConnect(); // 请先连接蓝牙设备
            return;
        }
        mSpeechOffLine.play("开始识别");
        // btnScan.setText("停止扫描");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mBTFactoryManager.onInfCyc();// 先让语音播放完再启动
                } catch (IOException e) {
                    DialogHelper.getInstance(BTBaseActivity.this).showDialogError("扫描过程出现异常！");
                }
            }
        }, 1000);
        isScanning = true;
    }

    /**
     * 停止扫描
     *
     * @param isOnListener 是否需要监听
     */
    public void stopInv(boolean isOnListener) throws IOException {
        if (!isOnListener) {
            mBTFactoryManager.onStopInfCycNoListener();
        } else {
            mBTFactoryManager.onStopInfCyc();// 停止盘点
            mSpeechOffLine.play("停止识别");
        }
        isScanning = false;
    }

    /**
     * Toast提示蓝牙的状况
     *
     * @param msg          要显示的信息
     * @param iconResource 要显示的图标
     */
    public void showBTToast(String msg, int iconResource) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(getApplicationContext());
        imageCodeProject.setImageResource(iconResource);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }


    /**
     * Toast提示连接蓝牙失败
     */
    public void btConnFail() {
        showBTToast(getResources().getString(R.string.act_btconn) + getResources()
                .getString(R.string.act_btconnfail), R.drawable.icon_r);
    }

    @Override
    protected void onDestroy() {
        try {// 断开与M100蓝牙扫描器的连接，注意：有时候发现蓝牙未关闭就退出Activity,
            // 进来再次与M100连接上了，但是扫描不到标签，这是因为退出Activity是没有断开与M100的连接
            if (mBTFactoryManager != null) mBTFactoryManager.disconnectM100NoListener();
        } catch (IOException e) {
            DialogHelper.getInstance(this).showDialogError("断开蓝牙出现异常！");
        }
        if (mBTReceiver != null) {
            this.unregisterReceiver(mBTReceiver);// 注销广播
        }
        super.onDestroy();
    }
}
