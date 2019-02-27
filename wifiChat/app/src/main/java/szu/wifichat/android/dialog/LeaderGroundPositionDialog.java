package szu.wifichat.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class LeaderGroundPositionDialog extends Dialog {
    private Context context;
    TextView tvTime;
    private String mContent = "";

    public LeaderGroundPositionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public LeaderGroundPositionDialog(@NonNull Context context, String content) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.mContent = content;
    }

    public LeaderGroundPositionDialog(@NonNull Context context, @StyleRes int themeResId, int count) {
        super(context, R.style.MyDialog);
        this.context = context;

    }

    protected LeaderGroundPositionDialog(@NonNull Context context, boolean cancelable,
                                         @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_leader_ground_psoition_layout);
        setCanceledOnTouchOutside(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e("TimeCount---", "dismiss");
                // timeCount.cancel();
            }
        });
        initView();
    }

    private void initView() {
        // LinearLayout llBgWarning = (LinearLayout) findViewById(R.id.ll_dialog_warning);
        // Animation ani = new AlphaAnimation(0.2f, 1f);
        // ani.setDuration(1000);
        // ani.setRepeatMode(Animation.REVERSE);
        // ani.setRepeatCount(Animation.INFINITE);
        // llBgWarning.startAnimation(ani);

        ImageView ivState = (ImageView) findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        btnCancel.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText("提示");
        ivState.setImageResource(R.drawable.dlg_info3);
        tvContext.setText("接地位置：" + mContent + "已定位");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }
}
