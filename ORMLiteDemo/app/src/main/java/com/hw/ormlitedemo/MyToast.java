package com.hw.ormlitedemo;

import android.widget.Toast;

/**
 * Created by huangqj on 2019-07-26.
 */

public class MyToast {

    private static Toast toast;

    private MyToast() {
    }

    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
