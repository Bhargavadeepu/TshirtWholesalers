package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.Addressadpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CartItemAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyCartActivity extends AppCompatActivity {

    public static MyCartActivity instance = null;
    private GraphClient graphClient;
    Button btn_placeOrder;
    private DBManager dbManager;
    private ArrayList<CartModel> cartModelArrayList;
    RecyclerView cart_recyclervw;
    CartItemAdpter cartItemAdpter;
    TextView tv_itemsno, tv_ttlprice, txt_cartttl, tv_discount, tv_ttlpayable, txtlbl_orderdetails;
    float ttlprice, comparterPrice;
    int qunatity;
    String checkoutId, checkoutWebUrl, pageInfo;
    ID id;
    View layout_price, fav_layout, pricing_layout, nocart_layout, btn_layout;
    public String variantId;
    public String title;
    public int quantity;
    Storefront.CheckoutLineItemInput itemInput;
    Storefront.CheckoutCreateInput input;
    double ttldiscountamnt, ttlPayable;
    private List<Storefront.CheckoutLineItemInput> datalist = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();

        dbManager = new DBManager(this);
        dbManager.open();

        quantity = 0;
        Util.cartItembind = 0;
        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            layout_price.setVisibility(View.GONE);
            btn_placeOrder.setVisibility(View.GONE);
            txtlbl_orderdetails.setVisibility(View.GONE);
            btn_layout.setVisibility(View.GONE);
            fav_layout.setVisibility(View.GONE);
            pricing_layout.setVisibility(View.GONE);
            nocart_layout.setVisibility(View.VISIBLE);

        } else {
            pageInfo = "myCart";
            cart_recyclervw.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            cart_recyclervw.setHasFixedSize(true);
            cartItemAdpter = new CartItemAdpter(MyCartActivity.this, cartModelArrayList, pageInfo);
            cart_recyclervw.setAdapter(cartItemAdpter);


            dataBinding();


        }


    }

    public void initViews() {

        btn_placeOrder = findViewById(R.id.btn_placeOrder);
        cart_recyclervw = findViewById(R.id.cart_recyclervw);
        tv_itemsno = findViewById(R.id.tv_itemsno);
        tv_ttlprice = findViewById(R.id.tv_ttlprice);
        txt_cartttl = findViewById(R.id.txt_cartttl);
        tv_discount = findViewById(R.id.tv_discount);
        tv_ttlpayable = findViewById(R.id.tv_ttlpayable);
        layout_price = findViewById(R.id.layout_price);
        fav_layout = findViewById(R.id.fav_layout);
        pricing_layout = findViewById(R.id.pricing_layout);
        txtlbl_orderdetails = findViewById(R.id.txtlbl_orderdetails);
        nocart_layout = findViewById(R.id.nocart_layout);
        btn_layout = findViewById(R.id.btn_layout);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        instance = this;
    }

    // Search Button Click

    public void btnsearch_OnClick(View view) {

        finish();
        Intent search = new Intent(getApplicationContext(), LivesearchActivity.class);
        search.putExtra("PageInfo", "NoData");
        startActivity(search);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void favlayout_OnClick(View view) {
        Intent favoriteList = new Intent(MyCartActivity.this, FavouriteItemsList.class);
        startActivity(favoriteList);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void btnback_OnClick(View view) {

        finish();
    }

    public void btn_placeOrder(View view) {

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {

        } else {

            if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                checkOutServiceCall();
            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }


        }


    }

    private void checkOutServiceCall() {

        graphClient = Util.graphclient(getApplicationContext());

        String productId = Util.variantId;


        for (int i = 0; i < cartModelArrayList.size(); i++) {

            //  String ids = cartModelArrayList.get(i).getVariantID();

            quantity = 0;

            id = new ID(cartModelArrayList.get(i).getVariantID());
            quantity = Integer.parseInt(cartModelArrayList.get(i).getQuantity());

            dataPass(cartModelArrayList.get(i).getVariantID(), cartModelArrayList.get(i).getQuantity());

        }


        input = new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(datalist)
                );


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

                        Intent login = new Intent(MyCartActivity.this, LoginActivity.class);
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
                        Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    private void dataPass(String id, String quntity) {

        ID varId = new ID(id);
        //  int quan = quantity;
        int quan = Integer.parseInt(quntity);
        datalist.add(new Storefront.CheckoutLineItemInput(quan, varId));

    /*    input = new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(Arrays.asList(
                        new Storefront.CheckoutLineItemInput(quan, varId)
                )));
*/
    }

    private void dataBinding() {


        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {

        } else {

            for (int i = 0; i < cartModelArrayList.size(); i++) {

                ttlprice = (float) (ttlprice + cartModelArrayList.get(i).getTotalPrice());

                comparterPrice = (float) cartModelArrayList.get(i).getComparaterPrice();

                quantity = Integer.parseInt((qunatity + cartModelArrayList.get(i).getQuantity()));
                double discountAmnt = Double.parseDouble(cartModelArrayList.get(i).getDiscount());

                if (discountAmnt == 0) {

                } else {
                    double perdiscount = Double.parseDouble(cartModelArrayList.get(i).getDiscount());
                    ttldiscountamnt = Double.parseDouble(String.valueOf((ttldiscountamnt + perdiscount)));

                }
            }

            if (ttldiscountamnt == 0) {
                tv_discount.setText("$" + "0.00");
                ttlPayable = ttlprice;
                txt_cartttl.setText("$" + String.format("%.2f", new BigDecimal(ttlprice)));
                //   tv_ttlpayable.setText("$" + ttlPayable);
                tv_ttlpayable.setText("$" + String.format("%.2f", new BigDecimal(ttlPayable)));

            } else {

                String grandTotal = String.format("%.2f", new BigDecimal(ttldiscountamnt));
                // Casting to two digits

                tv_discount.setText("$" + String.format("%.2f", new BigDecimal(ttldiscountamnt)));

                //  ttlPayable = (ttlprice - ttldiscountamnt);
                ttlPayable = (ttlprice - ttldiscountamnt);
                tv_ttlpayable.setText("$" + String.format("%.2f", new BigDecimal(ttlPayable)));


                txt_cartttl.setText("$" + String.format("%.2f", new BigDecimal(ttlprice)));
            }

            tv_itemsno.setText("ITEMS" + "(" + cartModelArrayList.size() + ")");
            CartModel cartModel = new CartModel();
            cartModel.getTotalPrice();
            tv_ttlprice.setText("TOTAL :" + "$" + String.format("%.2f", new BigDecimal(ttlPayable)));

            //ttlprice

        }


    }


    @Override
    protected void onResume() {
        super.onResume();


        if (Util.cartItembind == 1) {
            cartModelArrayList = dbManager.GetCartDetails();

            if (cartModelArrayList != null || cartModelArrayList.size() != 0) {

                cart_recyclervw.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                cart_recyclervw.setHasFixedSize(true);
                cartItemAdpter = new CartItemAdpter(MyCartActivity.this, cartModelArrayList, pageInfo);
                cart_recyclervw.setAdapter(cartItemAdpter);
            }


        }


    }


}



