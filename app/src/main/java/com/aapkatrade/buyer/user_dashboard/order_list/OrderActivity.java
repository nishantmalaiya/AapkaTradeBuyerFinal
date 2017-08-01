package com.aapkatrade.buyer.user_dashboard.order_list;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;

import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;

import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private ArrayList<OrderListData> orderListDatas = new ArrayList<>();
    private RecyclerView order_list;
    private OrderListAdapter orderListAdapter;
    private ProgressBarHandler progress_handler;
    private LinearLayout layout_container;
    private AppSharedPreference appSharedPreference;
    private String user_id;
    private Context context;

    String UserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order);
        context = OrderActivity.this;
        Log.e("hi////", "ghuygubgiugvuyuuihguogyuygukyvgbuk");

        setuptoolbar();

        progress_handler = new ProgressBarHandler(context);

        appSharedPreference = new AppSharedPreference(context);

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        setup_layout();

        get_web_data();

    }

    private void setup_layout() {
        layout_container = (LinearLayout) findViewById(R.id.layout_container);
        order_list = (RecyclerView) findViewById(R.id.order_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        order_list.setLayoutManager(mLayoutManager);
    }

    private void setuptoolbar() {
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


    private void get_web_data() {

        UserType = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), SharedPreferenceConstants.USER_TYPE_BUYER.toString());

        if (UserType.equals(SharedPreferenceConstants.USER_TYPE_BUYER.toString()))


        {
            UserType = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), SharedPreferenceConstants.USER_TYPE_BUYER.toString());

        } else if (UserType.equals(SharedPreferenceConstants.USER_TYPE_SELLER.toString()))


        {
            UserType = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), SharedPreferenceConstants.USER_TYPE_SELLER.toString());

        } else if (UserType.equals(SharedPreferenceConstants.USER_TYPE_ASSOCIATE.toString())) {

            UserType = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), SharedPreferenceConstants.USER_TYPE_ASSOCIATE.toString());
        }
        orderListDatas.clear();
        progress_handler.show();

        Log.e("hi////", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), user_id) + "GGGGGGG" + appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "1"));

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

                        //  System.out.println("jsonObject-------------" + result.toString());


                        if (result == null) {
                            progress_handler.hide();
                            layout_container.setVisibility(View.INVISIBLE);
                        } else


                        {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {
                                JsonObject jsonObject_result = result.getAsJsonObject("result");


                                System.out.println("message_data==================" + result.get("message").getAsString());


                                JsonArray orders = jsonObject_result.getAsJsonArray("orders");


                                for (int i = 0; i < orders.size(); i++) {

                                    JsonObject jsonObject2 = (JsonObject) orders.get(i);
//
                                    String order_id = jsonObject2.get("id").getAsString();

                                    String product_name = jsonObject2.get("product_name").getAsString();

                                    String product_price = jsonObject2.get("product_price").getAsString();

                                    String product_qty = jsonObject2.get("product_qty").getAsString();

                                    String address = jsonObject2.get("address").getAsString();

                                    String email = jsonObject2.get("email").getAsString();

                                    String buyersmobile = jsonObject2.get("buyersmobile").getAsString();

                                    String buyersname = jsonObject2.get("buyersname").getAsString();

                                    String company_name = jsonObject2.get("cname").getAsString();

                                    String status = jsonObject2.get("status").getAsString();

                                    String created_at = jsonObject2.get("created_at").getAsString();

                                    String product_image = jsonObject2.get("image_url").getAsString();

//                                    orderListDatas.add(new OrderListData(order_id, product_name, product_price, product_qty, address, email, buyersmobile, buyersname, company_name, status, created_at, product_image));


                                }
                                orderListAdapter = new OrderListAdapter(getApplicationContext(), orderListDatas);

                                order_list.setAdapter(orderListAdapter);

                                orderListAdapter.notifyDataSetChanged();

                                progress_handler.hide();

                                JsonArray orders_details = jsonObject_result.getAsJsonArray("orders_details");


                                for (int i = 0; i < orders_details.size(); i++) {


                                }


                            }


                        }


                    }
                });

    }

}
