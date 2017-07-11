package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.sellerproduct;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;

public class SellerProductListActivity extends AppCompatActivity
{

    private ArrayList<SellerProductData> orderListDatas = new ArrayList<>();
    private RecyclerView order_list;
    private SellerProductAdapter sellerProductAdapter;
    private ProgressBarHandler progress_handler;
    private AppSharedPreference appSharedPreference;
    private String user_id;
    private Context context;
    String UserType;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seller_product_list);

        context = SellerProductListActivity.this;

        setuptoolbar();

        progress_handler = new ProgressBarHandler(context);

        appSharedPreference = new AppSharedPreference(context);

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        setup_layout();

       // get_web_data();

    }

    private void setup_layout()
    {
        orderListDatas.add(new SellerProductData("","","","","","",""));
        orderListDatas.add(new SellerProductData("","","","","","",""));
        orderListDatas.add(new SellerProductData("","","","","","",""));
        orderListDatas.add(new SellerProductData("","","","","","",""));
        orderListDatas.add(new SellerProductData("","","","","","",""));

        order_list = (RecyclerView) findViewById(R.id.recyclerview);

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        order_list.setLayoutManager(linearLayoutManager);

        sellerProductAdapter = new SellerProductAdapter(getApplicationContext(), orderListDatas);

        order_list.setAdapter(sellerProductAdapter);

    }

    private void setuptoolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        appSharedPreference = new AppSharedPreference(context);
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


    private void get_web_data()
    {
        orderListDatas.clear();
        progress_handler.show();

        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url) + "/buyer_order_details")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("buyer_id", appSharedPreference.getSharedPref("userid", user_id))
                .setBodyParameter("type", UserType)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "order_list_response", result.toString());

                        if (result == null)
                        {
                            progress_handler.hide();
                            //layout_container.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {
                                JsonObject jsonObject_result = result.getAsJsonObject("result");


                                System.out.println("message_data==================" + result.get("message").getAsString());

                                progress_handler.hide();

                            }
                        }


                    }
                });

    }

}
