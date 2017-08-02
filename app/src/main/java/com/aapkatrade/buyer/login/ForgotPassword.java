package com.aapkatrade.buyer.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.smsreceiver.SmsListener;
import com.aapkatrade.buyer.smsreceiver.SmsReceiver;



public class ForgotPassword extends AppCompatActivity
{

    private CoordinatorLayout activity_forgot__password;
    private Context context;
    private ForgotPasswordFragment forgot_password_fragment;
    private ResetPasswordFragment reset_passwordFragment;
    String class_index, otp_id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        context = ForgotPassword.this;


        class_index = getIntent().getStringExtra("forgot_index");
        Log.e("class_index", "" + class_index);
        forgot_password_fragment = new ForgotPasswordFragment();

        setUpToolBar();
        setupForgetpassword(class_index);


        setupForgetpassword(class_index);
        setUpToolBar();
        initView();


    }

    public ForgotPassword() {
    }

    private void setupForgetpassword(String class_index) {
        if (class_index.contains("0")) {
            if (forgot_password_fragment == null) {
                forgot_password_fragment = new ForgotPasswordFragment();
            }
            String tagName = forgot_password_fragment.getClass().getName();


            replaceFragment(forgot_password_fragment, tagName);


        } else {

            otp_id = getIntent().getStringExtra("otp_id");
            if (reset_passwordFragment == null) {
                reset_passwordFragment = new ResetPasswordFragment();
                Bundle b = new Bundle();
                b.putString("otp_id", otp_id);
                reset_passwordFragment.setArguments(b);
            }
            String tagName = reset_passwordFragment.getClass().getName();


            replaceFragment(reset_passwordFragment, tagName);


        }


    }


    private void initView() {


        activity_forgot__password = (CoordinatorLayout) findViewById(R.id.activity_forgot__password);


    }


    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        AndroidUtils.setBackgroundSolid(toolbar, context, R.color.transparent, 0, GradientDrawable.RECTANGLE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void replaceFragment(Fragment newFragment, String tag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_forgot_password_container, newFragment, tag).addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        finish();

    }


}
