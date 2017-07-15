package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.entity.ProductListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.adapter.ServiceListAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.addService.AddServiceActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.entity.ServiceListData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ServiceManagementActivity extends AppCompatActivity {

    private ArrayList<ServiceListData> serviceListDatas = new ArrayList<>();
    private RecyclerView service_list;
    private ServiceListAdapter ServiceListAdapter;
    private ProgressBarHandler progress_handler;
    private AppSharedPreference appSharedPreference;
    private String user_id;
    private Context context;
    String UserType;
    private LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    ImageView btnAdd_service;
    int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seller_service_list);

        context = ServiceManagementActivity.this;

        setUpToolBar();

        progress_handler = new ProgressBarHandler(context);

        appSharedPreference = new AppSharedPreference(context);

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        setup_layout();

        get_web_data(++page);

        OnClickEvent();

    }

    private void OnClickEvent() {


        btnAdd_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceManagementActivity.this, AddServiceActivity.class);

                startActivity(i);
            }
        });


        service_list. addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView view, int scrollState) {

                super.onScrollStateChanged(service_list, scrollState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = gridLayoutManager.getItemCount();

                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                int lastVisibleItemCount = gridLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {

                        page = page + 1;

                        get_web_data(page);
                    } else {
                        //loadingProgress.setVisibility(View.GONE);
                    }

                }

            }

        });
    }


    private void setup_layout() {
        service_list = (RecyclerView) findViewById(R.id.recyclerview);
        btnAdd_service = (ImageView) findViewById(R.id.btnAdd_service);
        if (Tabletsize.isTablet(context)) {
            gridLayoutManager = new GridLayoutManager(context, 3);
        } else {

            gridLayoutManager = new GridLayoutManager(context, 2);
        }


        service_list.setLayoutManager(gridLayoutManager);

        ServiceListAdapter = new ServiceListAdapter(context, serviceListDatas);

        service_list.setAdapter(ServiceListAdapter);
        btnAdd_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(ServiceManagementActivity.this,AddServiceActivity.class);
                startActivity(i);



            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    private void get_web_data(int i) {
        serviceListDatas.clear();
        progress_handler.show();

        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url) + "/service_list")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", appSharedPreference.getSharedPref("userid", user_id))
                .setBodyParameter("page",""+i)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "order_list_response", result.toString());

                        if (result == null) {
                            progress_handler.hide();
                        } else {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {
                                JsonArray json_result = result.getAsJsonArray("result");

                                for (int i = 0; i < json_result.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) json_result.get(i);
                                    String service_id = jsonObject.get("service_id").getAsString();
                                    String service_name = jsonObject.get("name").getAsString();
                                    String service_image = jsonObject.get("image_url").getAsString();
                                    String service_category_name = jsonObject.get("cat_name").getAsString();

                                    String shop_name = jsonObject.get("company_name").getAsString();


                                    serviceListDatas.add(new ServiceListData(service_id,service_name,service_image,service_category_name,shop_name));
                                }
                                ServiceListAdapter.notifyDataSetChanged();
                                progress_handler.hide();


                            }
                        }


                    }
                });

    }

}
