package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class ThankyouActivity extends AppCompatActivity {

    TextView tv_Orderno;
    String pageData;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initViews() {

        tv_Orderno = findViewById(R.id.tv_Orderno);
        btn_home = findViewById(R.id.btn_home);
        Intent data = getIntent();
        pageData = data.getExtras().getString("OrderNo");
        tv_Orderno.setText("pageData");
        tv_Orderno.setText("" + pageData);

        //Toast.makeText(getApplicationContext(), "Order No " + pageData, Toast.LENGTH_SHORT).show();


    }

}
