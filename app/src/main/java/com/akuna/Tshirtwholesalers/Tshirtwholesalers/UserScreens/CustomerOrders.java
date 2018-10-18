package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
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
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CustomerOrders extends AppCompatActivity {

    private GraphClient graphClient;
    RecyclerView recyclervv_order;
    private ProgressDialog progressDialog;
    List<CustomerOrderModel> orderList = new ArrayList<>();
    RecyclerView recyclervw_order;
    CustomerOrderAdpter customerOrderAdpter;
    CustomerOrderModel customerOrderModel;
    String id, title, imagesrc, ordertitles;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        /*
         customer Orders Api calls
         */

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            orderApicall();
        }else{
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent custmerOrders = new Intent(CustomerOrders.this, CustomerProfile.class);
                startActivity(custmerOrders);
            }
        });
    }

    private void initViews() {
        recyclervw_order = findViewById(R.id.recyclervw_order);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dbManager = new DBManager(this);
        dbManager.open();
    }

    private void orderApicall() {

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient =  Util.graphclient(getApplicationContext());
   /*     Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .totalPrice()
                                                .subtotalPrice()
                                                .statusUrl()
                                                .customerUrl()
                                                .processedAt()

                                        )
                                )
                        )
                )
        );*/

        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .customerUrl()
                                                .processedAt()
                                                .totalPrice()
                                                .subtotalPrice()
                                                .statusUrl()
                                                .lineItems(arg -> arg.first(10), lineItems -> lineItems.edges(
                                                        orderlistiteam -> orderlistiteam.node(
                                                                child -> child.title().quantity().variant(
                                                                        va -> va.price()
                                                                                .title()
                                                                                .image(arg -> arg.originalSrc())

                                                                ))))
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

                List<Storefront.Order> orders = new ArrayList<>();
                for (Storefront.OrderEdge customer : response.data().getCustomer().getOrders().getEdges()) {
                    orders.add(customer.getNode());

                    List<Storefront.ProductVariant> Orderlineitems = new ArrayList<>();
                    for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                        Orderlineitems.add(orderLineItemEdge.getNode().getVariant());


                        customerOrderModel = new CustomerOrderModel();
                        for (int i = 0; i < Orderlineitems.size(); i++) {
/*
                            id = String.valueOf(Orderlineitems.get(i).getId());
                            title = Orderlineitems.get(i).getTitle();
                            imagesrc = Orderlineitems.get(i).getImage().getOriginalSrc().toString();
                            //  customerOrderModel.setTitle(Orderlineitems.get(i).getTitle());
                            customerOrderModel.setImagSrc(imagesrc);*/


                        }
                    }
                    for (int i = 0; i < orders.size(); i++) {

                        customerOrderModel.setOrderId(String.valueOf(orders.get(i).getOrderNumber()));
                        customerOrderModel.setTotalPrice(String.valueOf(orders.get(i).getTotalPrice()));
                        customerOrderModel.setUrl(orders.get(i).getStatusUrl());
                        customerOrderModel.setOrderDate(String.valueOf(orders.get(i).getProcessedAt()));

                        try {
                            // ordertitles = orders.get(i).getLineItems().getEdges().get(i).getNode().getTitle();
                            ordertitles = orders.get(i).getLineItems().getEdges().get(i).getNode().getTitle();
                            customerOrderModel.setTitle(ordertitles);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Toast.makeText(getApplicationContext(), " Title " + ordertitles, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (Exception e) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //   Toast.makeText(getApplicationContext(), " Excepation  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }
                    orderList.add(customerOrderModel);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String pageData = "hide";
                        recyclervw_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclervw_order.setHasFixedSize(true);
                        customerOrderAdpter = new CustomerOrderAdpter(getApplicationContext(), orderList, pageData);
                        recyclervw_order.setAdapter(customerOrderAdpter);

                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), " Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), " Please try again later" , Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    public void btnback_OnClick(View view) {
        finish();
    }

}

