package com.hw.adb;

public class Eenum
{

    public enum ShellType
    {
	FIND_DEVICE(1, "查找设备"), COPY_TO_PC(2, "拷贝手机文件到电脑"), INSTALL_APK(3,
		"安装apk"), COPY_TO_PHONE(4, "拷贝电脑文件到手机");

	ShellType(int type, String dec)
	{}
    }
}
