package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.AddressModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.Addressadpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyAddresses extends AppCompatActivity {

    private static GraphClient graphClient;
    AddressModel addressModel;
    private ProgressDialog progressDialog;
    List<AddressModel> addressList = new ArrayList<>();
    RecyclerView recyclerview_address;
    Addressadpter addressadpter;
    TextView txt_noaddress, tv_waringtxt;
    public static MyAddresses instance = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instance = this;
        initViews();

        /**** Funcation to get the Customer Addresss***/


        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            getMyaddress();
        }else{
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }


    }


    private void initViews() {
        txt_noaddress = findViewById(R.id.txt_noaddress);
        recyclerview_address = findViewById(R.id.recyclerview_address);
        tv_waringtxt = findViewById(R.id.tv_waringtxt);
        tv_waringtxt.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void btn_confirmclick(View view) {


        if (addressList == null || addressList.size() == 0) {
            Toast.makeText(getApplicationContext(), " Please add address for delivery", Toast.LENGTH_SHORT).show();
            tv_waringtxt.setVisibility(View.VISIBLE);

        } else {

            if (Util.addressSelection == 0) {
                Toast.makeText(getApplicationContext(), " Please Select any of the Address", Toast.LENGTH_SHORT).show();
            } else {
                updatingShippingAddress(view);
            }

        }


    }

    public void txt_addressonClick(View view) {

        Intent addAddress = new Intent(MyAddresses.this, AddAddress.class);
        addAddress.putExtra("PageInfo", "newAddress");
        startActivity(addAddress);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    public void btnback_OnClick(View view) {

        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    private void getMyaddress() {

        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        graphClient =  Util.graphclient(getApplicationContext());

        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .addresses(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .address1()
                                                .address2()
                                                .city()
                                                .province()
                                                .country()
                                                .zip()
                                                .firstName()
                                                .lastName()
                                                .company()
                                                .phone()

                                        )
                                )
                        )
                )
        );


        //  QueryGraphCall call = graphClient.queryGraph(query);
        QueryGraphCall call = graphClient.queryGraph(query);
        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                progressDialog.dismiss();

                List<Storefront.MailingAddress> addresses = new ArrayList<>();
                for (Storefront.MailingAddressEdge customer : response.data().getCustomer().getAddresses().getEdges()) {
                    addresses.add(customer.getNode());
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (addresses.size() == 0) {
                            tv_waringtxt.setVisibility(View.VISIBLE);
                        }

                    }
                });


                for (int i = 0; i < addresses.size(); i++) {

                    AddressModel addressModel = new AddressModel();
                    addressModel.setFirstName(addresses.get(i).getFirstName());
                    addressModel.setLastName(addresses.get(i).getLastName());
                    addressModel.setAddressLine1(addresses.get(i).getAddress1());
                    addressModel.setAddressLine2(addresses.get(i).getAddress2());
                    addressModel.setPhoneNumber(addresses.get(i).getPhone());
                    addressModel.setProvince(addresses.get(i).getProvince());
                    addressModel.setZipCode(addresses.get(i).getZip());
                    addressModel.setCountryName(addresses.get(i).getCountry());
                    addressModel.setCompanyName(addresses.get(i).getCompany());
                    addressModel.setCity(addresses.get(i).getCity());
                    addressModel.setId(String.valueOf(addresses.get(i).getId()));
                    addressList.add(addressModel);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            recyclerview_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerview_address.setHasFixedSize(true);
                            addressadpter = new Addressadpter(addressList, MyAddresses.this);
                            recyclerview_address.setAdapter(addressadpter);

                        }
                    });


                }


            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    private void updatingShippingAddress(View view) {

        Util.showProgress(MyAddresses.this, view);
/*

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        Util.getHttpmethod(getApplication());
        graphClient = GraphClient.builder(this)
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();
*/

        graphClient =  Util.graphclient(getApplicationContext());

        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1(Util.addressline1)
                .setAddress2(Util.addressline2)
                .setCity(Util.city)
                .setCountry(Util.country)
                .setFirstName(Util.firstName)
                .setLastName(Util.lastName)
                .setPhone(Util.phono)
                .setProvince(Util.provinceses)
                .setZip(Util.zipcode);


        ID id = new ID(Util.CheckoutId);

        Storefront.MutationQuery query = Storefront.mutation((mutationQuery -> mutationQuery
                        .checkoutShippingAddressUpdate(input, id, shippingAddressUpdatePayloadQuery -> shippingAddressUpdatePayloadQuery
                                .checkout(checkoutQuery -> checkoutQuery
                                        .webUrl()
                                )
                                .userErrors(userErrorQuery -> userErrorQuery
                                        .field()
                                        .message()
                                )
                        )
                )
        );
        MutationGraphCall call = graphClient.mutateGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                Util.dismissProgress();

                if (!response.data().getCheckoutShippingAddressUpdate().getUserErrors().isEmpty()) {
                    // handle user friendly errors
                } else {
                    Util.checkoutWebUrl = response.data().getCheckoutShippingAddressUpdate().getCheckout().getWebUrl();
                    Util.CheckoutId = response.data().getCheckoutShippingAddressUpdate().getCheckout().getId().toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        /*    Intent shippingCharges = new Intent(MyAddresses.this, ShippingCharge.class);
                            startActivity(shippingCharges);*/
                            Intent checkout = new Intent(MyAddresses.this, WebCheckoutActivity.class);
                            checkout.putExtra("pageInfo", "Customer");
                            startActivity(checkout);

                        }
                    });

                }


            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                Util.dismissProgress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


}
