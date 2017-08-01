package com.aapkatrade.buyer.dialogs.track_order.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
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
import android.widget.TextView;

import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.dialogs.track_order.CommonHolderOrderListAdapter;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.shopdetail.ShopViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class Order_detail extends AppCompatActivity {
    Context context;

    RecyclerView recyclerProductList;
    private ArrayList<String> imageUrlArrayList = new ArrayList<>();
    private String class_name, json_response;
    ArrayList<CommonData> commonDatas_products = new ArrayList<>();
    ViewPager view_pager_products;
    private ShopViewPagerAdapter viewPagerAdapter;
    ArrayList<CommomDataTrackingList> commomDatas_order = new ArrayList<>();
    private CircleIndicator circleIndicator;
    ArrayList<OrderDetailsDatas> orderDetailsDatas = new ArrayList<>();
    ArrayList<ProductDatas> productDatasArrayList = new ArrayList<>();
    private CommonHolderOrderListAdapter commomAdapter_latestpost;
    LinearLayoutManager llmanager_productlist;
    String result;

    TextView tvOrderid, tvOrderDate, tvAmountPaid, tvAddress, tvMobile;

    String orderId = "", OrderDate = "", AmoutPaid = "", Address = "", PhoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        context = this;
        class_name = getIntent().getStringExtra("class_name");
        result = getIntent().getStringExtra("result");

        initview();


        setUpToolBar();


        String_to_json_conversion(result);

    }

    private void initview() {

        llmanager_productlist = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        recyclerProductList = (RecyclerView) findViewById(R.id.recycleviewProductList);
        recyclerProductList.setLayoutManager(llmanager_productlist);
        tvOrderid = (TextView) findViewById(R.id.tvOrderId);

        tvAddress = (TextView) findViewById(R.id.tvAddress);

        tvAmountPaid = (TextView) findViewById(R.id.tvAmountPaid);

        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);

        tvMobile = (TextView) findViewById(R.id.tvMobile);


    }

    private void String_to_json_conversion(String server_response_string) {
        try {
            JSONObject server_result = new JSONObject(server_response_string);
            String error = server_result.getString("error");
            if (error.contains("false")) {

                JSONObject result = server_result.getJSONObject("result");

                ///// order Details

                orderId = result.get("ORDER_ID").toString();


                String name = result.get("name").toString();
                String email = result.get("email").toString();
                PhoneNumber = result.get("phone").toString();
                String pincode = result.get("pincode").toString();
                Address = result.get("address").toString();
                String city = result.get("city").toString();
                String state = result.get("state").toString();
                String product_qty = result.get("product_qty").toString();
                AmoutPaid = result.get("product_price").toString();
                String vpc_AuthorizeId = result.get("vpc_AuthorizeId").toString();
                String vpc_BatchNo = result.get("vpc_BatchNo").toString();
                String vpc_Card = result.get("vpc_Card").toString();
                String vpc_TransactionNo = result.get("vpc_TransactionNo").toString();
                OrderDate = result.get("payment_datetime").toString();

                //  set Text


                tvOrderDate.setText(OrderDate);

                tvOrderid.setText(orderId);

                tvAmountPaid.setText(AmoutPaid);

                tvAddress.setText(Address);

                tvMobile.setText(PhoneNumber);

                orderDetailsDatas.add(new OrderDetailsDatas(orderId, name, email, PhoneNumber, pincode, Address, city, state, product_qty, AmoutPaid, vpc_AuthorizeId, vpc_BatchNo, vpc_Card, vpc_TransactionNo, OrderDate));

                //  Products Details

                JSONArray order_detalis = result.getJSONArray("orders_details");

                for (int i = 0; i < order_detalis.length(); i++) {

                    JSONObject jsonObject_product = (JSONObject) order_detalis.get(i);
                    String product_name = jsonObject_product.get("product_name").toString();
                    String product_qty_order = jsonObject_product.get("product_qty").toString();
                    String product_price_order = jsonObject_product.get("product_price").toString();
                    String product_net_price = jsonObject_product.get("product_net_price").toString();
                    String discount = jsonObject_product.get("discount").toString();
                    String image_url = jsonObject_product.get("image_url").toString();
                    String product_id = jsonObject_product.get("product_id").toString();
                    imageUrlArrayList.add(image_url);


                    commomDatas_order.add(new CommomDataTrackingList(image_url, product_price_order,"", product_id,product_name,
                            Address,  product_net_price

                            ));




                    commomAdapter_latestpost = new CommonHolderOrderListAdapter(context, commomDatas_order, "OrderedProductList", "latestdeals");
                    recyclerProductList.setAdapter(commomAdapter_latestpost);
                    recyclerProductList.setNestedScrollingEnabled(false);

                    productDatasArrayList.add(new ProductDatas(product_name, product_qty_order, product_price_order, product_net_price, discount, image_url));


                }


                AndroidUtils.showErrorLog(context, "order info", orderId + "*****" + name);


            }


        } catch (JSONException e) {
            AndroidUtils.showErrorLog(context, e.toString());

        }
    }


    private void setUpToolBar() {
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


}
