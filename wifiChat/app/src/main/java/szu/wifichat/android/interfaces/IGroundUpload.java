package szu.wifichat.android.interfaces;

import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Created by huangqj on 2017-06-19.
 */

public interface IGroundUpload {
    /**
     * 上传失败
     *
     * @param request
     * @param e
     */
    public void onFailure(Request request, IOException e);

    /**
     * 上传成功
     *
     * @param response
     * @throws IOException
     */
    public void onResponse(String response) throws IOException;
}
