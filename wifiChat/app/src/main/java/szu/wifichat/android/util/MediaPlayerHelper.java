package szu.wifichat.android.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * 媒体播放帮助类
 *
 * @author liaomd
 *
 */
public class MediaPlayerHelper
{
    private static final String LOG_TAG = "MediaPlayerHelper";
    private static Context _Context = null;
    private static MediaPlayer _mp = new MediaPlayer();

    public enum MediaPathType
    {
        FullPath, Resource, String;
    }

    public static void setContext(Context context)
    {
        _Context = context;
    }

    public static MediaPlayer getMediaPlayer()
    {
        if (_mp == null)
        {
            _mp = new MediaPlayer();
        }
        else
        {
            if (_mp.isPlaying())
            {
                _mp.stop();
            }
            _mp.reset();
        }
        return _mp;
    }

    public static void playAssetFile(String fileName)
    {
        MediaPlayer mp = getMediaPlayer();
        AssetFileDescriptor afd;
        try
        {
            afd = _Context.getAssets().openFd(fileName);
            if (mp.isPlaying())
            {
                // mp.stop();
                return;
            }
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare(); // 准备
            mp.start(); // 准备
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "playAssetFile() failed");
        }
    }

    public static void playResFile(int fileId)
    {

        try
        {
            MediaPlayer mp = MediaPlayer.create(_Context, fileId);
            if (mp.isPlaying())
            {
                mp.stop();
            }
            mp.prepare(); // 准备
            mp.start(); // 准备
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "playAssetFile() failed");
        }
    }

    public static void playFile(String fileFullPath)
    {
        MediaPlayer mp = new MediaPlayer();
        try
        {
            mp.setDataSource(fileFullPath);
            mp.prepare(); // 准备
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "playAssetFile() failed");
        }
    }
}
