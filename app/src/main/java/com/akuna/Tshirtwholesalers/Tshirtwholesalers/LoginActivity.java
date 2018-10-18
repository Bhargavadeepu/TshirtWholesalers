package com.akuna.Tshirtwholesalers.Tshirtwholesalers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.CheckoutasGuest;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.ContactUs;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.CustomerOrders;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.MyAddresses;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.RegisterActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens.WebCheckoutActivity;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util.checkoutWebUrl;

public class LoginActivity extends AppCompatActivity {


    public static LoginActivity instance = null;
    private static GraphClient graphClient;
    private ProgressDialog progressDialog;
    Button btn_login, btn_register;
    TextInputLayout textInputLayout_emil, textInputLayout_password;
    EditText edt_email, edt_pswrd;
    String pageData;
    View btn_layout;
    TextView itemcost;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        /* Initializing the views*/
        initViews();

        Util.userName = sharedpreferences.getString("Name", "");
        Util.password = sharedpreferences.getString("password", "");


        itemcost.setText("$" + Util.itemTtlPrice);

        Intent info = getIntent();
        pageData = info.getExtras().getString("pageInfo");

        if ((pageData.contains("myCart") || pageData.contains("Buynow"))) {
            btn_register.setVisibility(View.GONE);
            if (!Util.userName.isEmpty()) {
                loginSeviceCall(Util.userName, Util.password);
            }
        } else {
            btn_layout.setVisibility(View.GONE);
            itemcost.setText(" ");
        }

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout_emil.setError("");
                textInputLayout_emil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_pswrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void btn_register(View view) {

        Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(reg);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void btnback_OnClick(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void loginbtn_click(View view) {

        if (edt_email.getText().toString().isEmpty()) {
            textInputLayout_emil.setError("please enter email address");
            textInputLayout_emil.setErrorEnabled(true);
        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
            textInputLayout_emil.setError("please enter a valid email address");
            textInputLayout_emil.setErrorEnabled(true);
        } else if (edt_pswrd.getText().toString().isEmpty()) {
            textInputLayout_password.setError("please enter password");
            textInputLayout_password.setErrorEnabled(true);
        } else {


            if (CheckNetwork.isInternetAvailable(LoginActivity.this)) {
                Util.hideSoftKeyBoard(getApplicationContext(), view);
                //  loginSeviceCall(view);
                loginSeviceCall(edt_email.getText().toString().trim(), edt_pswrd.getText().toString().trim());
            } else {
                Toast.makeText(LoginActivity.this, "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void register_onclick(View view) {


        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    private void initViews() {

        instance = this;
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        textInputLayout_emil = findViewById(R.id.textInputLayout_emil);
        textInputLayout_password = findViewById(R.id.textInputLayout_password);
        edt_email = findViewById(R.id.edt_email);
        edt_pswrd = findViewById(R.id.edt_pswrd);
        btn_layout = findViewById(R.id.btn_layout);
        itemcost = findViewById(R.id.itemcost);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
    }

    public void btn_checkoutguest(View view) {

        Intent checkoutguest = new Intent(LoginActivity.this, CheckoutasGuest.class);
        startActivity(checkoutguest);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    private void loginSeviceCall(String name, String password) {

        //  Util.showProgress(LoginActivity.this, v);
        progressDialog.setMessage("Loading...");
        //    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

       /* HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
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

        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(name, password);
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAccessTokenCreate(input, query -> query
                        .customerAccessToken(customerAccessToken -> customerAccessToken
                                .accessToken()
                                .expiresAt()
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
                //   Util.dismissProgress();
                progressDialog.dismiss();
                try {
                    if (!response.data().getCustomerAccessTokenCreate().getUserErrors().isEmpty()) {
                        for (Storefront.UserError error : response.data().getCustomerAccessTokenCreate().getUserErrors()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Storefront.CustomerAccessToken customerAccessToken = response.data().getCustomerAccessTokenCreate().getCustomerAccessToken();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Util.userName = edt_email.getText().toString().trim();
                                Util.password = edt_pswrd.getText().toString().trim();

                                editor.putString("Name", edt_email.getText().toString().trim());
                                editor.putString("password", edt_pswrd.getText().toString().trim());
                                editor.commit();
                                Util.accessToken = customerAccessToken.getAccessToken();
                                if (pageData.contains("orders")) {


                                    /*Intent orders = new Intent(LoginActivity.this, CustomerOrders.class);
                                    startActivity(orders);*/
                                    finish();
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    Util.loginOrder = 1;

                                } else {


                                    Intent myAddress = new Intent(LoginActivity.this, MyAddresses.class);
                                    startActivity(myAddress);
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                              /*  Intent checkout = new Intent(LoginActivity.this, WebCheckoutActivity.class);
                                startActivity(checkout);*/


                                }
                            }
                        });
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                //   Util.dismissProgress();
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void pswrdreset_onclick(View view) {
        Intent resetPswrd = new Intent(LoginActivity.this, ContactUs.class);
        startActivity(resetPswrd);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


}
