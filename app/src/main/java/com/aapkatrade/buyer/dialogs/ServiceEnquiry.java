package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressLint("ValidFragment")
public class ServiceEnquiry extends DialogFragment {


    private EditText etFullName, service_enquiry, price, mobile, email, etEndDate, etStartDate, description;
    private TextInputLayout input_layout_start_date, input_layout_end_date;
    private int isStartDate;
    private String date, productName, categoryName;
    private Context context;
    private TextView submit, tvCategoryName, tvProductname;
    private Button imgCLose;
    String shopid;
    private RelativeLayout dialogue_toolbar, startDateLayout, endDateLayout, rl_imgview_enquiry;
    private ImageView openStartDateCal, openEndDateCal;

    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;
    ProgressBar progressBar;
    ProgressDialog prog;

    public ServiceEnquiry(String shopid, Context context) {
        super();
        this.shopid = shopid;
        this.context = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        viewgrp = container;
        v = inflater.inflate(R.layout.fragment_service_enquiry, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialogHandler = new ProgressDialogHandler(getActivity());
        initView(v);
        return v;
    }


    private void initView(View v) {

        rl_imgview_enquiry = (RelativeLayout) v.findViewById(R.id.rl_imgview_enquiry);
        dialogue_toolbar = (RelativeLayout) v.findViewById(R.id.dialogue_toolbar);

        imgCLose = (Button) v.findViewById(R.id.imgCLose);
        etFullName = (EditText) v.findViewById(R.id.etFirstName);


        mobile = (EditText) v.findViewById(R.id.et_mobile);
        email = (EditText) v.findViewById(R.id.et_email);
        service_enquiry = (EditText) v.findViewById(R.id.et_service_enquiry);

        submit = (TextView) v.findViewById(R.id.buttonSubmit);
        AndroidUtils.setBackgroundSolid(rl_imgview_enquiry, getActivity(), R.color.white, 15, GradientDrawable.OVAL);

        AndroidUtils.setBackgroundSolid(submit, getActivity(), R.color.orange, 8, GradientDrawable.OVAL);
        AndroidUtils.setBackgroundSolid(dialogue_toolbar, getActivity(), R.color.green, 15, GradientDrawable.RECTANGLE);

        tvProductname = (TextView) v.findViewById(R.id.tvProductname);
        //tvCategoryName = (TextView) v.findViewById(R.id.tvCategoryName);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation.validateEdittext(etFullName)) {

                    if (Validation.isValidEmail(email.getText().toString())) {

                        if (Validation.isValidNumber(mobile.getText().toString(), Validation.getNumberPrefix(mobile.getText().toString()))) {

                            if (Validation.validateEdittext(service_enquiry)) {
                                String call_enquiry_url = getResources().getString(R.string.webservice_base_url) + "/enquiry";
                                call_enquiry_webservice(call_enquiry_url);

                            } else {

                                service_enquiry.setError("!Invalid Enquiry ");
                            }

                        } else {

                            mobile.setError("!Invalid Mobile No ");
                        }

                    } else {
                        email.setError("! Invalid Email");
                    }


                } else {

                    etFullName.setError("!Invalid Name");


                }


            }


        });


        imgCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void call_enquiry_webservice(String call_enquiry_url) {
        progressDialogHandler.show();
//

        Ion.with(getActivity())
                .load(call_enquiry_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("product_id", shopid)
                .setBodyParameter("name", etFullName.getText().toString().trim())
                .setBodyParameter("mobile", mobile.getText().toString().trim())
                .setBodyParameter("short_des", service_enquiry.getText().toString().trim())
                .setBodyParameter("email", email.getText().toString().trim())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result.get("error").getAsString().contains("false")) {
                            AndroidUtils.showSnackBar(viewgrp, result.get("message").getAsString());
                            AndroidUtils.showErrorLog(getActivity(), result.toString());

                            progressDialogHandler.hide();
                            dismiss();
                        } else {
                            progressDialogHandler.hide();

                        }


                    }
                });


    }

}
