package com.huawei.codelabkit.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huawei.codelabkit.R;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;

public class AdsActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ads);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_all_kits);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);


        // Obtain BannerView based on the configuration in layout/ad_fragment.xml.
        BannerView bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);

        // Call new BannerView(Context context) to create a BannerView class.
        BannerView topBannerView = new BannerView(this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        topBannerView.loadAd(adParam);
        loadInterstitialAd();

    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId("testb4znbuh3n2"); // testb4znbuh3n2 is a dedicated test ad slot ID.
        interstitialAd.setAdListener(adListener);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);

    }

    private void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Called when an ad is loaded successfully.

            showInterstitial();
        }
        @Override
        public void onAdFailed(int errorCode) {
            // Called when an ad fails to be loaded.
        }
        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
        }
        @Override
        public void onAdClicked() {
            // Called when an ad is clicked.
        }
        @Override
        public void onAdLeave() {
            // Called when a user leaves an ad.
        }
        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
        }
        @Override
        public void onAdImpression() {
            // Called when an ad impression occurs.
        }
    };

}
