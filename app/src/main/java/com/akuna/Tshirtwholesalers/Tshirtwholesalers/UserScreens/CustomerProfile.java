package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.Input;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util.accessToken;

public class CustomerProfile extends AppCompatActivity {

    private GraphClient graphClient;
    private ProgressDialog progressDialog;
    TextView edt_name, edt_lname, edt_email, edt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        /***  Customer Order Service Call***/

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            customerInfoserviceCall();
        }else{
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }



    }

    public void btnback_OnClick(View view) {
        finish();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        edt_name = findViewById(R.id.edt_name);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
    }

    public void updatebtn_click(View view) {

        Validation(view);

    }

    public void Validation(View v) {
        if (edt_name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter First Name", Toast.LENGTH_SHORT).show();
        } else if (edt_lname.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Last Name", Toast.LENGTH_SHORT).show();
        } else if (edt_email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Email Address", Toast.LENGTH_SHORT).show();
        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
        } else if (edt_phone.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {

            if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                customerProfileUpdate();
            }else{
                Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void customerInfoserviceCall() {


        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient =  Util.graphclient(getApplicationContext());


        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(Util.accessToken, customer -> customer
                        .firstName()
                        .lastName()
                        .email()
                        .phone()
                )
        );


        QueryGraphCall call = graphClient.queryGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                try{

                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edt_name.setText(response.data().getCustomer().getFirstName());
                            edt_lname.setText(response.data().getCustomer().getLastName());
                            edt_email.setText(response.data().getCustomer().getEmail());

                            if (response.data().getCustomer().getEmail().isEmpty()) {
                                edt_phone.setText("");
                            } else {
                                edt_phone.setText(response.data().getCustomer().getPhone());
                            }
                        }
                    });
                }catch (Exception e){


                }




            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private void customerProfileUpdate() {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient = Util.graphclient(getApplicationContext());

        Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput()
                .setFirstNameInput(Input.value(edt_name.getText().toString().trim()))
                .setLastNameInput(Input.value(edt_lname.getText().toString().trim()))
                .setEmailInput(Input.value(edt_email.getText().toString().trim()))
                .setPhoneInput(Input.value(edt_phone.getText().toString().trim()));
        // .setPhoneInput(Input.value(null));

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerUpdate(Util.accessToken, input, query -> query
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );
        graphClient =  Util.graphclient(getApplicationContext());

        MutationGraphCall call = graphClient.mutateGraph(mutationQuery);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                try{
                    progressDialog.dismiss();
                    if (!response.data().getCustomerUpdate().getUserErrors().isEmpty()) {
                        for (Storefront.UserError error : response.data().getCustomerUpdate().getUserErrors()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {

                        //  String customerCreateInput = response.data().getCustomerUpdate().getCustomer().getFirstName();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " Profile Successfully Updated  ", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

                    }
                }catch (Exception e){

                }




            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            }
        });

    }


}
