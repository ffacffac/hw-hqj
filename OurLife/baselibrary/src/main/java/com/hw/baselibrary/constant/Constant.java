package com.hw.baselibrary.constant;

import android.Manifest;

import com.hw.baselibrary.util.SdCardUtil;

import java.io.File;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */
public class Constant {
    /**
     * 密码
     * ffac1020
     */
    public static final String PASSWORD = "ZO5iXlYS943sLempg5VlrQ==";

    public static final String FILE_PROVIDER = "com.hw.ourlife.fileprovider";

    public static final String APP_FILE_DIR_NAME = "ourLife";

    public static final String CONFIG_FILE_DIR_PATH =
            SdCardUtil.getSdCardPath() + File.separator + APP_FILE_DIR_NAME + File.separator + "config";

    public static final String CONFIG_FILE_NAME = "config.fig";
    public static final String CONFIG_FILE_PATH = CONFIG_FILE_DIR_PATH + File.separator + CONFIG_FILE_NAME;
    /**
     * db name
     */
    public static final String DB_NAME = "life.db";
    public static final String DB_DIR = SdCardUtil.getSdCardPath() + File.separator + APP_FILE_DIR_NAME;
    public static final String DB_PATH =
            SdCardUtil.getSdCardPath() + File.separator + APP_FILE_DIR_NAME + File.separator + DB_NAME;
    /**
     * 附件存放路径
     */
    public static final String MEDIA_FILE_DIR_NAME = "lifeFile";
    public static final String MEDIA_FILE_DIR =
            SdCardUtil.getSdCardPath() + File.separator + APP_FILE_DIR_NAME + File.separator + MEDIA_FILE_DIR_NAME;

    // /**
    //  * 应用申请权限
    //  */
    // public static final String[] REQUEST_PER = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
    //         Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
    //         Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH_ADMIN,
    //         Manifest.permission.BLUETOOTH};
    /**
     * 应用申请权限
     */
    public static final String[] REQUEST_PER = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
}
