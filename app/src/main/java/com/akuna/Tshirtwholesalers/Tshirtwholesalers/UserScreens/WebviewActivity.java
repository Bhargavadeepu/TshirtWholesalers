package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class WebviewActivity extends AppCompatActivity {


    WebView webView;
    String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Improve webView performance

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        // webView.setWebViewClient(new myWebclient());
        webView.getSettings().setJavaScriptEnabled(true);
      /*  Url = "https://www.uniformwholesalers.com.au/";
        webView.loadUrl(Url);*/

        Intent i = getIntent();
        String pageData = i.getExtras().getString("PageInfo");

        weburlCall(pageData);


    }


    private void weburlCall(String pageInfo) {

        String data = pageInfo;


        switch (data) {

            case "AboutUs":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/about-us");
                break;
            case "Question":
                webView.loadUrl("https://www.uniformwholesalers.com.au/pages/questions");
                break;
            case "embroidery":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/embroidery");
                break;
            case "screenprints":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/screen-print");
                break;
            case "heattransfer":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/heat-transfer");
                break;
            case "ourcustomer":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/our-customers");
                break;
            case "shippingpolicy":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/shipping");
                break;
            case "refundpolicy":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/refunds-exchanges");
                break;
            case "reviews":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/reviews");
                break;
            case "blogs":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/blogs/news");
                break;
            case "privacypolicy":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/privacy-policy");
                break;
            case "termsandservices":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/terms-of-service");
                break;
            case "contactus":
                webView.loadUrl("https://www.tshirtwholesalers.com.au/pages/contact-us");
                break;

            case "login":
                Intent login = new Intent(WebviewActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
                break;

        }


    }


}
