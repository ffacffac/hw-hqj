package com.hw.encryptaesdemo;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * ASE加密界面帮助类
 * Created by huangqj on 2018-12-04.
 */

public class EncryptUtils {

    // 32位秘钥（跟服务端保持一致）
    // private static byte[] mKey = {(byte) 0XA6, 0X7D, (byte) 0XE1, 0X3F, 0X35, 0X0E, (byte) 0XE1, (byte) 0XA9,
    //         (byte) 0X83, (byte) 0XA5, 0X62, (byte) 0XAA, 0X7A, (byte) 0XAE, 0X79, (byte) 0X98, (byte) 0XA7, 0X33, 0X49,
    //         (byte) 0XFF, (byte) 0XE6, (byte) 0XAE, (byte) 0XBF, (byte) 0X8D, (byte) 0X8D, 0X20, (byte) 0X8A, 0X49, 0X31,
    //         0X3A, 0X12, 0X40};
    // 16位秘钥
    private static byte[] mKey = new byte[]{(byte) 0XA6, 0X7D, (byte) 0XE1, 0X3F, 0X35, 0X0E, (byte) 0XE1, (byte) 0XA9,
            (byte) 0X83, (byte) 0XA5, 0X62, (byte) 0XAA, 0X7A, (byte) 0XAE, 0X79, (byte) 0X98};
    private static byte[] mIV = {(byte) 0XF8, (byte) 0X8B, 0X01, (byte) 0XFB, 0X08, (byte) 0X85, (byte) 0X9A,
            (byte) 0XA4, (byte) 0XBE, 0X45, 0X28, 0X56, 0X03, 0X42, (byte) 0XF6, 0X19};

    /**
     * 加密
     *
     * @param sSrc 加密的内容
     * @return 加密后的密文
     */
    public static String Encrypt(String sSrc) throws Exception {
        // if (sKey == null) {
        //     System.out.print("Key为空null");
        //     return null;
        // }
        // // 判断Key是否为16位
        // if (sKey.length() != 16) {
        //     System.out.print("Key长度不是16位");
        //     return null;
        // }
        // byte[] raw = sKey.getBytes();
        // SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        SecretKeySpec skeySpec = new SecretKeySpec(mKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        // IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(mIV);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    /**
     * 解密
     *
     * @param sSrc 需要解密的密文
     */
    public static String Decrypt(String sSrc) throws Exception {
        try {
            // // 判断Key是否正确
            // if (sKey == null) {
            //     System.out.print("Key为空null");
            //     return null;
            // }
            // // 判断Key是否为16位
            // if (sKey.length() != 16) {
            //     System.out.print("Key长度不是16位");
            //     return null;
            // }
            // byte[] raw = sKey.getBytes("ASCII");
            // SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            SecretKeySpec skeySpec = new SecretKeySpec(mKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            IvParameterSpec iv = new IvParameterSpec(mIV);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original);
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


    //     public static void main(String[] args) throws Exception {
    //         /*
    //          * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
    //          * 此处使用AES-128-CBC加密模式，key需要为16位     +  base64加密。
    //          */
    //         String cKey = "123456789";
    //         StringBuffer markIds = new StringBuffer();
    //         markIds.append(1).append(",");
    //         String replace = markIds.substring(0, markIds.length() - 1).replace(",", "','");
    //
    //         String time = "1490840793103";
    //         String sysName = "haha";
    //         String orgCode = "13456789";
    //
    //         //--自定义加密规则
    //
    //         String keyString = sysName + time + orgCode;
    //         keyString = keyString.substring(keyString.length() - 6, keyString.length()) + keyString
    //                 .substring(0, keyString.length() - 6);
    //
    //         // 需要加密的字串
    //         String cSrc = keyString;
    //         System.out.println(cSrc);
    //         // 加密
    //         long lStart = System.currentTimeMillis();
    //         System.out.println(System.currentTimeMillis());
    //         String enString = EncryptUtils.Encrypt(cSrc);
    //         System.out.println("加密后的字串是：" + enString);
    //
    //         long lUseTime = System.currentTimeMillis() - lStart;
    //         System.out.println("加密耗时：" + lUseTime + "毫秒");
    //         // 解密
    //         lStart = System.currentTimeMillis();
    //         String DeString = EncryptUtils.Decrypt(enString);
    //         System.out.println("解密后的字串是：" + DeString);
    //         lUseTime = System.currentTimeMillis() - lStart;
    //         System.out.println("解密耗时：" + lUseTime + "毫秒");
    //
    //         System.out.println(System.currentTimeMillis());
    //     }
}
