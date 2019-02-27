package szu.wifichat.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import szu.wifichat.android.BaseActivity;
import szu.wifichat.android.R;
import szu.wifichat.android.view.HeaderLayout;
import szu.wifichat.android.view.HeaderLayout.HeaderStyle;

public class AboutActivity extends BaseActivity {

    private HeaderLayout mHeaderLayout;
    private TextView tvAbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.aboutus_header);
        mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle("关于我们", null);
    }

    @Override
    protected void initEvents() {
    }

    @Override
    public void processMessage(Message msg) {
    }

    private MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler {

        //弱引用持有外部类引用
        WeakReference<Activity> weakReference;
        AboutActivity aboutActivity;

        MyHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
            aboutActivity = (AboutActivity) weakReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            aboutActivity.tvAbs.setText("ll");
        }
    }
}
