package com.aapkatrade.buyer.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.ChangeFont;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class ResetPasswordFragment extends Fragment implements View.OnClickListener {

    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private TextView tv_forgot_password, tv_forgot_password_description;
    private EditText et_password, et_confirm_password;
    private Button btn_reset_password;
    private CoordinatorLayout activity_forgot__password;
    private String usertype;
    private String user_id;
    private String otp_id;
    private String classname;
    private ForgotPasswordFragment forgot_password_fragment;
    private ResetPasswordFragment reset_passwordFragment;
    private LinearLayout reset_password_container;
    private String class_index;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        otp_id = getArguments().getString("otp_id");

        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);

        initView(v);


        return v;

    }

    private void initView(View v) {
        appSharedPreference = new AppSharedPreference(getActivity());
        progressBarHandler = new ProgressBarHandler(getActivity());

        tv_forgot_password = (TextView) v.findViewById(R.id.tv_forgot_password);
        tv_forgot_password_description = (TextView) v.findViewById(R.id.tv_forgot_password_description);


        et_password = (EditText) v.findViewById(R.id.et_password);
        et_confirm_password = (EditText) v.findViewById(R.id.et_confirm_password);

        btn_reset_password = (Button) v.findViewById(R.id.btn_change_password);
        btn_reset_password.setOnClickListener(this);

        activity_forgot__password = (CoordinatorLayout) v.findViewById(R.id.coordinate_reset_password);
        reset_password_container = (LinearLayout) v.findViewById(R.id.reset_password_container);
        ChangeFont.Change_Font_textview(getActivity(), tv_forgot_password);
        ChangeFont.Change_Font_textview(getActivity(), tv_forgot_password_description);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_change_password:

                Log.e("enter", "enter");
                Validatefields();


                break;
        }

    }

    private void Validatefields() {

        if (Validation.isValidPassword(et_password.getText().toString().trim())) {
            Log.e("enter1", "enter1");
            if (Validation.isPasswordMatching(et_password.getText().toString().trim(), et_confirm_password.getText().toString().trim())) {

                call_reset_webservice();
            } else {
                showmessage(getResources().getString(R.string.passwordnotmathed));

            }

        }


//        else if (Validation.isValidPassword(et_confirm_password.getText().toString().trim())) {
//            Log.e("enter2","enter2");
//            if(Validation.isPasswordMatching(et_password.getText().toString().trim(),et_confirm_password.getText().toString().trim()))
//            {
//                call_reset_webservice();
//            }
//            else {
//                showMessage("! password not matched");
//
//            }
//
//
//        }


        else {

            showmessage(getResources().getString(R.string.password_validing_text));

        }


    }

    private void showmessage(String message) {
        AndroidUtils.showSnackBar(activity_forgot__password, message);
    }


    private void call_reset_webservice() {
        progressBarHandler.show();

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        String webservice_reset_password = getResources().getString(R.string.webservice_base_url) + "/resetPwd";


        Log.e("user_id", user_id);

        Ion.with(getActivity())
                .load(webservice_reset_password)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id ", user_id)
                .setBodyParameter("password", et_confirm_password.getText().toString())
                .setBodyParameter("otp_id", otp_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        System.out.println("result-------------"+result);

                        progressBarHandler.hide();

                        if (result != null) {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {


                                Intent go_to_activity_otp_verify = new Intent(getActivity(), HomeActivity.class);
                                go_to_activity_otp_verify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(go_to_activity_otp_verify);
                            }
                            String message = result.get("message").getAsString();
                            showmessage(message);

                            saveDataInSharedPreference(result);

                           /* String userid = result.get("user_id").getAsString();
                            JsonObject user_data = result.getAsJsonObject("user_data");

                            String fname = user_data.get("name").getAsString();
                            String userName = user_data.get("name").getAsString();
                            String lastname = user_data.get("lastname").getAsString();
                            String email = user_data.get("email").getAsString();

                            String mobile = user_data.get("mobile").getAsString();
                            String profilepic = user_data.get("profile_pic").getAsString();


                            String address = user_data.get("address").getAsString();
                            String sh_phone = user_data.get("sh_phone").getAsString();
                            String sh_state = user_data.get("sh_state").getAsString();

                            String sh_city = user_data.get("sh_city").getAsString();
                            String sh_landmark = user_data.get("sh_landmark").getAsString();
                            String sh_pincode = user_data.get("sh_pincode").getAsString();


                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), userid);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), userName);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), fname);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), lastname);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), email);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), mobile);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profilepic);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), address);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), sh_phone);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), sh_state);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), sh_city);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), sh_landmark);
                            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), sh_pincode);
*/

                            progressBarHandler.hide();
                        } else {
                            Log.e("error_json", e.toString());
                            progressBarHandler.hide();
                        }
                        Log.e("reset_password", result.toString());


//
                    }

                });

    }

    private void saveDataInSharedPreference(JsonObject webservice_returndata) {


        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), webservice_returndata.get("user_type").getAsString());

        JsonObject jsonObject = webservice_returndata.getAsJsonObject("all_info");
        Log.e("hi_login_response", jsonObject.toString());

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

        appSharedPreference.setSharedPref(SharedPreferenceConstants.CREATED_AT.toString(), webservice_returndata.get("createdAt").getAsString());


        if (webservice_returndata.get("user_type").getAsString().contains("1")) {

            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), jsonObject.get("sh_address").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), jsonObject.get("sh_phone").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), jsonObject.get("sh_name").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_STATE.toString(), jsonObject.get("sh_state").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), jsonObject.get("sh_city").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_LANDMARK.toString(), jsonObject.get("sh_landmark").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), jsonObject.get("sh_pincode").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pic").getAsString());

            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pic").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.ORDER_LIST_COUNT.toString(), webservice_returndata.get("order").getAsString());
        } else if (webservice_returndata.get("user_type").getAsString().contains("2"))
        {
            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), jsonObject.get("profile_pick").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.ORDER_LIST_COUNT.toString(), webservice_returndata.get("order").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.SHOP_LIST_COUNT.toString(), webservice_returndata.get("shops").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.ENQUIRY_LIST_COUNT.toString(), webservice_returndata.get("enquiries").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), jsonObject.get("profile_video").getAsString());
            appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), jsonObject.get("video_thumbnail").getAsString());

        }

        callwebserviceUpdateCart();
    }



    private void callwebserviceUpdateCart()
    {
        progressBarHandler.show();

        String login_url = getActivity().getResources().getString(R.string.webservice_base_url) + "/update_cart_user";

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        System.out.println("user_id--------------" + user_id);

        if (user_id.equals("notlogin"))
        {
            user_id = "";
        }

        Ion.with(getActivity())
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(getActivity()))
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
                                AndroidUtils.showToast(getActivity(),message);

                            } else {

                                Intent Homedashboard = new Intent(appSharedPreference, HomeActivity.class);
                                Homedashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(Homedashboard);

                            }
                        }


                    }
                });
    }



}
