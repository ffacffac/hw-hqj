package com.hw.baselibrary.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * @author huangqj
 */

public class CommonUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Show message
     *
     * @param activity Activity
     * @param msg      message
     */
    public static void showSnackMessage(Activity activity, String msg) {
        // Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        // View view = snackbar.getView();
        // ((TextView) view.findViewById(R.id.snackbar_text))
        //         .setTextColor(ContextCompat.getColor(activity, R.color.white));
        // snackbar.show();
    }

    /**
     * 判断2个对象是否相等
     *
     * @param a Object a
     * @param b Object b
     * @return isEqual
     */
    public static boolean isEquals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                                                                               .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取随机rgb颜色值
     */
    public static int randomColor() {
        Random random = new Random();
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        int red = random.nextInt(150);
        //0-190
        int green = random.nextInt(150);
        //0-190
        int blue = random.nextInt(150);
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue);
    }

    // public static int randomTagColor() {
    //     int randomNum = new Random().nextInt();
    //     int position = randomNum % Constants.TAB_COLORS.length;
    //     if (position < 0) {
    //         position = -position;
    //     }
    //     return Constants.TAB_COLORS[position];
    // }

    /**
     * 泛型转换工具方法 eg:object ==> map<String, String>
     *
     * @param object Object
     * @param <T>    转换得到的泛型对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

    public static final Long EXIT_TIME = 2000L;
    private static Long exitTime = 0L;

    /**
     * 再按一次退出程序
     */
    public static boolean onFinishByTime(Toast toast) {
        if ((System.currentTimeMillis() - exitTime) > EXIT_TIME) {
            toast.showToastCenter("再按一次退出程序");
            exitTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }
}
