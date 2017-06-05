package com.aapkatrade.buyer.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.Order_detail;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.CallWebService;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.interfaces.TaskCompleteReminder;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

public class ActivityOTPVerify extends AppCompatActivity {

    Button retryotp, verifyotp;
    TextView toolbar_title_txt, tittleTxt, otpNotRespond, couter_textview;
    public static EditText editText1, editText2, editText3, editText4;
    private ProgressBarHandler progressBarHandler;
    int count = 00;
    private Context context;
    AppSharedPreference appSharedPreference;

    CoordinatorLayout coordinatorLayout;
    BroadcastReceiver receiver;
    LocalBroadcastManager bManager;
    String class_name, etEmail, etFirstName, etPassword, etMobileNo, cityID, etLastName, state_id, address;
    String otp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        class_name = getIntent().getStringExtra("class_name");
        otp_id = getIntent().getStringExtra("otp_id");
        if (class_name.contains("BuyerRegistrationActivity")) {
            etEmail = getIntent().getStringExtra("email");
            etFirstName = getIntent().getStringExtra("name");
            etPassword = getIntent().getStringExtra("password");
            etMobileNo = getIntent().getStringExtra("mobile");
            cityID = getIntent().getStringExtra("city_id");
            etLastName = getIntent().getStringExtra("lastname");
            state_id = getIntent().getStringExtra("state_id");
            address = getIntent().getStringExtra("address");
        }


        context = ActivityOTPVerify.this;
        appSharedPreference = new AppSharedPreference(context);
        setUpToolBar();
        setup_layout();
    }

    public void update_otp(String message) {
        message = message.replace("Your otp is ", "").trim();
        String a = message.substring(0, 1).trim();
        String b = message.substring(1, 2).trim();
        String c = message.substring(2, 3).trim();
        String d = message.substring(3, 4).trim();
        editText1 = (EditText) findViewById(R.id.etotp1);
        editText2 = (EditText) findViewById(R.id.etotp2);
        editText3 = (EditText) findViewById(R.id.etotp3);
        editText4 = (EditText) findViewById(R.id.etotp4);
        editText1.setText(a);
        editText2.setText(b);
        editText3.setText(c);
        editText4.setText(d);
        Log.e("message412354235", message);
    }


    private void setUpToolBar() {


        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
        //Log.e("class_name",class_name);


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

    private void setup_layout() {


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_otpverify);
        progressBarHandler = new ProgressBarHandler(this);


        verifyotp = (Button) findViewById(R.id.btn_verify);

        couter_textview = (TextView) findViewById(R.id.couter_textview);

        editText1 = (EditText) findViewById(R.id.etotp1);

        editText2 = (EditText) findViewById(R.id.etotp2);

        editText3 = (EditText) findViewById(R.id.etotp3);

        editText4 = (EditText) findViewById(R.id.etotp4);


        retryotp = (Button) findViewById(R.id.retryButton);

        //tittleTxt = (TextView) findViewById(R.id.tittleTxt);

        otpNotRespond = (TextView) findViewById(R.id.otpNotRespond);


        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText1.getText().length() != 0) {
                    if (class_name.contains("TrackOrderDialog")) {
                        String otp = editText1.getText().toString().trim() + editText2.getText().toString().trim() + editText3.getText().toString().trim() + editText4.getText().toString().trim();

                        call_verifyotp_track_order(otp);

                        AndroidUtils.showErrorLog(context, "working 1");
                    } else {

                        AndroidUtils.showErrorLog(context, "working 2");
                        String otp = editText1.getText().toString().trim() + editText2.getText().toString().trim() + editText3.getText().toString().trim() + editText4.getText().toString().trim();

                        Log.e("otp ", otp);
                        callVerifyotpWebservice(otp);
                    }
                } else {

                    Log.e("otp null", "*****");

                }


            }
        });


        retryotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOtpRetryWebservice();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        editText1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (editText1.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        editText2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (editText2.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        editText3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (editText3.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                couter_textview.setText("00: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                couter_textview.setText("00:00");
            }

        }.start();


    }

    private void call_verifyotp_track_order(String otp) {
        progressBarHandler.show();

        String track_order_url = getString(R.string.webservice_base_url) + "/varify_track_num";
        Ion.with(context)
                .load(track_order_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("ORDER_ID", "AT210417060350")
                .setBodyParameter("client_id", "12")
                .setBodyParameter("otp", otp)


                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {


                if (result != null) {
                    String error = result.get("error").getAsString();
                    if (error.contains("false")) {
                        progressBarHandler.hide();


                        AndroidUtils.showErrorLog(context, result.toString());


                        Intent go_to_activity_otp_verify = new Intent(ActivityOTPVerify.this, Order_detail.class);
                        go_to_activity_otp_verify.putExtra("class_name", ActivityOTPVerify.this.getClass().getName());
                        go_to_activity_otp_verify.putExtra("result", result.toString());
                        startActivity(go_to_activity_otp_verify);


                        Log.e("otp_id", otp_id);


                    } else {
                        progressBarHandler.hide();
                    }


                }

                progressBarHandler.hide();

                Log.e("result", result.toString());
//               JsonObject res result.get("otp_id").getAsString();


            }
        });


    }

    private void callVerifyotpWebservice(String otp) {
        progressBarHandler.show();
        String getCurrentDeviceId = AppConfig.getCurrentDeviceId(ActivityOTPVerify.this);
        HashMap<String, String> webservice_body_parameter = new HashMap<>();
        webservice_body_parameter.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        webservice_body_parameter.put("client_id", getCurrentDeviceId);
        webservice_body_parameter.put("otp", otp);
        webservice_body_parameter.put("email", etEmail);
        webservice_body_parameter.put("name", etFirstName);
        webservice_body_parameter.put("password", etPassword);
        webservice_body_parameter.put("mobile", etMobileNo);
        webservice_body_parameter.put("city_id", cityID);
        webservice_body_parameter.put("lastname", etLastName);

        webservice_body_parameter.put("state_id", state_id);
        webservice_body_parameter.put("country_id", "101");
        webservice_body_parameter.put("address", address);


        HashMap<String, String> webservice_header_type = new HashMap<>();
        webservice_header_type.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");


        String verifyotp_url = getResources().getString(R.string.webservice_base_url) + "/varify_otp";
        CallWebService.verify_otp(ActivityOTPVerify.this, verifyotp_url, "resend_otp", webservice_body_parameter, webservice_header_type);

        CallWebService.taskCompleteReminder = new TaskCompleteReminder() {
            @Override
            public void Taskcomplete(JsonObject webservice_returndata) {
                progressBarHandler.hide();
                if (webservice_returndata != null) {
                    Log.e("webservice_returndata", webservice_returndata.toString());
                    JsonObject jsonObject = webservice_returndata.getAsJsonObject();

                    String error = jsonObject.get("error").getAsString();
                    String message = jsonObject.get("message").getAsString();
                    if (error.equals("false")) {
                        showMessage(message);

                        if (class_name.contains("BuyerRegistrationActivity")) {
                            String user_type = jsonObject.get("user_type").getAsString();

                            JsonArray jsonElements = jsonObject.get("all_info").getAsJsonArray();
                            JsonObject jsonObject1 = jsonElements.get(0).getAsJsonObject();
                            AndroidUtils.showErrorLog(context, "USERNAME***&&--->>" + jsonObject1.get("name").getAsString());

                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), jsonObject.get("user_id").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), jsonObject1.get("name").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), jsonObject1.get("name").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), jsonObject1.get("lastname").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), jsonObject1.get("email").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), jsonObject1.get("mobile").getAsString());

                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), jsonObject1.get("sh_address").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), jsonObject1.get("sh_name").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), jsonObject1.get("sh_phone").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), jsonObject1.get("sh_state").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), jsonObject1.get("sh_city").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), jsonObject1.get("sh_landmark").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), jsonObject1.get("sh_pincode").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), user_type);

                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);


                        } else if (class_name.contains("ForgotPassword")) {
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), jsonObject.get("user_id").getAsString());

                            Intent intent = new Intent(ActivityOTPVerify.this, ForgotPassword.class);
                            intent.putExtra("forgot_index", "2");
                            intent.putExtra("otp_id", otp_id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);


                        } else if (class_name.contains("TrackOrderDialog")) {
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), jsonObject.get("user_id").getAsString());
                            Intent intent = new Intent(ActivityOTPVerify.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (class_name.contains("SellerRegistrationActivity")) {
                            String user_type = jsonObject.get("user_type").getAsString();


                            JsonArray jsonElements = jsonObject.get("all_info").getAsJsonArray();
                            JsonObject jsonObject1 = jsonElements.get(0).getAsJsonObject();
                            AndroidUtils.showErrorLog(context, "USERNAME***&&--->>" + jsonObject1.get("name").getAsString());


                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), jsonObject.get("user_id").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), jsonObject1.get("name").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), jsonObject1.get("name").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), jsonObject1.get("lastname").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), jsonObject1.get("email").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), jsonObject1.get("mobile").getAsString());
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), user_type);


                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), jsonObject.get("user_id").getAsString());
                            callwebserviceUpdateCartSimple();

                        }
                    } else {
                        showMessage(message);
                    }
                }
            }
        };
    }


    private void callOtpRetryWebservice() {

        HashMap<String, String> webservice_body_parameter = new HashMap<>();
        webservice_body_parameter.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        webservice_body_parameter.put("client_id", "564735473442373");


        HashMap<String, String> webservice_header_type = new HashMap<>();
        webservice_header_type.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");


        String otp_url = getResources().getString(R.string.webservice_base_url) + "/resend_otp";
        CallWebService.resend_otp(ActivityOTPVerify.this, otp_url, "resend_otp", webservice_body_parameter, webservice_header_type);

        CallWebService.taskCompleteReminder = new TaskCompleteReminder() {
            @Override
            public void Taskcomplete(JsonObject webservice_returndata) {

//                Log.e("data2", webservice_returndata.toString());

                if (webservice_returndata != null) {
                    Log.e("webservice_returndata", webservice_returndata.toString());
                    JsonObject jsonObject = webservice_returndata.getAsJsonObject();

                    String error = jsonObject.get("error").getAsString();
                    String message = jsonObject.get("message").getAsString();


                    if (error.equals("false")) {
                        String user_id = jsonObject.get("user_id").getAsString();
                        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), user_id);

                        showMessage(message);

                        Intent Homedashboard = new Intent(ActivityOTPVerify.this, HomeActivity.class);
                        Homedashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(Homedashboard);

                    } else {
                        showMessage(message);
                    }


                }

            }
        };


    }


    public void showMessage(String message) {
        AndroidUtils.showSnackBar(coordinatorLayout, message);
    }

    private void callwebserviceUpdateCart() {

        progressBarHandler.show();

        String loginUrl = context.getResources().getString(R.string.webservice_base_url) + "/update_cart_user";

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (user_id.equals("notlogin")) {
            user_id = "";
        }


        Ion.with(context)
                .load(loginUrl)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {

                            System.out.println("update_cart_user--------------" + result.toString());
                            String error = result.get("error").getAsString();

                            if (error.contains("true")) {
                                String message = result.get("message").getAsString();
                                showMessage(message);
                            } else {

                            }
                        }

                        progressBarHandler.hide();
                    }
                });
    }


    private void callwebserviceUpdateCartSimple() {

        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/update_cart_user";

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (user_id.equals("notlogin")) {
            user_id = "";
        }

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            System.out.println("update_cart_user--------------" + result.toString());
                            String error = result.get("error").getAsString();

                            if (error.contains("true")) {
                                String message = result.get("message").getAsString();
                                //showMessage(message);
                            } else {
                                Intent intent = new Intent(ActivityOTPVerify.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }


                    }
                });

        progressBarHandler.hide();
    }


}
