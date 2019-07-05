package com.hw.baselibrary.loading;


import android.content.Context;
import android.view.View;

/**
 * @author huangqj
 */
public class ProgressLoadDialog extends BaseProgressDialog {
    private int mMaxProgress;
    private AnnularView mDeterminateView;
    /**
     * 加载完是否自动dismiss
     */
    private boolean mIsAutoDismiss = true;

    @Override
    protected View setContentView() {
        mDeterminateView = new AnnularView(getContext());
        return mDeterminateView;
    }

    public ProgressLoadDialog(Context context) {
        super(context);
    }

    /**
     * 进度条的最大值
     */
    public ProgressLoadDialog setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        mDeterminateView.setMax(maxProgress);
        return this;
    }

    /**
     * 进度条到最大值时,dialog是否自动关闭
     */
    public ProgressLoadDialog setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    public void setProgress(int progress) {
        if (mDeterminateView != null) {
            mDeterminateView.setProgress(progress);
            if (mIsAutoDismiss && progress >= mMaxProgress) {
                dismiss();
            }
        }
    }
}
