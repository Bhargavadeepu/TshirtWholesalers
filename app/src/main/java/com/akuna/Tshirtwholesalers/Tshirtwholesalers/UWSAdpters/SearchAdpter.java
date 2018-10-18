package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FovuroriteModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchAdpter extends BaseAdapter {

    String imagUri, title;
    private Context context;
    private List<CollectionModel> searchList = new ArrayList<>();
    private LayoutInflater inflater;
    private DBManager dbManager;
    private int favSelection = 0;
    private ArrayList<FovuroriteModel> favoriteArrylist;
    int favId;
    CollectionModel collectionModel;

    public SearchAdpter(Context context, List<CollectionModel> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        dbManager = new DBManager(context);
        dbManager.open();


        final ImageView img_fav, img_share, item_image;
        final TextView item_name, item_cost, tv_discountamnt;
        View relaytive_layout;

        View gridVw = view;
      /*  if (view == null) {

        } else {
            gridVw = (View) view;
        }
*/
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridVw = inflater.inflate(R.layout.productgridvwadpter, null);

        img_fav = gridVw.findViewById(R.id.img_fav);
        img_share = gridVw.findViewById(R.id.img_share);
        item_image = gridVw.findViewById(R.id.item_image);
        item_name = gridVw.findViewById(R.id.item_name);
        item_cost = gridVw.findViewById(R.id.item_cost);
        tv_discountamnt = gridVw.findViewById(R.id.tv_discountamnt);
        relaytive_layout = gridVw.findViewById(R.id.relaytive_layout);


        try {
            collectionModel = searchList.get(position);
            item_name.setText(collectionModel.getTitle());
            item_cost.setText("$" + collectionModel.getCost());
            imagUri = collectionModel.getImage();
            Picasso.with(context).load(imagUri).placeholder(R.drawable.gallerypic).fit().centerInside().into(item_image);
            title = collectionModel.getTitle();
        } catch (Exception e) {

            //  Toast.makeText(context, " Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        try {
            favoriteArrylist = dbManager.GetFavouriteDetails();
            if (favoriteArrylist == null || favoriteArrylist.size() == 0) {

            } else {

                for (int i = 0; i < favoriteArrylist.size(); i++) {

                    if (collectionModel.getVariantId().equals(favoriteArrylist.get(i).getVariantID())) {

                        favSelection = 1;
                        img_fav.setImageResource(R.drawable.favselected);
                    } else {

                    }
                }
            }
        } catch (Exception e) {
            // Toast.makeText(context, " Excepation" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        relaytive_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comparterPrice = String.valueOf(collectionModel.getCompareatprice());
                String discountPrice = String.valueOf(collectionModel.getDiscount());


                String cost = String.valueOf(collectionModel.getCost());
                Intent selectedItem = new Intent(context, SelectedItemActivity.class);
                Util.discountPrice = collectionModel.getCost();
                if ((collectionModel.getCompareatprice() == null || collectionModel.getCompareatprice().equals("null"))) {
                    Util.actualPrice = BigDecimal.valueOf(00.0);

                } else {
                    Util.actualPrice = collectionModel.getCompareatprice();

                }

                selectedItem.putExtra("productId", collectionModel.getProductId());
                selectedItem.putExtra("variantId", collectionModel.getVariantId());
                selectedItem.putExtra("decripation", collectionModel.getDescripation());
                selectedItem.putExtra("price", cost);
                selectedItem.putExtra("title", collectionModel.getTitle());
                selectedItem.putExtra("comparterPrice", comparterPrice);
                selectedItem.putExtra("discountPrice", discountPrice);
                selectedItem.putExtra("avialbleforsale", collectionModel.getAvailableSale());
                selectedItem.putExtra("PageInfo", "cartAdd");
                selectedItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(selectedItem);

            }
        });


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Util.selectedImage = collectionModel.getImage();
                Util.imageBitmap = Util.getBitmapFromView(item_image);

                try {
                    File file = new File(context.getExternalCacheDir(), "TshirtWholesalers.png");
                    Util.imagFile = new File(context.getExternalCacheDir(), "TshirtWholesalers.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    Util.imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //String shareBodyText = "Check out what i have found on the uniform whole salers app.I think you will like Hard Yakka Cotton Drill. You should try it here :http://www.akunatech.com/"; Util.selectedImage
                    String shareBodyText = "Check out what i have found on the Tshirt Wholesalers app.I think you will like.You should try it here :https://www.tshirtwholesalers.com.au" + Util.imageBitmap;
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/png");
                    Intent chooserIntent = Intent.createChooser(intent, "Share image via");
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(chooserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), " Excepation " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "  Please set the premissions from the settings ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // if (favSelection == 0) {
                if (img_fav.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_action_fav).getConstantState()) {
                    FovuroriteModel fovuroriteModel = new FovuroriteModel();
                    fovuroriteModel.setProductID(collectionModel.getProductId());
                    fovuroriteModel.setAvailbleforSale(collectionModel.getAvailableSale());
                    fovuroriteModel.setImageSrc(collectionModel.getImage());
                    fovuroriteModel.setQuantity(1);
                    fovuroriteModel.setVariantID(collectionModel.getVariantId());
                    fovuroriteModel.setTitle(collectionModel.getTitle());
                    fovuroriteModel.setSize("Small");
                    fovuroriteModel.setColor("Blue");
                    BigDecimal price = new BigDecimal(String.valueOf(collectionModel.getCost()));
                    double doubleprice = price.doubleValue();
                    BigDecimal ttlprice = new BigDecimal(String.valueOf(collectionModel.getCost()));
                    double ttldoubleprice = price.doubleValue();
                    fovuroriteModel.setTotalPrice(ttldoubleprice);
                    fovuroriteModel.setPrice(doubleprice);

                    if (Util.actualPrice == null) {
                        fovuroriteModel.setDiscount(0);
                    } else {
                        BigDecimal actulaitemprice = BigDecimal.valueOf(0.0);
                        double actualprice = actulaitemprice.doubleValue();
                        fovuroriteModel.setDiscount(actualprice);
                    }
                    dbManager.InsertFavouriteDetails(fovuroriteModel);
                    favSelection = 1;
                    img_fav.setImageResource(R.drawable.favselected);
                    Toast.makeText(context, " Item added to Favourites", Toast.LENGTH_SHORT).show();

                } else {

                    favoriteArrylist = dbManager.GetFavouriteDetails();

                    if (favoriteArrylist == null || favoriteArrylist.size() == 0) {


                    } else {

                        for (int i = 0; i < favoriteArrylist.size(); i++) {
                            favId = favoriteArrylist.get(i).getFavId();
                        }
                        dbManager.deleteFavourite(favId);

                    }

                    favSelection = 0;
                    img_fav.setImageResource(R.drawable.ic_action_fav);
                    Toast.makeText(context, " Item removed from Favourites", Toast.LENGTH_SHORT).show();


                }


            }
        });

        return gridVw;
    }
}
