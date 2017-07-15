package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement;

import android.content.Context;
import android.content.Intent;

import android.os.Build;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;
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

public class ProductManagementActivity extends AppCompatActivity
{

    private ArrayList<ProductListData> orderListDatas = new ArrayList<>();
    private RecyclerView order_list;
    private ProductListAdapter productListAdapter;
    private ProgressBarHandler progress_handler;
    private AppSharedPreference appSharedPreference;
    private String user_id;
    private Context context;
    String UserType;
    private LinearLayoutManager linearLayoutManager;
    ImageView img_shop_type;
    int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seller_product_list);

        context = ProductManagementActivity.this;

        initview();

        setUpToolBar();

        progress_handler = new ProgressBarHandler(context);

        appSharedPreference = new AppSharedPreference(context);

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        setup_layout();

        get_web_data(++page);

    }

    private void initview() {

        img_shop_type=(ImageView)findViewById(R.id.btnAdd_shop);

        order_list = (RecyclerView) findViewById(R.id.recyclerview);

        onClickEvents();



    }

    private void onClickEvents() {
        img_shop_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Intent addProductIntent=new Intent(context, AddProductActivity.class);
                startActivity(addProductIntent);
            }
        });



       order_list. addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView view, int scrollState) {

                super.onScrollStateChanged(order_list, scrollState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();

                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {

                        page = page + 1;

                        get_web_data(page);
                    } else {
                        //loadingProgress.setVisibility(View.GONE);
                    }

                }


                Intent addProductIntent=new Intent(context, AddProductActivity.class);
                startActivity(addProductIntent);

            }

        });
    }

    private void setup_layout()
    {


        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        order_list.setLayoutManager(linearLayoutManager);

        productListAdapter = new ProductListAdapter(ProductManagementActivity.this, orderListDatas);

        order_list.setAdapter(productListAdapter);





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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    private void get_web_data(int i)
    {
        orderListDatas.clear();
        progress_handler.show();

        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url) + "/product_list")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", appSharedPreference.getSharedPref("userid", user_id))
                .setBodyParameter("page",""+ i)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "order_list_response", result.toString());

                        if (result == null)
                        {
                            progress_handler.hide();
                        }
                        else
                        {
                            String error = result.get("error").getAsString();
                            if (error.contains("false"))
                            {
                                JsonArray json_result = result.getAsJsonArray("result");

                                for (int i=0; i<json_result.size(); i++)
                                {
                                    JsonObject jsonObject = (JsonObject) json_result.get(i);
                                    String product_id= jsonObject.get("id").getAsString();
                                    String product_name = jsonObject.get("name").getAsString();
                                    String product_image = jsonObject.get("image_url").getAsString();
                                    String category_name = jsonObject.get("category_name").getAsString();
                                    String State_name = jsonObject.get("state_name").getAsString();
                                    String shop_name = jsonObject.get("company_name").getAsString();
                                    String product_status = jsonObject.get("status").getAsString();

                                    orderListDatas.add(new ProductListData(product_id,product_name,product_image,category_name,State_name,shop_name,product_status));
                                }
                                productListAdapter.notifyDataSetChanged();
                                progress_handler.hide();


                            }
                        }


                    }
                });






    }

}
