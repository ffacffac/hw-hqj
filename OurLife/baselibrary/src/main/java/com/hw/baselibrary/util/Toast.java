package com.hw.baselibrary.util;

import android.app.Application;
import android.view.Gravity;

/**
 * toast
 *
 * @author huangqj
 *         Created by huangqj on 2018-02-01.
 */

public class Toast {

    private static Toast toastUtils;
    private Application context;
    private android.widget.Toast toast;

    private Toast(Application context) {
        this.context = context;
    }

    public static Toast get(Application context) {
        if (null == toastUtils) {
            synchronized (Toast.class) {
                toastUtils = new Toast(context);
            }
        }
        return toastUtils;
    }


    // /**
    //  * 显示长提示信息
    //  *
    //  * @param msg 要显示的信息
    //  */
    // public void showToastLong(String msg) {
    //     Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
    //     toast.show();
    // }
    //
    // /**
    //  * 显示短提示信息
    //  *
    //  * @param msg 要显示的信息
    //  */
    // public void showToastShort(String msg) {
    //     Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    //     toast.show();
    // }
    //
    // /**
    //  * 显示提示信息，1500显示一次
    //  *
    //  * @param msg 要显示的信息
    //  */
    // public void showToast1500(String msg) {
    //     showToastOnTime(msg, 1500);
    // }
    //
    // /**
    //  * 显示提示信息，1秒显示一次
    //  *
    //  * @param msg 要显示的信息
    //  */
    // public void showToast1000(String msg) {
    //     showToastOnTime(msg, 1000);
    // }
    //
    // /**
    //  * 显示提示信息，根据设定时间显示
    //  *
    //  * @param msg 要显示的信息
    //  */
    // public void showToastOnTime(String msg, long time) {
    //     if (!Utils.isFastClickByTime(time)) {
    //         Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    //         toast.show();
    //     }
    // }

    /**
     * 显示短提示信息
     *
     * @param msg 要显示的信息
     */
    public void showToastCenter(String msg) {
        if (toast == null) {
            toast = android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
