package com.akuna.Tshirtwholesalers.Tshirtwholesalers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shopify.buy3.GraphClient;
import com.shopify.buy3.HttpCachePolicy;

import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Util {
    public static String emailPattern = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    // public static int[] recycleImages = {R.drawable.cartimg, R.drawable.cartimg, R.drawable.cartimg, R.drawable.cartimg};
    public static Bitmap imageBitmap;
    public static File imagFile;
    public static Integer[] images = {R.drawable.sliderone, R.drawable.slidertwo, R.drawable.sliderthree, R.drawable.sliderfour};
    // public static Integer[] itemImges = {R.drawable.cartimg, R.drawable.cartimg};
    public static String accessToken = "";
    public static String userName = "";
    public static String password = "";

   /* public static String shopDomain = "budget-uniform.myshopify.com";
    public static String screateKey = "61a64bf39c0ae7046d5e267e753cea3b";*/

    // Live
   /*
    public static String shopDomain = "budget-workwear.myshopify.com";
    public static String screateKey = "0190f80bfebbe8cc90d84a575df36cc4";*/

    //  Live
    public static String shopDomain = "corporateapparel-online.myshopify.com";
    public static String screateKey = "8d8778cf83f588bd01efb9ec572f9746";


    public static GraphClient graphClient;
    public static String checkoutWebUrl = "";
    public static String CheckoutId = "";
    public static BigDecimal itemTtlPrice;
    public static BigDecimal shippingPrice;
    public static BigDecimal grandTtl;
    public static String selectedImage = "";
    public static String variantId = "";

    public static ProgressDialog mProgressDialog;
    public static String firstName = "";
    public static String lastName = "";
    public static String addressline1 = "";
    public static String addressline2 = "";
    public static String phono = "";
    public static String provinceses = "";
    public static String city = "";
    public static String country = "";
    public static String zipcode = "";
    public static int count = 1;
    public static int itempos = 0;
    public static int loginOrder = 0;


    public static int initcartItemCount = 0;
    public static int aftercartItemCount = 0;

    public static int addressSelection = 0;
    public static int shimentCharge = 0;
    public static int checkoutGuset = 0;
    public static int currentPage = 0;
    public static int cartItembind = 0;
    public static int sortItemClick = 0;

    public static BigDecimal discountPrice = BigDecimal.valueOf(0);
    public static BigDecimal actualPrice = BigDecimal.valueOf(0);

    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }

    public static void getHttpmethod(Context context) {


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

       /* graphClient = GraphClient.builder(context)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();*/
    }

    /**
     * user :Bhargava
     * Description : Show dialog
     **/

    public static void showProgress(Context context, View view) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                dismissProgress();
            }
            mProgressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading");
            // mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * user :Bhargava
     * Description : Dismiss dialog
     **/
    public static void dismissProgress() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public static GraphClient graphclient(Context context) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

     /*   OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();*/

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .build();

        graphClient = GraphClient.builder(context)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(context.getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();

        return graphClient;
    }

}
