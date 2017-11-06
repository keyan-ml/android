package com.example.zwm.myapplication.util;


import android.util.Log;

import com.example.zwm.myapplication.model.PublicVariable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
//            String cookieval = conn.getHeaderField("set-cookie");
//            String sessionid = null;
//            if (cookieval != null) {
//                sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//                Log.d("MyDebug", "sessionid: " + sessionid);
//            }
//            if (sessionid != null) {
//                conn.setRequestProperty("cookie", sessionid);
//                Log.d("MyDebug", "request中设置sessionid: " + sessionid);
//            }

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
//            String cookieval = conn.getHeaderField("Set-Cookie");
//            String sessionid = null;
//            if (cookieval != null) {
//                sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//                Log.d("MyDebug", "sessionid: " + sessionid);
//            }
            if (PublicVariable.sessionid != null) {
                conn.setRequestProperty("Cookie", "JSESSIONID=" + PublicVariable.sessionid);
                Log.d("MyDebug", "request中设置sessionid: " + PublicVariable.sessionid);
            }

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
//                byte[] buf = new byte[128];
//                while((len = is.read(buf)) != -1) {
//                    sb.append(new String(buf, 0, len));
//                }
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                char[] buf = new char[1024];
                while ((len = isr.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                is.close();
                isr.close();
                return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String post(String urlPath, String json, String encoding){
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", encoding);

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


//    public static String getExpress(String expressNum) {
//        try {
//            String urlPath = "http://apis.baidu.com/kuaidicom/express_api/express_api?muti=0&order=desc&nu="+expressNum;
//            Log.i("URL", urlPath);
//
//            URL url = new URL(urlPath);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Charset", "UTF-8");
//            conn.setRequestProperty("apikey", "d384a0990655462d92ce672108ea6fc8");
//
//            if(conn.getResponseCode() == 200){
//                InputStream is = conn.getInputStream();
//                StringBuilder sb = new StringBuilder();
//                int len = 0;
//                byte[] buf = new byte[128];
//                while((len = is.read(buf)) != -1) {
//                    sb.append(new String(buf, 0, len));
//                }
//
//                Log.i("查询快递", sb.toString());
//                return sb.toString();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

}