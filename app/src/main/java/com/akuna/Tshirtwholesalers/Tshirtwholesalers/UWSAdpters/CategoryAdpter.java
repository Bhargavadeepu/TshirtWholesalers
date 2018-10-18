package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CollectionModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SubcatogoriesListActivity;

import java.util.ArrayList;

public class CategoryAdpter extends RecyclerView.Adapter<CategoryAdpter.Viewholder> {


  //  ArrayList<String> items;

    ArrayList<String> itemsList = new ArrayList<>();
    private Context context;

    public CategoryAdpter(ArrayList<String> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

  /*  public CategoryAdpter(ArrayList<String> items, Context context) {
        this.items = items;
        this.context = context;
    }
*/

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new Viewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {



        holder.txt_item.setText(itemsList.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent categories = new Intent(context, SubcatogoriesListActivity.class);
                categories.putExtra("PageInfo",itemsList.get(position));
                categories.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(categories);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView txt_item;
        View layout;

        public Viewholder(View itemView) {
            super(itemView);
            txt_item = itemView.findViewById(R.id.txt_item);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}
