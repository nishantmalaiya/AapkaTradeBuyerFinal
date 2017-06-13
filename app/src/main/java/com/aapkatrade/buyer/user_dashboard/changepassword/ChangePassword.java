package com.aapkatrade.buyer.user_dashboard.changepassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.ConnectivityNotFound;
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class ChangePassword extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private LinearLayout linearChangePassword;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressHandler;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = ChangePassword.this;
        progressHandler = new ProgressBarHandler(context);
        setUpToolBar();
        initView();


    }


    private void showMessage(String message) {
        AndroidUtils.showSnackBar(linearChangePassword, message);
    }


    private void callChangePasswordWebService() {

        progressHandler.show();

        System.out.println("user_id------------" + appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()) + "usertype-----buyer" + "old password--" + etOldPassword.getText().toString() + "Confirm password----" + etConfirmPassword.getText().toString());

        if (ConnetivityCheck.isNetworkAvailable(ChangePassword.this)) {


            Log.e("changePassword", getResources().getString(R.string.webservice_base_url) + "/changePassword");
            Ion.with(ChangePassword.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/changePassword")
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "buyer")
                    .setBodyParameter("old_password", etOldPassword.getText().toString())
                    .setBodyParameter("new_password", etNewPassword.getText().toString())
                    .setBodyParameter("confirm_password", etConfirmPassword.getText().toString())
                    .setBodyParameter("id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressHandler.hide();
                            if (result == null) {
                                AndroidUtils.showErrorLog(context, "change_password_error result null", e.toString());
                            } else {
                                JsonObject jsonObject = result.getAsJsonObject();
                                String message = jsonObject.get("message").getAsString();
                                AndroidUtils.showErrorLog(context, "data_change_password", result.toString());
                                showMessage(message);
                                if (message.equalsIgnoreCase("Please enter correct current password")) {
                                    etOldPassword.setError(message);
                                } else if (message.equalsIgnoreCase("New password and confirm password does not match")) {
                                    etConfirmPassword.setError(message);
                                } else if (message.equalsIgnoreCase("Updated succesfully")) {
                                    AndroidUtils.showToast(context, message);
                                    Intent intent = new Intent(ChangePassword.this, HomeActivity.class);
                                    intent.putExtra("callerActivity", ChangePassword.class.getName());
                                    startActivity(intent);
                                }
                            }
                        }

                    });
        } else {
            Intent intent = new Intent(ChangePassword.this, ConnectivityNotFound.class);
            intent.putExtra("callerActivity", ChangePassword.class.getName());
            startActivity(intent);
        }


    }

    private void initView() {
        appSharedPreference = new AppSharedPreference(getApplicationContext());
        linearChangePassword = (LinearLayout) findViewById(R.id.linearChangePassword);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etNewPasswordConfirm);
        TextView saveNewPasswordButton = (TextView) findViewById(R.id.saveNewPasswordButton);
        saveNewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation.isNonEmptyStr(etOldPassword.getText().toString())) {
                    if (Validation.isNonEmptyStr(etConfirmPassword.getText().toString())) {
                        if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                            callChangePasswordWebService();
                        } else {
                            showMessage("Old and confirm password does not match");
                        }
                    } else {
                        showMessage("Please Enter Confirm Password");
                    }
                } else {
                    showMessage("Please Enter Old Password");
                }
            }
        });
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.logoWord).setVisibility(View.GONE);
        TextView header_name = (TextView) findViewById(R.id.header_name);
        header_name.setVisibility(View.VISIBLE);
        header_name.setText(getResources().getString(R.string.change_password_heading));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
