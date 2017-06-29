package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.Chat.ChatValidationDatas;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ChatDialogFragment extends DialogFragment {


    private Context context;


    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;

    RelativeLayout layoutcontainer;
    ImageView img_close_dialog;


    EditText EmailOrPhone, Name, Questions;
    ProgressBarHandler progressBarHandler;
    ArrayList<ChatValidationDatas> ValidateUserList = new ArrayList<>();

    public ChatDialogFragment(Context context) {
        super();

        this.context = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        viewgrp = container;
        v = inflater.inflate(R.layout.fragment_chat_dialog, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initView(v);
        AndroidUtils.setGradientColor(layoutcontainer, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.datanotfound_gradient_bottom), ContextCompat.getColor(context, R.color.datanotfound_gradient_top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);
        progressDialogHandler = new ProgressDialogHandler(getActivity());

        return v;
    }

    private void initView(View v) {

        img_close_dialog = (ImageView) v.findViewById(R.id.img_close_dialog);
        layoutcontainer = (RelativeLayout) v.findViewById(R.id.chat_container);
        EmailOrPhone = (EditText) v.findViewById(R.id.etEmail);
        Name = (EditText) v.findViewById(R.id.etname);
        Questions = (EditText) v.findViewById(R.id.etyourquestion);
        progressBarHandler = new ProgressBarHandler(getActivity());
        callEvents();
        ApplyValidation();


    }

    private void ApplyValidation() {


        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (EmailOrPhone.getText().length() != 0) {


                    callWebserviceValidateUser(EmailOrPhone.getText().toString());


                } else {


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void callWebserviceValidateUser(String s) {

        progressBarHandler.show();

        String urlValidateUser = getActivity().getResources().getString(R.string.webservice_base_url) + "/chat_user";


        Ion.with(context)
                .load(urlValidateUser)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("emailphone", s)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        ValidateUserList.clear();
                        progressBarHandler.hide();
                        if (result != null) {

                            if (result.get("status").getAsString().contains("true")) {

                                AndroidUtils.showErrorLog(getActivity(), "" + result.toString());

                                JsonArray jsonArray = result.getAsJsonArray("result");

                                for (int i = 0; i < jsonArray.size(); i++) {


                                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);

                                    String userid = jsonObject.get("id").getAsString();


                                    String usertype = jsonObject.get("type").getAsString();
                                    String username = jsonObject.get("name").getAsString();
                                    String profile_pic = jsonObject.get("profile_pic").getAsString();


                                    ValidateUserList.add(new ChatValidationDatas(userid, usertype, username, profile_pic));

                                }


                            } else {


                            }


                        } else {


                            AndroidUtils.showErrorLog(getActivity(), e.toString());

                        }


                    }
                });
    }

    private void callEvents() {

        layoutcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }


}
