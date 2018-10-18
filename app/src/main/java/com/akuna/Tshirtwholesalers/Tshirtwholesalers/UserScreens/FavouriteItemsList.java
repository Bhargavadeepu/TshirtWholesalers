package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.FavouriteAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.shopify.buy3.GraphClient;

import java.util.ArrayList;
import java.util.List;

public class FavouriteItemsList extends AppCompatActivity {


    private GraphClient graphClient;
    private DBManager dbManager;
    private ArrayList<FovuroriteModel> favoriteArrylist = new ArrayList<FovuroriteModel>();
    RecyclerView recyclervw_order;
    FavouriteAdpter favouriteAdpter;
    FovuroriteModel fovuroriteModel;
    View layout_noitems;
    List<FovuroriteModel> favoriteList = new ArrayList<>();
    List<CustomerOrderModel> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_items_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();

        try{
            favoriteArrylist = dbManager.GetFavouriteDetails();

            if (favoriteArrylist == null || favoriteArrylist.size() == 0) {
                layout_noitems.setVisibility(View.VISIBLE);

            } else {

                for (int i = 0; i < favoriteArrylist.size(); i++) {
                    fovuroriteModel = new FovuroriteModel();
                    fovuroriteModel.setAvailbleforSale(favoriteArrylist.get(i).getAvailbleforSale());
                    fovuroriteModel.setColor(favoriteArrylist.get(i).getColor());
                    fovuroriteModel.setDiscount(favoriteArrylist.get(i).getDiscount());
                    fovuroriteModel.setImageSrc(favoriteArrylist.get(i).getImageSrc());
                    fovuroriteModel.setPrice(favoriteArrylist.get(i).getPrice());
                    fovuroriteModel.setProductID(favoriteArrylist.get(i).getProductID());
                    fovuroriteModel.setQuantity(favoriteArrylist.get(i).getQuantity());
                    fovuroriteModel.setSize(favoriteArrylist.get(i).getSize());
                    fovuroriteModel.setTitle(favoriteArrylist.get(i).getTitle());
                    fovuroriteModel.setTotalPrice(favoriteArrylist.get(i).getTotalPrice());
                    fovuroriteModel.setVariantID(favoriteArrylist.get(i).getVariantID());
                    fovuroriteModel.setFavId(favoriteArrylist.get(i).getFavId());
                    fovuroriteModel.setDescripation(favoriteArrylist.get(i).getDescripation());
                    fovuroriteModel.setComparaterPrice(favoriteArrylist.get(i).getComparaterPrice());
                    favoriteList.add(fovuroriteModel);
                }

                String pagedata = "hide";
                recyclervw_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclervw_order.setHasFixedSize(true);
                favouriteAdpter = new FavouriteAdpter(FavouriteItemsList.this, favoriteList, pagedata);
                recyclervw_order.setAdapter(favouriteAdpter);

            }

        }catch (Exception e){

        }



    }

    private void initViews() {
        dbManager = new DBManager(this);
        dbManager.open();
        recyclervw_order = findViewById(R.id.recyclervw_order);
        layout_noitems = findViewById(R.id.layout_noitems);
    }

    public void btnback_OnClick(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
