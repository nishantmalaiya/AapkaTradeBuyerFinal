package com.aapkatrade.buyer.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.Home.buyerregistration.BuyerRegistrationActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.associate.RegistrationBusinessAssociateActivity;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
//import com.aapkatrade.buyer.seller.registration.SellerRegistrationActivity;
import com.aapkatrade.buyer.seller.registration.SellerRegistrationActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class LoginActivity extends AppCompatActivity {

    private TextView loginText, forgotPassword;
    private EditText etEmail, password;
    private RelativeLayout relativeLayoutLogin, relativeRegister;
    private AppSharedPreference appSharedpreference;
    private CoordinatorLayout coordinatorLayout;
    private Context context;
    private ProgressBarHandler progressBarHandler;
    String usertype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = LoginActivity.this;
        Intent intent = getIntent();

        Bundle b = intent.getExtras();
        usertype = b.getString("user_login");
        appSharedpreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        setUpToolBar();
        initView();
        putValues();

        relativeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (usertype.contains("SELLER")) {

                    Intent registerUserActivity = new Intent(context, SellerRegistrationActivity.class);
                    startActivity(registerUserActivity);
                } else if (usertype.contains("BUSINESS")) {
                    Intent registerUserActivity = new Intent(context, RegistrationBusinessAssociateActivity.class);
                    startActivity(registerUserActivity);
                } else {


                    Intent registerUserActivity = new Intent(context, BuyerRegistrationActivity.class);
                    startActivity(registerUserActivity);

                }


            }


        });

    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        findViewById(R.id.logoWord).setVisibility(View.INVISIBLE);
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
        AndroidUtils.setBackgroundSolid(toolbar, context, R.color.transparent, 0, GradientDrawable.RECTANGLE);
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


    private void putValues() {

        relativeLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_email = etEmail.getText().toString().trim();
                String input_password = password.getText().toString();


                if (Validation.isValidEmail(input_email) || Validation.isValidNumber(input_email, Validation.getNumberPrefix(input_email))) {


                    if (Validation.validateEdittext(password)) {
                        String login_url = "";

                        if (usertype.contains("SELLER")) {


                            callLoginWebService(getResources().getString(R.string.webservice_base_url) + "/sellerlogin", input_email, input_password);

                        } else if (usertype.contains("BUYER")) {

                            callLoginWebService(getResources().getString(R.string.webservice_base_url) + "/buyerlogin", input_email, input_password);


                        } else if (usertype.contains("BUSINESS ASSOCIATE LOGIN")) {
                            callLoginWebService(getResources().getString(R.string.webservice_base_url) + "/associatelogin", input_email, input_password);

                        }


                    } else {
                        showMessage(getResources().getString(R.string.password_validing_text));
                        password.setError(getResources().getString(R.string.password_validing_text));
                    }

                } else {
                    showMessage("Invalid Email Address / Mobile");
                    etEmail.setError("Invalid Email Address / Mobile");
                }


            }
        });
    }

    private void callLoginWebService(String login_url, String input_username, String input_password) {

        AndroidUtils.showErrorLog(context, "Login : " + input_username + "  Password : " + input_password, "*************   " + login_url + "***deviceid" + AppConfig.getCurrentDeviceId(context));

        progressBarHandler.show();
        Ion.with(context)
                .load(login_url)
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("email", input_username)
                .setBodyParameter("password", input_password)
                .setBodyParameter("platform", "Android")
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        progressBarHandler.hide();
                        if (result != null) {

                            System.out.println("result--------------" + result.toString());

                            String error = result.get("error").getAsString();

                            if (error.contains("true")) {
                                String message = result.get("message").getAsString();
                                showMessage(message);

                            } else {
                                String message = result.get("message").getAsString();

                                showMessage(message);
                                Log.e("webservice_returndata", result.toString());

                                saveDataInSharedPreference(result);


                            }
                        }


                    }
                });
    }

    private void saveDataInSharedPreference(JsonObject webservice_returndata) {


        JsonObject jsonObject = webservice_returndata.getAsJsonObject("all_info");
        Log.e("hi", jsonObject.toString());

        appSharedpreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), webservice_returndata.get("user_id").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), jsonObject.get("name").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), jsonObject.get("name").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), jsonObject.get("lastname").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), jsonObject.get("email").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), jsonObject.get("mobile").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.COUNTRY_ID.toString(), jsonObject.get("country_id").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.STATE_ID.toString(), jsonObject.get("state_id").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.CITY_ID.toString(), jsonObject.get("city_id").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.ADDRESS.toString(), jsonObject.get("address").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.DEVICE_ID.toString(), jsonObject.get("device_id").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.UPDATED_AT.toString(), jsonObject.get("updated_at").getAsString());
        appSharedpreference.setSharedPref(SharedPreferenceConstants.STATUS.toString(), jsonObject.get("status").getAsString());

        appSharedpreference.setSharedPref(SharedPreferenceConstants.CREATED_AT.toString(), webservice_returndata.get("createdAt").getAsString());


        if (usertype.contains("BUYER")) {

            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), jsonObject.get("sh_address").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), jsonObject.get("sh_phone").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), jsonObject.get("sh_name").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), jsonObject.get("sh_state").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), jsonObject.get("sh_city").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), jsonObject.get("sh_landmark").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), jsonObject.get("sh_pincode").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pic").getAsString());

            appSharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pic").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.ORDER_LIST_COUNT.toString(), webservice_returndata.get("order").getAsString());
        } else if (usertype.contains("SELLER")) {
            appSharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pick").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.ORDER_LIST_COUNT.toString(), webservice_returndata.get("order").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.SHOP_LIST_COUNT.toString(), webservice_returndata.get("shops").getAsString());
            appSharedpreference.setSharedPref(SharedPreferenceConstants.ENQUIRY_LIST_COUNT.toString(), webservice_returndata.get("enquiries").getAsString());


        }

        callwebserviceUpdateCart();
    }


    private void initView() {
        forgotPassword = (TextView) findViewById(R.id.tv_forgotpassword);
        loginText = (TextView) findViewById(R.id.tv_login);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        etEmail = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etpassword);
        relativeLayoutLogin = (RelativeLayout) findViewById(R.id.rl_login);
        relativeRegister = (RelativeLayout) findViewById(R.id.relativeRegister);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordActivity = new Intent(context, ForgotPassword.class);
                forgotPasswordActivity.putExtra("forgot_index", "0");
                startActivity(forgotPasswordActivity);
            }
        });


        loginText.setText(usertype);
    }

    public void showMessage(String message) {
        AndroidUtils.showSnackBar(coordinatorLayout, message);
    }


    private void callwebserviceUpdateCart() {
        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/update_cart_user";

        String user_id = appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        System.out.println("user_id--------------" + user_id);

        if (user_id.equals("notlogin")) {
            user_id = "";
        }

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .setBodyParameter("platform", "Android")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("update_cart_user--------------" + result.toString());

                        if (result != null) {

                            String error = result.get("error").getAsString();

                            if (error.contains("true")) {
                                String message = result.get("message").getAsString();
                                showMessage(message);

                            } else {

                                Intent Homedashboard = new Intent(context, HomeActivity.class);
                                Homedashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(Homedashboard);

                            }
                        }


                    }
                });


    }


}
