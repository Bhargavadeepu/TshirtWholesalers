package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.Images;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

import java.util.ArrayList;

public class CategorieItemAdpter extends RecyclerView.Adapter<CategorieItemAdpter.Viewholder> {


    ArrayList<Integer> itemImg;
    private Context context;

    public CategorieItemAdpter(ArrayList<Integer> itemImg, Context context) {
        this.itemImg = itemImg;
        this.context = context;
    }

    @NonNull
    @Override
    public CategorieItemAdpter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorie_item, parent, false);
        return new Viewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CategorieItemAdpter.Viewholder holder, int position) {

        //final Images images = itemImg.get(position);
        //  holder.itemImg.setImageResource(itemImg.get(position));
        holder.itemImg.setBackgroundResource(itemImg.get(position));

        // holder.imageView.setImageResource(R.drawable.caregorynetball);

    }

    @Override
    public int getItemCount() {
        //   Toast.makeText(context, "Item Size  " + itemImg.size(), Toast.LENGTH_SHORT).show();
        return itemImg.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView itemImg;

        public Viewholder(View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.img_item);


        }
    }
}
