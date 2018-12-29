package com.hw.adb;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hw.adb.Eenum.ShellType;
import com.hw.adb.dialog.DialogFrame;

public class ADBDemo extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 4989174621876358997L;
    public static final String ADB_PATH = "F:/ruanjian/android/AndroidSdk/platform-tools/";// adb所在路径
    public static final String COPY_PHONE_PATH = "sdcard/sdms/dbs";
    public static final String COPY_TO_PC_PATH = "F:/MyDB";
    public static final String COPY_TO_PHONE_PATH = "sdcard/";
    public static final String SHELL_FIND_DEV = "adb devices";
    public static final String SHELL_PULL = "adb pull";
    public static final String SHELL_PUSH = "adb push";
    public static final String SHELL_INSTALL = "adb install";
    private static PcClient pcClient;
    private static ADBDemo adbDemo;

    private int s = 600;// 窗口大小
    private int btnW = 80;// 通用按钮宽
    private int btnH = 40;
    private Button savePath = new Button("保存路径");
    private Button find = new Button("查找设备");
    private Button copy = new Button("开始拷贝");
    private Button close = new Button("关闭");
    private Button devName = new Button("连接设备列表");
    private Button chooseAPK = new Button("选择apk");
    private Button install = new Button("安装apk");
    private static JTextField tfInstallPro = new JTextField("安装进度:0%");
    private static JTextField tfCopyToPhonePro = new JTextField("拷贝到手机进度:0%");
    private static JTextField findDevTextInfo = new JTextField();
    private static int findDevCount = 0;// 查找设备次数

    // private Button btnAdbPathName = new Button("电脑端adb路径：");// adb路径
    // private static JTextField tfAdbPath = new JTextField("");// adb路径

    // 拷贝到手机的路径
    private Button btnCopyToPhoneStart = new Button("拷贝到手机");
    private Button btnCopyToPhone = new Button("拷贝到手机的路径");
    private static JTextField tfCopyToPhonePath = new JTextField(COPY_TO_PHONE_PATH);
    private Button btnChoosePcFile = new Button("选择电脑文件");
    private static JTextField tfCopyToPhoneFile = new JTextField();
    // 拷贝到电脑的路径
    private Button btnCopyToPcPath = new Button("拷贝到电脑的路径：");
    private static JTextField tfCopyToPcPath = new JTextField("");
    // 拷贝手机文件的路径
    private Button btnCopyPhonePath = new Button("拷贝手机文件路径：");
    private static JTextField tfCopyPhonePath = new JTextField(COPY_PHONE_PATH);
    private static JTextField tfCopyToPcPro = new JTextField();// 拷贝进度
    // JTextArea支持换行转义符，但是不支持文本居中
    // JTextField支持文本居中，但不支持换行
    private static JTextField tfRemindInfo = new JTextField("请配置好相关路径");// 提示文本
    private JTextField tfAPKPath = new JTextField();// 安装apk的路径

    private DialogFrame dialog = new DialogFrame();

    private int layoutMarginR = 200;
    private int layoutMarginT = 200;// 窗口起始显示位置

    public static void main(String[] args)
    {
	pcClient = new PcClient();
	pcClient.setADBListener(adbListener);
	adbDemo = new ADBDemo();
	Utils.setWindowListener(adbDemo, adbListener);
	adbDemo.init();
	findDevice(true);// 进来的时候先查找设备是否连接
    }

    private void init()
    {
	System.out.println("init()");
	adbPath = Utils.getPathProperty(FileUtils.getProjectPath() + "/platform-tools/", false);
	System.out.println("init adpPath=" + adbPath);
	findDevTextInfo.setFont(Utils.getFont("", 14));
	findDevTextInfo.setHorizontalAlignment(JTextField.CENTER);
	new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		Utils.initPath(Utils.getAllPath(), tfCopyPhonePath, tfCopyToPcPath,
			tfCopyToPhonePath);
		adbDemo.showLayout();
	    }
	}).start();
    }

    private int btnAdbPathH = btnH;
    private int btnPathW = 120;
    private int tfPathW = 190;// 通用文本框宽度

    // /**
    // * 显示图片
    // *
    // */
    // private void setImage()
    // {
    // try
    // {
    // String path = Utils.getPathProperty(FileUtils.getProjectPath() + "/ic_myimg.png",
    // false);
    // System.out.println("img path=" + path);
    // ImagePanel imgPanel = new ImagePanel(path, 0, 0);
    // imgPanel.setBounds(0, 0, 30, 30);
    // this.add(imgPanel);
    // }
    // catch (Exception e)
    // {
    // e.printStackTrace();
    // }
    // }

    /**
     * 设置adb的路径
     */
    private void setADBPathText()
    {
	// this.add(btnAdbPathName);
	// btnAdbPathName.addActionListener(this);
	// btnAdbPathName.setBounds(10, 10, btnPathW, btnH);
	// this.add(tfAdbPath);
	// tfAdbPath.setBounds(btnPathW + 15, 10, tfPathW, btnH);
	// tfAdbPath.setHorizontalAlignment(JTextField.CENTER);
    }

    private int installMarginTop = 400;// 安装apk按钮距离窗口顶部

    /**
     * 设置拷贝到手机的路径
     */
    private void setInstallText()
    {
	this.add(tfAPKPath);
	tfAPKPath.setBounds(10, installMarginTop, tfPathW, btnH);

	this.add(chooseAPK);
	chooseAPK.addActionListener(this);
	chooseAPK.setBounds(tfPathW + 10, installMarginTop, 60, btnH);

	this.add(install);
	install.addActionListener(this);
	install.setBounds(btnW + 10 + tfPathW, installMarginTop, 60, btnH);

	this.add(tfInstallPro);
	tfInstallPro.setBounds(btnW + 10 + tfPathW + 60 + 8, installMarginTop, 110, btnH);
    }

    /**
     * 设置拷贝到手机的路径
     */
    private void setCopyToPhonePathText()
    {
	this.add(btnCopyToPhone);
	btnCopyToPhone.addActionListener(this);
	int marginTop = installMarginTop + btnH + 10;
	btnCopyToPhone.setBounds(10, marginTop, btnPathW, btnH);
	this.add(tfCopyToPhonePath);
	tfCopyToPhonePath.setBounds(btnPathW + 15, marginTop, 120, btnH);
	tfCopyToPhonePath.setHorizontalAlignment(JTextField.CENTER);

	// 选择要拷贝到手机的文件
	int tfCopyToPhonePathMarginL = btnPathW + 120 + 10 + 20;
	this.add(tfCopyToPhoneFile);
	tfCopyToPhoneFile.setBounds(tfCopyToPhonePathMarginL, marginTop, 130, btnH);
	tfCopyToPhoneFile.setHorizontalAlignment(JTextField.CENTER);

	this.add(tfCopyToPhonePro);
	tfCopyToPhonePro.setBounds(tfCopyToPhonePathMarginL, marginTop + btnH + 5, 140, btnH);

	this.add(btnChoosePcFile);
	btnChoosePcFile.addActionListener(this);
	btnChoosePcFile.setBounds(tfCopyToPhonePathMarginL + 130 + 5, marginTop, btnW, btnH);

	this.add(btnCopyToPhoneStart);
	btnCopyToPhoneStart.addActionListener(this);
	btnCopyToPhoneStart.setBounds(tfCopyToPhonePathMarginL + 130 + btnW + 10, marginTop, btnW,
		btnH);
    }

    private void setCopyToPcPathText()
    {
	this.add(btnCopyToPcPath);
	btnCopyToPcPath.addActionListener(this);
	btnCopyToPcPath.setBounds(10, btnAdbPathH + 10, btnPathW, btnH);
	this.add(tfCopyToPcPath);
	tfCopyToPcPath.setBounds(btnPathW + 15, btnAdbPathH + 10, tfPathW, btnH);
	tfCopyToPcPath.setHorizontalAlignment(JTextField.CENTER);
    }

    private void setCopyPhonePath()
    {
	this.add(btnCopyPhonePath);
	btnCopyPhonePath.addActionListener(this);
	btnCopyPhonePath.setBounds(10, btnAdbPathH * 2 + 10, btnPathW, btnH);
	this.add(tfCopyPhonePath);
	tfCopyPhonePath.setBounds(btnPathW + 15, btnAdbPathH * 2 + 10, tfPathW, btnH);
	tfCopyPhonePath.setHorizontalAlignment(JTextField.CENTER);
    }

    private static Color colorErr = Color.decode("#EEB225");
    private static Color colorNor = Color.decode("#FFFFFF");

    public void showLayout()
    {
	this.setTitle("手机助手");
	this.setLayout(null);

	setADBPathText();
	setCopyToPcPathText();
	setCopyPhonePath();

	this.add(savePath);
	savePath.addActionListener(this);
	savePath.setBounds(10 + btnPathW + tfPathW + 7, 10 + btnH, 50, btnH * 2);

	this.add(find);
	find.addActionListener(this);
	find.setBounds(40, btnAdbPathH * 3 + 30, btnW, btnH);

	this.add(copy);
	copy.addActionListener(this);
	copy.setBounds(40 + btnW + 30, btnAdbPathH * 3 + 30, btnW, btnH);

	this.add(tfCopyToPcPro);
	tfCopyToPcPro.setBounds(btnW * 2 + 40 + 40 + 10, btnAdbPathH * 3 + 20, 110, 70);
	tfCopyToPcPro.setText("拷贝进度：0%");
	tfCopyToPcPro.setHorizontalAlignment(JTextField.CENTER);

	this.add(tfRemindInfo);
	tfRemindInfo.setBounds(50, btnAdbPathH * 6 + 15, 250, 120);
	tfRemindInfo.setBackground(colorErr);
	tfRemindInfo.setHorizontalAlignment(JTextField.CENTER);
	Utils.setRemindTF(tfRemindInfo, "配置路径不正确", false);

	this.add(devName);
	devName.addActionListener(this);
	devName.setBounds(380, 5, 200, 35);
	devName.setBackground(Color.decode("#5C6667"));
	devName.setFont(new Font("设备列表", 0, 20));
	devName.setForeground(Color.decode("#FFFFFF"));

	setInstallText();
	setCopyToPhonePathText();

	this.add(close);
	close.addActionListener(this);
	close.setBounds(480, 500, 100, 50);
	close.setBackground(Color.decode("#1CDBEE"));

	this.add(findDevTextInfo);
	findDevTextInfo.setBounds(380, 40, 200, 350);
	// 设置窗口的位置和大小
	this.setBounds(layoutMarginR, layoutMarginT, s, s);
	this.setVisible(true);
    }

    private static IADBListener adbListener = new IADBListener()
    {
	int s = 0;

	@Override
	public void findDevice(List<String> devices)
	{
	    if (devices == null || devices.isEmpty())
	    {
		findDevCount += 1;
		StringBuilder sb = new StringBuilder();
		sb.append(NO_FOUND_DEV).append("，").append("次数：").append(findDevCount);
		findDevTextInfo.setText(sb.toString());
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    setPathNormalView();
	    StringBuilder sb = new StringBuilder();
	    for (String name : devices)
	    {
		sb.append("\n").append("设备：").append(name).append("\n");
		if (s % 15 == 0 || s == 0)
		    System.out.println("name==" + name + "-----lenght=" + name.length());
	    }
	    findDevTextInfo.setText(sb.toString());
	    s++;
	}

	@Override
	public void filePro(String pro, ShellType shellType)
	{
	    if (ShellType.COPY_TO_PC == shellType)
	    {
		// 拷贝手机附件到到电脑
		tfCopyToPcPro.setText("拷贝进度：" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("拷贝成功");
	    }
	    else if (ShellType.INSTALL_APK == shellType)
	    {
		// 安装apk进度
		tfInstallPro.setText("安装进度：" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("安装成功");
	    }
	    else if (ShellType.COPY_TO_PHONE == shellType)
	    {
		// 拷贝电脑文件到手机的进度
		tfCopyToPhonePro.setText("拷贝到手机进度：" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("拷贝成功");
	    }
	}

	@Override
	public void shellException(ShellType shellType, String shellSrc, Exception e)
	{
	    String errMsg = "";
	    if (e != null)
	    {
		errMsg = e.getMessage();
	    }
	    JOptionPane.showMessageDialog(null, shellSrc + "\n" + "异常信息:" + errMsg);
	}

	@Override
	public void close()
	{
	    adbDemo.closeSys();
	}
    };

    private static final String NO_FOUND_DEV = "没有找到设备";
    private static final String NO_FOUND_DEV_CONNECT = "设备未连接，请用USB线连接手机";

    /**
     * 是否有设备连接上
     */
    private static boolean deviceConnect()
    {
	return (!findDevTextInfo.getText().contains(NO_FOUND_DEV) && pcClient.isFindDevRunning());
    }

    private void closeSys()
    {
	pcClient.stopFindDevice();
	saveAllPath();
	System.exit(0);
    }

    private void showDialog(String src)
    {
	Point point = getLocationOnScreen();
	// System.out.println("point.x=" + point.x + ",point.y" + point.y);
	if (dialog.isShowing()) dialog.disposeDialog();
	dialog.showDialogFrame(src, point.x, point.y + s);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	if (e.getSource() == close)
	{
	    closeSys();
	}
	else if (e.getSource() == find)
	{
	    findDevice(true);
	}
	else if (e.getSource() == savePath)
	{
	    saveAllPath();
	    showDialog("保存成功");
	}
	else if (e.getSource() == copy)
	{
	    tfCopyToPcPro.setText("拷贝进度：0%");
	    if (!isSetPath())
	    {
		return;
	    }
	    // 设备未连接
	    if (!deviceConnect())
	    {
		adbDemo.showDialog(NO_FOUND_DEV_CONNECT);
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    setPathNormalView();
	    Utils.savePath(adbPath, copyPhonePath, cpoyToPcPath, tfCopyToPhonePath.getText());
	    String shellCopy = adbPath + SHELL_PULL + " " + copyPhonePath + " " + cpoyToPcPath;
	    System.out.println("shellCopy==" + shellCopy);
	    pcClient.adbShell(shellCopy, ShellType.COPY_TO_PC);
	    return;
	}
	else if (!deviceConnect())
	{
	    adbDemo.showDialog(NO_FOUND_DEV_CONNECT);
	    return;
	}
	else if (e.getSource() == chooseAPK)
	{
	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    jfc.showDialog(new JLabel(), "选择");
	    File file = jfc.getSelectedFile();
	    if (file != null)
	    {
		tfAPKPath.setText(file.getAbsolutePath());// 选择后得到file
		Utils.setRemindTF(tfRemindInfo, "", false);
		tfAPKPath.setBackground(colorNor);
	    }
	}
	else if (e.getSource() == btnChoosePcFile)
	{
	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    jfc.showDialog(new JLabel(), "选择");
	    File file = jfc.getSelectedFile();
	    if (file != null)
	    {
		long lenght = file.length();
		System.out.println("file lenght==" + lenght);
		if (lenght <= 0)
		{
		    adbDemo.showDialog("文件异常，请替换中文路径");
		    return;
		}
		// 文件大小不能超过100M，文件太大的话拷贝会很慢
		if (lenght > 100 * 1024 * 1024)
		{
		    adbDemo.showDialog("文件超过100M，文件过大，无法复制");
		    return;
		}
		tfCopyToPhoneFile.setText(file.getAbsolutePath());// 选择后得到file
		Utils.setRemindTF(tfRemindInfo, "", false);
		tfCopyToPhoneFile.setBackground(colorNor);
	    }
	}
	else if (e.getSource() == install)
	{
	    tfInstallPro.setText("安装进度:0%");
	    setPathNormalView();
	    if (!deviceConnect())
	    { // 设备未连接
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    String apkPath = Utils.getPathProperty(tfAPKPath.getText(), false);
	    if (apkPath.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "请选择apk", true);
		tfAPKPath.setBackground(colorErr);
		return;
	    }
	    Utils.setRemindTF(tfRemindInfo, "", false);
	    tfAPKPath.setBackground(colorNor);
	    // 安装apk
	    String shell = adbPath + SHELL_INSTALL + " " + apkPath;
	    System.out.println("install==" + shell);
	    pcClient.adbShell(shell, ShellType.INSTALL_APK);
	    // 安装到指定设备：adb -s HT9BYL904399 install apk路径
	}
	else if (e.getSource() == btnCopyToPhoneStart)
	{
	    tfCopyToPhonePro.setText("拷贝进度：0%");
	    setPathNormalView();
	    if (!deviceConnect())
	    { // 设备未连接
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    String copyToPhonePath = Utils.getPathProperty(tfCopyToPhonePath.getText(), false);
	    String copyToPhoneFile = tfCopyToPhoneFile.getText();
	    if (copyToPhonePath.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "请配置拷贝到手机的文件夹路径", true);
		tfCopyToPhonePath.setBackground(colorErr);
		return;
	    }
	    if (copyToPhoneFile.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "请选择文件", true);
		tfCopyToPhoneFile.setBackground(colorErr);
		return;
	    }
	    Utils.setRemindTF(tfRemindInfo, "", false);
	    tfCopyToPhoneFile.setBackground(colorNor);
	    tfCopyToPhonePath.setBackground(colorNor);
	    // 二 从电脑端向手机复制文件
	    // 输入: adb pull 电脑路径 手机存储路径
	    // adb push /Users/xxxx/xxx.txt /sdcard/xxx
	    String cpoyToPhoneFilePath = Utils.getPathProperty(tfCopyToPhoneFile.getText(), false);
	    String shell = adbPath + SHELL_PUSH + " " + cpoyToPhoneFilePath + " " + copyToPhonePath;
	    System.out.println("copy to phone shell==" + shell);
	    pcClient.adbShell(shell, ShellType.COPY_TO_PHONE);
	}
    }

    private static void findDevice(boolean isWhile)
    {
	System.out.println("run findDevice");
	findDevCount = 0;
	setPathNormalView();
	String shell = adbPath + SHELL_FIND_DEV;
	System.out.println("shell on deo()==" + shell);
	System.out.println("shell adbPath==" + adbPath);
	if (!isWhile)
	{
	    pcClient.adbShell(shell, ShellType.FIND_DEVICE);
	}
	else
	{
	    if (!pcClient.isFindDevRunning()) pcClient.findDevice(shell);
	}
    }

    private static void setPathNormalView()
    {
	Utils.setRemindTF(tfRemindInfo, "", false);
	// tfAdbPath.setBackground(colorNor);
	tfCopyToPcPath.setBackground(colorNor);
	tfCopyPhonePath.setBackground(colorNor);
    }

    private static String adbPath = "";
    private static String cpoyToPcPath = "";
    private static String copyPhonePath = "";

    private static void getPath()
    {
	// adbPath = Utils.getPathProperty(tfAdbPath.getText(), true);
	cpoyToPcPath = Utils.getPathProperty(tfCopyToPcPath.getText(), false);
	copyPhonePath = Utils.getPathProperty(tfCopyPhonePath.getText(), false);
    }

    private static boolean isSetPath()
    {
	getPath();
	// if (adbPath.isEmpty()) {
	// setRemindTF("请配置adb路径", true);
	// tfAdbPath.setBackground(colorErr);
	// return false;
	// }
	if (cpoyToPcPath.isEmpty())
	{
	    Utils.setRemindTF(tfRemindInfo, "请配置拷贝到电脑的路径", true);
	    tfCopyToPcPath.setBackground(colorErr);
	    return false;
	}
	if (copyPhonePath.isEmpty())
	{
	    Utils.setRemindTF(tfRemindInfo, "请配置要拷贝手机文件的路径", true);
	    tfCopyPhonePath.setBackground(colorErr);
	    return false;
	}
	return true;
    }

    private static void saveAllPath()
    {
	String cpoyToPcPath = tfCopyToPcPath.getText();
	String copyPhonePath = tfCopyPhonePath.getText();
	String copyToPhonePath = tfCopyToPhonePath.getText();
	Utils.savePath(adbPath, copyPhonePath, cpoyToPcPath, copyToPhonePath);
    }
}
