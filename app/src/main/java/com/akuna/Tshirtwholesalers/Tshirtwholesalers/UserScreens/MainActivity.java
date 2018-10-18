package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSAdpters.CartItemAdpter;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance = null;
    private GraphClient graphClient;
    private ProgressDialog progressDialog;
    DashboardTab dashboardTab;
    TabLayout tabLayout;
    TextView txttilte, tv_cartcount;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    View homeView, categoriesView, profileView, cartView, otherInfoVw, homeViewunselected, categorySelected;
    private DBManager dbManager;
    ArrayList<CartModel> cartModelArrayList;
    int limitNumberOfPages = 2;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dashboardTab = new DashboardTab();
        progressDialog = new ProgressDialog(this);
        instance = this;

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //  setSupportActionBar(toolbar);
        initViews();

        dbManager = new DBManager(this);
        dbManager.open();

        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            // Toast.makeText(getApplicationContext(), " No Cart items to display", Toast.LENGTH_SHORT).show();
            tv_cartcount.setText("");
            tv_cartcount.setVisibility(View.INVISIBLE);
        } else {
            tv_cartcount.setText("" + cartModelArrayList.size());
        }



/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);*/


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mViewPager = (ViewPager) findViewById(R.id.container);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.


        mViewPager.setOffscreenPageLimit(limitNumberOfPages);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        //  setTabIcons();
        homeView = getLayoutInflater().inflate(R.layout.customtab, null);
        homeView.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_action_homeselected);
        //  homeView.findViewById(R.id.custom_layout).setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout.addTab(tabLayout.newTab().setCustomView(homeView));


        categoriesView = getLayoutInflater().inflate(R.layout.customtab, null);
        categoriesView.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_action_brandunselected);
        tabLayout.addTab(tabLayout.newTab().setCustomView(categoriesView));

       /* profileView = getLayoutInflater().inflate(R.layout.customtab, null);
        profileView.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_action_profile);
        tabLayout.addTab(tabLayout.newTab().setCustomView(profileView));*/


      /*  cartView = getLayoutInflater().inflate(R.layout.customcarttab, null);
        cartView.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_action_cart);
        // cartView.findViewById(R.id.tv_cart_count).
        tabLayout.addTab(tabLayout.newTab().setCustomView(cartView));*/


        otherInfoVw = getLayoutInflater().inflate(R.layout.customtab, null);
        otherInfoVw.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_action_iconunselected);
        tabLayout.addTab(tabLayout.newTab().setCustomView(otherInfoVw));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int tabPostion = tab.getPosition();
                switch (tabPostion) {

                    case 0:
                        TabLayout.Tab homeTab = tabLayout.getTabAt(tabPostion);
                        View selectedhometab = homeTab.getCustomView();
                        ImageView homeimg = selectedhometab.findViewById(R.id.icon);
                        homeimg.setBackgroundResource(R.drawable.ic_action_homeselected);
                        txttilte.setText("Tshirt Wholesalers");
                        break;
                    case 1:
                        TabLayout.Tab tab1 = tabLayout.getTabAt(tabPostion);
                        View selected = tab1.getCustomView();
                        ImageView img = selected.findViewById(R.id.icon);
                        img.setBackgroundResource(R.drawable.ic_action_brandselected);
                        // txttilte.setText("Categories");
                        txttilte.setText("Collections");
                        break;
                    case 2:
                        // tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_profileselected);
                     /*   TabLayout.Tab profileTab = tabLayout.getTabAt(tabPostion);
                        View selectedtab = profileTab.getCustomView();
                        ImageView profileimg = selectedtab.findViewById(R.id.icon);
                        profileimg.setBackgroundResource(R.drawable.ic_action_profileselected);
                        txttilte.setText("Favourite");
                        // fab.setVisibility(View.VISIBLE);*/

                        TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostion);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_iconselected);
                        txttilte.setText("More");


                        break;
                    // case 3:
                    //  tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_cartselected);
                   /*     TabLayout.Tab cartTab = tabLayout.getTabAt(tabPostion);
                        View selectedcartTab = cartTab.getCustomView();
                        ImageView cartimg = selectedcartTab.findViewById(R.id.icon);
                        cartimg.setBackgroundResource(R.drawable.ic_action_cartselected);
                        txttilte.setText("My Cart");
                        break;*/
             /*           TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostion);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_otherinfoselected);
                        txttilte.setText("More");*/
                    //     break;

                  /*  case 4:
                      TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostion);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_otherinfoselected);
                        txttilte.setText("More");
                        break;*/
                    default:
                        // tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_profileselected);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int tabPostions = tab.getPosition();

                switch (tabPostions) {

                    case 0:
                        // tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_home);
                        TabLayout.Tab homeTab = tabLayout.getTabAt(tabPostions);
                        View selectedhometab = homeTab.getCustomView();
                        //    selectedhometab.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_color_selector));
                        //    selectedhometab.setBackgroundColor(Color.parseColor("#EFDF0A"));
                        ImageView homeimg = selectedhometab.findViewById(R.id.icon);
                        homeimg.setBackgroundResource(R.drawable.homedeselected);
                        //  homeimg.setBackgroundColor(Color.parseColor("#8CBCFF"));
                        //  fab.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        // tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_categories);
                        TabLayout.Tab categorieTab = tabLayout.getTabAt(tabPostions);
                        View selected = categorieTab.getCustomView();
                        ImageView img = selected.findViewById(R.id.icon);
                        img.setBackgroundResource(R.drawable.ic_action_brandunselected);
                        //   fab.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                     /*   TabLayout.Tab profileTab = tabLayout.getTabAt(tabPostions);
                        View selectedtab = profileTab.getCustomView();
                        ImageView profileimg = selectedtab.findViewById(R.id.icon);
                        profileimg.setBackgroundResource(R.drawable.ic_action_profile);
                     //   fab.setVisibility(View.INVISIBLE);*/

                        TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostions);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_iconunselected);
                        break;


                    //    case 3:
                    // tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_cart);
                   /*     TabLayout.Tab cartTab = tabLayout.getTabAt(tabPostions);
                        View selectedcartTab = cartTab.getCustomView();
                        ImageView cartimg = selectedcartTab.findViewById(R.id.icon);
                        cartimg.setBackgroundResource(R.drawable.ic_action_cart);
                        fab.setVisibility(View.INVISIBLE);
                        break;*/
                  /*      TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostions);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_otherinfo);
                        break;*/



                   /* case 4:
                        TabLayout.Tab otherInfoTab = tabLayout.getTabAt(tabPostions);
                        View selectedothertab = otherInfoTab.getCustomView();
                        ImageView otherInfoimg = selectedothertab.findViewById(R.id.icon);
                        otherInfoimg.setBackgroundResource(R.drawable.ic_action_otherinfo);
                        break;*/
                    default:


                }


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                int tabPostions = tab.getPosition();

                //   Toast.makeText(getApplicationContext(), " ReSelected tab position  " + tabPostions, Toast.LENGTH_SHORT).show();


            }
        });
        //    fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent otherInfo = new Intent(MainActivity.this, FavouritesList.class);
                startActivity(otherInfo);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

    }



    /* Mycart Click Listner */


    public void btnmycart_OnClick(View view) {

        Intent cart = new Intent(MainActivity.this, MyCartActivity.class);
        startActivity(cart);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    public void btn_OnClicksearch(View view) {
        Intent search = new Intent(MainActivity.this, LivesearchActivity.class);
        search.putExtra("PageInfo", "NoData");
        startActivity(search);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    private int[] tabIcons = {

            R.drawable.ic_action_homeselected,
            R.drawable.ic_action_brandunselected,
            //    R.drawable.ic_action_profile,
            //   R.drawable.ic_action_cart,
            R.drawable.ic_action_iconunselected,
    };


    @Override
    protected void onPause() {
        super.onPause();
        // Toast.makeText(getApplicationContext(), " OnResume Called ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to exit from the application?");
        builder.setMessage("")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        //   spinner.setVisibility(View.GONE);


    }

    private void setTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        //  tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }


    private void initViews() {

        txttilte = findViewById(R.id.txttilte);
        tv_cartcount = findViewById(R.id.tv_cartcount);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    DashboardTab dashboardTab = new DashboardTab();
                    return dashboardTab;
                case 1:
                    CategoriesTab categoriesTab = new CategoriesTab();
                    return categoriesTab;
                case 2:
                  /*  MyFavouritesTab myProfileTab = new MyFavouritesTab();
                    return myProfileTab;*/
                    OtherInfoTab otherInfoTab = new OtherInfoTab();
                    return otherInfoTab;
                //     case 3:
                   /* MyCartTab myCartTab = new MyCartTab();
                    return myCartTab;*/
                   /* OtherInfoTab otherInfoTab = new OtherInfoTab();
                    return otherInfoTab;*/
             /*
                case 4:
                    OtherInfoTab otherInfoTab = new OtherInfoTab();
                    return otherInfoTab;*/
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    protected void onStart() {
        super.onStart();
        //   Toast.makeText(getApplicationContext(), " OnStart Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(), " onResume Called", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //   Toast.makeText(getApplicationContext(), " onStop Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(getApplicationContext(), " onRestart Called", Toast.LENGTH_SHORT).show();

        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            // Toast.makeText(getApplicationContext(), " No Cart items to display", Toast.LENGTH_SHORT).show();
            tv_cartcount.setText("");
            tv_cartcount.setVisibility(View.INVISIBLE);
        } else {
            tv_cartcount.setText("" + cartModelArrayList.size());
            tv_cartcount.setVisibility(View.VISIBLE);
        }

    }


}
