package com.aapkatrade.buyer.user_dashboard.order_list.order_details;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.CancelOrderDialog;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.order_list.OrderDetailAdapter;
import com.aapkatrade.buyer.user_dashboard.order_list.OrderDetailData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class OrderDetailsActivity extends AppCompatActivity {

    String userId, OrderId;
    RecyclerView recycle_view_order_list;
    Context context;
    LinearLayoutManager linearLayoutManager;
    OrderDetailAdapter orderDetailAdapter;
    ProgressBarHandler progressBarHandler;
    ArrayList<OrderDetailData> orderDetailDatas;
    public static CommonInterface commonInterface;
Button cancelOrder;

    TextView tvOrderId, TvOrderDate, OrderStatus, tvpincodetv, tvOrderAddress, tvEmail, tvPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_details);
        context = this;
        setuptoolbar();
        initView();
        userId = getIntent().getExtras().getString("userId");
        OrderId = getIntent().getExtras().getString("OrderId");
        call_order_detail_webservice(OrderId);

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();


                CancelOrderDialog cancel_order_dialog = new CancelOrderDialog(OrderId,-1);
                cancel_order_dialog.show(fm, "Track Order");


            }
        });




        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
if(Integer.parseInt(object.toString())!=-1) {
    orderDetailDatas.remove(Integer.parseInt(object.toString()));
    if (orderDetailDatas.size() > 0) {

        orderDetailAdapter.notifyDataSetChanged();
    } else {
        finish();
    }
}
else {
    finish();

}

                return null;
            }
        };


    }

    private void initView() {
        progressBarHandler = new ProgressBarHandler(this);
        recycle_view_order_list = (RecyclerView) findViewById(R.id.recycle_view_order_list);
        orderDetailDatas = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycle_view_order_list.setLayoutManager(mLayoutManager);

        tvOrderId = (TextView) findViewById(R.id.tvOrderId);
        TvOrderDate = (TextView) findViewById(R.id.TvOrderDate);
        OrderStatus = (TextView) findViewById(R.id.OrderStatus);
        tvpincodetv = (TextView) findViewById(R.id.tvpincode);
        tvOrderAddress = (TextView) findViewById(R.id.tvOrderAddress);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);

        cancelOrder=(Button)findViewById(R.id.cancelOrder);
    }


    private void setuptoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        //getSupportActionBar().setIcon(R.drawable.home_logo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.user, menu);
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

    private void call_order_detail_webservice(String orderId) {
        progressBarHandler.show();
        Ion.with(context)
                .load(context.getResources().getString(R.string.webservice_base_url) + "/buyer_order_details")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userId)
                .setBodyParameter("ORDER_ID", orderId)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result == null) {


                        } else {


                            if (result.get("error").getAsString().contains("false")) {


                                AndroidUtils.showErrorLog(context, "orderDetailData", result.toString());


                                JsonObject jsonObject = result.getAsJsonObject();


                                JsonObject jsonObject1 = jsonObject.getAsJsonObject("result");


                                JsonObject jsonObject_order = jsonObject1.getAsJsonObject("order");

                                String orderid = jsonObject_order.get("ORDER_ID").getAsString();
                                String email = jsonObject_order.get("email").getAsString();
                                String phone = jsonObject_order.get("phone").getAsString();
                                String pincode = jsonObject_order.get("pincode").getAsString();
                                String status = jsonObject_order.get("status").getAsString();
                                String OrderAddress = jsonObject_order.get("address").getAsString();

                                String created_at = jsonObject_order.get("created_at").getAsString();


                                TvOrderDate.setText(created_at);
                                OrderStatus.setText(status);
                                tvOrderAddress.setText(OrderAddress);
                                tvEmail.setText(email);
                                tvPhoneNo.setText(phone);
                                tvpincodetv.setText(pincode);


                                JsonArray list = jsonObject1.getAsJsonArray("detail");


                                for (int i = 0; i < list.size(); i++) {
                                    JsonObject jsonObject2 = (JsonObject) list.get(i);
                                    String product_name = jsonObject2.get("product_name").getAsString();
                                    String subOrderId = jsonObject2.get("id").getAsString();


                                    String product_price = jsonObject2.get("product_price").getAsString();
                                    String DateTime = jsonObject2.get("created_at").getAsString();


                                    String product_qty = jsonObject2.get("product_qty").getAsString();
                                    String product_image = jsonObject2.get("image_url").getAsString();

                                    String OrderStatus = jsonObject2.get("status").getAsString();
                                    String discount = jsonObject2.get("discount").getAsString() + "%";


                                    orderDetailDatas.add(new OrderDetailData(subOrderId, product_image, product_name, product_price, product_qty, DateTime, discount));


                                }


                                orderDetailAdapter = new OrderDetailAdapter(context, orderDetailDatas);
                                recycle_view_order_list.setAdapter(orderDetailAdapter);
                                orderDetailAdapter.notifyDataSetChanged();
                                progressBarHandler.hide();
                            }
                        }
                    }

                });
    }
}
