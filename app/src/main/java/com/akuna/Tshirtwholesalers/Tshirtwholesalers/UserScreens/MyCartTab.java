package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.LoginActivity;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;

public class MyCartTab extends Fragment {

    Button btn_placeOrder;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mycart, container, false);
        initViews();


        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.putExtra("pageInfo", "myCart");
                startActivity(login);

            }
        });

        return rootView;
    }


    public void initViews() {

        btn_placeOrder = rootView.findViewById(R.id.btn_placeOrder);

    }

 /*   public void btn_placeorder(View view) {



    }*/

}
