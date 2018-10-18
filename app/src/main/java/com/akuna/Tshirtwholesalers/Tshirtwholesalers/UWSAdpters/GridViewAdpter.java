package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdpter extends BaseAdapter {

    String imagUri;
    CollectionModel collectionModel;
    private Context context;
    private LayoutInflater inflater;
    private GridView gridView;
    List<CollectionModel> gridList = new ArrayList<>();

    public GridViewAdpter(Context context, List<CollectionModel> gridList) {
        this.context = context;
        this.gridList = gridList;

    }


    @Override
    public int getCount() {
        return gridList.size();

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
    public View getView(final int position, View view, ViewGroup parent) {


        final ImageView item_image, img_share, img_fav;
        final Button btn_shopnw;
        final TextView item_name, item_cost, tv_campareprice, tv_offerpercentage;
        final View relaytive_layout;

        View gridVw = view;


        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridVw = inflater.inflate(R.layout.grid_layout, null);
        } else {
            gridVw = (View) view;
        }

        btn_shopnw = gridVw.findViewById(R.id.btn_shopnw);
        item_name = gridVw.findViewById(R.id.item_name);
        item_cost = gridVw.findViewById(R.id.item_cost);
        item_image = gridVw.findViewById(R.id.item_image);
        tv_campareprice = gridVw.findViewById(R.id.tv_campareprice);
        relaytive_layout = gridVw.findViewById(R.id.relaytive_layout);
        tv_offerpercentage = gridVw.findViewById(R.id.tv_offerpercentage);

        // collectionModel = new CollectionModel();
        final CollectionModel collectionModel = gridList.get(position);
        item_name.setText(collectionModel.getTitle());
        item_cost.setText("$" + collectionModel.getCost());
        imagUri = collectionModel.getImage();

        //progress_animation
        Picasso.with(context).load(imagUri).placeholder(R.drawable.loadingimg).fit().centerCrop().into(item_image);

        // Picasso.with(context).load(imagUri).placeholder(R.drawable.ic_action_cart).fit().centerCrop().into(item_image);

        BigDecimal itemprice = collectionModel.getCost();
        BigDecimal offerprice = collectionModel.getCompareatprice();


        try {

            if ((itemprice.compareTo(offerprice) == 0 || offerprice == null)) {
                tv_campareprice.setText("");
                tv_offerpercentage.setText("");
            } else {
                tv_campareprice.setText("$ " + collectionModel.getCompareatprice());
                tv_campareprice.setPaintFlags(tv_campareprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                BigDecimal per = BigDecimal.valueOf(100);
                BigDecimal percentage = ((offerprice.multiply(itemprice)).divide(per));
                String itemPercentage = String.format("%.2f", new BigDecimal(String.valueOf(percentage)));
                //  Toast.makeText(context, " Percentage " + percentage, Toast.LENGTH_SHORT).show();
                tv_offerpercentage.setText("" + itemPercentage + " % offer ");

            }
        } catch (Exception e) {

            //   Toast.makeText(context, " Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        relaytive_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cost = String.valueOf(collectionModel.getCost());
                String comparterPrice = String.valueOf(collectionModel.getCompareatprice());
                Util.discountPrice = collectionModel.getCost();
                Util.actualPrice = collectionModel.getCompareatprice();
                String discountPrice = String.valueOf(collectionModel.getDiscount());

                Intent selectedItem = new Intent(context, SelectedItemActivity.class);
                selectedItem.putExtra("productId", collectionModel.getProductId());
                selectedItem.putExtra("variantId", collectionModel.getVariantId());
                selectedItem.putExtra("decripation", collectionModel.getDescripation());
                selectedItem.putExtra("price", cost);
                selectedItem.putExtra("title", collectionModel.getTitle());
                selectedItem.putExtra("discountPrice", discountPrice);
                selectedItem.putExtra("comparterPrice", comparterPrice);
                selectedItem.putExtra("avialbleforsale", collectionModel.getAvailableSale());
                selectedItem.putExtra("PageInfo", "cartAdd");
                selectedItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(selectedItem);
            }
        });


        return gridVw;
    }


}

