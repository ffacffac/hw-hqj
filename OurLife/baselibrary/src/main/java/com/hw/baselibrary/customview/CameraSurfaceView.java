package com.hw.baselibrary.customview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;

/**
 * 自定义相机
 *
 * @author huangqj
 *         Created by huangqj on 2017-07-12.
 */

public class CameraSurfaceView extends SurfaceView {

    public static final String TAG = "CameraSurfaceView";
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    protected static final int[] VIDEO_720 = {1280, 720};
    protected static final int[] VIDEO_1080 = {1920, 1080};
    private int screenOritation = Configuration.ORIENTATION_PORTRAIT;
    private Context mContext;

    public CameraSurfaceView(Context context) {
        super(context);
        mContext = context;
        initCameraSurfaceView();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initCameraSurfaceView();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initCameraSurfaceView();
    }

    /**
     * 初始化
     */
    private void initCameraSurfaceView() {
        try {
            mSurfaceHolder = getHolder();
            //设置屏幕常亮
            mSurfaceHolder.setKeepScreenOn(true);
            mSurfaceHolder.addCallback(new HolderCallback());
            openCamera();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onException(e);
            }
        }
    }

    /**
     * 打开相机（默认打开的是背后的镜头）
     */
    private void openCamera() {
        mCamera = Camera.open();
    }

    /**
     * 设置预览参数
     */
    private void initPreview() throws IOException {
        if (mCamera == null) {
            return;
        }
        mParameters = mCamera.getParameters();
        Camera.Size previewSize = CamParaUtil
                .getSize(mParameters.getSupportedPreviewSizes(), 1000, mCamera.new Size(VIDEO_1080[0], VIDEO_1080[1]));
        //设置预览参数
        mParameters.setPreviewSize(previewSize.width, previewSize.height);
        // mParameters.setPreviewSize(800, 480);//预览的参数可以设置成手机尺寸
        Camera.Size pictureSize = CamParaUtil
                .getSize(mParameters.getSupportedPictureSizes(), 1500, mCamera.new Size(VIDEO_1080[0], VIDEO_1080[1]));
        //图片的尺寸一般比预览的尺寸大
        mParameters.setPictureSize(pictureSize.width, pictureSize.height);
        if (CamParaUtil.isSupportedFormats(mParameters.getSupportedPictureFormats(), ImageFormat.JPEG)) {
            mParameters.setPictureFormat(ImageFormat.JPEG);
            mParameters.setJpegQuality(100);
        }
        if (CamParaUtil.isSupportedFocusMode(mParameters.getSupportedFocusModes(), FOCUS_MODE_AUTO)) {
            mParameters.setFocusMode(FOCUS_MODE_AUTO);
        }
        if (screenOritation != Configuration.ORIENTATION_LANDSCAPE) {
            mParameters.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            mParameters.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        mCamera.setParameters(mParameters);
        //设置预览回调
        mCamera.setPreviewDisplay(mSurfaceHolder);
        //开始预览画面
        mCamera.startPreview();
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (mCamera == null) {
            return;
        }
        //开始预览画面
        mCamera.startPreview();
    }

    /**
     * 拍照
     */
    public void takePictures() throws Exception {
        if (mCamera == null) {
            throw new Exception("相机未初始化");
        }
        mCamera.takePicture(null, null, new PictureCallback());
    }


    private class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            // openCamera();
            try {
                initPreview();
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onException(e);
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            stopPreview();
        }
    }

    /**
     * 得到照片的回调
     */
    private class PictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Log.e(TAG, "拍照成功");
            showToast("拍照成功");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix matrix = new Matrix();
            //相机默认是横屏的，要旋转90度
            matrix.setRotate(90f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            startPreview();
            if (callback != null) {
                callback.onPictureTaken(bitmap);
            }
        }
    }

    /**
     * 停止预览
     */
    private void stopPreview() {
        if (mCamera == null) {
            return;
        }
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
    }

    /**
     * 关闭相机
     */
    public void releaseCamera() {
        if (mCamera == null) {
            return;
        }
        mCamera.setPreviewCallback(null);
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }


    /**
     * ___________________________________以下为视频录制模块______________________________________
     **/
    MediaRecorder mediaRecorder = new MediaRecorder();
    private boolean isRecording = false;
    private boolean mOpenBackCamera = true;

    public boolean isRecording() {
        return isRecording;
    }

    public boolean startRecord(String videoPath) {
        return startRecord(-1, null, videoPath);
    }

    public boolean startRecord(int maxDurationMs, MediaRecorder.OnInfoListener onInfoListener, String videoPath) {
        if (mCamera == null) {
            return false;
        }
        // if (!FileUtil.isExternalStorageWritable()) {
        //     Toast.makeText(context, "请插入存储卡", Toast.LENGTH_SHORT).show();
        //     return false;
        // }
        mCamera.unlock();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        Camera.Size videoSize = CamParaUtil
                .getSize(mParameters.getSupportedVideoSizes(), 1200, mCamera.new Size(VIDEO_720[0], VIDEO_720[1]));
        Log.e("VideoSize.width", videoSize.width + "");
        Log.e("VideoSize.height", videoSize.height + "");
        //        mediaRecorder.setVideoSize(videoSize.width, videoSize.height);
        //        mSurfaceHolder.setFixedSize(640, 480);//
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        //        mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
        //        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());//设置录制预览surface
        if (mOpenBackCamera) {
            mediaRecorder.setOrientationHint(90);
        } else {
            if (screenOritation == Configuration.ORIENTATION_LANDSCAPE) {
                mediaRecorder.setOrientationHint(90);
            } else {
                mediaRecorder.setOrientationHint(270);
            }
        }
        if (maxDurationMs != -1) {
            mediaRecorder.setMaxDuration(maxDurationMs);
            mediaRecorder.setOnInfoListener(onInfoListener);
        }
        // mediaRecorder.setOutputFile(FileUtil.getMediaOutputPath());
        mediaRecorder.setOutputFile(videoPath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stopRecord() {
        if (!isRecording) {
            return;
        }
        mediaRecorder.setPreviewDisplay(null);
        try {
            mediaRecorder.stop();
            isRecording = false;
            showToast("录制完毕，视频已保存");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private MyCameraCallback callback;

    public void setMyCameraCallback(MyCameraCallback callback) {
        this.callback = callback;
    }

    public interface MyCameraCallback {
        void onPictureTaken(Bitmap bitmap);

        void onException(Exception e);
    }
}
