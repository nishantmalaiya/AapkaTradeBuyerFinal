package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class BillPaymentActivity extends AppCompatActivity {
    private Context context;
    private AppSharedPreference appSharedPreference;
    private TextView tvStatusTitle, tvStatusMsg, tvHeaderTransaction, tvSubHeaderTransaction,
            tvHeaderAmountPaid, tvSubHeaderAmountPaid, tvReceiptNo, tvDone;
    private ImageView tickImageView, circleImageView1, circleImageView2;
    private LinearLayout circleTile2Layout, circleTile1Layout, paymentCompletionRootLayout;
    private TextView rlSaveLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvAmount;

    private BillPaymentAdapter billPaymentAdapter;

    private ArrayList<BillPaymentListData> billPaymentListDatas = new ArrayList<>();

    private ProgressBarHandler progressBarHandler;
    public static CommonInterface commonInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        context = BillPaymentActivity.this;
        setUpToolBar();
        initView();

        callBillPayment();
    }

    private void callBillPayment() {

        recyclerView.setLayoutManager(linearLayoutManager);


        String webserviceUrlBillPayment = context.getString(R.string.webservice_base_url) + "/get_bill_payment";


        Ion.with(context)
                .load(webserviceUrlBillPayment)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3").asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {


                if (result != null) {
                    progressBarHandler.show();
                    if (result.get("error").getAsString().contains("false")) {


                        JsonArray jsonArrayResult = result.getAsJsonArray("result");

                        for (int k = 0; k < jsonArrayResult.size(); k++) {
                            JsonObject jsonObjectResult = jsonArrayResult.get(k).getAsJsonObject();


                            String machine_number = jsonObjectResult.get("machine_number").getAsString();
                            String machine_type = jsonObjectResult.get("machine_type").getAsString();
                            String machine_fee = jsonObjectResult.get("machine_fee").getAsString();

                            billPaymentListDatas.add(new BillPaymentListData(machine_type, machine_number, machine_fee, false, R.drawable.circle_sienna));


                        }
                        recyclerView.setLayoutManager(linearLayoutManager);

                        if (billPaymentAdapter == null) {


                            billPaymentAdapter = new BillPaymentAdapter(BillPaymentActivity.this, billPaymentListDatas);
                            recyclerView.setAdapter(billPaymentAdapter);
                        } else {
                            billPaymentAdapter.notifyDataSetChanged();
//
                        }
                        progressBarHandler.hide();

                    } else {
                        AndroidUtils.showErrorLog(context, "json_return_error", result.get("error").getAsString());
                        progressBarHandler.hide();
                    }


                } else {


                    progressBarHandler.hide();

                }


            }
        });


     /*   if (billPaymentAdapter == null) {

            billPaymentListDatas.add(new BillPaymentListData("POS", "5375475734", "250", false, R.drawable.circle_sienna));
            billPaymentListDatas.add(new BillPaymentListData("MPOS", "537547656", "1250", false, R.drawable.circle_purple));
            billPaymentListDatas.add(new BillPaymentListData("MPOS", "537547656", "1250", false, R.drawable.circle_purple));
            billPaymentListDatas.add(new BillPaymentListData("POS", "5375475734", "250", false, R.drawable.circle_sienna));
            billPaymentAdapter = new BillPaymentAdapter(BillPaymentActivity.this, billPaymentListDatas);
            recyclerView.setAdapter(billPaymentAdapter);
        } else {
            billPaymentAdapter.notifyDataSetChanged();
//
        }*/


    }


    private void initView() {
        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        tvStatusMsg = (TextView) findViewById(R.id.tvStatusMsg);
        tickImageView = (ImageView) findViewById(R.id.tickImageView);
        tvAmount = (TextView) findViewById(R.id.tv_billing_amount2);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView_bill_payment);
        rlSaveLayout = (TextView) findViewById(R.id.tvDone);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {


                tvAmount.setText(new StringBuilder(getString(R.string.rupay_text)).append("  ").append(String.valueOf((Integer) object)));


                return null;
            }
        };


        tvAmount.setText(new StringBuilder(getString(R.string.rupay_text)).append("  0"));
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        ImageView back_imagview = (ImageView) findViewById(R.id.back_imagview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
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

    private void callHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
