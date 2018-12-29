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
    public static final String ADB_PATH = "F:/ruanjian/android/AndroidSdk/platform-tools/";// adb����·��
    public static final String COPY_PHONE_PATH = "sdcard/sdms/dbs";
    public static final String COPY_TO_PC_PATH = "F:/MyDB";
    public static final String COPY_TO_PHONE_PATH = "sdcard/";
    public static final String SHELL_FIND_DEV = "adb devices";
    public static final String SHELL_PULL = "adb pull";
    public static final String SHELL_PUSH = "adb push";
    public static final String SHELL_INSTALL = "adb install";
    private static PcClient pcClient;
    private static ADBDemo adbDemo;

    private int s = 600;// ���ڴ�С
    private int btnW = 80;// ͨ�ð�ť��
    private int btnH = 40;
    private Button savePath = new Button("����·��");
    private Button find = new Button("�����豸");
    private Button copy = new Button("��ʼ����");
    private Button close = new Button("�ر�");
    private Button devName = new Button("�����豸�б�");
    private Button chooseAPK = new Button("ѡ��apk");
    private Button install = new Button("��װapk");
    private static JTextField tfInstallPro = new JTextField("��װ����:0%");
    private static JTextField tfCopyToPhonePro = new JTextField("�������ֻ�����:0%");
    private static JTextField findDevTextInfo = new JTextField();
    private static int findDevCount = 0;// �����豸����

    // private Button btnAdbPathName = new Button("���Զ�adb·����");// adb·��
    // private static JTextField tfAdbPath = new JTextField("");// adb·��

    // �������ֻ���·��
    private Button btnCopyToPhoneStart = new Button("�������ֻ�");
    private Button btnCopyToPhone = new Button("�������ֻ���·��");
    private static JTextField tfCopyToPhonePath = new JTextField(COPY_TO_PHONE_PATH);
    private Button btnChoosePcFile = new Button("ѡ������ļ�");
    private static JTextField tfCopyToPhoneFile = new JTextField();
    // ���������Ե�·��
    private Button btnCopyToPcPath = new Button("���������Ե�·����");
    private static JTextField tfCopyToPcPath = new JTextField("");
    // �����ֻ��ļ���·��
    private Button btnCopyPhonePath = new Button("�����ֻ��ļ�·����");
    private static JTextField tfCopyPhonePath = new JTextField(COPY_PHONE_PATH);
    private static JTextField tfCopyToPcPro = new JTextField();// ��������
    // JTextArea֧�ֻ���ת��������ǲ�֧���ı�����
    // JTextField֧���ı����У�����֧�ֻ���
    private static JTextField tfRemindInfo = new JTextField("�����ú����·��");// ��ʾ�ı�
    private JTextField tfAPKPath = new JTextField();// ��װapk��·��

    private DialogFrame dialog = new DialogFrame();

    private int layoutMarginR = 200;
    private int layoutMarginT = 200;// ������ʼ��ʾλ��

    public static void main(String[] args)
    {
	pcClient = new PcClient();
	pcClient.setADBListener(adbListener);
	adbDemo = new ADBDemo();
	Utils.setWindowListener(adbDemo, adbListener);
	adbDemo.init();
	findDevice(true);// ������ʱ���Ȳ����豸�Ƿ�����
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
    private int tfPathW = 190;// ͨ���ı�����

    // /**
    // * ��ʾͼƬ
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
     * ����adb��·��
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

    private int installMarginTop = 400;// ��װapk��ť���봰�ڶ���

    /**
     * ���ÿ������ֻ���·��
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
     * ���ÿ������ֻ���·��
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

	// ѡ��Ҫ�������ֻ����ļ�
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
	this.setTitle("�ֻ�����");
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
	tfCopyToPcPro.setText("�������ȣ�0%");
	tfCopyToPcPro.setHorizontalAlignment(JTextField.CENTER);

	this.add(tfRemindInfo);
	tfRemindInfo.setBounds(50, btnAdbPathH * 6 + 15, 250, 120);
	tfRemindInfo.setBackground(colorErr);
	tfRemindInfo.setHorizontalAlignment(JTextField.CENTER);
	Utils.setRemindTF(tfRemindInfo, "����·������ȷ", false);

	this.add(devName);
	devName.addActionListener(this);
	devName.setBounds(380, 5, 200, 35);
	devName.setBackground(Color.decode("#5C6667"));
	devName.setFont(new Font("�豸�б�", 0, 20));
	devName.setForeground(Color.decode("#FFFFFF"));

	setInstallText();
	setCopyToPhonePathText();

	this.add(close);
	close.addActionListener(this);
	close.setBounds(480, 500, 100, 50);
	close.setBackground(Color.decode("#1CDBEE"));

	this.add(findDevTextInfo);
	findDevTextInfo.setBounds(380, 40, 200, 350);
	// ���ô��ڵ�λ�úʹ�С
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
		sb.append(NO_FOUND_DEV).append("��").append("������").append(findDevCount);
		findDevTextInfo.setText(sb.toString());
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    setPathNormalView();
	    StringBuilder sb = new StringBuilder();
	    for (String name : devices)
	    {
		sb.append("\n").append("�豸��").append(name).append("\n");
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
		// �����ֻ�������������
		tfCopyToPcPro.setText("�������ȣ�" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("�����ɹ�");
	    }
	    else if (ShellType.INSTALL_APK == shellType)
	    {
		// ��װapk����
		tfInstallPro.setText("��װ���ȣ�" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("��װ�ɹ�");
	    }
	    else if (ShellType.COPY_TO_PHONE == shellType)
	    {
		// ���������ļ����ֻ��Ľ���
		tfCopyToPhonePro.setText("�������ֻ����ȣ�" + pro);
		if (pro.equals("100%")) adbDemo.showDialog("�����ɹ�");
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
	    JOptionPane.showMessageDialog(null, shellSrc + "\n" + "�쳣��Ϣ:" + errMsg);
	}

	@Override
	public void close()
	{
	    adbDemo.closeSys();
	}
    };

    private static final String NO_FOUND_DEV = "û���ҵ��豸";
    private static final String NO_FOUND_DEV_CONNECT = "�豸δ���ӣ�����USB�������ֻ�";

    /**
     * �Ƿ����豸������
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
	    showDialog("����ɹ�");
	}
	else if (e.getSource() == copy)
	{
	    tfCopyToPcPro.setText("�������ȣ�0%");
	    if (!isSetPath())
	    {
		return;
	    }
	    // �豸δ����
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
	    jfc.showDialog(new JLabel(), "ѡ��");
	    File file = jfc.getSelectedFile();
	    if (file != null)
	    {
		tfAPKPath.setText(file.getAbsolutePath());// ѡ���õ�file
		Utils.setRemindTF(tfRemindInfo, "", false);
		tfAPKPath.setBackground(colorNor);
	    }
	}
	else if (e.getSource() == btnChoosePcFile)
	{
	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    jfc.showDialog(new JLabel(), "ѡ��");
	    File file = jfc.getSelectedFile();
	    if (file != null)
	    {
		long lenght = file.length();
		System.out.println("file lenght==" + lenght);
		if (lenght <= 0)
		{
		    adbDemo.showDialog("�ļ��쳣�����滻����·��");
		    return;
		}
		// �ļ���С���ܳ���100M���ļ�̫��Ļ����������
		if (lenght > 100 * 1024 * 1024)
		{
		    adbDemo.showDialog("�ļ�����100M���ļ������޷�����");
		    return;
		}
		tfCopyToPhoneFile.setText(file.getAbsolutePath());// ѡ���õ�file
		Utils.setRemindTF(tfRemindInfo, "", false);
		tfCopyToPhoneFile.setBackground(colorNor);
	    }
	}
	else if (e.getSource() == install)
	{
	    tfInstallPro.setText("��װ����:0%");
	    setPathNormalView();
	    if (!deviceConnect())
	    { // �豸δ����
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    String apkPath = Utils.getPathProperty(tfAPKPath.getText(), false);
	    if (apkPath.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "��ѡ��apk", true);
		tfAPKPath.setBackground(colorErr);
		return;
	    }
	    Utils.setRemindTF(tfRemindInfo, "", false);
	    tfAPKPath.setBackground(colorNor);
	    // ��װapk
	    String shell = adbPath + SHELL_INSTALL + " " + apkPath;
	    System.out.println("install==" + shell);
	    pcClient.adbShell(shell, ShellType.INSTALL_APK);
	    // ��װ��ָ���豸��adb -s HT9BYL904399 install apk·��
	}
	else if (e.getSource() == btnCopyToPhoneStart)
	{
	    tfCopyToPhonePro.setText("�������ȣ�0%");
	    setPathNormalView();
	    if (!deviceConnect())
	    { // �豸δ����
		Utils.setRemindTF(tfRemindInfo, NO_FOUND_DEV_CONNECT, true);
		return;
	    }
	    String copyToPhonePath = Utils.getPathProperty(tfCopyToPhonePath.getText(), false);
	    String copyToPhoneFile = tfCopyToPhoneFile.getText();
	    if (copyToPhonePath.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "�����ÿ������ֻ����ļ���·��", true);
		tfCopyToPhonePath.setBackground(colorErr);
		return;
	    }
	    if (copyToPhoneFile.isEmpty())
	    {
		Utils.setRemindTF(tfRemindInfo, "��ѡ���ļ�", true);
		tfCopyToPhoneFile.setBackground(colorErr);
		return;
	    }
	    Utils.setRemindTF(tfRemindInfo, "", false);
	    tfCopyToPhoneFile.setBackground(colorNor);
	    tfCopyToPhonePath.setBackground(colorNor);
	    // �� �ӵ��Զ����ֻ������ļ�
	    // ����: adb pull ����·�� �ֻ��洢·��
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
	// setRemindTF("������adb·��", true);
	// tfAdbPath.setBackground(colorErr);
	// return false;
	// }
	if (cpoyToPcPath.isEmpty())
	{
	    Utils.setRemindTF(tfRemindInfo, "�����ÿ��������Ե�·��", true);
	    tfCopyToPcPath.setBackground(colorErr);
	    return false;
	}
	if (copyPhonePath.isEmpty())
	{
	    Utils.setRemindTF(tfRemindInfo, "������Ҫ�����ֻ��ļ���·��", true);
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
