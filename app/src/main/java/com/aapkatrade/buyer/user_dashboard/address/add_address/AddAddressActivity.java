package com.aapkatrade.buyer.user_dashboard.address.add_address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.address.viewpager.CartCheckoutActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;


public class AddAddressActivity extends AppCompatActivity
{

    private ArrayList<String> stateList = new ArrayList<>();
    private AppSharedPreference appSharedPreference;
    private String userid, Name, address, mobile, state_id,cityID = "",city_id,pincode,landmark;
    private EditText etFirstName, etMobileNo, etAddress,etPincode,etLandMark;
    private Button buttonSave;
    public EditText etState,etCity;
    public RelativeLayout activity_add_address;
    private ProgressBarHandler progress_handler;
    Context context;
    private ArrayList<City> cityList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_address);

        context = AddAddressActivity.this;

        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));

        progress_handler = new ProgressBarHandler(this);

        appSharedPreference = new AppSharedPreference(getApplicationContext());

        userid = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        Name = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), "");

        mobile = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), "");

        address = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), "");

        state_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), "");

        city_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), "");

        pincode  = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), "");

        landmark = appSharedPreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), "");

        System.out.println("state_id-----------" + Name+mobile+address+state_id);

        setuptoolbar();

        setup_layout();


    }

    private void setup_layout()
    {

        activity_add_address = (RelativeLayout) findViewById(R.id.activity_add_address);

        etState = (EditText) findViewById(R.id.etStateCategory);

        etState.setText(state_id);

        etCity = (EditText) findViewById(R.id.etCityCategory);

        etCity.setText(city_id);


       /* spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                // your code here
                appSharedPreference.setSharedPref(SharedPreferenceConstants.STATE_ID.toString(), String.valueOf(position));
                state_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.STATE_ID.toString(), "");
                spState.setSelection(Integer.valueOf(state_id));

                cityList = new ArrayList<>();
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0)
                {
                  *//*  findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
                    findViewById(R.id.view1).setVisibility(View.VISIBLE);*//*
                    getCity(String.valueOf(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                // your code here
            }

        });

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(AddAddressActivity.this, R.layout.black_textcolor_spinner, stateList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.black_textcolor_spinner);
        spState.setAdapter(spinnerArrayAdapter);

         if (state_id.equals("")){


         }
         else
         {

             spState.setSelection(Integer.valueOf(state_id));

         }
         */

        etFirstName = (EditText) findViewById(R.id.etFirstName);

        etMobileNo = (EditText) findViewById(R.id.etMobileNo);

        etPincode = (EditText) findViewById(R.id.etPincode);

        etAddress = (EditText) findViewById(R.id.etAddress);

        etLandMark = (EditText) findViewById(R.id.etLandMark);

        etPincode = (EditText) findViewById(R.id.etPincode);

        buttonSave = (Button) findViewById(R.id.buttonSave);

        etFirstName.setText(Name);

        etMobileNo.setText(mobile);

        etAddress.setText(address);

        etPincode.setText(pincode);

        etLandMark.setText(landmark);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (!etFirstName.getText().toString().equals(""))
                {

                    if (!etMobileNo.getText().toString().equals(""))
                    {

                        if (Validation.isValidNumber(etMobileNo.getText().toString(), Validation.getNumberPrefix(etMobileNo.getText().toString())))
                        {

                            if (!etPincode.getText().toString().equals("")){

                                if (etPincode.getText().toString().length()==6)
                                {

                                    if (!etState.getText().toString().equals(""))
                                    {

                                        if (!etCity.getText().toString().equals(""))
                                        {

                                            if (!etAddress.getText().toString().equals(""))
                                            {
                                                if (!etLandMark.getText().toString().equals(""))
                                                {

                                                    System.out.println("userid------------"+userid+etFirstName.getText().toString()+etAddress.getText().toString());
                                                    callAddCompanyWebService(userid, etFirstName.getText().toString(), etAddress.getText().toString(),etMobileNo.getText().toString(),etPincode.getText().toString(),etLandMark.getText().toString(),etState.getText().toString(),etCity.getText().toString());

                                                }
                                                else
                                                {
                                                    showMessage("Please Enter Land mark");

                                                }

                                            }
                                            else
                                            {
                                                showMessage("Please Enter Address");

                                            }

                                        }
                                        else
                                        {
                                            showMessage("Please Enter City Name");

                                        }

                                    }
                                    else
                                    {

                                        showMessage("Please Enter State Name");

                                    }



                                }
                                else
                                {
                                    showMessage("Please Enter 6 digit Pincode");

                                }

                            }
                            else
                            {
                                showMessage("Please Enter Pincode");

                            }

                        }
                        else
                        {

                            showMessage("Please Enter 10 digit or valid Mobile Number");

                        }
                    }
                    else
                    {
                        showMessage("Please Enter  Mobile Number");

                    }

                }
                else
                {
                    showMessage("Please Enter First Name");

                }


            }
        });



    }

    private void callAddCompanyWebService(String userId, final String firstName, final String address,final String phone,final String Pincode, final String landmark , final  String state, final String city)
    {
        progress_handler.show();


        Ion.with(AddAddressActivity.this)
                .load((getResources().getString(R.string.webservice_base_url)) + "/edit_shipping_address")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("name", firstName)
                .setBodyParameter("pincode", Pincode)
                .setBodyParameter("address", address)
                .setBodyParameter("state", state)
                .setBodyParameter("city",city)
                .setBodyParameter("buyer_id", userId)
                .setBodyParameter("phone",phone)
                .setBodyParameter("landmark",landmark)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {

                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        System.out.println("result-------------"+result);


                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(),  etAddress.getText().toString());
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), etFirstName.getText().toString());
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), etMobileNo.getText().toString());
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), state);
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), city);
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), etLandMark.getText().toString());
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), etPincode.getText().toString());

                        progress_handler.hide();
                        Intent checkout = new Intent(AddAddressActivity.this, CartCheckoutActivity.class);
                        checkout.putExtra("fname", etFirstName.getText().toString());
                        checkout.putExtra("mobile", etMobileNo.getText().toString());
                        checkout.putExtra("state_id", state);
                        checkout.putExtra("address", etAddress.getText().toString());
                        startActivity(checkout);


                    }
                });
    }

 /*   private void getState() {
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(context, stateList);
        spState.setAdapter(spinnerArrayAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                cityList = new ArrayList<>();
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0)
                {

                    findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
                    findViewById(R.id.view1).setVisibility(View.VISIBLE);
                    getCity(String.valueOf(position));

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


*/
   /* private void getCity(String stateId)
    {
        progress_handler.show();
        //findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
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
                        progress_handler.hide();
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
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    cityID = cityList.get(position).cityId;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        } else {
                            //showMessage("No City Found");
                        }
                    }

                });

    }
*/
    private void setuptoolbar()
    {

        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        findViewById(R.id.logoWord).setVisibility(View.GONE);

        TextView header_name = (TextView) findViewById(R.id.header_name);
        header_name.setVisibility(View.VISIBLE);

        header_name.setText(getResources().getString(R.string.add_address_heading));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        AndroidUtils.setImageColor(homeIcon, this, R.color.white);

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddAddressActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }



    public void showMessage(String message)
    {
        AndroidUtils.showSnackBar(activity_add_address, message);
    }




}

