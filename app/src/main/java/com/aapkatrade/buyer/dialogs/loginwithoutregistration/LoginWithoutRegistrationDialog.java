package com.aapkatrade.buyer.dialogs.loginwithoutregistration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.user_dashboard.address.add_address.AddAddressActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by PPC17 on 16-May-17.
 */

public class LoginWithoutRegistrationDialog extends DialogFragment {

    private ProgressDialogHandler progressDialogHandler;
    private AppSharedPreference appSharedPreference;
    private ImageView dialog_close_image_view, editMobile;
    private Context context;
    private TextView tvTourMsg, tvPassword, tvOTP, tvResend;
    private EditText etEmailOrMobile, etOTP, etPassword;
    private Button submit;
    private CardView loginWithoutRegistrationContainer;
    private LinearLayout row2Layout;
    private RelativeLayout passwordLayout, otpLayout;
    private boolean isStep1 = true, isAlreadyExistUser = false;
    private String type = "2";


    public LoginWithoutRegistrationDialog(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_without_registration, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initView(v);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(isStep1 && isAlreadyExistUser){
                    type = "1";
                    callLoginWebService();
                }else */
                if (isStep1) {
                    callStep1WebService();
                } else {
                    callStep2WebService();
                }
            }
        });

        dialog_close_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        editMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmailOrMobile.isEnabled()) {
                    etEmailOrMobile.setEnabled(false);
                } else {
                    etEmailOrMobile.setEnabled(true);
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, " Code Resend clicked");
                if (type.equals("2"))
                    callStep1WebService();
                else callLoginWebService();
            }
        });

        return v;
    }

    private void callLoginWebService() {
        progressDialogHandler.show();
        final String emailPhone = etEmailOrMobile.getText().toString();
        if (Validation.isValidEmail(emailPhone) || Validation.isValidNumber(emailPhone, Validation.getNumberPrefix(emailPhone))) {
            Ion.with(context)
                    .load(new StringBuilder(getString(R.string.webservice_base_url)).append("/").append("login").toString())
                    .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("emailphone", emailPhone)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialogHandler.hide();
                            if (result != null && result.get("error").getAsString().contains("false")) {
                                AndroidUtils.showErrorLog(context, "Login WebService Result Found --> ", result);

                                AndroidUtils.showErrorLog(context, result);
                                appSharedPreference.setSharedPref(SharedPreferenceConstants.TEMP_USER_ID.toString(), result.get("user_id").getAsString());

                                AndroidUtils.showSnackBar(loginWithoutRegistrationContainer, result.get("message").getAsString());
                                JsonObject resultJsonObject = result.get("result").getAsJsonObject();
                                if (resultJsonObject != null) {
                                    appSharedPreference.setSharedPref(SharedPreferenceConstants.CLIENT_ID.toString(), resultJsonObject.get("client_id").getAsString());
                                    appSharedPreference.setSharedPref(SharedPreferenceConstants.OTP_ID.toString(), resultJsonObject.get("otp_id").getAsString());
                                    isStep1 = false;
                                    visibleHiddenLayouts();

                                } else {
                                    AndroidUtils.showErrorLog(context, "Null Result Tag");
                                }

                            } else {
                                AndroidUtils.showErrorLog(context, "Login WebService Null Result Found");
                            }
                        }
                    });
        }
    }

    private void saveDataInSharedPreference(JsonObject webservice_returndata) {

        JsonObject jsonObject = webservice_returndata.getAsJsonObject("all_info");
        Log.e("hi", jsonObject.toString());

        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), webservice_returndata.get("user_id").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), jsonObject.get("name").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), jsonObject.get("name").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), jsonObject.get("lastname").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), jsonObject.get("email").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), jsonObject.get("mobile").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.COUNTRY_ID.toString(), jsonObject.get("country_id").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.STATE_ID.toString(), jsonObject.get("state_id").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.CITY_ID.toString(), jsonObject.get("city_id").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.ADDRESS.toString(), jsonObject.get("address").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.DEVICE_ID.toString(), jsonObject.get("device_id").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.UPDATED_AT.toString(), jsonObject.get("updated_at").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.STATUS.toString(), jsonObject.get("status").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.ORDER.toString(), webservice_returndata.get("order").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.CREATED_AT.toString(), webservice_returndata.get("createdAt").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), jsonObject.get("sh_address").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), jsonObject.get("sh_phone").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), jsonObject.get("sh_name").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), jsonObject.get("sh_state").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), jsonObject.get("sh_city").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), jsonObject.get("sh_landmark").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), jsonObject.get("sh_pincode").getAsString());
        appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pic").getAsString());
    }

    private void callStep1WebService() {
        progressDialogHandler.show();
        final String emailPhone = etEmailOrMobile.getText().toString();
        if (Validation.isValidEmail(emailPhone) || Validation.isValidNumber(emailPhone, Validation.getNumberPrefix(emailPhone))) {
            Ion.with(context)
                    .load(new StringBuilder(getString(R.string.webservice_base_url)).append("/").append("register").toString())
                    .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("emailphone", emailPhone)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialogHandler.hide();
                            if (result != null && result.get("error").getAsString().contains("false")) {
                                AndroidUtils.showErrorLog(context, result);
                                appSharedPreference.setSharedPref(SharedPreferenceConstants.TEMP_USER_ID.toString(), result.get("user_id").getAsString());

                                AndroidUtils.showSnackBar(loginWithoutRegistrationContainer, result.get("message").getAsString());
                                JsonObject resultJsonObject = result.get("result").getAsJsonObject();
                                if (resultJsonObject != null) {
                                    appSharedPreference.setSharedPref(SharedPreferenceConstants.CLIENT_ID.toString(), resultJsonObject.get("client_id").getAsString());
                                    appSharedPreference.setSharedPref(SharedPreferenceConstants.OTP_ID.toString(), resultJsonObject.get("otp_id").getAsString());
                                    isStep1 = false;
                                    visibleHiddenLayouts();

                                } else {
                                    AndroidUtils.showErrorLog(context, "Null Result Tag");
                                }
                            } else if (result != null && result.get("error").getAsString().contains("true")) {
                                AndroidUtils.showErrorLog(context, "Register WebService Error Found", result);
                                AndroidUtils.showSnackBar(loginWithoutRegistrationContainer, result.get("message").getAsString());
                                if (result.get("message").getAsString().contains("already") || result.get("message").getAsString().contains("exist")) {
                                    tvOTP.setVisibility(View.VISIBLE);
                                    row2Layout.setVisibility(View.VISIBLE);
                                    otpLayout.setVisibility(View.VISIBLE);
                                    isStep1 = false;
                                    type = "1";
                                    callLoginWebService();
                                }
                            } else {
                                AndroidUtils.showErrorLog(context, "Register WebService Null Result Found");
                            }
                        }
                    });
        }
    }

    private void visibleHiddenLayouts() {
        passwordLayout.setVisibility(View.VISIBLE);
        row2Layout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.VISIBLE);
        tvOTP.setVisibility(View.VISIBLE);
        tvPassword.setVisibility(View.VISIBLE);
        editMobile.setVisibility(View.VISIBLE);
        etEmailOrMobile.setEnabled(false);
    }

    private void callStep2WebService() {

        AndroidUtils.showErrorLog(context, " URL  ---> " + new StringBuilder(getString(R.string.webservice_base_url)).append("/").append("varify_buyer_otp").toString());
        AndroidUtils.showErrorLog(context, "Data to sent UserID : " + appSharedPreference.getSharedPref(SharedPreferenceConstants.TEMP_USER_ID.toString()) + "  OTP " + etOTP.getText().toString() + "  CLIENT_ID  :  " + appSharedPreference.getSharedPref(SharedPreferenceConstants.CLIENT_ID.toString()) + " PASSWORD : " + etPassword.getText().toString() + "type: " + type);
        progressDialogHandler.show();
        Ion.with(context)
                .load(new StringBuilder(getString(R.string.webservice_base_url)).append("/").append("varify_buyer_otp").toString())
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.TEMP_USER_ID.toString()))
                .setBodyParameter("otp", etOTP.getText().toString())
                .setBodyParameter("client_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.CLIENT_ID.toString()))
                .setBodyParameter("password", etPassword.getText().toString())
                .setBodyParameter("type", type)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialogHandler.hide();
                        if (result != null && result.get("error").getAsString().contains("false")) {
                            AndroidUtils.showErrorLog(context, result);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), result.get("user_id").getAsString());
                            AndroidUtils.showSnackBar(loginWithoutRegistrationContainer, result.get("message").getAsString());
                            saveDataInSharedPreference(result);
                            if (result.get("message").getAsString().toLowerCase().contains("successfully") && result.get("message").getAsString().toLowerCase().contains("login")) {


                                callwebserviceUpdateCart();


                            }

                        } else if (result != null && result.get("error").getAsString().contains("true")) {
                            AndroidUtils.showErrorLog(context, "Verify Buyer WebService Error Found", result);
                            if (result.get("message") != null)
                                AndroidUtils.showSnackBar(loginWithoutRegistrationContainer, result.get("message").getAsString());
                        } else {
                            AndroidUtils.showErrorLog(context, "Verify Buyer WebService Null Result Found");
                        }
                    }
                });
    }


    @SuppressLint("NewApi")
    private void initView(View view) {
        progressDialogHandler = new ProgressDialogHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        dialog_close_image_view = (ImageView) view.findViewById(R.id.dialog_close_image_view);
        tvTourMsg = (TextView) view.findViewById(R.id.tvTourMsg);
        if (Build.VERSION.SDK_INT >= 24) {
            tvTourMsg.setText(Html.fromHtml("Get started with our secure <font color = \'#F96004\'> LOGIN </font>flow", 0));

        } else {
            tvTourMsg.setText(Html.fromHtml("Get started with our secure <font color = \'#F96004\'> LOGIN </font>flow"));
        }

        submit = (Button) view.findViewById(R.id.submit);
        etEmailOrMobile = (EditText) view.findViewById(R.id.etEmailOrMobile);
        loginWithoutRegistrationContainer = (CardView) view.findViewById(R.id.loginWithoutRegistrationContainer);
        editMobile = (ImageView) view.findViewById(R.id.editMobile);
        tvPassword = (TextView) view.findViewById(R.id.tvPassword);
        row2Layout = (LinearLayout) view.findViewById(R.id.row2Layout);
        passwordLayout = (RelativeLayout) view.findViewById(R.id.passwordLayout);
        tvOTP = (TextView) view.findViewById(R.id.tvOTP);
        otpLayout = (RelativeLayout) view.findViewById(R.id.otpLayout);
        etOTP = (EditText) view.findViewById(R.id.etOTP);
        editMobile.setVisibility(View.GONE);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        tvResend = (TextView) view.findViewById(R.id.tvResend);
    }



    private void callwebserviceUpdateCart()
    {


        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/update_cart_user";

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

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
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("update_cart_user--------------" + result.toString());

                        if (result != null) {

                            String error = result.get("error").getAsString();

                            if (error.contains("true"))
                            {
                                String message = result.get("message").getAsString();
                               // showMessage(message);
                                dismiss();

                            }
                            else
                            {

                                dismiss();
                                Intent intent = new Intent(context, AddAddressActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }


                    }
                });


    }


}