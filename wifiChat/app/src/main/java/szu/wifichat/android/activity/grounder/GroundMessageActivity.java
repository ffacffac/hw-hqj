package szu.wifichat.android.activity.grounder;

import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;

import com.squareup.okhttp.Request;

import java.io.IOException;

import szu.wifichat.android.bluetooth.BTBaseActivity;
import szu.wifichat.android.conf.Configs;
import szu.wifichat.android.dialog.LeaderGroundPositionDialog;
import szu.wifichat.android.dialog.WarningDialog;
import szu.wifichat.android.entity.GroundMessage;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.httputils.OkHttpClientManager;
import szu.wifichat.android.interfaces.IGroundUpload;
import szu.wifichat.android.socket.UDPSocketThread;
import szu.wifichat.android.uhf.ScanFactory;

public abstract class GroundMessageActivity extends BTBaseActivity implements OnClickListener {

    public ScanFactory mScanFactory = null;// 扫描工厂类
    public boolean isRFID;// 是否超高频标签
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    //    protected HeaderLayout mHeaderLayout;
    //    protected ChatListView mClvList;
    //    protected ScrollLayout mLayoutScroll;
    //    protected LinearLayout mLayoutRounds;
    //    protected EmoteInputView mInputView;
    //
    //    protected ImageButton mIbTextDitorPlus;
    //    protected ImageButton mIbTextDitorKeyBoard;
    //    protected ImageButton mIbTextDitorEmote;
    //    protected EmoticonsEditText mEetTextDitorEditer;
    //    protected Button mBtnTextDitorSend;
    //    protected ImageView mIvTextDitorAudio;
    //    protected ImageView mIvAvatar;
    //
    //    protected ImageButton mIbAudioDitorPlus;
    //    protected ImageButton mIbAudioDitorKeyBoard;
    //    protected ImageView mIvAudioDitorAudioBtn;
    //
    //    protected LinearLayout mLayoutFullScreenMask;
    //    protected LinearLayout mLayoutMessagePlusBar;
    //    protected LinearLayout mLayoutMessagePlusPicture;
    //    protected LinearLayout mLayoutMessagePlusCamera;
    //    protected LinearLayout mLayoutMessagePlusLocation;
    //    protected LinearLayout mLayoutMessagePlusGift;

    // protected List<GroundMessage> mMessagesList = new ArrayList<GroundMessage>(); // 消息列表
    //    protected ChatAdapter mAdapter;
    // protected NearByPeople mPeople; // 聊天的对象
    //    protected SqlDBOperate mDBOperate;// 新增数据库类可以操作用户数据库和聊天信息数据库

    // protected Bitmap mRoundsSelected;
    // protected Bitmap mRoundsNormal;

    //    private ChatPopupWindow mChatPopupWindow;
    //    private int mWidth;
    //    private int mHeaderHeight;

    //    protected SimpleListDialog mDialog;
    //    protected int mCheckId = 0;
    //    protected BaseDialog mSynchronousDialog;
    //    protected String mCameraImagePath;

    // 录音变量
    //    protected String mVoicePath;
    // private static final int MAX_RECORD_TIME = 30; // 最长录制时间，单位秒，0为无时间限制
    //    protected static final int MIN_RECORD_TIME = 1; // 最短录制时间，单位秒，0为无时间限制
    //    protected static final int RECORD_OFF = 0; // 不在录音
    //    protected static final int RECORD_ON = 1; // 正在录音
    //    protected String RECORD_FILENAME; // 录音文件名
    //
    //    protected TextView mTvRecordDialogTxt;
    //    protected ImageView mIvRecVolume;
    //
    //    protected Dialog mRecordDialog;
    //    protected AudioRecorderUtils mAudioRecorder;
    //    protected Thread mRecordThread;
    //
    //    protected int recordState = 0; // 录音状态
    //    protected float recodeTime = 0.0f; // 录音时长
    //    protected double voiceValue = 0.0; // 录音的音量值
    //    protected boolean isMove = false; // 手指是否移动
    //    protected float downY;

    // 文件传输变量
    // protected TcpClient tcpClient = null;
    // protected TcpService tcpService = null;
    // protected HashMap<String, FileState> sendFileStates;
    // protected HashMap<String, FileState> reciveFileStates;

    //    protected String mNickName;
    protected String mIMEI;
    protected String mIpaddress;
    //    protected int mID;
    //    protected int mSenderID;

    protected boolean isClient = true;//默认是客户端
    protected GroundMessage mMsg;//接收到的消息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUDPSocketThread = UDPSocketThread.getInstance(mApplication, this); // 获取对象
    }

    private static final String TAG_GROUND_UPLOAD = "GroundUpload";

    /**
     * 发送数据给后台
     *
     * @param stepID    步骤
     * @param grounName 杆号名称，如果是下达命令的步骤，或者领导端的审核步骤，则杆号名称为0,
     * @param state     1--正常，2--有拒绝状态的
     */
    public void groundUpload(int stepID, String grounName, int state) {
        if (grounName.contains("#")) {
            grounName = grounName.replace("#", "");
        }
        StringBuffer url = new StringBuffer().append(Configs.GROUND_UPLOAD).append(stepID).append("/").append(grounName)
                                             .append("/").append(state);
        Log.e(TAG_GROUND_UPLOAD, "发送杆号信息" + url);
        //同时发送给后台调度员
        OkHttpClientManager.getInstance().getAsyn1(url.toString(), new IGroundUpload() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG_GROUND_UPLOAD, "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(String response) throws IOException {
                Log.e(TAG_GROUND_UPLOAD, "请求成功：" + response);
            }
        });
    }

    WarningDialog mWarningDialog = null;
    LeaderGroundPositionDialog mLeaderDialog = null;

    protected void showWarningDialog(Device device) {
        if (mWarningDialog != null && mWarningDialog.isShowing()) {
            mWarningDialog.dismiss();
            mWarningDialog = null;
        }
        mWarningDialog = new WarningDialog(this, device.getName());
        mWarningDialog.show();
    }

    protected void dismissWarningDialog() {
        if (mWarningDialog != null && mWarningDialog.isShowing()) {
            mWarningDialog.dismiss();
            mWarningDialog = null;
        }
    }

    protected void showLeaderDialog(Device device) {
        if (mLeaderDialog != null && mLeaderDialog.isShowing()) {
            mLeaderDialog.dismiss();
            mLeaderDialog = null;
        }
        mLeaderDialog = new LeaderGroundPositionDialog(this, device.getName() + device.getGroundPosition());
        mLeaderDialog.show();
    }

    protected void dismissLeaderDialog() {
        if (mLeaderDialog != null && mLeaderDialog.isShowing()) {
            mLeaderDialog.dismiss();
            mLeaderDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }
}
