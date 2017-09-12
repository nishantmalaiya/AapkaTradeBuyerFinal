package com.aapkatrade.buyer.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class AddWalletMoneyActivity extends AppCompatActivity
{

    public static TextView tvWalletAmount;
     EditText edtAmount;
     Button buttonAddMoney;
     Context context;
     ProgressBarHandler progressBarHandler;
     AppSharedPreference appSharedPreference;
     String amount ,transaction_id,created_date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_wallet_money);

        context = AddWalletMoneyActivity.this;

        appSharedPreference = new AppSharedPreference(context);

        progressBarHandler = new ProgressBarHandler(context);

        Intent oIntent = getIntent();

        amount = oIntent.getExtras().getString("Amount");


      //  AndroidUtils.showToast(context,amount);

        setUpToolBar();

        setupLayout();
    }

    private void setupLayout()
    {
        tvWalletAmount = (TextView) findViewById(R.id.tvWalletAmount);

        edtAmount = (EditText) findViewById(R.id.edtAmount);

        tvWalletAmount.setText(amount);

        buttonAddMoney = (Button) findViewById(R.id.buttonAddMoney);

        buttonAddMoney.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!edtAmount.getText().toString().equals(""))
                {
                    Intent i = new Intent(AddWalletMoneyActivity.this, WalletPaymentActivity.class);
                    i.putExtra("FIRST_NAME",appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), ""));
                    i.putExtra("PHONE_NUMBER",appSharedPreference.getSharedPref(SharedPreferenceConstants.MOBILE.toString(), ""));
                    i.putExtra("EMAIL_ADDRESS",appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), ""));
                    i.putExtra("RECHARGE_AMT",edtAmount.getText().toString());
                    startActivity(i);

                }
                else
                {
                    AndroidUtils.showToast(context,"Please Enter Amount");
                }

            }
        });

    }

   /*
    private void callWebserviceAddWalletMoney() {

        progressBarHandler.show();

        Ion.with(context)
                .load(context.getResources().getString(R.string.webservice_base_url)+"/check_delivery")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin"))
                .setBodyParameter("user_type","")
                .setBodyParameter("user_type","")
                .setBodyParameter("user_type","")
                .setBodyParameter("user_type","")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null)
                        {

                            progressBarHandler.hide();
                            System.out.println("result--------------" + result);
                            String message = result.get("message").getAsString();
                            AndroidUtils.showToast(context, message);

                        }
                        else
                        {
                            progressBarHandler.hide();
                            AndroidUtils.showToast(context, "Server is not responding. Please try again.");
                        }

                    }
                });


    }


    */


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
        header_name.setText(getResources().getString(R.string.change_password_heading));
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
