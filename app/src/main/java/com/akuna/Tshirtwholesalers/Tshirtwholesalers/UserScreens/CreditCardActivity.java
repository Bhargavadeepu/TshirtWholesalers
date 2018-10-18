package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.CardClient;
import com.shopify.buy3.CreditCard;
import com.shopify.buy3.CreditCardVaultCall;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CreditCardActivity extends AppCompatActivity {

    private GraphClient graphClient;
    private CardClient cardClient;
    Spinner spinner_mnth, spinner_year;
    EditText edt_month, edt_year, edt_cardno, edt_firstname, edt_lstname, edt_cvvno;
    TextInputLayout textInputLayout_creditcardno, textInputLayout_firstname, textInputLayout_lastname;
    List<String> monthList = new ArrayList<>();
    List<String> yearList = new ArrayList<>();
    String cardUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*  Initilization of ids*/
        initViews();

     /*   creditCardUrl();
        // Setting adpter for the spinner month
        monthList.add("1");
        monthList.add("2");
        monthList.add("3");
        monthList.add("4");
        monthList.add("5");
        monthList.add("6");
        monthList.add("7");
        monthList.add("8");
        monthList.add("9");
        monthList.add("10");
        monthList.add("11");
        monthList.add("12");


        ArrayAdapter monthadpter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, monthList);
        monthadpter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_mnth.setAdapter(monthadpter);

        // Setting adpter for the spinner year
        yearList.add("2017");
        yearList.add("2018");
        yearList.add("2019");
        yearList.add("2020");
        yearList.add("2021");
        yearList.add("2021");
        yearList.add("2022");
        yearList.add("2023");
        yearList.add("2024");
        yearList.add("2025");
        yearList.add("2026");
        yearList.add("2027");

        ArrayAdapter yearAdpter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList);
        yearAdpter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_year.setAdapter(yearAdpter);
*/

    }

    public void initViews() {

        spinner_mnth = findViewById(R.id.spinner_mnth);
        spinner_year = findViewById(R.id.spinner_year);

        edt_cvvno = findViewById(R.id.edt_cvvno);
        edt_cardno = findViewById(R.id.edt_cardno);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lstname = findViewById(R.id.edt_lstname);

        textInputLayout_creditcardno = findViewById(R.id.textInputLayout_creditcardno);
        textInputLayout_firstname = findViewById(R.id.textInputLayout_firstname);
        textInputLayout_lastname = findViewById(R.id.textInputLayout_lastname);

    }

    /*  Spinner Month click Listner*/
    public void edt_Onclickmnth(View view) {
        spinner_mnth.performClick();
    }

    /*  Spinner year click Listner*/
    public void edt_Onclickyear(View view) {
        spinner_year.performClick();
    }

    public void btn_confirmclick(View view) {
        validation();
    }


    public void validation() {
        if (edt_cardno.getText().toString().isEmpty()) {
            textInputLayout_creditcardno.setErrorEnabled(true);
            textInputLayout_creditcardno.setError(" Please enter credit card number");
        } else if (edt_firstname.getText().toString().isEmpty()) {
            textInputLayout_firstname.setErrorEnabled(true);
            textInputLayout_firstname.setError(" Please enter first name");
        } else if (edt_lstname.getText().toString().isEmpty()) {
            textInputLayout_lastname.setErrorEnabled(true);
            textInputLayout_lastname.setError(" Please enter last name");
        } else if (edt_cvvno.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), " please enter credit card CVV number ", Toast.LENGTH_SHORT).show();
        } else {

            creditCardPaymentService();
        }

    }


    public void btnback_OnClick(View view) {

        finish();
    }


    private void creditCardPaymentService() {

        edt_cvvno = findViewById(R.id.edt_cvvno);
        edt_cardno = findViewById(R.id.edt_cardno);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();


        CardClient cardClient = new CardClient();

        CreditCard creditCard = CreditCard.builder()
                .firstName(edt_firstname.getText().toString())
                .lastName(edt_lstname.getText().toString())
                .number(edt_cardno.getText().toString())
                .expireMonth("03")
                .expireYear("2018")
                .verificationCode(edt_cvvno.getText().toString())
                .build();
        cardClient.vault(creditCard, cardUrl).enqueue(new CreditCardVaultCall.Callback() {
            @Override
            public void onResponse(@NonNull String token) {

                String responseData = token.toString();

            }

            @Override
            public void onFailure(@NonNull IOException error) {

                Toast.makeText(getApplicationContext(), " error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void creditCardUrl() {

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

        Storefront.QueryRootQuery query = Storefront.query(rootQueryBuilder ->
                rootQueryBuilder
                        .shop(shopQueryBuilder ->
                                shopQueryBuilder
                                        .paymentSettings(paymentQueryBuilder -> paymentQueryBuilder
                                                .cardVaultUrl()
                                        )
                        )
        );

        QueryGraphCall call = graphClient.queryGraph(query);
        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                cardUrl = response.data().getShop().getPaymentSettings().getCardVaultUrl();

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

                Toast.makeText(getApplicationContext(), " Erro " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}
