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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class AddAddress extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static GraphClient graphClient;
    TextInputLayout textInputLayout_fisrtName, textInputLayout_LastName, textInputLayout_company, textInputLayout_contactno,
            textInputLayout_addressone, textInputLayout_addresstwo, textInputLayout_country, textInputLayout_state,
            textInputLayout_city, textInputLayout_postalcode, textInputLayout_Email;
    Button btn_add;
    EditText edt_firstname, edt_lastname, edt_company, edt_contactno, edt_Email,
            edt_addressline1, edt_addressline2, edt_countryName, edt_state, edt_city, edt_postalcode;
    String addressId, pageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();

        edt_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout_fisrtName.setErrorEnabled(false);
                textInputLayout_fisrtName.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_contactno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textInputLayout_contactno.setErrorEnabled(false);
                textInputLayout_contactno.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textInputLayout_Email.setErrorEnabled(false);
                textInputLayout_Email.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_postalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textInputLayout_postalcode.setErrorEnabled(false);
                textInputLayout_postalcode.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_addressline1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                textInputLayout_addressone.setErrorEnabled(false);
                textInputLayout_addressone.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent i = getIntent();
        pageData = i.getExtras().getString("PageInfo");

        if (pageData.equals("addressEdit")) {

            edt_firstname.setText(i.getExtras().getString("firtname"));
            edt_lastname.setText(i.getExtras().getString("lastname"));
            edt_company.setText(i.getExtras().getString("company"));
            edt_contactno.setText(i.getExtras().getString("phoneNumber"));
            edt_Email.setText(i.getExtras().getString("lastname"));
            edt_addressline1.setText(i.getExtras().getString("Address1"));
            edt_addressline2.setText(i.getExtras().getString("Address2"));
            edt_countryName.setText(i.getExtras().getString("country"));
            edt_state.setText(i.getExtras().getString("city"));
            edt_city.setText(i.getExtras().getString("city"));
            edt_postalcode.setText(i.getExtras().getString("zipcode"));
            addressId = i.getExtras().getString("id");
        }


    }


    public void btnback_OnClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void initViews() {

        textInputLayout_fisrtName = findViewById(R.id.textInputLayout_fisrtName);
        textInputLayout_LastName = findViewById(R.id.textInputLayout_LastName);
        textInputLayout_company = findViewById(R.id.textInputLayout_company);
        textInputLayout_contactno = findViewById(R.id.textInputLayout_contactno);
        textInputLayout_addressone = findViewById(R.id.textInputLayout_addressone);
        textInputLayout_addresstwo = findViewById(R.id.textInputLayout_addresstwo);
        textInputLayout_country = findViewById(R.id.textInputLayout_country);
        textInputLayout_state = findViewById(R.id.textInputLayout_state);
        textInputLayout_city = findViewById(R.id.textInputLayout_city);
        textInputLayout_postalcode = findViewById(R.id.textInputLayout_postalcode);
        textInputLayout_Email = findViewById(R.id.textInputLayout_Email);

        btn_add = findViewById(R.id.btn_add);

        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_company = findViewById(R.id.edt_company);
        edt_contactno = findViewById(R.id.edt_contactno);
        edt_addressline1 = findViewById(R.id.edt_addressline1);
        edt_addressline2 = findViewById(R.id.edt_addressline2);
        edt_countryName = findViewById(R.id.edt_countryName);
        edt_state = findViewById(R.id.edt_state);
        edt_city = findViewById(R.id.edt_city);
        edt_postalcode = findViewById(R.id.edt_postalcode);
        edt_Email = findViewById(R.id.edt_Email);
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);

    }

    public void btn_addclick(View view) {

        textFieldValidation(view);
    }


    private void textFieldValidation(View view) {


        if (edt_firstname.getText().toString().trim().isEmpty()) {
            textInputLayout_fisrtName.setErrorEnabled(true);
            textInputLayout_fisrtName.setError("Please enter First Name");
        } else if (edt_contactno.getText().toString().trim().isEmpty()) {
            textInputLayout_contactno.setErrorEnabled(true);
            textInputLayout_contactno.setError("Please enter Contact Number");
        }
     /*   else if (edt_Email.getText().toString().trim().isEmpty()) {
            textInputLayout_Email.setErrorEnabled(true);
            textInputLayout_Email.setError("Please enter Email Address");
        } else if (!edt_Email.getText().toString().matches(Util.emailPattern)) {
            textInputLayout_Email.setErrorEnabled(true);
            textInputLayout_Email.setError("Please enter valid Email Address");
        } */
        else if (edt_addressline1.getText().toString().trim().isEmpty()) {
            textInputLayout_addressone.setErrorEnabled(true);
            textInputLayout_addressone.setError("Please enter Address");
        } else if (edt_countryName.getText().toString().trim().isEmpty()) {
            textInputLayout_country.setErrorEnabled(true);
            textInputLayout_country.setError("Please enter Country Name");
        } else if (edt_state.getText().toString().trim().isEmpty()) {
            textInputLayout_state.setErrorEnabled(true);
            textInputLayout_state.setError("Please enter State");
        } else if (edt_city.getText().toString().trim().isEmpty()) {
            textInputLayout_city.setErrorEnabled(true);
            textInputLayout_city.setError("Please enter City");
        } else if (edt_postalcode.getText().toString().trim().isEmpty()) {
            textInputLayout_postalcode.setErrorEnabled(true);
            textInputLayout_postalcode.setError("Please enter Postal Code");
        } else {


            Util.hideSoftKeyBoard(getApplicationContext(), view);

            if (pageData.equals("addressEdit")) {


                if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                    updateAddressServiceCall(view);
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
                }

            } else {

                if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
                    addressServiceCall(view);
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void addressServiceCall(View view) {

        Util.showProgress(AddAddress.this, view);
        graphClient =  Util.graphclient(getApplicationContext());



        String firstName = edt_firstname.getText().toString().trim();
        String lastName = edt_lastname.getText().toString().trim();
        String address1 = edt_addressline1.getText().toString().trim();
        String address2 = edt_addressline2.getText().toString().trim();
        String city = edt_city.getText().toString().trim();
        String country = edt_countryName.getText().toString().trim();
        String phone = edt_contactno.getText().toString().trim();
        String province = edt_state.getText().toString().trim();
        String zipcode = edt_postalcode.getText().toString().trim();
        String companyName = edt_company.getText().toString().trim();

        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1Input(Input.value(address1))
                .setAddress2Input(Input.value(address2))
                .setCityInput(Input.value(city))
                .setCountryInput(Input.value(country))
                .setFirstNameInput(Input.value(firstName))
                .setLastNameInput(Input.value(lastName))
                .setPhoneInput(Input.value(phone))
                .setProvinceInput(Input.value(province))
                .setCompanyInput(Input.value(companyName))
                .setZipInput(Input.value(zipcode));

        String token = "";
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressCreate(Util.accessToken, input, query -> query
                        .customerAddress(customerAddress -> customerAddress
                                .address1()
                                .address2()
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
                    if (!response.data().getCustomerAddressCreate().getUserErrors().isEmpty()) {
                        for (Storefront.UserError error : response.data().getCustomerAddressCreate().getUserErrors()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), " Error message " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {


                        String customerCreateInput = response.data().getCustomerAddressCreate().getCustomerAddress().getAddress1();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " Address Added Sucessfully  ", Toast.LENGTH_SHORT).show();
                                finish();
                                MyAddresses.instance.finish();
                                Intent myAddress = new Intent(AddAddress.this, MyAddresses.class);
                                startActivity(myAddress);

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

    private void updateAddressServiceCall(View view) {


        graphClient =  Util.graphclient(getApplicationContext());


        String firstName = edt_firstname.getText().toString().trim();
        String lastName = edt_lastname.getText().toString().trim();
        String address1 = edt_addressline1.getText().toString().trim();
        String address2 = edt_addressline2.getText().toString().trim();
        String city = edt_city.getText().toString().trim();
        String country = edt_countryName.getText().toString().trim();
        String phone = edt_contactno.getText().toString().trim();
        String province = edt_state.getText().toString().trim();
        String zipcode = edt_postalcode.getText().toString().trim();
        String companyName = edt_company.getText().toString().trim();

        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1Input(Input.value(address1))
                .setAddress2Input(Input.value(address2))
                .setCityInput(Input.value(city))
                .setCountryInput(Input.value(country))
                .setFirstNameInput(Input.value(firstName))
                .setLastNameInput(Input.value(lastName))
                .setPhoneInput(Input.value(phone))
                .setProvinceInput(Input.value(province))
                .setCompanyInput(Input.value(companyName))
                .setZipInput(Input.value(zipcode));


        ID id = new ID(addressId);
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressUpdate(Util.accessToken, id, input, query -> query
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

                    if (!response.data().getCustomerAddressUpdate().getUserErrors().isEmpty()) {
                        for (Storefront.UserError error : response.data().getCustomerAddressUpdate().getUserErrors()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), " Error message " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {

                        //    String customerCreateInput = response.data().getCustomerAddressCreate().getCustomerAddress().getAddress1();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), " Address Updated Sucessfully  ", Toast.LENGTH_SHORT).show();
                                finish();
                                MyAddresses.instance.finish();
                                Intent myAddress = new Intent(AddAddress.this, MyAddresses.class);
                                startActivity(myAddress);

                            }
                        });
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Util.dismissProgress();
                Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
