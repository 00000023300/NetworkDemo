package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.networkdemo.NetworkUtils.uploadFile;


public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OkHttpActivity";
    // 请求的URL
    private static final String IP_BASE_URL = "http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP_URL = IP_BASE_URL + "?ip=112.2.253.197";
    private static final String UPLOAD_FILE_URL = "https://api.github.com/markdown/raw";
    private static final String DOWNLOAD_URL ="https://github.com/zhayh/AndroidExample/blob/master/README.md" ;


    private TextView tvResult;
    private ImageView imageView;
    private ScrollView scrollView;
    private OkHttpClient client;
    private Button get,post,handin,download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);



        tvResult = findViewById(R.id.text);
        imageView = findViewById(R.id.image);
        scrollView= findViewById(R.id.scrollView);
        get = findViewById(R.id.get);
        post = findViewById(R.id.post);
        handin = findViewById(R.id.handin);
        download = findViewById(R.id.download);

        get.setOnClickListener(this);
        post.setOnClickListener(this);
        handin.setOnClickListener(this);
        download.setOnClickListener(this);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                //支持HTTPS请求，跳过证书验证
                .sslSocketFactory(HttpsUtil.getSSLSocketFactory(), (X509TrustManager) HttpsUtil.getTrustManager()[0])
                .hostnameVerifier(new HttpsUtil.TrustAllHostnameVerifier())
                .build();


    }

    @Override
    public void onClick(View v) {
        String path = getFilesDir().getAbsolutePath();
        switch (v.getId()) {
            case R.id.get:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                get(IP_URL );
                break;

            case R.id.post:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Map<String, String> params = new HashMap<>();
                params.put("ip", "112.2.253.197");
                post(IP_URL, params);

                break;

            case R.id.handin:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                String fileName = path + File.separator+"readme.md";
                uploadFile(UPLOAD_FILE_URL,fileName);
                break;

            case R.id.download:
                downFile(DOWNLOAD_URL,path);

                break;
        }

    }

    // get异步请求是在子线程中执行的，需要切换到主线程才能更新UI
    private void get(String strUrl) {
        // 1. 构造Request
        Request request = new Request.Builder().url(strUrl)
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .build();
        // 2. 发送请求，并处理回调
        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    //1.获取响应主体的json字符串
                    String json = response.body().string();
                    //2.使用FastJson库解析json字符串
                     final Ip ip =  JSON.parseObject(json, Ip.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //3.根据返回的code判断获取是否成功
                            if(ip.getCode() != 0){
                                tvResult.setText("未获取值");
                            }else{
                                //4.解析数据
                                IPData data = ip.getData();
                                tvResult.setText(data.getIp()+","+data.getArea()+","+data.getCity());
                            }

                        }
                    });
                }
            }
        });
    }

    private void post(String url, Map<String, String> params) {
        // 1. 构建RequestBody
        RequestBody body = setRequestBody(params);
        // 2. 创建Request对象
        Request request = new Request.Builder().url(url).post(body)
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
                .addHeader("Accept", "application/json")
                .build();

        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    final Ip ip = JSON.parseObject(json,Ip.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ip.getCode() != 0){
                                tvResult.setText("未获得数据");
                            }else {
                                IPData data = ip.getData();
                                tvResult.setText(data.getIp() + "," + data.getArea());
                            }
                        }
                    });
                }
            }
        });
    }
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private void uploadFile(String url,final String fileName){
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(new File(fileName),MEDIA_TYPE_MARKDOWN))
                .build();
        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("okHttpActivity",e.getMessage());
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(fileName + "上传失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("上传成功，" +str);
                        }
                    });
                }else {
                    Log.d("okHttpActivity",response.body().string());
                }

            }
        });
    }

    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private void uploadImage(String url, final String fileName){
        //1.创建请求主体RequestBody
        RequestBody fileBody = RequestBody.create(
                new File(fileName),MEDIA_TYPE_PNG);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title","头像")
                .addFormDataPart("file",fileName,fileBody)
                .build();
        //2.创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("okHttpActivity",e.getMessage());
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(fileName + "上传失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("上传成功，" +str);
                        }
                    });
                }else {
                    Log.d("okHttpActivity",response.body().string());
                }

            }
        });
    }

    private static void writeFile(InputStream is,String path,String fileName)throws  IOException{
        //1.根据path创建目录对象，并检查path是否存在，不存在则创建
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
        //2.根据path合fileName创建文件对象，如果文件存在则删除
        File file = new File(path,fileName);
        if(file.exists()){
            file.delete();
        }
        //3.创建文件输出流对象，根据输入流创建缓冲输入流对象
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        //4.以每次1024个字节写入输出流对象
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) != -1){
            fos.write(buffer,0,len);
        }
        fos.flush();
        //5.关闭输入流，输出流对象
        fos.close();
        bis.close();
    }

    private void downFile(final String url,final String path){
        //1.创建Request对象
        Request request = new Request.Builder().url(url).build();
        //2.创建OKHTTPClient对象，发送请求，并处理回调
        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("okHttpActivity",e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("文件下载失败");
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    // 1. 获取下载文件的后缀名
                    String ext = url.substring(url.lastIndexOf(".")+1);
                    // 2. 根据当前时间创建文件名，避免重名冲突
                    final String fileName = System.currentTimeMillis() + "." +ext;
                    // 3. 获取响应主体的字节流
                    InputStream is = response.body().byteStream();
                    // 4. 将文件写入path目录
                    writeFile(is,path,fileName);
                     // 5. 在界面给出提示信息
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(fileName + "下载成功，存放在" +path);
                        }
                    });
                }

            }
        });
    }


    private RequestBody setRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }

        return builder.build();
    }



}
