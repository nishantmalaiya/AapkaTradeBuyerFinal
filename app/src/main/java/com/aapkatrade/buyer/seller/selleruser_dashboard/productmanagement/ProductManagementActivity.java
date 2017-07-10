package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopListAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopManagementActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ProductManagementActivity extends AppCompatActivity {
    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private String userId;
    private TextView listFooterName;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewCompanyShop;
    private CompanyShopListAdapter companyShopListAdapter;
    private LinkedList<CompanyShopData> companyShopLinkedList = new LinkedList<>();
    private int page = 0;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_shop_management);
        context = ProductManagementActivity.this;
        setUpToolBar();
        initView();
        callCompanyShopListWebService(page);






        recyclerViewCompanyShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        page = page + 1;
                        callCompanyShopListWebService(++page);
                    }
                }
            }

        });
    }

    private void doCircularReveal(final View view) {

        // get the center for the clipping circle
//        int centerX = (view.getLeft() + view.getRight()) / 2;
//        int centerY = (view.getTop() + view.getBottom()) / 2;
        int centerX = view.getRight();
        int centerY = view.getBottom();

        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view,
                    centerX, centerY, startRadius, endRadius);
        }
        if (anim != null) {
            anim.setDuration(1000);
            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Intent bankDetails = new Intent(context, AddCompanyShopActivity.class);
                    bankDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(bankDetails);
                }
            });
           /* view.animate().alpha(1.0f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                }
            });*/
            anim.start();
        }
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
                                            CompanyShopData companyShopData = new CompanyShopData(jsonObject.get("companyId").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("address").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("created").getAsString(), jsonObject.get("product_count").getAsString(),"");
                                            if(!companyShopLinkedList.contains(companyShopData)){
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
                                AndroidUtils.showErrorLog(context, "error::::::TRUE");
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, "result::::::NULL");
                        }
                    }
                });

    }

    private void initView() {
      findViewById(R.id.btnAdd_shop).setVisibility(View.GONE);
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        linearLayoutManager = new LinearLayoutManager(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        listFooterName = (TextView) findViewById(R.id.listfootername);
        recyclerViewCompanyShop = (RecyclerView) findViewById(R.id.recyclerview);
        listFooterName.setText(R.string.title_activity_product_management);
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
