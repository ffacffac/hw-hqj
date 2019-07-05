package com.hw.baselibrary.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文件操作帮助类
 *
 * @author huangqj
 */
public class FileUtil {
    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return true--存在
     */
    public static Boolean isFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return true;
        }

        return false;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param dirPath 文件路径
     * @return true--存在
     */
    public static Boolean isDirExist(String dirPath) {
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory()) {
            return true;
        }

        return false;
    }

    /**
     * 创建一个文件
     *
     * @param fileName 文件名
     * @return true--创建成功
     * @throws IOException 异常
     */
    public static Boolean createFile(String fileName) throws IOException {
        File file = new File(fileName);
        return file.createNewFile();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径：.../dir/file.jpg
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.deleteOnExit();
    }

    /**
     * 拷贝SdCard卡上的文件
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @param rewrite  是否可重写
     * @throws IOException 拷贝文件时的IO异常
     */
    public static void copyFile(File fromFile, File toFile, Boolean rewrite) throws IOException {
        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(fromFile);
            outputStream = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = inputStream.read(bt)) > 0) {
                // 将内容写到新文件当中
                outputStream.write(bt, 0, c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 拼装文件路径
     *
     * @param dir      文件所在的目录
     * @param fileName 文件名
     * @return 返回文件完成路径
     */
    public static String concatFilePath(String dir, String fileName) {
        String filePath = "";
        if (dir.endsWith("\\") || dir.endsWith("/")) {
            filePath = dir + fileName;
        } else {
            filePath = dir + "\\" + fileName;
        }
        return filePath;
    }

    public static byte[] toBytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * file转换成 byte[]
     *
     * @param file file
     * @return byte
     */
    public static byte[] file2Bytes(File file) {
        int byteSize = 1024;
        byte[] b = new byte[byteSize];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteSize);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串写入文件
     *
     * @param path 文件路径
     * @param name 文件名
     */
    public static void writeStrToFile(String path, String name, String strContent) {
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            File fileName = new File(path, name);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
            FileWriter fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(strContent);
            bw.flush();
        } catch (IOException ioe) {
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 读文件
     *
     * @param path 路径：.../dir/file.txt
     * @return 文件信息
     * @throws IOException 异常
     */
    public static String readFile(String path) throws IOException {
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e("FileUtil", "readFile: ", e);
            throw new IOException();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    private static String[] arrImg = {".jpg", ".png", ".jpeg", ".gif"};
    private static String[] arrVideo = {".mp4", ".m4v", ".3gp", ".wmv"};

    public static boolean isImage(String fileName) {
        String newFileName = fileName.toLowerCase();
        for (String suffix : arrImg) {
            if (newFileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideo(String fileName) {
        String newFileName = fileName.toLowerCase();
        for (String suffix : arrVideo) {
            if (newFileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     * 先删除文件，在删除文件夹
     *
     * @param delpath 文件夹路径
     * @return
     * @throws IOException
     */
    public static boolean deleteDirFile(String delpath) throws IOException {
        File file = new File(delpath);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File delfile : fileList) {
                if (!delfile.isDirectory()) {
                    delfile.delete();
                } else if (delfile.isDirectory()) {
                    //递归
                    deleteDirFile(delfile.getPath());
                }
            }
            //删完文件在删文件夹
            file.delete();
        }
        return true;
    }

    /**
     * 重命名后在删除
     *
     * @param delPath 文件夹路径
     */
    public static boolean deleteSDCardFolder(String delPath) throws IOException {
        File delFile = new File(delPath);
        if (!delFile.exists()) return true;
        File to = new File(delFile.getAbsolutePath() + System.currentTimeMillis());
        delFile.renameTo(to);
        if (to.isDirectory()) {
            String[] children = to.list();
            for (String aChildren : children) {
                File temp = new File(to, aChildren);
                if (temp.isDirectory()) {
                    deleteSDCardFolder(temp.getAbsolutePath());
                } else {
                    boolean b = temp.delete();
                    if (!b) {
                        Log.e("deleteSDCardFolder", "DELETE FAIL");
                    }
                }
            }
            return to.delete();
        } else {
            return to.delete();
        }
    }

    /**
     * 删除附件,目录不删除
     *
     * @param delpath 路径文件夹
     * @param prdfix  附件的前缀 如：前缀为gms，则只删除gms_***.jpg的图片
     */
    public static boolean deleteDirFileByPrdfix(String delpath, String prdfix) throws IOException {
        File file = new File(delpath);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory() && file.getName().startsWith(prdfix)) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File delfile : fileList) {
                    if (!delfile.isDirectory() && delfile.getName().startsWith(prdfix)) {
                        delfile.delete();
                    }
                }
            }
        }
        return true;
    }

    /**
     * 删除附件,目录不删除
     *
     * @param delpath 路径文件夹
     */
    public static boolean deleteDirChildFile(String delpath) throws IOException {
        File file = new File(delpath);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File delfile : fileList) {
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    }
                }
            }
        }
        return true;
    }
}
