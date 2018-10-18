package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class ProductListAdpter  extends RecyclerView.Adapter<ProductListAdpter.ViewHolder> {


    @NonNull
    @Override
    public ProductListAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_template, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdpter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_price;
        ImageView img_fav;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_price = itemView.findViewById(R.id.img_fav);
            tv_title = itemView.findViewById(R.id.tv_title);
            img_fav = itemView.findViewById(R.id.img_fav);




        }
    }
}
