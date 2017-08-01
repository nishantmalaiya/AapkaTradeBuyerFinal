package com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class BillHistory extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    AppSharedPreference appSharedPreference;
    Context context;
    ArrayList<String> machineList = new ArrayList<>();
    Spinner SpMachineList;
    CustomSpinnerAdapter customSpinnerAdapter;
    RecyclerView recycleBillHistory;

    Button BillHistorysearch;
    private int isStartDate = -1;
    EditText etstartdate, etenddate;
    private String date;
    private LinearLayoutManager linearLayoutManager;
    BillHistoryAdapter billHistoryAdapter;
    ArrayList<BillHistoryData> billHistoryDatas = new ArrayList<>();
    ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        setUpToolBar();
        callWebserviceMachineList();


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

                        callWebserviceBillHistory(SpMachineList.getSelectedItemPosition());

                    } else {

                        AndroidUtils.showToast(context, "Select Machine No");

                    }


                } else {


                    AndroidUtils.showToast(context, "Invalid Start/End Date");
                }


            }
        });


    }

    private void openCalender() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                BillHistory.this,
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

    private void callWebserviceMachineList() {

        String MachineListWebservice = getString(R.string.webservice_base_url) + "/get_machine";


        Ion.with(this)
                .load(MachineListWebservice)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (result != null) {

                    if (result.get("error").getAsString().contains("false"))

                    {
                        machineList.add("Select Machine");

                        JsonArray resultjsonarray = result.getAsJsonArray("result");


                        for (int i = 0; i < resultjsonarray.size(); i++) {

                            JsonObject jsonObjectMachineno = resultjsonarray.get(i).getAsJsonObject();

                            String machinenumber = jsonObjectMachineno.get("machine_number").getAsString();

                            machineList.add(machinenumber);

                        }


                        customSpinnerAdapter = new CustomSpinnerAdapter(context, machineList);

                        SpMachineList.setAdapter(customSpinnerAdapter);

                        AndroidUtils.showErrorLog(context, "responseMachineList", result.toString());


                    } else {

                        AndroidUtils.showErrorLog(context, "responseMachineList", result.toString());
                    }


                }


            }
        });


    }

    private void initView() {
        context = this;

        progressBarHandler = new ProgressBarHandler(this);

        appSharedPreference = new AppSharedPreference(this);
        recycleBillHistory = (RecyclerView) findViewById(R.id.recyclePaymentHistory);
        BillHistorysearch = (Button) findViewById(R.id.searchBillHistory);
        SpMachineList = (Spinner) findViewById(R.id.spMachineList);

        etstartdate = (EditText) findViewById(R.id.etstartdate);

        etenddate = (EditText) findViewById(R.id.etenddate);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    private void callWebserviceBillHistory(int selectedItemPosition) {
        progressBarHandler.show();

        String BillHistoryWebservice = getString(R.string.webservice_base_url) + "/billing_history";


        Ion.with(this)
                .load(BillHistoryWebservice).setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))

                .setBodyParameter("machine_no", machineList.get(selectedItemPosition).toString())

               /* .setBodyParameter("seller_id", "2")

                .setBodyParameter("machine_no", "15456324")*/

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


                        } else {
                            JsonArray result_jsonArray = result.getAsJsonArray("result");


                            for (int k = 0; k < result_jsonArray.size(); k++) {

                                JsonObject result_jsonobject = result_jsonArray.get(k).getAsJsonObject();
                                String paymentDate = result_jsonobject.get("created_at").getAsString();

                                String paymentStatus = result_jsonobject.get("status_name").getAsString();
                                String paymentMode = result_jsonobject.get("transferType").getAsString();
                                String RequestRefNo = result_jsonobject.get("request_reference_no").getAsString();
                                String BankRefNo = result_jsonobject.get("bankReferenceNo").getAsString();
                                String paymentAmount = result_jsonobject.get("total_vendor_amt").getAsString();

                                billHistoryDatas.add(new BillHistoryData(paymentMode, paymentDate, RequestRefNo, BankRefNo, paymentStatus, paymentAmount));


                            }

                            billHistoryAdapter = new BillHistoryAdapter(BillHistory.this, billHistoryDatas);

                            recycleBillHistory.setLayoutManager(linearLayoutManager);
                            recycleBillHistory.setAdapter(billHistoryAdapter);


                            AndroidUtils.showErrorLog(context, "responseBillPayment2", result.toString());
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
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView header_name = (TextView) findViewById(R.id.header_name);
        header_name.setVisibility(View.VISIBLE);
        header_name.setText("");
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

    private void callHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
