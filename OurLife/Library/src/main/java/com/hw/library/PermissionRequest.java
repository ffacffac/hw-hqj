package com.hw.library;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-16.
 */

public interface PermissionRequest {
    /**
     * 继续请求接口，用户拒绝一次后，给出dialog提示
     */
    void proceed();
}
