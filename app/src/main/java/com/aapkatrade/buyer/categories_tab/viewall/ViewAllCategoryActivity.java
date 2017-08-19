package com.aapkatrade.buyer.categories_tab.viewall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.StateUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;

public class ViewAllCategoryActivity extends AppCompatActivity {

    private Context context;
    private AppSharedPreference appSharedPreference;
    public static TextView tvCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_category);
        context = ViewAllCategoryActivity.this;
        appSharedPreference = new AppSharedPreference(context);
        setUpToolBar();


    }



    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(homeIcon!=null)
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
//        AndroidUtils.showErrorLog(context, "---------> list of state ", StateUtils.getStateList(context));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        final MenuItem alertMenuItem = menu.findItem(R.id.cart_total_item);
        final MenuItem login = menu.findItem(R.id.login);
        login.setVisible(false);
        RelativeLayout badgeLayout = (RelativeLayout) alertMenuItem.getActionView();
        tvCartCount = (TextView) badgeLayout.findViewById(R.id.tvCartCount);
        tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        if(appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString()).equals("2")){
            alertMenuItem.setVisible(false);
            AndroidUtils.showErrorLog(context, "cart visibility gone");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.cart_total_item:
                if (appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0) == 0) {
                    AndroidUtils.showToast(context, "My Cart have no items please add items in cart");
                } else {
                    Intent intent = new Intent(context, MyCartActivity.class);
                    startActivity(intent);
                }

                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }


}
