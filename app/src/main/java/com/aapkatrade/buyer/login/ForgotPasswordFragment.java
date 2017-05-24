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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.CallWebService;
import com.aapkatrade.buyer.general.Change_Font;
import com.aapkatrade.buyer.general.interfaces.TaskCompleteReminder;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;

import java.util.HashMap;


public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private CoordinatorLayout activityForgotPassword;
    private String usertype = "buyer";

    private String classname;
    private ForgotPasswordFragment forgot_password_fragment;
    private ResetPasswordFragment reset_passwordFragment;

    private String class_index;
    private ProgressBarHandler progressBarHandler;
    private TextView tv_forgot_password, tv_forgot_password_description;
    private EditText et_email_forgot, et_mobile_no;
    private Button btn_send_otp;
    private FrameLayout forgot_password_container;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {

        progressBarHandler = new ProgressBarHandler(getActivity());
        forgot_password_container = (FrameLayout) v.findViewById(R.id.forgot_password_container);

        tv_forgot_password = (TextView) v.findViewById(R.id.tv_forgot_password);
        tv_forgot_password_description = (TextView) v.findViewById(R.id.tv_forgot_password_description);


        et_email_forgot = (EditText) v.findViewById(R.id.et_email_forgot);
        et_mobile_no = (EditText) v.findViewById(R.id.et_mobile_no);

        btn_send_otp = (Button) v.findViewById(R.id.btn_send_otp);
        btn_send_otp.setOnClickListener(this);
        Change_Font.Change_Font_textview(getActivity(), tv_forgot_password);
        Change_Font.Change_Font_textview(getActivity(), tv_forgot_password_description);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send_otp:


                Validatefields();


                break;
        }

    }

    private void Validatefields() {
        //et_email_forgot.getText().toString() != null ? "" : et_email_forgot.getText().toString()
        if (Validation.isValidEmail(et_email_forgot.getText().toString())) {
            call_forgotpasswod_webservice();
            AndroidUtils.showErrorLog(getActivity(), "EmailAddress", et_email_forgot.getText().toString());

        } else if (Validation.isValidNumber(et_mobile_no.getText().toString(), Validation.getNumberPrefix(et_mobile_no.getText().toString()))) {
            AndroidUtils.showErrorLog(getActivity(), "phoneNo", et_mobile_no.getText().toString());
            call_forgotpasswod_webservice();


        } else {
            AndroidUtils.showErrorLog(getActivity(), "error in Validation", "error in Validation");
        }


    }


    private void call_forgotpasswod_webservice() {
        progressBarHandler.show();


        String webservice_forgot_password = getResources().getString(R.string.webservice_base_url) + "/forget";

        HashMap<String, String> webservice_body_parameter = new HashMap<>();
        webservice_body_parameter.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        webservice_body_parameter.put("type", getString(R.string.user_type));
        webservice_body_parameter.put("email", et_email_forgot.getText().toString().trim());
        webservice_body_parameter.put("mobile", et_mobile_no.getText().toString().trim());
        webservice_body_parameter.put("client_id", AppConfig.getCurrentDeviceId(getActivity()));


        HashMap<String, String> webservice_header_type = new HashMap<>();
        webservice_header_type.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");


        CallWebService.forgot_password(getActivity(), webservice_forgot_password, "forgot_password", webservice_body_parameter, webservice_header_type);
        CallWebService.taskCompleteReminder = new TaskCompleteReminder() {
            @Override
            public void Taskcomplete(JsonObject data) {
                if (data != null) {
                    String error = data.get("error").getAsString();
                    if (error.contains("false")) {
                        Log.e("data", data.toString());

                        Intent go_to_activity_otp_verify = new Intent(getActivity(), ActivityOTPVerify.class);
                        go_to_activity_otp_verify.putExtra("class_name", getActivity().getClass().getName());
                        go_to_activity_otp_verify.putExtra("otp_id", data.get("otp_id").getAsString());
                        startActivity(go_to_activity_otp_verify);
                    }
                    String message = data.get("message").getAsString();
                    AndroidUtils.showSnackBar(forgot_password_container, message);
                    progressBarHandler.hide();
                } else {
                    progressBarHandler.hide();
                }
            }
        };
    }


}
