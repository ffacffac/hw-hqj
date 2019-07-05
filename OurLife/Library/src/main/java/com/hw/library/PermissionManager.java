package com.hw.library;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-16.
 */

public class PermissionManager {

    /**
     * 请求权限
     */
    public static void request(Activity activity, String[] permissions) {
        String className = activity.getClass().getName() + "$Permissions";
        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.requestPermission(activity, permissions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 授权结果返回
     *
     * @param activity
     * @param requestCode
     * @param grantResults
     */
    public static void onResquestPermissionResult(Activity activity, int requestCode, @NonNull int[] grantResults) {
        String className = activity.getClass().getName() + "$Permissions";
        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.onRequestPermissionResult(activity, requestCode, grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
