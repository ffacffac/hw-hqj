package szu.wifichat.android.activity.grounder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import szu.wifichat.android.R;
import szu.wifichat.android.adapter.DismantleAdapter;
import szu.wifichat.android.adapter.GroundAffirmAdapter;
import szu.wifichat.android.adapter.GroundLeandAdapter;
import szu.wifichat.android.conf.ConstData;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.dialog.CountDownDialog;
import szu.wifichat.android.dialog.DeviceCheckFragment;
import szu.wifichat.android.entity.GroundMessage;
import szu.wifichat.android.finger.PdaFingerUtil;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.httputils.GroundState;
import szu.wifichat.android.httputils.GrounderRequest;
import szu.wifichat.android.interfaces.IGroundRequest;
import szu.wifichat.android.interfaces.IOnClickListenter;
import szu.wifichat.android.socket.IPMSGConst;
import szu.wifichat.android.socket.OnActiveChatActivityListenner;
import szu.wifichat.android.uhf.IScanResultListenner;
import szu.wifichat.android.util.DateUtils;
import szu.wifichat.android.util.MediaPlayerHelper;
import szu.wifichat.android.util.SessionUtils;
import szu.wifichat.android.view.DialogHelper;
import szu.wifichat.android.view.ListViewForScrollView;

public class GroundActivity2 extends GroundMessageActivity
        implements OnActiveChatActivityListenner, IScanResultListenner, IOnClickListenter, IGroundRequest {

    private static final String TAG = "GroundLeandActivity";

    @BindView(R.id.tv_custom_title)
    TextView tvTitle;
    private String[] mStepName = null;
    @BindView(R.id.lv_ground_step_affirm)
    ListViewForScrollView lvGroundStepAffirm;
    @BindView(R.id.iv_ground_order_state)
    ImageView ivGroundOrderState;
    @BindView(R.id.tv_ground_order_step_name)
    TextView tvGroundOrderStepName;
    @BindView(R.id.tv_ground_order_step_time)
    TextView tvGroundOrderStepTime;
    @BindView(R.id.lv_ground_leand_step)
    ListViewForScrollView lvGroundLeandStep;
    @BindView(R.id.iv_ground_dismantle_step_state)
    ImageView ivGroundDismantleStepState;
    @BindView(R.id.tv_ground_dismantle_step_name)
    TextView tvGroundDismantleStepName;
    @BindView(R.id.tv_ground_dismantle_order_time)
    TextView tvGroundDismantleOrderTime;
    @BindView(R.id.lv_dismantle_ground_step)
    ListViewForScrollView lvDismantleGroundStep;

    @BindView(R.id.tv_ground_order_state)
    TextView tvGroundOrdeState;
    @BindView(R.id.tv_ground_order_communicators)
    TextView getTvGroundOrderCors;//接挂命令下达者
    @BindView(R.id.tv_ground_dismantle_order_state)
    TextView tvDismantleOrderState;
    @BindView(R.id.tv_ground_dismantle_order_communicators)
    TextView tvDilOrderCors;//拆除命令下达者

    @BindView(R.id.iv_custom_title_left)
    ImageView ivConnect;
    @BindView(R.id.iv_custom_title_right)
    ImageView ivScan;

    private GroundAffirmAdapter affirmAdapter;
    private GroundLeandAdapter groundLeandAdapter;
    private DismantleAdapter dismantleAdapter;
    private ArrayList<Device> mDeviceAffirms = new ArrayList<>();
    private ArrayList<Device> mGroundLeands = new ArrayList<>();//接挂地线
    private ArrayList<Device> mDismantles = new ArrayList<>();//拆除地线

    private boolean isAllGroundAffirm = false;//是否所有的杆号都已经扫描完毕

    private CountDownDialog downDialog;

    private int mGroundPosition;//操作的杆号位置
    private int mStepPosition;//操作的步骤
    private boolean isOpenFingerSucceed;//指纹模块是否打开过了
    private boolean isOpenScan = false;//扫描模块是否打开
    private GrounderRequest mRequestThread = null;//轮询后台数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        ButterKnife.bind(this);
        tvTitle.setText("接地卡控管理系统");
        changeActiveChatActivity(this); // 注册到changeActiveChatActivity,用于接收消息
        initViews();
        initEvents();
        initScan();
        //发送IP给服务端
        // Device device = new Device();
        // device.setName("PDA");
        // device.setLabel(SessionUtils.getLocalIPaddress());
        // Log.e("GroundLeaderActivity2", SessionUtils.getLocalIPaddress());
        // sendMessage(device, "地线", GroundMessage.CONTENT_TYPE.IP);
    }

    /**
     * 初始化扫描程序
     */
    private void initScan() {
        PdaFingerUtil.init(new PdaFingerCallback());//初始化指纹
        // mScanFactory = new ScanFactory();
        // mScanFactory.initScanLabel(this);// 初始化标签扫描
        // isOpenScan = true;//已经打开扫描模块
    }

    @Override
    protected void initViews() {
        ivScan.setOnClickListener(this);
        ivConnect.setOnClickListener(this);

        affirmAdapter = new GroundAffirmAdapter(this, mDeviceAffirms);
        lvGroundStepAffirm.setAdapter(affirmAdapter);

        groundLeandAdapter = new GroundLeandAdapter(this, mGroundLeands);
        lvGroundLeandStep.setAdapter(groundLeandAdapter);

        dismantleAdapter = new DismantleAdapter(this, mDismantles);
        lvDismantleGroundStep.setAdapter(dismantleAdapter);
        // showT();
    }

    private void showDeviceCheckDialog(int position, Device device) {
        dismissLeaderDialog();
        DeviceCheckFragment deviceCheck = new DeviceCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Device", (Serializable) device);
        bundle.putInt("Position", position);
        deviceCheck.setArguments(bundle);
        deviceCheck.show(getSupportFragmentManager(), "DeviceCheckFragment");
        deviceCheck.setIOnClickListenter(this);
    }

    private void showT() {
        getTvGroundOrderCors.setVisibility(View.VISIBLE);
        getTvGroundOrderCors.setText("下达人：顾明");
        tvGroundOrdeState.setText("命令状态：已下达");
        tvGroundOrderStepTime.setText("下达时间：2018-01-30 :12:31");
        ivGroundOrderState.setImageResource(R.drawable.icon_finish);
        // //同时提示第三步骤可以进行
        // mGroundLeands.get(0).setState(WorkEnum.DeviceState.WnderWay.getState());
        // groundLeandAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvents() {
        mStepName = new String[]{getResources().getString(R.string.step_scan_card),
                getResources().getString(R.string.step_hang_order), getResources().getString(R.string.step_hang),
                getResources().getString(R.string.step_dismantle_order),
                getResources().getString(R.string.step_dismantle)};
        //确认杆号步骤数据
        Device deviceAffirms = new Device(-1, mStepName[0], 0, "");
        deviceAffirms.setTitle(true);//第一个设置为标题
        mDeviceAffirms.add(0, deviceAffirms);
        for (int i = 0; i < 4; i++) {
            Device device = new Device(i + 1, ConstData.mDeviceName[i], 0, ConstData.mDeviceLabel[i]);
            device.setStepId(ConstData.mStepID[0]);//设置对应的步骤ID
            device.setKM(ConstData.KM[i]);
            device.setGroundPosition(ConstData.mGroundPosition[i]);
            // if (i == 2) {
            //     device.setState(WorkEnum.DeviceState.Error.getState());
            // } else if (i == 3) {
            //     device.setState(WorkEnum.DeviceState.WnderWay.getState());
            // } else {
            //     device.setState(WorkEnum.DeviceState.Finish.getState());
            // }
            mDeviceAffirms.add(device);
        }
        //设置第一个步骤是操作状态
        mDeviceAffirms.get(0).setState(WorkEnum.DeviceState.WnderWay.getState());

        //接挂地线数据
        Device deviceGroundLeand = new Device(-1, mStepName[2], 0, "");
        deviceGroundLeand.setTitle(true);//第一个设置为标题
        mGroundLeands.add(0, deviceGroundLeand);
        for (int i = 0; i < 4; i++) {
            Device device = new Device(i + 1, ConstData.mDeviceName[i], 0, ConstData.mDeviceLabel[i]);
            device.setStepId(ConstData.mStepID[2]);//设置对应的步骤ID
            device.setKM(ConstData.KM[i]);
            device.setGroundPosition(ConstData.mGroundPosition[i]);
            //     if (i == 2) {
            //         device.setState(WorkEnum.DeviceState.Error.getState());
            //     } else if (i == 3) {
            //         device.setState(WorkEnum.DeviceState.WnderWay.getState());
            //     } else {
            //         device.setState(WorkEnum.DeviceState.Finish.getState());
            //     }
            mGroundLeands.add(device);
        }
        // //同时提示第三步骤可以进行
        // mGroundLeands.get(0).setState(WorkEnum.DeviceState.WnderWay.getState());

        //拆除地线数据
        Device deviceDismantle = new Device(-1, mStepName[4], 0, "");
        deviceDismantle.setTitle(true);//第一个设置为标题
        mDismantles.add(0, deviceDismantle);
        for (int i = 0; i < 4; i++) {
            Device device = new Device(i + 1, ConstData.mDeviceName[i], 0, ConstData.mDeviceLabel[i]);
            device.setStepId(ConstData.mStepID[4]);//设置对应的步骤ID
            device.setKM(ConstData.KM[i]);
            device.setGroundPosition(ConstData.mGroundPosition[i]);
            mDismantles.add(device);
        }
        affirmAdapter.notifyDataSetChanged();
        groundLeandAdapter.notifyDataSetChanged();
        dismantleAdapter.notifyDataSetChanged();
        mIMEI = SessionUtils.getIMEI();
    }

    @OnItemClick({R.id.lv_ground_step_affirm, R.id.lv_ground_leand_step, R.id.lv_dismantle_ground_step})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_ground_step_affirm:
                showDeviceCheckDialog(position, mDeviceAffirms.get(position));
                // showWarningDialog(new Device(1, "警告：接地位置有误", 1, ""));
                break;
            case R.id.lv_ground_leand_step:
                if (tvGroundOrdeState.getText().toString().trim().contains("已下达")) {
                    showDeviceCheckDialog(position, mGroundLeands.get(position));
                } else {
                    DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
                }
                break;
            case R.id.lv_dismantle_ground_step:
                if (tvDismantleOrderState.getText().toString().trim().contains("已下达")) {
                    showDeviceCheckDialog(position, mDismantles.get(position));
                } else {
                    DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 接收到消息了
     *
     * @param msg 接收到的消息对象
     */
    @Override
    public void processMessage(Message msg) {
        Log.i("SZU_ChatActivity", "进入processMessage()");
        switch (msg.what) {
            case IPMSGConst.IPMSG_SENDMSG:
                if (mMsg == null) return;
                Device d = mMsg.getDevice();
                if (d == null) return;
                Log.e(TAG, d.toString());
                //扫到错误的杆号或者没有扫到
                if (d.getName() != null && d.getName().startsWith("警告")) {
                    // mSpeechOffLine.play(d.getName());
                    return;
                }
                //第一步
                if (d.getStepId() == ConstData.mStepID[0]) {
                    changeGroundState(d, mDeviceAffirms, affirmAdapter, ivGroundOrderState);
                } else if (d.getStepId() == ConstData.mStepID[1]) {//第二个步骤
                    playYZSVoice(R.string.speech_receive_ground_lend_order);
                    getTvGroundOrderCors.setVisibility(View.VISIBLE);
                    getTvGroundOrderCors.setText("下达人：顾明");
                    tvGroundOrdeState.setText("命令状态：已下达");
                    tvGroundOrderStepTime.setText("下达时间：" + mMsg.getSendTime());
                    ivGroundOrderState.setImageResource(R.drawable.icon_finish);
                    //同时提示第三步骤可以进行
                    mGroundLeands.get(0).setState(WorkEnum.DeviceState.WnderWay.getState());
                    groundLeandAdapter.notifyDataSetChanged();
                } else if (d.getStepId() == ConstData.mStepID[2]) {//第三步骤
                    changeGroundState(d, mGroundLeands, groundLeandAdapter, ivGroundDismantleStepState);
                } else if (d.getStepId() == ConstData.mStepID[3]) {//第四步骤
                    playYZSVoice(R.string.speech_receive_dismantle_order);
                    tvDilOrderCors.setVisibility(View.VISIBLE);
                    tvDilOrderCors.setText("下达人：顾明");
                    tvDismantleOrderState.setText("命令状态：已下达");
                    tvGroundDismantleOrderTime.setText("下达时间：" + mMsg.getSendTime());
                    ivGroundDismantleStepState.setImageResource(R.drawable.icon_finish);
                    //同时提示第四步骤可以进行，设置标题状态为可执行
                    mDismantles.get(0).setState(WorkEnum.DeviceState.WnderWay.getState());
                    dismantleAdapter.notifyDataSetChanged();
                } else if (d.getStepId() == ConstData.mStepID[4]) {//第五步骤
                    changeGroundState(d, mDismantles, dismantleAdapter, null);
                }
                break;
        }
    }

    /**
     * 收到消息后，改变杆号状态
     *
     * @param d
     * @param devices
     * @param adapter
     * @param ivNextState
     */
    private void changeGroundState(Device d, ArrayList<Device> devices, BaseAdapter adapter, ImageView ivNextState) {
        int stepDeviceSize = devices.size();
        for (int i = 0; i < stepDeviceSize; i++) {
            Device d1 = devices.get(i);
            if (d1 == null) continue;
            if (d1.getId() == d.getId()) {
                if (!d.isRefuseByLeader()) {
                    if (d.getState() != WorkEnum.DeviceState.Finish.getState()) {
                        showDeviceCheckDialog(i, d1);
                    }
                    if (d.getState() == WorkEnum.DeviceState.WnderWay.getState()) {
                        playDeviceAlreardAffirm(d1.getName());//杆号位置已确认
                        DialogHelper.getInstance(GroundActivity2.this).showToastShort("杆号：" + d.getName() + "已定位");
                    } else if (d.getState() == WorkEnum.DeviceState.Finish.getState()) {
                        playDeviceAffirm(d1.getName());//播放杆号审核通过
                        DialogHelper.getInstance(GroundActivity2.this).showToastShort("杆号：" + d.getName() + "审核通过");
                    }
                    //没有被拒绝的话，设置设备已经确认完毕
                    // devices.get(i).setState(WorkEnum.DeviceState.Finish.getState());
                    devices.get(i).setState(d.getState());
                    //如果第一步骤已经完成，则屏蔽掉发送按钮
                    if (isFinishStep(devices)) {
                        devices.get(0).setCanSendAffirm(false);
                        devices.get(0).setState(WorkEnum.DeviceState.Finish.getState());
                        if (ivNextState != null) ivNextState.setImageResource(R.drawable.icon_step2_select);
                    }
                } else {
                    playDeviceRefuseed(d1.getName());//播放杆号被拒绝语音
                    //如果被拒绝的话，设置设备设置为拒绝状态
                    devices.get(i).setState(WorkEnum.DeviceState.Error.getState());
                    devices.get(i).setRefuse(d.getRefuse());
                }
                break;
            }
        }
        adapter.notifyDataSetChanged();
        if (isFinishStep(devices)) {
            stopRequest();//所有杆号如果都已经确认，那么就停止轮询
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_ground:
                break;
            case R.id.btn_revice:
                break;
            case R.id.iv_custom_title_right:
                break;
            case R.id.iv_custom_title_left:
                break;
        }
    }

    /**
     * 发送消息
     *
     * @param content 内容
     * @param type    消息类型
     */
    public void sendMessage(Device device, String content, GroundMessage.CONTENT_TYPE type) {
        String nowtime = DateUtils.getNowtime();
        GroundMessage msg = new GroundMessage(mIMEI, nowtime, content, type);
        msg.setDevice(device);//设备信息
        switch (type) {
            case TEXT:
                //                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG,
                //                        getIpaddress(), msg);
                //                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG,
                //                        "192.168.43.63", msg);
                //有指纹的平板IP
                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, "192.168.43.27", msg);
                break;
            case IP:
                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, "192.168.43.1", msg);//发给IP给服务端
                break;
        }
    }

    /**
     * @param paramMsg 收到的消息对象
     * @return
     */
    @Override
    public boolean isThisActivityMsg(GroundMessage paramMsg) {
        mMsg = paramMsg;
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //131是指纹识别手持机F1键
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == 134 || keyCode == 131) {
            // if (isAllGroundAffirm) {
            //     DialogHelper.getInstance(this).showToastShort("杆号位置已确认完毕，无需再扫描");
            //     return true;
            // }
            controllerScan();
        }
        return super.onKeyDown(keyCode, event);
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
        // playYZSVoice(R.string.speech_start_discern); //开始识别
        // Log.e(TAG, "startScan==isOpenScan=" + isOpenScan);
        // if (!isOpenScan) {
        //     mScanFactory.initScanLabel(this);// 初始化标签扫描
        // }
        // // 先让语音播放完再启动
        // new Handler().postDelayed(new Runnable() {
        //     @Override
        //     public void run() {
        //         mScanFactory.startInventory();
        //     }
        // }, 1000);
        // isRFID = true;
        playYZSVoice(R.string.speech_start_discern); //开始识别
        Device device = new Device();
        device.setName("startScan");
        isRFID = true;//开始扫描
        sendMessage(device, "startScan", GroundMessage.CONTENT_TYPE.IP);

    }

    private void stopScan(boolean isStopPlay) {
        // mScanFactory.stopInventory();
        // if (!isStopPlay) playYZSVoice(R.string.speech_stop_discern);  //停止识别
        // isRFID = false;

        //发送指令给扫描设备，开始扫描识别杆号
        if (!isStopPlay) playYZSVoice(R.string.speech_stop_discern);  //停止识别
        Device device = new Device();
        device.setName("stopScan");
        isRFID = false;//停止扫描
        sendMessage(device, "stopScan", GroundMessage.CONTENT_TYPE.IP);
    }

    @Override
    public void onEPCIn(String epc) {
        Log.e(TAG, epc);
        for (int i = 0; i < ConstData.mDeviceLabel.length; i++) {
            if (epc.equals(ConstData.mDeviceLabel[i])) {
                //因为第一个步骤才有扫描，所以传步骤一进去
                int itemPosition = getDevicePositionByScan(mDeviceAffirms, epc);
                //设备的状态为已经确认过的，则扫描到的不用再处理了
                if (itemPosition < 0 || mDeviceAffirms.get(itemPosition).getState() == WorkEnum.DeviceState.Finish
                        .getState()) {
                    return;
                }
                stopScan(true);//扫到一个就停止扫描
                if (mScanFactory != null) mScanFactory.stopLabelThread();
                isOpenScan = false;
                sendMsg(epc, 1, itemPosition);
                return;
            }
        }
    }

    private void sendMsg(String epc, int whats, int position) {
        Message msg = new Message();
        msg.what = whats;
        msg.obj = epc;
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //找到位置
                    int itemPosition = msg.arg1;
                    if (itemPosition >= 0) {
                        //播放杆号已定位
                        playDeviceAlreardAffirm(mDeviceAffirms.get(itemPosition).getName());
                        if (mDeviceAffirms.get(itemPosition).getState() == WorkEnum.DeviceState.Normal.getState()) {
                            //将扫描到的杆号设置成进行中
                            mDeviceAffirms.get(itemPosition).setState(WorkEnum.DeviceState.WnderWay.getState());
                            affirmAdapter.notifyDataSetChanged();
                        }
                        showDeviceCheckDialog(itemPosition, mDeviceAffirms.get(itemPosition));
                    }
                    break;
            }
        }
    };

    /**
     * 扫描到标签后获取设备的位置
     *
     * @return
     */
    private int getDevicePositionByScan(ArrayList<Device> devices, String label) {
        int deviceSize = devices.size();
        for (int i = 0; i < deviceSize; i++) {
            if (!devices.get(i).isTitle() && devices.get(i).getLabel() != null && devices.get(i).getLabel()
                                                                                         .equals(label)) {
                return i;//得到标签对应设备的位置
            }
        }
        return -1;
    }

    /**
     * 判断步骤是否完成
     *
     * @return
     */
    private boolean isFinishStep(ArrayList<Device> stepDevices) {
        int deviceSize = stepDevices.size();
        for (int i = 0; i < deviceSize; i++) {
            //不是标题
            if (!stepDevices.get(i).isTitle() && stepDevices.get(i).getState() != WorkEnum.DeviceState.Finish
                    .getState()) {
                return false;//步骤未完成
            }
        }
        return true;
    }

    /**
     * 确认发送
     */
    @Override
    public void onMyClick(int btnItem, int position, Device device) {
        switch (btnItem) {
            case 1://发送
                //第一步，确认接挂杆号
                if (device.getStepId() == ConstData.mStepID[0]) {
                    sendGroundMsg(position, device, mDeviceAffirms, affirmAdapter, ConstData.mStepID[0]);
                    // mGroundPosition = position;//缓存杆号的位置
                    // mStepPosition = ConstData.mStepID[0];
                    // mDeviceAffirms.get(position).setImgPath(device.getImgPath());
                    // Log.e(TAG, "地线端杆号：" + device.getName() + "图片路径：" + device.getImgPath());
                    // //发送消息后要把状态改成进行中
                    // if (mDeviceAffirms.get(position).getState() != WorkEnum.DeviceState.WnderWay.getState()
                    //         || mDeviceAffirms.get(position).getState() != WorkEnum.DeviceState.Finish.getState()) {
                    //     mDeviceAffirms.get(position).setState(WorkEnum.DeviceState.WnderWay.getState());
                    //     affirmAdapter.notifyDataSetChanged();//刷新第一个步骤
                    // }
                    // sendGroundAffirm();//确认指纹，发送消息
                    // //提示可以发送确认好的杆号给领导端
                    // if (isOperationFinish(mDeviceAffirms)) {
                    //     isAllGroundAffirm = true;//所有的杆号已经确认完毕，关闭掉扫描标签模块
                    //     //所有杆号已经定位好，可以开始轮询
                    //     startRequest();
                    // }
                } else if (device.getStepId() == ConstData.mStepID[2]) {
                    sendGroundMsg(position, device, mGroundLeands, groundLeandAdapter, ConstData.mStepID[2]);
                    // mGroundPosition = position;//缓存杆号的位置
                    // mStepPosition = ConstData.mStepID[2];
                    // mGroundLeands.get(position).setImgPath(device.getImgPath());
                    // //发送消息后要把状态改成进行中
                    // if (mGroundLeands.get(position).getState() != WorkEnum.DeviceState.WnderWay.getState()
                    //         || mGroundLeands.get(position).getState() != WorkEnum.DeviceState.Finish.getState()) {
                    //     mGroundLeands.get(position).setState(WorkEnum.DeviceState.WnderWay.getState());
                    //     groundLeandAdapter.notifyDataSetChanged();
                    // }
                    // sendGroundAffirm();//确认指纹，发送消息
                    // if (isOperationFinish(mGroundLeands)) {
                    //     //所有杆号已经定位好，可以开始轮询
                    //     startRequest();
                    // }
                } else if (device.getStepId() == ConstData.mStepID[4]) {
                    sendGroundMsg(position, device, mDismantles, dismantleAdapter, ConstData.mStepID[4]);
                    // mGroundPosition = position;//缓存杆号的位置
                    // mStepPosition = ConstData.mStepID[4];
                    // mDismantles.get(position).setImgPath(device.getImgPath());
                    // //发送消息后要把状态改成进行中
                    // if (mDismantles.get(position).getState() != WorkEnum.DeviceState.WnderWay.getState()
                    //         || mDismantles.get(position).getState() != WorkEnum.DeviceState.Finish.getState()) {
                    //     mDismantles.get(position).setState(WorkEnum.DeviceState.WnderWay.getState());
                    //     dismantleAdapter.notifyDataSetChanged();
                    // }
                    // sendGroundAffirm();//确认指纹，发送消息
                    // if (isOperationFinish(mDismantles)) {
                    //     //所有杆号已经定位好，可以开始轮询
                    //     startRequest();
                    // }
                }
                break;
            case 2://拒绝/取消
                break;
        }
    }

    /**
     * 发送杆号信息
     */
    private void sendGroundMsg(int position, Device device, ArrayList<Device> devices, BaseAdapter adapter,
                               int stepPosition) {
        mGroundPosition = position;//缓存杆号的位置
        mStepPosition = stepPosition;
        devices.get(position).setImgPath(device.getImgPath());
        Log.e(TAG, "地线端杆号：" + device.getName() + "图片路径：" + device.getImgPath());
        //发送消息后要把状态改成进行中
        if (devices.get(position).getState() != WorkEnum.DeviceState.WnderWay.getState()
                || devices.get(position).getState() != WorkEnum.DeviceState.Finish.getState()) {
            devices.get(position).setState(WorkEnum.DeviceState.WnderWay.getState());
            adapter.notifyDataSetChanged();//刷新第一个步骤
        }
        sendGroundAffirm();//确认指纹，发送消息
        //提示可以发送确认好的杆号给领导端
        if (isOperationFinish(devices)) {
            if (device.getStepId() == ConstData.mStepID[0]) {
                isAllGroundAffirm = true;//所有的杆号已经确认完毕，关闭掉扫描标签模块
            }
            startRequest(); //所有杆号已经定位好，可以开始轮询
        }
    }

    @Override
    public void onDismiss(int btnItem, int position, Device device) {

    }

    /**
     * 弹出提示可以发送杆号确认提示框
     */
    private void sendGroundAffirm() {
        //        DialogHelper.getInstance(this).showDialogAsk("所有杆号已确认，可通过指纹确认发送杆号信息？", new
        // IDialogAction() {
        //            @Override
        //            public void action() {
        //                sendStepAffirm();//发送数据
        //            }
        //            @Override
        //            public void action(int position) {
        //            }
        //        }, null);
        playYZSVoice(R.string.speech_affirm_finger);
        showTimeCount(10);//显示倒计时dialog
        // DialogHelper.getInstance(GroundActivity1.this).showDialogNoButton("请确认指纹", new IDialogAction1() {
        //     @Override
        //     public void action(int position) {
        //         getFingerSuccessAndSend();//模拟指纹确认成功并且发送信息
        //     }
        // });


        //停止掉扫描模块，因为跟指纹模块冲突了
        if (isOpenScan) {
            if (mScanFactory != null) mScanFactory.stopLabelThread();
            isOpenScan = false;
        }
        if (!isOpenFingerSucceed) {
            boolean openResult = PdaFingerUtil.openDevice();//打开设备
            if (openResult) {
                isOpenFingerSucceed = true;//指纹打开成功，下次不用再次打开
                PdaFingerUtil.startScan(7000);//在指定的时间内确认指纹
            }
        } else {
            PdaFingerUtil.startScan(7000);//在指定的时间内确认指纹
        }
    }

    /**
     * 是否可以执行第二步骤
     *
     * @return
     */

    private boolean isOperationFinish(ArrayList<Device> devices) {
        for (Device device : devices) {
            if (device.isTitle()) continue;
            if (device.getState() == WorkEnum.DeviceState.Normal.getState()) {
                return false;
            }
        }
        return true;
    }

    private void showTimeCount(int time) {
        downDialog = new CountDownDialog(this, 0, time);
        downDialog.show();
    }

    private void dismissTimeCount() {
        if (downDialog == null || !downDialog.isShowing()) return;
        downDialog.dismiss();
        downDialog = null;
    }

    /**
     * 轮询结果
     *
     * @param groundState
     */
    @Override
    public void response(GroundState groundState) {
        if (groundState == null) return;
        if (mStepPosition == ConstData.mStepID[0]) {
            //杆号确认下达
            if (groundState.getConfirm() == 1) {
                stopRequest();//所有杆号审核通过，停止轮询
                changeDeviceState(mDeviceAffirms, affirmAdapter);
            }
        } else if (mStepPosition == ConstData.mStepID[2]) {
            //接挂结果审核通过
            if (groundState.getAddConfirm() == 1) {
                stopRequest();//所有杆号审核通过，停止轮询
                changeDeviceState(mGroundLeands, groundLeandAdapter);
            }
        } else if (mStepPosition == ConstData.mStepID[4]) {
            //拆除结果审核通过
            if (groundState.getDelConfirm() == 1) {
                stopRequest();//所有杆号审核通过，停止轮询
                changeDeviceState(mDismantles, dismantleAdapter);
            }
        }
    }

    /**
     * 后台审核通过，改变所有杆号的状态为“已审核”
     *
     * @param devices
     * @param adapter
     */
    private void changeDeviceState(ArrayList<Device> devices, BaseAdapter adapter) {
        // stopRequest();//所有杆号审核通过，停止轮询
        MediaPlayerHelper.playAssetFile("raw/crystalring.mp3");
        if (devices.get(0).getStepId() == ConstData.mStepID[0]) {
            playYZSVoice("所有杆号审核通过");
            Log.e(TAG, "后台确认：所有杆号已确认");
        } else if (devices.get(0).getStepId() == ConstData.mStepID[2]) {
            playYZSVoice("所有杆号审核通过");
            Log.e(TAG, "后台确认：所有杆号接挂审核通过");
        }
        if (devices.get(0).getStepId() == ConstData.mStepID[4]) {
            playYZSVoice("所有杆号审核通过");
            Log.e(TAG, "后台确认：所有杆号拆除审核通过");
        }
        for (Device device : devices) {
            if (!device.isTitle()) {
                device.setState(WorkEnum.DeviceState.Finish.getState());//将杆号状态改变
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 开始轮询
     */
    private void startRequest() {
        Log.e(TAG, "开始轮询。。。");
        mRequestThread = new GrounderRequest(this);
        mRequestThread.start();
    }

    /**
     * 停止轮询
     */
    private void stopRequest() {
        Log.e(TAG, "停止轮询。。。");
        if (mRequestThread != null) {
            mRequestThread.stopRequest();
            mRequestThread = null;
        }
    }

    private class PdaFingerCallback implements PdaFingerUtil.Callback {
        @Override
        public void onGetMessage(String message) {
        }

        @Override
        public void timeOut() {
        }

        @Override
        public void onGetFinger(Object fingerData) {
            getFingerSuccessAndSend();//获取指纹成功并且发送信息
        }

        @Override
        public void onScanning(int remainTime) {
        }
    }

    /**
     * 获取指纹成功并且发送信息
     */
    private void getFingerSuccessAndSend() {
        playYZSVoice(R.string.speech_finger_right);
        DialogHelper.getInstance(GroundActivity2.this).cancelFingerDialog();
        DialogHelper.getInstance(GroundActivity2.this).showToastShort("指纹确认成功");
        Log.e(TAG, "指纹仪获取到");
        dismissTimeCount();//取消显示Dialog
        if (mStepPosition == ConstData.mStepID[0]) {
            PdaFingerUtil.closeDevice();//关闭指纹识别
            isOpenFingerSucceed = false;
            Log.e(TAG, "步骤一指纹识别关闭。。。");
            sendMessage(mDeviceAffirms.get(mGroundPosition), "地线", GroundMessage.CONTENT_TYPE.TEXT);
            groundUpload(ConstData.mStepID[0] + 1, mDeviceAffirms.get(mGroundPosition).getName(), 1);//发送数据给后台
        } else if (mStepPosition == ConstData.mStepID[2]) {
            sendMessage(mGroundLeands.get(mGroundPosition), "地线", GroundMessage.CONTENT_TYPE.TEXT);
            groundUpload(ConstData.mStepID[2] + 2, mGroundLeands.get(mGroundPosition).getName(), 1);//发送数据给后台
        } else if (mStepPosition == ConstData.mStepID[4]) {
            sendMessage(mDismantles.get(mGroundPosition), "地线", GroundMessage.CONTENT_TYPE.TEXT);
            groundUpload(ConstData.mStepID[4] + 3, mDismantles.get(mGroundPosition).getName(), 1);//发送数据给后台
        }
    }

    @Override
    public void finish() {
        if (isOpenFingerSucceed) {
            PdaFingerUtil.closeDevice();//关闭指纹
        }
        stopRequest();//停止轮询
        mUDPSocketThread.stopUDPSocketThread();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        if (mScanFactory != null) mScanFactory.stopLabelThread();
        super.onDestroy();
    }
}
