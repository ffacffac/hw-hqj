package com.hw.baselibrary.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式帮助类
 *
 * @author huangqj
 */
public class DateUtils {
    /**
     * 日期格式 yyyyMMdd
     */
    public static final String FORMATE_STRING_1 = "yyyyMMdd";
    /**
     * 日期格式 yyyy-MM-dd H:m:s
     */
    public static final String FORMATE_STRING_2 = "yyyy-MM-dd H:m:s";
    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMATE_STRING_3 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式 yyyyMMddHHmmss
     */
    public static final String FORMATE_STRING_4 = "yyyyMMddHHmmss";
    /**
     * 日期格式yyyy-MM-dd
     */
    public static final String FORMATE_STRING_5 = "yyyy-MM-dd";

    public static final String FORMATE_STRING_6 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format
     * @return
     */
    public static String formatNowDate(SimpleDateFormat format) {
        return format.format(new Date());
    }

    /**
     * 获取当前系统指定格式的日期
     *
     * @param format //日期格式，如yyyy-MM-dd
     * @return
     */
    public static String getNowDate(String format) {
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        return formatter.format(new Date());
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format
     * @return
     */
    public static String formatNowDate(String format) {
        return formatNowDate(new SimpleDateFormat(format, Locale.getDefault()));
    }

    /**
     * 获取当前日期的yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @param format
     * @return
     */
    public static String formatNowDate() {
        return formatNowDate(FORMATE_STRING_3);
    }

    /**
     * 将日期转成指定格式的字符串
     *
     * @param format
     * @param date
     * @return
     */
    public static String formatDate(SimpleDateFormat format, Date date) {
        return format.format(date);
    }

    /**
     * 将日期转成指定格式的字符串
     *
     * @param format
     * @param date
     * @return
     */
    public static String formatDate(String format, Date date) {
        return formatDate(new SimpleDateFormat(format, Locale.getDefault()), date);
    }

    /**
     * 将日期转成yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(FORMATE_STRING_3, date);
    }

    /**
     * 将指定格式的日期字符串转成日期
     *
     * @param format     日期格式化对象
     * @param dateString 日期字符串
     * @return 日期对象
     * @throws Exception
     */
    public static Date formatString(SimpleDateFormat format, String dateString) throws Exception {
        Date date = null;
        try {
            if (TextUtils.isEmpty(dateString)) {
                date = getNow();
            } else {
                date = format.parse(dateString);
            }
        } catch (ParseException e) {
            throw new Exception("日期：" + dateString + "不是\"" + format.toPattern() + "\"格式", e);
        }
        return date;
    }

    /**
     * 将指定格式的日期字符串转成日期
     *
     * @param format     日期格式化对象
     * @param dateString 日期字符串
     * @return 日期对象
     * @throws Exception
     */
    public static Date formatString(String format, String dateString) throws Exception {
        return formatString(new SimpleDateFormat(format, Locale.getDefault()), dateString);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转日期
     *
     * @param str 目标字符串
     * @return
     * @throws Exception
     */
    public static Date formatString(String str) throws Exception {
        return formatString(FORMATE_STRING_3, str);
    }

    /**
     * 获取date1和date2的小时数之差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static double getHourDeference(Date date1, Date date2) {
        // 每小时的许毫秒数为：60 * 60 *1000 = 3600000
        Long milliSeconde = date1.getTime() - date2.getTime();
        BigDecimal hour = new BigDecimal(1.0 * milliSeconde / 3600000);
        hour = hour.setScale(4, RoundingMode.HALF_UP);
        return hour.doubleValue();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param format 指定的日期格式
     * @param date   时间
     * @return
     */
    public static String getLongStringDate(String format, Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 只获取年月日
     * 格式为：2018-05-31 12:00:11
     */
    public static String getYearMonthDay(String txtDate) {
        if (txtDate == null) {
            return "";
        }
        if (txtDate.length() >= 10) {
            txtDate = txtDate.substring(0, 10);
        }
        return txtDate;
    }

    /**
     * 只获取年月日
     */
    public static String getTimeToSecond(String txtDate) {
        if (txtDate == null) {
            return "";
        }
        if (txtDate.length() >= 19) {
            txtDate = txtDate.substring(0, 19);
        }
        return txtDate;
    }
}
