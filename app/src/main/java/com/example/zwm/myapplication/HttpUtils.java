package com.example.zwm.myapplication;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 进行http请求工具包
 * Created by CN on 2017/8/19.
 */

public class HttpUtils {

    /**
     * 使用HttpURLConnection进行get请求
     * @param urlPath
     * @param params
     * @return
     */
    public static String get(String urlPath, String params) {
        try {
            urlPath += "?" + params;
            Log.i("URL", urlPath);

            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");

            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                int len = 0;
                byte[] buf = new byte[128];
                while((len = is.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                return sb.toString();
//                return IOUtils.toString(is);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用HttpURLConnection进行post请求
     * @param urlPath
     * @param json
     * @return
     */
    public static String post(String urlPath, String json){
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");

            // 设置请求的超时时间
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
//            // 设置请求的头
//            conn.setRequestProperty("User-Agent",
//                "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("utf-8"));
            os.flush();
            os.close();

            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                int len = 0;
                byte[] buf = new byte[128];
                while((len = is.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询快递
     * @param expressNum
     * @return
     */
    public static String getExpress(String expressNum) {
        try {
            String urlPath = "http://apis.baidu.com/kuaidicom/express_api/express_api?muti=0&order=desc&nu="+expressNum;
            Log.i("URL", urlPath);

            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("apikey", "d384a0990655462d92ce672108ea6fc8");

            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                int len = 0;
                byte[] buf = new byte[128];
                while((len = is.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }

                Log.i("查询快递", sb.toString());
                return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}