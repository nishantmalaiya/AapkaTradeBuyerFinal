package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;

public class BillPaymentActivity extends AppCompatActivity {
    Context context;
    private AppSharedPreference appSharedPreference;
    private TextView tvStatusTitle, tvStatusMsg, tvHeaderTransaction, tvSubHeaderTransaction,
            tvHeaderAmountPaid, tvSubHeaderAmountPaid, tvReceiptNo, tvDone;
    private ImageView tickImageView, circleImageView1, circleImageView2;
    private LinearLayout circleTile2Layout, circleTile1Layout, paymentCompletionRootLayout;
    private RelativeLayout rlDoneLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        context = BillPaymentActivity.this;
        setUpToolBar();
        initView();
    }


    private void initView() {
        appSharedPreference = new AppSharedPreference(context);
        tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        tvStatusMsg = (TextView) findViewById(R.id.tvStatusMsg);
        tickImageView = (ImageView) findViewById(R.id.tickImageView);


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

}
