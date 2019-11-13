package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ThreeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button weather,oil,news;
    private ScrollView scrollView;
    private TextView textView;
    //聚合数据
    private static final String KEY="1b72dd975519dcd65cebad9d737860f1";
  private static final String WEATHER_URL=" http://apis.juhe.cn/simpleWeather/query" ;
    private static final String OIL_URL="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        scrollView = findViewById(R.id.scrollView);
        textView = findViewById(R.id.text);
        weather = findViewById(R.id.weather);
        oil = findViewById(R.id.oil);
        news = findViewById(R.id.news);

        weather.setOnClickListener(this);
        oil.setOnClickListener(this);
        news.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather:
                getWeather("南京");
                break;
            case R.id.oil:
                break;
            case R.id.news:
                break;
        }
    }

    private void getWeather(String city) {
        //1.组装数据请求的URL
        try {
            String url = WEATHER_URL+"?key="+KEY+"&cityname="+ URLEncoder.encode(city,"utf-8");
            //2.使用okhttp发送请求
           Request request = new Request.Builder().url(url).build();
           new OkHttpClient().newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(@NotNull Call call, @NotNull IOException e) {

               }

               @Override
               public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                   //3.数据的处理
                   if(response.isSuccessful()){
                       String json = response.body().string();
                       JSONObject obj = JSON.parseObject("result");
                       if(obj!=null){
                           JSONObject realtime = obj.getJSONObject("realtime");

                           //利用FastJson转对象
                           WeatherRealtime weather = JSON.parseObject(realtime.toJSONString(),WeatherRealtime.class);
                       }


                   }



               }
           });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
