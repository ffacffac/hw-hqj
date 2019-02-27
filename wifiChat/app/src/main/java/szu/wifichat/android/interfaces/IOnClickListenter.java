package szu.wifichat.android.interfaces;

import szu.wifichat.android.groundbeen.Device;

/**
 * Created by huangqj on 2017-06-13.
 */

public interface IOnClickListenter {
    void onMyClick(int btnItem, int position, Device device);

    void onDismiss(int btnItem, int position, Device device);
}
