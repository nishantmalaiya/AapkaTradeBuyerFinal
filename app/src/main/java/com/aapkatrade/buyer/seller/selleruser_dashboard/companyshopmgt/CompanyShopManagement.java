package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.LinkedList;

public class CompanyShopManagement extends AppCompatActivity {
    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private String userId;
    private TextView listFooterName;
    private RecyclerView recyclerViewCompanyShop;
    private LinkedList<CompanyShopData> companyShopLinkedList = new LinkedList<>();
    private int page = 0;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_shop_management);
        context = CompanyShopManagement.this;
        setUpToolBar();
        initView();
        callCompanyShopListWebService(page);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.color_voilet));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void callCompanyShopListWebService(int page) {

        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/listCompany")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userId)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                if (result.get("message").getAsString().toLowerCase().contains("success")) {
                                    JsonArray jsonArray = result.get("result").getAsJsonArray();
                                    if (jsonArray != null && jsonArray.size() > 0) {
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            companyShopLinkedList.add(new CompanyShopData(jsonObject.get("companyId").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("address").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("created").getAsString()));
                                        }
                                        AndroidUtils.showErrorLog(context, companyShopLinkedList.size(),"**********");
                                        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(context);
                                        recyclerViewCompanyShop.setLayoutManager(myLayoutManager);
                                        recyclerViewCompanyShop.setAdapter(new CompanyShopListAdapter(context, companyShopLinkedList));
                                    }

                                } else {
                                    AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                }

                            } else {
                                AndroidUtils.showErrorLog(context, "error::::::TRUE");
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, "result::::::NULL");
                        }
                    }
                });

    }

    private void initView() {
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        listFooterName = (TextView) findViewById(R.id.listfootername);
        recyclerViewCompanyShop = (RecyclerView) findViewById(R.id.recyclerview);
        listFooterName.setText(R.string.title_activity_company_shop_management);
    }


    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
