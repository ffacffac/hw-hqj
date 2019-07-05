package com.hw.baselibrary.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.hw.baselibrary.constant.Constant;

import java.io.File;

/**
 * @author huangqj
 *         Created by huangqj on 2018-11-27.
 */

public class PhotoUtils {

    /**
     * 获取一个新的照片名称("account"+"yyyyMMddHHmmss"+".jpg")
     *
     * @return 20190410170325.jpg
     */
    public static String getPhotoName() {
        // 照片命名
        return DateUtils.formatNowDate(DateUtils.FORMATE_STRING_4) + ".jpg";
    }

    /**
     * 拍照获取图片,注意7.0系统的拍照功能需要处理
     *
     * @param imgName     自定义名
     * @param requestCode 回调code
     */
    public static void getPhotoFromCamera(Activity context, final int requestCode, String imgName,
                                          IPhotoCallback photoCallback) {
        try {
            String imgDirStr = Constant.MEDIA_FILE_DIR;
            String imageName;// 照片命名
            if (imgName == null || imgName.isEmpty()) {
                // 照片命名
                imageName = getPhotoName();
            } else {
                imageName = imgName;
            }
            File imgDir = new File(imgDirStr);
            if (!imgDir.exists()) {
                imgDir.mkdir();
                imgDir.mkdirs();
            }
            File imgFile = new File(imgDir, imageName);
            if (photoCallback != null) {
                photoCallback.onPhotoMsg(imgDirStr, imageName);
            }
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imgUri;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                //参数2为在mainifest中注册的provider的authorities
                imgUri = FileProvider.getUriForFile(context, Constant.FILE_PROVIDER, imgFile);
                //表示对目标应用临时授权该Uri所代表的文件
                imageCaptureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                imgUri = Uri.fromFile(imgFile);
            }
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            context.startActivityForResult(imageCaptureIntent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            // LogInfoHelper.error(e);
            // DialogHelper.getInstance(context).showDialogError("拍照获取图片时出错！", e);
        }
    }

    /**
     * 获取本地相册图片
     *
     * @param context     activity
     * @param requestCode 请求码
     */
    public static void choseImageFromGallery(Activity context, final int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, requestCode);
    }

    public interface IPhotoCallback {
        /**
         * 拍照回调
         *
         * @param imgDirStr 文件夹
         * @param imageName 文件名
         */
        void onPhotoMsg(String imgDirStr, String imageName);
    }
}
