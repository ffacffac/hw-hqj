package szu.wifichat.android.activity.grounder;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import szu.wifichat.android.R;
import szu.wifichat.android.adapter.DeviceAdapter;
import szu.wifichat.android.adapter.LeaderStepAdapter;
import szu.wifichat.android.conf.ConstData;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.dialog.CountDownDialog;
import szu.wifichat.android.dialog.LeaderAffirmFragment;
import szu.wifichat.android.entity.GroundMessage;
import szu.wifichat.android.entity.NearByPeople;
import szu.wifichat.android.finger.FingerprintUtil;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.groundbeen.Step;
import szu.wifichat.android.httputils.GroundState;
import szu.wifichat.android.httputils.GrounderRequest;
import szu.wifichat.android.interfaces.IGroundRequest;
import szu.wifichat.android.interfaces.IOnClickListenter;
import szu.wifichat.android.interfaces.IOnItemClickListener;
import szu.wifichat.android.socket.IPMSGConst;
import szu.wifichat.android.socket.OnActiveChatActivityListenner;
import szu.wifichat.android.util.DateUtils;
import szu.wifichat.android.util.FileUtils;
import szu.wifichat.android.util.MediaPlayerHelper;
import szu.wifichat.android.util.SessionUtils;
import szu.wifichat.android.view.DialogHelper;

public class GroundLeaderActivity1 extends GroundMessageActivity
        implements OnActiveChatActivityListenner, IOnClickListenter, AdapterView.OnItemClickListener, IGroundRequest {

    private static final String TAG = "GroundLeandActivity";
    @BindView(R.id.tv_custom_title)
    TextView tvTitle;
    @BindView(R.id.layout_title)
    FrameLayout layoutTitle;
    @BindView(R.id.lv_leader_step)
    ListView lvLeaderStep;
    @BindView(R.id.rv_ground_device_affirm)
    RecyclerView rlvDeviceNumber;
    @BindView(R.id.tv_device_group_title)
    TextView tvDeviceTitle;
    @BindView(R.id.ll_set_order_leader)
    LinearLayout llSetOrder;
    @BindView(R.id.ll_leader_right)
    LinearLayout llDevice;
    @BindView(R.id.tv_hint_already_set_order)
    TextView tvAlreadySetOrder;
    private LeaderStepAdapter leaderStepAdapter;

    private ArrayList<Step> mSteps = new ArrayList<>();//缓存显示当前页面设备数据
    private ArrayList<Device> mDevices = new ArrayList<>();
    private ArrayList<Device> mDevices0 = new ArrayList<>();
    private ArrayList<Device> mDevices2 = new ArrayList<>();
    private ArrayList<Device> mDevices4 = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private String[] mStepName = null;
    private int[] mStepID = {0, 1, 2, 3, 4};//步骤ID标识
    private int mPosition = 0;//当前正在执行的步骤
    private CountDownDialog downDialog;
    private boolean isVideoPlayStep1 = false;

    private int mGroundReceiveCount = 0;//已经接收到的杆号的个数
    private ArrayList<LeaderAffirmFragment> mDialogs = new ArrayList<>();
    private GrounderRequest mRequestThread = null;//轮询后台数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_leader1);
        ButterKnife.bind(this);
        tvTitle.setText("接地卡控管理系统");
        Bundle bundle = getIntent().getExtras();
        isClient = bundle.getBoolean("isClient", true);
        changeActiveChatActivity(this); // 注册到changeActiveChatActivity,用于接收消息
        initViews();
        initEvents();
        initFinger();
    }

    private void initFinger() {
        FingerprintUtil.init(this, new MyFPCallback(), true);//已经来就自动打开指纹识别设备
    }

    /**
     * 选中哪个步骤
     *
     * @param selectPosition
     */
    private void selectListViewItem(int selectPosition) {
        lvLeaderStep.performItemClick(lvLeaderStep.getChildAt(selectPosition), selectPosition, lvLeaderStep
                .getItemIdAtPosition(selectPosition));
    }

    @Override
    protected void initViews() {
        leaderStepAdapter = new LeaderStepAdapter(this, mSteps);
        lvLeaderStep.setAdapter(leaderStepAdapter);
        lvLeaderStep.setOnItemClickListener(this);
        tvDeviceTitle.setText(ConstData.GROUPS);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rlvDeviceNumber.setLayoutManager(gridLayoutManager);
        deviceAdapter = new DeviceAdapter(this, mDevices);
        rlvDeviceNumber.setAdapter(deviceAdapter);
        deviceAdapter.setIOnItemClickListener(new IOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                int deviceState = mDevices.get(position).getState();
                if (deviceState == WorkEnum.DeviceState.Normal.getState()) {
                    DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("当前杆号不可操作");
                    return;
                }
                showDeviceCheckDialog(position, mDevices.get(position));
            }
        });
    }

    private void showDeviceCheckDialog(int position, Device device) {
        LeaderAffirmFragment deviceCheck = new LeaderAffirmFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Device_Leader", device);
        bundle.putInt("Position", position);
        deviceCheck.setArguments(bundle);
        deviceCheck.show(getSupportFragmentManager(), "LeaderAffirmFragment");
        deviceCheck.setIOnClickListenter(this);
        mDialogs.add(deviceCheck);
    }

    @Override
    protected void initEvents() {
        mIMEI = SessionUtils.getIMEI();
        mStepName = new String[]{"确认接挂杆号", "下达接挂命令", "确认接挂完毕", "下达拆除命令", "确认拆除完毕"};
        mSteps.clear();
        for (int i = 0; i < mStepName.length; i++) {
            mSteps.add(getStep(mStepID[i], mStepName[i], 0));
        }
        //如果第一步是不可操作的，那么将其设置为可操作的
        mSteps.get(0).setState(WorkEnum.StepState.WnderWay.getState());//设置成进行中
        mDevices.clear();
        mDevices0.addAll(mSteps.get(mStepID[0]).getDevices());//设备杆号数据
        mDevices2.addAll(mSteps.get(mStepID[2]).getDevices());//设备杆号数据
        mDevices4.addAll(mSteps.get(mStepID[4]).getDevices());//设备杆号数据
        mDevices.addAll(mDevices0);//首次加载默认显示第一个步骤
        leaderStepAdapter.notifyDataSetChanged();
        deviceAdapter.notifyDataSetChanged();
        leaderStepAdapter.setItemBacView(0);
    }

    private Step getStep(int stepId, String name, int state) {
        Step step = new Step(stepId, name, state);
        ArrayList<Device> devices = new ArrayList<>();
        for (int i = 0; i < ConstData.mDeviceName.length; i++) {
            Device device = new Device(i + 1, ConstData.mDeviceName[i], 0, ConstData.mDeviceLabel[i]);
            device.setStepId(stepId);
            device.setKM(ConstData.KM[i]);
            device.setGroundPosition(ConstData.mGroundPosition[i]);
            devices.add(device);
        }
        step.setDevices(devices);
        return step;
    }


    private String getIpaddress() {
        HashMap<String, NearByPeople> mMap = mApplication.getOnlineUserMap();
        Log.d("NearByPeopleFragment", "HashMap size:" + mMap.size());
        if (mMap == null || mMap.isEmpty()) return null;
        mIpaddress = mMap.get(0).getIpaddress();
        return mIpaddress;
    }

    /**
     * 接收到消息了
     *
     * @param msg 接收到的消息对象
     */
    @Override
    public void processMessage(Message msg) {
        Log.i("SZU_ChatActivity", "进入processMessage()");
        // TODO 待完成
        switch (msg.what) {
            case IPMSGConst.IPMSG_SENDMSG:
                synchronized (mMsg) {
                    final Device device = mMsg.getDevice();
                    if (device == null) return;
                    if (device.getStepId() == mStepID[0] || device.getStepId() == mStepID[2]
                            || device.getStepId() == mStepID[4]) {//第三个或者第五个步骤，确认接挂地线
                        showStepData(device);
                    }
                }
                break;
            case IPMSGConst.IPMSG_RELEASEFILES:  // 拒绝接受文件,停止发送文件线程
                break;
            case IPMSGConst.FILESENDSUCCESS:  // 文件发送成功
                break;
            case IPMSGConst.IPMSG_RECIEVE_IMAGE_DATA: // 图片开始发送
                break;
            case IPMSGConst.IPMSG_RECIEVE_VOICE_DATA: // 语音开始发送
                break;
            case IPMSGConst.IPMSG_GET_IMAGE_SUCCESS:  // 图片发送成功
                break;
        }
    }

    private void showStep0(Device device) {
        if (!isVideoPlayStep1) {
            MediaPlayerHelper.playAssetFile("raw/crystalring.mp3");
            playYZSVoice(R.string.speech_need_ground_affirm);
        }
        if (mPosition != device.getStepId()) {
            mPosition = device.getStepId();
            selectListViewItem(device.getStepId());
        }
        isVideoPlayStep1 = true;//由于步骤1是循环发送过来4个数据，因此响一次声音就行了
        for (int i = 0; i < mDevices.size(); i++) {
            Device dev = mDevices.get(i);
            if (dev.getId() == device.getId()) {
                if (!dev.getRefuse().isEmpty()) {//拒绝状态的杆号，再次发送过来，则铃声要响
                    MediaPlayerHelper.playAssetFile("raw/crystalring.mp3");
                }
                if (dev.isTitle()) continue;
                mDevices.get(i).setImgPath(device.getImgPath()); //设置图片路径
                //                Log.e(TAG, "领导杆号：" + dev.getName() + "图片路径：" + dev.getImgPath());
                //收到消息后，要改变状态
                if (mDevices.get(i).getState() != WorkEnum.DeviceState.WnderWay.getState()
                        || mDevices.get(i).getState() != WorkEnum.DeviceState.Finish.getState()) {
                    mDevices.get(i).setState(WorkEnum.DeviceState.WnderWay.getState());
                    deviceAdapter.notifyDataSetChanged();
                    showDeviceCheckDialog(i, mDevices.get(i));
                }
                break;
            }
        }
    }

    /**
     * 接收到数据，改变数据的状态
     *
     * @param device
     */
    private void showStepData(Device device) {
        mGroundReceiveCount++;//接收到多少个杆号的信息，要累加起来
        Log.e(TAG, "mGroundReceiveCount:==" + mGroundReceiveCount);
        MediaPlayerHelper.playAssetFile("raw/crystalring.mp3");
        if (device.getStepId() == ConstData.mStepID[0]) {
            playDeviceRequestAffirm(device.getName());//步骤一，杆号请求确认
        } else if (device.getStepId() == ConstData.mStepID[2]) {
            playDeviceGroundLead(device.getName());//步骤三，杆号接挂完毕，请求审核
        } else if (device.getStepId() == ConstData.mStepID[4]) {
            playDeviceDismantles(device.getName());//步骤五，杆号拆除完毕，请求审核
        }
        if (mPosition != device.getStepId()) {
            selectListViewItem(device.getStepId());//代码模拟选择步骤选择
        }
        for (int i = 0; i < mDevices.size(); i++) {
            Device dev = mDevices.get(i);
            if (dev.getId() == device.getId()) {
                if (dev.isTitle()) continue;
                mDevices.get(i).setImgPath(device.getImgPath()); //设置图片路径
                //收到消息后，要改变状态
                if (mDevices.get(i).getState() != WorkEnum.DeviceState.WnderWay.getState()
                        || mDevices.get(i).getState() != WorkEnum.DeviceState.Finish.getState()) {
                    mDevices.get(i).setState(WorkEnum.DeviceState.WnderWay.getState());
                    deviceAdapter.notifyDataSetChanged();
                    showDeviceCheckDialog(i, mDevices.get(i));
                }
                break;
            }
        }
        if (mGroundReceiveCount == 4) {
            startRequest();//当接收到四个杆号信息后，开始轮询，后台有可能确认
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_ground:
                break;
            case R.id.btn_revice:
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
        msg.setDevice(device);
        switch (type) {
            case TEXT:
                //                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG,
                //                        getIpaddress(), msg);
                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, "192.168.43.1", msg);//发送给服务端
                //带指纹PDA IP地址：192.168.43.162
                // mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, "192.168.43.162", msg);
                break;

            case IMAGE:
                GroundMessage imageMsg = msg.clone();
                imageMsg.setMsgContent(FileUtils.getNameByPath(msg.getMsgContent()));
                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, mIpaddress, imageMsg);
                break;

            case VOICE:
                GroundMessage voiceMsg = msg.clone();
                voiceMsg.setMsgContent(FileUtils.getNameByPath(msg.getMsgContent()));
                mUDPSocketThread.sendUDPdata(IPMSGConst.IPMSG_SENDMSG, mIpaddress, voiceMsg);
                break;

            case FILE:
                break;
        }
    }

    /**
     * 是否有对应的聊天界面打开
     *
     * @param paramMsg 收到的消息对象
     * @return
     */
    @Override
    public boolean isThisActivityMsg(GroundMessage paramMsg) {
        mMsg = paramMsg;
        return true;
    }

    @Override
    public void onMyClick(int btnItem, int position, Device device) {
        switch (btnItem) {
            case 1://通过
                if (mDevices.get(position).getState() == WorkEnum.DeviceState.Finish.getState()) {
                    DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("该杆号已审核通过，不可再操作");
                    playYZSVoice("该杆号已审核通过，无需再操作");
                    return;
                }
                mDevices.get(position).setRefuseByLeader(false);
                mDevices.get(position).setState(WorkEnum.DeviceState.Finish.getState());
                refuseStep(mDevices.get(position).getStepId());//更新步骤
                break;
            case 2://拒绝
                mDevices.get(position).setRefuseByLeader(true);
                mDevices.get(position).setState(WorkEnum.DeviceState.Error.getState());
                mDevices.get(position).setRefuse(device.getRefuse());
                break;
        }
        deviceAdapter.notifyDataSetChanged();
        sendMessage(mDevices.get(position), "领导人", GroundMessage.CONTENT_TYPE.TEXT);
        //如果杆号已经处理（包含拒绝、通过）完了，则发送数据通知后台
        //        if (isDisposehStep(mDevices)) {
        int[] stepRet = sendLeaderStepOperation();
        if (stepRet[0] == 1) {
            Log.e(TAG, "领导端发送确认通知给后台");
            stopRequest();//停止轮询
            mGroundReceiveCount = 0;
            int stepID = mDevices.get(0).getStepId();
            if (stepID == ConstData.mStepID[0]) {
                stepID = stepID + 2;
            } else if (stepID == ConstData.mStepID[2]) {
                stepID = stepID + 3;
            } else if (stepID == ConstData.mStepID[4]) {
                stepID = stepID + 4;
            }
            groundUpload(stepID, "0", stepRet[1]);//领导端审核完了要发送给后台端
        }
    }

    @Override
    public void onDismiss(int btnItem, int position, Device device) {
        //        DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("Dialog消失==" +
        //                position);
        //        if (mDialogs.size() > 0) {
        //            mDialogs.remove(position);
        //        }
        if (mDialogs.size() > 0) {
            //            for (int i = 0; i < mDevices.size(); i++) {
            //
            //            }
            //            mDialogs.clear();
            mDialogs.remove(mDialogs.size() - 1);
        }
        if (position == 0 && mDialogs.size() > 0) {
            mDialogs.clear();
        }
    }

    private int[] sendLeaderStepOperation() {
        int[] result = new int[]{1, 1};//第一位，代表杆号已经审核完毕；第二位代表是否有拒绝状态的杆号，有则为2
        for (Device device : mDevices) {
            if (device.getState() == WorkEnum.DeviceState.WnderWay.getState()
                    || device.getState() == WorkEnum.DeviceState.Normal.getState()) {
                result[0] = -1;//如果还有“正常”状态或“进行中”状态，则标识为处理完
                return result;
            }
            //如果有拒绝的话，则数组的第二个就存拒绝状态
            if (device.getState() == WorkEnum.DeviceState.Error.getState()) {
                result[0] = 2;
            }
        }
        return result;
    }

    /**
     * 更新步骤
     */
    private void refuseStep(int stepID) {
        if (stepID == mStepID[0]) {
            if (!isFinishStep(mDevices0)) return;//当前步骤是否已经完成
        } else if (stepID == mStepID[2]) {
            if (!isFinishStep(mDevices2)) return;//当前步骤是否已经完成
        } else if (stepID == mStepID[4]) {
            if (!isFinishStep(mDevices4)) return;//当前步骤是否已经完成
        }
        for (int i = 0; i < mSteps.size(); i++) {
            if (stepID == mSteps.get(i).getId()) {
                mSteps.get(i).setState(WorkEnum.StepState.Finish.getState());
                if (i != mSteps.size() - 1) {
                    mSteps.get(i + 1).setState(WorkEnum.StepState.WnderWay.getState());
                }
                leaderStepAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_leader_step:
                mPosition = position;//表示当前正在执行的步骤
                showRightLayout(position);
                break;
        }
    }

    private void showRightLayout(int stepPosition) {
        //第一个步骤
        if (stepPosition == mStepID[0]) {
            notifyAdapter(stepPosition);
        }
        //第二个步骤
        else if (stepPosition == mStepID[1]) {
            if (mSteps.get(0).getState() == WorkEnum.StepState.Finish.getState() || isFinishStep(mDevices0)) {
                llDevice.setVisibility(View.GONE);
                llSetOrder.setVisibility(View.VISIBLE);
                leaderStepAdapter.setItemBacView(stepPosition);
                //步骤未完成则下达命令
                if (mSteps.get(1).getState() != WorkEnum.StepState.Finish.getState()) {
                    playYZSVoice(R.string.speech_affirm_finger);//请确认指纹
                    FingerprintUtil.getFeature(10000);//获取指纹特征
                    //                    DialogHelper.getInstance(GroundLeaderActivity1.this)
                    //                            .showDialogNoButton("请确认您的指纹", new IDialogAction1() {
                    //                                @Override
                    //                                public void action(int position) {
                    ////                                    mFingerStepID = mStepID[1];//第二个步骤下达命令
                    ////                                    getFeatureCode("得到指纹特征");
                    //                                }
                    //                            });
                    showTimeCount(9);
                } else {
                    showInfo("提示：接挂命令已下达\n下达人："+ConstData.LEADER);
                }
            } else {
                DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
            }
        }
        //第三个步骤
        else if (stepPosition == mStepID[2]) {
            if (mSteps.get(1).getState() == WorkEnum.StepState.Finish.getState()) {
                notifyAdapter(stepPosition);

            } else {
                DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
            }
        }
        //第四个步骤
        else if (stepPosition == mStepID[3]) {
            if (mSteps.get(2).getState() == WorkEnum.StepState.Finish.getState() || isFinishStep(mDevices2)) {
                llDevice.setVisibility(View.GONE);
                llSetOrder.setVisibility(View.VISIBLE);
                leaderStepAdapter.setItemBacView(stepPosition);
                //步骤未完成则下达命令
                if (mSteps.get(mStepID[3]).getState() != WorkEnum.StepState.Finish.getState()) {
                    showInfo("");//文字设置为空的
                    playYZSVoice(R.string.speech_affirm_finger);//请确认指纹
                    FingerprintUtil.getFeature(10000);//获取指纹特征
                    //                    DialogHelper.getInstance(GroundLeaderActivity1.this)
                    //                            .showDialogNoButton("请确认您的指纹", new IDialogAction1() {
                    //                                @Override
                    //                                public void action(int position) {
                    ////                                    mFingerStepID = mStepID[3];//第二个步骤下达命令
                    ////                                    getFeatureCode("得到指纹特征");
                    //                                }
                    //                            });
                    showTimeCount(9);
                } else {
                    showInfo("提示：拆除命令已下达\n下达人："+ConstData.LEADER);
                }
            } else {
                DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
            }
        }
        //第五个步骤
        else if (stepPosition == mStepID[4]) {
            if (mSteps.get(3).getState() == WorkEnum.StepState.Finish.getState()) {
                notifyAdapter(stepPosition);
            } else {
                DialogHelper.getInstance(this).showDialogWarning("请先将上一个步骤执行完毕");
            }
        }
    }

    /**
     * 刷新listview
     *
     * @param stepPosition
     */
    private void notifyAdapter(int stepPosition) {
        Animation rightIn = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        llDevice.setVisibility(View.VISIBLE);
        llDevice.startAnimation(rightIn);
        llSetOrder.setVisibility(View.GONE);
        mDevices.clear();
        if (stepPosition == mStepID[0]) {
            mDevices.addAll(mDevices0);
        }
        if (stepPosition == mStepID[2]) {
            mDevices.addAll(mDevices2);
        } else if (stepPosition == mStepID[4]) {
            mDevices.addAll(mDevices4);
        }
        deviceAdapter.notifyDataSetChanged();
        leaderStepAdapter.setItemBacView(stepPosition);
    }

    /**
     * 判断步骤（1、3、5）是否有处理过，处理过的包含（拒绝、通过）
     *
     * @return
     */
    private boolean isDisposehStep(ArrayList<Device> devices) {
        int deviceSize = devices.size();
        for (int i = 0; i < deviceSize; i++) {
            if (devices.get(i).getState() == WorkEnum.DeviceState.Normal.getState()
                    || devices.get(i).getState() == WorkEnum.DeviceState.WnderWay.getState()) {
                return false;//未处理完
            }
        }
        return true;
    }

    /**
     * 判断步骤（1、3、5）是否完成
     *
     * @return
     */
    private boolean isFinishStep(ArrayList<Device> stepDevices) {
        int deviceSize = stepDevices.size();
        for (int i = 0; i < deviceSize; i++) {
            if (stepDevices.get(i).getState() != WorkEnum.DeviceState.Finish.getState()) {
                return false;//步骤未完成
            }
        }
        return true;
    }

    @Override
    public void response(GroundState groundState) {
        if (groundState == null) return;
        if (mPosition == ConstData.mStepID[0]) {
            //杆号确认下达
            if (groundState.getConfirm() == 1) {
                playYZSVoice("所有杆号已确认");
                changeDeviceState(mDevices0);
            }
        } else if (mPosition == ConstData.mStepID[2]) {
            //接挂结果审核通过
            if (groundState.getAddConfirm() == 1) {
                playYZSVoice("所有杆号接挂审核通过");
                changeDeviceState(mDevices2);
            }
        } else if (mPosition == ConstData.mStepID[4]) {
            //拆除结果审核通过
            if (groundState.getDelConfirm() == 1) {
                playYZSVoice("所有杆号拆除审核通过");
                changeDeviceState(mDevices4);
            }
        }
    }

    private void changeDeviceState(ArrayList<Device> devices) {
        mGroundReceiveCount = 0;//将接收到的数据置成0
        stopRequest();//停止轮询
        MediaPlayerHelper.playAssetFile("raw/crystalring.mp3");
        for (Device device : devices) {
            device.setState(WorkEnum.DeviceState.Finish.getState());//将杆号状态改变
        }
        if (mPosition != devices.get(0).getStepId()) {
            selectListViewItem(mPosition);//代码模拟选择步骤选择
        } else {
            deviceAdapter.notifyDataSetChanged();
        }
        refuseStep(devices.get(0).getStepId());//刷新步骤
        if (mDevices.size() > 0) {
            //让弹窗消失
            for (int i = 0; i < mDialogs.size(); i++) {
                if (mDialogs.get(i).isVisible()) {
                    mDialogs.get(i).dismiss();
                }
            }
            mDialogs.clear();//并清空掉弹窗
        }
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

    private class MyFPCallback implements FingerprintUtil.Callback {
        @Override
        public void onConnectDevice(boolean isConnectSuccess, int errorCode) {
            if (isConnectSuccess) {
                DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("设备已开启");
            } else {
                DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("开启设备失败");
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
            getFeatureCode(featureCode);//获取到指纹，开始下达命令
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

    /**
     * 获取到指纹并下达命令
     */
    private void getFeatureCode(String featureCode) {
        if (featureCode != null && !featureCode.isEmpty()) {
            //发送第二或第四步骤的信息
            Device device = new Device();
            device.setStepId(mPosition);
            sendMessage(device, "领导人", GroundMessage.CONTENT_TYPE.TEXT);
            DialogHelper.getInstance(GroundLeaderActivity1.this).showToastShort("指纹确认成功");
            mSteps.get(mPosition).setState(WorkEnum.StepState.Finish.getState());
            mSteps.get(mPosition + 1).setState(WorkEnum.StepState.WnderWay.getState());
            leaderStepAdapter.notifyDataSetChanged();
            if (mPosition == mStepID[1]) {
                groundUpload(mStepID[1] + 2, "0", 1);// 发送数据给后台，1代表正常，下达命令后，这个步骤状态是正常的
                playYZSVoice(R.string.speech_finger_right_set_ground_order);
                showInfo("提示：接挂命令已下达\n下达人："+ConstData.LEADER);
            } else if (mPosition == mStepID[3]) {
                groundUpload(mStepID[3] + 3, "0", 1);// 发送数据给后台，1代表正常，下达命令后，这个步骤状态是正常的
                playYZSVoice(R.string.speech_finger_right_set_dismantle_order);
                showInfo("提示：拆除命令已下达\n下达人："+ConstData.LEADER);
            }
            dismissTimeCount();//取消对话框
        }
        //        DialogHelper.getInstance(GroundLeaderActivity1.this).cancelFingerDialog();
    }

    private void showInfo(String info) {
        tvAlreadySetOrder.setVisibility(View.VISIBLE);
        tvAlreadySetOrder.setText(info);
    }

    private void dismissTimeCount() {
        if (downDialog == null) return;
        downDialog.dismiss();
        downDialog = null;
    }

    private void showTimeCount(int time) {
        downDialog = new CountDownDialog(this, 0, time);
        downDialog.show();
    }

    @Override
    public void finish() {
        FingerprintUtil.closeDevice();
        stopRequest();//停止轮询
        if (mUDPSocketThread != null) mUDPSocketThread.stopUDPSocketThread();//停止接收socket接收线程
        super.finish();
    }
}
