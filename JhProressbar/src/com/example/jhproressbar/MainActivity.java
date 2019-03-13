package com.example.jhproressbar;

import com.example.jhproressbar.view.BaseProgressDialog;
import com.example.jhproressbar.view.ProgressDialog;
import com.example.jhproressbar.view.ProgressLoadDialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Button indeterminate = (Button) findViewById(R.id.indeterminate);
	indeterminate.setOnClickListener(this);

	Button labelIndeterminate = (Button) findViewById(R.id.label_indeterminate);
	labelIndeterminate.setOnClickListener(this);

	Button detailIndeterminate = (Button) findViewById(R.id.detail_indeterminate);
	detailIndeterminate.setOnClickListener(this);

	Button annularDeterminate = (Button) findViewById(R.id.annular_determinate);
	annularDeterminate.setOnClickListener(this);

	Button dimBackground = (Button) findViewById(R.id.dim_background);
	dimBackground.setOnClickListener(this);

    }

    private BaseProgressDialog hud;

    @Override
    public void onClick(View v)
    {
	switch (v.getId())
	{
	    case R.id.indeterminate:
		hud = new ProgressDialog(this);
		scheduleDismiss();
		break;
	    case R.id.label_indeterminate:
		hud = new ProgressDialog(this).setLabel("Please wait");
		scheduleDismiss();
		break;
	    case R.id.detail_indeterminate:
		hud = new ProgressDialog(this).setLabel("Please wait").setDetailsLabel(
			"Downloading data");
		scheduleDismiss();
		break;

	    case R.id.annular_determinate:
		hud = new ProgressLoadDialog(this).setLabel("Please wait");
		simulateProgressUpdate();
		break;

	    case R.id.dim_background:
		hud = new ProgressDialog(this).setDimAmount(0.5f);
		scheduleDismiss();
		break;

	}

	hud.show();
    }

    private void simulateProgressUpdate()
    {
	if (hud instanceof ProgressLoadDialog)
	{
	    final ProgressLoadDialog progressLoadDialog = (ProgressLoadDialog) hud;
	    progressLoadDialog.setMaxProgress(100);
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable()
	    {
		int currentProgress;

		@Override
		public void run()
		{
		    currentProgress += 1;
		    progressLoadDialog.setProgress(currentProgress);
		    if (currentProgress < 100)
		    {
			handler.postDelayed(this, 50);
		    }
		}
	    }, 100);
	}
    }

    private void scheduleDismiss()
    {
	Handler handler = new Handler();
	handler.postDelayed(new Runnable()
	{
	    @Override
	    public void run()
	    {
		if (hud.isShowing())
		{
		    hud.dismiss();
		}
	    }
	}, 2000);
    }
}
