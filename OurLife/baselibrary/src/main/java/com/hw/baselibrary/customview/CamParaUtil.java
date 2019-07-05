package com.hw.baselibrary.customview;

import android.hardware.Camera.Size;
import android.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * @author huangqj
 */
public class CamParaUtil {

    /**
     * 筛选支持的尺寸
     *
     * @param list        尺寸列表
     * @param th          th
     * @param defaultSize 默认尺寸
     * @return 合适的尺寸
     */
    public static Size getSize(List<Size> list, int th, Size defaultSize) {
        if (null == list || list.isEmpty()) {
            return defaultSize;
        }
        Collections.sort(list, (lhs, rhs) -> {
            //作升序排序
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        });
        Log.e("支持的分辨率类型list==>", "" + list.size());
        int i = 0;
        for (Size s : list) {
            Log.e("Size.width==>", s.width + "");
            Log.e("Size.height==>", s.height + "");
            //&& equalRate(s, rate)
            if ((s.width > th)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            return list.get(i - 1);
        } else {
            return list.get(i);
        }
    }

    public static boolean isSupportedFocusMode(List<String> focusList, String focusMode) {
        for (int i = 0; i < focusList.size(); i++) {
            if (focusMode.equals(focusList.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportedFormats(List<Integer> supportedFormats, int jpeg) {
        for (int i = 0; i < supportedFormats.size(); i++) {
            if (jpeg == supportedFormats.get(i)) {
                return true;
            }
        }
        return false;
    }
}
