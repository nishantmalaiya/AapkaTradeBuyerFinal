package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class CompanyShopManagementActivity extends AppCompatActivity {

    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private String userId;
    private TextView listFooterName;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewCompanyShop;
    private CompanyShopListAdapter companyShopListAdapter;
    private LinkedList<CompanyShopData> companyShopLinkedList = new LinkedList<>();
    private int page = 0, totalPage = 0;
    private LinearLayoutManager mLayoutManager;

    RelativeLayout rlSearchContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_shop_management);
        context = CompanyShopManagementActivity.this;
        setUpToolBar();
        initView();
        callCompanyShopListWebService(++page);


        ImageButton btnAddShop = findViewById(R.id.btnAdd_shop);
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bankDetails = new Intent(context, AddCompanyShopActivity.class);
                bankDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(bankDetails);
            }
        });


        recyclerViewCompanyShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0 && totalPage > page) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        callCompanyShopListWebService(++page);
                    }
                }
            }

        });
    }

    private void callCompanyShopListWebService(int page) {
        progressBarHandler.show();
        AndroidUtils.showErrorLog(context, "________________page_____________________", page);
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/shoplist")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("apply", "1")
                .setBodyParameter("seller_id", userId)
                .setBodyParameter("lat", "0")
                .setBodyParameter("long", "0")
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                totalPage = result.get("total_page").getAsInt();

                                JsonArray jsonArray = result.get("result").getAsJsonArray();
                                if (jsonArray != null && jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                        CompanyShopData companyShopData = new CompanyShopData(jsonObject.get("id").getAsString(), jsonObject.get("company_name").getAsString(), jsonObject.get("short_des").getAsString(), jsonObject.get("short_des").getAsString(), jsonObject.get("created_at").getAsString(), jsonObject.get("product_count").getAsString(), jsonObject.get("image_url").getAsString());

                                        AndroidUtils.showErrorLog(context,"company"+ jsonObject.get("company_name").getAsString()+jsonObject.get("company_id").getAsString());

                                        if (!companyShopLinkedList.contains(companyShopData)) {
                                            companyShopLinkedList.add(companyShopData);
                                        }

                                    }
                                    Collections.sort(companyShopLinkedList, new Comparator<CompanyShopData>() {
                                        @Override
                                        public int compare(CompanyShopData o1, CompanyShopData o2) {
                                            return o1.getName().compareTo(o2.getName());
                                        }
                                    });
                                    AndroidUtils.showErrorLog(context, companyShopLinkedList.size(), "**********");
                                    if (companyShopListAdapter == null) {
                                        companyShopListAdapter = new CompanyShopListAdapter(context, companyShopLinkedList);
                                        recyclerViewCompanyShop.setLayoutManager(linearLayoutManager);
                                        recyclerViewCompanyShop.setAdapter(companyShopListAdapter);
                                    } else {
                                        companyShopListAdapter.notifyDataSetChanged();
                                    }
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
        linearLayoutManager = new LinearLayoutManager(context);
        rlSearchContainer=(RelativeLayout)findViewById(R.id.rlSearchContainer);
        rlSearchContainer.setVisibility(View.GONE);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        listFooterName = (TextView) findViewById(R.id.listfootername);
        recyclerViewCompanyShop = (RecyclerView) findViewById(R.id.recyclerview);
        listFooterName.setText(R.string.title_activity_company_shop_management);
    }


    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
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
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
