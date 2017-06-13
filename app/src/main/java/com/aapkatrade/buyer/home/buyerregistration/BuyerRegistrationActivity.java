package com.aapkatrade.buyer.home.buyerregistration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.BuyerRegistration;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.login.ActivityOTPVerify;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class BuyerRegistrationActivity extends AppCompatActivity
{
    private static BuyerRegistration formBuyerData = new BuyerRegistration();
    private int isAllFieldSet = 0;
    private Spinner spState, spCity;
    private EditText etFirstName, etLastName, etEmail, etMobileNo, etAddress, etPassword, etReenterPassword;
    private TextView tvSave, tv_agreement;
    private LinearLayout registrationLayout;
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> stateIds = new ArrayList<>();
    private ArrayList<City> cityList = new ArrayList<>();
    private String stateID = "", cityID = "";
    private DatePickerDialog datePickerDialog;
    private ProgressBarHandler progressBarHandler;
    private Context context;
    private CheckBox agreement_check;
    String refreshedToken;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        context = BuyerRegistrationActivity.this;

         refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("refreshedToken2----------------"+refreshedToken);


        setUpToolBar();

        initView();

        getState();

        tvSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("reach", "reach3");
                getBuyerFormData();
                validateFields();
                if (isAllFieldSet == 0) {
                    callWebServiceForBuyerRegistration();
                }
            }
        });


    }

    private void callWebServiceForBuyerRegistration()
    {

        Log.e("reach", " Buyer Data--------->\n" + formBuyerData.toString());

        progressBarHandler.show();
        Ion.with(BuyerRegistrationActivity.this)
                .load(getResources().getString(R.string.webservice_base_url) + "/buyerregister")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "1")
                .setBodyParameter("platform", "android")
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .setBodyParameter("name", formBuyerData.getFirstName())
                .setBodyParameter("lastname", formBuyerData.getLastName())
                .setBodyParameter("country_id", formBuyerData.getCountryId())
                .setBodyParameter("state_id", formBuyerData.getStateId())
                .setBodyParameter("city_id", formBuyerData.getCityId())
                .setBodyParameter("address", formBuyerData.getAddress())
                .setBodyParameter("email", formBuyerData.getEmail())
                .setBodyParameter("mobile", formBuyerData.getMobile())
                .setBodyParameter("password", formBuyerData.getPassword())
                .setBodyParameter("client_id", formBuyerData.getClientId())
                .setBodyParameter("token",refreshedToken)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {

                            if (result.get("error").getAsString().equals("false")) {
                                Log.e("registration_buyer", result.toString());
                                AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());

                                progressBarHandler.hide();
                                Intent call_to_startactivity = new Intent(BuyerRegistrationActivity.this, ActivityOTPVerify.class);
                                call_to_startactivity.putExtra("email", etEmail.getText().toString());
                                call_to_startactivity.putExtra("name", etFirstName.getText().toString());
                                call_to_startactivity.putExtra("password", etPassword.getText().toString());
                                call_to_startactivity.putExtra("mobile", etMobileNo.getText().toString());
                                call_to_startactivity.putExtra("city_id", cityID);
                                call_to_startactivity.putExtra("lastname", etLastName.getText().toString());
                                call_to_startactivity.putExtra("country_id", "101");
                                call_to_startactivity.putExtra("state_id", stateID);
                                call_to_startactivity.putExtra("address", etAddress.getText().toString());
                                call_to_startactivity.putExtra("class_name", context.getClass().getSimpleName());

                                System.out.println("data is available");

                                startActivity(call_to_startactivity);

                            } else {
                                progressBarHandler.hide();
                                AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());
                            }
                        } else {

                            Log.e("result_seller_error", e.toString());
                            showMessage(e.toString());
                        }
                    }

                });
    }


    private void getState() {
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        stateIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_ids)));
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(context, stateList);
        spState.setAdapter(spinnerArrayAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList = new ArrayList<>();
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0) {
                    stateID = String.valueOf(position);
                    findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
                    findViewById(R.id.view1).setVisibility(View.VISIBLE);

                    getCity(String.valueOf(stateIds.get(position)));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(String stateId) {
        progressBarHandler.show();
        findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
        Ion.with(context)
                .load("http://aapkatrade.com/slim/dropdown")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "city")
                .setBodyParameter("id", stateId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        Log.e("city result ", result == null ? "null" : result.toString());

                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");

                            City cityEntity_init = new City("-1", "Please Select City");
                            cityList.add(cityEntity_init);

                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                City cityEntity = new City(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString());
                                cityList.add(cityEntity);
                            }

                            SpCityAdapter spCityAdapter = new SpCityAdapter(context, cityList);
                            spCity.setAdapter(spCityAdapter);

                            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    cityID = cityList.get(position).cityId;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            showMessage("No City Found");
                        }
                    }

                });

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

    private void initView()
    {
        progressBarHandler = new ProgressBarHandler(this);
        registrationLayout = (LinearLayout) findViewById(R.id.registrationLayout);
        spState = (Spinner) findViewById(R.id.spStateCategory);
        spCity = (Spinner) findViewById(R.id.spCityCategory);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setText(getString(R.string.save));

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etReenterPassword = (EditText) findViewById(R.id.etReenterPassword);

        agreement_check = (CheckBox) findViewById(R.id.agreement_check);
        tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        tv_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreement_check.isChecked()) {
                    agreement_check.setChecked(false);
                } else {
                    agreement_check.setChecked(true);
                }
            }
        });

        findViewById(R.id.input_layout_city).setVisibility(View.GONE);
        findViewById(R.id.view1).setVisibility(View.GONE);


    }

    private void validateFields() {
        isAllFieldSet = 0;
        Log.e("reach", "validateFiledsCalled");


        if (formBuyerData != null) {
            if (Validation.isEmptyStr(formBuyerData.getFirstName())) {
                putError(0);
                isAllFieldSet++;
            } else if (Validation.isEmptyStr(formBuyerData.getLastName())) {
                putError(1);
                isAllFieldSet++;
            } else if (!Validation.isValidNumber(formBuyerData.getMobile(), Validation.getNumberPrefix(formBuyerData.getMobile()))) {
                putError(3);
                isAllFieldSet++;
            } else if (spState == null || spState.getSelectedItemPosition() == 0) {
                showMessage("Please Select State");
                isAllFieldSet++;
            } else if (spCity == null || spCity.getSelectedItemPosition() == 0) {
                showMessage("Please Select City");
                isAllFieldSet++;
            } else if (Validation.isEmptyStr(formBuyerData.getAddress())) {
                putError(9);
                isAllFieldSet++;
            } else if (!Validation.isValidEmail(formBuyerData.getEmail())) {
                putError(2);
                isAllFieldSet++;
            } else if (!Validation.isEmptyStr(formBuyerData.getUserName())) {
                putError(10);
                isAllFieldSet++;
            } else if (!Validation.isValidPassword(formBuyerData.getPassword())) {
                putError(4);
                isAllFieldSet++;
            }/* else if (!Validation.isValidPassword(formBuyerData.getConfirmPassword())) {
                putError(15);
                isAllFieldSet++;
            }*/ else if (!Validation.isPasswordMatching(formBuyerData.getPassword(), formBuyerData.getConfirmPassword())) {
                putError(5);
                isAllFieldSet++;
            } else if (!agreement_check.isChecked()) {
                putError(16);
                isAllFieldSet++;
            }
        }
    }

    private void putError(int id)
    {
        Log.e("reach", "       )))))))))" + id);
        switch (id) {
            case 0:
                etFirstName.setError("First Name Can't be empty");
                showMessage("First Name Can't be empty");
                break;
            case 1:
                etLastName.setError("Last Name Can't be empty");
                showMessage("Last Name Can't be empty");
                break;
            case 2:
                etEmail.setError("Please Enter Valid Email");
                showMessage("Please Enter Valid Email");
                break;
            case 3:
                etMobileNo.setError("Please Enter Valid Mobile Number");
                showMessage("Please Enter Valid Mobile Number");
                break;
            case 4:
                etPassword.setError(getResources().getString(R.string.password_validing_text));
                showMessage(getResources().getString(R.string.password_validing_text));
                break;
            case 5:
                etReenterPassword.setError("Password did not match");
                showMessage("Password did not match");
                break;
            case 9:
                etAddress.setError("Address Can't be empty");
                showMessage("Address Can't be empty");
                break;
            case 10:
                showMessage("Please Enter Valid UserName");
                break;

            case 15:
                etReenterPassword.setError(getResources().getString(R.string.password_validing_text));
                showMessage(getResources().getString(R.string.password_validing_text));
                break;
            case 16:
                showMessage("Please Check the Agreement");
                break;

            default:
                break;
        }
    }

    public void showMessage(String message) {
        AndroidUtils.showSnackBar(registrationLayout, message);
    }


    public void getBuyerFormData() {
        formBuyerData.setStateId(stateID == null ? "" : stateID);
        formBuyerData.setCityId(cityID == null ? "" : cityID);
        formBuyerData.setAddress(etAddress.getText().toString());
        formBuyerData.setFirstName(etFirstName.getText().toString());
        formBuyerData.setLastName(etLastName.getText().toString());
        formBuyerData.setEmail(etEmail.getText().toString());
        formBuyerData.setMobile(etMobileNo.getText().toString());
        formBuyerData.setPassword(etPassword.getText().toString());
        formBuyerData.setConfirmPassword(etReenterPassword.getText().toString());
        formBuyerData.setClientId(AppConfig.getCurrentDeviceId(BuyerRegistrationActivity.this));
    }


}