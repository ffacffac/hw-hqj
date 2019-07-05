package com.hw.library;

import android.support.annotation.NonNull;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-16.
 */

public interface RequestPermission<T> {

    /**
     * 请求权限
     *
     * @param target      act
     * @param permissions 权限组
     */
    void requestPermission(T target, String[] permissions);

    /**
     * 授权结果返回
     *
     * @param target
     * @param requestCode
     * @param grantResults
     */
    void onRequestPermissionResult(T target, int requestCode, @NonNull int[] grantResults);
}
