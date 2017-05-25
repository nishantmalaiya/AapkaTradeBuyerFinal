package com.aapkatrade.buyer.categories_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.Home.cart.MyCartActivity;
import com.aapkatrade.buyer.Home.registration.entity.State;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.filter.FilterDialog;
import com.aapkatrade.buyer.filter.entity.FilterObject;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;


public class ShopListByCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoriesListAdapter categoriesListAdapter;
    private ArrayList<CategoriesListData> shopArrayListByCategory = new ArrayList<>();
    ProgressBarHandler progress_handler;
    private FrameLayout layout_container;
    private static String category_id, latitude = "0.0", longitude = "0.0";
    private AppSharedPreference appSharedPreference;
    private ArrayList<State> productAvailableStateList = new ArrayList<>();
    private Context context;
    private TextView toolbarRightText;
    private ArrayMap<String, ArrayList<FilterObject>> filterHashMap = null;
    private ViewGroup view;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    public static TextView tvCartCount;
    private int categoryListActivity = 1;


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
            category_id = b.getString("category_id");
            AndroidUtils.showErrorLog(context, "_______________latitude" + latitude + "longitude" + longitude + "category_id" + category_id);
        }
        setUpToolBar();
        initView();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        StikkyHeaderBuilder.stickTo(mRecyclerView).setHeader(R.id.header_simple, view).minHeightHeaderDim(R.dimen.min_header_height).build();
        getShopListData("0");
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        page = page + 1;
                        getShopListData_scroll(String.valueOf(page));
                    }
                }
            }

        });

    }

    private void initView() {
        view = (ViewGroup) findViewById(android.R.id.content);
        progress_handler = new ProgressBarHandler(this);
        layout_container = (FrameLayout) view.findViewById(R.id.layout_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }

    private void getShopListData(final String pageNumber) {
        State state = new State("-1", "Select State", "0");
        productAvailableStateList.add(state);
        AndroidUtils.showErrorLog(context, shopArrayListByCategory.size() + "^^^^^^^^^");

        Log.e(AndroidUtils.getTag(context), "called categorylist webservice for category_id : " + category_id + "**" + latitude + "*****" + longitude);


        if (!(FilterDialog.filterString != null && FilterDialog.filterString.length() > 0)) {
            progress_handler.show();

            AndroidUtils.showErrorLog(context, "shoplist by NOOOOOOOOOOOOOOO filter" + getResources().getString(R.string.webservice_base_url) + "/shoplist");
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")
                    .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", category_id)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            AndroidUtils.showErrorLog(context, latitude + longitude + category_id + "resul_data" + result + pageNumber + "sachin tendulkar2");


                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);
                                progress_handler.hide();
                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {
                                    if (Validation.isNumber(result.get("total_result").getAsString()) && Integer.parseInt(result.get("total_result").getAsString()) > 1) {
                                        //toolbarRightText.setVisibility(View.VISIBLE);

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
//                                    mRecyclerView.smoothScrollToPosition(21);
                                            }


                                            progress_handler.hide();

                                        }

                                    }
                                }
                            }


                        }


                    });


        } else {

            AndroidUtils.showErrorLog(context, "shoplist by filter --> " + FilterDialog.filterString);

            progress_handler.show();
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")

                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", category_id)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("extrafilter", FilterDialog.filterString)
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)

//                    .asString()
//                    .setCallback(new FutureCallback<String>() {
//                        @Override
//                        public void onCompleted(Exception e, String result) {
//                             if(result == null){
//                                AndroidUtils.showErrorLog(context, "++++++++++++++++"+e.toString()+"___________");
//
//                            }else {AndroidUtils.showErrorLog(context, "shoplist by filter daaaaaaaaaata is "+result);
//
//
//                             }
//                        }
//                    });

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);
                                progress_handler.hide();

                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {
                                    if (Validation.isNumber(result.get("total_result").getAsString()) && Integer.parseInt(result.get("total_result").getAsString()) > 1) {
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
//                                    mRecyclerView.smoothScrollToPosition(21);
                                            }


                                            progress_handler.hide();

                                        }

                                    }
                                }
                            }


                        }


                    });

//            FilterDialog.filterString = "";
        }

    }


    private void getShopListData_scroll(final String pageNumber) {
        State state = new State("-1", "Select State", "0");
        productAvailableStateList.add(state);
        AndroidUtils.showErrorLog(context, shopArrayListByCategory.size() + "^^^^^^^^^");

        Log.e(AndroidUtils.getTag(context), "called categorylist webservice for category_id : " + category_id + "**" + latitude + "*****" + longitude);


        if (!(FilterDialog.filterString != null && FilterDialog.filterString.length() > 0)) {
            //progress_handler.show();

            AndroidUtils.showErrorLog(context, "shoplist by NOOOOOOOOOOOOOOO filter" + getResources().getString(R.string.webservice_base_url) + "/shoplist");
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")
                    .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", category_id)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            AndroidUtils.showErrorLog(context, latitude + longitude + category_id + "resul_data" + result + pageNumber + "sachin tendulkar2");


                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);
                                progress_handler.hide();
                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {
                                    if (Validation.isNumber(result.get("total_result").getAsString()) && Integer.parseInt(result.get("total_result").getAsString()) > 1) {
                                        //toolbarRightText.setVisibility(View.VISIBLE);

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
//                                    mRecyclerView.smoothScrollToPosition(21);
                                            }


                                            progress_handler.hide();

                                        }

                                    }
                                }
                            }


                        }


                    });


        } else {

            AndroidUtils.showErrorLog(context, "shoplist by filter --> " + FilterDialog.filterString);

            // progress_handler.show();
            Ion.with(ShopListByCategoryActivity.this)
                    .load(getResources().getString(R.string.webservice_base_url) + "/shoplist")

                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "category")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("category_id", category_id)
                    .setBodyParameter("apply", "1")
                    .setBodyParameter("extrafilter", FilterDialog.filterString)
                    .setBodyParameter("page", pageNumber)
                    .setBodyParameter("lat", latitude)
                    .setBodyParameter("long", longitude)

//                    .asString()
//                    .setCallback(new FutureCallback<String>() {
//                        @Override
//                        public void onCompleted(Exception e, String result) {
//                             if(result == null){
//                                AndroidUtils.showErrorLog(context, "++++++++++++++++"+e.toString()+"___________");
//
//                            }else {AndroidUtils.showErrorLog(context, "shoplist by filter daaaaaaaaaata is "+result);
//
//
//                             }
//                        }
//                    });

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {


                            AndroidUtils.showErrorLog(context, "shoplist by filter daaaaaaaaaata is " + result.toString());
                            if (result == null) {
                                layout_container.setVisibility(View.INVISIBLE);
                                progress_handler.hide();

                            } else {

                                Log.e(AndroidUtils.getTag(context), result.toString());
                                if (result.get("error").getAsString().contains("false")) {
                                    if (Validation.isNumber(result.get("total_result").getAsString()) && Integer.parseInt(result.get("total_result").getAsString()) > 1) {
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
                                                //mRecyclerView.smoothScrollToPosition(21);
                                            }


                                            progress_handler.hide();

                                        }

                                    }
                                }
                            }


                        }


                    });

//            FilterDialog.filterString = "";
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
            String shopLocation = /*jsonObject2.get("city_name").getAsString() + "," +*/ jsonObject2.get("state_name").getAsString() + "," + jsonObject2.get("country_name").getAsString();

            Log.e("shop_list", shopImage);
            shopArrayListByCategory.add(new CategoriesListData(shopId, shopName, shopImage, shopLocation, distance));

            AndroidUtils.showErrorLog(context, "shopArrayListByCategory : " + shopArrayListByCategory.get(i).shopImage);
        }


    }

    private void setUpToolBar() {

        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
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
        toolbarRightText = (TextView) findViewById(R.id.toolbarRightText);
        toolbarRightText.setVisibility(View.GONE);
        //toolbarRightText.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_filter));
        toolbarRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FilterDialog filterDialog = new FilterDialog(context, category_id, filterHashMap);
                filterDialog.show();*/
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

        tvCartCount = (TextView) badgeLayout.findViewById(R.id.tvCartCount);

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
                    Toast.makeText(getApplicationContext(), "My Cart have no items please add items in cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ShopListByCategoryActivity.this, MyCartActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.filter:
                FilterDialog filterDialog = new FilterDialog(context, category_id, filterHashMap);
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
