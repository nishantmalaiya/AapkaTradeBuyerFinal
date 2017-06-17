package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.uicomponent.daystile.DaysTileView;

public class AddProductActivity extends AppCompatActivity {
    private DaysTileView daysTileView, daysTileView2, daysTileView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        daysTileView = (DaysTileView) findViewById(R.id.daysTileView);
        daysTileView.setBackgroundColor(R.color.green);
        daysTileView.setDayName("Mon - Fri");

        daysTileView2 = (DaysTileView) findViewById(R.id.daysTileView2);
        daysTileView2.setBackgroundColor(R.color.md_material_blue_600);
        daysTileView2.setDayName("Saturday");

        daysTileView3 = (DaysTileView) findViewById(R.id.daysTileView3);
        daysTileView3.setBackgroundColor(R.color.red);
        daysTileView3.setDayName("Sunday");


        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showToast(AddProductActivity.this, daysTileView.getOpeningTime()+"   "+daysTileView.getClosingTime());
                AndroidUtils.showToast(AddProductActivity.this, daysTileView2.getOpeningTime()+"   "+daysTileView2.getClosingTime());

            }
        });



    }

}
