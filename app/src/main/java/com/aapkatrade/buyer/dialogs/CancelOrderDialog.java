package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.BlankFragment;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.user_dashboard.order_list.order_details.OrderDetailsActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC17 on 16-May-17.
 */

@SuppressLint("ValidFragment")
public class CancelOrderDialog extends DialogFragment {
    ImageView dialog_close;
    Spinner spinnerCancelReason;
    ArrayList stateList = new ArrayList();
    EditText comments;
    ViewGroup container;
    ProgressDialogHandler progressDialogHandler;
    String orderid;
    Button submit;
    View v;
    int position;

    public CancelOrderDialog(String orderid, int position) {
        this.orderid = orderid;
        this.position = position;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentcancelreason, container, false);
        this.container = container;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initview(v);

        progressDialogHandler = new ProgressDialogHandler(getActivity());
        return v;
    }


    private void initview(View v) {


        dialog_close = (ImageView) v.findViewById(R.id.dialog_close_reason);
        comments = (EditText) v.findViewById(R.id.comment);
        submit = (Button) v.findViewById(R.id.submit);


///// set spinner adapter

        AndroidUtils.showErrorLog(getActivity(), "get Name", getActivity().getClass().getSimpleName());

        spinnerCancelReason = (Spinner) v.findViewById(R.id.spinnerCancelReason);

        stateList.add("--Select a Reason--");
        stateList.add("The delivery is delayed");
        stateList.add("Order placed by mistake");
        stateList.add("Expected delivery time is too long");
        stateList.add("Item price/shipping cost is too long");
        stateList.add("Need to change shipping address");
        stateList.add("Bought it from somewhere else");
        stateList.add("I'll buy later");
        stateList.add("I'm getting a better product offline");
        stateList.add("Any other Reason");


        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.black_textcolor_spinner, stateList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.black_textcolor_spinner);

        spinnerCancelReason.setAdapter(spinnerArrayAdapter);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getClass().getSimpleName().contains("OrderManagementActivity")) {

                    callCancelOrderWebservice();

                } else {

                    if(position==-1)
                    {
                        callCancelOrderWebservice();

                    }
                    else
                    {

                        callCancelSubOrderWebservice();

                    }




                }


            }
        });


    }

    private void callCancelOrderWebservice() {

        if (spinnerCancelReason.getSelectedItemPosition() != 0) {

            if (comments.getText().length() != 0) {
                progressDialogHandler.show();
                String cancel_reason_url = getResources().getString(R.string.webservice_base_url) + "/cancel_all_order";

                AndroidUtils.showErrorLog(getActivity(), "cancel order detail", orderid);
                AndroidUtils.showErrorLog(getActivity(), "reason", spinnerCancelReason.getSelectedItem().toString());
                AndroidUtils.showErrorLog(getActivity(), "comments", comments.getText().toString());

                Ion.with(getActivity())
                        .load(cancel_reason_url)
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("order_id", orderid)
                        .setBodyParameter("reason", spinnerCancelReason.getSelectedItem().toString())
                        .setBodyParameter("comments", comments.getText().toString())


                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {
                                progressDialogHandler.hide();

                                getDialog().dismiss();
                                String message = result.get("message").getAsString();
                                AndroidUtils.showErrorLog(getActivity(), message.toString());

                                if(position==-1)
                                {
                                    OrderDetailsActivity.commonInterface.getData(position);

                                }
                                else{

                                    AndroidUtils.showErrorLog(getActivity(), result.toString());
                                    BlankFragment.commonInterface.getData(position);

                                }





                            } else {

                                getDialog().dismiss();
                                progressDialogHandler.hide();
                            }


                        } else {
                            getDialog().dismiss();
                            progressDialogHandler.hide();


                        }


//

                    }
                });


            } else {

                AndroidUtils.showSnackBar((ViewGroup) v.findViewById(R.id.cardViewCancelDialog), "Please Enter Comments");
                comments.setError("Please Enter Comments");
            }


        } else {

            AndroidUtils.showSnackBar((ViewGroup) v.findViewById(R.id.cardViewCancelDialog), "Select Cancel Reason");


        }


    }


    private void callCancelSubOrderWebservice() {

        if (spinnerCancelReason.getSelectedItemPosition() != 0) {

            if (comments.getText().length() != 0) {
                progressDialogHandler.show();
                String cancel_reason_url = getResources().getString(R.string.webservice_base_url) + "/cancel_order";

                AndroidUtils.showErrorLog(getActivity(), "cancel order detail", orderid);
                AndroidUtils.showErrorLog(getActivity(), "reason", spinnerCancelReason.getSelectedItem().toString());
                AndroidUtils.showErrorLog(getActivity(), "comments", comments.getText().toString());

                Ion.with(getActivity())
                        .load(cancel_reason_url)
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("id", orderid)
                        .setBodyParameter("reason", spinnerCancelReason.getSelectedItem().toString())
                        .setBodyParameter("comments", comments.getText().toString())


                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {
                                progressDialogHandler.hide();

                                getDialog().dismiss();
                                String message = result.get("message").getAsString();
                                AndroidUtils.showErrorLog(getActivity(), message.toString());
                                OrderDetailsActivity.commonInterface.getData(position);





                            } else {

                                getDialog().dismiss();
                                progressDialogHandler.hide();
                            }


                        } else {
                            getDialog().dismiss();
                            progressDialogHandler.hide();


                        }


//               JsonObject res result.get("otp_id").getAsString();


                    }
                });


            } else {

                AndroidUtils.showSnackBar((ViewGroup) v.findViewById(R.id.cardViewCancelDialog), "Please Enter Comments");
                comments.setError("Please Enter Comments");
            }


        } else {

            AndroidUtils.showSnackBar((ViewGroup) v.findViewById(R.id.cardViewCancelDialog), "Select Cancel Reason");


        }


    }


}