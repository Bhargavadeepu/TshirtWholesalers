package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CartItemAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/*
Author : Bhargava
Date : 5/29/18
 */

public class ShippingCharge extends AppCompatActivity {

    public static ShippingCharge instance = null;

    private GraphClient graphClient;
    RadioButton radioButton;
    TextView tv_shippingtxt, tv_shippingcharge, tv_shippinamnt, tv_orderamnt, tv_ttlpayble, itemcost, tv_noitems;
    boolean shippingCharges = false;
    BigDecimal rate, sum;
    RecyclerView cartitem_recyclerview;
    private ProgressDialog progressDialog;
    ArrayList<CartModel> cartModelArrayList;
    private DBManager dbManager;
    CartItemAdpter cartItemAdpter;
    String pageInfo;
    String checkoutId, checkoutWebUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_charge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Id  initialization*/
        initViews();
        dbManager = new DBManager(this);
        dbManager.open();

        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            // Toast.makeText(getApplicationContext(), " No Cart items to display", Toast.LENGTH_SHORT).show();

        } else {
            pageInfo = "ShippingCharge";
            cartitem_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            cartitem_recyclerview.setHasFixedSize(true);
            cartItemAdpter = new CartItemAdpter(ShippingCharge.this, cartModelArrayList, pageInfo);
            cartitem_recyclerview.setAdapter(cartItemAdpter);

            tv_noitems.setText("Items" + "(" + cartModelArrayList.size() + ")");

        }


        itemcost.setText("$" + Util.itemTtlPrice);

        /*Radio button Click Event */
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == radioButton) {


                    if (rate.compareTo(BigDecimal.valueOf(0.00)) == 0) {
/*
                        tv_shippingcharge.setText("Free");
                        tv_shippingcharge.setTextColor(Color.parseColor("#00b300"));*/


                    } else {

                        tv_shippingtxt.setVisibility(View.VISIBLE);
                        tv_shippingcharge.setVisibility(View.VISIBLE);
                        shippingCharges = true;

                        tv_orderamnt.setText("$" + Util.itemTtlPrice);
                        sum = Util.itemTtlPrice.add(rate);
                        tv_ttlpayble.setText("$" + sum);
                        Util.grandTtl = sum;


                        tv_shippingcharge.setText("$" + rate);
                        // rate
                    }

                }

            }
        });

        /*
         * Api call to get the Shipping charges
         */
        shippingCharges();

    }


    public void initViews() {
        radioButton = findViewById(R.id.radioButton);
        tv_shippingtxt = findViewById(R.id.tv_shippingtxt);
        tv_shippingcharge = findViewById(R.id.tv_shippingcharge);
        tv_shippinamnt = findViewById(R.id.tv_shippinamnt);
        tv_orderamnt = findViewById(R.id.tv_orderamnt);
        tv_ttlpayble = findViewById(R.id.tv_ttlpayble);
        itemcost = findViewById(R.id.itemcost);
        cartitem_recyclerview = findViewById(R.id.cartitem_recyclerview);
        tv_noitems = findViewById(R.id.tv_noitems);
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        instance = this;
    }


    /*Back arrow btn click Event*/
    public void btnback_OnClick(View view) {
        finish();
    }

    /*Button continue click Event*/

    public void btn_continueclick(View view) {

        if (tv_shippinamnt.getText().toString().contains("Free")) {

            newIntents();
        } else if (shippingCharges == false) {
            Toast.makeText(getApplicationContext(), " Please select the shipping charges to proceed", Toast.LENGTH_SHORT).show();
        } else {
            newIntents();
        }


    }


    private void newIntents() {

   /*
        Intent paymentMode = new Intent(ShippingCharge.this, PaymentMode.class);
        startActivity(paymentMode);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/

        Intent checkout = new Intent(ShippingCharge.this, WebCheckoutActivity.class);
        startActivity(checkout);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    private void shippingCharges() {

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        graphClient = GraphClient.builder(this)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();


        ID checkoutId = new ID(Util.CheckoutId);
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(checkoutId, nodeQuery -> nodeQuery
                        .onCheckout(checkoutQuery -> checkoutQuery
                                .webUrl()
                                .availableShippingRates(availableShippingRatesQuery -> availableShippingRatesQuery
                                        .ready()
                                        .shippingRates(shippingRateQuery -> shippingRateQuery
                                                .handle()
                                                .price()
                                                .title()
                                        )
                                )
                        )
                )
        );


        QueryGraphCall call = graphClient.queryGraph(query);
        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                progressDialog.dismiss();

                Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
                List<Storefront.ShippingRate> shippingRates = checkout.getAvailableShippingRates().getShippingRates();

                for (int i = 0; i < shippingRates.size(); i++) {
                    rate = shippingRates.get(i).getPrice();
                    Util.shippingPrice = shippingRates.get(i).getPrice();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // tv_shippinamnt.setText("$" + rate);
                        tv_ttlpayble.setText("$" + Util.itemTtlPrice);
                        tv_orderamnt.setText("$" + Util.itemTtlPrice);

                        if (rate.compareTo(BigDecimal.valueOf(0.00)) == 0) {

                            Util.shimentCharge = 1;
                            tv_shippinamnt.setText("Free");
                            tv_shippinamnt.setTextColor(Color.parseColor("#00b300"));

                        } else {
                            tv_shippinamnt.setText("$" + rate);
                            Util.shimentCharge = 0;
                        }


                    }
                });
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

  /*  private void checkOutServiceCall() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        graphClient = GraphClient.builder(this)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                //  .httpCache(getCacheDir(), 1024 * 1024 * 10)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(20, TimeUnit.MINUTES))
                .build();

        // String productId = "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8zNDc1NjQwMjUwNQ==";
        // String productId = "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8zMzUzOTAwMzU5Mw==";
        String productId = Util.variantId;


        for (int i = 0; i < cartModelArrayList.size(); i++) {

            //  String ids = cartModelArrayList.get(i).getVariantID();

            id = new ID(cartModelArrayList.get(i).getVariantID());
            quantity = Integer.parseInt(cartModelArrayList.get(i).getQuantity());

            dataPass(cartModelArrayList.get(i).getVariantID(), cartModelArrayList.get(i).getQuantity());

        }


        //   List<Storefront.CheckoutLineItemInput> datalist = new ArrayList<>();
        //  datalist.add(new Storefront.CheckoutLineItemInput(quan, varId));

        //  datalist


        input = new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(datalist)
                );
        //    itemInput = new Storefront.CheckoutLineItemInput(quantity, id);
      *//*  List<Storefront.CheckoutLineItemInput> storefrontLineItems = mapItems(lineItems, lineItem ->
                new Storefront.CheckoutLineItemInput(lineItem.quantity, new ID(lineItem.variantId)));

        Storefront.CheckoutCreateInput input1 = new Storefront.CheckoutCreateInput().setLineItems(storefrontLineItems);

*//*

     *//*
        Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
                .setLineItems(Collections.singletonList(itemInput));*//*


        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutCreate(input, createPayloadQuery -> createPayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                                .totalPrice()
                                .subtotalPrice()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );


        graphClient.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                if (!response.data().getCheckoutCreate().getUserErrors().isEmpty()) {
                    // handle user friendly errors
                } else {
                    checkoutId = response.data().getCheckoutCreate().getCheckout().getId().toString();
                    checkoutWebUrl = response.data().getCheckoutCreate().getCheckout().getWebUrl();
                    Util.checkoutWebUrl = response.data().getCheckoutCreate().getCheckout().getWebUrl();
                    Util.CheckoutId = response.data().getCheckoutCreate().getCheckout().getId().toString();
                    Util.itemTtlPrice = response.data().getCheckoutCreate().getCheckout().getTotalPrice();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent login = new Intent(ShippingCharge.this, LoginActivity.class);
                        login.putExtra("pageInfo", "myCart");
                        startActivity(login);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }*/


}
