package szu.wifichat.android.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SdCardHelper
{
    /**
     * 判断SdCard卡是否存在，并且是否具有读写权限
     *
     * @return
     */
    public static boolean Exist()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * @return 获取sdCard根据路径
     */
    public static String getSdCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 读取文件内容
     *
     * @param file
     *            目标文件
     * @return 返回文件内容
     * @throws IOException
     */
    public static byte[] readFile(File file) throws IOException
    {
        byte[] fileContent = null;
        if (file.exists())
        {
            FileInputStream inputStream = null;
            try
            {

                inputStream = new FileInputStream(file);
                fileContent = new byte[inputStream.available()];
                inputStream.read(fileContent);
            }
            finally
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
        }

        return fileContent;
    }

}
