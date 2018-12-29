package com.hw.adb.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class DialogFrame extends JFrame
{
    private static final long serialVersionUID = 3318304219185131170L;
    private JLabel text;
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension screensize = tk.getScreenSize();
    int height = screensize.height;
    int width = screensize.width;
    private String str = null;
    private int dialogW = 350;
    private int dialogH = 110;

    private int locX, locY;

    private Timer timer;
    private boolean isShowing = false;

    public DialogFrame()
    {
	text = new JLabel();
	Font font = new Font("", 0, 20);
	text.setFont(font);
	text.setBackground(new Color(92, 229, 62));
	text.setHorizontalAlignment(JLabel.CENTER);
	getContentPane().add(text, BorderLayout.CENTER);
	getContentPane().setBackground(Color.decode("#7BE665"));// ÉèÖÃ´°¿Ú±³¾°ÑÕÉ«
	locX = width / 2;
	locY = height / 2;
    }

    public void showDialogFrame(String str, int locX, int locY)
    {
	this.locX = locX;
	this.locY = locY;
	this.str = str;
	isShowing = true;
	timer = new Timer();
	timer.schedule(new TimerTask()
	{
	    @Override
	    public void run()
	    {
		disposeDialog();
	    }
	}, 1500);
	new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		initGUI();
	    }
	}).start();
    }

    private void initGUI()
    {
	try
	{
	    setUndecorated(true);
	    setLocationRelativeTo(null);
	    setVisible(true);
	    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    {
		String src = "<html>" + str + "</html>";
		text.setText(src);
	    }
	    pack();
	    int x = locX + dialogW / 2;
	    int y = locY / 2;
	    setBounds(x, y, dialogW, dialogH);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public boolean isShowing()
    {
	return isShowing;
    }

    public void disposeDialog()
    {
	if (timer != null) timer.cancel();
	isShowing = false;
	dispose();
    }
}
