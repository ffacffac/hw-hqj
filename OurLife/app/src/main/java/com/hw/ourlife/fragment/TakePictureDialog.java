package com.hw.ourlife.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.hw.baselibrary.customview.CameraSurfaceView;
import com.hw.baselibrary.util.LogUtils;
import com.hw.ourlife.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 拍照dialog
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-11.
 */

public class TakePictureDialog extends BaseDialogFragment {

    private static final String TAG = TakePictureDialog.class.getName();
    @BindView(R.id.view_camera)
    CameraSurfaceView cameraSurfaceView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dialog全屏设置，去掉周围padding
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_take_picture;
    }

    @Override
    protected void initEventAndData() {
        cameraSurfaceView.setMyCameraCallback(new CameraCallback());
    }

    @OnClick({R.id.btn_take_picture})
    void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_picture:
                try {
                    cameraSurfaceView.takePictures();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.ex(TAG, "onMyClick", e);
                }
                break;
            default:
                break;
        }
    }

    private class CameraCallback implements CameraSurfaceView.MyCameraCallback {

        @Override
        public void onPictureTaken(Bitmap bitmap) {
            if (callback != null) {
                callback.onPictureTaken(bitmap);
            }
        }

        @Override
        public void onException(Exception e) {
        }
    }

    private CameraDialogCallback callback;

    public void setCameraDialogCallback(CameraDialogCallback callback) {
        this.callback = callback;
    }

    public interface CameraDialogCallback {
        /**
         * 拍照成功
         *
         * @param bitmap bitmap
         */
        void onPictureTaken(Bitmap bitmap);

        /**
         * 录像成功
         *
         * @param videoPath 路径
         * @param videoName 名称
         */
        void onVideoTaken(String videoPath, String videoName);

        /**
         * 结束拍照，返回
         */
        void back();
    }

    @Override
    public void onStop() {
        super.onStop();
        cameraSurfaceView.releaseCamera();
        if (callback != null) {
            callback.back();
        }
    }
}
