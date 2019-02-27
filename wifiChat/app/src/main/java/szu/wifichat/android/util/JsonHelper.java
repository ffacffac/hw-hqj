package szu.wifichat.android.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Json帮助类
 *
 * @author maj
 *
 */
public class JsonHelper
{
    private static JsonHelper mJsonHelper;
    private Gson mGson;

    private JsonHelper()
    {
    }

    public static JsonHelper getInstance()
    {
        if (null == mJsonHelper)
        {
            mJsonHelper = new JsonHelper();
        }

        return mJsonHelper;
    }

    /**
     * 对象转成json格式字符串
     *
     * @param obj
     * @return
     */
    public String toJson(Object obj)
    {
        return getGson().toJson(obj);
    }

    /**
     * json转成对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public <T> T fromJson(String json, Class<T> classOfT)
    {
        return getGson().fromJson(json, classOfT);
    }

    /**
     * json转成对象
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public <T> T fromJson(String json, Type typeOfT)
    {
        return getGson().fromJson(json, typeOfT);
    }

    private Gson getGson()
    {
        if (null == mGson)
        {
            mGson = new Gson();
        }

        return mGson;
    }
}
