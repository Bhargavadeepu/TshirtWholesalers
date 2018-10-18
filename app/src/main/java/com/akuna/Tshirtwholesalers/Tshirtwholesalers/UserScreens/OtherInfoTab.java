package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class OtherInfoTab extends Fragment {


    TextView txt_abtus, txt_questions, txt_embroidery, txt_screenprint, txt_heattransfer, txt_ourcustomer,
            txt_shippingpolicy, txt_refundpolicy, txt_contactUs, txt_reviews, txt_blog, txt_privacypolicy, txt_termsofservice,
            txt_rateus, txt_shareApp, txt_login;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_otherinfo, container, false);

        initViews();


        txt_abtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("AboutUs");
            }
        });


        txt_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("Question");
            }
        });
        txt_embroidery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("embroidery");
            }
        });


        txt_screenprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("screenprints");
            }
        });
        txt_heattransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("heattransfer");
            }
        });
        txt_ourcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("ourcustomer");
            }
        });
        txt_shippingpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("shippingpolicy");
            }
        });
        txt_refundpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("refundpolicy");
            }
        });
        txt_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("contactus");
            }
        });
        txt_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("reviews");
            }
        });
        txt_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("blogs");
            }
        });
        txt_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("privacypolicy");
            }
        });
        txt_termsofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall("termsandservices");
            }
        });
        txt_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "I love using Tshirt Wholesalers, it's simple and incredible. You should try it here :https://play.google.com/store/apps/details?id=com.akuna.Tshirtwholesalers.Tshirtwholesalers";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing via"));*/


                String appname = "com.akuna.Tshirtwholesalers.Tshirtwholesalers";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appname)));


            }
        });
        txt_shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //  String shareBodyText = "I love using Tshirt Wholesalers, it's simple and incredible. You should try it here :https://play.google.com/store/apps/details?id=ttmobile.cli.com.ttmobile";
                String shareBodyText = "I love using Tshirt Wholesalers, it's simple and incredible. You should try it here :https://play.google.com/store/apps/details?id=com.akuna.Tshirtwholesalers.Tshirtwholesalers";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing via"));

                // launchMarket();
            }
        });
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intentCall("login");*/
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.putExtra("pageInfo", "orders");
                startActivity(login);
            }
        });

        return rootView;
    }


    private void initViews() {


        txt_abtus = rootView.findViewById(R.id.txt_abtus);
        txt_questions = rootView.findViewById(R.id.txt_questions);
        txt_embroidery = rootView.findViewById(R.id.txt_embroidery);
        txt_screenprint = rootView.findViewById(R.id.txt_screenprint);
        txt_heattransfer = rootView.findViewById(R.id.txt_heattransfer);
        txt_ourcustomer = rootView.findViewById(R.id.txt_ourcustomer);
        txt_shippingpolicy = rootView.findViewById(R.id.txt_shippingpolicy);
        txt_refundpolicy = rootView.findViewById(R.id.txt_refundpolicy);
        txt_contactUs = rootView.findViewById(R.id.txt_contactUs);
        txt_reviews = rootView.findViewById(R.id.txt_reviews);
        txt_privacypolicy = rootView.findViewById(R.id.txt_privacypolicy);
        txt_termsofservice = rootView.findViewById(R.id.txt_termsofservice);
        txt_rateus = rootView.findViewById(R.id.txt_rateus);
        txt_shareApp = rootView.findViewById(R.id.txt_shareApp);
        txt_login = rootView.findViewById(R.id.txt_login);
        txt_blog = rootView.findViewById(R.id.txt_blog);

    }


    private void intentCall(String pageInfo) {
        String data = pageInfo;
        Intent webIntent = new Intent(getActivity(), WebviewActivity.class);
        webIntent.putExtra("PageInfo", data);
        startActivity(webIntent);
    }

    String name = "joyway.appzgate.com.joyway";

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + name);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {

            Toast.makeText(getActivity(), " Not found in playstore ", Toast.LENGTH_SHORT).show();

        }
    }
}
