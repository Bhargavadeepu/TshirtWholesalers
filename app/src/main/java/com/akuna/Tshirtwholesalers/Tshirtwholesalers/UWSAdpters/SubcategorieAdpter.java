package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.ProductListAcitvity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.LivesearchActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SubCategoryProducts;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SubcatogoriesListActivity;

import java.util.ArrayList;
import java.util.List;

public class SubcategorieAdpter extends RecyclerView.Adapter<SubcategorieAdpter.ViewHolder> {


    private Context context;
    // List<String> subcategorieList = new ArrayList<>();
    List<String> subcategorieLists = new ArrayList<>();

    /* public SubcategorieAdpter(Context context, List<String> subcategorieList) {
         this.context = context;
         this.subcategorieList = subcategorieList;
     }
 */
    String selectedtxt;

    public SubcategorieAdpter(Context context, List<String> subcategorieLists) {
        this.context = context;
        this.subcategorieLists = subcategorieLists;
    }

    @NonNull
    @Override
    public SubcategorieAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategorieAdpter.ViewHolder holder, int position) {


        holder.txt_item.setText(subcategorieLists.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int i = 0; i < subcategorieLists.size(); i++) {
                    selectedtxt = subcategorieLists.get(position);
                }

                //  Toast.makeText(context," Selectedtxt "+selectedtxt,Toast.LENGTH_SHORT).show();
                Intent categories = new Intent(context, SubCategoryProducts.class);
                categories.putExtra("pageinfo", selectedtxt);
                categories.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                categories.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(categories);

            }
        });


    }

    @Override
    public int getItemCount() {
        return subcategorieLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_item;
        View layout;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_item = itemView.findViewById(R.id.txt_item);
            layout = itemView.findViewById(R.id.layout);


        }
    }
}
