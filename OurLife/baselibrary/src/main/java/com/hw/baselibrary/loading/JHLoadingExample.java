package com.hw.baselibrary.loading;

import android.content.Context;

/**
 * @author huangqj
 */
public class JHLoadingExample {

    public void show(Context context) {
        //一，进度
        ProgressLoadDialog loadDialog = new ProgressLoadDialog(context);
        loadDialog.setLabel("正在加载，请稍后...");
        //设置进度最大值
        loadDialog.setMaxProgress(100);
        //设置进度百分比
        // loadDialog.setProgress(50);
        loadDialog.show();
        //dismiss
        // loadDialog.dismiss();

        /*二，加载*/
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setLabel("正在加载。。。");
        progressDialog.show();
        //dismiss
        // progressDialog.dismiss();
    }
}
