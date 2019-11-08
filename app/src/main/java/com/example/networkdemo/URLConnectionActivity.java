package com.example.networkdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class URLConnectionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IP_BASE_URL = "http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP_URL = IP_BASE_URL + "?ip=221.226.155.10";
    private static final String UPLOAD_URL = "https://api.github.com/markdown/raw";

    private Button get,post,handin,download;
    private ScrollView scrollView;
    private TextView textView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlconnection);

        get = findViewById(R.id.get);
        post = findViewById(R.id.post);
        handin = findViewById(R.id.handin);
        download = findViewById(R.id.download);
        scrollView = findViewById(R.id.scrollView);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.image);



        get.setOnClickListener(this);
        post.setOnClickListener(this);
        handin.setOnClickListener(this);
        download.setOnClickListener(this);

        GlideApp.with(this).load("https://www.baidu.com/img/bd_logo1.png")
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.get:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = NetworkUtils.get(IP_URL);
                        if(result!=null){
                            Log.d("MainActivity",result);
                          runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(result);
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("请求失败，未获得数据");
                                }
                            });
                        }
                    }
                }).start();
                break;

            case R.id.post:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("ip","221.226.155.10"));
                        final String result = NetworkUtils.post(IP_BASE_URL,params);
                        if(result != null){
                            Log.d("MainActivity",result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(result);
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("请求失败，未获得数据");
                                }
                            });
                        }
                    }
                }).start();
                break;

            case R.id.handin:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                final String fileName = getFilesDir().getAbsolutePath() + File.separator + "readme.md";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = NetworkUtils.uploadFile(UPLOAD_URL, fileName);
                        if(result != null && !TextUtils.isEmpty(result)) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("上传结果： " + result);
                                }
                            });
                        }

                    }
                }).start();

                break;

            case R.id.download:
//                scrollView.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        NetworkUtils.downFile(URLConnectionActivity.this, DOWNLOAD_URL);
//                    }
//                }).start();
//                break;
//            default:
                break;
        }
    }
}
