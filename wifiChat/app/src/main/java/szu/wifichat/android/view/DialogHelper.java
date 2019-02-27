package szu.wifichat.android.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import szu.wifichat.android.R;
import szu.wifichat.android.interfaces.IDialogAction;
import szu.wifichat.android.interfaces.IDialogAction1;
import szu.wifichat.android.interfaces.IEditTextAction;
import szu.wifichat.android.interfaces.IEtSpListenner;
import szu.wifichat.android.interfaces.MyDialogIAction;
import szu.wifichat.android.util.StringHelper;

public class DialogHelper {

    private static DialogHelper dialogHelper;
    private Context m_Context;
    private ProgressDialog pgDialog;

    private DialogHelper() {
    }

    public static DialogHelper getInstance(Context context) {
        if (null == dialogHelper) {
            synchronized (DialogHelper.class) {
                dialogHelper = new DialogHelper();
            }
        }
        dialogHelper.m_Context = context;
        return dialogHelper;
    }

    /**
     * 显示长提示信息
     *
     * @param msg 要显示的信息
     */
    public void showToastLong(String msg) {
        Toast toast = Toast.makeText(m_Context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示短提示信息
     *
     * @param msg 要显示的信息
     */
    public void showToastShort(String msg) {
        Toast toast = Toast.makeText(m_Context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * @param msg        要显示的信息
     * @param drawableId 资源图片id
     * @param duration   显示延时
     */
    public void showToastWithPic(String msg, int drawableId, int duration) {
        Toast toast = Toast.makeText(m_Context, msg, Toast.LENGTH_SHORT);// 需要修改
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(m_Context);
        imageCodeProject.setImageResource(drawableId);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    /**
     * 显示提示对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogInfo(String msgContent) {
        showDialog(m_Context, "提示", msgContent, R.drawable.dlg_info3, null, null, false);
    }

    /**
     * 显示询问对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogAsk(String msgContent, MyDialogIAction positiveAction, MyDialogIAction NegativeAction) {
        showDialogMy(m_Context, "询问", msgContent, R.drawable.dlg_ask3, positiveAction, NegativeAction, true);
    }

    // 弹出对话框
    private void showDialogMy(Context context, String title, String msg, int icon, final MyDialogIAction positiveAction,
                              final MyDialogIAction NegativeAction, boolean showNegativeButton) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(true);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (positiveAction != null) {
                    positiveAction.doSth();
                }
                dialog.dismiss();
            }
        });
        if (showNegativeButton) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    AppUtils.vibrate(m_Context);
                    if (NegativeAction != null) {
                        NegativeAction.doSth();
                    }
                    dialog.dismiss();
                }
            });
        } else {
            btnCancel.setVisibility(View.GONE);
        }
    }

    /**
     * 显示提示对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogInfo(String msgContent, IDialogAction1 action) {
        showDialog(m_Context, "提示", msgContent, R.drawable.dlg_info3, "确定", action, "取消");
    }

    /**
     * 显示提示对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogInfo(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction) {
        showDialog(m_Context, "提示", msgContent, R.drawable.dlg_info3, positiveAction, NegativeAction, true);
    }

    public void showDialogInfo(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction,
                               boolean showNegativeButton) {
        showDialog(m_Context, "提示", msgContent, R.drawable.dlg_info3, positiveAction, NegativeAction, showNegativeButton);
    }

    /**
     * 显示询问对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogAsk(String msgContent, IDialogAction positiveAction) {
        showDialog(m_Context, "询问", msgContent, R.drawable.dlg_ask3, positiveAction, null, false);
    }

    /**
     * 显示询问对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogAsk(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction) {
        showDialog(m_Context, "询问", msgContent, R.drawable.dlg_ask3, positiveAction, NegativeAction, true);
    }

    /**
     * 显示询问对话框
     *
     * @param msgContent   要显示的信息
     * @param positiveText 确定按键的文字
     */
    public void showDialogAsk(String msgContent, String positiveText, IDialogAction positiveAction, String negativeText,
                              IDialogAction NegativeAction) {
        showDialog(m_Context, "询问", msgContent, R.drawable.dlg_ask3, positiveText, positiveAction, negativeText, NegativeAction, true);
    }

    public void showDialogAsk(String msgContent, String confirmStr, String cancelStr, IDialogAction positiveAction,
                              IDialogAction NegativeAction) {
        showDialog2(m_Context, "询问", msgContent, confirmStr, cancelStr, R.drawable.dlg_ask3, positiveAction, NegativeAction, true);
    }

    public void showDialogAsk(String title, String msgContent, String confirmStr, String cancelStr,
                              IDialogAction positiveAction, IDialogAction NegativeAction, boolean showNegativeButton) {
        showDialog2(m_Context, title, msgContent, confirmStr, cancelStr, R.drawable.dlg_ask3, positiveAction, NegativeAction, showNegativeButton);
    }

    /**
     * 显示询问对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogAsk(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction,
                              IDialogAction cancelAction) {
        showDialog(m_Context, "询问", msgContent, R.drawable.dlg_ask3, positiveAction, NegativeAction, cancelAction);
    }

    public void showDialogAsk(String title, String msgContent, String confirmStr, String otherStr, String cancelStr,
                              boolean isShowEtContext, String etContextHint, IEditTextAction positiveAction,
                              IEditTextAction otherAction, MyDialogIAction cancelAction) {
        showDialogThree(title, msgContent, R.drawable.dlg_ask3, confirmStr, otherStr, cancelStr, positiveAction, otherAction, cancelAction, isShowEtContext, etContextHint);
    }

    /**
     * 显示警告对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogWarning(String msgContent) {
        showDialog(m_Context, "警告", msgContent, R.drawable.dlg_warning3, null, null, false);
    }

    /**
     * 显示警告对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogWarning(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction) {
        showDialog(m_Context, "警告", msgContent, R.drawable.dlg_warning3, positiveAction, NegativeAction, true);
    }

    /**
     * 显示错误对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogError(String msgContent) {
        //        LogHelper.logMsg(msgContent);
        showDialog(m_Context, "错误", msgContent, R.drawable.dlg_error3, null, null, false);
    }

    /**
     * 显示错误对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogError(String msgContent, IDialogAction positiveAction, IDialogAction NegativeAction) {
        //        LogHelper.logMsg(msgContent);
        showDialog(m_Context, "错误", msgContent, R.drawable.dlg_error3, positiveAction, NegativeAction, true);
    }

    /**
     * 显示错误对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogError(String msgContent, Throwable e) {
        //        LogHelper.logMsg(msgContent, e);
        showDialog(m_Context, "错误", msgContent, R.drawable.dlg_error3, null, null, false);
    }

    /**
     * 显示错误对话框
     *
     * @param msgContent 要显示的信息
     */
    public void showDialogError(String msgContent, Throwable e, IDialogAction positiveAction,
                                IDialogAction NegativeAction) {
        //        LogHelper.logMsg(msgContent, e);
        showDialog(m_Context, "错误", msgContent, R.drawable.dlg_error3, positiveAction, NegativeAction, true);
    }

    // 弹出对话框
    private void showDialog(Context context, String title, String msg, int icon, String positiveText,
                            final IDialogAction1 positiveAction, String negativeText) {
        showDialog1(context, title, msg, icon, positiveText, positiveAction, negativeText);
    }

    // 弹出对话框
    private void showDialog(Context context, String title, String msg, int icon, final IDialogAction positiveAction,
                            final IDialogAction NegativeAction, boolean showNegativeButton) {
        showDialog(context, title, msg, icon, null, positiveAction, null, NegativeAction, showNegativeButton);
    }

    // 弹出对话框
    private void showDialog(Context context, String title, String msg, int icon, String positiveText,
                            final IDialogAction positiveAction, String negativeText, final IDialogAction NegativeAction,
                            boolean showNegativeButton) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(true);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setText(StringHelper.isNullOrEmpty(positiveText) ? "确定" : positiveText);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (positiveAction != null) {
                    positiveAction.action();
                }
                dialog.dismiss();
            }
        });

        if (showNegativeButton) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setText(StringHelper.isNullOrEmpty(positiveText) ? "取消" : negativeText);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    AppUtils.vibrate(m_Context);
                    if (NegativeAction != null) {
                        NegativeAction.action();
                    }
                    dialog.dismiss();
                }
            });
        } else {
            btnCancel.setVisibility(View.GONE);
        }
    }

    // 弹出对话框
    private void showDialog1(Context context, String title, String msg, int icon, String positiveText,
                             final IDialogAction1 positiveAction, String negativeText) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(true);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setText(StringHelper.isNullOrEmpty(positiveText) ? "确定" : positiveText);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveAction != null) {
                    positiveAction.action(0);
                }
                dialog.dismiss();
            }
        });
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setText(StringHelper.isNullOrEmpty(positiveText) ? "取消" : negativeText);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveAction != null) {
                    positiveAction.action(1);
                }
                dialog.dismiss();
            }
        });
    }

    // 弹出对话框
    private void showDialog2(Context context, String title, String msg, String confirmStr, String cancelStr, int icon,
                             final IDialogAction positiveAction, final IDialogAction NegativeAction,
                             boolean showNegativeButton) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(true);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (positiveAction != null) {
                    positiveAction.action();
                }
                dialog.dismiss();
            }
        });

        if (showNegativeButton) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                    AppUtils.vibrate(m_Context);
                    if (NegativeAction != null) {
                        NegativeAction.action();
                    }
                    dialog.dismiss();
                }
            });
        } else {
            btnCancel.setVisibility(View.GONE);
        }
    }

    // 弹出对话框
    private void showDialog(Context context, String title, String msg, int icon, final IDialogAction positiveAction,
                            final IDialogAction NegativeAction, final IDialogAction cancelAction) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(true);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (positiveAction != null) {
                    positiveAction.action();
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (NegativeAction != null) {
                    NegativeAction.action();
                }
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelAction != null) {
                    //                    AppUtils.vibrate(m_Context);
                    cancelAction.action();
                }
            }
        });
    }

    // 弹出对话框
    private void showDialogThree(String title, String msg, int icon, String confirmStr, String otherStr,
                                 String cancelStr, final IEditTextAction positiveAction,
                                 final IEditTextAction otherAction, final MyDialogIAction cancelAction,
                                 final boolean isShowEtContext, String etContextHint) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_operation_three_layout, R.style.MyDialog);
        dialog.show();
        dialog.setCancelable(false);
        ImageView ivState = (ImageView) dialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnOther = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_other);// 其他按键
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_ooperation_title);
        final EditText etContext = (EditText) dialog.findViewById(R.id.et_message);
        if (isShowEtContext) {
            etContext.setVisibility(View.VISIBLE);
            etContext.setHint(etContextHint);
        }
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setText(confirmStr);
        btnOther.setText(otherStr);
        btnCancel.setText(cancelStr);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (positiveAction != null) {
                    String strEt = "";
                    if (isShowEtContext) strEt = etContext.getText().toString().trim();
                    positiveAction.action(strEt);
                }
                dialog.dismiss();
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (otherAction != null) {
                    String strEt = "";
                    if (isShowEtContext) strEt = etContext.getText().toString().trim();
                    otherAction.action(strEt);
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                AppUtils.vibrate(m_Context);
                if (cancelAction != null) {
                    cancelAction.doSth();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示进度提示框
     *
     * @param title
     * @param msgContent
     * @param onCancelListener
     */
    public void showProgressDialog(String title, String msgContent, OnCancelListener onCancelListener) {
        pgDialog = new ProgressDialog(m_Context);
        pgDialog.setOnCancelListener(onCancelListener);
        pgDialog.setIcon(R.drawable.dlg_info1);
        pgDialog.setTitle(title);
        pgDialog.setMessage(msgContent);
        pgDialog.show();
    }

    /**
     * 取消进度提示框
     */
    public void cancelProgressDialog() {
        if (null != pgDialog && pgDialog.isShowing()) {
            pgDialog.cancel();
        }
    }

    /**
     * 设置DialogActivity的宽度和高度
     *
     * @param activity      要设置的Activity
     * @param widthPercent  宽度相对全屏的占比(值在0到1之间， 0.6表示全屏的60%高度)
     * @param heightPercent 高度相对全屏的占比(值在0到1之间， 0.85表示全屏的85%宽度度)
     */
    public void setDialogActHeightWidth(Activity activity, Double widthPercent, Double heightPercent) throws Exception {
        if (widthPercent != null) {
            if (widthPercent < 0 || widthPercent > 1) {
                throw new Exception("宽度比应该在大于0且小于1");
            }
        }
        if (heightPercent != null) {
            if (heightPercent < 0 || heightPercent > 1) {
                throw new Exception("高度比应该在大于0且小于1");
            }
        }

        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = activity.getWindow().getAttributes(); // 获取对话框当前的参数值
        if (heightPercent != null) {
            p.height = (int) (d.getHeight() * heightPercent); // 高度设置
        }
        if (widthPercent != null) {
            p.width = (int) (d.getWidth() * widthPercent); // 宽度设置
        }
        activity.getWindow().setAttributes(p);
    }

    public int getWindowWidth(Activity activity) {
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽
        return d.getWidth();
    }

    public int getWindowHeight(Activity activity) {
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏高
        return d.getHeight();
    }

    /**
     * 显示单选对话框
     *
     * @param title        标题
     * @param items        对话框列表内容
     * @param selectAction
     */
    public void showSingleSelectDialog(String title, String[] items, final IDialogAction selectAction) {
        Builder builder = new Builder(m_Context);
        builder.setTitle(title);

        // 设置一个下拉的列表选择项
        builder.setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAction.action(which);
            }
        });
        builder.show();
    }

    /**
     * 显示单选对话框
     *
     * @param title        标题
     * @param items        对话框列表内容
     * @param selectAction
     */
    public void showSingleSelectDialog(String title, String[] items, final IDialogAction selectAction,
                                       boolean cancelable) {
        Builder builder = new Builder(m_Context);
        builder.setTitle(title);
        // 设置一个下拉的列表选择项
        builder.setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAction.action(which);
            }
        });
        builder.setCancelable(cancelable);
        builder.show();
    }

    /**
     * 编辑框
     *
     * @param titleText
     * @param iEditTextActionConfirm
     * @param iEditTextActionCancel
     */
    public void showEditDialog(String titleText, String textHint, int inputType,
                               final IEditTextAction iEditTextActionConfirm,
                               final IEditTextAction iEditTextActionCancel) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_normal_layout, R.style.MyDialog);
        dialog.show();
        Button confirm = (Button) dialog.findViewById(R.id.positiveButton);
        Button cancel = (Button) dialog.findViewById(R.id.negativeButton);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(titleText);
        final EditText edt = (EditText) dialog.findViewById(R.id.message);
        edt.setHint(textHint);
        edt.setInputType(inputType);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editResult = edt.getText().toString().trim();
                if (edt.getText().toString().isEmpty()) {
                    showToastShort("不能为空");
                    return;
                } else if (!StringHelper.isNumberByStr(editResult)) // 判断输入的数字正不正确
                {
                    showToastShort("请输入正确的数量");
                    return;
                } else if (Double.valueOf(editResult) < 0) {
                    showToastShort("数量不能为负数");
                    return;
                } else {
                    if (iEditTextActionConfirm != null) {
                        iEditTextActionConfirm.action(edt.getText().toString());
                        dialog.dismiss();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (iEditTextActionCancel != null) {

                }
                dialog.dismiss();

            }
        });
    }

    public void showEditSpinnerDialog(String titleText, String hint, final String[] spinnerSrcArr, boolean cancelable,
                                      boolean isSpinnerVistable, final IEtSpListenner iEtSpListennerConfirm) {
        final CustomDialog dialog = new CustomDialog(m_Context, R.layout.dialog_edit_spinner_layout, R.style.MyDialog);
        dialog.show();
        Button confirm = (Button) dialog.findViewById(R.id.btn_dialog_etsp_positive);// 确认
        Button cancel = (Button) dialog.findViewById(R.id.btn_dialog_etsp_negative);// 取消
        TextView title = (TextView) dialog.findViewById(R.id.tv_dialog_etsp_title);
        title.setText(titleText);
        final Spinner spOperator = (Spinner) dialog.findViewById(R.id.sp_dialog_etsp_operator);
        final EditText edtRemark = (EditText) dialog.findViewById(R.id.et_dialog_etsp_remark);
        edtRemark.setHint(hint);
        dialog.setCancelable(cancelable);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(m_Context, R.layout.spinner_item, spinnerSrcArr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOperator.setAdapter(arrayAdapter);
        spOperator.setVisibility(isSpinnerVistable ? View.VISIBLE : View.GONE);
        spOperator.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (spinnerSrcArr[arg2].equals("到位")) {
                    edtRemark.setText("手动考勤到位");// 备注栏设置
                    return;
                }
                edtRemark.setText(spinnerSrcArr[arg2]);// 备注栏设置
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSpinner = spinnerSrcArr[spOperator.getSelectedItemPosition()];
                if (strSpinner.equals("缺席") && (edtRemark.getText().toString() == null || edtRemark.getText().toString()
                                                                                                   .isEmpty())) {
                    showToastShort("请填写缺席原因");
                    return;
                }
                if (iEtSpListennerConfirm != null) {
                    iEtSpListennerConfirm.result(edtRemark.getText().toString(), spOperator.getSelectedItemPosition());
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 日期选择器
     */
    //    public void showDateDialog(final TextView textView)
    //    {
    //        DateTimePickerDialog dialog = new DateTimePickerDialog(m_Context,
    //                System.currentTimeMillis());
    //        dialog.setOnDateTimeSetListener(new OnDateTimeSetListener()
    //        {
    //            public void OnDateTimeSet(AlertDialog dialog, long date)
    //            {
    //                textView.setText(DateHelper.getLongStringDate("yyyy-MM-dd HH:mm", date));
    //            }
    //        });
    //        dialog.show();
    //    }

    /**
     * 日期选择器
     *
     * @param context
     * @param title
     * @param message
     * @param textView
     */
    public void showDatePickerDialog(Context context, String title, String message, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(formatDate(year, monthOfYear, dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.setTitle(title);
        datePickerDialog.setMessage(message);
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        return new StringBuilder().append(year).append("-").append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                                  .append("-").append((day < 10) ? "0" + day : day).toString();
    }

    /**
     * 显示单选对话框(旁边有个选项图标)
     *
     * @param title        标题
     * @param items        对话框列表内容
     * @param selectAction
     */
    public void showSingleSelectDialog1(String title, String[] items, int checkedItem, boolean cancelable,
                                        final IDialogAction selectAction) {
        Builder builder = new Builder(m_Context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, checkedItem, new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectAction.action(arg1);
                arg0.dismiss();
            }
        });
        builder.setCancelable(cancelable);
        builder.create();
        builder.show();
    }

    /**
     * 显示单选对话框
     *
     * @param title        标题
     * @param selectAction
     */
    public void showSelectUserDialog(String title, List<String> userList, final IDialogAction selectAction,
                                     boolean cancelable) {
        if (userList == null || userList.size() == 0) {
            return;
        }
        String users[] = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            users[i] = userList.get(i);
        }
        Builder builder = new Builder(m_Context);
        builder.setTitle(title);
        // 设置一个下拉的列表选择项
        builder.setItems(users, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAction.action(which);
            }
        });
        builder.setCancelable(cancelable);
        builder.show();
    }

    public void showDialogNoButton(String msgContent, IDialogAction1 positiveAction) {
        showDialogNoButton("提示", msgContent, R.drawable.dlg_info3, positiveAction);
    }

    private CustomDialog mFingerDialog = null;

    // 弹出对话框
    private void showDialogNoButton(String title, String msg, int icon, final IDialogAction1 positiveAction) {
        mFingerDialog = new CustomDialog(m_Context, R.layout.dialog_operation_layout, R.style.MyDialog);
        mFingerDialog.show();
        mFingerDialog.setCancelable(true);
        ImageView ivState = (ImageView) mFingerDialog.findViewById(R.id.iv_dialog_ooperation_state);
        TextView tvContext = (TextView) mFingerDialog.findViewById(R.id.tv_dialog_ooperation_cntext);
        Button btnConfirm = (Button) mFingerDialog.findViewById(R.id.btn_dialog_ooperation_positive);// 确认
        Button btnCancel = (Button) mFingerDialog.findViewById(R.id.btn_dialog_ooperation_negative);// 取消
        //        btnConfirm.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        TextView tvTitle = (TextView) mFingerDialog.findViewById(R.id.tv_dialog_ooperation_title);
        tvTitle.setText(title);
        ivState.setImageResource(icon);
        tvContext.setText(msg);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveAction != null) {
                    positiveAction.action(0);
                }
                if (mFingerDialog != null) mFingerDialog.dismiss();
            }
        });
    }

    public void cancelFingerDialog() {
        if (mFingerDialog != null && mFingerDialog.isShowing()) {
            mFingerDialog.cancel();
            mFingerDialog = null;
        }
    }
}
