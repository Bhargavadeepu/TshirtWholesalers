package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionnewModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CategorieItemAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CollectionsAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.GridViewAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.ViewPagerAdapter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Live Project

public class DashboardTab extends Fragment {
    private ProgressDialog progressDialog;
    ViewPager viewPager;
    CategorieItemAdpter categorieItemAdpter;
    LinearLayout sliderDotspanel;
    CollectionModel collectionModel;
    GridLayout gridlayout;
    ImageView img_bizcollection, img_hardyakka, img_citycollection, img_kingee, img_sysmik;
    private int dotscount;
    private ImageView[] dots;
    private GraphClient graphClient;
    ImageView icon;
    ArrayList<Integer> itemImg = new ArrayList<>();
    List<CollectionModel> gridList = new ArrayList<>();
    ExpandableHeightGridView expandableHeightGridView;
    View rootView;
    String productPageCursor;
    int initload = 0;
    boolean hasNextProductPage;
    View layoutProgressBar;
    RecyclerView recyclerview_collections;
    CollectionsAdpter collectionsAdpter;
    CollectionnewModel collectionnewModel;
    List<CollectionnewModel> collectionList = new ArrayList<>();
    ProgressBar progressbar, progressbar2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
/*
        img_bizcollection = rootView.findViewById(R.id.img_bizcollection);
        img_hardyakka = rootView.findViewById(R.id.img_hardyakka);
        img_kingee = rootView.findViewById(R.id.img_kingee);
        img_citycollection = rootView.findViewById(R.id.img_citycollection);*/

        recyclerview_collections = rootView.findViewById(R.id.recyclerview_collections);

        layoutProgressBar = rootView.findViewById(R.id.layoutProgressBar);

        progressbar = rootView.findViewById(R.id.progressbar);
        progressbar2 = rootView.findViewById(R.id.progressBar2);

        if (progressbar != null) {
            progressbar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        /** Api call for the Featured collecdtions***/
        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();
        //  featuredCollections(rootView);
        expandableHeightGridView = rootView.findViewById(R.id.gridView);


        icon = rootView.findViewById(R.id.icon);
        gridlayout = rootView.findViewById(R.id.gridlayout);


        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);

        categorieItemAdpter = new CategorieItemAdpter(itemImg, getActivity());


     /*   img_bizcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent products = new Intent(getActivity(), ProductListAcitvity.class);
                products.putExtra("pageinfo", "Biz Collection");
                // products.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzg3NTI2NjA3NA==");
                startActivity(products);

            }
        });


        img_hardyakka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent products = new Intent(getActivity(), ProductListAcitvity.class);
                products.putExtra("pageinfo", "Anvil");
                //  products.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzgxODIxNjk4Ng==");
                startActivity(products);

            }
        });


        img_citycollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent products = new Intent(getActivity(), ProductListAcitvity.class);
                products.putExtra("pageinfo", "Gildan");
                //  products.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzQ0MTA2NDY1Mw==");
                startActivity(products);

            }
        });


        img_kingee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent products = new Intent(getActivity(), ProductListAcitvity.class);
                products.putExtra("pageinfo", "Ascolour");
                // products.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzQ0MDk2OTc0MQ==");
                startActivity(products);
                //pagination();
            }
        });*/

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);


        // Viewpage Adpter   click listner


        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent itemsItent = new Intent(getActivity(), SelectedItemActivity.class);
                // Intent itemsItent = new Intent(getActivity(), ProgrssbarActivity.class);
                startActivity(itemsItent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });


        expandableHeightGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //    Toast.makeText(getActivity(), " Scroll event changed event  ",
                // .LENGTH_SHORT).show();


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // End has been reached

                    //Toast.makeText(getApplicationContext(), " Scroll end has been reched ", Toast.LENGTH_SHORT).show();
                    //    isScrollCompleted();

                }

            }
        });


        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }


        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //  pagination();

        return rootView;
    }


    private void isScrollCompleted() {

        //  featuredCollections();
        //

        if (initload == 0) {
        } else {
            //  pagination();

            //    Toast.makeText(getActivity(), " page Scroll called ", Toast.LENGTH_SHORT).show();


        }

    }

    public void shareImage() {


        Bitmap bitmaps = Util.getBitmapFromView(img_bizcollection);

        Util.imageBitmap = Util.getBitmapFromView(img_bizcollection);

        try {
            File file = new File(getActivity().getExternalCacheDir(), "Fotballjersey.png");
            Util.imagFile = new File(getActivity().getExternalCacheDir(), "Fotballjersey.png");


            FileOutputStream fOut = new FileOutputStream(file);
            bitmaps.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String shareBodyText = "I love using TshirtWholesalers App, it's simple and incredible. You should try it here :http://www.akunatech.com/";
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void featuredCollections(View view) {

        if (CheckNetwork.isInternetAvailable(getActivity())) {
            //    Util.hideSoftKeyBoard(getActivity(), view);
            //   Util.showProgress(getActivity(), view);
            graphClient = Util.graphclient(getActivity());
            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shopQuery -> shopQuery
                            // .collections(arg -> arg.first(2), collectionConnectionQuery -> collectionConnectionQuery Bhargava Collectiona
                            //   Bisley Workwear
                            .collections(arg -> arg.first(20), collectionConnectionQuery -> collectionConnectionQuery
                                    //     .collections(arg -> arg.first(1).query("Ascolour Sweats"), collectionConnectionQuery -> collectionConnectionQuery
                                    //  .collections(arg -> arg.first(1).query("Kids"), collectionConnectionQuery -> collectionConnectionQuery
                                    //  .collections(arg -> arg.first(25), collectionConnectionQuery -> collectionConnectionQuery
                                    .edges(collectionEdgeQuery -> collectionEdgeQuery
                                            .node(collectionQuery -> collectionQuery
                                                    .title()
                                                    .image(arg -> arg.originalSrc())
                                                    .products(arg -> arg.first(20), productConnectionQuery -> productConnectionQuery
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


                    try {
                        List<Storefront.Collection> collections = new ArrayList<>();
                        for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                            collections.add(collectionEdge.getNode());

                            String collectiontitle = String.valueOf(collectionEdge.get("title"));

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

                                    try {
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


                                    } catch (Exception e) {


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


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GridViewAdpter adpter = new GridViewAdpter(getActivity(), gridList);
                                expandableHeightGridView.setAdapter(adpter);
                                expandableHeightGridView.setExpanded(true);
                                layoutProgressBar.setVisibility(View.GONE);
                                progressbar2.setVisibility(View.GONE);
                            }
                        });


                    } catch (Exception e) {


                    }


                }


                @Override
                public void onFailure(@NonNull GraphError error) {

                    //  Util.dismissProgress();

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

    private void pagination() {

        graphClient = Util.graphclient(getActivity());

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID("Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzQ0MDk2OTc0MQ=="), nodeQuery -> nodeQuery
                        .onCollection(collectionQuery -> collectionQuery
                                .products(
                                        args -> args
                                                .first(5)
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
                    Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
                    hasNextProductPage = collection.getProducts().getPageInfo().getHasNextPage();
                    List<Storefront.Product> products = new ArrayList<>();


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            //         Toast.makeText(getActivity(), " productPageCursor  " + productPageCursor, Toast.LENGTH_SHORT).show();

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


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GridViewAdpter adpter = new GridViewAdpter(getActivity(), gridList);
                            expandableHeightGridView.setAdapter(adpter);
                            expandableHeightGridView.setExpanded(true);
                            initload = 1;
                        }
                    });

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }
        });


    }


    /****  Api call For the Collections ****/


    private void collectionServiceCall() {


        if (CheckNetwork.isInternetAvailable(getActivity())) {

            graphClient = Util.graphclient(getActivity());


            /*Intent dailogue = new Intent(getActivity(), ProgrssbarActivity.class);
            startActivity(dailogue);*/


            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .shop(shopQuery -> shopQuery
                            .collections(arg -> arg.first(32), collectionConnectionQuery -> collectionConnectionQuery

                                    .edges(collectionEdgeQuery -> collectionEdgeQuery
                                            .node(collectionQuery -> collectionQuery
                                                    .title()
                                                    .image(arg -> arg.originalSrc())
                                                    .products(arg -> arg.first(1), productConnectionQuery -> productConnectionQuery
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


                    try {

                        //  collectionnewModel = new CollectionnewModel();

                        List<Storefront.Collection> collections = new ArrayList<>();
                        for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                            collections.add(collectionEdge.getNode());


                        }

                        for (int i = 0; i < collections.size(); i++) {


                            CollectionnewModel collectionnewModel = new CollectionnewModel();

                            try {
                                if (collections.get(i).getImage().getOriginalSrc() == null) {

                                } else {
                                    collectionnewModel.setCollectionId(String.valueOf(collections.get(i).getId()));
                                    collectionnewModel.setImgSrc(collections.get(i).getImage().getOriginalSrc());
                                    collectionnewModel.setTitle(collections.get(i).getTitle());
                                    collectionList.add(collectionnewModel);

                                }

                            } catch (Exception e) {


                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    //  Toast.makeText(getActivity(), " List Size " + collectionList.size(), Toast.LENGTH_SHORT).show();

                                    recyclerview_collections.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerview_collections.setHasFixedSize(true);
                                    collectionsAdpter = new CollectionsAdpter(collectionList, getActivity());
                                    recyclerview_collections.setAdapter(collectionsAdpter);

                                    progressbar.setVisibility(View.GONE);

                                }
                            });


                        }


                        //      }


                    } catch (Exception e) {


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //  Toast.makeText(getActivity(), " Excepation  " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                }

                @Override
                public void onFailure(@NonNull GraphError error) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getActivity(), " Error   " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });


        } else {
            Toast.makeText(getActivity(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }

    }


    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            //  featuredCollections(rootView);
            // pagination();

            collectionServiceCall();
            return null;
        }
    }


}
