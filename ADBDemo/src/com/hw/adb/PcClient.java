package com.hw.adb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.hw.adb.Eenum.ShellType;

// http://download.csdn.net/download/liuyifirsttime/9397977
/**
 * ��ADB������adb shell����Ϊ#������echo <�豸������> > /sys/class/android_usb/android0/iSerial �磺 echo
 * HWPAD0014523FFAG > /sys/class/android_usb/android0/iSerial
 * 
 * @author huangqj
 *
 */
public class PcClient
{
    private static final boolean D = true;
    private boolean findDevice = false;// �Ƿ�����豸
    private IADBListener adbListener;

    public void setADBListener(IADBListener adbListener)
    {
	this.adbListener = adbListener;
    }

    private List<String> devices = new ArrayList<>();
    private String m_adbShell = "";

    public void stopFindDevice()
    {
	findDevice = false;
    }

    public boolean isFindDevRunning()
    {
	return findDevice;
    }

    public void findDevice(final String adbShell)
    {
	if (adbShell == null || adbShell.isEmpty()) return;
	findDevice = true;
	new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		while (findDevice)
		{
		    devices.clear();
		    shell(adbShell, true, ShellType.FIND_DEVICE);
		    try
		    {
			Thread.sleep(3000);
		    }
		    catch (InterruptedException e)
		    {
			e.printStackTrace();
			findDevice = false;
		    }
		}
	    }
	}).start();
    }

    public void adbShell(final String adbShell, ShellType shellType)
    {
	m_adbShell = adbShell;
	new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		shell(m_adbShell, false, shellType);
	    }
	}).start();
    }

    public void shell(String shell, boolean isFindDev, ShellType shellType)
    {
	try
	{
	    // ��ѯ�豸
	    // String shell = "adb -s 0123456789ABCDEF shell rename
	    // /sdcard/sdms/edf.txt
	    // /sdcard/sdms/1.txt";
	    // String shell = adb_path + "adb devices";
	    // String shell = "adb pull sdcard/sdms/dbs/
	    // F:\\USBFile";//
	    // ����·��
	    // System.out.println("shell on run()==" + shell);
	    // Process sh = Runtime.getRuntime().exec(shell);
	    Process sh = Runtime.getRuntime().exec(shell);
	    InputStream in = sh.getInputStream();
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String content = null;
	    if (D && !isFindDev) System.out.println("input result:");
	    while ((content = br.readLine()) != null)
	    {
		if (content.contains("device") && !content.contains("devices"))
		{
		    String name = content.replace("device", "");
		    devices.add(name);
		}
		if (content.contains("%"))
		{
		    String pro = content.substring(1, content.indexOf("]"));
		    adbListener.filePro(pro, shellType);
		}
		if (content.contains("Success"))
		{
		    adbListener.filePro("100%", shellType);
		}
		if (content.contains("adb: error:"))
		{
		    adbListener.shellException(shellType, shell, new Exception(content));
		}
		if (D && !isFindDev) System.out.println(content);
	    }
	    adbListener.findDevice(devices);
	    br.close();
	    in.close();

	    in = sh.getErrorStream();
	    br = new BufferedReader(new InputStreamReader(in));
	    content = null;
	    if (D && !isFindDev) System.out.println("error result:");
	    while ((content = br.readLine()) != null)
	    {
		if (D && !isFindDev) System.out.println(content);// û���豸����ʱ��ӡerror:
		// device
		// not found
	    }
	    if (D && !isFindDev) System.out.println("----------------�ָ���------------------");
	    br.close();
	    in.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    if (isFindDev) findDevice = false;
	    adbListener.shellException(shellType, shell, e);
	}
    }
}
