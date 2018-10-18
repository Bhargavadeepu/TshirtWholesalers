package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.ZoomImageActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SelecteditemAdpter extends PagerAdapter {


    Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    List<String> imageList = new ArrayList<>();
    String imgSrc;

    public SelecteditemAdpter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }


    @Override
    public int getCount() {
        //  return Util.itemImges.length;
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView imageView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.selecteditem_layout, null);
        imageView = (ImageView) view.findViewById(R.id.imageVw);

        //  imageView.setImageResource(Integer.parseInt(imageList.get(position)));
        //imageView.setImageResource(imageList.get(position));

        //   Picasso.with(context).load(imageList.get(position)).fit().centerCrop().into(imageView);
        //   Picasso.with(context).load(imageList.get(position)).fit().into(imageView);
        Picasso.with(context).load(imageList.get(position)).placeholder(R.drawable.gallerypic).into(imageView);
        // Picasso.with(context).load(imageList.get(position)).placeholder(R.drawable.progress_animation).into(imageView);
        //  Picasso.with(context).load(imagUri).placeholder(R.drawable.progress_animation).fit().centerCrop().into(item_image);
        //  Picasso.with(context).load(imageList.get(position)).resize(650,350).into(imageView);
        imgSrc = imageList.get(position).toString();
        // imageView.setImageResource(imageList.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent zoomIntent = new Intent(context, ZoomImageActivity.class);
                zoomIntent.putExtra("imageSrc", imageList.get(position).toString());
                zoomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(zoomIntent);
                Util.selectedImage = imageList.get(position).toString();

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
