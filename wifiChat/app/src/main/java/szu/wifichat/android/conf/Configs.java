package szu.wifichat.android.conf;

import szu.wifichat.android.util.SdCardHelper;

/**
 * Created by huangqj on 2017-06-13.
 */

public class Configs {

    public static final String KEY_MAC_BOOK_IP_NAME = "macBookIpName";
    public static final String KEY_IPAD_IP_NAME = "ipadIpName";

    public static String photoPath = SdCardHelper.getSdCardPath() + "/ground/photos/"; // 图片文件存放目录

    public static String BASE_IP = "192.168.43.131:8096";
    public static String BASE_URL = "http://192.168.43.131:8096";
    //上传杆号数据  http://192.168.0.77:8093/ground/UploadInfo/{CODE}/{GROUNDNUM}
    public static String GROUND_UPLOAD = BASE_URL + "/Server/ground/UploadInfo/";
    //获取杆号状态  http://192.168.0.77:8093/ground/GetData 处的服务
    public static String GET_GROUND_STATE = BASE_URL + "/Server/ground/GetData";

}
