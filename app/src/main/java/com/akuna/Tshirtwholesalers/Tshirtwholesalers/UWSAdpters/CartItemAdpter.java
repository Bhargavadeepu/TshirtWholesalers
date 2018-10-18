package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyCartActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.SelectedItemActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartItemAdpter extends RecyclerView.Adapter<CartItemAdpter.ViewHolder> {

    private Context context;
    ArrayList<CartModel> cartModelArrayList;
    private DBManager dbManager;
    CartItemAdpter cartItemAdpter;
    String pageData;
    String quanity;
    CartModel cartModel;
    List<String> sizelist = new ArrayList<>();
    double ttlprice, vardiscount;
    int itemsQuantity;

    public CartItemAdpter(Context context, ArrayList<CartModel> cartModelArrayList, String pageData) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
        this.pageData = pageData;
    }

    @NonNull
    @Override
    public CartItemAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = null;
        if (pageData.contains("ShippingCharge")) {

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shippingcart, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitemtemplate, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdpter.ViewHolder holder, int position) {
        dbManager = new DBManager(context);
        dbManager.open();

        final CartModel cartModel = cartModelArrayList.get(position);
        holder.tv_title.setText(cartModel.getTitle());
        holder.tv_itmeprice.setText("$ " + cartModel.getPrice());
        holder.tv_qunatity.setText("Qty:" + cartModel.getQuantity());
        holder.tv_sizecolor.setText(cartModel.getSize() + "/" + cartModel.getColor());
        Picasso.with(context).load(cartModel.getImageSrc()).placeholder(R.drawable.loadingimg).fit().centerInside().into(holder.img_item);
        holder.tv_acutlaitemprice.setPaintFlags(holder.tv_acutlaitemprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        double disPrice = Double.parseDouble(cartModel.getPrice());

        double actualPrice = Double.parseDouble(cartModel.getDiscount());
        if (disPrice == actualPrice || actualPrice == 0.0) {
            holder.tv_acutlaitemprice.setText("");
        } else {
            holder.tv_acutlaitemprice.setText("$" + cartModel.getComparaterPrice());
        }


        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you wish to remove the item from your cart?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dbManager.deleteCart(cartModel.getCartId());
                                cartModelArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(cartModel.getCartId(), cartModelArrayList.size());
                                notifyDataSetChanged();
                                ((Activity) context).finish();
                                Intent myCart = new Intent(context, MyCartActivity.class);
                                myCart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(myCart);

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editPopup(cartModel.getTitle(), cartModel.getSize(), cartModel.getColor(), cartModel.getCartId(), Double.parseDouble(cartModel.getPrice()), Double.parseDouble(cartModel.getDiscount()), cartModel.getComparaterPrice());


            }
        });

        if (!pageData.contains("ShippingCharge")) {

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String cost = String.valueOf(cartModel.getPrice());
                    String comparterPrice = String.valueOf(cartModel.getComparaterPrice());
                    double itemCost = Double.parseDouble(cartModel.getPrice());
                    Util.discountPrice = BigDecimal.valueOf(itemCost);
                    Util.actualPrice = BigDecimal.valueOf(cartModel.getComparaterPrice());
                    String discountPrice = String.valueOf(cartModel.getDiscount());
                    Intent selectedItem = new Intent(context, SelectedItemActivity.class);
                    selectedItem.putExtra("productId", cartModel.getProductID());
                    selectedItem.putExtra("variantId", cartModel.getVariantID());
                    selectedItem.putExtra("decripation", cartModel.getTitle());
                    selectedItem.putExtra("price", cost);
                    selectedItem.putExtra("title", cartModel.getTitle());
                    selectedItem.putExtra("discountPrice", discountPrice);
                    selectedItem.putExtra("comparterPrice", comparterPrice);
                    selectedItem.putExtra("avialbleforsale", cartModel.getAvailbleforSale());
                    selectedItem.putExtra("PageInfo", "CartEdit");
                    selectedItem.putExtra("decripation", cartModel.getDescripation());
                    selectedItem.putExtra("cartId", cartModel.getCartId());
                    selectedItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(selectedItem);
                    ((Activity) context).finish();


                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_sizecolor, tv_qunatity, tv_itmeprice, tv_remove, tv_edit, tv_acutlaitemprice, tv_outofstock;
        ImageView img_item;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_sizecolor = itemView.findViewById(R.id.tv_price);
            tv_qunatity = itemView.findViewById(R.id.tv_qunatity);
            tv_itmeprice = itemView.findViewById(R.id.tv_itmeprice);
            tv_remove = itemView.findViewById(R.id.tv_remove);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            img_item = itemView.findViewById(R.id.img_item);
            tv_outofstock = itemView.findViewById(R.id.tv_outofstock);
            tv_acutlaitemprice = itemView.findViewById(R.id.tv_acutlaitemprice);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public void editPopup(String ttile, String size, String color, int Cartid, double price, double discount, double comparterPrice) {

        vardiscount = discount;

        ImageView img_close;
        Button btn_addqty;
        TextView tv_producttl, tv_sizecolor;
        Spinner spinner_qty;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.editcartpopup, null);

        img_close = view.findViewById(R.id.img_close);
        btn_addqty = view.findViewById(R.id.btn_addqty);
        tv_producttl = view.findViewById(R.id.tv_producttl);
        tv_sizecolor = view.findViewById(R.id.tv_price);
        spinner_qty = view.findViewById(R.id.spinner_qty);

        final AlertDialog dialog;
        mBuilder.setView(view);
        dialog = mBuilder.create();
        dialog.show();


        /*** Binding the data to the textview ***/
        tv_producttl.setText(ttile);
        tv_sizecolor.setText(size + "/" + color);

        sizelist.add("1");
        sizelist.add("2");
        sizelist.add("3");
        sizelist.add("4");
        sizelist.add("5");
        sizelist.add("6");
        sizelist.add("7");
        sizelist.add("8");
        sizelist.add("9");
        sizelist.add("10");
        sizelist.add("11");
        sizelist.add("12");
        sizelist.add("13");
        sizelist.add("14");
        sizelist.add("15");
        sizelist.add("16");
        sizelist.add("17");
        sizelist.add("18");
        sizelist.add("19");
        sizelist.add("20");
        sizelist.add("21");
        sizelist.add("22");
        sizelist.add("23");
        sizelist.add("24");
        sizelist.add("25");


        /****  Qunatity Adpter  ***/
        ArrayAdapter quantityAdpter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sizelist);
        quantityAdpter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_qty.setAdapter(quantityAdpter);


        spinner_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                quanity = spinner_qty.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_addqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double itemQuntity = Double.parseDouble(quanity);

                if (itemQuntity == 100) {

                } else {

                    try {


                        //   double itemPrice = Double.parseDouble(price);
                        double itemPrice = price;
                        double ttlPrice = (itemQuntity * itemPrice);

                        double adddingdiscount;
                        double compareterPrice;

                        cartModel = new CartModel();
                        //  cartModel.setPrice(String.valueOf(ttlPrice));
                        itemsQuantity = (int) (itemsQuantity + itemQuntity);
                        cartModel.setTotalPrice(ttlPrice);
                        cartModel.setCartId(Cartid);
                        cartModel.setQuantity(String.valueOf(itemQuntity));


                        if (vardiscount > 0) {

                            double discountAmnt = comparterPrice - price;

                            // adddingdiscount = (itemQuntity * vardiscount);
                            compareterPrice = (itemQuntity * comparterPrice);
                            adddingdiscount = (itemQuntity * discountAmnt);
                            cartModel.setDiscount(String.valueOf(adddingdiscount));
                            //  cartModel.setComparaterPrice(compareterPrice);
                            cartModel.setTotalPrice(compareterPrice);

                        } else {
                            // cartModel.setComparaterPrice(comparterPrice);
                            cartModel.setDiscount(String.valueOf(vardiscount));
                        }

                      /*  String disconut = cartModel.getDiscount();
                        if (disconut == null || disconut == "0") {
                        } else {
                            cartModel.setDiscount(String.valueOf(ttlPrice));
                        }*/
                        dbManager.UpdateCartDetails(cartModel);
                        notifyDataSetChanged();
                        dialog.dismiss();
                        ((Activity) context).finish();
                        Intent myCart = new Intent(context, MyCartActivity.class);
                        myCart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myCart);
                        Toast.makeText(context, "Updated the quantity of the Item", Toast.LENGTH_SHORT).show();
                        vardiscount = 0.0;

                    } catch (Exception e) {

                       // Toast.makeText(context, " Expection " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });


    }


}
