package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.AddressModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FavouriteItemsList;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FavouritesList;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FovuroriteModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MainActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyCartActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyFavouritesTab;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.PaymentMode;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;


import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class FavouriteAdpter extends RecyclerView.Adapter<FavouriteAdpter.ViewHolder> {

    private Context context;
    List<FovuroriteModel> favoriteArrylist;
    private DBManager dbManager;
    private String pagaInfo;
    private GraphClient graphClient;
    private List<Storefront.Image> productImages;
    List<String> imageList = new ArrayList<String>();
    AlertDialog dialog;
    String selectedColor, selectedSize;
    private HashSet<String> uniqImages;
    private HashSet<String> uniqColor;
    private HashSet<String> uniqSize;
    List<String> sizelist;
    List<String> colorList;
    Spinner spinner_color, spinner_size;
    private ProgressDialog progressDialog;

    public FavouriteAdpter(Context context, List<FovuroriteModel> favoriteArrylist, String pagaInfo) {
        this.context = context;
        this.favoriteArrylist = favoriteArrylist;
        this.pagaInfo = pagaInfo;
    }

    @NonNull
    @Override
    public FavouriteAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_template, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdpter.ViewHolder holder, int position) {


        progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dbManager = new DBManager(context);
        dbManager.open();

        final FovuroriteModel fovuroriteModel = favoriteArrylist.get(position);
        holder.tv_title.setText(fovuroriteModel.getTitle());
        holder.tv_price.setText("$" + fovuroriteModel.getPrice());
        String imagUri = fovuroriteModel.getImageSrc();
        Picasso.with(context).load(imagUri).fit().centerCrop().into(holder.img_item);


        if (fovuroriteModel.getDiscount() == 0) {
            holder.txt_actualprice.setText("");
        } else {
            holder.txt_actualprice.setText("$" + fovuroriteModel.getDiscount());
        }


        /**** Condition to hide the View more textview *****/

        if (pagaInfo.contains("tab")) {
        } else {
            holder.tv_viewmore.setVisibility(View.GONE);
        }


        if (fovuroriteModel.getAvailbleforSale().contains("false")) {

            holder.tv_addtocart.setText("  OUT OF STOCK  ");

        } else {
            holder.tv_addtocart.setText("  Add to cart  ");
        }

        holder.tv_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  giftCardPopup();


                ImageView img_close;
                Button btn_ok;


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                //  View mView = context.getLayoutInflater().inflate(R.layout.addsfavouritepopup, null);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View mView = inflater.inflate(R.layout.addsfavouritepopup, null);
                img_close = mView.findViewById(R.id.img_close);
                btn_ok = mView.findViewById(R.id.btn_ok);
                spinner_color = mView.findViewById(R.id.spinner_color);
                spinner_size = mView.findViewById(R.id.spinner_size);
                mBuilder.setView(mView);
                dialog = mBuilder.create();


                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                spinner_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selectedColor = spinner_color.getSelectedItem().toString();
                        //  Toast.makeText(context, " Selected Color " + selectedColor, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selectedSize = spinner_size.getSelectedItem().toString();
                        //   Toast.makeText(context, " Selected Size " + selectedSize, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                serviceCall(fovuroriteModel.getProductID());

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final FovuroriteModel fovuroriteModel = favoriteArrylist.get(position);

                        if (fovuroriteModel.getAvailbleforSale().contains("false")) {


                        } else {
                            CartModel cartModel = new CartModel();
                            cartModel.setPrice(String.valueOf(fovuroriteModel.getPrice()));
                            cartModel.setTotalPrice(fovuroriteModel.getTotalPrice());
                            cartModel.setQuantity(String.valueOf(fovuroriteModel.getQuantity()));
                            cartModel.setDiscount(String.valueOf(fovuroriteModel.getDiscount()));
                            cartModel.setTitle(fovuroriteModel.getTitle());
                            cartModel.setAvailbleforSale(fovuroriteModel.getAvailbleforSale());
                            cartModel.setImageSrc(fovuroriteModel.getImageSrc());
                            cartModel.setColor(selectedColor);
                            cartModel.setSize(selectedSize);
                            cartModel.setVariantID(fovuroriteModel.getVariantID());
                            cartModel.setProductID(fovuroriteModel.getProductID());
                            dbManager.UpdateCartDetails(cartModel);


                            /***  Deleting the Favourite Item***/


                            dbManager.deleteFavourite(fovuroriteModel.getFavId());
                            favoriteArrylist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(fovuroriteModel.getFavId(), favoriteArrylist.size());
                            notifyDataSetChanged();
                            dialog.dismiss();

                            Util.cartItembind = 1;

                        }

                    }
                });


            }
        });


        holder.tv_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vwMore = new Intent(context, FavouriteItemsList.class);
                vwMore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(vwMore);
            }
        });


        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you wish to remove the item from your Favourite List?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dbManager.deleteFavourite(fovuroriteModel.getFavId());
                                favoriteArrylist.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(fovuroriteModel.getFavId(), favoriteArrylist.size());
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fovuroriteModel.getAvailbleforSale().contains("false")) {


                } else {

                    String cost = String.valueOf(fovuroriteModel.getPrice());
                    String comparterPrice = String.valueOf(fovuroriteModel.getComparaterPrice());
                    double itemCost = Double.parseDouble(String.valueOf(fovuroriteModel.getPrice()));
                    Util.discountPrice = BigDecimal.valueOf(itemCost);
                    Util.actualPrice = BigDecimal.valueOf(fovuroriteModel.getComparaterPrice());
                    String discountPrice = String.valueOf(fovuroriteModel.getDiscount());
                    Intent selectedItem = new Intent(context, SelectedItemActivity.class);
                    selectedItem.putExtra("productId", fovuroriteModel.getProductID());
                    selectedItem.putExtra("variantId", fovuroriteModel.getVariantID());
                    selectedItem.putExtra("decripation", fovuroriteModel.getTitle());
                    selectedItem.putExtra("price", cost);
                    selectedItem.putExtra("title", fovuroriteModel.getTitle());
                    selectedItem.putExtra("discountPrice", discountPrice);
                    selectedItem.putExtra("comparterPrice", comparterPrice);
                    selectedItem.putExtra("avialbleforsale", fovuroriteModel.getAvailbleforSale());
                    selectedItem.putExtra("PageInfo", "favourite");
                    selectedItem.putExtra("decripation", fovuroriteModel.getDescripation());
                    selectedItem.putExtra("favId", fovuroriteModel.getFavId());
                    selectedItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(selectedItem);

                }


            }
        });


        // serviceCall(fovuroriteModel.getVariantID(), holder);

    }

    @Override
    public int getItemCount() {

        if (pagaInfo.contains("tab")) {
            return favoriteArrylist.size();
            // return 1;
        } else {

            return favoriteArrylist.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_title, tv_price, txt_actualprice, tv_addtocart,
                tv_viewmore, txt_offferprice, tv_discountpercentage;
        ImageView img_item, img_close;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
            tv_addtocart = itemView.findViewById(R.id.tv_addtocart);
            tv_viewmore = itemView.findViewById(R.id.tv_viewmore);
            img_item = itemView.findViewById(R.id.img_item);
            img_close = itemView.findViewById(R.id.img_close);
            txt_offferprice = itemView.findViewById(R.id.txt_offferprice);
            tv_discountpercentage = itemView.findViewById(R.id.tv_discountpercentage);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    // private void serviceCall(String VarintId, final FavouriteAdpter.ViewHolder holder) {
    private void serviceCall(String VarintId) {


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

        Util.getHttpmethod(context);
        graphClient = GraphClient.builder(context)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(context.getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();
        ID productVariantId = new ID(VarintId);

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(productVariantId, nodeQuery -> nodeQuery
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

        QueryGraphCall call = graphClient.queryGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
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

                    List<Storefront.SelectedOption> title = variantslist.get(k).getSelectedOptions();


                    for (int x = 0; x < title.size(); x = x + 2) {
                        String size = title.get(x).getValue();
                        uniqSize.add(size);
                        // sizelist.add(size);

                    }
                    sizelist = new ArrayList<>(uniqSize);

                    for (int x = 1; x < title.size(); x++) {
                        String color = title.get(x).getValue();
                        uniqColor.add(color);
                        //colorList.add(color);

                    }

                    colorList = new ArrayList<>(uniqColor);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            /***   Color Adpter       ***/
                            ArrayAdapter colordpter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, colorList);
                            colordpter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_color.setAdapter(colordpter);

                            /****  Size Adpter  ***/
                            ArrayAdapter sizeAdpter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sizelist);
                            sizeAdpter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_size.setAdapter(sizeAdpter);
                        }
                    });
                }

                imageList = new ArrayList<>(uniqImages);


                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();

                     /*   MainActivity.instance.finish();
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        context.startActivity(mainIntent);*/
                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                progressDialog.dismiss();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
