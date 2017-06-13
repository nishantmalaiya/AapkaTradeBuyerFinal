package com.aapkatrade.buyer.payment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;

public class PaymentCompletionActivity extends AppCompatActivity {
    private Context context;
    private TextView tvStatusTitle, tvDone, tvStatusMsg, tvHeaderTransaction, tvHeaderAmountPaid, tvSubHeaderTransaction, tvSubHeaderAmountPaid, tvReceiptNo;
    private LinearLayout circleTile1Layout, circleTile2Layout, paymentCompletionRootLayout;
    private ImageView tickImageView;
    private RelativeLayout rlDoneLayout;
    private ImageView circleImageView1, circleImageView2;
    private boolean isSuccess = false;
    private double transactionAmount;
    private String transactionNo, receiptNo;
    private AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_completion);
        context = PaymentCompletionActivity.this;
        if (getIntent() != null) {
            if (getIntent().getExtras().get("isSuccess").toString().equals("true")) {
                isSuccess = true;
            }
            if (isSuccess) {
                transactionAmount = Double.parseDouble(getIntent().getExtras().get("vpc_Amount").toString())/100;
                transactionNo = getIntent().getExtras().get("vpc_TransactionNo").toString();
                receiptNo = getIntent().getExtras().get("vpc_ReceiptNo").toString();
            }
        }
        setUpToolBar();
        initView();
        rlDoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });

        if (isSuccess) {
            tvSubHeaderTransaction.setText(transactionNo);
            tvSubHeaderAmountPaid.setText(new StringBuilder(getString(R.string.rupay_text)).append("  ").append(transactionAmount));
            tvReceiptNo.setText(Validation.isEmptyStr(receiptNo) ? "RECEIPT" : new StringBuilder("Receipt No : ").append(receiptNo));
            appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0);
        } else {
            tvStatusTitle.setText(getString(R.string.payment_unsuccess));
            tvDone.setText("Try Again");
            findViewById(R.id.doneIcon).setVisibility(View.GONE);
            paymentCompletionRootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.payment_not_received));
            AndroidUtils.setBackgroundStroke(rlDoneLayout, context, R.color.green, 10, 2);
            tickImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cross));
            tvSubHeaderTransaction.setText(Validation.isEmptyStr(transactionNo)?"Not Available": transactionNo);
            tvSubHeaderAmountPaid.setText(Validation.isEmptyStr(String.valueOf(transactionAmount))?"Not Available": new StringBuilder(getString(R.string.rupay_text)).append("  ").append(transactionAmount));
            tvReceiptNo.setText(Validation.isEmptyStr(receiptNo) ? "Receipt Not Available" : new StringBuilder("Receipt No : ").append(receiptNo));
        }
    }

    private void initView() {
        appSharedPreference = new AppSharedPreference(context);
        tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        tvStatusMsg = (TextView) findViewById(R.id.tvStatusMsg);
        tickImageView = (ImageView) findViewById(R.id.tickImageView);

        circleTile1Layout = (LinearLayout) findViewById(R.id.circleTile1Layout);
        circleTile2Layout = (LinearLayout) findViewById(R.id.circleTile2Layout);
        circleImageView1 = (ImageView) circleTile1Layout.findViewById(R.id.circleImageView);
        circleImageView2 = (ImageView) circleTile2Layout.findViewById(R.id.circleImageView);

        circleImageView1.setImageResource(R.drawable.ic_transaction);
        tvHeaderTransaction = (TextView) circleTile1Layout.findViewById(R.id.tvHeader);
        tvSubHeaderTransaction = (TextView) circleTile1Layout.findViewById(R.id.tvSubHeader);
        tvHeaderTransaction.setText("Transaction ID");
        tvSubHeaderTransaction.setText("APKTRADE23548464");

        circleImageView2.setImageResource(R.drawable.ic_amount_paid2);
        tvHeaderAmountPaid = (TextView) circleTile2Layout.findViewById(R.id.tvHeader);
        tvSubHeaderAmountPaid = (TextView) circleTile2Layout.findViewById(R.id.tvSubHeader);
        ((TextView) circleTile2Layout.findViewById(R.id.tvHeader)).setText("Amount Paid");
        ((TextView) circleTile2Layout.findViewById(R.id.tvSubHeader)).setText("APKTRADE23548464");
        tvDone = (TextView) findViewById(R.id.tvDone);
        paymentCompletionRootLayout = (LinearLayout) findViewById(R.id.paymentCompletionRootLayout);

        tvReceiptNo = (TextView) findViewById(R.id.tvReceiptNo);
        rlDoneLayout = (RelativeLayout) findViewById(R.id.rlDoneLayout);
        AndroidUtils.setBackgroundSolid(rlDoneLayout, context, R.color.golden_text, 10, GradientDrawable.RECTANGLE);
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        ImageView back_imagview = (ImageView) findViewById(R.id.back_imagview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });
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

    private void callHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        callHomeActivity();
    }
}
