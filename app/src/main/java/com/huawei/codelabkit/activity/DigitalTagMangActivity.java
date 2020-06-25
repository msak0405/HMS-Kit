package com.huawei.codelabkit.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.huawei.codelabkit.R;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class DigitalTagMangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_tag_mang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dtm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        // Obtain the HiAnalyticsInstance instance.
        HiAnalyticsInstance instance = HiAnalytics.getInstance(this);
        HiAnalyticsTools.enableLog();
        String eventName = "Integration";
        // Customize event parameters.
        Bundle bundle = new Bundle();
        bundle.putString("country", "Finland");
        bundle.putBoolean("isHuawei", true);
        // Report events.
        if (instance != null) {
            instance.onEvent(eventName, bundle);
        }

    }

}
