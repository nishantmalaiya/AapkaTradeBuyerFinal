package com.aapkatrade.buyer.user_dashboard.address.viewpager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.Home.cart.CartData;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import com.aapkatrade.buyer.payment.PaymentActivity;
import com.aapkatrade.buyer.user_dashboard.address.add_address.AddAddressActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class CartCheckoutActivity extends AppCompatActivity {

    RelativeLayout relativePayment;
    AppSharedPreference app_sharedpreference;
    ArrayList<CartData> cartDataArrayList = new ArrayList<>();
    private Context context;
    private ImageView locationImageView;
    public static TextView tvContinue, tvPriceItemsHeading, tvPriceItems, tvLastPayableAmount, tvAmountPayable,addressPhone;
    RelativeLayout buttonContainer;
    RecyclerView mycartRecyclerView;
    CartCheckOutAdapter cartAdapter;
    private ProgressBarHandler progressBarHandler;
    public static CardView cardviewProductDeatails;
    public static LinearLayout linearAddressLayout;
    TextView addressLine1, addressLine2, addressLine3;
    String userid;
    int page = 1;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart_checkout);

        context = CartCheckoutActivity.this;

        setuptoolbar();

        progressBarHandler = new ProgressBarHandler(context);

        app_sharedpreference = new AppSharedPreference(getApplicationContext());


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


    private void initView() {

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        linearAddressLayout = (LinearLayout) findViewById(R.id.linearAddressLayout);

        cardviewProductDeatails = (CardView) findViewById(R.id.cardviewProductDeatails);

        addressLine1 = (TextView) findViewById(R.id.addressLine1);

        addressLine2 = (TextView) findViewById(R.id.addressLine2);

        addressLine3 = (TextView) findViewById(R.id.addressLine3);

        addressPhone = (TextView) findViewById(R.id.addressPhone);


        addressLine1.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), ""));

        addressLine2.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), ""));

        addressLine3.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), "")+"-"+app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), ""));

        addressPhone.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), ""));


        locationImageView = (ImageView) findViewById(R.id.locationImageView);

        tvLastPayableAmount = (TextView) findViewById(R.id.tvLastPayableAmount);

        tvPriceItemsHeading = (TextView) findViewById(R.id.tvPriceItemsHeading);

        tvAmountPayable = (TextView) findViewById(R.id.tvAmountPayable);

        tvPriceItems = (TextView) findViewById(R.id.tvPriceItems);

        tvPriceItems.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + "4251");

        tvAmountPayable.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + "4251");

        AndroidUtils.setImageColor(locationImageView, context, R.color.green);

    }


    private void setup_layout() {

        relativePayment = (RelativeLayout) findViewById(R.id.relativePayment);

        mycartRecyclerView = (RecyclerView) findViewById(R.id.order_list);

        cartList("0");


        relativePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");
                callwebservice__save_order(userid);


            }
        });


    }


    private void setuptoolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
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

        progressBarHandler.show();

        String user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (user_id.equals("notlogin")) {
            user_id = "";
        }


        Ion.with(CartCheckoutActivity.this)
                .load(getResources().getString(R.string.webservice_base_url) + "/list_cart")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("page", pageNumber)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "-jsonObject------------" + result.toString());

                            JsonObject jsonObject = result.getAsJsonObject("result");

                            String cart_count = jsonObject.get("total_qty").getAsString();
                            String total_amount = jsonObject.get("total_amount").getAsString();

                            tvPriceItemsHeading.setText("Price (" + cart_count + " item)");
                            tvPriceItems.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + total_amount);
                            tvAmountPayable.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + total_amount);
                            // tvLastPayableAmount.setText(getApplicationContext().getResources().getText(R.string.rupay_text)+total_amount);

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
                                    cartDataArrayList.add(new CartData(Id, productName, productqty, price, productImage, product_id, subtotal_price));
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mycartRecyclerView.setLayoutManager(mLayoutManager);
                                cartAdapter = new CartCheckOutAdapter(context, cartDataArrayList);
                                mycartRecyclerView.setAdapter(cartAdapter);

                                linearAddressLayout.setVisibility(View.VISIBLE);
                                cardviewProductDeatails.setVisibility(View.VISIBLE);

                            }
                        } else {
                            AndroidUtils.showErrorLog(context, "-jsonObject------------NULL RESULT FOUND");
                        }

                    }
                });
    }


    private void callwebservice__save_order(String buyer_id)
    {

        progressBarHandler.show();
        String user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/save_order";
        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("buyer_id", buyer_id)
                .setBodyParameter("user_id",user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                        System.out.println("result-----------" + result.toString());

                        String message = result.get("message").getAsString();
                        if (message.equals("Order Saved Successfully!")) {
                            JsonObject jsonObject = result.getAsJsonObject("result");
                            String order_number = jsonObject.get("tracking_no").getAsString();
                            progressBarHandler.hide();
                            Intent payment = new Intent(CartCheckoutActivity.this, PaymentActivity.class);
                            payment.putExtra("order_number", order_number);
                            payment.putExtra("userid", userid);
                            payment.putExtra("price", tvAmountPayable.getText().toString().replace(getApplicationContext().getResources().getText(R.string.rupay_text), "").trim());
                            startActivity(payment);


                        } else {
                            progressBarHandler.hide();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

}
