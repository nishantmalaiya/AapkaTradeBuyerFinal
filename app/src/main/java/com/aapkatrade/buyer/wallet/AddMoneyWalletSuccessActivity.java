package com.aapkatrade.buyer.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.home.HomeActivity;

public class AddMoneyWalletSuccessActivity extends AppCompatActivity
{

    Context context;
    String amount,transaction_id,created_date;
    Button buttonAmount;
    TextView txtDate,txtBankTransactionId;
    private boolean isSuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_money_wallet_success);

        setUpToolBar();

        Intent oIntent = getIntent();

        if (getIntent() != null)
        {
                amount = oIntent.getExtras().getString("transaction_amount");
                transaction_id =oIntent.getExtras().getString("transaction_id");
                created_date =oIntent.getExtras().getString("transaction_date");

        }

        setup_layout();



    }

    private void setup_layout()
    {
        buttonAmount = (Button) findViewById(R.id.buttonAmount);

        buttonAmount.setText(getResources().getText(R.string.rupay_text)+amount);

        txtDate = (TextView) findViewById(R.id.txtDate);

        txtDate.setText(created_date);

        txtBankTransactionId = (TextView) findViewById(R.id.txtBankTransactionId);

        txtBankTransactionId.setText("Bank Txn ID: "+transaction_id);


    }

    private void setUpToolBar()
    {
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
