package com.huawei.codelabkit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.huawei.codelabkit.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_account).setOnClickListener(this);
        findViewById(R.id.btn_push).setOnClickListener(this);
        findViewById(R.id.btn_site).setOnClickListener(this);
        findViewById(R.id.btn_ads).setOnClickListener(this);
        findViewById(R.id.btn_analytic).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_account:
                startRelevantActivity(AccountActivity.class);
                break;
            case R.id.btn_push:
                startRelevantActivity(PushActivity.class);
                break;
            case R.id.btn_site:
                startRelevantActivity(SiteActivity.class);
                break;
            case R.id.btn_ads:
                startRelevantActivity(AdsActivity.class);
                break;
            case R.id.btn_analytic:
                //startRelevantActivity(AnalyticActivity.class);
                startRelevantActivity(AnalyticArticleActivity.class);
                break;
            default:
                break;
        }

    }

    private void startRelevantActivity(Class<?> activity){
        Intent i = new Intent(MainActivity.this, activity);
        startActivity(i);

    }
}
