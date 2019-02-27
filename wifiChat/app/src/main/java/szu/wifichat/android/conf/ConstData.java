package szu.wifichat.android.conf;

/**
 * Created by huangqj on 2017-06-19.
 */

public class ConstData {
    public static int[] mStepID = {0, 1, 2, 3, 4};//步骤ID标识
    //杆号名称
    //    public String[] mDeviceName = {"银盏坳站24#", "银盏坳站30#", "银盏坳站34#", "银盏坳站80#"};
    // public static String[] mDeviceName = {"L012", "L013", "L014", "L015"};
    public static String[] mDeviceName = {"I5", "I6", "I27", "I128"};
    //公里标
    // public static String[] KM = {"k2210+638", "k2210+763", "k2210+818", "k2211+944"};
    public static String[] KM = {"K0+50", "K0+50", "K0+150", "K0+151"};
    //接挂位置
    // public static String[] mGroundPosition = {"定位器、加强线", "渡线", "渡线", "渡线"};
    public static String[] mGroundPosition = {"", "", "定位器、加强线", "渡线"};
    //标签
    //    public static String[] mDeviceLabel = {"E20041026+51201810850C23D", "E20041026512014626300DAB",
    //            "E2003072020102661060AE73", "E20041026512003721803621"};
    // /**
    //  * PDA扫到的标签
    //  */
    //     public static String[] mDeviceLabel = {"E2005104091001850260F5B9", "E2005104091001850390ECEB",
    //             "E2005104091001850290F236", "E2005104091001850220F7B9"};
    /**
     * PDA扫到的标签
     */
    public static String[] mDeviceLabel = {"300833B2DDD9014000000000", "E2005104091001850260F5B9",
            "E2005104091001850390ECEB", "E2005104091001850290F236"};
    /**
     * 标签
     */
    // public static String[] mDeviceLabel = {"005104091001850220f7b9fa", "005104091001850290f2369b",
    //         "005104091001850390ecebd7", "005104091001850260f5b981"};

    //北京天气
    public static String beiJing = "http://www.weather.com.cn/data/cityinfo/101010100.html";


    public static final String GROUPS = "欧汉明、张膺基";
    public static final String LEADER = "李俊龙";
}
