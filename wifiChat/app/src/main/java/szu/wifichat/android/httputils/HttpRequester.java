package szu.wifichat.android.httputils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import szu.wifichat.android.util.FileHelper;

/**
 * HTTP请求对象
 */
public class HttpRequester
{
    private String defaultContentEncoding;

    public HttpRequester()
    {
        this.defaultContentEncoding = Charset.defaultCharset().name();
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGet(String urlString) throws IOException
    {
        return this.send(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGet(String urlString, Map<String, String> params) throws IOException
    {
        return this.send(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendGet(String urlString, Map<String, String> params,
                                Map<String, String> propertys) throws IOException
    {
        return this.send(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPost(String urlString) throws IOException
    {
        return this.send(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param content 传送的参数
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPost(String urlString, String content) throws IOException
    {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("contentType", "UTF-8");
        urlConnection.getOutputStream().write(content.getBytes("UTF-8"));
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();
        return this.makeContent(urlString, urlConnection);
    }

    public HttpResponse sendPut(String urlString, String content) throws IOException
    {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("PUT");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("contentType", "UTF-8");
        urlConnection.getOutputStream().write(content.getBytes("UTF-8"));
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();
        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPost(String urlString, Map<String, String> params) throws IOException
    {
        return this.send(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpResponse sendPost(String urlString, Map<String, String> params,
                                 Map<String, String> propertys) throws IOException
    {
        return this.send(urlString, "POST", params, propertys);
    }

    public HttpResponse sendPost(String urlString, File file) throws IOException
    {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("contentType", "UTF-8");
        byte[] bytes = FileHelper.File2Bytes(file);
        urlConnection.getOutputStream().write(bytes);
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    private HttpResponse send(String urlString, String method, Map<String, String> parameters,
                              Map<String, String> propertys) throws IOException
    {
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null)
        {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet())
            {
                if (i == 0) param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);

        if ((method.toUpperCase().equals("GET")))
        {
            urlConnection.setDoOutput(false);
        }
        else
        {
            urlConnection.setDoOutput(true);
        }

        urlConnection.setConnectTimeout(12 * 1000);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("contentType", "UTF-8");

        if (parameters == null || parameters.size() == 0)
        {
            urlConnection.setRequestProperty("Content-Length", "0");
        }
        else
        {
            int length = 0;
            Collection<String> map = parameters.values();
            if (map != null && map.size() > 0)
            {
                for (String str : map)
                {
                    length += str.length();
                }
                urlConnection.setRequestProperty("Content-Length", length + "");
            }
        }

        if (propertys != null) for (String key : propertys.keySet())
        {
            urlConnection.addRequestProperty(key, propertys.get(key));
        }

        if (method.equalsIgnoreCase("POST") && parameters != null)
        {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet())
            {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }
        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 得到响应对象
     *
     * @param urlConnection
     * @return 响应对象
     * @throws IOException
     */
    private HttpResponse makeContent(String urlString, HttpURLConnection urlConnection)
            throws IOException
    {
        HttpResponse httpResponser = new HttpResponse();
        try
        {
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                Log.d("tag", "http response code is " + urlConnection.getResponseCode());
                InputStream eIn = urlConnection.getErrorStream();
                BufferedReader eb = new BufferedReader(new InputStreamReader(eIn, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = eb.readLine();
                while (line != null)
                {
                    sb.append(line);
                    line = eb.readLine();
                }

                System.out.println(sb.toString());
            }

            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null)
            {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            httpResponser.urlString = urlString;
            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();
            httpResponser.content = new String(temp.toString().getBytes());
            httpResponser.contentEncoding = urlConnection.getContentEncoding();
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();

            return httpResponser;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding()
    {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding)
    {
        this.defaultContentEncoding = defaultContentEncoding;
    }
}
