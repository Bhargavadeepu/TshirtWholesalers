package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CheckoutasGuest extends AppCompatActivity {


    EditText edt_email;
    TextInputLayout textInputLayout_email;
    Button btn_proceed;
    String pageData;
    private GraphClient graphClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutas_guest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();

        // shippingCharges();
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                textInputLayout_email.setErrorEnabled(false);
                textInputLayout_email.setError("");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void initViews() {
        edt_email = findViewById(R.id.edt_email);
        textInputLayout_email = findViewById(R.id.textInputLayout_email);
        btn_proceed = findViewById(R.id.btn_proceed);
        progressDialog = new ProgressDialog(this);
    }

    /* Procedd Buttton Click Listner*/
    public void btn_click(View view) {


        if (edt_email.getText().toString().isEmpty()) {

            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError(" Please enter email Address");

        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {

            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError(" Please enter a valid email Address");
        } else {


/* Intent address = new Intent(CheckoutasGuest.this, MyAddresses.class);
            startActivity(address);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
            serviceCall(view);

        }
    }

    private void serviceCall(View view) {

        Util.showProgress(CheckoutasGuest.this, view);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        Util.getHttpmethod(getApplication());
        graphClient = GraphClient.builder(this)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();

        ID checkoutId = new ID(Util.CheckoutId);

        String emailId = edt_email.getText().toString();

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutEmailUpdate(checkoutId, emailId, emailUpdatePayloadQuery -> emailUpdatePayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );

        MutationGraphCall call = graphClient.mutateGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                try{
                    Util.dismissProgress();
                    if (!response.data().getCheckoutEmailUpdate().getUserErrors().isEmpty()) {
                        // handle user friendly errors
                    } else {
                        Util.checkoutWebUrl = response.data().getCheckoutEmailUpdate().getCheckout().getWebUrl();
                        Intent address = new Intent(CheckoutasGuest.this, WebCheckoutActivity.class);
                        address.putExtra("pageInfo", "Guest");
                        startActivity(address);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        Util.checkoutGuset = 1;
                    }


                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Util.dismissProgress();
               // Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Please Try Again some time later" , Toast.LENGTH_SHORT).show();
            }
        });


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


}
