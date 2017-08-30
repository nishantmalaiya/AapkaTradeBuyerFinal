package com.aapkatrade.buyer.categories_tab.viewall;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.viewall.adapter.ViewAllCategoriesAdapter;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.home.navigation.entity.SubCategory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;

import java.util.ArrayList;

public class ViewAllCategoryActivity extends AppCompatActivity {

    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    public static TextView tvCartCount;
    private ImageView icListOrGrid;
    private JellyToggleButton toggleButton;
    private boolean isList = true;
    private ArrayList<Category> listDataHeader = new ArrayList<>();
    private RecyclerView recyclerView;
    public static GridLayoutManager gridLayoutManager;
    private ViewAllCategoriesAdapter viewAllCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_category);
        context = ViewAllCategoryActivity.this;
        setUpToolBar();
        initView();


    }

    private void initView() {
        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);

        icListOrGrid = (ImageView) findViewById(R.id.ic_list_or_grid);
        toggleButton = (JellyToggleButton) findViewById(R.id.toggle_button);
        toggleButton.setText("List", "Grid");
        toggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float v, State state, JellyToggleButton jellyToggleButton) {
                if (state.equals(State.RIGHT)) {
                    icListOrGrid.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_list_view));
                    gridLayoutManager.setSpanCount(1);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(viewAllCategoriesAdapter);
                } else if (state.equals(State.LEFT)) {
                    icListOrGrid.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_grid_view));
                    if(Tabletsize.isTablet(context)){
                        gridLayoutManager.setSpanCount(3);
                    } else {
                        gridLayoutManager.setSpanCount(3);
                    }
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(viewAllCategoriesAdapter);

                }
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(Tabletsize.isTablet(context)){
            gridLayoutManager = new GridLayoutManager(context, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }


        getCategory();

    }


    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (homeIcon != null)
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
        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString()).equals("2")) {
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


    private void getCategory() {
        listDataHeader.clear();
        progressBarHandler.show();
        AndroidUtils.showErrorLog(context, "data", "getCategory Entered");
        Ion.with(context)
                .load(getString(R.string.webservice_base_url).concat("/dropdown"))
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "category")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject data) {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, "data", "getCategory result" + data);
                        if (data != null) {
                            JsonObject jsonObject = data.getAsJsonObject();
                            JsonArray jsonResultArray = jsonObject.getAsJsonArray("result");

                            listDataHeader = new ArrayList<>();
                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                listDataHeader.add(new Category(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString(),jsonObject1.get("cat_icon").getAsString()));
                            }
                            if(viewAllCategoriesAdapter == null){
                                viewAllCategoriesAdapter = new ViewAllCategoriesAdapter(context, listDataHeader);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(viewAllCategoriesAdapter);
                            } else {
                                viewAllCategoriesAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
    }


}
