package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public class SalesTransaction extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    AppSharedPreference appSharedPreference;
    Context c;
    ArrayList<String> TransactionType = new ArrayList<>();
    Spinner SpMachineList;
    CustomSpinnerAdapter customSpinnerAdapter;
    RecyclerView recycleBillHistory;

    Button BillHistorysearch;
    private int isStartDate = -1;
    EditText etstartdate, etenddate;
    private String date;
    private LinearLayoutManager linearLayoutManager;
    SalesTransactionAdapter salesTransactionAdapter;
    ArrayList<SalesMachineResultData> salesTransactionDatas = new ArrayList<>();
    ProgressBarHandler progressBarHandler;
    ViewPager viewpagerSalesTransaction;
    ViewpagerAdapterSalesTransaction viewpagerAdapterSalesTransaction;
    FrameLayout fl_salestransaction;
    CircleIndicator circleIndicator;
    TextView txn_amount_total, sales_amount_total;
    ArrayList<ArrayList<SalesTransactionMachine>> MachineDatas = new ArrayList<>();
    ArrayList<SalesTransactionMachine> salesTransactionMachineArrayList = new ArrayList<>();

    CommonInterface commonInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysalestransaction);


        initView();
        setUpToolBar();
        setSpinnerTransactionType();
        callWebserviceSalesTransaction();


        etstartdate.setOnClickListener(new View.OnClickListener() {
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
        });


        BillHistorysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etstartdate.getText().length() != 0 && etenddate.getText().length() != 0) {

                    if (SpMachineList.getSelectedItemPosition() != 0) {

                        if (fl_salestransaction.getVisibility() == View.VISIBLE) {
                            fl_salestransaction.setVisibility(View.GONE);
                            viewpagerSalesTransaction.setVisibility(View.VISIBLE);
                            setViewPager(MachineDatas);

                        }


                        //callWebserviceSalesTransaction(SpMachineList.getSelectedItemPosition());

                    } else {

                        AndroidUtils.showToast(c, "Select Machine No");

                    }


                } else {


                    AndroidUtils.showToast(c, "Invalid Start/End Date");
                }


            }
        });


    }

    private void setViewPager(ArrayList<ArrayList<SalesTransactionMachine>> machineDatas) {





        salesTransactionMachineArrayList.add(new SalesTransactionMachine("1245646", "15-May-2017", "436", "547", "COMPLETE", "15-June-2017"));
        salesTransactionMachineArrayList.add(new SalesTransactionMachine("1245646", "15-May-2017", "436", "547", "COMPLETE", "15-June-2017"));
        salesTransactionMachineArrayList.add(new SalesTransactionMachine("1245646", "15-May-2017", "436", "547", "COMPLETE", "15-June-2017"));

        MachineDatas.add(salesTransactionMachineArrayList);

        viewpagerAdapterSalesTransaction = new ViewpagerAdapterSalesTransaction(c, this.salesTransactionDatas, machineDatas, viewpagerSalesTransaction);

        viewpagerSalesTransaction.setAdapter(viewpagerAdapterSalesTransaction);

        //circleIndicator.setViewPager(viewpagerSalesTransaction);


        txn_amount_total.setText("Txn Amount Total:1250,50");
        sales_amount_total.setText("Sales Amount Total:1250,50");


    }

    private void openCalender() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SalesTransaction.this,
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

        customSpinnerAdapter = new CustomSpinnerAdapter(c, TransactionType);

        SpMachineList.setAdapter(customSpinnerAdapter);


    }

    private void initView() {
        c = this;

        progressBarHandler = new ProgressBarHandler(this);

        appSharedPreference = new AppSharedPreference(this);
        recycleBillHistory = (RecyclerView) findViewById(R.id.recyclePaymentHistory);
        BillHistorysearch = (Button) findViewById(R.id.searchBillHistory);
        SpMachineList = (Spinner) findViewById(R.id.spMachineList);

        etstartdate = (EditText) findViewById(R.id.etstartdate);

        etenddate = (EditText) findViewById(R.id.etenddate);
        linearLayoutManager = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        viewpagerSalesTransaction = (ViewPager) findViewById(R.id.viewpagerSalesTransactionResult);

        fl_salestransaction = (FrameLayout) findViewById(R.id.fl_salestransaction);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_custom_sales_transaction);

        txn_amount_total = (TextView) findViewById(R.id.txn_amount_total);
        sales_amount_total = (TextView) findViewById(R.id.sales_amount_total);

    }

    private void callWebserviceSalesTransaction() {
        progressBarHandler.show();

        String BillHistoryWebservice = getString(R.string.webservice_base_url) + "/sellerTransaction";


        Ion.with(this)
                .load(BillHistoryWebservice).setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
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
                            AndroidUtils.showToast(c, "No Bill History Found On selected Dates");


                        } else {
                            JsonArray result_jsonArray = result.getAsJsonArray("result");


                            for (int k = 0; k < result_jsonArray.size(); k++) {

                                JsonObject result_jsonobject = result_jsonArray.get(k).getAsJsonObject();
                                String machine_number = result_jsonobject.get("machine_number").getAsString();
                                String transationStatus;
                                if (result_jsonobject.get("payout_status").getAsString().contains("1")) {
                                    transationStatus = "COMPLETED";
                                } else {
                                    transationStatus = "PENDING";

                                }

                                String txnAmount = result_jsonobject.get("txnAmount").getAsString();
                                String salesAmount = result_jsonobject.get("total_txt_amount").getAsString();
                                String ToDate = etenddate.getText().toString();
                                String fromDate =etstartdate.getText().toString();

                                salesTransactionDatas.add(new SalesMachineResultData(machine_number, txnAmount,salesAmount, ToDate, fromDate, "COMPLETE"));
                            }


                            MachineDatas.add(salesTransactionMachineArrayList);

                            setViewPager(MachineDatas);


                            AndroidUtils.showErrorLog(c, "responseBillPayment2", result.toString());
                            progressBarHandler.hide();
                        }


                    } else {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(c, "responseBillPayment2", result.toString());
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
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        ImageView back_imagview = (ImageView) findViewById(R.id.back_imagview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, c, R.color.white);
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
        Intent intent = new Intent(c, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
