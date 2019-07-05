package com.hw.baselibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-11.
 */

public class ImageEncryptUtils {

    /**
     * AES加密使用的秘钥，注意的是秘钥的长度必须是16位
     */
    private static final String AES_KEY = "myffacourlife";
    /**
     * 混入的字节
     */
    private static final String BYTE_KEY = "ffac_Byte";

    /**
     * 混入字节加密
     */
    public static void addImgByte(Bitmap bitmap, String outPath) throws IOException {
        ByteArrayOutputStream baos = null;
        FileOutputStream fops = null;
        try {
            //获取图片的字节流
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            fops = new FileOutputStream(outPath);
            //混入的字节流
            byte[] bytesAdd = BYTE_KEY.getBytes();
            fops.write(bytesAdd);
            fops.write(bytes);
            fops.flush();
        } finally {
            if (fops != null) {
                fops.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
    }

    /**
     * 移除混入的字节解密图片
     */
    public static Bitmap removeImgByte(String imagePath) throws IOException {
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
            stream = new FileInputStream(new File(imagePath));
            out = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            int i = 0;
            while ((n = stream.read(b)) != -1) {
                if (i == 0) {
                    //第一次写文件流的时候，移除我们之前混入的字节
                    out.write(b, BYTE_KEY.length(), n - BYTE_KEY.length());
                } else {
                    out.write(b, 0, n);
                }
                i++;
            }
            //获取字节流显示图片
            byte[] bytes = out.toByteArray();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 使用AES加密标准进行加密
     */
    public static void aesEncrypt(InputStream inputStream, String outPath) throws Exception {
        // FileInputStream fis = null;
        FileOutputStream fos = null;
        CipherOutputStream cos = null;
        try {
            // fis = new FileInputStream(filePath);
            fos = new FileOutputStream(outPath);
            //SecretKeySpec此类来根据一个字节数组构造一个 SecretKey
            SecretKeySpec sks = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            //Cipher类为加密和解密提供密码功能,获取实例
            Cipher cipher = Cipher.getInstance("AES");
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            //CipherOutputStream 为加密输出流
            cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[1024];
            // while ((b = fis.read(d)) != -1) {
            while ((b = inputStream.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
        } finally {
            if (cos != null) {
                cos.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 使用AES标准解密
     */
    public static Bitmap aesDecrypt(String outPath) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream out = null;
        CipherInputStream cis = null;
        try {
            fis = new FileInputStream(outPath);
            out = new ByteArrayOutputStream(1024);
            SecretKeySpec sks = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            //CipherInputStream 为加密输入流
            cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[1024];
            while ((b = cis.read(d)) != -1) {
                out.write(d, 0, b);
            }
            out.flush();
            //获取字节流显示图片
            byte[] bytes = out.toByteArray();
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } finally {
            if (out != null) {
                out.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (cis != null) {
                cis.close();
            }
        }
    }
}
