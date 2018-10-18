package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CustomerOrderAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.FavouriteAdpter;
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

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class FavouritesList extends AppCompatActivity {

    private GraphClient graphClient;
    private DBManager dbManager;
    private ArrayList<FovuroriteModel> favoriteArrylist = new ArrayList<FovuroriteModel>();
    RecyclerView favourite_recyclerview, order_recyclerview;
    private FavouriteAdpter favouriteAdpter;
    private FovuroriteModel fovuroriteModel;
    private List<FovuroriteModel> favoriteList = new ArrayList<>();
    private String pagedata;
    Button btn_login, btn_shopping;
    TextView tv_loginlbl, tv_favtxt, txt_noorders;
    ImageView img_orders;
    CustomerOrderAdpter customerOrderAdpter;
    CustomerOrderModel customerOrderModel;
    List<CustomerOrderModel> orderList = new ArrayList<>();
    private ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String id, title, imagesrc, ordertitles, pageData;
    private ImageView img_fav;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        Util.userName = sharedpreferences.getString("Name", "");
        Util.password = sharedpreferences.getString("password", "");

      /*  Toast.makeText(getApplicationContext(), "Name" + Util.userName, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Password" + Util.password, Toast.LENGTH_SHORT).show();*/

        if (Util.userName.isEmpty()) {
            // btn_logout.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
        } else {
            img_orders.setVisibility(View.GONE);
            tv_loginlbl.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);

            /** Customer orders  Api call **/
            AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
            asynTaskrunner.execute();
        }


        try {
            dbManager = new DBManager(this);
            dbManager.open();
            favoriteArrylist = dbManager.GetFavouriteDetails();
            if (favoriteArrylist == null || favoriteArrylist.size() == 0) {

                img_fav.setVisibility(View.VISIBLE);
                tv_favtxt.setVisibility(View.VISIBLE);
                btn_shopping.setVisibility(View.VISIBLE);
                favourite_recyclerview.setVisibility(View.GONE);


            } else {

                for (int i = 0; i < favoriteArrylist.size(); i++) {
                    fovuroriteModel = new FovuroriteModel();
                    fovuroriteModel.setAvailbleforSale(favoriteArrylist.get(i).getAvailbleforSale());
                    fovuroriteModel.setColor(favoriteArrylist.get(i).getColor());
                    fovuroriteModel.setDiscount(favoriteArrylist.get(i).getDiscount());
                    fovuroriteModel.setImageSrc(favoriteArrylist.get(i).getImageSrc());
                    fovuroriteModel.setPrice(favoriteArrylist.get(i).getPrice());
                    fovuroriteModel.setProductID(favoriteArrylist.get(i).getProductID());
                    fovuroriteModel.setQuantity(favoriteArrylist.get(i).getQuantity());
                    fovuroriteModel.setSize(favoriteArrylist.get(i).getSize());
                    fovuroriteModel.setTitle(favoriteArrylist.get(i).getTitle());
                    fovuroriteModel.setTotalPrice(favoriteArrylist.get(i).getTotalPrice());
                    fovuroriteModel.setVariantID(favoriteArrylist.get(i).getVariantID());
                    fovuroriteModel.setFavId(favoriteArrylist.get(i).getFavId());
                    fovuroriteModel.setDescripation(favoriteArrylist.get(i).getDescripation());
                    fovuroriteModel.setComparaterPrice(favoriteArrylist.get(i).getComparaterPrice());
                    favoriteList.add(fovuroriteModel);
                }

                pagedata = "tab";
                favourite_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                favourite_recyclerview.setHasFixedSize(true);
                favouriteAdpter = new FavouriteAdpter(FavouritesList.this, favoriteList, pagedata);
                favourite_recyclerview.setAdapter(favouriteAdpter);


            }
        } catch (Exception e) {


        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_login.getText().toString().contains("Logout")) {

                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.clear();
                    editor.remove("Name");
                    editor.remove("password");
                    editor.commit();
                    img_orders.setVisibility(View.VISIBLE);
                    tv_loginlbl.setVisibility(View.VISIBLE);
                    //  order_recyclerview.setVisibility(View.GONE);
                    Util.loginOrder = 0;
                    btn_login.setText("LOGIN");
                } else {
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.putExtra("pageInfo", "orders");
                    startActivity(login);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });

    }


    private void initViews() {
        favourite_recyclerview = findViewById(R.id.favourite_recyclerview);
        btn_login = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(getApplicationContext(), R.style.MyAlertDialogStyle);
        img_orders = findViewById(R.id.img_orders);
        tv_loginlbl = findViewById(R.id.tv_loginlbl);
        img_fav = findViewById(R.id.img_fav);
        tv_favtxt = findViewById(R.id.tv_favtxt);
        order_recyclerview = findViewById(R.id.order_recyclerview);
        btn_shopping = findViewById(R.id.btn_shopping);
        txt_noorders = findViewById(R.id.txt_noorders);
        progressbar = findViewById(R.id.progressbar);


    }


    private void Pagenation() {


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

        String productPageCursor = "10";

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID("IjoxNDg4MTc3MzEsImxhc3R"), nodeQuery -> nodeQuery
                        .onCollection(collectionQuery -> collectionQuery
                                .products(
                                        args -> args
                                                .first(10)
                                                .after(productPageCursor),
                                        productConnectionQuery -> productConnectionQuery
                                                .pageInfo(pageInfoQuery -> pageInfoQuery
                                                        .hasNextPage()
                                                )
                                                .edges(productEdgeQuery -> productEdgeQuery
                                                        .cursor()
                                                        .node(productQuery -> productQuery
                                                                .title()
                                                                .productType()
                                                                .description()
                                                        )
                                                )
                                )
                        )
                )
        );

        QueryGraphCall call = graphClient.queryGraph(query);
        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                String data = response.data().responseData.toString();

                Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
                boolean hasNextProductPage = collection.getProducts().getPageInfo().getHasNextPage();
                List<Storefront.Product> products = new ArrayList<>();
                for (Storefront.ProductEdge productEdge : collection.getProducts().getEdges()) {
                    String productPageCursor = productEdge.getCursor();
                    products.add(productEdge.getNode());
                }
            }


            @Override
            public void onFailure(@NonNull GraphError error) {

                //      Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void btnback_OnClick(View view) {
        finish();
    }

    private void loginSeviceCall() {

        //  Util.showProgress(getActivity().this, v);

     /*   progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/

     /*   HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        Util.getHttpmethod(getApplicationContext());
        graphClient = GraphClient.builder(getApplicationContext())
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();*/

        graphClient = Util.graphclient(getApplicationContext());

        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(Util.userName, Util.password);
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAccessTokenCreate(input, query -> query
                        .customerAccessToken(customerAccessToken -> customerAccessToken
                                .accessToken()
                                .expiresAt()
                        )
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

                //   progressDialog.dismiss();

                if (!response.data().getCustomerAccessTokenCreate().getUserErrors().isEmpty()) {
                    for (Storefront.UserError error : response.data().getCustomerAccessTokenCreate().getUserErrors()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //   Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Storefront.CustomerAccessToken customerAccessToken = response.data().getCustomerAccessTokenCreate().getCustomerAccessToken();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), " AccessToken  " + customerAccessToken.getAccessToken(), Toast.LENGTH_SHORT).show();
                            Util.accessToken = customerAccessToken.getAccessToken();

                            orderApicall();

                          /*  Intent register = new Intent(getApplicationContext(), CustomerOrders.class);
                            startActivity(register);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/


                        }
                    });
                }

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                //  progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private void orderApicall() {
/*
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                //    Toast.makeText(getApplicationContext(), "Service Called ", Toast.LENGTH_SHORT).show();


            }
        });

/*
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
                .build();*/
        graphClient = Util.graphclient(getApplicationContext());
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
                //  progressDialog.dismiss();

                //   for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                try {
                    Storefront.Product product = (Storefront.Product) response.data().getNode();
                    List<Storefront.Order> orders = new ArrayList<>();
                    for (Storefront.OrderEdge customer : response.data().getCustomer().getOrders().getEdges()) {
                        orders.add(customer.getNode());

                        List<Storefront.ProductVariant> Orderlineitems = new ArrayList<>();
                        for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                            Orderlineitems.add(orderLineItemEdge.getNode().getVariant());


                            try {
                                customerOrderModel = new CustomerOrderModel();
                                for (int i = 0; i < Orderlineitems.size(); i++) {
                                    //  id = String.valueOf(Orderlineitems.get(i).getId());
                                    //  title = Orderlineitems.get(i).getTitle();
                                    imagesrc = Orderlineitems.get(i).getImage().getOriginalSrc().toString();
                                    //  customerOrderModel.setTitle(Orderlineitems.get(i).getTitle());
                                    customerOrderModel.setImagSrc(imagesrc);
                                }
                            } catch (Exception e) {

                            }

                        }

                        for (int i = 0; i < orders.size(); i++) {

                            customerOrderModel.setOrderId(String.valueOf(orders.get(i).getOrderNumber()));
                            customerOrderModel.setTotalPrice(String.valueOf(orders.get(i).getTotalPrice()));
                            customerOrderModel.setUrl(orders.get(i).getStatusUrl());
                            customerOrderModel.setOrderDate(String.valueOf(orders.get(i).getProcessedAt()));
                            // customerOrderModel.setImagSrc(orders.get(i).getLineItems().);
                            try {
                                ordertitles = orders.get(i).getLineItems().getEdges().get(i).getNode().getTitle();
                                customerOrderModel.setTitle(ordertitles);

                            } catch (Exception e) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(getApplicationContext(), " Excepation  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        orderList.add(customerOrderModel);

                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //  Toast.makeText(getApplicationContext(), " Order list " + orderList.size(), Toast.LENGTH_SHORT).show();
                            pageData = "show";
                            order_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            order_recyclerview.setHasFixedSize(true);
                            customerOrderAdpter = new CustomerOrderAdpter(getApplicationContext(), orderList, pageData);
                            order_recyclerview.setAdapter(customerOrderAdpter);

                            img_orders.setVisibility(View.GONE);
                            tv_loginlbl.setVisibility(View.GONE);

                            if (orderList.size() == 0) {

                                txt_noorders.setVisibility(View.VISIBLE);
                            } else {

                                progressbar.setVisibility(View.GONE);

                            }


                        }
                    });
                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


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


    public void btn_shoppingclick(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(getApplicationContext(), " OnStart Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  Toast.makeText(getApplicationContext(), " onResume Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //    Toast.makeText(getApplicationContext(), " onPause Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //   Toast.makeText(getApplicationContext(), " onStop Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (Util.loginOrder == 1) {
            orderApicall();
            if (btn_login.getText().toString().contains("Logout")) {
                btn_login.setText("Login");
            } else {
                btn_login.setText("Logout");
            }

        } else {


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    //  orderApicall();

    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            loginSeviceCall();
            // orderApicall();
            return null;
        }
    }
}
