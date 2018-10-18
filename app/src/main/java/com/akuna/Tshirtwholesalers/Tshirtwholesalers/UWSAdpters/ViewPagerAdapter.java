package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.ProductListAcitvity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.ZoomImageActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;


    /*public ViewPagerAdapter(Activity act,Context context) {
        this.context = context;
        activity = act;
    }*/

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

  /*  public ViewPagerAdapter(SelectedItemActivity selectedItemActivity) {
        activity = selectedItemActivity;
    }*/

    @Override
    public int getCount() {
        return Util.images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView imageView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        imageView = (ImageView) view.findViewById(R.id.imageVw);
        //  imageView.setImageResource(Util.images[position]);
      //  Picasso.with(context).load(Util.images[position]).fit().centerCrop().into(imageView);
        Picasso.with(context).load(Util.images[position]).placeholder(R.drawable.gallerypic).fit().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {

                    Intent zoomIntent = new Intent(context, ProductListAcitvity.class);
                    //  zoomIntent.putExtra("pageinfo", "Ascolour");
                    zoomIntent.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzQ0MDk2OTc0MQ==");
                    // zoomIntent.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0LzMwMDQ4Mjg4Nzk0");
                    zoomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(zoomIntent);
                } else if (position == 1) {
                    Intent zoomIntent = new Intent(context, ProductListAcitvity.class);
                    // zoomIntent.putExtra("pageinfo", "polos");
                    zoomIntent.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzgxOTYyNjAxMA==");
                    zoomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(zoomIntent);

                } else if (position == 2) {
                    Intent zoomIntent = new Intent(context, ProductListAcitvity.class);
                    // zoomIntent.putExtra("pageinfo", "Singlet");
                    zoomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    zoomIntent.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzgxODIxNjk4Ng==");
                    context.startActivity(zoomIntent);
                } else {
                    Intent zoomIntent = new Intent(context, ProductListAcitvity.class);
                    // zoomIntent.putExtra("pageinfo", "Sweats");
                    zoomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    zoomIntent.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzgxOTQ5NDkzOA==");
                    context.startActivity(zoomIntent);
                }


            }
        });


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }


}
