package com.aapkatrade.buyer.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginDashboard extends AppCompatActivity {

    FrameLayout fl_seller, fl_buyer;
    AppSharedPreference app_sharedpreference;
    private Context context;
    TextView skip;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logindashboard);
        context = LoginDashboard.this;
        app_sharedpreference = new AppSharedPreference(this);
        setUpToolBar();
        Initview();

        fl_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app_sharedpreference.sharedPreferences != null)
                    app_sharedpreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(),SharedPreferenceConstants.USER_TYPE_SELLER.toString());

                Intent i = new Intent(LoginDashboard.this, LoginActivity.class);
                i.putExtra("user_login", "SELLER LOGIN");
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        fl_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app_sharedpreference.sharedPreferences != null)
                    app_sharedpreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(),SharedPreferenceConstants.USER_TYPE_BUYER.toString());
                Intent i = new Intent(LoginDashboard.this, LoginActivity.class);
                i.putExtra("user_login", "BUYER LOGIN");
                startActivity(i);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


    }

    private void Initview() {

        fl_seller = (FrameLayout) findViewById(R.id.fl_seller);
        fl_buyer = (FrameLayout) findViewById(R.id.fl_buyer);

        fl_seller = (FrameLayout) findViewById(R.id.fl_seller);


        skip = (TextView) findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        fl_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginDashboard.this, HomeActivity.class);

                startActivity(i);
            }
        });


    }

    private void setUpToolBar()
    {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        findViewById(R.id.logoWord).setVisibility(View.INVISIBLE);
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
}
