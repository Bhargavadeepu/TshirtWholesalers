package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.AddressModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.AddAddress;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyCartActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Addressadpter extends RecyclerView.Adapter<Addressadpter.ViewHolder> {


    private GraphClient graphClient;
    AddressModel addressModel;
    Context context;
    ArrayList<AddressModel> addressList;
    String addressId;
    private int lastSelectedPosition = -1;
    private ProgressDialog progressDialog;

    public Addressadpter(List<AddressModel> addressList, Context context) {
        this.context = context;
        this.addressList = (ArrayList<AddressModel>) addressList;
    }

    @NonNull
    @Override
    public Addressadpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_template, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Addressadpter.ViewHolder holder, int position) {

        progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);

        final AddressModel addressModel = addressList.get(position);

        //  holder.tv_name.setText(addressModel.getFirstName() + addressModel.getLastName());
        holder.tv_name.setText(addressModel.getFirstName());
        holder.tv_addressline1.setText(addressModel.getAddressLine1());
        holder.tv_addressline2.setText(addressModel.getAddressLine2());
        holder.tv_addressline2.setText(addressModel.getAddressLine2());
        holder.tv_state.setText(addressModel.getProvince());
        holder.tv_country.setText(addressModel.getCountryName());
        holder.tv_mobileno.setText(" Mobile :" + addressModel.getPhoneNumber());
        holder.tv_cityandpincode.setText(addressModel.getProvince() + addressModel.getZipCode());
        holder.tv_lastname.setText(addressModel.getLastName());
        holder.tv_companyname.setText(addressModel.getCompanyName());
        holder.tv_zipcode.setText(addressModel.getZipCode());
        holder.radiobtn_address.setChecked(lastSelectedPosition == position);


        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(context, " Clicked on the delete ", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure to remove this address?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                addressId = addressModel.getId();
                                deleteAddress(position);

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
                //    Toast.makeText(context, " Clicked on the Edit ", Toast.LENGTH_SHORT).show();

                Intent address = new Intent(context, AddAddress.class);
                address.putExtra("PageInfo", "addressEdit");
                address.putExtra("firtname", addressModel.getFirstName());
                address.putExtra("lastname", addressModel.getLastName());
                address.putExtra("phoneNumber", addressModel.getPhoneNumber());
                address.putExtra("Address1", addressModel.getAddressLine1());
                address.putExtra("Address2", addressModel.getAddressLine2());
                address.putExtra("country", addressModel.getCountryName());
                address.putExtra("zipcode", addressModel.getZipCode());
                address.putExtra("city", addressModel.getProvince());
                address.putExtra("company", addressModel.getCompanyName());
                address.putExtra("emailId", addressModel.getCompanyName());
                address.putExtra("id", addressModel.getId());
                address.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(address);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_name, tv_addressline1, tv_addressline2, tv_cityandpincode, tv_companyname,
                tv_state, tv_country, tv_mobileno, tv_remove, tv_edit, tv_lastname, tv_zipcode;
        RadioButton radiobtn_address;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_addressline1 = itemView.findViewById(R.id.tv_addressline1);
            tv_addressline2 = itemView.findViewById(R.id.tv_addressline2);
            tv_cityandpincode = itemView.findViewById(R.id.tv_cityandpincode);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_country = itemView.findViewById(R.id.tv_country);
            tv_mobileno = itemView.findViewById(R.id.tv_mobileno);
            tv_remove = itemView.findViewById(R.id.tv_remove);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_companyname = itemView.findViewById(R.id.tv_companyname);
            radiobtn_address = itemView.findViewById(R.id.radiobtn_address);
            tv_lastname = itemView.findViewById(R.id.tv_lastname);
            tv_zipcode = itemView.findViewById(R.id.tv_zipcode);

            radiobtn_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    Util.firstName = "";
                    Util.lastName = "";
                    Util.addressline1 = "";
                    Util.addressline2 = "";
                    Util.city = "";
                    Util.provinceses = "";
                    Util.phono = "";
                    Util.zipcode = "";
                    Util.country = "";

                    Util.firstName = tv_name.getText().toString();
                    Util.lastName = tv_lastname.getText().toString();
                    Util.addressline1 = tv_addressline1.getText().toString();
                    Util.addressline2 = tv_addressline2.getText().toString();
                    Util.city = tv_state.getText().toString();
                    Util.provinceses = tv_state.getText().toString();
                    //  Util.phono = tv_mobileno.getText().toString();
                    Util.zipcode = tv_zipcode.getText().toString();
                    Util.country = tv_country.getText().toString();

                    String phNo = tv_mobileno.getText().toString();
                    String newphNo = phNo.replaceFirst("Mobile :", "");
                    Util.phono = newphNo;
                    Util.addressSelection = 1;

                    //  Toast.makeText(context, " newphNo" + newphNo, Toast.LENGTH_SHORT).show();


                }
            });


        }
    }

    private void deleteAddress(int itemposition) {

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        graphClient = GraphClient.builder(context)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(context.getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();


        ID id = new ID(addressId);

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressDelete(id, Util.accessToken, query -> query
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );
        MutationGraphCall call = graphClient.mutateGraph(mutationQuery);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                progressDialog.dismiss();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //String resData = response.data().toString();
                        notifyDataSetChanged();
                        addressList.remove(itemposition);
                        notifyItemRemoved(itemposition);
                        notifyDataSetChanged();
                        Toast.makeText(context, " Address Deleted successfully ", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
