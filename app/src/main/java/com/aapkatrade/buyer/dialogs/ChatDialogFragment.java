package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.chat.ChatActivity;
import com.aapkatrade.buyer.chat.ChatValidationDatas;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.uicomponent.ChatUserTypeSelection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static com.aapkatrade.buyer.R.id.etyourquestion;

@SuppressLint("ValidFragment")
public class ChatDialogFragment extends DialogFragment {


    private Context context;


    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;
    int i = 0, j = 0;

    LinearLayout layoutcontainer, layoutcontainer1, layoutcontainer2;
    ImageView img_close_dialog;
    ChatUserTypeSelection chatUserTypeSelection1, chatUserTypeSelection2;

    TextView tvSubHeader1, tvSubHeader2, welcome_user;
    EditText EmailOrPhone, Name, Questions;
    ProgressBarHandler progressBarHandler;
    ArrayList<ChatValidationDatas> ValidateUserList = new ArrayList<>();
    RadioButton radioButton1, radioButton2;
    RelativeLayout buyer_seller_type;
    String firebasetoken;
    Button btn_start_chat;
    AppSharedPreference appSharedPreference;

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
        SharedPreferences pref = getActivity().getSharedPreferences(AppSharedPreference.app_pref, 0);
        firebasetoken = pref.getString(SharedPreferenceConstants.FIREBASE_REG_ID.toString(), null);
        return v;
    }

    private void initView(View v) {
        btn_start_chat = (Button) v.findViewById(R.id.btn_start_chat);
        appSharedPreference = new AppSharedPreference(getActivity());
        img_close_dialog = (ImageView) v.findViewById(R.id.img_close_dialog);
        chatUserTypeSelection1 = (ChatUserTypeSelection) v.findViewById(R.id.chatusertypeselction1);
        radioButton1 = (RadioButton) chatUserTypeSelection1.findViewById(R.id.radio_usertypeselection);
        tvSubHeader1 = (TextView) chatUserTypeSelection1.findViewById(R.id.tv_usertype);
        tvSubHeader1.setText(getResources().getString(R.string.buyer));
        tvSubHeader1.setTextColor(Color.WHITE);
        tvSubHeader1.setTextSize(12);
        welcome_user = (TextView) v.findViewById(R.id.welcome_user);
        layoutcontainer1 = (LinearLayout) chatUserTypeSelection1.findViewById(R.id.circleImageLinearLayout);
        buyer_seller_type = (RelativeLayout) v.findViewById(R.id.buyer_seller_type);
        chatUserTypeSelection2 = (ChatUserTypeSelection) v.findViewById(R.id.chatusertypeselction2);

        radioButton2 = (RadioButton) chatUserTypeSelection2.findViewById(R.id.radio_usertypeselection);

        tvSubHeader2 = (TextView) chatUserTypeSelection2.findViewById(R.id.tv_usertype);

        tvSubHeader2.setText(getResources().getString(R.string.seller));
        tvSubHeader2.setTextColor(Color.WHITE);
        tvSubHeader2.setTextSize(12);

        layoutcontainer2 = (LinearLayout) chatUserTypeSelection2.findViewById(R.id.circleImageLinearLayout);


        layoutcontainer = (LinearLayout) v.findViewById(R.id.chat_container);
        EmailOrPhone = (EditText) v.findViewById(R.id.etEmail);
        Name = (EditText) v.findViewById(R.id.etname);
        Questions = (EditText) v.findViewById(etyourquestion);
        progressBarHandler = new ProgressBarHandler(getActivity());
        callEvents();
        ApplyValidation();


    }

    private void ApplyValidation() {


        EmailOrPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Validation.isValidEmail(s.toString()) || Validation.isValidNumber(s.toString(), Validation.getNumberPrefix(s.toString()))) {


                    callWebserviceValidateUser(EmailOrPhone.getText().toString());


                } else {
                    EmailOrPhone.setError("!Invalid Mobile No/Email");
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
                                if (jsonArray.size() == 2) {
                                    tvSubHeader1.setText(ValidateUserList.get(1).username + "\n( " + getResources().getString(R.string.buyer) + ")");
                                    tvSubHeader2.setText(ValidateUserList.get(0).username + "\n( " + getResources().getString(R.string.seller) + ")");
                                    if (buyer_seller_type.getVisibility() == View.GONE) {
                                        buyer_seller_type.setVisibility(View.VISIBLE);
                                        if (radioButton1.isChecked()) {
                                            Name.setText(ValidateUserList.get(1).username);

                                        } else {

                                            Name.setText(ValidateUserList.get(0).username);
                                        }

                                    } else {


                                        buyer_seller_type.setVisibility(View.GONE);
                                    }
                                } else {
                                    Name.setText(ValidateUserList.get(0).username);
                                    welcome_user.setText("Welcome " + ValidateUserList.get(0).username);
                                    buyer_seller_type.setVisibility(View.GONE);

                                }
                                AndroidUtils.showErrorLog(getActivity(), ValidateUserList.toString());


                            } else {
                                if (buyer_seller_type.getVisibility() == View.VISIBLE) {
                                    buyer_seller_type.setVisibility(View.GONE);

                                }
                                AndroidUtils.showErrorLog(getActivity(), result.toString());
                            }


                        } else {


                            AndroidUtils.showErrorLog(getActivity(), e.toString());

                        }


                    }
                });
    }

    private void callEvents() {
        btn_start_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isNonEmptyStr(EmailOrPhone.getText().toString()) || Validation.isNonEmptyStr(Name.getText().toString()) || Validation.isNonEmptyStr(Questions.getText().toString())) {
                    if (Validation.isValidEmail(EmailOrPhone.getText().toString()) || Validation.isNonEmptyStr(Name.getText().toString())) {


                        callChatInitiateWebService(EmailOrPhone.getText().toString(), Name.getText().toString(), Questions.getText().toString());


                    } else {
                        EmailOrPhone.setError("!Invalid Mobile No/Email");
                    }


                } else {


                    AndroidUtils.showToast(context, "!Invalid Input Entries");
                }


            }
        });

        img_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        chatUserTypeSelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {

                    radioButton1.setChecked(true);

                    radioButton2.setChecked(false);
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));


                    i = 1;
                } else {
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(true);
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));

                    i = 0;

                }

            }
        });


        chatUserTypeSelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j == 0) {

                    radioButton2.setChecked(true);

                    radioButton1.setChecked(false);
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));

                    j = 1;
                } else {
                    radioButton2.setChecked(false);
                    radioButton1.setChecked(true);
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));
                    j = 0;

                }

            }
        });
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    radioButton2.setChecked(false);
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));
                } else {

                }

            }
        });


        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    radioButton1.setChecked(false);
                    layoutcontainer2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_white));
                    layoutcontainer1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.circle_dark_green));

                } else {

                }

            }
        });


    }

    private void callChatInitiateWebService(final String EmailOrPhone, final String Name, final String Questions) {


        progressBarHandler.show();

        String urlValidateUser = getActivity().getResources().getString(R.string.webservice_base_url) + "/login_chat";


        final String FirebaseTokenId = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIREBASE_REG_ID.toString());


        Ion.with(context)
                .load(urlValidateUser)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("emailphone", EmailOrPhone)
                .setBodyParameter("name", Name)
                .setBodyParameter("message", Questions)
                .setBodyParameter("token", FirebaseTokenId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {

                            AndroidUtils.showErrorLog(getActivity(), result.toString());
                            progressBarHandler.hide();


                            if (result.get("error").getAsString().contains("false")) {


                                String chatid = result.get("chat_id").getAsString();

                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("chatid", chatid);

                                i.putExtra("emailphone", EmailOrPhone);
                                i.putExtra("token", FirebaseTokenId);
                                i.putExtra("name", Name);
                                i.putExtra("message", Questions);
                                i.putExtra("className", getActivity().getClass().getSimpleName());


                                startActivity(i);
                            }

                        } else {
                            AndroidUtils.showErrorLog(getActivity(), e.toString());
                            progressBarHandler.hide();
                        }

                    }
                });


    }


}
