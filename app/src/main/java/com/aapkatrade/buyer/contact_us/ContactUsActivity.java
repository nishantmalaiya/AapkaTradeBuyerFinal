package com.aapkatrade.buyer.contact_us;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ContactUsActivity extends AppCompatActivity {

    EditText etSubject, etUserName, etMobileNo, etEmail, etQuery;
    Button buttonSave;
    ProgressBarHandler progress_handler;
    ImageView imgPhone, imgEmail;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_us);
        context = ContactUsActivity.this;
        progress_handler = new ProgressBarHandler(context);
        setUpToolBar();
        initView();
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String subject = etSubject.getText().toString();
                String username = etUserName.getText().toString();
                String mobileno = etMobileNo.getText().toString();
                String email = etEmail.getText().toString();
                String query = etQuery.getText().toString();
                if (!subject.equals("")) {

                    if (!username.equals("")) {

                        if (!mobileno.equals("")) {

                            if (mobileno.length() == 10) {

                                if (!email.equals("")) {

                                    if (Validation.isValidEmail(email)) {
                                        if (!query.equals("")) {

                                            callContactUsWebService(subject, username, mobileno, email, query);

                                        } else {

                                            AndroidUtils.showToast(context, "Please Enter Query");
                                        }

                                    } else {
                                        AndroidUtils.showToast(context, "Please Enter Valid Email Address");
                                    }
                                } else {
                                    AndroidUtils.showToast(context, "Please Enter Email Address");
                                }

                            } else {
                                AndroidUtils.showToast(context, "Please Enter 10 digit Mobile Number");
                            }

                        } else {
                            AndroidUtils.showToast(context, "Please Enter Mobile Number");
                        }

                    } else {
                        AndroidUtils.showToast(context, "Please Enter User Name");
                    }

                } else {
                    AndroidUtils.showToast(context, "Please Enter Subject");
                }

            }
        });

    }


    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView header_name = (TextView) findViewById(R.id.header_name);
        //header_name.setVisibility(View.VISIBLE);
        header_name.setText(getResources().getString(R.string.service_enquiry_list_heading));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    }

    private void initView() {
        imgPhone = (ImageView) findViewById(R.id.imgPhone);
        imgEmail = (ImageView) findViewById(R.id.imgEmail);
        AndroidUtils.setImageColor(imgEmail, context, R.color.black);
        AndroidUtils.setImageColor(imgPhone, context, R.color.black);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etQuery = (EditText) findViewById(R.id.etQuery);
        buttonSave = (Button) findViewById(R.id.buttonSave);


    }


    private void callContactUsWebService(String subject, String username, String mobile, String email, String query) {
        progress_handler.show();
        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url) + "/contact_us")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_name", username)
                .setBodyParameter("email", email)
                .setBodyParameter("mobile_num", mobile)
                .setBodyParameter("message", query)
                .setBodyParameter("subject", subject)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {

                            progress_handler.hide();
                        } else {
                            JsonObject jsonObject = result.getAsJsonObject();


                            if (jsonObject.get("error").getAsString().contains("false")) {
                                String message = jsonObject.get("message").getAsString();
                                AndroidUtils.showToast(context, message);

                            }
                            Log.e("message", jsonObject.toString());

                            etSubject.setText("");
                            etUserName.setText("");
                            etMobileNo.setText("");
                            etEmail.setText("");
                            etQuery.setText("");

                            progress_handler.hide();

                        }
                    }
                });
    }


}
