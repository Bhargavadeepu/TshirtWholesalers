package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CategoryAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.SubcategorieAdpter;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SubcatogoriesListActivity extends AppCompatActivity {


    RecyclerView subcatgorie_recyclerview;
    private GraphClient graphClient;
    String pageData;
    HashSet<String> subcategorieList = new HashSet<>();
    SubcategorieAdpter subcategorieAdpter;
    private ProgressDialog progressDialog;
    private ProgressBar loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcatogories_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        Intent i = getIntent();
        pageData = i.getExtras().getString("PageInfo");
        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();

      /*  if (loadingbar != null) {
            loadingbar.getIndeterminateDrawable().setColorFilter(0xFF00FF00, android.graphics.PorterDuff.Mode.MULTIPLY);
        }*/


    }
    private void initViews() {
        subcatgorie_recyclerview = findViewById(R.id.subcatgorie_recyclerview);
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        loadingbar = findViewById(R.id.loadingbar);
    }


    public void btnback_OnClick(View view) {
        finish();
    }

    private void subcatogoriesServiceCall(String colletionname) {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {

            graphClient = Util.graphclient(getApplicationContext());
            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shopQuery -> shopQuery
                            .collections(arg -> arg.first(1).query(colletionname), collectionConnectionQuery -> collectionConnectionQuery
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
                        }

                        for (int i = 0; i < products.size(); i++) {

                            String productType = products.get(i).getProductType().toString();
                            subcategorieList.add(products.get(i).getProductType().toString());

                        }


                        List<String> subcategorieLists = new ArrayList<>(subcategorieList);
                        Collections.sort(subcategorieLists);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                subcatgorie_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                subcatgorie_recyclerview.setHasFixedSize(true);
                                subcategorieAdpter = new SubcategorieAdpter(getApplicationContext(), subcategorieLists);
                                subcatgorie_recyclerview.setAdapter(subcategorieAdpter);
                                loadingbar.setVisibility(View.GONE);

                            }
                        });


                    }



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


        } else {

            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }

    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            subcatogoriesServiceCall(pageData);

            return null;
        }
    }

}
