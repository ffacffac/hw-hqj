package szu.wifichat.android.uhf;

import java.io.IOException;

import szu.wifichat.android.conf.WorkEnum;

/**
 * 管理扫描类
 *
 * @author huangqj
 */
public class ScanFactory {
    private InventoryThread m_inventoryThread;// 深坂PDA标签扫描
    //    private RFIDController m_rfidController;// 联欣PDA标签扫描
    //    private ScanThread m_SBSacnScanThread;// 深坂PDA条码扫描
    //    private ScanThreadNew m_LXScanThread;// 联欣PDA条码扫描
    private String scanType;
    // private Context context;

    public ScanFactory() {
        // this.context = context;
        scanType = WorkEnum.PdaType.SB.getValue();
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        }
    }

    /**
     * 实例化条码扫描
     *
     * @throws IOException
     * @throws SecurityException
     */
    //    public void getScanLineInstance(Handler handler) throws SecurityException, IOException {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            m_SBSacnScanThread = new ScanThread(handler);
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            m_LXScanThread = new ScanThreadNew(context, handler);
    //        }
    //    }

    /**
     * 初始化扫描标签
     *
     * @param iscanResultListenner
     */
    public void initScanLabel(IScanResultListenner iscanResultListenner) {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread = new InventoryThread();
            m_inventoryThread.setIScanResultListenner(iscanResultListenner);
            // m_inventoryThread.setOutputPower(26);// 设置功率（不过好像没多大作用）
            // m_inventoryThread.setSensitivity(3);// 设置灵敏度，1，2，3，3是最大的（不过好像没多大作用）
            m_inventoryThread.connect();// 先连接模块
            m_inventoryThread.start();
        }
        //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        //            m_rfidController.setIRadioListener(iradioListener);
        //            m_rfidController.connect();
        //        }
    }

    /**
     * 初始化条码扫描
     */
    //    public void initScanLine() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            m_SBSacnScanThread.start();
    //        }
    //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            m_LXScanThread.newBroadcast();
    //            m_LXScanThread.regReceiver();
    //        }
    //    }

    /**
     * 重新扫描
     */
    public void startInventory() {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread.startInventory();
        }
        //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        //            m_rfidController.startInventory();
        //        }
    }

    /**
     * 暂停扫描
     */
    public void stopInventory() {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread.stopInventory();
        }
        //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        //            m_rfidController.stopInventory();
        //        }
    }

    /**
     * 暂停扫描，可以控制关闭哪一个
     */
    public void stopInventory(String stopTpye) {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue()) && stopTpye.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread.stopInventory();
        }
        //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())
        //                && stopTpye.equals(WorkEnum.PdaType.LX.getValue())) {
        //            m_rfidController.stopInventory();
        //        }
    }

    /**
     * 单次扫描,标签的
     */
    public void scanOneTime(int interval) {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread.scanOneTime();
        }
        //        else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        //            m_rfidController.scanOneTime(interval);
        //        }
    }

    /**
     * 终止线程,扫标签
     */
    public void stopLabelThread() {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            if (m_inventoryThread == null) {
                return;
            }
            m_inventoryThread.stopThread();
            m_inventoryThread = null;
        }
    }

    /**
     * 单次扫描，,条码的
     */
    //    public void scanOneTime() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            m_SBSacnScanThread.scanOneTime();
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            // m_LXScanThread.setScanModel();// 联欣的就扫描一次
    //            m_LXScanThread.start_scan();// 联欣的就扫描一次
    //        }
    //    }

    /**
     * 启动持续扫描(深坂才有)
     */
    //    public void scanContinued() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            m_SBSacnScanThread.scanContinued();
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            // m_LXScanThread.setScanModel();// 联欣的就扫描一次
    //            m_LXScanThread.start_scan();
    //        }
    //    }

    /**
     * 设置一些属性(联欣版才需要设置)
     */
    //    public void setScanModel() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            m_LXScanThread.setScanModel();// 联欣的就扫描一次
    //        }
    //    }

    /**
     * 停止持续扫描(深坂才有)
     */
    //    public void stopContinuedScan() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            if (m_SBSacnScanThread == null) {
    //                return;
    //            }
    //            m_SBSacnScanThread.stopContinuedScan();
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            if (m_LXScanThread == null) {
    //                return;
    //            }
    //            m_LXScanThread.stop_scan();// 关闭扫描
    //        }
    //    }

    /**
     * 关闭条码扫描
     *
     * @param isUnRegisterReceiver 是否注销广播，联欣PDA扫描码时，有时需要不需要注销广播，只需关闭扫描
     */
    //    public void closeLineScan() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //            if (m_SBSacnScanThread != null) {
    //                m_SBSacnScanThread.stopContinuedScan();
    //                m_SBSacnScanThread.stopThread();
    //                m_SBSacnScanThread.close();
    //            }
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            if (m_LXScanThread != null) {
    //                m_LXScanThread.close();
    //                m_LXScanThread.unRegisterReceiver();
    //            }
    //        }
    //    }

    /**
     * 打开联欣PDA电源串口
     */
    //    public void openScanLx() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            if (m_LXScanThread != null) {
    //                m_LXScanThread.open();
    //            }
    //        }
    //    }

    /**
     * 联欣版，只关闭串口，不注销广播
     */
    //    public void closeOnlyLineScanLx() {
    //        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
    //
    //        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
    //            if (m_LXScanThread != null) {
    //                m_LXScanThread.close();
    //            }
    //        }
    //    }

    /**
     * 设置持续扫描延时
     *
     * @param timeSpan
     */
    public void setTimeSpan(int timeSpan) {
        if (scanType.equals(WorkEnum.PdaType.SB.getValue())) {
            m_inventoryThread.setTimeSpan(timeSpan);
        } else if (scanType.equals(WorkEnum.PdaType.LX.getValue())) {
        }
    }
}
