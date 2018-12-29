package com.hw.adb;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
    private static final long serialVersionUID = 6943717305316631913L;
    private String path;
    private int w = 60;
    private int h = 60;

    public ImagePanel(String path, int w, int h)
    {
	this.path = path;
	if (w > 0) this.w = w;
	if (h > 0) this.h = h;
    }

    @Override
    public void paint(Graphics g)
    {
	super.paint(g);
	if (path == null || path.isEmpty()) return;
	// ImageIcon icon = new ImageIcon("F:\\chat\\icon_moto.png");
	ImageIcon icon = new ImageIcon(path);
	g.drawImage(icon.getImage(), 0, 0, w, h, this);
    }
}
