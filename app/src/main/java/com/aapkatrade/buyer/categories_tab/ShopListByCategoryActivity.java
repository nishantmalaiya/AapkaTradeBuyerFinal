package com.aapkatrade.buyer.categories_tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.State;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.filter.FilterDialog;
import com.aapkatrade.buyer.filter.entity.FilterObject;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.search.Search;
import com.aapkatrade.buyer.service.GpsLocationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class ShopListByCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoriesListAdapter categoriesListAdapter;
    private ArrayList<CategoriesListData> shopArrayListByCategory = new ArrayList<>();
    ProgressBarHandler progressBarHandler;
    private CoordinatorLayout layout_container;
    private static String categoryId, latitude = "0.0", longitude = "0.0";
    private AppSharedPreference appSharedPreference;
    private ArrayList<State> productAvailableStateList = new ArrayList<>();
    private Context context;
    private TextView toolbarRightText, listfootername, tvListQuantity;
    private ArrayMap<String, ArrayList<FilterObject>> filterHashMap = null;
    private ViewGroup view;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    public static TextView tvCartCount, tvfilter;
    private int categoryListActivity = 1;
    CardView cardviewListContainer;
    GpsLocationService gpsLocationService;
    ImageButton btnSearch;
    RelativeLayout rlTryagain, llDataNotFound;

    com.aapkatrade.buyer.uicomponent.customcardview.CustomCardViewHeader customCardViewHeader_business_detail, customCardViewHeader_personal_detail, customCardViewHeader_newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list_by_category);
        appSharedPreference = new AppSharedPreference(ShopListByCategoryActivity.this);
        context = ShopListByCategoryActivity.this;
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            latitude = b.getString("latitude");
            longitude = b.getString("longitude");
            categoryId = b.getString("category_id");
            AndroidUtils.showErrorLog(context, "_______________latitude" + latitude + "longitude" + longitude + "categoryId" + categoryId);
        }
        setUpToolBar();
        initView();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        getShopListData("0");
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        page = page + 1;
                        getShopListData(String.valueOf(page));
                    }
                }
            }

        });

    }

    private void initView() {
        view = (ViewGroup) findViewById(android.R.id.content);
        cardviewListContainer = (CardView) view.findViewById(R.id.cardview_list_container);
        llDataNotFound = (RelativeLayout) view.findViewById(R.id.ll_data_not_found);
        btnSearch = (ImageButton) view.findViewById(R.id.btnsearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManagerCheck locationManagerCheck = new LocationManagerCheck(context);
                if (locationManagerCheck.isLocationServiceAvailable()) {
                    progressBarHandler.show();
                    gpsLocationService = new GpsLocationService(context);
                    if (gpsLocationService.canGetLocation()) {
                       /* double latitude = gpsLocationService.getLatitude();
                        double longitude = gpsLocationService.getLongitude();
                        String statename = gpsLocationService.getStaeName();*/
                        Intent intentAsync = new Intent(ShopListByCategoryActivity.this, Search.class);
                        intentAsync.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentAsync.putExtra("classname", context.getClass().getSimpleName());
                        intentAsync.putExtra("state_name", "");
                        intentAsync.putExtra("latitude", String.valueOf(0.0));
                        intentAsync.putExtra("longitude", String.valueOf(0.0));
                        startActivity(intentAsync);

                        progressBarHandler.hide();
                    } else {
                        gpsLocationService.showSettingsAlert();
                    }
                } else {
                    locationManagerCheck.createLocationServiceError(ShopListByCategoryActivity.this);
                }
            }
        });


        AndroidUtils.setGradientColor(llDataNotFound, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.datanotfound_gradient_bottom), ContextCompat.getColor(context, R.color.datanotfound_gradient_top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);

        rlTryagain = (RelativeLayout) view.findViewById(R.id.rl_tryagain);

        AndroidUtils.setBackgroundSolid(rlTryagain, context, R.color.listing_not_found_color, 15, GradientDrawable.RECTANGLE);

        rlTryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShopListData("0");
            }
        });
        progressBarHandler = new ProgressBarHandler(this);
        layout_container = (CoordinatorLayout) view.findViewById(R.id.layout_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        listfootername = (TextView) view.findViewById(R.id.listfootername);
        tvListQuantity = (TextView) view.findViewById(R.id.tv_list_quantity);
    }

    private void getShopListData(final String pageNumber) {
        State state = new State("-1", "Select State", "0");
        productAvailableStateList.add(state);
        AndroidUtils.showErrorLog(context, shopArrayListByCategory.size() + "^^^^^^^^^");
        AndroidUtils.showErrorLog(context, "called categorylist webservice for categoryId : " + categoryId + "**" + latitude + "*****" + longitude);
        if (!(FilterDialog.filterString != null && FilterDialog.filterString.length() > 0)) {
            progressBarHandler.show();

            AndroidUtils.showErrorLog(context, "shoplist by NOOOOOOOOOOOOOOO filter" + getResources().getString(R.string.webservice_base_url) + "/shoplist");
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")
                    .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", categoryId)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            AndroidUtils.showErrorLog(context, latitude + longitude + categoryId + "resul_data" + result + pageNumber + "sachin tendulkar2");

                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);

                                progressBarHandler.hide();
                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {

                                    int totalresult = Integer.parseInt(result.get("total_result").getAsString());
                                    if (totalresult > 1) {
                                        tvListQuantity.setText(String.valueOf(totalresult));

                                        JsonArray statesArray1 = result.get("filter").getAsJsonArray();
                                        for (int i = 0; i < statesArray1.size(); i++) {
                                            JsonObject stateObject = (JsonObject) statesArray1.get(i);
                                            Log.e("stateobject", stateObject.toString());
                                        }

                                        String message = result.get("message").toString();

                                        String message_data = message.replace("\"", "");
                                        AndroidUtils.showErrorLog(context, "message_data " + message_data);
                                        Log.e("message_product_list", result.toString());

                                        if (message_data.equals("No record found")) {
                                            layout_container.setVisibility(View.INVISIBLE);
                                            progressBarHandler.hide();

                                            Log.e("data----------1", "No record found");

                                        } else {
                                            JsonArray resultJsonArray = result.getAsJsonArray("result");
                                            JsonArray filterArray = result.getAsJsonArray("filter");
                                            if (filterArray != null) {
                                                loadFilterDataInHashMap(filterArray);
                                            }

                                            if (resultJsonArray != null) {
                                                loadResultData(resultJsonArray);
                                            }
                                            if (categoriesListAdapter == null) {
                                                categoriesListAdapter = new CategoriesListAdapter(ShopListByCategoryActivity.this, shopArrayListByCategory);
                                                mRecyclerView.setAdapter(categoriesListAdapter);
                                            } else {
                                                categoriesListAdapter.notifyDataSetChanged();
                                            }


                                            progressBarHandler.hide();

                                        }

                                    } else {
                                        Log.e("data----------1", "No record found list");
                                        layout_container.setVisibility(View.GONE);
                                        findViewById(R.id.filter).setVisibility(View.GONE);
                                        cardviewListContainer.setVisibility(View.GONE);
                                        progressBarHandler.hide();
                                    }
                                } else {
                                    Log.e("data----------1", "No record found list");
                                    layout_container.setVisibility(View.GONE);
                                    cardviewListContainer.setVisibility(View.GONE);

                                    llDataNotFound.setVisibility(View.VISIBLE);
                                    progressBarHandler.hide();
                                }
                            }


                        }


                    });


        } else {

            AndroidUtils.showErrorLog(context, "shoplist by filter --> " + FilterDialog.filterString);

            progressBarHandler.show();
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", categoryId)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("extrafilter", FilterDialog.filterString)
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);
                                progressBarHandler.hide();

                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {
                                    int totalresult = Integer.parseInt(result.get("total_result").getAsString());
                                    if (totalresult > 1) {
                                        tvListQuantity.setText(totalresult + "");
                                        Log.e("data----------2", "sachin2");
                                        toolbarRightText.setVisibility(View.VISIBLE);

                                        JsonArray statesArray1 = result.get("filter").getAsJsonArray();
                                        for (int i = 0; i < statesArray1.size(); i++) {
                                            JsonObject stateObject = (JsonObject) statesArray1.get(i);
                                            Log.e("stateobject", stateObject.toString());
                                        }

                                        String message = result.get("message").toString();

                                        String message_data = message.replace("\"", "");
                                        AndroidUtils.showErrorLog(context, "message_data " + message_data);
                                        Log.e("message_product_list", result.toString());

                                        if (message_data.equals("No record found")) {
                                            layout_container.setVisibility(View.INVISIBLE);
                                            progressBarHandler.hide();
                                        } else {
                                            JsonArray resultJsonArray = result.getAsJsonArray("result");
                                            JsonArray filterArray = result.getAsJsonArray("filter");
                                            if (filterArray != null) {
                                                loadFilterDataInHashMap(filterArray);
                                            }

                                            if (resultJsonArray != null) {
                                                loadResultData(resultJsonArray);
                                            }
                                            if (categoriesListAdapter == null) {
                                                categoriesListAdapter = new CategoriesListAdapter(ShopListByCategoryActivity.this, shopArrayListByCategory);
                                                mRecyclerView.setAdapter(categoriesListAdapter);
                                            } else {
                                                categoriesListAdapter.notifyDataSetChanged();
                                            }


                                            progressBarHandler.hide();

                                        }

                                    } else {

                                        layout_container.setVisibility(View.INVISIBLE);
                                        cardviewListContainer.setVisibility(View.GONE);
                                        llDataNotFound.setVisibility(View.VISIBLE);
                                        findViewById(R.id.filter).setVisibility(View.GONE);
                                        progressBarHandler.hide();
                                    }

                                } else {
                                    Log.e("data----------1", "No record found list");
                                    layout_container.setVisibility(View.INVISIBLE);
                                    progressBarHandler.hide();
                                }
                            }
                        }
                    });
        }

    }

    private void loadResultData(JsonArray resultJsonArray) {
        for (int i = 0; i < resultJsonArray.size(); i++) {
            JsonObject jsonObject2 = (JsonObject) resultJsonArray.get(i);
            AndroidUtils.showErrorLog(context, "<--result-->cITY" + i + jsonObject2.toString());
            String shopId = jsonObject2.get("id").getAsString();
            String shopName = jsonObject2.get("name").getAsString();
            String shopImage = jsonObject2.get("image_url").getAsString();
            String distance = jsonObject2.get("distance").getAsString();
            String category_name = jsonObject2.get("category_name").getAsString();

            String shopLocation = /*jsonObject2.get("city_name").getAsString() + "," +*/ jsonObject2.get("state_name").getAsString() + "," + jsonObject2.get("country_name").getAsString();

            Log.e("shop_list", shopImage);
            shopArrayListByCategory.add(new CategoriesListData(shopId, shopName, shopImage, shopLocation, distance, category_name));

            AndroidUtils.showErrorLog(context, "shopArrayListByCategory : " + shopArrayListByCategory.get(i).shopImage);
        }
        listfootername.setText(shopArrayListByCategory.get(0).shopCategory);

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

        toolbarRightText = (TextView) findViewById(R.id.toolbarRightText);
        toolbarRightText.setVisibility(View.GONE);
        toolbarRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        getMenuInflater().inflate(R.menu.user, menu);

        final MenuItem alertMenuItem = menu.findItem(R.id.cart_total_item);

        RelativeLayout badgeLayout = (RelativeLayout) alertMenuItem.getActionView();
        if(appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString()).equals("2")){
            alertMenuItem.setVisible(false);
        }

        tvCartCount = (TextView) badgeLayout.findViewById(R.id.tvCartCount);
        tvfilter = (TextView) badgeLayout.findViewById(R.id.filter);


        tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));

        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
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
                    Intent intent = new Intent(ShopListByCategoryActivity.this, MyCartActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.filter:
                FilterDialog filterDialog = new FilterDialog(context, categoryId, filterHashMap);
                filterDialog.show();
                break;

            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadFilterDataInHashMap(JsonArray filterArray) {
        filterHashMap = new ArrayMap<>();
        if (filterArray.size() > 0) {
            AndroidUtils.showErrorLog(context, "size of filter Array is  :  " + filterArray.size());
            for (int i = 0; i < filterArray.size(); i++) {
                JsonObject filterObject = (JsonObject) filterArray.get(i);
                String filterName = filterObject.get("name").getAsString();
                JsonArray valueJsonArray = filterObject.get("values").getAsJsonArray();
                ArrayList<FilterObject> valueArrayList = new ArrayList<>();

                if (valueJsonArray != null) {

                    for (int j = 0; j < valueJsonArray.size(); j++) {

                        FilterObject filterObjectData = new FilterObject();
                        JsonObject filterValueObject = (JsonObject) valueJsonArray.get(j);
                        String[] filterValueObjectArray = filterValueObject.toString().replaceAll("\\{", "").replaceAll("\\}", "").trim().split(",");
                        AndroidUtils.showErrorLog(context, "Length of filter value array is : ******" + filterValueObjectArray.length);

                        for (int k = 0; k < filterValueObjectArray.length; k++) {
                            AndroidUtils.showErrorLog(context, "filterValueObjectArray[k]" + filterValueObjectArray[k]);
                            String key = filterValueObjectArray[k].split(":")[0].replaceAll("\"", "");
                            String value = filterValueObjectArray[k].split(":")[1].replaceAll("\"", "");
                            if (key.contains("id")) {
                                filterObjectData.id.key = key;
                                filterObjectData.id.value = value;
                            } else if (key.contains("name")) {
                                filterObjectData.name.key = key;
                                filterObjectData.name.value = value;
                            } else if (key.contains("count")) {
                                filterObjectData.count.key = key;
                                filterObjectData.count.value = value;
                            }
                        }

                        valueArrayList.add(filterObjectData);

                    }
                }
                filterHashMap.put(filterName, valueArrayList);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (categoryListActivity == 1) {
            categoryListActivity = 2;
        } else {
            tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
        }

    }


}
