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
import android.widget.EditText;
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

public class LeaderAffirmFragment extends DialogFragment {

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
    @BindView(R.id.et_leader_refuse)
    EditText etRefuse;
    @BindView(R.id.tv_show_check_text)
    TextView tvShowText;
    private Device mDevice;
    private int mPosition;
    private String filePath = "";
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
        mDevice = (Device) bundle.getSerializable("Device_Leader");
        getActivity().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_leader_affirm_dialog, container, false);
        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        init();
        return view;
    }

    private void init() {
        tvShowText.setText("接地位置" + mDevice.getName() + mDevice.getGroundPosition() + "已定位，请求审核");
        if (mDevice.getState() == WorkEnum.DeviceState.Finish.getState()) {
            btnDialogOoperationPositive.setEnabled(false);
            btnDialogOoperationNegative.setEnabled(false);
        }
        btnDialogOoperationPositive.setText("确认通过");
        btnDialogOoperationNegative.setText("拒绝通过");
        String imgPath = mDevice.getImgPath();
        if (imgPath != null && !imgPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            if (bitmap != null) {
                tvShowText.setVisibility(View.GONE);
                ivImage.setVisibility(View.VISIBLE);
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
                etRefuse.setVisibility(View.VISIBLE);
                etRefuse.setText("拒绝原因：" + refuse);
                etRefuse.setEnabled(false);
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
                    mDevice.setImgPath(filePath);
                    mDevice.setRefuse("");
                    mIOnClickListenter.onMyClick(1, mPosition, mDevice);
                    mIOnClickListenter.onDismiss(1, mPosition, mDevice);
                    //                    DialogHelper.getInstance(getActivity()).showToastShort("发送成功");
                    dismiss();
                }
                break;
            case R.id.btn_dialog_ooperation_negative:
                String refuse = etRefuse.getText().toString().trim();
                if (refuse.isEmpty()) {
                    etRefuse.setVisibility(View.VISIBLE);
                } else {
                    if (mIOnClickListenter != null) {
                        mDevice.setImgPath(filePath);
                        mDevice.setRefuse(refuse);
                        mIOnClickListenter.onMyClick(2, mPosition, mDevice);
                        mIOnClickListenter.onDismiss(2, mPosition, mDevice);
                        //                        DialogHelper.getInstance(getActivity()).showToastShort("发送成功");
                        dismiss();
                    }
                }
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
