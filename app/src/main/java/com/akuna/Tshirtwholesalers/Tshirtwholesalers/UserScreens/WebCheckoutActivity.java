package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class WebCheckoutActivity extends AppCompatActivity {

    private GraphClient graphClient;
    private WebView webvwcheckout;
    String checkoutId, checkoutWebUrl;
    List<CustomerOrderModel> orderList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private DBManager dbManager;
    String id, orderNo, pageInfo;
    private ArrayList<CartModel> cartModelArrayList;
    View layoutProgressBar;
    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(this);
        dbManager.open();

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        webvwcheckout = findViewById(R.id.webvwcheckout);

        layoutProgressBar = findViewById(R.id.layoutProgressBar);


        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            associateCustomer();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


        Intent info = getIntent();

        pageInfo = info.getExtras().getString("pageInfo");

        if (pageInfo.contains("Guest")) {

        } else {
            AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
            asynTaskrunner.execute();
        }


    }


    public void btnback_OnClick(View view) {

        if (Util.checkoutGuset == 1) {
            MyCartActivity.instance.finish();
            finish();

        } else {

            if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                customerOrderServiceCall();
            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void associateCustomer() {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient = Util.graphclient(getApplicationContext());

        ID checkoutid = new ID(Util.CheckoutId);
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                // .checkoutCustomerAssociate(checkoutid, Util.accessToken, query -> query
                .checkoutCustomerAssociate(checkoutid, Util.accessToken, query -> query
                        .checkout(Storefront.CheckoutQuery::webUrl)
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );

        MutationGraphCall call = graphClient.mutateGraph(mutationQuery);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                progressDialog.dismiss();

                if (!response.hasErrors() && response.data() != null) {
                    Storefront.Checkout checkout = response.data().getCheckoutCustomerAssociate().getCheckout();


                    webUrl = "";

                    webUrl = checkout.getWebUrl();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            WebSettings webSettings = webvwcheckout.getSettings();
                            webSettings.setJavaScriptEnabled(true);

                            webvwcheckout.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                            webvwcheckout.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            webvwcheckout.getSettings().setAppCacheEnabled(true);
                            webSettings.setDomStorageEnabled(true);
                            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                            webSettings.setUseWideViewPort(true);
                            webSettings.setSavePassword(true);
                            webSettings.setSaveFormData(true);
                            webSettings.setEnableSmoothTransition(true);
                            webvwcheckout.setWebViewClient(new myWebclient());
                            webvwcheckout.getSettings().setJavaScriptEnabled(true);
                            webvwcheckout.loadUrl(webUrl);
                        /*    webvwcheckout.getSettings().setJavaScriptEnabled(true);
                            webvwcheckout.loadUrl(webUrl);
                            webvwcheckout.setWebViewClient(new WebViewClient());*/
                            layoutProgressBar.setVisibility(View.GONE);

                        }
                    });


                } else {

                    Toast.makeText(getApplicationContext(), "    " + response.formatErrorMessage(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " erros " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }


    private void customerOrderServiceCall() {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        graphClient = Util.graphclient(getApplicationContext());

        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .customerUrl()
                                                .totalPrice()
                                                .subtotalPrice()
                                                .statusUrl()
                                                .lineItems(arg -> arg.first(20), lineItems -> lineItems.edges(orderlistiteam -> orderlistiteam.node(
                                                        child -> child.title().quantity().variant(va -> va.price()))))
                                        )
                                )
                        )
                )
        );


        QueryGraphCall call = Util.graphClient.queryGraph(query);
        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {


                progressDialog.dismiss();

                if (response.data().equals(null)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            //  Toast.makeText(getApplicationContext(), " No Matches found", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                } else {

                    List<Storefront.Order> orders = new ArrayList<>();
                    for (Storefront.OrderEdge customer : response.data().getCustomer().getOrders().getEdges()) {
                        orders.add(customer.getNode());


                        for (int i = 0; i < orders.size(); i++) {

                            orderNo = String.valueOf(orders.get(i).getOrderNumber());


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //     Toast.makeText(getApplicationContext(), " Order Number " + orderNo, Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                        List<Storefront.ProductVariant> Orderlineitems = new ArrayList<>();
                        for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                            Orderlineitems.add(orderLineItemEdge.getNode().getVariant());

                            for (int i = 0; i < Orderlineitems.size(); i++) {

                                id = String.valueOf(Orderlineitems.get(i).getId());

                                Util.aftercartItemCount = orders.size();

                                if (Util.aftercartItemCount > Util.initcartItemCount) {
                                    cartModelArrayList = dbManager.GetCartDetails();
                                    if (cartModelArrayList == null || cartModelArrayList.size() == 0) {

                                    } else {
                                        for (int k = 0; k < cartModelArrayList.size(); k++) {

                                            if (id.equals(cartModelArrayList.get(k).getVariantID())) {
                                                dbManager.deleteCartitemBasedOnvarId(id);

                                            }
                                        }

                                    }


                                } else {

                                    finish();
                                }

                            }
                        }
                    }

                    if (Util.aftercartItemCount > Util.initcartItemCount) {
                        LoginActivity.instance.finish();
                        MyAddresses.instance.finish();
                        MyCartActivity.instance.finish();
                        // ShippingCharge.instance.finish();
                        //    PaymentMode.instance.finish();
                        //  SelectedItemActivity.instance.finish();
                        finish();
                        Intent thankupage = new Intent(WebCheckoutActivity.this, ThankyouActivity.class);
                        thankupage.putExtra("OrderNo", orderNo);
                        startActivity(thankupage);
                    } else {
                        finish();
                    }


                }


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

    @Override
    public void onBackPressed() {

        if (Util.checkoutGuset == 1) {

            MyCartActivity.instance.finish();
            finish();

        } else {

            if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                customerOrderServiceCall();

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }


        }


        //  finish();
    }

    private void orderApicall() {


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


        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .customerUrl()
                                                .totalPrice()
                                                .subtotalPrice()
                                                .statusUrl()
                                                .lineItems(arg -> arg.first(10), lineItems -> lineItems.edges(orderlistiteam -> orderlistiteam.node(
                                                        child -> child.title().quantity().variant(va -> va.price()))))
                                        )
                                )
                        )
                )
        );


        QueryGraphCall call = graphClient.queryGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                //   progressDialog.dismiss();
                List<Storefront.Order> orders = new ArrayList<>();
                for (Storefront.OrderEdge customer : response.data().getCustomer().getOrders().getEdges()) {
                    orders.add(customer.getNode());

                    List<Storefront.ProductVariant> Orderlineitems = new ArrayList<>();
                    for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                        Orderlineitems.add(orderLineItemEdge.getNode().getVariant());

                    }


                }
                Util.initcartItemCount = orders.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Toast.makeText(getApplicationContext(), " init Order Count  " + Util.initcartItemCount, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                //  progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            orderApicall();
            return null;
        }
    }


    public class myWebclient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            layoutProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            layoutProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

    }


}
