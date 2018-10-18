package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.shopify.buy3.Storefront;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ContactUs extends AppCompatActivity {

    TextInputLayout textInputLayout_name, textInputLayout_email;
    EditText edt_name, edt_email, edttxt_msg;
    Button btn_send;
    private ProgressDialog progressDialog;
    private GraphClient graphClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_email.getText().toString().isEmpty()) {
                    textInputLayout_email.setErrorEnabled(true);
                    textInputLayout_email.setError("Please enter your Email Address");
                } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
                    textInputLayout_email.setErrorEnabled(true);
                    textInputLayout_email.setError("Please enter a valid Email Address");
                } else {


                    if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                        passwordResetServiceCall(edt_email.getText().toString().trim());
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

    }



    /* public void btn_sendclick(View view) {

       if (edt_name.getText().toString().trim().isEmpty()) {

            textInputLayout_name.setErrorEnabled(true);
            textInputLayout_name.setError("Please enter your Name");
        } else if (edt_email.getText().toString().isEmpty()) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter your Email Address");
        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter a valid Email Address");
        } else if (edttxt_msg.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), " Please write some message ", Toast.LENGTH_SHORT).show();
        }else {
        }

        if (edt_email.getText().toString().isEmpty()) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter your Email Address");
        } else if (!edt_email.getText().toString().matches(Util.emailPattern)) {
            textInputLayout_email.setErrorEnabled(true);
            textInputLayout_email.setError("Please enter a valid Email Address");
        } else {

            passwordResetServiceCall(edt_email.getText().toString().trim());
        }
    } */

    private void passwordResetServiceCall(String email) {


        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        graphClient = Util.graphclient(getApplicationContext());

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerRecover(email, query -> query
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

                try {
                    progressDialog.dismiss();

                    if (!response.data().getCustomerRecover().getUserErrors().isEmpty()) {
                        for (Storefront.UserError error : response.data().getCustomerRecover().getUserErrors()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " Sent an link to your email address to reset your password ", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Please try agajn later" , Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            }
        });


    }

    private void initViews() {
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        edt_email = findViewById(R.id.edt_email);
        textInputLayout_email = findViewById(R.id.textInputLayout_email);
        btn_send = findViewById(R.id.btn_send);
    }
}
