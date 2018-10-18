package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CategoryAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.Productgridadpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.SearchAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.RetryHandler;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class LivesearchActivity extends AppCompatActivity {

    private GraphClient graphClient;
    ListView list_vw;
    Button btn_sort;
    ArrayAdapter<String> items_adapter;
    SearchAdpter searchAdpter;
    List<String> itemsarrayList = new ArrayList<>();
    List<String> search_result_arraylist = new ArrayList<>();
    List<CollectionModel> gridList = new ArrayList<>();
    CollectionModel collectionModel;
    List<CollectionModel> searchList = new ArrayList<>();
    GridView gridView;
    List<String> sortingList = new ArrayList<>();
    private String pageData;
    private ProgressDialog progressDialog;
    String productPageCursor, searchData;
    int itemCount = 0;
    FloatingActionButton searchfab;
    View layoutProgressBar;
    Animation slideUp, slideDown;
    RecyclerView recyclerview;
    CategoryAdpter categoryAdpter;
    HashSet<String> items = new HashSet<>();
    TextView tv_searchtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();


        btn_sort.setVisibility(View.GONE);
        Intent i = getIntent();
        pageData = i.getExtras().getString("PageInfo");

        if (pageData.equals("NoData")) {

        } else {
            // searchItem(pageData);
            productApicall(pageData);
        }
        layoutProgressBar.setVisibility(View.GONE);
        btn_sort.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        searchfab.setVisibility(View.GONE);


        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   sortpopup();

                Intent popup = new Intent(LivesearchActivity.this, AlertpopupAcitivtiy.class);
                startActivity(popup);
                overridePendingTransition(R.anim.slidedown, R.anim.slidedown);

            }
        });


        searchfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productApicall(searchData);
                layoutProgressBar.setVisibility(View.VISIBLE);
                btn_sort.setVisibility(View.GONE);
            }
        });

        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();


        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);

        /*********** Grid View ClickListener ***************/


        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    // here you have reached end of list, load more data
                    //  fetchMoreItems();
                    if (itemCount != 0) {
                        // Toast.makeText(getApplicationContext(), " End has beeen reached ", Toast.LENGTH_SHORT).show();
                        // productApicall(searchData);
                        // Toast.makeText(getApplicationContext(), " Page Data  " + searchData, Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //blank, not required in your case
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    //   Toast.makeText(getApplicationContext(), " Stopped Scrolling  ", Toast.LENGTH_SHORT).show();
                    btn_sort.setVisibility(View.VISIBLE);
                    btn_sort.setAnimation(slideUp);

                } else {
                    btn_sort.setVisibility(View.GONE);
                    btn_sort.setAnimation(slideDown);
                }


            }
        });

    }

    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            serviceCall();
            return null;
        }
    }


    private void initViews() {

        //  list_vw = findViewById(R.id.list_vw);
        gridView = findViewById(R.id.gridView);
        btn_sort = findViewById(R.id.btn_sort);
        // progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        searchfab = (FloatingActionButton) findViewById(R.id.search_fab);
        layoutProgressBar = findViewById(R.id.layoutProgressBar);
        recyclerview = findViewById(R.id.recyclerview);
        tv_searchtxt = findViewById(R.id.tv_searchtxt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.search_view_menu_item, menu);

        MenuItem search_item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search_item.getActionView();

        searchView.setFocusable(true);
        searchView.setQueryHint("Search");
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData = s;
                searchView.setIconified(false);
                tv_searchtxt.setText("Searching for  " + searchData);
                //  searchItem(s);
                productApicall(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                /*items_adapter.getFilter().filter(newText);*/

                String quertytxt = newText;

                if (quertytxt.isEmpty()) {
                    searchList.clear();
                    recyclerview.setVisibility(View.VISIBLE);
                    tv_searchtxt.setText("Search");
                    searchfab.setVisibility(View.GONE);
                    btn_sort.setVisibility(View.GONE);

                } else {
                    recyclerview.setVisibility(View.GONE);

                }


                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    private void searchItem(String searchtxt) {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String querytxt = searchtxt;

        graphClient = Util.graphclient(getApplicationContext());

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .shop(shop -> shop
                        .products(arg -> arg.first(100).query(querytxt), connection -> connection
                                .edges(edges -> edges
                                        .node(productQuery -> productQuery
                                                .title()
                                                .productType()
                                                .description()
                                                .descriptionHtml()

                                                .images(arg -> arg.first(15), imageConnectionQuery -> imageConnectionQuery
                                                        .edges(imageEdgeQuery -> imageEdgeQuery
                                                                .node(imageQuery -> imageQuery
                                                                        .src()
                                                                )
                                                        )
                                                )
                                                .variants(arg -> arg.first(15), variantConnectionQuery -> variantConnectionQuery
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

                if (!Objects.equals(response.data().getShop().getProducts().getEdges().size(), 0)) {
                    List<Storefront.Product> products = new ArrayList<>();
                    for (Storefront.ProductEdge productEdge : response.data().getShop().getProducts().getEdges()) {
                        //products found is an already initialized arraylist
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
                            collectionModel.setDescripation(products.get(j).getDescriptionHtml().toString());

                        }

                        searchList.add(collectionModel);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                searchAdpter = new SearchAdpter(getApplicationContext(), searchList);
                                gridView.setAdapter(searchAdpter);

                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), " No Matches found", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }


            @Override
            public void onFailure(@NonNull GraphError error) {
                //   Util.dismissProgress();
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //   Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

        });


    }


    private void sortpopup() {


        sortingList.add("Pirce - High-Low");
        sortingList.add("Pirce - Low-High");
        sortingList.add("Discount");
        sortingList.add("New Collections");

        ImageView img_close;
        ListView listvw_sortitems;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.sortpopup, null);

        img_close = mView.findViewById(R.id.img_close);
        listvw_sortitems = mView.findViewById(R.id.listvw_sortitems);


        ArrayAdapter listAdpter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sortingList);
        listvw_sortitems.setAdapter(listAdpter);


        final AlertDialog dialog;
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        listvw_sortitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //     Toast.makeText(LivesearchActivity.this, "You had clicked over item :" + position, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                searchList.clear();

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    private void productApicall(String data) {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (itemCount == 1) {

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

                    if (!Objects.equals(response.data().getShop().getProducts().getEdges().size(), 0)) {
                        List<Storefront.Product> products = new ArrayList<>();
                        for (Storefront.ProductEdge productEdge : response.data().getShop().getProducts().getEdges()) {
                            //products found is an already initialized arraylist
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
                                collectionModel.setDescripation(products.get(j).getDescriptionHtml().toString());

                            }

                            searchList.add(collectionModel);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchfab.setVisibility(View.VISIBLE);
                                    btn_sort.setVisibility(View.VISIBLE);
                                    gridView.setVisibility(View.VISIBLE);
                                    searchAdpter = new SearchAdpter(getApplicationContext(), searchList);
                                    if (itemCount == 0) {
                                        gridView.setAdapter(searchAdpter);
                                        itemCount = 1;
                                    } else {
                                        searchAdpter.notifyDataSetChanged();
                                    }
                                    tv_searchtxt.setText("Searching  result for " + searchData);
                                    layoutProgressBar.setVisibility(View.GONE);
                                    searchfab.setVisibility(View.VISIBLE);
                                    recyclerview.setVisibility(View.GONE);
                                }
                            });
                        }
                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " No Matches found", Toast.LENGTH_SHORT).show();

                            }
                        });

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

    private void serviceCall() {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {


            graphClient = Util.graphclient(getApplicationContext());
            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shopQuery -> shopQuery
                            // .collections(arg -> arg.first(2), collectionConnectionQuery -> collectionConnectionQuery Bhargava Collection
                            //   Bisley Workwear
                            .collections(arg -> arg.first(30), collectionConnectionQuery -> collectionConnectionQuery
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
                        //   progressDialog.dismiss();

                        List<Storefront.Collection> collections = new ArrayList<>();
                        for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                            collections.add(collectionEdge.getNode());
                        }


                        for (int i = 0; i < collections.size(); i++) {

                           /* if (collections.get(i).getTitle().contains("Ascolour") || collections.get(i).getTitle().contains("Gildan") || collections.get(i).getTitle().contains("Biz Collection") || collections.get(i).getTitle().contains("Anvil")) {
                                items.add(collections.get(i).getTitle());
                            } else {

                            }*/
                            items.add(collections.get(i).getTitle());

                        }


                        List<String> itemsList = new ArrayList<String>(items);

                        Collections.sort(itemsList);


                        // Collections.sort(items);
                  /*  Collections.sort(gridList, new Comparator<CollectionModel>() {
                        @Override
                        public int compare(CollectionModel o1, CollectionModel o2) {
                            return o1.getDiscount().compareTo(o2.getDiscount());
                        }
                    });*/


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerview.setHasFixedSize(true);
                                categoryAdpter = new CategoryAdpter((ArrayList<String>) itemsList, getApplicationContext());
                                recyclerview.setAdapter(categoryAdpter);
                            }
                        });

                        Util.dismissProgress();

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(@NonNull GraphError error) {
                    //   progressDialog.dismiss();
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

    private void sortServiceCall(String data) {
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
                            searchList.add(collectionModel);
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
                          //  searchAdpter

                           /* Productgridadpter productgridadpter = new Productgridadpter(getApplication(), gridList);
                            gridView.setAdapter(productgridadpter);*/

                            searchAdpter = new SearchAdpter(getApplicationContext(),searchList);
                            gridView.setAdapter(searchAdpter);

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

}
