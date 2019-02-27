package szu.wifichat.android.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import szu.wifichat.android.R;
import szu.wifichat.android.conf.Configs;
import szu.wifichat.android.conf.WorkEnum;
import szu.wifichat.android.groundbeen.Device;
import szu.wifichat.android.interfaces.IOnClickListenter;

/**
 * Created by huangqj on 2017-06-05.
 */

public class DeviceCheckFragment extends DialogFragment {

    @BindView(R.id.tv_dialog_ooperation_title)
    TextView tvTitle;
    @BindView(R.id.iv_device_check_image)
    ImageView ivImage;
    @BindView(R.id.iv_device_check_photo)
    ImageView ivPhoto;
    @BindView(R.id.btn_dialog_ooperation_positive)
    Button btnDialogOoperationPositive;
    @BindView(R.id.btn_dialog_ooperation_negative)
    Button btnDialogOoperationNegative;
    @BindView(R.id.tv_way_refuse)
    TextView tvWayRefuse;
    @BindView(R.id.tv_show_check_text)
    TextView tvShowText;
    //    private StepAndDevice mStepDevice;
    private Device mDevice;
    private int mPosition;

    String filePath = "";

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private IOnClickListenter mIOnClickListenter;

    public void setIOnClickListenter(IOnClickListenter iOnClickListenter) {
        this.mIOnClickListenter = iOnClickListenter;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mPosition = bundle.getInt("Position", -1);
        //        mStepDevice = (StepAndDevice) bundle.getSerializable("Device");
        mDevice = (Device) bundle.getSerializable("Device");
        getActivity().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_device_check, container, false);
        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        init();
        return view;
    }

    private void init() {
        //杆号已经完成的话就不能再操作“请求审核”按钮
        if (mDevice.getState() == WorkEnum.DeviceState.Finish.getState()) {
            btnDialogOoperationPositive.setEnabled(false);
        }
        //        if (mDevice.getStepId() == 0) {
        //            btnDialogOoperationPositive.setText("保存信息");
        btnDialogOoperationPositive.setText("请求审核");
        btnDialogOoperationNegative.setText("取消");
        //        } else {
        //            btnDialogOoperationPositive.setText("确认发送");
        //            btnDialogOoperationPositive.setText("请求审核");
        //            btnDialogOoperationNegative.setText("取消");
        //        }
        //状态是已完成的话就屏蔽掉相片按钮
        if (mDevice.getState() == WorkEnum.DeviceState.Finish.getState()) {
            ivPhoto.setVisibility(View.GONE);
        }
        tvShowText.setText("接地位置" + mDevice.getName() + mDevice.getGroundPosition() + "已定位，请求审核");
        String imgPath = mDevice.getImgPath();//设置图片
        if (imgPath != null && !imgPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            if (bitmap != null) {
                ivImage.setVisibility(View.VISIBLE);
                tvShowText.setVisibility(View.GONE);
                ivImage.setImageBitmap(bitmap);
            }
        }
        if (mDevice != null) {
            String strState = getState();
            if (strState != null && !strState.isEmpty()) {
                tvTitle.setText(mDevice.getName() + "(" + getState() + ")");
            } else {
                tvTitle.setText(mDevice.getName());
            }
            String refuse = mDevice.getRefuse();
            if (refuse != null && !refuse.isEmpty()) {
                tvWayRefuse.setVisibility(View.VISIBLE);
                tvWayRefuse.setText("拒绝原因：" + refuse);
            }
        }
    }

    private String getState() {
        String strState = "";
        int deviceState = mDevice.getState();
        if (deviceState == WorkEnum.DeviceState.Finish.getState()) {
            strState = "已确认";
        } else if (deviceState == WorkEnum.DeviceState.Error.getState()) {
            strState = "已拒绝";
        }
        return strState;
    }

    @OnClick({R.id.btn_dialog_ooperation_positive, R.id.btn_dialog_ooperation_negative, R.id.iv_device_check_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_ooperation_positive:
                if (mIOnClickListenter != null) {
                    //如果杆号状态已经完成，那么不在进行处理了
                    if (mDevice.getState() != WorkEnum.DeviceState.Finish.getState()) {
                        mDevice.setImgPath(filePath);
                        mIOnClickListenter.onMyClick(1, mPosition, mDevice);
                        //                        if (mDevice.getStepId() != 0) {
                        //                            DialogHelper.getInstance(getActivity()).showToastShort("发送成功");
                        //                        }
                    }
                }
                dismiss();
                break;
            case R.id.btn_dialog_ooperation_negative:
                if (mIOnClickListenter != null) {
                    if (mDevice.getState() != WorkEnum.DeviceState.Finish.getState()) {
                        mIOnClickListenter.onMyClick(2, mPosition, mDevice);
                    }
                }
                this.dismiss();
                break;
            case R.id.iv_device_check_photo:
                File file = new File(Configs.photoPath);
                File[] files = file.listFiles();
                if (file != null) {
                    if (mPosition >= 0 && mPosition % 5 != 0) {
                        filePath = files[mPosition % 5 - 1].getAbsolutePath();
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap == null) return;
                    tvShowText.setVisibility(View.GONE);
                    ivImage.setVisibility(View.VISIBLE);
                    ivImage.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
