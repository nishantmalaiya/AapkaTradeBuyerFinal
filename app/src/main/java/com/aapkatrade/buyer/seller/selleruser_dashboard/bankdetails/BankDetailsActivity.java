package com.aapkatrade.buyer.seller.selleruser_dashboard.bankdetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.aapkatrade.buyer.home.HomeActivity;
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
    private Spinner spState;
    private ImageView editDetailsIcon;
    private String stateID = "", userId = "", ifscCode = "", beneficiaryName = "", beneficiaryAccount = "", stateName = "", beneficiaryBankName = "", beneficiaryAddress = "";
    private EditText etBeneficiaryName, etBeneficiaryIFSC, etBeneficiaryAccount, etBeneficiaryBankName, etBeneficiaryAddress;
    private RelativeLayout coordinateMyprofile;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        context = BankDetailsActivity.this;
        setUpToolBar();
        initView();
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        saveButton.setVisibility(View.GONE);
        loadInitSpinner();
        if (Validation.isNonEmptyStr(userId)) {
            AndroidUtils.showErrorLog(context, "userId:::if:::", userId);
            callBankDetailWebService();
        } else {
            AndroidUtils.showErrorLog(context, "userId else::::::", userId);
        }

        etBeneficiaryIFSC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ifscCode = s.toString();
                if (Validation.isNonEmptyStr(ifscCode) && ifscCode.length() >= 11)
                    hitIFSCWebService(ifscCode);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editDetailsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableViews();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, "Clicked Save ");

                if (isAllValidateFields()) {
                    callUpdateBankDetailWebService();
                } else {
                    AndroidUtils.showSnackBar(coordinateMyprofile, "Invalid Data");
                    AndroidUtils.showErrorLog(context, "Invalid Data");

                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, "Clicked Save ");

                if (isAllValidateFields()) {
                    callUpdateBankDetailWebService();
                } else {
                    AndroidUtils.showSnackBar(coordinateMyprofile, "Invalid Data");
                    AndroidUtils.showErrorLog(context, "Invalid Data");

                }
            }
        });

    }

    private void hitIFSCWebService(String ifscCode) {
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/check_ifsc")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("ifsc_code", ifscCode)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if (result.get("error").getAsString().contains("false")) {
                                if (Validation.containsIgnoreCase(result.get("status").getAsString(), "success")) {
                                    JsonObject jsonObject = result.get("data").getAsJsonObject();
                                    stateName = jsonObject.get("STATE").getAsString();
                                    beneficiaryBankName = jsonObject.get("BANK").getAsString();
                                    beneficiaryAddress = jsonObject.get("ADDRESS").getAsString();

                                    etBeneficiaryBankName.setText(beneficiaryBankName);
                                    etBeneficiaryAddress.setText(beneficiaryAddress);
                                    AndroidUtils.showErrorLog(context, "   " + stateName);
                                    if (Validation.isNonEmptyStr(stateName)) {
                                        for (int j = 0; j < stateList.size(); j++) {
                                            if (stateList.get(j).equalsIgnoreCase(stateName)) {
                                                AndroidUtils.showErrorLog(context, stateName, "      state name   ");
                                                spState.setSelection(j);
                                            }
                                        }
                                    }
                                } else {
                                    AndroidUtils.showErrorLog(context, "result::::::NULL");
                                }
                            }
                        }

                    }
                });
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
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                if (result.get("message").getAsString().equalsIgnoreCase("Success")) {
                                    JsonArray jsonArray = result.get("result").getAsJsonArray();
                                    if (jsonArray != null && jsonArray.size() > 0) {
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                            ifscCode = jsonObject.get("beneficiaryCode").getAsString();
                                            beneficiaryName = jsonObject.get("beneficiaryName").getAsString();
                                            beneficiaryAccount = jsonObject.get("beneficiaryAccount").getAsString();
                                            stateName = jsonObject.get("statename").getAsString();
                                            beneficiaryBankName = jsonObject.get("beneficiaryBankName").getAsString();
                                            beneficiaryAddress = jsonObject.get("beneficiaryAddress").getAsString();

                                            etBeneficiaryIFSC.setText(ifscCode);
                                            etBeneficiaryName.setText(beneficiaryName);
                                            etBeneficiaryAccount.setText(beneficiaryAccount);
                                            etBeneficiaryBankName.setText(beneficiaryBankName);
                                            etBeneficiaryAddress.setText(beneficiaryAddress);
                                            AndroidUtils.showErrorLog(context, "   " + stateName);
                                            if (Validation.isNonEmptyStr(stateName)) {
                                                for (int j = 0; j < stateList.size(); j++) {
                                                    if (stateList.get(j).equalsIgnoreCase(stateName)) {
                                                        AndroidUtils.showErrorLog(context, stateName, "      state name");
                                                        spState.setSelection(j);
                                                    }
                                                }
                                            }
                                            disableViews();
                                        }
                                    } else {

                                        saveButton.setVisibility(View.VISIBLE);
                                        editDetailsIcon.setVisibility(View.GONE);
                                    }

                                } else {
                                    AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                }

                            } else {
                                AndroidUtils.showErrorLog(context, "error::::::TRUE");
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, "result::::::NULL");
                        }
                    }
                });
    }

    private boolean isAllValidateFields() {
        boolean isAllValidate = true;

        if (Validation.isEmptyStr(userId)) {
            isAllValidate = false;
            AndroidUtils.showToast(context, "Invalid User.");
        } else if (Validation.isEmptyStr(etBeneficiaryIFSC.getText().toString())) {
            isAllValidate = false;
            etBeneficiaryIFSC.setError("Please Enter Valid IFSC Code.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Enter Valid IFSC Code.");
        } else if (Validation.isEmptyStr(etBeneficiaryAccount.getText().toString())) {
            isAllValidate = false;
            etBeneficiaryAccount.setError("Please Enter Valid Account No.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Enter Valid Account No.");
        } else if (Validation.isEmptyStr(etBeneficiaryBankName.getText().toString())) {
            isAllValidate = false;
            etBeneficiaryBankName.setError("Please Enter Valid Bank Name.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Enter Valid Bank Name.");
        } else if (Validation.isEmptyStr(stateID)) {
            isAllValidate = false;
            etBeneficiaryBankName.setError("Please Select State.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Select State.");
        } else if (Validation.isEmptyStr(etBeneficiaryAddress.getText().toString())) {
            isAllValidate = false;
            etBeneficiaryAddress.setError("Please Enter Address.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Enter Address.");
        } else if (Validation.isEmptyStr(etBeneficiaryName.getText().toString())) {
            isAllValidate = false;
            etBeneficiaryName.setError("Please Enter Name.");
            AndroidUtils.showSnackBar(coordinateMyprofile, "Please Enter Name.");
        }

        return isAllValidate;
    }

    private void callUpdateBankDetailWebService() {
        AndroidUtils.showErrorLog(context,
                "-----userID-----" + userId
                        + "------user_type--------"+ SharedPreferenceConstants.USER_TYPE_SELLER.toString()
                        + "--------state_id----------- "+ stateID
                        + "--------beneficiaryBankName---------"+ etBeneficiaryBankName.getText().toString()
                        + "--------beneficiaryAccount----------- "+ etBeneficiaryAccount.getText().toString()
                        + "----------beneficiaryName---------- "+ etBeneficiaryName.getText().toString()
                        + "--------beneficiaryAddress------------"+ etBeneficiaryAddress.getText().toString());
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/bank_detail_update")
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userId)
                .setBodyParameter("user_type", SharedPreferenceConstants.USER_TYPE_SELLER.toString())
                .setBodyParameter("beneficiaryCode", etBeneficiaryIFSC.getText().toString())
                .setBodyParameter("beneficiaryAccount", etBeneficiaryAccount.getText().toString())
                .setBodyParameter("beneficiaryBankName", etBeneficiaryBankName.getText().toString())
                .setBodyParameter("state_id", stateID)
                .setBodyParameter("beneficiaryAddress", etBeneficiaryAddress.getText().toString())
                .setBodyParameter("beneficiaryName", etBeneficiaryName.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result);
                            if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                if (result.get("message").getAsString().equalsIgnoreCase("Success")) {
                                    disableViews();
                                    AndroidUtils.showSnackBar(coordinateMyprofile, "Your bank details updated successfully.");
                                } else {
                                    AndroidUtils.showErrorLog(context, "message:::::: Not Success");
                                }

                            } else {
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
        coordinateMyprofile = (RelativeLayout) findViewById(R.id.coordinate_myprofile);
        spState = (Spinner) findViewById(R.id.spStateCategory);
        etBeneficiaryName = (EditText) findViewById(R.id.etBeneficiaryName);
        etBeneficiaryIFSC = (EditText) findViewById(R.id.etBeneficiaryIFSC);
        etBeneficiaryAccount = (EditText) findViewById(R.id.etBeneficiaryAccount);
        etBeneficiaryBankName = (EditText) findViewById(R.id.etBeneficiaryBankName);
        etBeneficiaryAddress = (EditText) findViewById(R.id.etBeneficiaryAddress);
        editDetailsIcon = (ImageView) findViewById(R.id.editDetailsIcon);
        saveButton = (Button) findViewById(R.id.btnSave);
        AndroidUtils.setImageColor(editDetailsIcon, context, R.color.white);
    }

    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }

    private void getState() {
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        spState.setAdapter(new CustomSpinnerAdapter(context, stateList));
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


    private void enableViews() {
        etBeneficiaryAddress.setEnabled(true);
        etBeneficiaryIFSC.setEnabled(true);
        etBeneficiaryBankName.setEnabled(true);
        etBeneficiaryName.setEnabled(true);
        etBeneficiaryAccount.setEnabled(true);
        spState.setEnabled(true);
        saveButton.setVisibility(View.VISIBLE);
        editDetailsIcon.setVisibility(View.GONE);
    }

    private void disableViews() {
        etBeneficiaryAddress.setEnabled(false);
        etBeneficiaryIFSC.setEnabled(false);
        etBeneficiaryBankName.setEnabled(false);
        etBeneficiaryName.setEnabled(false);
        etBeneficiaryAccount.setEnabled(false);
        spState.setEnabled(false);
        saveButton.setVisibility(View.GONE);
        editDetailsIcon.setVisibility(View.VISIBLE);
    }
}