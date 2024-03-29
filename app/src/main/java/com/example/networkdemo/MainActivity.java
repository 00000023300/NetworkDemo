package com.example.networkdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button ok,http,three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ok = findViewById(R.id.ok);
        http = findViewById(R.id.http);
        three = findViewById(R.id.three);

        ok.setOnClickListener(this);
        http.setOnClickListener(this);
        three.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                Intent intent = new Intent(MainActivity.this, OkHttpActivity.class);
                startActivity(intent);
                break;

            case R.id.http:
                intent = new Intent(MainActivity.this,URLConnectionActivity .class);
                startActivity(intent);
                break;

            case R.id.three:
                intent = new Intent(MainActivity.this,ThreeActivity .class);
                startActivity(intent);
                break;

        }
    }
}
