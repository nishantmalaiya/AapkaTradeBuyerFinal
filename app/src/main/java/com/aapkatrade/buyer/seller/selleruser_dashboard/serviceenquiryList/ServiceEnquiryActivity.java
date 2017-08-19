package com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class ServiceEnquiryActivity extends AppCompatActivity  {

     public RecyclerView recyclerViewcompanylist;
    ServiceEnquiryAdapter serviceEnquiryAdapter;
    ArrayList<ServiceEnquiryData> serviceEnquiryDatas = new ArrayList<>();
    RelativeLayout relativeCompanylist;
    AppSharedPreference app_sharedpreference;
    String user_id;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    TextView tvTitle;
    LinearLayoutManager mLayoutManager;
    int page = 0;
    SwipeRefreshLayout mSwipyRefreshLayout;
    private Context context;

    private int totalpage;


    public  Boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_enquiry);
        context = ServiceEnquiryActivity.this;
        setUpToolBar();

        app_sharedpreference = new AppSharedPreference(getApplicationContext());

        user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        setup_layout();
    }

    private void setUpToolBar()
    {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView header_name = (TextView) findViewById(R.id.header_name);
        //header_name.setVisibility(View.VISIBLE);
        header_name.setText(getResources().getString(R.string.service_enquiry_list_heading));
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


    private void setup_layout()
    {

        mSwipyRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
       // mSwipyRefreshLayout.setEnabled(false);


        mSwipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                        page = 0;
                        get_company_list_data(page);

                    }
                }, 3000);
            }
        });

        relativeCompanylist = (RelativeLayout) findViewById(R.id.relativeCompanylist);

        recyclerViewcompanylist = (RecyclerView) findViewById(R.id.companylist);

        mLayoutManager = new LinearLayoutManager(context);

        recyclerViewcompanylist.setLayoutManager(mLayoutManager);


        get_company_list_data(page);


        serviceEnquiryAdapter = new ServiceEnquiryAdapter(ServiceEnquiryActivity.this, serviceEnquiryDatas, ServiceEnquiryActivity.this);

        recyclerViewcompanylist.setAdapter(serviceEnquiryAdapter);


        app_sharedpreference = new AppSharedPreference(ServiceEnquiryActivity.this);

        recyclerViewcompanylist.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = dy - ydy;
                ydy = dy;
                boolean shouldRefresh = (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                        && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) && offset > 30;
                if (shouldRefresh) {
                    mSwipyRefreshLayout.setRefreshing(true);
                    //Refresh to load data here.
                    AndroidUtils.showErrorLog(context,"show refresh data");
                    page = 0;
                    get_company_list_data(page);
                    return;
                }
                boolean shouldPullUpRefresh = mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager.getChildCount() - 1
                        && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING && offset < -30;
                if (shouldPullUpRefresh) {
                    mSwipyRefreshLayout.setRefreshing(true);
                    //refresh to load data here.
                    AndroidUtils.showErrorLog(context,"show refresh data id enable");
                    get_service_list_data(page);
                    return;
                }
                mSwipyRefreshLayout.setRefreshing(false);


            }
        });




            }

        });
     */




    }



    public void get_company_list_data(int loadpage)
    {
        isLoading =true;

        mSwipyRefreshLayout.setRefreshing(true);
        relativeCompanylist.setVisibility(View.INVISIBLE);

        serviceEnquiryDatas.clear();
        Ion.with(ServiceEnquiryActivity.this)
                .load(getResources().getString(R.string.webservice_base_url) + "/enquiry_service_list")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "company")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "0"))

                .setBodyParameter("page", String.valueOf(loadpage))

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result == null) {
                            mSwipyRefreshLayout.setRefreshing(false);
                            isLoading =false;
                        } else {
                            Log.e("data===============", result.toString());

                            JsonObject jsonObject = result.getAsJsonObject();
                        totalpage=result.get("total_page").getAsInt();
                            JsonArray jsonArray = jsonObject.getAsJsonArray("result");

                            if (jsonArray.size() != 0) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObject2 = (JsonObject) jsonArray.get(i);

                                    String service_enquiry_id = jsonObject2.get("id").getAsString();

                                    String product_name = jsonObject2.get("product_name").getAsString();

                                    String product_price = jsonObject2.get("price").getAsString();

                                    String user_name = jsonObject2.get("name").getAsString();

                                    String user_email = jsonObject2.get("email").getAsString();

                                    String user_mobile = jsonObject2.get("mobile").getAsString();

                                    String description = jsonObject2.get("short_des").getAsString();

                                    String created_date = jsonObject2.get("created_at").getAsString();

                                    String category_name = jsonObject2.get("category_name").getAsString();

                                    serviceEnquiryDatas.add(new ServiceEnquiryData(service_enquiry_id, product_name, product_price, user_name, user_email, user_mobile, description, created_date, category_name));

                                }
                                isLoading = false;
                                serviceEnquiryAdapter.notifyDataSetChanged();

                                // progressView.setVisibility(View.INVISIBLE);
                                relativeCompanylist.setVisibility(View.VISIBLE);

                               mSwipyRefreshLayout.setRefreshing(false);

                            }
                            else
                            {
                                mSwipyRefreshLayout.setRefreshing(false);
                                AndroidUtils.showToast(context, "No Service Enquiry  Found in Your Account");
                            }
                        }

                    }

                });
    }






    @Override
    public void onRefresh() {
        page=1;
        get_company_list_data(page+"");
    }

    public void get_service_list_data(int page)
    {
        isLoading =true;
        Ion.with(ServiceEnquiryActivity.this)

                .load(getResources().getString(R.string.webservice_base_url) + "/enquiry_service_list")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "company")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "0"))
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result == null) {

                            isLoading = false;
                            // progress_handler.hide();
                        } else {
                            Log.e("data===============", result.toString());

                            JsonObject jsonObject = result.getAsJsonObject();

                            JsonArray jsonArray = jsonObject.getAsJsonArray("result");

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject2 = (JsonObject) jsonArray.get(i);

                                String service_enquiry_id = jsonObject2.get("id").getAsString();

                                String product_name = jsonObject2.get("product_name").getAsString();

                                String product_price = jsonObject2.get("price").getAsString();

                                String user_name = jsonObject2.get("name").getAsString();

                                String user_email = jsonObject2.get("email").getAsString();

                                String user_mobile = jsonObject2.get("mobile").getAsString();

                                String description = jsonObject2.get("short_des").getAsString();

                                String created_date = jsonObject2.get("created_at").getAsString();

                                String category_name = jsonObject2.get("category_name").getAsString();

                                serviceEnquiryDatas.add(new ServiceEnquiryData(service_enquiry_id, product_name, product_price, user_name, user_email, user_mobile, description, created_date, category_name));

                            }
                            int prevSize = serviceEnquiryDatas.size();

                            serviceEnquiryAdapter.notifyItemRangeInserted(prevSize, serviceEnquiryDatas.size() -prevSize);

                            //serviceEnquiryAdapter.notifyDataSetChanged();

                            // progress_handler.hide();
                            // progressView.setVisibility(View.INVISIBLE);
                            relativeCompanylist.setVisibility(View.VISIBLE);
                            isLoading = false;

                        }

                    }

                });
    }





}
