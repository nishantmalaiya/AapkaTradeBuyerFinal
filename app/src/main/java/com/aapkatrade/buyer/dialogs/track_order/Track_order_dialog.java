package com.aapkatrade.buyer.dialogs.track_order;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.login.ActivityOTPVerify;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


/**
 * A simple {@link Fragment} subclass.
 */
public class Track_order_dialog extends DialogFragment {
    ImageView dialog_close;
    EditText tracking_id;
    Button validate_order_id;
    ProgressBarHandler progressBarHandler;
    TextToSpeech t1;

    public Track_order_dialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_track_order_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);

        initview(v);
        progressBarHandler = new ProgressBarHandler(getActivity());

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        validate_order_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation.validateEdittext(tracking_id)) {
                    call_Validate_order_webservice();
                } else {
                    AndroidUtils.showSnackBar(container, "!Enter correct Order id");
                }

            }
        });


        return v;
    }


    private void call_Validate_order_webservice() {

        progressBarHandler.show();
        String track_order_url = getString(R.string.webservice_base_url) + "/track_order";


        Ion.with(getActivity())
                .load(track_order_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("ORDER_ID", "AT210417060350")
                .setBodyParameter("client_id", "12")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {


                if (result != null) {
                    String error = result.get("error").getAsString();
                    if (error.contains("false")) {


                        String otp_id = result.get("result").getAsJsonObject().get("otp_id").getAsString();


                        Intent go_to_activity_otp_verify = new Intent(getActivity(), ActivityOTPVerify.class);
                        go_to_activity_otp_verify.putExtra("class_name", "Track_order_dialog");
                        go_to_activity_otp_verify.putExtra("otp_id", otp_id);
                        startActivity(go_to_activity_otp_verify);


                        Log.e("otp_id", getActivity().getClass().getName());


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

    private void initview(View v) {


        dialog_close = (ImageView) v.findViewById(R.id.dialog_close);

        tracking_id = (EditText) v.findViewById(R.id.et_orderid);
        validate_order_id = (Button) v.findViewById(R.id.btn_trackorder);
        tracking_id.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

    }

}
