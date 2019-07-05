package com.hw.baselibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片文件帮助类
 *
 * @author huangqj
 */
public class BitmapUtils {
    /**
     * 根据resourceId获取图片
     *
     * @param context
     * @param resId   resourceId
     * @return
     */
    public static Bitmap getBitmapFromId(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 化为二进制流
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /***
     * 根据drawableId获取图片，转成指定宽、高的图片
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap getBitmapFromId(Context context, int drawableId, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);

        return getBitmap(bitmap, screenWidth, screenHight);
    }

    /***
     * 将给定图片转成指定的宽、高的图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth, int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 根据图片路径获取图片
     *
     * @param filePath 图片所在的sdCard路径
     * @return Bitmap
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapFromFile(String filePath) throws FileNotFoundException {
        Bitmap bm = null;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            bm = BitmapFactory.decodeFile(filePath);
        } else {
            throw new FileNotFoundException("图片文件不存在!");
        }

        return bm;
    }

    /**
     * 将输入流转化成图片，给按指定的宽、高返回图片
     *
     * @param stream      图片的输入流
     * @param screenWidth 指定图片的宽度
     * @param screenHight 指定图片的
     * @return
     */
    public static Bitmap getBitmapFromIO(InputStream stream, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);

        return getBitmap(bitmap, screenWidth, screenHight);
    }

    /**
     * 字节数组转换成Bitmap
     *
     * @param temp byte[]
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] temp) {
        Bitmap bmp = null;
        if (temp != null) {
            bmp = BitmapFactory.decodeByteArray(temp, 0, temp.length);
        }

        return bmp;
    }

    /**
     * 图片转化成字节数据
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 图片转成输入流
     *
     * @param bitmap
     * @return
     */
    public static InputStream getBitmapStream(Bitmap bitmap) {
        byte[] outs = getBitmapByte(bitmap);
        return new ByteArrayInputStream(outs);
    }

    /**
     * 保存图片到本地路径
     *
     * @param savePath   保存文件的目录
     * @param bitmapName 图片名
     * @param bitmap     要保存的图片
     */
    public static void saveBitMapToSDCard(String savePath, String bitmapName, Bitmap bitmap) throws IOException {
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File mycaptureFile = new File(FileUtil.concatFilePath(savePath, bitmapName));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mycaptureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 字节转FileInputStream
     *
     * @param bytes
     * @return
     */
    public FileInputStream byteToFileInputStream(byte[] bytes, String fileName) {
        File file = new File(fileName);
        FileInputStream fileInputStream = null;
        try {
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            fileInputStream = new FileInputStream(file);
            file.deleteOnExit();
            return fileInputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    /**
     * 压缩图片并保存
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath 存储文件的路径
     * @param maxSize 指定不能超过多少kb target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        int leng = os.toByteArray().length / 1024;
        Log.e("os.toByteArray():", leng + "");
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        BufferedOutputStream buf = new BufferedOutputStream(fos);
        buf.write(os.toByteArray());
        buf.flush();
        // fos.write(os.toByteArray());
        // fos.flush();
        fos.close();
        buf.close();
        Log.e("compressAndGenImage：", "压缩完毕");
    }

}
