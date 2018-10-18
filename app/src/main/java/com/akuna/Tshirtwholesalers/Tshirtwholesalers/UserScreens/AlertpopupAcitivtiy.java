package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.app.ListActivity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.akuna.Tshirtwholesalers.Tshirtwholesalers.Util;

public class AlertpopupAcitivtiy extends ListActivity {

    // TextView tv_highprice,tv_lowprice,tv_title;

    //ListView listvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertpopup_acitivtiy);

        initViews();

        String[] names = new String[]{"Price High-Low", "Price Low - High", "New Products"};
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1, names));
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemPos = position;

                finish();
                Util.count = 2;
                // int pos = Integer.parseInt(itemPos);
                Util.itempos = itemPos;
            }
        });



    }

    private void initViews() {


    }
}
