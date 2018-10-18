package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.Input;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    EditText edt_name, edt_lname, edt_email, edt_password, edt_confpassword;
    TextInputLayout textInputLayout_name, textInputLayout_lastname, textInputLayout_email, textInputLayout_password, textInputLayout_confpassword;
    private static GraphClient graphClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        progressDialog = new ProgressDialog(this);
    }

    private void initViews() {
        edt_name = findViewById(R.id.edt_name);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confpassword = findViewById(R.id.edt_confpassword);
        textInputLayout_name = findViewById(R.id.textInputLayout_name);
        textInputLayout_lastname = findViewById(R.id.textInputLayout_lastname);
        textInputLayout_email = findViewById(R.id.textInputLayout_email);
        textInputLayout_password = findViewById(R.id.textInputLayout_password);
        textInputLayout_confpassword = findViewById(R.id.textInputLayout_confpassword);
    }

    public void registerbtn_click(View view) {
        Validation(view);
    }


    public void Validation(View v) {
        if (edt_name.getText().toString().isEmpty()) {
            textInputLayout_name.setErrorEnabled(true);
            textInputLayout_name.setError("Please enter Name");
        } else if (edt_lname.getText().toString().isEmpty()) {
            textInputLayout_lastname.setErrorEnabled(true);
            textInputLayout_lastname.setError("Please enter Last Name");
        } else if (edt_email.getText().toString().isEmpty()) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter Email Address");
        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter a valid Email Address");
        } else if (edt_password.getText().toString().isEmpty()) {
            textInputLayout_password.setErrorEnabled(true);
            textInputLayout_password.setError("Please enter  password");
        } else if (edt_confpassword.getText().toString().isEmpty()) {
            textInputLayout_password.setErrorEnabled(true);
            textInputLayout_password.setError("Please enter Confirm password");
        } else if (!edt_confpassword.getText().toString().trim().matches(edt_password.getText().toString().trim())) {
            textInputLayout_password.setErrorEnabled(true);
            textInputLayout_password.setError("The confirm password does not match with Passwrod");
        } else {
            if (CheckNetwork.isInternetAvailable(RegisterActivity.this)) {
                Util.hideSoftKeyBoard(getApplicationContext(), v);
                regisatrationServiceCall(v);
            } else {
                Toast.makeText(RegisterActivity.this, "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void regisatrationServiceCall(View v) {


        Util.showProgress(RegisterActivity.this, v);


     /*   HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
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

        String firstName = edt_name.getText().toString().trim();
        String lastName = edt_lname.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String emailId = edt_email.getText().toString().trim();
        Storefront.CustomerCreateInput input = new Storefront.CustomerCreateInput(emailId, password)
                .setFirstNameInput(Input.value(firstName))
                .setLastNameInput(Input.value(lastName))
                .setAcceptsMarketingInput(Input.value(true));


        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerCreate(input, query -> query
                        .customer(customer -> customer
                                .id()
                                .email()
                                .firstName()
                                .lastName()
                        )
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


                try{
                    Util.dismissProgress();


                    try {
                        if (!response.data().getCustomerCreate().getUserErrors().isEmpty()) {
                            for (Storefront.UserError error : response.data().getCustomerCreate().getUserErrors()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            //  Storefront.CustomerAccessToken customerAccessToken = response.data().getCustomerAccessTokenCreate().getCustomerAccessToken();
                            String customerCreateInput = response.data().getCustomerCreate().getCustomer().getFirstName();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //  Toast.makeText(getApplicationContext(), " First Name   " + customerCreateInput, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), " Registration Sucessfull  ", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                    login.putExtra("pageInfo", "myCart");
                                    startActivity(login);


                                }
                            });

                        }

                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  Toast.makeText(getApplicationContext(), " Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }catch (Exception e){

                }




            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Util.dismissProgress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }

    public void btnback_OnClick(View view) {
        closePage();
    }

    @Override
    public void onBackPressed() {
        closePage();
    }


    private void closePage() {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
