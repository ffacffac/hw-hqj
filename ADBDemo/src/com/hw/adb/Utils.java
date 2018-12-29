package com.hw.adb;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JTextField;

import java.util.Map.Entry;

public class Utils
{

    public static void savePath(String adbPath, String phonePath, String pcPath,
	    String copyToPhonePath)
    {
	adbPath = FileUtils.KEY_ADB + FileUtils.KEY_SPLIT + adbPath + FileUtils.PATH_SPLIT;
	phonePath = FileUtils.KEY_PHNOE + FileUtils.KEY_SPLIT + phonePath + FileUtils.PATH_SPLIT;
	pcPath = FileUtils.KEY_PC + FileUtils.KEY_SPLIT + pcPath + FileUtils.PATH_SPLIT;
	copyToPhonePath = FileUtils.KEY_TO_PHONE_PATH + FileUtils.KEY_SPLIT + copyToPhonePath;
	StringBuilder sb = new StringBuilder();
	sb.append(adbPath).append(phonePath).append(pcPath).append(copyToPhonePath);
	try
	{
	    FileUtils.writeADBPath(sb.toString());
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}

    }

    /**
     * 获取保存的路径：项目路径/adbPath.txt
     * 
     * @return
     */
    public static HashMap<String, String> getAllPath()
    {
	try
	{
	    String src = FileUtils.getAdbPath();// 获取项目所在路径
	    if (src == null || src.isEmpty()) return null;
	    if (src.contains(FileUtils.PATH_SPLIT))
	    {
		String[] arr = src.split(FileUtils.PATH_SPLIT);
		if (arr == null) return null;
		HashMap<String, String> hashMap = new HashMap<>();
		for (String string : arr)
		{
		    if (string.contains(FileUtils.KEY_SPLIT))
		    {
			String[] arr_key_value = string.split(FileUtils.KEY_SPLIT);
			if (arr_key_value != null && arr_key_value.length > 0)
			{
			    if (arr_key_value.length >= 2)
			    {
				hashMap.put(arr_key_value[0], arr_key_value[1]);
			    }
			    else
			    {
				hashMap.put(arr_key_value[0], "");
			    }
			}
		    }
		}
		return hashMap;
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 读取文件,配置路径
     * 
     * @param hashMap
     */
    public static void initPath(HashMap<String, String> hashMap, JTextField tfCopyPhonePath,
	    JTextField tfCopyToPcPath, JTextField tfCopyToPhonePath)
    {
	if (hashMap == null || hashMap.isEmpty()) return;
	Set<Entry<String, String>> set = hashMap.entrySet();
	for (Entry<String, String> entry : set)
	{
	    String key = entry.getKey();
	    String value = entry.getValue();
	    switch (key)
	    {
		case FileUtils.KEY_ADB:
		    // tfAdbPath.setText(value);
		    break;
		case FileUtils.KEY_PHNOE:
		    if (value.isEmpty()) tfCopyPhonePath.setText(ADBDemo.COPY_PHONE_PATH);
		    else
			tfCopyPhonePath.setText(Utils.getPathProperty(value, false));
		    break;
		case FileUtils.KEY_PC:
		    tfCopyToPcPath.setText(Utils.getPathProperty(value, false));
		    break;
		case FileUtils.KEY_TO_PHONE_PATH:
		    if (value.isEmpty()) tfCopyToPhonePath.setText(ADBDemo.COPY_TO_PHONE_PATH);
		    else
			tfCopyToPhonePath.setText(Utils.getPathProperty(value, false));
		    break;
		default:
		    break;
	    }
	}
    }

    /**
     * 转换为就java路径：如F：\abc\abc
     * 
     * @param path 路径
     * @param isAddEnd 是否在结尾添加反斜杠
     */
    public static String getPathProperty(String path, boolean isAddEnd)
    {
	if (path == null || path.isEmpty()) return "";
	if (path.contains("\\"))
	{
	    path = path.replace("\\", "/");
	}
	if (!isAddEnd) return path;
	if (!path.endsWith("/"))
	{
	    path = path + "/";
	}
	return path;
    }

    public static Font getFont(String str, int textSize)
    {
	return new Font(str, 0, textSize);
    }

    public static void setRemindTF(JTextField tfRemindInfo, String str, boolean vis)
    {
	if (!vis)
	{
	    tfRemindInfo.setVisible(vis);
	    return;
	}
	tfRemindInfo.setVisible(vis);
	tfRemindInfo.setFont(Utils.getFont(str, 16));
	tfRemindInfo.setText(str);
    }

    public static void setWindowListener(ADBDemo adbDemo, IADBListener iadbListener)
    {
	adbDemo.addWindowListener(new WindowListener()
	{

	    @Override
	    public void windowOpened(WindowEvent e)
	    {
		System.out.println("windowOpened------");
	    }

	    @Override
	    public void windowIconified(WindowEvent e)
	    {}

	    @Override
	    public void windowDeiconified(WindowEvent e)
	    {}

	    @Override
	    public void windowDeactivated(WindowEvent e)
	    {}

	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		System.out.println("windowClosing------");
		iadbListener.close();
	    }

	    @Override
	    public void windowClosed(WindowEvent e)
	    {
		System.out.println("windowClosed--------");
	    }

	    @Override
	    public void windowActivated(WindowEvent e)
	    {
		System.out.println("windowActivated--------");
	    }
	});
    }

}
