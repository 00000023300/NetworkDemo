package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ThreeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button weather,oil,news;
    private ScrollView scrollView;
    private TextView textView;

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
                break;
            case R.id.oil:
                break;
            case R.id.news:
                break;
        }
    }
}
