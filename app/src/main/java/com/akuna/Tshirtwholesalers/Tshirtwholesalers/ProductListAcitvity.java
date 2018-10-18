package com.akuna.Tshirtwholesalers.Tshirtwholesalers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.GridViewAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.ProductListAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.Productgridadpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.SearchAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.AlertpopupAcitivtiy;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.DashboardTab;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.LivesearchActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.ProgrssbarActivity;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.RetryHandler;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class ProductListAcitvity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private GraphClient graphClient;
    CollectionModel collectionModel;
    ArrayList<Integer> itemImg = new ArrayList<>();
    List<CollectionModel> gridList = new ArrayList<>();
    Productgridadpter productgridadpter;
    ProductListAdpter productListAdpter;
    GridView gridView;
    String pageData, productPageCursor;
    List<String> sortingList = new ArrayList<>();
    private ProgressDialog progressDialog;
    HashSet<CollectionModel> itemSet;
    RecyclerView recyclervw;
    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private HashSet<CollectionModel> itemset = new HashSet<>();
    private int initload = 0, pageload = 0;
    View sort_layout;
    ImageView img_sort;
    GridLayoutManager mLayoutManager;
    View layoutProgressBar;
    boolean hasNextProductPage;
    Button btn_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_acitvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sortingList.add("Dumy Data");
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        Intent i = getIntent();
        pageData = i.getExtras().getString("pageinfo");
        recyclervw = findViewById(R.id.recyclervw);
        Util.currentPage = 0;
        initViews();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        layoutProgressBar.setVisibility(View.GONE);


        sort_layout.setVisibility(View.GONE);

        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();


        /*******  Sort Button Click *******/


        sort_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent popup = new Intent(ProductListAcitvity.this, AlertpopupAcitivtiy.class);
                startActivity(popup);
                overridePendingTransition(R.anim.slidedown, R.anim.slidedown);
                img_sort.setBackgroundResource(R.drawable.sortgreen);
                Util.sortItemClick = 0;
             /*   pagination(pageData);
                pageload = 1;*/

            }
        });


        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popup = new Intent(ProductListAcitvity.this, AlertpopupAcitivtiy.class);
                startActivity(popup);
                overridePendingTransition(R.anim.slidedown, R.anim.slidedown);
                img_sort.setBackgroundResource(R.drawable.sortgreen);
            }
        });


        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                currentScrollState = scrollState;
                //  isScrollCompleted();
                Util.currentPage = 1;
                initload = 1;

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    sort_layout.setVisibility(View.VISIBLE);

                } else {
                    sort_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /*currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;*/

                if (initload == 1) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                        // End has been reached
                        // isScrollCompleted();
                        //    layoutProgressBar.setVisibility(View.VISIBLE);
                        //   Toast.makeText(getApplicationContext(), " Scroll end has been reched ", Toast.LENGTH_SHORT).show();


                    }


                } else {


                }


            }

        });


        // Floating Button clicklisnter

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagination(pageData);

                //   productApicall(pageData);
                layoutProgressBar.setVisibility(View.VISIBLE);

            }
        });


    }


    private void initViews() {
        gridView = findViewById(R.id.gridView);
        sort_layout = findViewById(R.id.sort_layout);
        img_sort = findViewById(R.id.img_sort);
        layoutProgressBar = findViewById(R.id.layoutProgressBar);
        btn_sort = findViewById(R.id.btn_sort);
    }

    private void isScrollCompleted() {

        productApicall(pageData);
        //    pagination(pageData);

    }

    private void productApicall(String data) {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (Util.currentPage == 1) {

                    } else {
                        progressDialog.setMessage("Loading...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                    }

                }
            });
            graphClient = Util.graphclient(getApplicationContext());

            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shop -> shop
                            .products(arg -> arg.first(10).query(data).after(productPageCursor), connection -> connection
                                    .pageInfo(pageInfoQuery -> pageInfoQuery
                                            .hasNextPage())
                                    .edges(edges -> edges
                                            .cursor()
                                            .node(productQuery -> productQuery
                                                    .title()
                                                    .productType()
                                                    .description()
                                                    .descriptionHtml()
                                                    .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
                                                            .edges(imageEdgeQuery -> imageEdgeQuery
                                                                    .node(imageQuery -> imageQuery
                                                                            .src()
                                                                    )
                                                            )
                                                    )
                                                    .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
                                                            .edges(variantEdgeQuery -> variantEdgeQuery
                                                                    .node(productVariantQuery -> productVariantQuery
                                                                            .price()
                                                                            .title()
                                                                            .compareAtPrice()
                                                                            .availableForSale()


                                                                    )
                                                            )
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

                    progressDialog.dismiss();


                    try {


                        if (!Objects.equals(response.data().getShop().getProducts().getEdges().size(), 0)) {

                            //boolean hasNextProductPage = productEdge.getProducts().getPageInfo().getHasNextPage();

                            Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
                            // hasNextProductPage = collection.getProducts().getPageInfo().getHasNextPage();
                            List<Storefront.Product> products = new ArrayList<>();
                            for (Storefront.ProductEdge productEdge : response.data().getShop().getProducts().getEdges()) {
                                //products found is an already initialized arraylist

                                productPageCursor = productEdge.getCursor();
                                products.add(productEdge.getNode());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        // Toast.makeText(getApplicationContext(), " Cursor data " + productPageCursor, Toast.LENGTH_SHORT).show();

                                    }
                                });
                                List<Storefront.ProductVariant> variantslist = new ArrayList<>();
                                for (Storefront.ProductVariantEdge variantEdge : productEdge.getNode().getVariants().getEdges()) {
                                    variantslist.add(variantEdge.getNode());

                                }
                                List<Storefront.Image> imagelist = new ArrayList<>();
                                for (Storefront.ImageEdge productVariantEdge : productEdge.getNode().getImages().getEdges()) {
                                    imagelist.add(productVariantEdge.getNode());
                                }


                                collectionModel = new CollectionModel();
                                for (int k = 0; k < variantslist.size(); k++) {
                                    try {
                                        double price = Double.parseDouble(variantslist.get(k).get("price").toString());
                                        double comparaterprice = Double.parseDouble(variantslist.get(k).getCompareAtPrice().toString());
                                        BigDecimal ttldiscount = BigDecimal.valueOf((comparaterprice - price));
                                        collectionModel.setName(variantslist.get(k).get("title").toString());
                                        collectionModel.setCost((BigDecimal) variantslist.get(k).get("price"));
                                        collectionModel.setVariantId(variantslist.get(k).getId().toString());
                                        collectionModel.setAvailableSale(variantslist.get(k).get("availableForSale").toString());
                                        collectionModel.setCompareatprice(variantslist.get(k).getCompareAtPrice());
                                        collectionModel.setDiscount(ttldiscount);

                                    } catch (Exception e) {
                                    }
                                    for (int i = 0; i < imagelist.size(); i++) {
                                        String src = imagelist.get(i).get("src").toString();
                                        collectionModel.setImage(src);
                                    }
                                    //  gridList.add(collectionModel);
                                }

                                for (int j = 0; j < products.size(); j++) {

                                    collectionModel.setProductId(products.get(j).getId().toString());
                                    collectionModel.setTitle(products.get(j).getTitle().toString());
                                    collectionModel.setDescripation(products.get(j).getDescription().toString());
                                    //  collectionModel.setDescripation(products.get(j).getDescriptionHtml().toString());

                                }

                                itemset.add(collectionModel);
                                gridList = new ArrayList<>(itemset);

                                productgridadpter = new Productgridadpter(getApplication(), gridList);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       /*  productgridadpter = new Productgridadpter(getApplication(), gridList);
                                gridView.setAdapter(productgridadpter);
                                layoutProgressBar.setVisibility(View.GONE);*/
                                        layoutProgressBar.setVisibility(View.GONE);
                                        if (Util.currentPage == 1) {
                                            // productgridadpter = new Productgridadpter(getApplication(), gridList);
                                            productgridadpter.notifyDataSetChanged();
                                            gridView.setAdapter(productgridadpter);
                                            // layoutProgressBar.setVisibility(View.GONE);
                                            //  Toast.makeText(getApplicationContext(), " Called only  Grid View  adpter called ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //  productgridadpter = new Productgridadpter(getApplication(), gridList);
                                            gridView.setAdapter(productgridadpter);
                                            //   layoutProgressBar.setVisibility(View.GONE);
                                            //  Toast.makeText(getApplicationContext(), " Set adpter is also called ", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                            }
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    //  Toast.makeText(getApplicationContext(), " No Matches found", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                    } catch (Exception e) {

                    }


                }


                @Override
                public void onFailure(@NonNull GraphError error) {
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }

    public void btnback_OnClick(View view) {

        finish();
    }


    private void sortServiceCall(String data) {
        //  gridList.clear();
        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);



            graphClient = Util.graphclient(getApplicationContext());


            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shopQuery -> shopQuery
                            // .collections(arg -> arg.first(1).query(data), collectionConnectionQuery -> collectionConnectionQuery
                            .collections(arg -> arg.first(1).query(productPageCursor), collectionConnectionQuery -> collectionConnectionQuery
                                    .edges(collectionEdgeQuery -> collectionEdgeQuery
                                            .node(collectionQuery -> collectionQuery
                                                    .title()
                                                    .products(arg -> arg.first(10), productConnectionQuery -> productConnectionQuery
                                                            .edges(productEdgeQuery -> productEdgeQuery
                                                                    .node(productQuery -> productQuery
                                                                            .title()
                                                                            .productType()
                                                                            .description()
                                                                            .descriptionHtml()

                                                                            .images(arg -> arg.first(1), imageConnectionQuery -> imageConnectionQuery
                                                                                    .edges(imageEdgeQuery -> imageEdgeQuery
                                                                                            .node(imageQuery -> imageQuery
                                                                                                    .src()
                                                                                            )
                                                                                    )
                                                                            )
                                                                            .variants(arg -> arg.first(1), variantConnectionQuery -> variantConnectionQuery
                                                                                    .edges(variantEdgeQuery -> variantEdgeQuery
                                                                                            .node(productVariantQuery -> productVariantQuery
                                                                                                    .price()
                                                                                                    .title()
                                                                                                    .compareAtPrice()
                                                                                                    .availableForSale()

                                                                                            )
                                                                                    )
                                                                            )
                                                                    )
                                                            )


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

                    for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                        // collections.add(collectionEdge.getNode());
                        // collectionList.add(collectionEdge.getNode());
                        // }
                        List<Storefront.Product> products = new ArrayList<>();
                        for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
                            products.add(productEdge.getNode());

                            List<Storefront.ProductVariant> variantslist = new ArrayList<>();
                            for (Storefront.ProductVariantEdge variantEdge : productEdge.getNode().getVariants().getEdges()) {
                                variantslist.add(variantEdge.getNode());

                            }
                            List<Storefront.Image> imagelist = new ArrayList<>();
                            for (Storefront.ImageEdge productVariantEdge : productEdge.getNode().getImages().getEdges()) {
                                imagelist.add(productVariantEdge.getNode());
                            }


                            collectionModel = new CollectionModel();
                            for (int k = 0; k < variantslist.size(); k++) {

                                collectionModel.setName(variantslist.get(k).get("title").toString());
                                collectionModel.setCost((BigDecimal) variantslist.get(k).get("price"));
                                collectionModel.setVariantId(variantslist.get(k).getId().toString());
                                collectionModel.setAvailableSale((variantslist.get(k).getAvailableForSale().toString()));


                                /** Condition to check the comparator price */
                                BigDecimal comparetorPrice = (BigDecimal) variantslist.get(k).get("compareAtPrice");
                                collectionModel.setCompareatprice(comparetorPrice);

                                for (int i = 0; i < imagelist.size(); i++) {
                                    String src = imagelist.get(i).get("src").toString();
                                    collectionModel.setImage(src);
                                }
                                //  gridList.add(collectionModel);
                            }

                            for (int j = 0; j < products.size(); j++) {
                                collectionModel.setProductId(products.get(j).getId().toString());
                                collectionModel.setTitle(products.get(j).getTitle().toString());
                                collectionModel.setDescripation(products.get(j).getDescriptionHtml().toString());

                            }
                            gridList.add(collectionModel);
                        }


                    }


                    if (Util.itempos == 0) {
                        Collections.sort(gridList, new Comparator<CollectionModel>() {
                            @Override
                            public int compare(CollectionModel o1, CollectionModel o2) {
                                return o2.getCost().compareTo(o1.getCost());
                            }
                        });
                    } else if (Util.itempos == 1) {
                        Collections.sort(gridList, new Comparator<CollectionModel>() {
                            @Override
                            public int compare(CollectionModel o1, CollectionModel o2) {
                                return o1.getCost().compareTo(o2.getCost());
                            }
                        });

                    } else {

                        Collections.sort(gridList, new Comparator<CollectionModel>() {
                            @Override
                            public int compare(CollectionModel o1, CollectionModel o2) {
                                return o2.getTitle().compareTo(o1.getTitle());
                            }
                        });


                    }
/*
                    Collections.sort(gridList, new Comparator<CollectionModel>() {
                        @Override
                        public int compare(CollectionModel o1, CollectionModel o2) {
                            return o1.getCompareatprice().compareTo(o2.getCompareatprice());
                        }
                    });*/


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Productgridadpter productgridadpter = new Productgridadpter(getApplication(), gridList);
                            gridView.setAdapter(productgridadpter);

                        }
                    });

                    progressDialog.dismiss();

                }


                @Override
                public void onFailure(@NonNull GraphError error) {
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });


        } else {

            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Util.count == 2) {
            sortServiceCall(pageData);
            // Toast.makeText(getApplicationContext(), " Sort data " + pageData, Toast.LENGTH_SHORT).show();

        } else {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }


    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            //  productApicall(pageData);
            pagination(pageData);
            return null;
        }
    }

    private void pagination(String data) {


        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (Util.currentPage == 1) {

                    } else {
                        progressDialog.setMessage("Loading...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                    }

                   /* Intent popup = new Intent(ProductListAcitvity.this, ProgrssbarActivity.class);
                    startActivity(popup);*/


                }
            });


            graphClient = Util.graphclient(getApplicationContext());

            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .node(new ID(pageData), nodeQuery -> nodeQuery
                            .onCollection(collectionQuery -> collectionQuery
                                    .products(arg -> arg.first(10).after(productPageCursor), productConnectionQuery -> productConnectionQuery
                                            //    .products(arg -> arg.first(10).query(data).after(productPageCursor), connection -> connection
                                            .pageInfo(pageInfoQuery -> pageInfoQuery
                                                    .hasNextPage()
                                            )
                                            .edges(productEdgeQuery -> productEdgeQuery
                                                    .cursor()
                                                    .node(productQuery -> productQuery
                                                            .title()
                                                            .productType()
                                                            .description()
                                                            .descriptionHtml()

                                                            .images(arg -> arg.first(1), imageConnectionQuery -> imageConnectionQuery
                                                                    .edges(imageEdgeQuery -> imageEdgeQuery
                                                                            .node(imageQuery -> imageQuery
                                                                                    .src()
                                                                            )
                                                                    )
                                                            )
                                                            .variants(arg -> arg.first(1), variantConnectionQuery -> variantConnectionQuery
                                                                    .edges(variantEdgeQuery -> variantEdgeQuery
                                                                            .node(productVariantQuery -> productVariantQuery
                                                                                    .price()
                                                                                    .title()
                                                                                    .compareAtPrice()
                                                                                    .availableForSale()
                                                                            )
                                                                    )
                                                            )
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


                    try {

                        progressDialog.dismiss();
                        Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
                        hasNextProductPage = collection.getProducts().getPageInfo().getHasNextPage();
                        List<Storefront.Product> products = new ArrayList<>();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                //   Toast.makeText(getApplicationContext(), " productPageCursor  " + productPageCursor, Toast.LENGTH_SHORT).show();
                                //   Toast.makeText(getApplicationContext(), "hasNextProductPage Information  " + hasNextProductPage, Toast.LENGTH_SHORT).show();
                            }
                        });

                        for (Storefront.ProductEdge productEdge : collection.getProducts().getEdges()) {


                            productPageCursor = "";
                            productPageCursor = productEdge.getCursor();
                            products.add(productEdge.getNode());


                            List<Storefront.ProductVariant> variantslist = new ArrayList<>();
                            for (Storefront.ProductVariantEdge variantEdge : productEdge.getNode().getVariants().getEdges()) {
                                variantslist.add(variantEdge.getNode());

                            }
                            List<Storefront.Image> imagelist = new ArrayList<>();
                            for (Storefront.ImageEdge productVariantEdge : productEdge.getNode().getImages().getEdges()) {
                                imagelist.add(productVariantEdge.getNode());
                            }


                            collectionModel = new CollectionModel();
                            for (int k = 0; k < variantslist.size(); k++) {


                                double price = Double.parseDouble(variantslist.get(k).get("price").toString());
                                double comparaterprice = Double.parseDouble(variantslist.get(k).getCompareAtPrice().toString());
                                BigDecimal ttldiscount = BigDecimal.valueOf((comparaterprice - price));

                                collectionModel.setName(variantslist.get(k).get("title").toString());
                                collectionModel.setCost((BigDecimal) variantslist.get(k).get("price"));
                                collectionModel.setVariantId(variantslist.get(k).getId().toString());
                                collectionModel.setCompareatprice(variantslist.get(k).getCompareAtPrice());
                                collectionModel.setDiscount(ttldiscount);
                                collectionModel.setAvailableSale(String.valueOf(variantslist.get(k).getAvailableForSale()));


                                for (int i = 0; i < imagelist.size(); i++) {
                                    String src = imagelist.get(i).get("src").toString();
                                    collectionModel.setImage(src);

                                }


                                //  gridList.add(collectionModel);
                            }

                            for (int j = 0; j < products.size(); j++) {

                                collectionModel.setProductId(products.get(j).getId().toString());
                                collectionModel.setTitle(products.get(j).getTitle().toString());
                                //  collectionModel.setDescripation(products.get(j).getDescription().toString());
                                collectionModel.setDescripation(products.get(j).getDescriptionHtml().toString());

                            }
                            gridList.add(collectionModel);
                        }

                        Collections.sort(gridList, new Comparator<CollectionModel>() {
                            @Override
                            public int compare(CollectionModel o1, CollectionModel o2) {
                                // return o1.getCompareatprice().compareTo(o2.getCompareatprice());
                                // String val = String.valueOf(discounts);
                                BigDecimal discounts = new BigDecimal(o1.getDiscount().compareTo(o2.getDiscount()));
                                //  BigDecimal discounts = new BigDecimal(String.valueOf(o1.getCompareatprice()));
                                double val = Double.valueOf(String.valueOf(discounts));
                                return (int) val;
                            }
                        });


                        productgridadpter = new Productgridadpter(getApplication(), gridList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                              /*  productgridadpter = new Productgridadpter(getApplication(), gridList);
                                gridView.setAdapter(productgridadpter);
                                layoutProgressBar.setVisibility(View.GONE);*/
                                layoutProgressBar.setVisibility(View.GONE);
                                if (Util.currentPage == 1) {
                                    // productgridadpter = new Productgridadpter(getApplication(), gridList);
                                    productgridadpter.notifyDataSetChanged();
                                    //  gridView.setAdapter(productgridadpter);
                                    // layoutProgressBar.setVisibility(View.GONE);
                                    // Toast.makeText(getApplicationContext(), " Called only  Grid View  adpter called ", Toast.LENGTH_SHORT).show();
                                } else {
                                    //  productgridadpter = new Productgridadpter(getApplication(), gridList);
                                    gridView.setAdapter(productgridadpter);
                                    //   layoutProgressBar.setVisibility(View.GONE);
                                    // Toast.makeText(getApplicationContext(), " Set adpter is also called ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        ProgrssbarActivity.instance.finish();


                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(@NonNull GraphError error) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }


}



