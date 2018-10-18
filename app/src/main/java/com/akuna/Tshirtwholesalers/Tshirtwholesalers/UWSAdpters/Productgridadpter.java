package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.FovuroriteModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyBounceInterpolator;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util.variantId;

public class Productgridadpter extends BaseAdapter {


    String imagUri, title;
    List<String> imageList = new ArrayList<String>();
    Integer[] images = {};
    private DBManager dbManager;
    private int favSelection = 0;
    private ArrayList<FovuroriteModel> favoriteArrylist;
    Productgridadpter.OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public Productgridadpter(Context context, List<CollectionModel> gridList) {
        this.context = context;
        this.gridList = gridList;
    }

    int favId;
    private LayoutInflater inflater;
    private Context context;
    List<CollectionModel> gridList = new ArrayList<>();

    @Override
    public int getCount() {
        //   return gridList.size();
        return gridList != null ? gridList.size() : 0;

    }

    @Override
    public Object getItem(int position) {
        return gridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ImageView img_fav, img_share, item_image;
        final TextView item_name, item_cost, tv_discountamnt, tv_dispercentage;
        View relaytive_layout, layout_item;
        View gridVw;

        //  if (convertView == null) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridVw = inflater.inflate(R.layout.productgridvwadpter, null);


        dbManager = new DBManager(context);
        dbManager.open();


        img_fav = gridVw.findViewById(R.id.img_fav);
        img_share = gridVw.findViewById(R.id.img_share);
        item_image = gridVw.findViewById(R.id.item_image);
        item_name = gridVw.findViewById(R.id.item_name);
        item_cost = gridVw.findViewById(R.id.item_cost);
        tv_discountamnt = gridVw.findViewById(R.id.tv_discountamnt);
        relaytive_layout = gridVw.findViewById(R.id.relaytive_layout);
        tv_dispercentage = gridVw.findViewById(R.id.tv_dispercentage);
        layout_item = gridVw.findViewById(R.id.layout_item);


        final CollectionModel collectionModel = gridList.get(position);
        try {
            item_name.setText(collectionModel.getTitle());
            item_cost.setText("$" + collectionModel.getCost());
            imagUri = collectionModel.getImage();
            Picasso.with(context).load(imagUri).placeholder(R.drawable.gallerypic).fit().centerInside().into(item_image);
            imageList.add(imagUri);
            title = collectionModel.getTitle();


            BigDecimal itemprice = collectionModel.getCost();
            BigDecimal offerprice = collectionModel.getCompareatprice();

            if (itemprice.compareTo(offerprice) == 0) {
                tv_discountamnt.setText("");
            } else {
                tv_discountamnt.setText("$ " + collectionModel.getCompareatprice());
                tv_discountamnt.setPaintFlags(tv_discountamnt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                BigDecimal per = BigDecimal.valueOf(100);
                BigDecimal percentage = ((offerprice.multiply(itemprice)).divide(per));
                String itemPercentage = String.format("%.2f", new BigDecimal(String.valueOf(percentage)));
                // Toast.makeText(context, " Percentage " + percentage, Toast.LENGTH_SHORT).show();
                tv_dispercentage.setText("" + itemPercentage + " % offer ");


            }

        } catch (Exception e) {

        }


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


        img_fav.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {


                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                img_fav.startAnimation(myAnim);

                //if (img_fav.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_action_fav).getConstantState()) {
                if (img_fav.getDrawable().getConstantState().equals(img_fav.getContext().getDrawable(R.drawable.ic_action_fav).getConstantState())) {
                    //   if (favSelection == 0) {
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
        relaytive_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comparterPrice = String.valueOf(collectionModel.getCompareatprice());
                String discountPrice = String.valueOf(collectionModel.getDiscount());
                String cost = String.valueOf(collectionModel.getCost());
                Util.discountPrice = collectionModel.getCost();
                Util.actualPrice = collectionModel.getCompareatprice();
                Intent selectedItem = new Intent(context, SelectedItemActivity.class);
                selectedItem.putExtra("productId", collectionModel.getProductId());
                selectedItem.putExtra("variantId", collectionModel.getVariantId());
                selectedItem.putExtra("price", cost);
                selectedItem.putExtra("decripation", collectionModel.getDescripation());
                selectedItem.putExtra("title", collectionModel.getTitle());
                selectedItem.putExtra("comparterPrice", comparterPrice);
                selectedItem.putExtra("discountPrice", discountPrice);
                selectedItem.putExtra("avialbleforsale", collectionModel.getAvailableSale());
                selectedItem.putExtra("PageInfo", "cartAdd");
                selectedItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(selectedItem);
            }
        });
     /*   } else {
            gridVw = (View) convertView;
        }*/

        return gridVw;

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


}
