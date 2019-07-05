package com.hw.library;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by huangqj on 2019-04-16.
 */

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getName();

    private PermissionUtils() {

    }

    /**
     * 检查所有权限是否都已经被赋予
     *
     * @param grantResults
     * @return
     */
    public static boolean verifyParmissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限组
     *
     * @param permissions
     * @return
     */
    public static boolean hasSelfParmissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!hasSelfParmission(context, permission)) {
                return false;
            }
        }
        return false;
    }

    /**
     * 用户申请权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasSelfParmission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        try {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException x) {
            Log.e(TAG, "hasSelfParmission: ", x);
            return false;
        }
    }

    /**
     * 检查被拒绝的权限组中，是否有点击了"不在询问"的权限
     * <p>
     *     第一次打开APP时，false
     *     上一次弹出了权限申请，选择拒绝，没有勾选“不在询问” true
     *     上一次弹出了权限申请，选择拒绝，并且勾选“不在询问” false
     *     点击了拒绝，但没有勾选“不在询问”，返回true，如果勾选“不在询问” 则返回false
     * </p>
     * @param activity act
     * @param permissions 被拒绝的权限组
     * @return
     */
    public static boolean shouldShowRequesrPermissionRatianals(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }
}
