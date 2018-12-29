package com.hw.adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils
{
    public static final String KEY_ADB = "adb";
    public static final String KEY_PHNOE = "phone";
    public static final String KEY_PC = "pc";
    public static final String KEY_TO_PHONE_PATH = "copyToPhonePath";
    public static final String PATH_SPLIT = ",";
    public static final String KEY_SPLIT = "=";

    // public static void main(String[] args) {
    // try {
    // // String path = getProjectPath() + "/adbPath.txt";
    // // createADBPathFile(path);
    // writeADBPath("F:/ruanjian/hwEclipse/work/ADBDemo123");
    // String adbPath = getAdbPath();
    // System.out.println("adbPath=" + adbPath);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    public static String getProjectPath()
    {
	return System.getProperty("user.dir");
    }

    public static boolean createADBPathFile(String filePath) throws IOException
    {
	File file = new File(filePath);
	if (!file.exists())
	{
	    return file.createNewFile();
	}
	else
	    return true;
    }

    public static void writeADBPath(String src) throws IOException
    {
	if (src == null || src.isEmpty()) return;
	String path = getProjectPath() + "/adbPath.txt";
	if (!createADBPathFile(path)) return;
	File file = new File(path);
	FileWriter fileWriter = new FileWriter(file);
	fileWriter.write(src);
	fileWriter.flush();
	fileWriter.close();
    }

    public static String getAdbPath() throws IOException
    {
	BufferedReader br = null;
	String path = getProjectPath() + "/adbPath.txt";
	File file = new File(path);
	if (!file.exists()) return "";
	FileInputStream fis = new FileInputStream(file);
	br = new BufferedReader(new InputStreamReader(fis));
	StringBuilder sb = new StringBuilder();
	String line;
	while ((line = br.readLine()) != null)
	{
	    sb.append(line);
	}
	br.close();
	fis.close();
	return sb.toString();
    }
}
