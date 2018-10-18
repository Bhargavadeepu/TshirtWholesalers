package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;

public class StatusWebView extends AppCompatActivity {

    WebView webvw_status;
    String pageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        pageData = i.getExtras().getString("PageInfo");
        webvw_status = findViewById(R.id.webvw_status);
        webvw_status.getSettings().setJavaScriptEnabled(true);
        webvw_status.loadUrl(pageData);
        webvw_status.setWebViewClient(new WebViewClient());

    }
    public void btnback_OnClick(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
