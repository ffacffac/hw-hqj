package szu.wifichat.android.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作帮助类
 *
 */
public class FileHelper
{
    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return
     */
    public static Boolean isFileExist(String filePath)
    {
        File file = new File(filePath);
        if (file.exists() && file.isFile())
        {
            return true;
        }

        return false;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param dirPath 文件路径
     * @return
     */
    public static Boolean isDirExist(String dirPath)
    {
        File file = new File(dirPath);
        if (file.exists() && file.isDirectory())
        {
            return true;
        }

        return false;
    }

    /**
     * 创建一个文件
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static Boolean createFile(String fileName) throws IOException
    {
        File file = new File(fileName);
        return file.createNewFile();
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            return file.delete();
        }
        return false;
    }

    /**
     * 复制多个文件
     *
     * @param dirPath
     * @param toDir
     * @throws IOException
     */
    public static void copyManyFile(String dirPath, String toDir) throws IOException
    {
        File dirFile = new File(dirPath);
        File[] files = dirFile.listFiles();
        for (File file : files)
        {
            File toDirFile = new File(toDir + file.getName());
            copyFile(file, toDirFile, true);
        }
    }

    /**
     * 拷贝SdCard卡上的文件
     *
     * @param fromFile 源文件
     * @param toFile 目标文件
     * @param rewrite 是否可重写
     * @throws IOException 拷贝文件时的IO异常
     */
    public static void copyFile(File fromFile, File toFile, Boolean rewrite) throws IOException
    {
        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead())
        {
            return;
        }

        if (!toFile.getParentFile().exists())
        {
            toFile.getParentFile().mkdirs();
        }

        if (toFile.exists() && rewrite)
        {
            toFile.delete();
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try
        {//
            inputStream = new FileInputStream(fromFile);
            outputStream = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = inputStream.read(bt)) > 0)
            {
                outputStream.write(bt, 0, c); // 将内容写到新文件当中
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
            if (outputStream != null)
            {
                outputStream.close();
            }
        }
    }

    /**
     * 拼装文件路径
     *
     * @param dir 文件所在的目录
     * @param fileName 文件名
     * @return 返回文件完成路径
     */
    public static String concatFilePath(String dir, String fileName)
    {
        String filePath = "";
        if (dir.endsWith("\\") || dir.endsWith("/"))
        {
            filePath = dir + fileName;
        }
        else
        {
            filePath = dir + "\\" + fileName;
        }
        return filePath;
    }

    public static byte[] toBytes(File file)
    {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1;)
            {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] File2Bytes(File file)
    {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1;)
            {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        }
        catch (IOException e)
        {
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
    public static void writeStrToFile(String path, String name, String strContent)
    {
        BufferedWriter bw = null;
        try
        {
            File file = new File(path);
            File fileName = new File(path, name);
            if (!file.exists())
            {
                file.mkdirs();
            }
            if (!fileName.exists())
            {
                fileName.createNewFile();
            }
            FileWriter fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(strContent);
            bw.flush();
        }
        catch (IOException ioe)
        {}
        finally
        {
            if (bw != null)
            {
                try
                {
                    bw.close();
                }
                catch (IOException e)
                {}
            }
        }
    }

    /**
     * 1\可先创建文件的路径
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath)
    {
        File file = null;
        try
        {
            file = new File(filePath);
            if (!file.exists())
            {
                file.mkdir();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 2\然后在创建文件名就不会在报该错误
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File getFilePath(String filePath, String fileName)
    {
        File file = null;
        makeRootDirectory(filePath);
        try
        {
            file = new File(filePath + fileName);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean deleteDirFile(String delpath) throws FileNotFoundException, IOException
    {
        File file = new File(delpath);
        if (!file.exists())
        {
            return true;
        }
        if (!file.isDirectory())
        {
            file.delete();
        }
        else if (file.isDirectory())
        {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                File delfile = fileList[i];
                if (!delfile.isDirectory())
                {
                    delfile.delete();
                }
                else if (delfile.isDirectory())
                {
                    deleteDirFile(fileList[i].getPath());
                }
            }
            file.delete();
        }
        return true;
    }
}
