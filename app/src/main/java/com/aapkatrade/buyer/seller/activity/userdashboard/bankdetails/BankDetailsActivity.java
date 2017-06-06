package com.aapkatrade.buyer.seller.activity.userdashboard.bankdetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;

public class BankDetailsActivity extends AppCompatActivity {
    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> stateIds = new ArrayList<>();
    private Spinner spState;
    private String stateID = "", userId= "";
    private EditText etBeneficiaryName, etBeneficiaryIFSC, etBeneficiaryAccount, etBeneficiaryBankName, etBeneficiaryAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        context = BankDetailsActivity.this;
        setUpToolBar();
        initView();
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        if(Validation.isNonEmptyStr(userId)){
            AndroidUtils.showErrorLog(context, "userId:::if:::", userId);
            callBankDetailWebService();
        }else {
            AndroidUtils.showErrorLog(context, "userId else::::::", userId);
        }
        loadInitSpinner();

    }

    private void callBankDetailWebService() {
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/bankdetails")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if(result!=null){
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if(result.get("error").getAsString().equalsIgnoreCase("false")){
                                if(result.get("message").getAsString().equalsIgnoreCase("Success")){
                                    JsonArray jsonArray = result.get("result").getAsJsonArray();
                                    if (jsonArray!=null && jsonArray.size()>0){
//                                        for (JsonObject jsonObject : jsonArray){
//
//                                        }
                                    }

                                }else {
                                    AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                }

                            }else {
                                AndroidUtils.showErrorLog(context, "error::::::TRUE");
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, "result::::::NULL");
                        }
                    }
                });
    }

    private void loadInitSpinner() {
        getState();
    }

    private void initView() {
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        spState = (Spinner) findViewById(R.id.spStateCategory);
        etBeneficiaryName = (EditText) findViewById(R.id.etBeneficiaryName);
        etBeneficiaryIFSC = (EditText) findViewById(R.id.etBeneficiaryIFSC);
        etBeneficiaryAccount = (EditText) findViewById(R.id.etBeneficiaryAccount);
        etBeneficiaryBankName = (EditText) findViewById(R.id.etBeneficiaryBankName);
        etBeneficiaryAddress = (EditText) findViewById(R.id.etBeneficiaryAddress);
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }

    private void getState() {
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        stateIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_ids)));
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(context, stateList);
        spState.setAdapter(spinnerArrayAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0) {
                    stateID = String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
