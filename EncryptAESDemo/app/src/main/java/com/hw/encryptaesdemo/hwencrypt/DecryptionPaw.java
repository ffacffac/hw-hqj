package com.hw.encryptaesdemo.hwencrypt;

import android.util.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecryptionPaw {
    // private static String cPaw = "1234";

    // private static String cKey = "古宁";

    // public static void main(String[] args) {
    // String abcd = "abcd";
    // byte[] b = getUniocdeBytes(abcd);
    // String strB = new String(b);
    // System.out.println("abcd==" + abcd);
    // System.out.println(strB);
    // System.out.println(strB.length());
    // System.out.println(strB.replace("", "1"));
    // char[] c = strB.toCharArray();
    // StringBuilder sb = new StringBuilder();
    // for (int i = 0; i < c.length; i++)
    // {
    // // if (!Character.isSpaceChar(c[i]))
    // if (!Character.isSpace(c[i]))
    // {
    // sb.append(c[i]);
    // }
    // }
    // System.out.println(sb.toString());

    //     try {
    //         endPaw();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // public static String computeHash(String paw) throws Exception {
    //     String newPaw = EncryptHelper.ComputeHash(paw, cKey.toLowerCase());
    //     System.out.println(newPaw);
    //     return newPaw;
    // }

    /**
     * 解密密码
     *
     * @param userName      用户名
     * @param encryptionPaw 加密后的密码
     */
    public static String decryptionPaw(String userName, String encryptionPaw) throws Exception {
        // byte[] bytes = Base64.getDecoder().decode(paw);//java 的base64
        byte[] bytes = Base64.decode(encryptionPaw, Base64.NO_WRAP);//android 的base64
        if (userName == null || userName.length() == 0) {
            userName = "Encrypthejinhua";
        }
        byte[] buffer2 = EncryptHelper.getUniocdeBytes(userName);
        byte num2 = EncryptHelper.ToByte(buffer2.length);
        byte num3 = 2;
        byte index = 0;
        for (int i = 0; i < bytes.length; i++) {
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
        String result = new String(bytes);
        // String strOrigina1 = result;
        // int length = 0;
        String str = "abcdefghjklmnopqrstuvwxyz";
        if (result.length() == 25) {
            byte[] byteKey = EncryptHelper.getUniocdeBytes(str);
            for (int j = bytes.length - 1; j > 0; j--) {
                if (bytes[j] != byteKey[j]) {
                    result = result.substring(0, (j + 2) / 2);
                    // length = j;
                    break;
                }
            }
        }
        // System.out.println(result);
        // char[] a_z = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
        // 'b', 'c', 'd', 'e',
        // 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        // 'u', 'v', 'w',
        // 'x', 'y', 'z' };
        String[] a_z = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        List<String> listA_Z = new ArrayList<>();
        listA_Z.addAll(Arrays.asList(a_z));
        // List<String> listA_Z = Arrays.asList(a_z);//直接用这种方式转换成list，在Iterator remove时会报错
        char[] cArr = result.toCharArray();
        StringBuilder sb = new StringBuilder();
        // long startTime = System.currentTimeMillis();
        // System.out.println("开始时间：" + startTime);
        //第一种
        for (char aCArr : cArr) {
            for (String anA_z : listA_Z) {
                if (String.valueOf(aCArr).toLowerCase().equals(anA_z.toLowerCase())) {
                    sb.append(aCArr);
                    // listA_Z.remove(anA_z);//这里不能remove ，因为有的密码可能有两个相同的字母，需要多次比较判断
                    break;
                }
            }
            //第二种
            // Iterator<String> iterator = listA_Z.iterator();
            // while (iterator.hasNext()) {
            //     String value = iterator.next();
            //     if (String.valueOf(aCArr).toLowerCase().equals(value.toLowerCase())) {
            //         sb.append(aCArr);
            //         // iterator.remove();//这里不能remove ，因为有的密码可以有两个相同的字母，需要多次比较判断
            //         break;
            //     }
            // }
        }
        // long endTime = System.currentTimeMillis();
        // System.out.println("结束时间：" + endTime);
        // System.out.println(("耗时时间：" + (endTime - startTime)));
        // System.out.println(sb.toString());
        return sb.toString();
    }
}
