package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CustomerOrderAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SplashActivity extends Activity {

    private DBManager dbManager;
    String id;
    private GraphClient graphClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (CheckNetwork.isInternetAvailable(SplashActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }

            Util.checkoutWebUrl = "";
            Util.CheckoutId = "";
            Util.accessToken = "";
            Util.checkoutWebUrl = "";
            Util.CheckoutId = " ";
            Util.aftercartItemCount = 0;
            Util.initcartItemCount = 0;
            Util.addressSelection = 0;
            Util.checkoutGuset = 0;
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    Intent login = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(login);
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1000);

            //  orderApicall();
        } else {
            Toast.makeText(SplashActivity.this, "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }





    }


}

