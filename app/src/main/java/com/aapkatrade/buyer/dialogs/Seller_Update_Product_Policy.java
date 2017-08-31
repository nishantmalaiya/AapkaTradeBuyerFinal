package com.aapkatrade.buyer.dialogs;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by PPC16 on 7/11/2017.
 */

public class Seller_Update_Product_Policy extends DialogFragment
{
    private EditText etWarranty, et_free_installation,  et_cash_on_delivery, et_replacement_policy,et_schedule_delivery;
    private Context context;
    private TextView submit,tvProductname;
    private ImageView imgCLose;
    String shopid;
    private RelativeLayout dialogue_toolbar, rl_imgview_enquiry;
    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;

    public Seller_Update_Product_Policy(String shopid, Context context)
    {
        super();
        this.shopid = shopid;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        viewgrp = container;
        v = inflater.inflate(R.layout.fragment_seller_product_update_policy, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        progressDialogHandler = new ProgressDialogHandler(getActivity());
        initView(v);
        return v;
    }

    private void initView(View v)
    {
        rl_imgview_enquiry = (RelativeLayout) v.findViewById(R.id.rl_imgview_enquiry);
        dialogue_toolbar = (RelativeLayout) v.findViewById(R.id.dialogue_toolbar);

        imgCLose = (ImageView) v.findViewById(R.id.img_close_dialog);
        etWarranty = (EditText) v.findViewById(R.id.etWarranty);

        et_cash_on_delivery = (EditText) v.findViewById(R.id.et_cash_on_delivery);
        et_replacement_policy = (EditText) v.findViewById(R.id.et_replacement_policy);
        et_free_installation = (EditText) v.findViewById(R.id.et_free_installation);

        et_schedule_delivery = (EditText) v.findViewById(R.id.et_schedule_delivery);

        submit = (TextView) v.findViewById(R.id.buttonSubmit);

      /*  AndroidUtils.setBackgroundSolid(rl_imgview_enquiry, getActivity(), R.color.white, 15, GradientDrawable.OVAL);

        AndroidUtils.setBackgroundSolid(submit, getActivity(), R.color.orange, 8, GradientDrawable.OVAL);
        AndroidUtils.setBackgroundSolid(dialogue_toolbar, getActivity(), R.color.green, 15, GradientDrawable.RECTANGLE);
        */
        tvProductname = (TextView) v.findViewById(R.id.tvProductname);
        //tvCategoryName = (TextView) v.findViewById(R.id.tvCategoryName);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation.validateEdittext(etWarranty)) {

                    if (Validation.isValidEmail(et_replacement_policy.getText().toString())) {

                        if (Validation.isValidNumber(et_cash_on_delivery.getText().toString())) {

                            if (Validation.validateEdittext(et_free_installation)) {
                                String call_enquiry_url = getResources().getString(R.string.webservice_base_url) + "/enquiry";
                                call_enquiry_webservice(call_enquiry_url);

                            } else {

                                et_free_installation.setError("!Invalid Enquiry ");
                            }

                        } else {

                            et_cash_on_delivery.setError("!Invalid Mobile No ");
                        }

                    } else {
                        et_replacement_policy.setError("! Invalid Email");
                    }


                } else {

                    etWarranty.setError("!Invalid Name");


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
                .setBodyParameter("name", etWarranty.getText().toString().trim())
                .setBodyParameter("mobile", et_cash_on_delivery.getText().toString().trim())
                .setBodyParameter("short_des", et_free_installation.getText().toString().trim())
                .setBodyParameter("email", et_replacement_policy.getText().toString().trim())
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
