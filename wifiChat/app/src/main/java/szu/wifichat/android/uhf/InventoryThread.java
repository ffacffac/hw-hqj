package szu.wifichat.android.uhf;

import android.util.Log;

import java.util.List;

/**
 * 超高频读卡线程
 *
 * @author macc 2015-10-23
 */
public class InventoryThread extends Thread {
    private List<byte[]> epcList;// 扫描结果字节数组
    private boolean isReading;// 暂停标志
    private boolean isScanOneTime; // 是否扫描一次
    private boolean isRunning = true;// 线程终止控制
    private UhfReader reader = UhfReader.getInstance();
    private IScanResultListenner iScanResultListenner;
    private int timeSpan = 400;// 持续扫描间隔

    public InventoryThread() {
        this.timeSpan = 400;
    }

    @Override
    public void run() {
        super.run();
        while (isRunning) {
            while ((isReading && !isInterrupted()) || isScanOneTime) {
                epcList = reader.inventoryRealTime(); // 实时盘存
                Log.e("reading------epc", epcList.toString());
                if (epcList != null && !epcList.isEmpty()) {
                    Log.e("reading------epc", epcList.toString());
                    for (byte[] epc : epcList) {
                        String epcStr = Tools.Bytes2HexString(epc, epc.length);
                        iScanResultListenner.onEPCIn(epcStr);
                        if (isScanOneTime) {
                            isReading = false;
                            isScanOneTime = false;
                        }
                    }
                }
                epcList = null;
                try {
                    Log.e("timeSpan------", String.valueOf(timeSpan));
                    Thread.sleep(timeSpan);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(timeSpan);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置扫描结果监听器
     *
     * @param iScanResultListenner
     */
    public void setIScanResultListenner(IScanResultListenner iScanResultListenner) {
        this.iScanResultListenner = iScanResultListenner;
    }

    /**
     * 设置持续扫描延时
     *
     * @param timeSpan
     */
    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    /**
     * 重新扫描
     */
    public void startInventory() {
        isReading = true;
    }

    /**
     * 暂停扫描
     */
    public void stopInventory() {
        isReading = false;
    }

    /**
     * 单次扫描
     */
    public void scanOneTime() {
        isScanOneTime = true;
    }

    /**
     * 终止线程
     */
    public void stopThread() {
        isScanOneTime = false;
        isReading = false;
        isRunning = false;
        iScanResultListenner = null;
        close();
    }

    /**
     * 终止线程
     */
    public void stopRunningThread() {
        isScanOneTime = false;
        isReading = false;
        isRunning = false;
    }

    /**
     * 连接模块
     *
     * @return
     */
    public boolean connect() {
        byte[] versionBytes = reader.getFirmware();
        if (versionBytes != null) {
            reader.setWorkArea(3);// 设置成欧标
            return true;
        }
        return false;
    }

    public boolean setOutputPower(int value) {
        return reader.setOutputPower(value);
    }

    public void setSensitivity(int value) {
        reader.setSensitivity(value);
    }

    /**
     * 关闭串口
     */
    private void close() {
        if (reader != null) {
            reader.close();
        }
    }
}
