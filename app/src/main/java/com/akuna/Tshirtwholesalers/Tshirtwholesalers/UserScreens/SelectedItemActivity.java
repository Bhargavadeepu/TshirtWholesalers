package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.GridViewAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.SelecteditemAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SelectedItemActivity extends AppCompatActivity {

    public static SelectedItemActivity instance = null;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Spinner spinner_size, spinner_color;
    View img_layout;
    private ProgressDialog progressDialog;
    String productid, variantId, descripation, cost, discountPrice, compareterPrice,
            title, selectedSize, selectedColor, selectedImage, avilableforsale, pageInfo;
    TextView tv_itemname, tv_itemprice, tv_cartCount, tv_discountprice;
    List<String> sizelist;
    List<String> colorList;
    private GraphClient graphClient;
    WebView tv_html;
    List<String> imageList;
    SelecteditemAdpter selecteditemAdpter;
    private DBManager dbManager;
    private Button btn_addedtocart, btn_buynow;
    private List<Storefront.Image> productImages;
    private ArrayList<CartModel> cartModelArrayList;
    private ArrayList<FovuroriteModel> favoriteArrylist;
    private int itemposition;
    private boolean isSpinnerInitial = true;
    private boolean isSpinnerInitialcolor = true;
    int cartId, favId, favouriteId;
    private int favSelection = 0;
    private ImageView img_favicon;
    private HashSet<String> uniqImages;
    private HashSet<String> uniqColor;
    private HashSet<String> uniqSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        Intent data = getIntent();
        productid = data.getExtras().getString("productId");
        variantId = data.getExtras().getString("variantId");
        Util.variantId = data.getExtras().getString("variantId");
        // price = BigDecimal.valueOf(data.getExtras().getInt("price"));
        cost = (data.getExtras().getString("price"));
        discountPrice = (data.getExtras().getString("discountPrice"));
        descripation = data.getExtras().getString("decripation");
        avilableforsale = data.getExtras().getString("avialbleforsale");
        pageInfo = data.getExtras().getString("PageInfo");
        compareterPrice = (data.getExtras().getString("comparterPrice"));
        title = data.getExtras().getString("title");
        favouriteId = data.getExtras().getInt("favId");
        cartId = data.getExtras().getInt("cartId");
        //   tv_html.loadData(descripation, "text/html; charset=UTF-8", null);
        tv_html.loadData(descripation, "text/html; UTF-16", null);
        tv_itemprice.setText("$" + cost);
        tv_itemname.setText(title);

        try {

            if (Util.discountPrice.compareTo(Util.actualPrice) == 0) {
                tv_discountprice.setText("");
                tv_discountprice.setVisibility(View.INVISIBLE);
            } else {
                //  tv_discountprice.setText("$" + discountPrice);
                tv_discountprice.setText("$" + compareterPrice);

                tv_discountprice.setPaintFlags(tv_discountprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

        } catch (Exception e) {


        }


        cartSizeBinding();

        favoriteArrylist = dbManager.GetFavouriteDetails();

        if (favoriteArrylist == null || favoriteArrylist.size() == 0) {

        } else {

            for (int i = 0; i < favoriteArrylist.size(); i++) {

                if (variantId.equals(favoriteArrylist.get(i).getVariantID())) {

                    favSelection = 1;
                    img_favicon.setImageResource(R.drawable.favselected);
                } else {

                }
            }
        }


        /* Product Api Call */

        productServiceCall();
     /*   Asyntaskrunner asyntaskrunner = new Asyntaskrunner();
        asyntaskrunner.execute();*/


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        //  spinner_size.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //  spinner_color.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        spinner_size.setPopupBackgroundResource(R.color.infotxtcolor);
       // spinner_size.setDropDownWidth(100);


        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                  //  ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedSize = spinner_size.getSelectedItem().toString();
                } catch (Exception e) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_color.setPopupBackgroundResource(R.color.infotxtcolor);

        spinner_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                  //  ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedColor = spinner_color.getSelectedItem().toString();
                    itemposition = position;
                    selectedImage = imageList.get(itemposition);
                    viewpagerScrollingPosition(itemposition);
                } catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();

        //    Toast.makeText(getApplicationContext(), " onPause Called ", Toast.LENGTH_SHORT).show();
        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            tv_cartCount.setVisibility(View.INVISIBLE);
        } else {
            tv_cartCount.setText("" + cartModelArrayList.size());
        }


    }

    /****   Add to cart Button Click listner***/
    public void btn_onclickaddtocart(View view) {

        try {
            if (selectedSize.contains("Select Size")) {
                Toast.makeText(getApplicationContext(), " Please select size", Toast.LENGTH_SHORT).show();
            } else if (selectedColor.contains("Select Color")) {
                Toast.makeText(getApplicationContext(), " Please select Color", Toast.LENGTH_SHORT).show();
            } else {
                cartButtonAnimation("Addtocart");
            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), " Unable to add to cart ", Toast.LENGTH_SHORT).show();
        }


    }


    /***   Button buy click event  ***/

    public void btnback_OnClickbuynw(View view) {

        try {
            if (selectedColor.contains("Select Color")) {
                Toast.makeText(getApplicationContext(), " Please select Color", Toast.LENGTH_SHORT).show();
            } else if (selectedSize.contains("Select size")) {
                Toast.makeText(getApplicationContext(), " Please select size", Toast.LENGTH_SHORT).show();
            } else {
                //  alertpopup(selectedSize, selectedColor, "BuyNow");
                cartButtonAnimation("BuyNow");
                pageInfo = "BuyNow";
            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), " Unavailble for sale ", Toast.LENGTH_SHORT).show();
        }


    }


    /*Search Clicklisnter*/
    public void btnsearch_OnClick(View view) {

        Intent search = new Intent(SelectedItemActivity.this, LivesearchActivity.class);
        search.putExtra("PageInfo", "NoData");
        startActivity(search);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /*Cart clicklistner */
    public void btncart_OnClick(View view) {

        Intent myCart = new Intent(SelectedItemActivity.this, MyCartActivity.class);
        startActivity(myCart);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


    }


    public void edt_Onclicksize(View view) {

        spinner_size.performClick();
    }


    public void edt_spinnercolor(View view) {

        spinner_color.performClick();

    }

    private void initViews() {
        spinner_size = findViewById(R.id.spinner_size);
        spinner_color = findViewById(R.id.spinner_color);
        img_layout = findViewById(R.id.img_layout);
        tv_itemname = findViewById(R.id.tv_itemname);
        tv_itemprice = findViewById(R.id.tv_itemprice);
        tv_html = findViewById(R.id.tv_html);
        tv_cartCount = findViewById(R.id.tv_cartCount);
        tv_discountprice = findViewById(R.id.tv_discountprice);
        img_favicon = findViewById(R.id.img_favicon);
        btn_addedtocart = findViewById(R.id.btn_addedtocart);
        btn_buynow = findViewById(R.id.btn_buynow);
        instance = this;
        dbManager = new DBManager(this);
        dbManager.open();
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
    }


    /* Back Arrow Click Listner*/

    public void btnback_OnClick(View view) {
        finish();
    }


    public void tv_onClickShare(View view) {

        //  Bitmap bitmaps = Util.getBitmapFromView(img_layout);
        Util.imageBitmap = Util.getBitmapFromView(img_layout);

        try {
            File file = new File(getApplicationContext().getExternalCacheDir(), "TshirtWholesalers.png");
            Util.imagFile = new File(getExternalCacheDir(), "TshirtWholesalers.png");
            FileOutputStream fOut = new FileOutputStream(file);
            Util.imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //String shareBodyText = "Check out what i have found on the uniform whole salers app.I think you will like Hard Yakka Cotton Drill. You should try it here :http://www.akunatech.com/";
            String shareBodyText = "Check out what i have found on the Tshirt Wholesalers app.I think you will like.You should try it here :https://www.tshirtwholesalers.com.au" + Util.selectedImage;
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), " Excepation " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "  Please set the premissions from the settings ", Toast.LENGTH_SHORT).show();
        }


    }


    /*  Favourite Click  */

    public void tv_onClickfavourite(View view) {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        img_favicon.startAnimation(myAnim);

        if (favSelection == 0) {

            FovuroriteModel fovuroriteModel = new FovuroriteModel();
            fovuroriteModel.setColor(selectedColor);
            fovuroriteModel.setProductID(productid);
            fovuroriteModel.setAvailbleforSale(avilableforsale);
            fovuroriteModel.setImageSrc(selectedImage);
            fovuroriteModel.setPrice(Double.parseDouble(cost));
            fovuroriteModel.setSize(selectedSize);
            fovuroriteModel.setQuantity(1);
            fovuroriteModel.setTotalPrice(Double.parseDouble(compareterPrice));
            fovuroriteModel.setVariantID(variantId);
            fovuroriteModel.setTitle(title);
            fovuroriteModel.setDescripation(descripation);
            fovuroriteModel.setComparaterPrice(Double.parseDouble(compareterPrice));


            if (Util.actualPrice == null) {
                fovuroriteModel.setDiscount(0);
            } else {
                BigDecimal actulaitemprice = BigDecimal.valueOf(0.0);
                double actualprice = actulaitemprice.doubleValue();
                fovuroriteModel.setDiscount(actualprice);
            }
            dbManager.InsertFavouriteDetails(fovuroriteModel);
            favSelection = 1;
            img_favicon.setImageResource(R.drawable.favselected);
            Toast.makeText(getApplicationContext(), " Item added to Favourites", Toast.LENGTH_SHORT).show();
        } else {

            /****  Event to delete the item from the WhishList  *****/

            favoriteArrylist = dbManager.GetFavouriteDetails();

            if (favoriteArrylist == null || favoriteArrylist.size() == 0) {
            } else {
                for (int i = 0; i < favoriteArrylist.size(); i++) {
                    favId = favoriteArrylist.get(i).getFavId();
                }
                dbManager.deleteFavourite(favId);

            }

            favSelection = 0;
            img_favicon.setImageResource(R.drawable.ic_action_fav);
            Toast.makeText(getApplicationContext(), " Item removed from Favourites", Toast.LENGTH_SHORT).show();
        }


    }


    private void productServiceCall() {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient = Util.graphclient(getApplicationContext());

        ID productId = new ID(productid);

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(productId, nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                // .products(arg -> arg.first(20), productConnectionQuery -> productConnectionQuery
                                .title()
                                .description()
                                .images(arg -> arg.first(30), imageConnectionQuery -> imageConnectionQuery
                                        .edges(imageEdgeQuery -> imageEdgeQuery
                                                .node(imageQuery -> imageQuery
                                                        .src()
                                                )
                                        )
                                )
                                .variants(arg -> arg.first(30), variantConnectionQuery -> variantConnectionQuery
                                        .edges(variantEdgeQuery -> variantEdgeQuery
                                                .node(productVariantQuery -> productVariantQuery
                                                        .price()
                                                        .title()
                                                        .compareAtPrice()
                                                        .availableForSale()
                                                        .image(Storefront.ImageQuery::src)
                                                        .selectedOptions(selectedOption -> selectedOption
                                                                .name()
                                                                .value()
                                                        )

                                                )
                                        )
                                )
                        )
                )
        );


        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                try {

                    progressDialog.dismiss();

                    Storefront.Product product = (Storefront.Product) response.data().getNode();
                    productImages = new ArrayList<>();
                    for (final Storefront.ImageEdge imageEdge : product.getImages().getEdges()) {
                        productImages.add(imageEdge.getNode());
                    }
                    List<Storefront.ProductVariant> variantslist = new ArrayList<>();
                    for (Storefront.ProductVariantEdge variantEdge : product.getVariants().getEdges()) {
                        variantslist.add(variantEdge.getNode());


                    }

                    uniqImages = new HashSet<>();
                    uniqColor = new HashSet<>();
                    uniqSize = new HashSet<>();
                    for (int k = 0; k < variantslist.size(); k++) {

                        String src = variantslist.get(k).getImage().getSrc();
                        uniqImages.add(src);


                        // imageList.add(src);

                        //  String title = variantslist.get(k).get("selectedOptions").toString();
                        List<Storefront.SelectedOption> title = variantslist.get(k).getSelectedOptions();


                        for (int x = 0; x < title.size(); x++) {

                            String name = title.get(x).getName().toString();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });

                            String size = title.get(x).getValue();

                            if (name.contains("Color")) {
                                uniqColor.add(size);
                            } else {
                                uniqSize.add(size);
                            }
                            // sizelist.add(size);
                        }
                        sizelist = new ArrayList<>(uniqSize);
                        colorList = new ArrayList<>(uniqColor);

                /*    for (int x = 1; x < title.size(); x++) {
                        String color = title.get(x).getValue();
                        uniqColor.add(color);
                        //colorList.add(color);

                    }
*/
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                /***   Color Adpter       ***/
                                ArrayAdapter colordpter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, colorList);
                                // colordpter.setDropDownViewResource(R.layout.spinner_item);
                                colordpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_color.setAdapter(colordpter);
                                /****  Size Adpter  ***/
                                ArrayAdapter sizeAdpter = new ArrayAdapter(getApplication(), R.layout.spinner_item, sizelist);
                                sizeAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_size.setAdapter(sizeAdpter);
                            }
                        });
                    }

                    imageList = new ArrayList<>(uniqImages);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            selecteditemAdpter = new SelecteditemAdpter(getApplicationContext(), imageList);
                            viewPager.setAdapter(selecteditemAdpter);
                            dotscount = imageList.size();
                            dots = new ImageView[dotscount];

                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(getApplicationContext());
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                sliderDotspanel.addView(dots[i], params);

                            }
                            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    for (int i = 0; i < dotscount; i++) {
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                                    }
                                    //   viewpagerScrollingPosition(position);

                                    try {
                                        String imageSelected = imageList.get(position);
                                        selectedImage = imageSelected;
                                    } catch (Exception e) {

                                    }

                                    //  Toast.makeText(getApplicationContext(), " Selected image URl  " + imageSelected, Toast.LENGTH_SHORT).show();

                                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                                }

                                @Override
                                public void onPageSelected(int position) {
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });


                        }
                    });
                } catch (Exception e) {


                }

            }


            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    private void alertpopup(String size, String color, String pagedata) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You have selected the following options");
        builder.setMessage("Size     :" + size + "\n" + "Color   :" + color)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /***  Inerting data into Local Db***/
                        inertingCartitemsData(pagedata);

                        // Make servive  call

                        //     productduplicateCheckServiceCall();

                    }
                })
                .setNegativeButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void inertingCartitemsData(String pagedata) {


        try {
            CartModel cartModel = new CartModel();
            cartModel.setProductID(productid);
            cartModel.setVariantID(variantId);
            cartModel.setSize(selectedSize);
            cartModel.setColor(selectedColor);
            cartModel.setImageSrc(selectedImage);
            // cartModel.setImageSrc(Util.selectedImage);
            cartModel.setAvailbleforSale(avilableforsale);
            cartModel.setTitle(title);
            cartModel.setPrice(cost);
            cartModel.setTotalPrice(Double.parseDouble(compareterPrice));
            cartModel.setQuantity("1");
            cartModel.setComparaterPrice(Double.parseDouble(compareterPrice));
            cartModel.setDescripation(descripation);

            //  cartModel.setPrice(String.valueOf(cost));

            cartModel.setDiscount(String.valueOf(Util.actualPrice));
            if (Util.discountPrice.compareTo(Util.actualPrice) == 0) {
                cartModel.setDiscount("0");
            } else {
                // cartModel.setDiscount(String.valueOf(Util.actualPrice));
                cartModel.setDiscount(discountPrice);
            }

            dbManager = new DBManager(this);
            dbManager.open();


            switch (pageInfo) {

                case "cartAdd":
                    dbManager.UpdateCartDetails(cartModel);
                    animation();
                    break;

                case "CartEdit":
                    cartModel.setCartId(cartId);
                    dbManager.updateCartItmesData(cartModel);
                    animation();
                    break;
                case "favourite":
                    dbManager.deleteFavourite(favouriteId);
                    dbManager.UpdateCartDetails(cartModel);
                    animation();
                    break;
                default:
                    Intent cartPage = new Intent(SelectedItemActivity.this, MyCartActivity.class);
                    startActivity(cartPage);
                    dbManager.UpdateCartDetails(cartModel);
            }


            try {
                //  cartModelArrayList = dbManager.GetCartDetails();
                if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
                    tv_cartCount.setVisibility(View.INVISIBLE);
                } else {
                    tv_cartCount.setText("" + cartModelArrayList.size());
                }
            } catch (Exception e) {

            }


        } catch (Exception e) {

        }


    }

    private void viewpagerScrollingPosition(int postion) {
        viewPager.setCurrentItem(postion);

    }

    private void cartButtonAnimation(String text) {

        if (btn_addedtocart.getText().toString().contains("Go To Cart")) {
            Intent myCart = new Intent(SelectedItemActivity.this, MyCartActivity.class);
            startActivity(myCart);

        } else {
            alertpopup(selectedSize, selectedColor, text);
        }

    }

    private void cartSizeBinding() {
        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            tv_cartCount.setVisibility(View.INVISIBLE);
        } else {
            tv_cartCount.setText("" + cartModelArrayList.size());
        }
    }


    private void productduplicateCheckServiceCall() {


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

        ID productId = new ID(productid);

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(productId, nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .description()
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
                                                        .image(Storefront.ImageQuery::src)
                                                        .selectedOptions(selectedOption -> selectedOption
                                                                .name()
                                                                .value()
                                                        )

                                                )
                                        )
                                )

                        )
                )
        );


        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                progressDialog.dismiss();
                Storefront.Product product = (Storefront.Product) response.data().getNode();
                productImages = new ArrayList<>();
                for (final Storefront.ImageEdge imageEdge : product.getImages().getEdges()) {
                    productImages.add(imageEdge.getNode());
                }
                List<Storefront.ProductVariant> variantslist = new ArrayList<>();
                for (Storefront.ProductVariantEdge variantEdge : product.getVariants().getEdges()) {
                    variantslist.add(variantEdge.getNode());
                }
                uniqImages = new HashSet<>();
                uniqColor = new HashSet<>();
                uniqSize = new HashSet<>();
                for (int k = 0; k < variantslist.size(); k++) {

                    String src = variantslist.get(k).getImage().getSrc();
                    uniqImages.add(src);
                    List<Storefront.SelectedOption> title = variantslist.get(k).getSelectedOptions();

                    // Condition to check wether the selected item is present are not
/*

                    if(){


                    }
*/


                }
            }


            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    private void animation() {
        Animation a = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        a.setRepeatCount(0);
        a.setDuration(500);
        btn_addedtocart.setAnimation(a);
        tv_cartCount.setVisibility(View.VISIBLE);
        btn_addedtocart.setText("Go To Cart");
        cartSizeBinding();
        Toast.makeText(getApplicationContext(), "Item Added to cart successfully", Toast.LENGTH_SHORT).show();
    }


    private class Asyntaskrunner extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            //    productServiceCall();
            return null;
        }
    }


}


