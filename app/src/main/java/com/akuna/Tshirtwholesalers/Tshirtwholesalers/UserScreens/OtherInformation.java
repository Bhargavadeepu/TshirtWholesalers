package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class OtherInformation extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

/*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //  onBackPressed();
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void txt_abtus(View view) {



        intentCall("AboutUs");
    }

    public void txt_question(View view) {

        intentCall("Question");
    }

    public void txt_embroidery(View view) {
        intentCall("embroidery");
    }

    public void txt_screenprint(View view) {
        intentCall("screenprints");
    }

    public void txt_heattransfer(View view) {
        intentCall("heattransfer");
    }

    public void txt_ourcustomer(View view) {
        intentCall("ourcustomer");
    }

    public void txt_shippingpolicy(View view) {
        intentCall("shippingpolicy");
    }

    public void txt_refundpolicy(View view) {
        intentCall("refundpolicy");
    }

    public void txt_contactus(View view) {
        intentCall("contactus");
    }

    public void txt_reviews(View view) {
        intentCall("reviews");
    }

    public void txt_blogs(View view) {
        intentCall("blogs");
    }

    public void txt_privacypolicy(View view) {
        intentCall("privacypolicy");
    }

    public void txt_termsandservice(View view) {
        intentCall("termsandservices");
    }

    public void txt_login(View view) {
        intentCall("login");
    }

    public void txt_rateus(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "I love using Uniform Wholesalers, it's simple and incredible. You should try it here :https://www.uniformwholesalers.com.au/";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing via"));
    }

    private void intentCall(String pageInfo) {
        String data = pageInfo;
        Intent webIntent = new Intent(OtherInformation.this, WebviewActivity.class);
        webIntent.putExtra("PageInfo", data);
        startActivity(webIntent);
    }

    public void txt_shareapp(View view) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "I love using Uniform Wholesalers, it's simple and incredible. You should try it here :https://www.uniformwholesalers.com.au/";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Sharing via"));
    }
*/

}
