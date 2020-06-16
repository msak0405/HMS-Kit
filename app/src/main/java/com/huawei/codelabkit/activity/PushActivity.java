package com.huawei.codelabkit.activity;

import android.os.Bundle;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.codelabkit.R;
import com.huawei.hms.aaid.HmsInstanceId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PushActivity extends AppCompatActivity {

    private static final String TAG = "Push Activity";
    Button btnToken;
    TextView txtTokenView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_push);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        btnToken = findViewById(R.id.btn_get_token);
        txtTokenView = findViewById(R.id.tv_log);

        btnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
    }

    private void showToken(final String token) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (txtTokenView instanceof TextView) {
                    ((TextView) txtTokenView).setText(token);
                    Toast.makeText(PushActivity.this, token, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(PushActivity.this).getString("client/app_id");
                    String requestedToken = HmsInstanceId.getInstance(PushActivity.this).getToken(appId, "HCM");
                    if(!TextUtils.isEmpty(requestedToken)) {
                        Log.i(TAG, "requested token is :" + requestedToken);
                        showToken(requestedToken);
                    }
                } catch (Exception e) {
                    Log.i(TAG,"getToken failed, " + e);

                }
            }
        }.start();
    }
}

