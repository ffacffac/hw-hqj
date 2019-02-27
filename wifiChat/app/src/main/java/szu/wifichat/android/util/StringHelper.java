package szu.wifichat.android.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper
{
    public static String format(String format, Object... args)
    {
        return String.format(Locale.getDefault(), format, args);
    }

    /**
     * 字符串是否为NULL或者为空字符串
     *
     * @param str 目标字符串
     * @return 返回结果
     */
    public static boolean isNullOrEmpty(String str)
    {
        return isNullOrEmpty(str, false);
    }

    /**
     * 字符串是否为NULL或者为仅包含空白字符的字符串或者空字符串
     *
     * @param str 目标字符串
     * @return 返回结果
     */
    public static boolean isNullOrEmpty(String str, boolean afterTrim)
    {
        boolean flag = false;
        if (str == null || str.isEmpty())
        {
            flag = true;
        }
        else
        {
            if (afterTrim && str.trim().isEmpty())
            {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * 获取字符型，如果为null则转成空串
     *
     * @param str
     * @return
     */
    public static String getString(String str)
    {
        return (isNullOrEmpty(str)) ? "" : str;
    }

    /**
     * 字符串转short
     *
     * @param str
     * @return 转完后的short
     */
    public static short getShort(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? (short) 0 : Short.parseShort(str);
    }

    /**
     * 转成int型，默认值0
     *
     * @param str
     * @return
     */
    public static int getInt(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? 0 : Integer.parseInt(str);
    }

    /**
     * 转成long型，默认值0
     *
     * @param str
     * @return
     */
    public static long getLong(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? 0L : Long.parseLong(str);
    }

    /**
     * 转成double型，默认值0
     *
     * @param str
     * @return
     */
    public static double getDouble(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? 0D : Double.parseDouble(str);
    }

    /**
     * 转成float型，默认值0
     *
     * @param str 要转型的字符串
     * @return
     */
    public static float getFloat(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? 0F : Float.parseFloat(str);
    }

    /**
     * 返回指定精度的值
     *
     * @param value
     * @param scale
     * @return
     */
    public static float getFloat(float value, int scale)
    {
        BigDecimal b = new BigDecimal(value);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        return f1;
    }

    /**
     * 转成BigDecimal型，默认值0
     *
     * @param str
     * @return
     */
    public static BigDecimal getBigDecimal(String str)
    {
        return (StringHelper.isNullOrEmpty(str)) ? BigDecimal.ZERO : BigDecimal.valueOf(Double
                .parseDouble(str));
    }

    /**
     * 转成boolean型，默认值0
     *
     * @param str
     * @return
     */
    public static boolean getBoolean(String str)
    {
        return (StringHelper.isNullOrEmpty(str) || str.equalsIgnoreCase("false") || str.equals("0")) ? false
                : true;
    }

    /**
     * 字符串是否为"true"或者"1"
     *
     * @param str 目标字符串
     * @return
     */
    public static boolean isTrue(String str)
    {
        return str.equalsIgnoreCase(Boolean.toString(true)) || str.equalsIgnoreCase("1");
    }

    public static String getShortString(short value)
    {
        return ((Short) value).toString();
    }

    /**
     * 获取某一字符在字符串中的出现次数
     *
     * @param s
     * @param c
     * @return
     */
    public static int getCharCount(String s, char c)
    {
        int count = 0;
        for (int i = 0; i < s.length(); i++)
        {
            if (s.charAt(i) == c)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取某个子符在字符串中第N次出现的位置下标
     *
     * @param string
     * @param srcChar
     * @param times
     * @return
     */
    public static int getCharacterPosition(String string, String srcChar, int N)
    {
        Matcher slashMatcher = Pattern.compile(srcChar).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find())
        {
            mIdx++;
            if (mIdx == N)
            {
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * 判断字符串是不是数字
     *
     * @return
     */
    public static boolean isNumberByStr(String string)
    {
        if (string == null || string.isEmpty())
        {
            return false;
        }
        return string.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * double保留两位小数
     *
     * @param value
     * @return
     */
    public static String doubleFormayTow(double value)
    {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
    }

    /**
     * 去掉字符串中指定的某个字符
     *
     * @param str 要操作的字符串
     * @param charStr 要去掉的某个字符
     * @return
     */
    public static String replaceStr(String str, String charStr)
    {
        if (str == null || charStr == null)
        {
            return "";
        }
        String newStr = str.replace(charStr, "").trim();// 利用空字符去替换掉指定的字符
        return newStr;
    }

    /**
     * 判断两个字符串转换成int后的大小,不过要注意顺序，现在是判断str1是否大于str2
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareIntByString(String str1, String str2)
    {
        return getInt(str1) > getInt(str2);
    }

    /**
     * 判断权限配置
     *
     * @param a
     * @return
     */
    public static boolean getBooleanByCharConfig(char a)
    {
        return a == '1' ? true : false;
    }

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str)
    {
        if (str != null && !str.isEmpty())
        {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            return pattern.matcher(str).matches();
        }
        return false;
    }

    /*
     * 判断是否为浮点数，包括double和float
     * @param str 传入的字符串
     * @return 是浮点数返回true,否则返回false
     */
    public static boolean isDouble(String str)
    {
        if (str != null && !str.isEmpty())
        {
            return str.matches("[\\d]+\\.[\\d]+");
        }
        return false;
    }

    /**
     * 处理分割符 如：">=50;<=70" ，">=90" ,"<=100"
     *
     * @param value
     * @return
     */
    public static String getCompareValue(String value)
    {
        String result = "";
        if (value == null || value.isEmpty())
        {
            return result;
        }
        String strSplit = ";";// 分隔符号
        if (value.contains(">=") && !value.contains("<=") && !value.contains(strSplit))
        {
            result = value;
        }
        else if (value.contains("<=") && !value.contains(">=") && !value.contains(strSplit))
        {
            result = value;
        }
        else if (value.contains("<=") && value.contains(">=") && value.contains(strSplit))
        {
            String arr[] = value.split(strSplit);
            result = arr[0].replace(">=", "") + "~" + arr[1].replace("<=", "");
        }
        return result;
    }

    /**
     * 提供精确的加法运算，v1+v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算，v1-v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 转换公里标，原先公里标数据：7.9，将公里标转换成：K7+900这种样式
     *
     * @param km
     */
    public static String transitionKM(float km)
    {
        String transitionKM = String.valueOf(km);
        StringBuffer kmTemp = new StringBuffer();// 原先公里标数据：7.9，将公里标转换成：K7+900这种样式
        if (transitionKM.contains("."))
        {
            String kmArr[] = transitionKM.split("\\.");
            if (kmArr != null && kmArr.length > 0)
            {
                kmTemp.append("K").append(kmArr[0]);
                if (kmArr.length >= 2)
                {
                    kmTemp.append("+").append(kmArr[1]);
                }
            }
            return kmTemp.toString();
        }
        return kmTemp.append("k").append(transitionKM).toString();
    }
}
