package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class ProgrssbarActivity extends AppCompatActivity {

    public static ProgrssbarActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progrssbar);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        instance = this;
    }
}
