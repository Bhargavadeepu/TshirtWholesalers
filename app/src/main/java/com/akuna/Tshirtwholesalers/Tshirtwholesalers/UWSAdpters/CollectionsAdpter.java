package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.AddressModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionnewModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.ProductListAcitvity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class CollectionsAdpter extends RecyclerView.Adapter<CollectionsAdpter.ViewHolder> {

    String imagUri, title;
    Context context;
    CollectionnewModel collectionnewModel;
    ArrayList<CollectionnewModel> collectionlist;

    public CollectionsAdpter(List<CollectionnewModel> collectionlist, Context context) {
        this.context = context;
        this.collectionlist = (ArrayList<CollectionnewModel>) collectionlist;
    }

    @NonNull


    @Override
    public CollectionsAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collectiontemplate, parent, false);
        return new ViewHolder(v);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsAdpter.ViewHolder holder, int position) {

        final CollectionnewModel collectionnewModel = collectionlist.get(position);
        imagUri = collectionnewModel.getImgSrc();

        //  Toast.makeText(context, " Img Src " + imagUri, Toast.LENGTH_SHORT).show();


        Picasso.with(context).load(imagUri).placeholder(R.drawable.gallerypic).fit().into(holder.img_collections);


        //start progressbar here
      /*  Picasso.with(context)
                .load(imagUri)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.img_collections.setImageBitmap(bitmap);
                        holder.progressBar.setVisibility(View.GONE);

                        //stop progressbar here
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        //stop progressbar here
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });*/

        holder.img_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent products = new Intent(context, ProductListAcitvity.class);
                products.putExtra("pageinfo", collectionnewModel.getCollectionId().toString());
                // products.putExtra("pageinfo", "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzg3NTI2NjA3NA==");
                products.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(products);


            }
        });


    }

    @Override
    public int getItemCount() {
        return collectionlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_collections;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_collections = itemView.findViewById(R.id.img_collections);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }
}
