package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter.SalesMachineResultData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter.ViewpagerAdapterSalesTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import me.relex.circleindicator.CircleIndicator;

public class SalesTransactionActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    AppSharedPreference appSharedPreference;
    Context context;
    ArrayList<String> TransactionType = new ArrayList<>();
    Spinner SpMachineList;
    CustomSpinnerAdapter customSpinnerAdapter;
    RecyclerView recycleBillHistory;
    Button billHistorysearch;
    private int isStartDate = -1;
    EditText etstartdate, etenddate;
    private String date;
    private LinearLayoutManager linearLayoutManager;
    SalesTransactionAdapter salesTransactionAdapter;
    ImageView img_startdate, img_enddate;
    ProgressBarHandler progressBarHandler;
    ViewPager viewpagerSalesTransaction;
    ViewpagerAdapterSalesTransaction viewpagerAdapterSalesTransaction;
    RelativeLayout fl_salestransaction, salestransactionfooter;
    CircleIndicator circleIndicator;
    TextView txn_amount_total, sales_amount_total;
    ArrayList<SalesMachineResultData> MachineDatas = new ArrayList<>();
    ArrayList<SalesTransactionData> SalesTransactionData = new ArrayList<>();
    CommonInterface commonInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysalestransaction);


        initView();
        setUpToolBar();
        setSpinnerTransactionType();
        img_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = 1;
                openCalender();
            }
        });


        img_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = 0;

                openCalender();

            }
        });
        /*etstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartDate = 0;

                openCalender();


            }

        });
        etenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = 1;
                openCalender();
            }
        });*/


        billHistorysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etstartdate.getText().length() != 0 && etenddate.getText().length() != 0) {

                    if (SpMachineList.getSelectedItemPosition() != 0) {

                        if (fl_salestransaction.getVisibility() == View.VISIBLE) {
                            fl_salestransaction.setVisibility(View.GONE);
                            viewpagerSalesTransaction.setVisibility(View.VISIBLE);
                            salestransactionfooter.setVisibility(View.VISIBLE);
                            callWebserviceSalesTransaction();

                        }


                        //callWebserviceSalesTransaction(SpMachineList.getSelectedItemPosition());

                    } else {

                        AndroidUtils.showToast(context, "Select Transaction Type");

                    }


                } else {


                    AndroidUtils.showToast(context, "Invalid Start/End Date");
                }


            }
        });


    }

    private void setViewPager(ArrayList<SalesMachineResultData> machineDatas, String totalTxnAmount, String totalSalesAmount) {


        viewpagerAdapterSalesTransaction = new ViewpagerAdapterSalesTransaction(context, machineDatas, viewpagerSalesTransaction);

        viewpagerSalesTransaction.setAdapter(viewpagerAdapterSalesTransaction);

        //circleIndicator.setViewPager(viewpagerSalesTransaction);

        StringBuilder stringBuilder_txnamount = new StringBuilder("<br><font size=\"25\" color=" + "#ffffff>Txn Amount. <br> " + "Total" + " " + context.getString(R.string.rupay_text) + totalTxnAmount + "</font>");
        String tvData = stringBuilder_txnamount.toString();
        txn_amount_total.setText(Html.fromHtml(tvData));

        StringBuilder stringBuilder_salesamount = new StringBuilder("<br><font size=\"20\" color=" + "#ffffff>Sales Amount. <br> " + "Total" + " " + context.getString(R.string.rupay_text) + totalSalesAmount + "</font>");
        String tvData2 = stringBuilder_salesamount.toString();
        sales_amount_total.setText(Html.fromHtml(tvData2));


    }

    private void openCalender() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SalesTransactionActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        if (isStartDate == 1) {
            if (etstartdate.getText() != null || etstartdate.getText().toString().length() >= 8)


                dpd.setMinDate(AndroidUtils.stringToCalender(etstartdate.getText().toString()));
        }
        dpd.show(getFragmentManager(), "DatePickerDialog");

    }

    private void setSpinnerTransactionType() {
        TransactionType.add("Select Transaction Type");
        TransactionType.add("POP");
        TransactionType.add("ONLINE SALES");

        customSpinnerAdapter = new CustomSpinnerAdapter(context, TransactionType);

        SpMachineList.setAdapter(customSpinnerAdapter);


    }

    private void initView()
    {
        context = this;

        progressBarHandler = new ProgressBarHandler(this);

        appSharedPreference = new AppSharedPreference(this);
        recycleBillHistory = (RecyclerView) findViewById(R.id.recyclePaymentHistory);
        billHistorysearch = (Button) findViewById(R.id.searchBillHistory);
        SpMachineList = (Spinner) findViewById(R.id.spMachineList);

        etstartdate = (EditText) findViewById(R.id.etstartdate);

        etenddate = (EditText) findViewById(R.id.etenddate);
        img_startdate = (ImageView) findViewById(R.id.img_startdate);
        img_enddate = (ImageView) findViewById(R.id.img_enddate);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        viewpagerSalesTransaction = (ViewPager) findViewById(R.id.viewpagerSalesTransactionResult);
        salestransactionfooter = (RelativeLayout) findViewById(R.id.salestransactionfooter);
        fl_salestransaction = (RelativeLayout) findViewById(R.id.fl_salestransaction);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_custom_sales_transaction);

        txn_amount_total = (TextView) findViewById(R.id.txn_amount_total);
        sales_amount_total = (TextView) findViewById(R.id.sales_amount_total);

    }

    private void callWebserviceSalesTransaction() {
        progressBarHandler.show();

        String BillHistoryWebservice = getString(R.string.webservice_base_url) + "/sellerTransaction";


        Ion.with(this)
                .load(BillHistoryWebservice).setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
//                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))


                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .setBodyParameter("from_date", etstartdate.getText().toString())
                .setBodyParameter("to_date", etenddate.getText().toString())
                .setBodyParameter("page", "1")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (result != null) {

                    if (result.get("error").getAsString().contains("false"))

                    {

                        JsonObject pagination_jsonobject = result.getAsJsonObject("pagination");
                        String total_result = pagination_jsonobject.get("total_result").getAsString();
                        if (total_result.contains("0")) {
                            progressBarHandler.hide();
                            AndroidUtils.showToast(context, "No Bill History Found On selected Dates");
                            viewpagerSalesTransaction.setVisibility(View.GONE);
                            salestransactionfooter.setVisibility(View.GONE);
                            fl_salestransaction.setVisibility(View.VISIBLE);


                        } else {
                            JsonArray result_jsonArray = result.getAsJsonArray("result");
                            String TotalTxnAmount = "", TotalSalesAmount = "";

                            for (int k = 0; k < result_jsonArray.size(); k++) {

                                JsonObject result_jsonobject = result_jsonArray.get(k).getAsJsonObject();
                                String machine_number = result_jsonobject.get("machine_number").getAsString();
                                String transationStatus;
                                if (result_jsonobject.get("payout_status").getAsString().contains("1")) {
                                    transationStatus = "COMPLETED";
                                } else {
                                    transationStatus = "PENDING";

                                }

                                String txnAmount = result_jsonobject.get("txn_amount").getAsString();
                                String salesAmount = result_jsonobject.get("total_txt_amount").getAsString();
                                String ToDate = etenddate.getText().toString();
                                String fromDate = etstartdate.getText().toString();
                                JsonObject transactions = result_jsonobject.getAsJsonObject("transactions");
                                JsonObject total = transactions.getAsJsonObject("total");

                                TotalTxnAmount = total.get("txn_amount").getAsString();
                                TotalSalesAmount = total.get("sales_amount").getAsString();

                                JsonArray resulr_list_data = transactions.getAsJsonArray("list");

                                for (int l = 0; l < resulr_list_data.size(); l++) {
                                    JsonObject resultjsonlist = resulr_list_data.get(l).getAsJsonObject();
                                    String listStatus;
                                    if (resultjsonlist.get("payout_status").getAsString().contains("1")) {
                                        listStatus = "COMPLETED";
                                    } else {
                                        listStatus = "PENDING";

                                    }
                                    String paymentdate = resultjsonlist.get("payout_date").getAsString();
                                    String RequestRefNo = resultjsonlist.get("request_reference_no").getAsString();
                                    String BankRefNo = resultjsonlist.get("request_reference_no").getAsString();

                                    String paymentAmount = resultjsonlist.get("net_amount").getAsString();


                                    SalesTransactionData.add(new SalesTransactionData(listStatus, paymentdate, RequestRefNo, BankRefNo, listStatus, paymentAmount));
                                }


                                MachineDatas.add(new SalesMachineResultData(machine_number, txnAmount, salesAmount, ToDate, fromDate, "COMPLETE", TotalTxnAmount, TotalSalesAmount, SalesTransactionData));
                            }


                            setViewPager(MachineDatas, TotalTxnAmount, TotalSalesAmount);


                            AndroidUtils.showErrorLog(context, "responseBillPayment2", result.toString());


                            if (findViewById(R.id.nestedScrollViewsalesTransaction).getVisibility() == View.GONE) {
                                findViewById(R.id.nestedScrollViewsalesTransaction).setVisibility(View.VISIBLE);
                            }
                            progressBarHandler.hide();
                        }


                    } else {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, "responseBillPayment2", result.toString());
                    }


                }


            }
        });


    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date = (new StringBuilder()).append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString();
        if (isStartDate == 0) {
            etstartdate.setText(date);
        } else if (isStartDate == 1) {
            etenddate.setText(date);
        }


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }


    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        ImageView back_imagview = (ImageView) findViewById(R.id.back_imagview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


    @Override
    public void onBackPressed() {

        if (fl_salestransaction.getVisibility() == View.GONE) {

            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);

            /*SpMachineList.invalidate();
            etstartdate.invalidate();
            etenddate.invalidate();
            SpMachineList.clearFocus();*/

            viewpagerSalesTransaction.setVisibility(View.GONE);
            salestransactionfooter.setVisibility(View.GONE);
            fl_salestransaction.setVisibility(View.VISIBLE);

        } else {


            super.onBackPressed();

        }


    }
}
