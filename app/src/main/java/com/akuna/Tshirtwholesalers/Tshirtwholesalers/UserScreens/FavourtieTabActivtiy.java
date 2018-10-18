package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.FavouriteAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class FavourtieTabActivtiy extends AppCompatActivity {


    private DBManager dbManager;
    //  private ArrayList<FovuroriteModel> favoriteArrylist = new ArrayList<FovuroriteModel>();
    TextView txt_oldprice;
    View rootView;
    Button btn_login, btn_logout;
    RecyclerView recyclerview_fav;
    FavouriteAdpter favouriteAdpter;
    FovuroriteModel fovuroriteModel;
    private List<FovuroriteModel> favoriteArrylist = new ArrayList<>();
    private String pageData;
    private GraphClient graphClient;
    private ProgressDialog progressDialog;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourtie_tab_activtiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        //  Toast.makeText(getActivity(), "Name " + (sharedpreferences.getString("Name", "")), Toast.LENGTH_SHORT).show();

        Util.userName = sharedpreferences.getString("Name", "");
        Util.password = sharedpreferences.getString("password", "");
        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        /* Initialization of views*/
        initViews();
//        txt_oldprice.setPaintFlags(txt_oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        if (!Util.userName.isEmpty()) {

            btn_logout.setVisibility(View.VISIBLE);

        }


        AsynTaskrunner asynTaskrunner = new AsynTaskrunner();
        asynTaskrunner.execute();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Util.userName.isEmpty()) {
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.putExtra("pageInfo", "orders");
                    startActivity(login);

                } else {


                    loginSeviceCall();
                }


            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.clear();
                editor.remove("Name");
                editor.remove("password");
                editor.commit();

                // Reload current fragment
           /*     Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("MyFavouritesTab");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();*/
               /* Fragment currentFragment = getFragmentManager().findFragmentByTag("MyFavouritesTab");
                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment);
                fragTransaction.attach(currentFragment);
                fragTransaction.commit();*/

            }
        });


    }

    private class AsynTaskrunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            favoriteArrylist = dbManager.GetFavouriteDetails();

            if (favoriteArrylist == null || favoriteArrylist.size() == 0) {

            } else {

                // Toast.makeText(getActivity(), " Favourite List Size " + favoriteArrylist.size(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < favoriteArrylist.size(); i++) {

                    //     Toast.makeText(getActivity(), "  For loop i value" + i, Toast.LENGTH_SHORT).show();

                    //  if (i == 1) {
                    fovuroriteModel = new FovuroriteModel();
                    fovuroriteModel.setAvailbleforSale(favoriteArrylist.get(i).getAvailbleforSale());
                    fovuroriteModel.setColor(favoriteArrylist.get(i).getColor());
                    fovuroriteModel.setDiscount(favoriteArrylist.get(i).getDiscount());
                    fovuroriteModel.setImageSrc(favoriteArrylist.get(i).getImageSrc());
                    fovuroriteModel.setPrice(favoriteArrylist.get(i).getPrice());
                    fovuroriteModel.setProductID(favoriteArrylist.get(i).getProductID());
                    fovuroriteModel.setQuantity(favoriteArrylist.get(i).getQuantity());
                    fovuroriteModel.setSize(favoriteArrylist.get(i).getSize());
                    fovuroriteModel.setTitle(favoriteArrylist.get(i).getTitle());
                    fovuroriteModel.setTotalPrice(favoriteArrylist.get(i).getTotalPrice());
                    fovuroriteModel.setVariantID(favoriteArrylist.get(i).getVariantID());
                    fovuroriteModel.setFavId(favoriteArrylist.get(i).getFavId());
                    fovuroriteModel.setDescripation(favoriteArrylist.get(i).getDescripation());
                    fovuroriteModel.setComparaterPrice(favoriteArrylist.get(i).getComparaterPrice());
                    favoriteArrylist.add(fovuroriteModel);


               //     recyclerview = findViewById(R.id.recyclerview);
                    pageData = "tab";
                    recyclerview_fav.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerview_fav.setHasFixedSize(true);
                    favouriteAdpter = new FavouriteAdpter(getApplicationContext(), favoriteArrylist, pageData);
                    recyclerview_fav.setAdapter(favouriteAdpter);


                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }


    private void initViews() {
        txt_oldprice = findViewById(R.id.txt_actualprice);
        recyclerview_fav = findViewById(R.id.recyclerview_fav);
        btn_login = findViewById(R.id.btn_login);
        btn_logout = findViewById(R.id.btn_logout);
        progressDialog = new ProgressDialog(getApplicationContext(), R.style.MyAlertDialogStyle);
        dbManager = new DBManager(getApplicationContext());
        dbManager.open();
    }

    private void loginSeviceCall() {

        //  Util.showProgress(getActivity().this, v);

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

        Util.getHttpmethod(getApplicationContext());
        graphClient = GraphClient.builder(getApplicationContext())
                .shopDomain(Util.shopDomain)
                .accessToken(Util.screateKey)
                .httpClient(httpClient)
                .httpCache(new File(getApplicationContext().getCacheDir(), "/https"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();

        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(Util.userName, Util.password);
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

                progressDialog.dismiss();

                if (!response.data().getCustomerAccessTokenCreate().getUserErrors().isEmpty()) {
                    for (Storefront.UserError error : response.data().getCustomerAccessTokenCreate().getUserErrors()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Storefront.CustomerAccessToken customerAccessToken = response.data().getCustomerAccessTokenCreate().getCustomerAccessToken();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), " AccessToken  " + customerAccessToken.getAccessToken(), Toast.LENGTH_SHORT).show();
                            Util.accessToken = customerAccessToken.getAccessToken();
                            Intent register = new Intent(getApplicationContext(), CustomerOrders.class);
                            startActivity(register);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


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
                        Toast.makeText(getApplicationContext(), " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


}
