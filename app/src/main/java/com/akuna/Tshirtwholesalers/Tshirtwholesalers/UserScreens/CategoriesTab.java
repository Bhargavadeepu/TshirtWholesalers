package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CategoryAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.GridViewAdpter;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CategoriesTab extends Fragment {

    RecyclerView recyclerview;
    CategoryAdpter categoryAdpter;
    private GraphClient graphClient;
    //  ArrayList<String> items = new ArrayList<>();
    HashSet<String> items = new HashSet<>();
    private ProgressDialog progressDialog;
    View progressBar_layout;
    View rootView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        progressBar_layout = rootView.findViewById(R.id.progressBar_layout);
        progressBar = rootView.findViewById(R.id.progressBar);

        // serviceCall(rootView);
        //  progressDialog = new ProgressDialog(getActivity());
        //  progressDialog = new ProgressDialog(getActivity());
        //  progressDialog = new ProgressDialog(getActivity());
        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();

     /*   if (progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
*/
        return rootView;
    }


    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            serviceCall(rootView);
            return null;
        }
    }


    private void serviceCall(View view) {

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            Util.hideSoftKeyBoard(getActivity(), view);


            graphClient = Util.graphclient(getActivity());
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


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerview.setHasFixedSize(true);
                                categoryAdpter = new CategoryAdpter((ArrayList<String>) itemsList, getActivity());
                                recyclerview.setAdapter(categoryAdpter);
                                progressBar_layout.setVisibility(View.GONE);

                            }
                        });

                        Util.dismissProgress();

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(@NonNull GraphError error) {
                    //   progressDialog.dismiss();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getActivity(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });


        } else {
            Toast.makeText(getActivity(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }

}
