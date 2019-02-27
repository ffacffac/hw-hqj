package szu.wifichat.android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CustomDialog extends Dialog
{

    Context context;
    int layoutResID;

    public CustomDialog(Context context, int layoutResID)
    {
        super(context);
        this.context = context;
        this.layoutResID = layoutResID;
    }

    public CustomDialog(Context context, int layoutResID, int theme)
    {
        super(context, theme);
        this.context = context;
        this.layoutResID = layoutResID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResID);
    }
}