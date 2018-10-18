package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.InternetCheck.CheckNetwork;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models.CartModel;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.PaymentGateWay.PaypalpaymentActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.UWSDb.DBManager;
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

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class PaymentMode extends AppCompatActivity {

    public static PaymentMode instance = null;
    RadioButton radiobtn_creditcard, radioButton_other;
    boolean creditCard = false, otherCard = false;
    TextView tv_orderttl, tv_shippingcharges, tv_grandttl, tv_noitems, itemcost, tv_discountlbl,
            tv_custmaername, tv_addressline1, tv_state, tv_country, tv_zipcode, tv_mobileno, tv_discountprice;
    private DBManager dbManager;
    ArrayList<CartModel> cartModelArrayList;
    private GraphClient graphClient;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        orderApicall();




        dbManager = new DBManager(this);
        dbManager.open();

        cartModelArrayList = dbManager.GetCartDetails();

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {
            //  Toast.makeText(getApplicationContext(), " No Cart items to display", Toast.LENGTH_SHORT).show();
        } else {
            tv_noitems.setText("Items" + "(" + cartModelArrayList.size() + ")");
        }

        //   tv_custmaername.setText(Util.firstName + Util.lastName);
        tv_custmaername.setText(Util.firstName);
        tv_addressline1.setText(Util.addressline1);
        tv_state.setText(Util.provinceses);
        tv_country.setText(Util.country);
        tv_zipcode.setText(Util.zipcode);
        tv_mobileno.setText(Util.phono);
        tv_orderttl.setText("$" + Util.itemTtlPrice);

        tv_shippingcharges.setText("" + Util.shippingPrice);

        //    itemcost.setText("$" + Util.grandTtl);

        if (Util.shimentCharge == 1) {
            tv_grandttl.setText("" + Util.itemTtlPrice);
            itemcost.setText("" + Util.itemTtlPrice);
        } else {
            tv_grandttl.setText("" + Util.grandTtl);
            itemcost.setText("" + Util.grandTtl);
        }


        //  itemcost.setText("$" + Util.itemTtlPrice);
        /*Radio  button click events*/
        radiobtn_creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otherCard) {
                    otherCard = false;
                    creditCard = true;
                    radioButton_other.setChecked(false);
                } else {
                    creditCard = true;
                }
            }
        });

        radioButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (creditCard) {
                    creditCard = false;
                    otherCard = true;
                    radiobtn_creditcard.setChecked(false);

                } else {
                    otherCard = true;
                }
            }
        });

    }

    public void btnback_OnClick(View view) {

        /** Funcation to delete the cart Item **/

        cartItemDelete();
        finish();


    }

    public void initViews() {
        radiobtn_creditcard = findViewById(R.id.radiobtn_creditcard);
        radioButton_other = findViewById(R.id.radioButton_other);
        tv_orderttl = findViewById(R.id.tv_orderttl);
        tv_shippingcharges = findViewById(R.id.tv_shippingcharges);
        tv_grandttl = findViewById(R.id.tv_grandttl);

        tv_noitems = findViewById(R.id.tv_noitems);
        tv_custmaername = findViewById(R.id.tv_custmaername);
        tv_addressline1 = findViewById(R.id.tv_addressline1);
        tv_state = findViewById(R.id.tv_state);
        tv_country = findViewById(R.id.tv_country);
        tv_zipcode = findViewById(R.id.tv_zipcode);
        tv_mobileno = findViewById(R.id.tv_mobileno);
        itemcost = findViewById(R.id.itemcost);
        tv_discountprice = findViewById(R.id.tv_discountprice);
        tv_discountlbl = findViewById(R.id.tv_discountlbl);
        instance = this;
    }


    public void btn_confrimorder(View view) {

     /*   if (creditCard) {
            alertPopup("creditCard");

        } else {
            alertPopup("othermethod");

        }*/
        Intent checkout = new Intent(PaymentMode.this, WebCheckoutActivity.class);
        startActivity(checkout);

    }

    public void tv_giftCardClick(View view) {
        giftCardPopup();

    }

    public void tv_promocode(View view) {
        couponCodePopup();
    }


    public void giftCardPopup() {

        ImageView img_close;
        Button btn_applygiftcard;
        final TextInputLayout textInputLayout_edittxt;
        final EditText edt_txtfield;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentMode.this);
        View mView = getLayoutInflater().inflate(R.layout.giftcardpopup, null);

        img_close = mView.findViewById(R.id.img_close);
        btn_applygiftcard = mView.findViewById(R.id.btn_applygiftcard);
        textInputLayout_edittxt = mView.findViewById(R.id.textInputLayout_edittxt);
        edt_txtfield = mView.findViewById(R.id.edt_txtfield);


        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_applygiftcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              /*  if (edt_txtfield.getText().toString().isEmpty()) {
                    textInputLayout_edittxt.setError("Please enter a gift card code");
                    textInputLayout_edittxt.setErrorEnabled(true);
                } else {
                    // Api calls
                  giftCardserviceCall();

                }*/
                giftCardserviceCall(v, edt_txtfield.getText().toString().trim());

            }
        });


    }


    public void couponCodePopup() {

        ImageView img_close;
        Button btn_applygiftcard;
        final TextInputLayout textInputLayout_edittxt;
        final EditText edt_txtfield;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentMode.this);
        View mView = getLayoutInflater().inflate(R.layout.promocodepopup, null);

        img_close = mView.findViewById(R.id.img_close);
        btn_applygiftcard = mView.findViewById(R.id.btn_applygiftcard);
        textInputLayout_edittxt = mView.findViewById(R.id.textInputLayout_edittxt);
        edt_txtfield = mView.findViewById(R.id.edt_txtfield);

        // final AlertDialog dialog;

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_applygiftcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_txtfield.getText().toString().isEmpty()) {
                    textInputLayout_edittxt.setError("Please enter a coupon code");
                    textInputLayout_edittxt.setErrorEnabled(true);
                } else {
                    // Api calls
                    giftCardserviceCall(v, edt_txtfield.getText().toString().trim());

                }
            }
        });


    }


    public void alertPopup(final String infofdata) {

        String title = null;


        if (infofdata.contains("creditCard")) {
            title = "Are you sure you wish to pay via Credit Card?";

        } else {
            title = "Are you sure you wish to pay via Other ?";
        }


        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (infofdata.contains("creditCard")) {
                                    Intent checkout = new Intent(PaymentMode.this, CreditCardActivity.class);
                                    startActivity(checkout);
                                } else {


                                   /* Intent checkout = new Intent(PaymentMode.this, PaypalpaymentActivity.class);
                                    startActivity(checkout);*/


                                    Intent checkout = new Intent(PaymentMode.this, WebCheckoutActivity.class);
                                    startActivity(checkout);


                                }

                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );


        dialog = b.create();
        dialog.show();

    }


    private void giftCardserviceCall(View v, String giftcode) {


        Util.showProgress(PaymentMode.this, v);
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

        ID checkoutId = new ID(Util.CheckoutId);

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutDiscountCodeApply(giftcode, checkoutId, discountQuery -> discountQuery
                        .userErrors(userErrorQuery -> userErrorQuery
                                .message()
                                .field()
                        )
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                                .totalPrice()
                                .subtotalPrice()
                                .taxesIncluded()
                                .totalTax()
                                .orderStatusUrl()
                                .appliedGiftCards(giftCardQuery -> giftCardQuery
                                        .amountUsed()
                                        .balance()
                                )
                        )
                )
        );
        MutationGraphCall call = graphClient.mutateGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                Util.dismissProgress();
                if (!response.data().getCheckoutDiscountCodeApply().getUserErrors().isEmpty()) {
                    for (Storefront.UserError error : response.data().getCheckoutDiscountCodeApply().getUserErrors()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "  " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    try {

                        BigDecimal discountttlprice = response.data().getCheckoutDiscountCodeApply().getCheckout().getTotalPrice();
                        String webUrl = response.data().getCheckoutDiscountCodeApply().getCheckout().getWebUrl();
                        BigDecimal subttlprice = response.data().getCheckoutDiscountCodeApply().getCheckout().getSubtotalPrice();
                        BigDecimal totalprice = Util.itemTtlPrice;
                        BigDecimal discount = totalprice.subtract(subttlprice);
                        //  Util.checkoutWebUrl = webUrl;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    dialog.dismiss();
                                    // BigDecimal amnt =  Util.grandTtl;
                                    tv_discountlbl.setVisibility(View.VISIBLE);
                                    tv_discountprice.setVisibility(View.VISIBLE);
                                    tv_discountlbl.setVisibility(View.VISIBLE);
                                    tv_discountprice.setText("$ -" + discount);
                                    // BigDecimal ttlAmnt = Util.grandTtl.subtract(discount);
                                    BigDecimal ttlAmnt = Util.itemTtlPrice.subtract(discount);
                                    tv_grandttl.setText("" + ttlAmnt);
                                    itemcost.setText("" + ttlAmnt);

                                } catch (Exception e) {

                                    Toast.makeText(getApplicationContext(), " Excepation " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), " Excepation ", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Util.dismissProgress();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


    private void cartItemDelete() {

        if (cartModelArrayList == null || cartModelArrayList.size() == 0) {

        } else {
            tv_noitems.setText("Items" + "(" + cartModelArrayList.size() + ")");

            if (cartModelArrayList.size() > Util.initcartItemCount) {
                for (int i = 0; i < cartModelArrayList.size(); i++) {

                    dbManager.deleteCart(cartModelArrayList.get(i).getCartId());
                    // dbManager.deleteCartitemBasedOnvarId(cartModelArrayList.get(i).getVariantID());
                }

            } else {

                //     Toast.makeText(getApplicationContext(), " In else part ", Toast.LENGTH_SHORT).show();


            }


        }


    }

    private void orderApicall() {



        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
            graphClient =  Util.graphclient(getApplicationContext());

            Storefront.QueryRootQuery query = Storefront.query(root -> root
                    .customer(Util.accessToken, customer -> customer
                            .orders(arg -> arg.first(10), connection -> connection
                                    .edges(edge -> edge
                                            .node(node -> node
                                                    .orderNumber()
                                                    .customerUrl()
                                                    .totalPrice()
                                                    .subtotalPrice()
                                                    .statusUrl()
                                                    .lineItems(arg -> arg.first(10), lineItems -> lineItems.edges(orderlistiteam -> orderlistiteam.node(
                                                            child -> child.title().quantity().variant(va -> va.price()))))
                                            )
                                    )
                            )
                    )
            );


            QueryGraphCall call = graphClient.queryGraph(query);

            call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                @Override
                public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                    //   progressDialog.dismiss();
                    List<Storefront.Order> orders = new ArrayList<>();
                    for (Storefront.OrderEdge customer : response.data().getCustomer().getOrders().getEdges()) {
                        orders.add(customer.getNode());

                        List<Storefront.ProductVariant> Orderlineitems = new ArrayList<>();
                        for (Storefront.OrderLineItemEdge orderLineItemEdge : customer.getNode().getLineItems().getEdges()) {
                            Orderlineitems.add(orderLineItemEdge.getNode().getVariant());

                        }


                    }
                    Util.initcartItemCount = orders.size();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   Toast.makeText(getApplicationContext(), " init Order Count  " + Util.initcartItemCount, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull GraphError error) {
                    //  progressDialog.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), " " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Please check your internet connection.Try again later", Toast.LENGTH_SHORT).show();
        }







    }


}
