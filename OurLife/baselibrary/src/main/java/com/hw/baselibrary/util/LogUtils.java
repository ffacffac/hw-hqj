package com.hw.baselibrary.util;

import android.util.Log;

/**
 * 日志输出
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class LogUtils {
    /**
     * debug状态下，输出日志
     */
    public static final boolean DEBUG = true;

    /**
     * 输出error日志
     *
     * @param tag        tag
     * @param methodName 方法名
     * @param e          异常
     * @param msg        msg内容
     */
    public synchronized static void e(final String tag, String methodName, Throwable e, String msg) {
        if (!DEBUG) {
            return;
        }
        if (e != null) {
            Log.e(tag, getMsg(methodName, msg), e);
        } else {
            Log.e(tag, getMsg(methodName, msg));
        }
    }

    public static void ex(final String tag, String methodName, Throwable e) {
        if (!DEBUG) {
            return;
        }
        e(tag, methodName, e, null);
    }

    public static void e(final String tag, String methodName, String msg) {
        if (!DEBUG) {
            return;
        }
        e(tag, methodName, null, msg);
    }

    public synchronized static void d(final String tag, String methodName, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.d(tag, getMsg(methodName, msg));
    }

    public synchronized static void i(final String tag, String methodName, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.i(tag, getMsg(methodName, msg));
    }

    private static String getMsg(String methodName, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append(": ");
        if (msg != null) {
            sb.append(msg);
        }
        return sb.toString();
    }
}
