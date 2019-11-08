package com.example.networkdemo;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import org.apache.http.NameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    public static String get(String urlPath) {
        InputStream is = null;

        // 1.将url字符串转为URL对象
        try {
            URL url = new URL(urlPath);

            // 2.获得HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 3.设置连接的相关参数
            connection.setRequestMethod("GET");//默认为GET
            connection.setUseCaches(false);// 不使用缓存
            connection.setConnectTimeout(15000);//设置连接超时时间
            connection.setReadTimeout(15000);// 设置读取超时时间
            connection.setRequestProperty("Connection", "keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            connection.setDoInput(true);//设置是否从httpUrlConnection读入);


            // 4.配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection)
                        connection).setSSLSocketFactory(
                        HttpsUtil.getSSLSocketFactory());
            }


            // 5.进行数据的读取，首先判断响应码是否为200
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //获得输入流
                is = connection.getInputStream();

                //包装字节流为字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                //读取数据
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // 6.关闭资源
                is.close();
                connection.disconnect();

                // 7.返回结果
                return response.toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //post请求的网络请求方法
    public static String post(String urlPath, List<NameValuePair> params) {
        //1.处理请求参数
        if (params == null || params.size() == 0) {
            return get(urlPath);
        }
        try {
            String body = getParamString(params);
            byte[] data = body.getBytes();

            // 1.将url字符串转为URL对象
            URL url = new URL(urlPath);

            // 2.获得HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 3.设置连接的相关参数
            connection.setRequestMethod("POST");//默认为GET
            connection.setUseCaches(false);// 不使用缓存
            connection.setConnectTimeout(15000);//设置连接超时时间
            connection.setReadTimeout(15000);// 设置读取超时时间
            connection.setRequestProperty("Connection", "keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            connection.setDoInput(true);//设置是否从httpUrlConnection读入);
            connection.setDoOutput(true);


            // 4.配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection)
                        connection).setSSLSocketFactory(
                        HttpsUtil.getSSLSocketFactory());
            }

            // 5.进行数据的读取，首先判断响应码是否为200
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream os = connection.getOutputStream();
            os.write(data);
            os.flush();

            // 5.进行数据的读取，首先判断响应码是否为200
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //获得输入流
                InputStream is = connection.getInputStream();

                //包装字节流为字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                //读取数据
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // 6.关闭资源
                is.close();
                connection.disconnect();

                // 7.返回结果
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String post(String urlPath, Map<String, String> params) {
        //1.处理请求参数
        if (params == null || params.size() == 0) {
            return get(urlPath);
        }
        try {
            String body = getParamString(params);
            byte[] data = body.getBytes();

            // 1.将url字符串转为URL对象
            URL url = new URL(urlPath);

            // 2.获得HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 3.设置连接的相关参数
            connection.setRequestMethod("POST");//默认为GET
            connection.setUseCaches(false);// 不使用缓存
            connection.setConnectTimeout(15000);//设置连接超时时间
            connection.setReadTimeout(15000);// 设置读取超时时间
            connection.setRequestProperty("Connection", "keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            connection.setDoInput(true);//设置是否从httpUrlConnection读入);
            connection.setDoOutput(true);


            // 4.配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection)
                        connection).setSSLSocketFactory(
                        HttpsUtil.getSSLSocketFactory());
            }

            // 5.进行数据的读取，首先判断响应码是否为200
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream os = connection.getOutputStream();
            os.write(data);

            // 6.进行数据的读取，首先判断响应码是否为200
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                //获得输入流
                InputStream is = connection.getInputStream();

                //包装字节流为字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                //读取数据
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // 7.关闭资源
                os.close();
                is.close();
                connection.disconnect();

                // 8.返回结果
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void downFile(Context context, String strUrl) {
//        String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
//        URL url = null;
//        try {
//            url = new URL(strUrl);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            //配置https的证书
//            if ("https".equalsIgnoreCase(url.getProtocol())) {
//                ((HttpURLConnection) connection).setSSLSocketFactory(HttpsUtil.getSSLSocketFactory());
//            }
//            connection.setRequestProperty("Connection", "keep-Alive");
//
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream is = connection.getInputStream();
//                writeFile(Context, fileName, is);
//            }
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static final String BOUNDARY = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");

    public static String uploadFile(String strUrl, String fileName) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(HttpsUtil.getSSLSocketFactory());
            }
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            connection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            connection.setRequestProperty("Content-Type", "text/x-markdown;charset=utf-8;boundary=" + BOUNDARY);

            // 设置DataOutputStream，上传请求头信息
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            StringBuilder builder = new StringBuilder();

            // 上传文件信息
            FileInputStream fis = new FileInputStream(fileName);
            int bufferSize = 1024;
            byte[] tmp = new byte[bufferSize];
            int length = -1;
            while ((length = fis.read(tmp)) != -1) {
                ds.write(tmp, 0, length);
            }

            ds.flush();
            fis.close();
            ds.close();

            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is" + connection.getResponseCode() + ",message:" + connection);
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String temp;
                builder = new StringBuilder();
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((temp = reader.readLine()) != null) {
                    builder.append(temp).append("\n");
                }
                reader.close();
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //组装请求参数
    private static String getParamString(List<NameValuePair> pairs) throws
            UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (NameValuePair pair : pairs) {
            if (!TextUtils.isEmpty(builder)) {
                builder.append("δ");
            }
            builder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return builder.toString();
    }

    public static void writeFile(Context context,String fileName,InputStream is)throws Exception{
        String path = context.getFilesDir().getAbsolutePath();
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
        File file = new File(path,fileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        while((len = bis.read(buffer)) != -1){
            fos.write(buffer,0,len);

        }

        fos.flush();
        fos.close();
        bis.close();
    }

    //组装请求参数
    private static String getParamString(Map<String,String> params) throws
            UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> param = iterator.next();
            String key = URLEncoder.encode(param.getKey(), "UTF-8");
            String value = URLEncoder.encode(param.getValue(), "UTF-8");
            builder.append(key).append('=').append(value);
            if(iterator.hasNext()){
                builder.append('&');
            }
        }
        return builder.toString();
    }

}

