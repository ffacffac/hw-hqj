package szu.wifichat.android.bluetooth;

public interface IBTClientListener {
    /**
     * 搜索蓝牙监听，是否可搜索
     */
    public void onSearchListener(boolean isSearch);

    /**
     * 蓝牙连接是否成功
     */
    public void onConnectListener(boolean isConnect);

    /**
     * 盘点
     */
    public void onInfCycListener(boolean isInfCyc);

    /**
     * 停止盘点
     */
    public void onStopInfCycListener(boolean isStopInfCyc);

    /**
     * 蓝牙断开
     *
     * @param isDisconnect
     */
    public void onDisconnectListener(boolean isDisconnect);

    /**
     * 手机蓝牙关闭
     */
    public void onBTCloseListener();

    /**
     * 手机蓝牙打开
     */
    public void onBTOpenListener();

    /**
     * 获取蓝牙设备地址
     */
    public void onGetAddressListener(String address);

    /**
     * 获取标签信息
     *
     * @param MSG_INV_OR_CMDRESP 是MSG_M100DATARESP_INV或MSG_M100CMDRESP
     * @param epc                标签数据
     */
    public void getEPCData(int MSG_INV_OR_CMDRESP, String epc);

    /**
     * 通过广播获取标签信息
     *
     * @param MSG_INV_OR_CMDRESP 是MSG_M100DATARESP_INV或MSG_M100CMDRESP
     * @param epc                标签数据
     */
    public void getEPCDataByBroadcast(int MSG_INV_OR_CMDRESP, String epc);

}
