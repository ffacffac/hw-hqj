package com.hw.encryptaesdemo.hwencrypt;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密帮助类
 */
public class EncryptHelper {
	// region 私有变量
	private static String m_Seed = "1mleyygc2";
	private static final String m_HEX = "0123456789ABCDEF";

	// endRegion

	// region 公开方法

	/**
	 * 返回密文
	 *
	 * @param cleartext 明文
	 * @return 返回密文
	 * @throws Exception
	 */
	public static String encrypt(String cleartext) throws Exception {
		byte[] rawKey = getRawKey(m_Seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());

		return toHex(result);
	}

	/**
	 * 返回密文
	 *
	 * @param encrypted 密文
	 * @return 返回明文
	 * @throws Exception
	 */
	public static String decrypt(String encrypted) throws Exception {
		byte[] rawKey = getRawKey(m_Seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);

		return new String(result);
	}

	/**
	 * 执行MD5加密
	 *
	 * @param string 需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		return toHex(hash);
	}

	// endRegion

	// region 私有方法
	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);

		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);

		return decrypted;
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}

		return result;
	}

	private static String toHex(byte[] buf) {
		if (buf == null) {
			return "";
		}

		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}

		return result.toString();
	}

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(m_HEX.charAt((b >> 4) & 0x0f)).append(m_HEX.charAt(b & 0x0f));
	}

	/**
	 * 加密算法
	 *
	 * @param source 密码
	 * @param key    小写的用户名
	 * @return
	 * @throws Exception
	 */
	public static String ComputeHash(String source, String key) throws Exception {
		if (source == null) {
			return "";
		}
		String str = "abcdefghjklmnopqrstuvwxyz";
		if (source.length() < 0x1a) {
			source = source + str.substring(source.length());
		}
		byte[] bytes = getUniocdeBytes(source);
		int length = bytes.length;
		if ((key == null) || (key.length() == 0)) {
			key = "Encrypthejinhua";
		}
		byte[] buffer2 = getUniocdeBytes(key);
		byte num2 = ToByte(buffer2.length);
		byte num3 = 2;
		byte index = 0;
		for (int i = 0; i < length; i++) {
			byte num5 = (byte) (buffer2[index] | num2);
			num5 = (byte) (num5 & num3);
			bytes[i] = (byte) (bytes[i] ^ num5);
			num3 = (byte) (num3 + 1);
			if (num3 > 0xfd) {
				num3 = 2;
			}
			index = (byte) (index + 1);
			if (index >= num2) {
				index = 0;
			}
		}
		return toBase64String(bytes);
	}

	/**
	 * base64加密
	 *
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String toBase64String(byte[] bytes) throws Exception {
		// Base64Encoder encode = new Base64Encoder();
		// 将byte[]转换为base64
		return Base64Encoder.encode(bytes);
	}

	/**
	 * 整型转换成字节
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte ToByte(int value) throws Exception {
		if ((value < 0) || (value > 0xff)) {
			throw new Exception("overflowException");
		}
		return (byte) value;
	}

	/**
	 * 获取unicode编码的字节
	 *
	 * @param str
	 * @return
	 */
	public static byte[] getUniocdeBytes(String str) {
		int size = 0;
		if (str != null && (size = str.length()) > 0) {
			byte[] buffer = new byte[size * 2];
			for (int i = 0; i < size; i++) {
				char chr = str.charAt(i);
				buffer[i * 2] = (byte) (chr & 0xff);
				buffer[i * 2 + 1] = (byte) ((chr >> 8));
			}
			return buffer;
		}
		return new byte[size];
	}

	/**
	 * base64编码密码
	 *
	 * @param string 编码的原字符串
	 */
	public static String base64Encode(String string) {
		if (string == null || string.isEmpty()) return string;
		StringBuilder sb = new StringBuilder();
		sb.append("h").append(string).append("w");
		byte[] bytes = Base64.encode(sb.toString().getBytes(), Base64.NO_WRAP);
		// if (bytes.length >= 2) {
		//     byte temp = bytes[0];
		//     bytes[0] = bytes[1];
		//     bytes[1] = temp;
		// }

		// String exchangeStr = new String(bytes);
		// byte[] decode = Base64.decode(exchangeStr, Base64.NO_WRAP);
		// if (decode.length >= 2) {
		//     byte temp = decode[0];
		//     decode[0] = decode[1];
		//     decode[1] = temp;
		// }
		// String exchangeStr1 = new String(decode);
		// String ex = exchangeStr1;

		// String s16 = bytesToHexString(bytes);
		// byte[] bytes16 = s16.getBytes();
		// String strC16 = new String(bytes16);
		// byte[] decode = Base64.decode(strC16, Base64.NO_WRAP);
		// String strDecode = new String(decode);

		// byte[] decode = Base64.decode(newStr, Base64.NO_WRAP);
		// String strDecode = new String(decode);
		// try {
		//     String toBase = toBase64String(sb.toString().getBytes());
		//     byte[] s = Base64.decode(toBase, Base64.NO_WRAP);
		//     String s1 = new String(s);
		//     String newP = s1;
		// } catch (Exception e) {
		//     e.printStackTrace();
		// }
		return new String(bytes);
	}

	/**
	 * base64解码密码
	 *
	 * @param string 被编码的密码
	 */
	public static String base64Decode(String string) {
		if (string == null || string.isEmpty()) return string;
		byte[] decode = Base64.decode(string, Base64.NO_WRAP);
		String str = new String(decode);
		if (str.startsWith("h") && str.endsWith("w")) {
			String s = str.substring(1, str.length() - 1);
			// return str.substring(1, str.length() - 1);
			return s;
		}
		return str;
	}
}
