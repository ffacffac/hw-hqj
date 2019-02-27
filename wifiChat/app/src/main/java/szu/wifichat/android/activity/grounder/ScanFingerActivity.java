package szu.wifichat.android.activity.grounder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import szu.wifichat.android.R;
import szu.wifichat.android.finger.FingerprintUtil;
import szu.wifichat.android.uhf.IScanResultListenner;
import szu.wifichat.android.uhf.ScanFactory;
import szu.wifichat.android.view.DialogHelper;

public class ScanFingerActivity extends AppCompatActivity implements IScanResultListenner {

    public ScanFactory mScanFactory = null;// 扫描工厂类
    public boolean isRFID;// 是否超高频标签
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_finger)
    Button btnFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_finger);
        ButterKnife.bind(this);
        initScan();
    }

    /**
     * 初始化扫描程序
     */
    private void initScan() {
        FingerprintUtil.init(this, new MyFPCallback(), true);//已经来就自动打开指纹识别设备
        mScanFactory = new ScanFactory();
        mScanFactory.initScanLabel(this);// 初始化标签扫描
    }

    @OnClick({R.id.btn_scan, R.id.btn_finger})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                controllerScan();
                break;
            case R.id.btn_finger:
                FingerprintUtil.getFeature(8000);//获取指纹特征
                break;
        }
    }

    /**
     * 控制扫描
     */
    private void controllerScan() {
        if (!isRFID) {
            startScan();
        } else {
            stopScan(false);
        }
    }

    private void startScan() {
        mScanFactory.startInventory();
        isRFID = true;
        btnScan.setText("停止扫描");
    }

    private void stopScan(boolean isStopPlay) {
        mScanFactory.stopInventory();
        isRFID = false;
        btnScan.setText("开始扫描");
    }

    @Override
    public void onEPCIn(final String epc) {
        Log.e("扫描标签：", epc);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanFingerActivity.this, "扫描成功：" + epc, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyFPCallback implements FingerprintUtil.Callback {
        @Override
        public void onConnectDevice(boolean isConnectSuccess, int errorCode) {
            if (isConnectSuccess) {
                DialogHelper.getInstance(ScanFingerActivity.this).showToastShort("设备已开启");
            } else {
                DialogHelper.getInstance(ScanFingerActivity.this).showToastShort("开启设备失败");
            }
        }

        @Override
        public boolean onUsbPermissionDenied() {
            return true;
        }

        @Override
        public void onDeviceNotFound() {
        }

        @Override
        public void onScanning(int remainTime) {
        }

        @Override
        public void onGetFeature(String featureCode) {
            DialogHelper.getInstance(ScanFingerActivity.this).showToastShort("指纹获取成功");
        }

        @Override
        public void onGetFeatureBad() {
        }

        @Override
        public void timeOut() {
        }

        @Override
        public void onGetImageError() {
        }

        @Override
        public void onGetDeviceError() {
        }
    }

    @Override
    public void finish() {
        mScanFactory.stopLabelThread();
        FingerprintUtil.closeDevice();
        super.finish();
    }
}
