package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CustomerOrderModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.CustomerOrders;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.StatusWebView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerOrderAdpter extends RecyclerView.Adapter<CustomerOrderAdpter.ViewHolder> {

    private Context context;
    List<CustomerOrderModel> orderList = new ArrayList<>();
    Date orderDate;
    private String pageInfo;

    public CustomerOrderAdpter(Context context, List<CustomerOrderModel> orderList, String pageInfo) {
        this.context = context;
        this.orderList = orderList;
        this.pageInfo = pageInfo;
    }

    @NonNull
    @Override
    public CustomerOrderAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customerordertemplate, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderAdpter.ViewHolder holder, int position) {

        //  final CollectionModel collectionModel = searchList.get(position);


        final CustomerOrderModel customerOrderModel = orderList.get(position);

        holder.tv_orderid.setText("Order No:" + customerOrderModel.getOrderId());
        holder.tv_ttlprice.setText("$" + customerOrderModel.getTotalPrice());
        // holder.tv_orderdate.setText(customerOrderModel.getOrderDate());
        holder.tv_title.setText(customerOrderModel.getTitle());
        Picasso.with(context).load(customerOrderModel.getImagSrc()).placeholder(R.drawable.gallerypic).fit().centerCrop().into(holder.img_item);
        try {

            DateFormat outputFormat = new SimpleDateFormat(" HH:mm a dd MMM yyyy ", Locale.US);
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
            String inputText = customerOrderModel.getOrderDate();
            Date date = inputFormat.parse(inputText);
            String outputText = outputFormat.format(date);
            holder.tv_orderdate.setText(outputText);

        } catch (Exception e) {
          //  Toast.makeText(context, " Date Excepation " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pageInfo.contains("Show")) {
                } else {
                    Intent webvw = new Intent(context, StatusWebView.class);
                    webvw.putExtra("PageInfo", customerOrderModel.getUrl());
                    webvw.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(webvw);
                }
            }
        });

        if (pageInfo.contains("hide")) {
            holder.tv_vwmore.setVisibility(View.GONE);
        }


        holder.tv_vwmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent orders = new Intent(context, CustomerOrders.class);
                orders.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(orders);
                //  context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_orderid, tv_ttlprice, tv_orderdate, tv_title, tv_vwmore;
        ImageView img_item;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_orderid = itemView.findViewById(R.id.tv_orderid);
            tv_ttlprice = itemView.findViewById(R.id.tv_ttlprice);
            tv_orderdate = itemView.findViewById(R.id.tv_orderdate);
            tv_title = itemView.findViewById(R.id.tv_title);
            img_item = itemView.findViewById(R.id.img_item);
            tv_vwmore = itemView.findViewById(R.id.tv_vwmore);
            layout = itemView.findViewById(R.id.layout);


        }
    }
}
