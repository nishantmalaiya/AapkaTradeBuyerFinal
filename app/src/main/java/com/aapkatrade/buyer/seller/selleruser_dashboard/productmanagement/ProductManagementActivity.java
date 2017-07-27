package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.Seller_Update_Product_Policy;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.adapter.ProductListAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.entity.ProductListData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ProductManagementActivity extends AppCompatActivity {

    private ArrayList<ProductListData> productArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private ProgressBarHandler progressBarHandler;
    private AppSharedPreference appSharedPreference;
    private String userID;
    private int totalPage = 0;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private ImageView imgShopType;
    private int page = 0;
    private TextView tvHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_list);
        context = ProductManagementActivity.this;
        setUpToolBar();
        initview();
        hitProductListWebService(++page);
        onScrollEvents();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(linearLayoutManager);
        productListAdapter = new ProductListAdapter(context, productArrayList);
        recyclerView.setAdapter(productListAdapter);
    }

    private void initview() {
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        userID = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");
        AndroidUtils.showErrorLog(context, "+++++++++++++-----------USERID----------------++++++++++" + userID);
        imgShopType = (ImageView) findViewById(R.id.btnAdd_shop);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        imgShopType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(context, AddProductActivity.class);
                startActivity(addProductIntent);
            }
        });

        tvHeading = (TextView) findViewById(R.id.listfootername);
        tvHeading.setText("Product Managment");
    }

    private void onScrollEvents() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                AndroidUtils.showErrorLog(context, "++++++totalItemCount++++++" + totalItemCount, "_________lastVisibleItemCount___________" + lastVisibleItemCount);
                if (totalItemCount > 0 && totalPage > page) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        hitProductListWebService(++page);
                    }
                }
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


    private void hitProductListWebService(int page) {
        progressBarHandler.show();

        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url) + "/product_list")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userID)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, "order_list_response", result);

                        if (result != null) {
                            if (result.get("error").getAsString().contains("false")) {
                                JsonArray jsonArray = result.getAsJsonArray("result");

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                                    productArrayList.add(new ProductListData(jsonObject.get("id").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("image_url").getAsString(), jsonObject.get("category_name").getAsString(), jsonObject.get("state_name").getAsString(), jsonObject.get("company_name").getAsString(), jsonObject.get("status").getAsString()));
                                }

                                if (productListAdapter == null) {
                                    setUpRecyclerView();
                                    totalPage = result.get("total_page").getAsInt();
                                } else {
                                    productListAdapter.notifyDataSetChanged();
                                }

                            }
                        }


                    }
                });


    }


    public void fragment_call(String product_id) {
        Seller_Update_Product_Policy seller_update_product_policy = new Seller_Update_Product_Policy(product_id, context);
        FragmentManager fm = getSupportFragmentManager();
        seller_update_product_policy.show(fm, "enquiry");
    }


}
