package szu.wifichat.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import szu.wifichat.android.R;

/**
 * Created by huangqj on 2017-06-17.
 */

public class CountDownDialog extends Dialog {
    private Context context;
    //    int layoutResID;
    private TimeCount timeCount;
    private int mCount;//秒数
    TextView tvTime;

    public CountDownDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CountDownDialog(@NonNull Context context, @StyleRes int themeResId, int count) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.mCount = count;

    }

    protected CountDownDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_affirm_fringer_layout);
        setCanceledOnTouchOutside(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e("TimeCount---", "dismiss");
                timeCount.cancel();
            }
        });
        initView();
    }

    private void initView() {
        timeCount = new TimeCount(mCount * 1000, 1000);
        timeCount.start();
        ImageView ivState = (ImageView) findViewById(R.id.iv_dialog_ooperation_state);
        tvTime = (TextView) findViewById(R.id.tv_finger_time);
        TextView tvContext = (TextView) findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        btnCancel.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText("请确认您的指纹");
        ivState.setImageResource(R.drawable.dlg_info3);
        tvContext.setText("请确认您的指纹");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                if (positiveAction != null) {
                //                    positiveAction.action(0);
                //                }
                //                mFingerDialog.dismiss();
            }
        });
    }

    //    @Override
    //    public void onDismiss(DialogInterface dialog) {
    //
    //    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    /**
     * 倒计时
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCount = (int) (millisUntilFinished / 1000);
            tvTime.setText(mCount + "s");
        }

        @Override
        public void onFinish() {
            mCount = 0;
            dismiss();
            //            tvCountDown.setVisibility(View.GONE);
        }
    }


}
