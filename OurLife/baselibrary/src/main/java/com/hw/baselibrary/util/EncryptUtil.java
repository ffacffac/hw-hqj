package com.hw.baselibrary.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密帮助类
 *
 * @author huangqj
 */
public class EncryptUtil {

    // region 私有变量
    // private static String m_Seed = "1mleyygc2";
    // private static final String m_HEX = "0123456789ABCDEF";

    /**
     * 16位秘钥
     */
    private static byte[] mKey = new byte[]{(byte) 0XA6, 0X7D, (byte) 0XE1, 0X3F, 0X35, 0X0E, (byte) 0XE1, (byte) 0XA9,
            (byte) 0X83, (byte) 0XA5, 0X62, (byte) 0XAA, 0X7A, (byte) 0XAE, 0X79, (byte) 0X98};
    private static byte[] mIV = {(byte) 0XF8, (byte) 0X8B, 0X01, (byte) 0XFB, 0X08, (byte) 0X85, (byte) 0X9A,
            (byte) 0XA4, (byte) 0XBE, 0X45, 0X28, 0X56, 0X03, 0X42, (byte) 0XF6, 0X19};

    // endRegion

    // region 公开方法

    // /**
    //  * 返回密文
    //  *
    //  * @param cleartext 明文
    //  * @return 返回密文
    //  * @throws Exception
    //  */
    // public static String encrypt(String cleartext) throws Exception {
    //     byte[] rawKey = getRawKey(m_Seed.getBytes());
    //     byte[] result = encrypt(rawKey, cleartext.getBytes());
    //
    //     return toHex(result);
    // }
    //
    // /**
    //  * 返回密文
    //  *
    //  * @param encrypted 密文
    //  * @return 返回明文
    //  * @throws Exception
    //  */
    // public static String decrypt(String encrypted) throws Exception {
    //     byte[] rawKey = getRawKey(m_Seed.getBytes());
    //     byte[] enc = toByte(encrypted);
    //     byte[] result = decrypt(rawKey, enc);
    //
    //     return new String(result);
    // }
    //
    // /**
    //  * 执行MD5加密
    //  *
    //  * @param string 需要加密的字符串
    //  * @return 加密后的字符串
    //  */
    // public static String md5(String string) {
    //     byte[] hash;
    //     try {
    //         hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
    //     } catch (NoSuchAlgorithmException e) {
    //         throw new RuntimeException("Huh, MD5 should be supported?", e);
    //     } catch (UnsupportedEncodingException e) {
    //         throw new RuntimeException("Huh, UTF-8 should be supported?", e);
    //     }
    //
    //     return toHex(hash);
    // }
    //
    // // endRegion
    //
    // // region 私有方法
    // private static byte[] getRawKey(byte[] seed) throws Exception {
    //     KeyGenerator kgen = KeyGenerator.getInstance("AES");
    //     SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    //     sr.setSeed(seed);
    //     kgen.init(128, sr); // 192 and 256 bits may not be available
    //     SecretKey skey = kgen.generateKey();
    //     byte[] raw = skey.getEncoded();
    //
    //     return raw;
    // }
    //
    // private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    //     SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    //     Cipher cipher = Cipher.getInstance("AES");
    //     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    //     byte[] encrypted = cipher.doFinal(clear);
    //
    //     return encrypted;
    // }
    //
    // private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    //     SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    //     Cipher cipher = Cipher.getInstance("AES");
    //     cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    //     byte[] decrypted = cipher.doFinal(encrypted);
    //
    //     return decrypted;
    // }
    //
    // private static byte[] toByte(String hexString) {
    //     int len = hexString.length() / 2;
    //     byte[] result = new byte[len];
    //     for (int i = 0; i < len; i++) {
    //         result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
    //     }
    //
    //     return result;
    // }
    //
    // private static String toHex(byte[] buf) {
    //     if (buf == null) {
    //         return "";
    //     }
    //
    //     StringBuffer result = new StringBuffer(2 * buf.length);
    //     for (int i = 0; i < buf.length; i++) {
    //         appendHex(result, buf[i]);
    //     }
    //
    //     return result.toString();
    // }
    //
    // private static void appendHex(StringBuffer sb, byte b) {
    //     sb.append(m_HEX.charAt((b >> 4) & 0x0f)).append(m_HEX.charAt(b & 0x0f));
    // }
    //
    // /**
    //  * 加密算法
    //  *
    //  * @param source 密码
    //  * @param key    小写的用户名
    //  * @return
    //  * @throws Exception
    //  */
    // public static String ComputeHash(String source, String key) throws Exception {
    //     if (source == null) {
    //         return "";
    //     }
    //     String str = "abcdefghjklmnopqrstuvwxyz";
    //     if (source.length() < 0x1a) {
    //         source = source + str.substring(source.length());
    //     }
    //     byte[] bytes = getUniocdeBytes(source);
    //     int length = bytes.length;
    //     if ((key == null) || (key.length() == 0)) {
    //         key = "Encrypthejinhua";
    //     }
    //     byte[] buffer2 = getUniocdeBytes(key);
    //     byte num2 = ToByte(buffer2.length);
    //     byte num3 = 2;
    //     byte index = 0;
    //     for (int i = 0; i < length; i++) {
    //         byte num5 = (byte) (buffer2[index] | num2);
    //         num5 = (byte) (num5 & num3);
    //         bytes[i] = (byte) (bytes[i] ^ num5);
    //         num3 = (byte) (num3 + 1);
    //         if (num3 > 0xfd) {
    //             num3 = 2;
    //         }
    //         index = (byte) (index + 1);
    //         if (index >= num2) {
    //             index = 0;
    //         }
    //     }
    //     return toBase64String(bytes);
    // }
    //
    // /**
    //  * base64加密
    //  *
    //  * @param bytes
    //  * @return
    //  * @throws Exception
    //  */
    // public static String toBase64String(byte[] bytes) throws Exception {
    //     // Base64Encoder encode = new Base64Encoder();
    //     // 将byte[]转换为base64
    //     return Base64Encoder.encode(bytes);
    // }
    //
    // /**
    //  * 整型转换成字节
    //  *
    //  * @param value
    //  * @return
    //  * @throws Exception
    //  */
    // public static byte ToByte(int value) throws Exception {
    //     if ((value < 0) || (value > 0xff)) {
    //         throw new Exception("overflowException");
    //     }
    //     return (byte) value;
    // }
    //
    // /**
    //  * 获取unicode编码的字节
    //  *
    //  * @param str
    //  * @return
    //  */
    // private static byte[] getUniocdeBytes(String str) {
    //     int size = 0;
    //     if (str != null && (size = str.length()) > 0) {
    //         byte[] buffer = new byte[size * 2];
    //         for (int i = 0; i < size; i++) {
    //             char chr = str.charAt(i);
    //             buffer[i * 2] = (byte) (chr & 0xff);
    //             buffer[i * 2 + 1] = (byte) ((chr >> 8));
    //         }
    //         return buffer;
    //     }
    //     return new byte[size];
    // }

    /**
     * 加密
     *
     * @param sSrc 加密的内容
     * @return 加密后的密文
     */
    public static String encrypt(String sSrc) throws Exception {
        if (sSrc == null || sSrc.isEmpty()) {
            throw new Exception("加密内容不能为空");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(mKey, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(mIV);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }


    /**
     * 解密
     *
     * @param sSrc 需要解密的密文
     */
    public static String decrypt(String sSrc) throws Exception {
        if (sSrc == null || sSrc.isEmpty()) {
            throw new Exception("解密内容不能为空");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(mKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(mIV);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        //先用base64解密
        byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }
}
