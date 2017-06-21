package com.aapkatrade.buyer.categories_tab.particular_data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.CategoriesListAdapter;
import com.aapkatrade.buyer.categories_tab.CategoriesListData;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.general.recycleview_custom.MyRecyclerViewEffect;
import com.aapkatrade.buyer.location.Mylocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class ParticularActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    CategoriesListAdapter categoriesListAdapter;
    ArrayList<CategoriesListData> shopListDatas = new ArrayList<>();
    ProgressBarHandler progress_handler;
    FrameLayout layout_container, layout_container_relativeSearch;
    MyRecyclerViewEffect myRecyclerViewEffect;
    JsonObject home_data;
    String url,latitude = "0.0", longitude = "0.0";
    Mylocation mylocation;
    private Context context;
    TextView listfootername,tv_list_quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = ParticularActivity.this;


        Intent intent = getIntent();

        Bundle b = intent.getExtras();
        if (b != null)
        {
            url = intent.getStringExtra("url");
            latitude = b.getString("latitude");
            longitude = b.getString("longitude");
            AndroidUtils.showErrorLog(context, "_______________latitude"+latitude+"longitude"+longitude);
        }

       /* Intent intent = getIntent();

        url = intent.getStringExtra("url");
      */



        System.out.println("url---------------" + url);
        setContentView(R.layout.activity_shop_list_by_category);

        setUpToolBar();

        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);

        progress_handler = new ProgressBarHandler(this);

        layout_container = (FrameLayout) view.findViewById(R.id.layout_container);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listfootername=(TextView)view.findViewById(R.id.listfootername);
        tv_list_quantity=(TextView)view.findViewById(R.id.tv_list_quantity);
        mRecyclerView.setHasFixedSize(true);



        get_web_data();

    }

    private void get_web_data() {
        shopListDatas.clear();
        progress_handler.show();

        Ion.with(ParticularActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("lat", latitude)
                .setBodyParameter("long", longitude)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        System.out.println("result  result==================" + result);

                        if (result == null) {
                            progress_handler.hide();
                            layout_container.setVisibility(View.INVISIBLE);
                        } else {
                            JsonObject jsonObject = result.getAsJsonObject();

                            String message = jsonObject.get("message").toString().substring(0, jsonObject.get("message").toString().length());

                            String message_data = message.replace("\"", "");

                            System.out.println("message_data==================" + message_data);

                            if (message_data.equals("No record found")) {

                                progress_handler.hide();
                                layout_container.setVisibility(View.INVISIBLE);

                            } else {
                                JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                                tv_list_quantity.setText(jsonArray.size()+"");

                                for (int i = 0; i < jsonArray.size(); i++)
                                {
                                    JsonObject jsonObject2 = (JsonObject) jsonArray.get(i);

                                    String shopId = jsonObject2.get("id").getAsString();

                                    String shopName = jsonObject2.get("prodname").getAsString();

                                    //  String shopPrice = jsonObject2.get("price").getAsString();

                                    //String product_cross_price = jsonObject2.get("cross_price").getAsString();

                                    String shopLocation = jsonObject2.get("city_name").getAsString() + "," + jsonObject2.get("state_name").getAsString() + "," +
                                            jsonObject2.get("country_name").getAsString();
                                    String shopImage = jsonObject2.get("imageUrl").getAsString();

                                    String distance = jsonObject2.get("distance").getAsString();

                                    String category_name = jsonObject2.get("category_name").getAsString();

                                    System.out.println("distance-------------"+distance);

                                    shopListDatas.add(new CategoriesListData(shopId, shopName, shopImage, shopLocation, distance,category_name));

                                }
                                categoriesListAdapter = new CategoriesListAdapter(ParticularActivity.this, shopListDatas);
                                myRecyclerViewEffect = new MyRecyclerViewEffect(ParticularActivity.this);
                                mRecyclerView.setAdapter(categoriesListAdapter);
                                listfootername.setText(shopListDatas.get(0).shopCategory);

                            }
////                                categoriesListAdapter = new BillPaymentAdapter(ParticularActivity.this, productListDatas);
////                                myRecyclerViewEffect = new MyRecyclerViewEffect(ParticularActivity.this);
////                                mRecyclerView.setAdapter(categoriesListAdapter);
//
//                                categoriesListAdapter.notifyDataSetChanged();

                            progress_handler.hide();
                        }

                    }


                });

    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
