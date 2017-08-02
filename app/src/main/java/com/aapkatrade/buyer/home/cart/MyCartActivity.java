package com.aapkatrade.buyer.home.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;

import com.aapkatrade.buyer.dialogs.loginwithoutregistration.LoginWithoutRegistrationDialog;
import com.aapkatrade.buyer.general.AppSharedPreference;

import com.aapkatrade.buyer.general.AppConfig;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.address.add_address.AddAddressActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class MyCartActivity extends AppCompatActivity {

    ArrayList<CartData> cartDataArrayList = new ArrayList<>();
    private Context context;
    private ImageView locationImageView;
    public static TextView tvContinue, tvPriceItemsHeading, tvPriceItems, tvLastPayableAmount, tvAmountPayable;
    RecyclerView mycartRecyclerView;
    CartAdapter cartAdapter;
    private ProgressBarHandler progressBarHandler;
    public static CardView cardviewProductDeatails, cardBottom;
    AppSharedPreference app_sharedpreference;
    private int page = 1;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_cart);

        context = MyCartActivity.this;

        setuptoolbar();

        progressBarHandler = new ProgressBarHandler(context);

        app_sharedpreference = new AppSharedPreference(MyCartActivity.this);

        initView();

        setup_layout();

        mycartRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        page = page + 1;
                        cartList(String.valueOf(page));
                    }
                }
            }
        });


    }

    private void setuptoolbar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("null");
            getSupportActionBar().setElevation(0);
        }


    }


    private void initView() {
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        cardviewProductDeatails = (CardView) findViewById(R.id.cardviewProductDeatails);

        cardBottom = (CardView) findViewById(R.id.cardBottom);

        locationImageView = (ImageView) findViewById(R.id.locationImageView);

        tvLastPayableAmount = (TextView) findViewById(R.id.tvLastPayableAmount);

        tvPriceItemsHeading = (TextView) findViewById(R.id.tvPriceItemsHeading);

        tvAmountPayable = (TextView) findViewById(R.id.tvAmountPayable);

        tvPriceItems = (TextView) findViewById(R.id.tvPriceItems);


        tvContinue = (TextView) findViewById(R.id.tvSaveButton);

        tvLastPayableAmount = (TextView) findViewById(R.id.tvLastPayableAmount);


        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                    LoginWithoutRegistrationDialog loginWithoutRegistrationDialog = new LoginWithoutRegistrationDialog(context);
                    loginWithoutRegistrationDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoginWithoutRegistrationDialog");

                } else {
                    Intent i = new Intent(MyCartActivity.this, AddAddressActivity.class);
                    startActivity(i);
                }


            }
        });

        AndroidUtils.setImageColor(locationImageView, context, R.color.green);

        /* checkDeliveryButton = (TextView) findViewById(R.id.checkDeliveryButton);

        buttonContainer = (RelativeLayout) findViewById(R.id.buttonContainer);
       AndroidUtils.setBackgroundSolid(buttonContainer, context, R.color.orange, 25, GradientDrawable.RECTANGLE);
      */


    }

    private void setup_layout() {
        mycartRecyclerView = (RecyclerView) findViewById(R.id.order_list);

        cartList("0");

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

    private void cartList(String pageNumber) {
        //cartDataArrayList.clear();
        progressBarHandler.show();

        String user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (user_id.equals("notlogin")) {
            user_id = "";
        }

        System.out.println("deveice -id----------" + user_id + "ghsdfs" + AppConfig.getCurrentDeviceId(context));

        System.out.println("cart_item------------" + app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""));

        Ion.with(MyCartActivity.this)
                .load(getResources().getString(R.string.webservice_base_url) + "/list_cart")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("page", pageNumber)
                .setBodyParameter("user_type", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "my_cart--------", result);

                        if (result != null) {

                            JsonObject jsonObject = result.getAsJsonObject("result");

                            String cart_count = jsonObject.get("total_qty").getAsString();
                            String total_amount = jsonObject.get("total_amount").getAsString();

                            tvPriceItemsHeading.setText(new StringBuilder("Price ( ").append(cart_count).append(" Items )"));
                            tvPriceItems.setText(new StringBuilder(context.getResources().getText(R.string.rupay_text)).append(" ").append(total_amount));
                            tvAmountPayable.setText(new StringBuilder(context.getString(R.string.rupay_text)).append(" ").append(total_amount));
                            tvLastPayableAmount.setText(new StringBuilder(context.getString(R.string.rupay_text)).append(" ").append(total_amount));

                            JsonArray jsonProductList = jsonObject.getAsJsonArray("items");

                            if (jsonProductList != null && jsonProductList.size() > 0) {

                                for (int i = 0; i < jsonProductList.size(); i++) {
                                    JsonObject jsonproduct = (JsonObject) jsonProductList.get(i);
                                    String Id = jsonproduct.get("id").getAsString();
                                    String productName = jsonproduct.get("name").getAsString();
                                    String productqty = jsonproduct.get("quantity").getAsString();
                                    String price = jsonproduct.get("price").getAsString();
                                    String subtotal_price = jsonproduct.get("sub_total").getAsString();

                                    System.out.println("price--------------------" + price);
                                    String productImage = jsonproduct.get("image_url").getAsString();
                                    String product_id = jsonproduct.get("product_id").getAsString();
                                    cartDataArrayList.add(new CartData(Id, productName, productqty, price, productImage, product_id, subtotal_price, "", ""));


                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mycartRecyclerView.setLayoutManager(mLayoutManager);
                                cartAdapter = new CartAdapter(context, cartDataArrayList);
                                mycartRecyclerView.setAdapter(cartAdapter);
                                cardviewProductDeatails.setVisibility(View.VISIBLE);
                                cardBottom.setVisibility(View.VISIBLE);
                                progressBarHandler.hide();

                            } else {

                                progressBarHandler.hide();

                                AndroidUtils.showErrorLog(context, "-jsonObject------------NULL RESULT FOUND");
                            }

                        }
                    }
                });
    }
}




