package com.hw.baselibrary.loading;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author huangqj
 */
public class ProgressDialog extends BaseProgressDialog {
    private static final float ANIMATE_SPEED = 1;
    private SpinView mIndeterminateView;

    @Override
    protected View setContentView() {
        mIndeterminateView = new SpinView(getContext());
        mIndeterminateView.setAnimationSpeed(ANIMATE_SPEED);
        int widthMeasureSpec = Helper.dpToPixel(40, getContext());
        // 改变加载图标大小
        int heightMeasureSpec = Helper.dpToPixel(40, getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthMeasureSpec, heightMeasureSpec);
        mIndeterminateView.setLayoutParams(lp);
        return mIndeterminateView;
    }

    public ProgressDialog(Context context) {
        super(context);
    }

    /**
     * 改变动画的速度
     *
     * @param scale 默认 1, 2 速度提升2倍, 0.5 速度减少半倍..等
     */
    public void setAnimationSpeed(int scale) {
        mIndeterminateView.setAnimationSpeed(scale);
    }
}
