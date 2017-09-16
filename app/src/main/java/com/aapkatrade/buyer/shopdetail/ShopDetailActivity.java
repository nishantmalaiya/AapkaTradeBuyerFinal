package com.aapkatrade.buyer.shopdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.home.CommonAdapter;
import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.ServiceEnquiry;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.login.LoginDashboard;
import com.aapkatrade.buyer.map.GoogleMapActivity;
import com.aapkatrade.buyer.rateus.RateUsActivity;
import com.aapkatrade.buyer.shopdetail.opening_closing_days.OpenCloseDaysRecyclerAdapter;
import com.aapkatrade.buyer.shopdetail.opening_closing_days.OpenCloseShopData;
import com.aapkatrade.buyer.shopdetail.reviewlist.ReviewListAdapter;
import com.aapkatrade.buyer.shopdetail.reviewlist.ReviewListData;
import com.aapkatrade.buyer.shopdetail.shop_all_product.ShopAllProductActivity;
import com.aapkatrade.buyer.uicomponent.bottomnavigationview.CustomBottomNavigationView;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import github.nisrulz.stackedhorizontalprogressbar.StackedHorizontalProgressBar;
import me.relex.circleindicator.CircleIndicator;


public class ShopDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private LinearLayout viewpagerindicator, linearlayoutShare, linearlayoutLocation;
    private RelativeLayout shopProductsLayout, openingClosingRelativeLayout, relativeLayoutlViewAllProducts, viewpager_container_shopdetail;
    private Spinner spinner;
    private int max = 10;
    private ArrayList<ShopDetailMediaDatas> imageList;
    private int currentPage = 0;
    private int isStartDate = -1;
    private ServiceEnquiry serviceEnquiry;
    private String date;
    private StackedHorizontalProgressBar progressbarFive, progressbarFour, progressbarThree, progressbarTwo, progressbarOne;
    private HorizontalInfiniteCycleViewPager vp;
    private ShopViewPagerAdapter viewpageradapter;
    private int dotsCount;
    private CircleIndicator circleIndicator;
    private ImageView[] dots;
    private Timer banner_timer = new Timer();

    private RelativeLayout relativeRateReview;
    private LinearLayout linearProductDetail, RelativeProductDetail;
    private TextView tvshopName, tvProPrice, tvCrossPrice, tvDiscription, tvSpecification, tvQuatity;
    private ProgressBarHandler progress_handler;
    private String product_id;
    private ImageView imgViewPlus, imgViewMinus;
    private int quantity_value = 1;
    private ProgressBarHandler progressBarHandler;
    private String productlocation, categoryName;
    private LinearLayout linearLayoutQuantity;
    private EditText firstName, quantity, price, mobile, email, etEndDate, etStatDate, description, editText;
    private TextView tvServiceBuy, textViewQuantity, tvRatingAverage, tvTotal_rating_review, tvShopAddress, tvMobile, tvPhone, listfootername, tv_list_quantity;
    private Dialog dialog;
    private Context context;
    private ArrayList<CommonData> productlist = new ArrayList<>();
    private String product_name, productUrlPart;
    private DroppyMenuPopup droppyMenu;
    private AppSharedPreference appSharedPreference;
    private RecyclerView reviewList, openShopList, productRecyclerView;
    private LinearLayoutManager mLayoutManager, mLayoutManagerShoplist, llmanagerProductList;
    private ReviewListAdapter reviewListAdapter;
    private OpenCloseDaysRecyclerAdapter openCloseDaysRecyclerAdapter;
    private ArrayList<ReviewListData> reviewListDatas = new ArrayList<>();
    private CommonAdapter commonAdapter_latestpost;
    private ArrayList<OpenCloseShopData> openCloseDayArrayList = new ArrayList<>();
    private String shopId;
    public static TextView tvCartCount;
    private int shopDetailActivity = 1;
    private CustomBottomNavigationView bottomNavigationShop;
    private CoordinatorLayout coordinatorLayout;
    ImageButton btnServiceEnquiry;
    String VideoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("time  Product Detail", String.valueOf(System.currentTimeMillis()));

        setContentView(R.layout.activity_shop_detail);

        appSharedPreference = new AppSharedPreference(ShopDetailActivity.this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        context = ShopDetailActivity.this;

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        product_id = b.getString("product_id");

        AndroidUtils.showErrorLog(context, "product_id", product_id);


        progressBarHandler = new ProgressBarHandler(context);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_product_detail);
        setUpToolBar();
        initView();

        getShopDetailsData();
        relativeLayoutlViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentViewAllProducts = new Intent(ShopDetailActivity.this, ShopAllProductActivity.class);
                intentViewAllProducts.putExtra("shopId", shopId);
                startActivity(intentViewAllProducts);
            }
        });


    }

    private void getShopDetailsData() {

        linearProductDetail.setVisibility(View.GONE);
        progress_handler.show();
        AndroidUtils.showErrorLog(context, "data_productdetail", getResources().getString(R.string.webservice_base_url) + "     " + product_id);

        Ion.with(getApplicationContext())
                .load(getResources().getString(R.string.webservice_base_url).concat("/shop_detail/").concat(product_id))
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                //.setBodyParameter("id", "0")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {

                            AndroidUtils.showErrorLog(context, "result---------", result);
                            JsonObject json_result = result.get("result").getAsJsonObject();
                            shopId = json_result.get("id").getAsString();
                            productUrlPart = json_result.get("product_url").getAsString();
                            VideoPath=json_result.get("shop_video").getAsString();

                            AndroidUtils.showErrorLog(context,"video_Path",VideoPath);
                            JsonObject json_total_rating = result.getAsJsonObject("total_rating");
                            String avg_rating = json_total_rating.get("avg_rating").getAsString();
                            tvRatingAverage.setText(avg_rating);
                            String total_review = json_total_rating.get("countreviews").getAsString();
                            tvTotal_rating_review.setText(total_review + " rating and " + "review " + total_review);
                            JsonArray jsonArrayImage = json_result.getAsJsonArray("product_images");
                            JsonArray jsonArrayReview = result.getAsJsonArray("reviews");
                            if (jsonArrayReview.size() != 0) {
                                findViewById(R.id.reviewListLayout).setVisibility(View.VISIBLE);
                                for (int j = 0; j < jsonArrayReview.size(); j++) {
                                    JsonObject jsonreview_data = (JsonObject) jsonArrayReview.get(j);
                                    String email = jsonreview_data.get("email").getAsString();
                                    String name = jsonreview_data.get("name").getAsString();
                                    String message = jsonreview_data.get("message").getAsString();
                                    String rating = jsonreview_data.get("rating").getAsString();
                                    String title = jsonreview_data.get("title").getAsString();
                                    String created_date = jsonreview_data.get("created_at").getAsString();
                                    Log.e("jsonreview---", title);
                                    reviewListDatas.add(new ReviewListData(email, name, message, rating, title, created_date));
                                }
                                reviewListAdapter = new ReviewListAdapter(ShopDetailActivity.this, reviewListDatas);
                                reviewList.setAdapter(reviewListAdapter);
                            }
                            AndroidUtils.showErrorLog(context, "jsonArrayImage------" + jsonArrayReview.toString());
                            for (int i = 0; i < jsonArrayImage.size(); i++) {
                                JsonObject jsonimage = (JsonObject) jsonArrayImage.get(i);
                                String image_url = jsonimage.get("image_url").getAsString();
                                AndroidUtils.showErrorLog(context, "imageUrl---------" + image_url);
                                imageList.add(new ShopDetailMediaDatas(image_url,false));
                            }

                            imageList.add(new ShopDetailMediaDatas(VideoPath,true));
                            product_name = json_result.get("name").getAsString();
                            categoryName = json_result.get("catname").getAsString();
//                            String product_price = json_result.get("price").getAsString();
//                            String product_cross_price = json_result.get("cross_price").getAsString();
                            String description = json_result.get("short_des").getAsString();
                            String duration = json_result.get("deliverday").getAsString();
                            String pincode = json_result.get("pincode").getAsString();
                            String address = json_result.get("address").getAsString();
                            if (Validation.isNonEmptyStr(address) && Validation.isNonEmptyStr(pincode)) {
                                address = new StringBuilder(address).append(", PINCODE - ").append(pincode).toString();
                            }
                            String mobile = json_result.get("mobile").getAsString();
                            String phone = json_result.get("phone").getAsString();


                            /*
                            ===================================== Shop Product List ========================================
                             */
                            JsonArray jsonProductList = json_result.getAsJsonArray("associate_product");
                            if (jsonProductList != null && jsonProductList.size() > 0) {
                                shopProductsLayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonProductList.size(); i++) {
                                    JsonObject jsonproduct = (JsonObject) jsonProductList.get(i);
                                    String product_id = jsonproduct.get("id").getAsString();
                                    AndroidUtils.showErrorLog(context, "___________PRODUCT ID---22--------->" + product_id);
                                    String product_name = jsonproduct.get("name").getAsString();
                                    String productShortDescription = jsonproduct.get("short_des").getAsString();
                                    String price = jsonproduct.get("price").getAsString();
                                    String product_image = jsonproduct.get("image_url").getAsString();
                                    productlist.add(new CommonData(product_id, product_name, price, product_image, address));
                                }
                                commonAdapter_latestpost = new CommonAdapter(context, productlist, "list_product", "latestdeals");
                                productRecyclerView.setAdapter(commonAdapter_latestpost);
                            } else {
                                shopProductsLayout.setVisibility(View.GONE);
                            }

                            tvShopAddress.setText(address);

                            if (Validation.isEmptyStr(phone)) {
                                tvPhone.setVisibility(View.GONE);
                                findViewById(R.id.img_phone).setVisibility(View.GONE);
                            } else
                                tvPhone.setText(phone);
                            if (Validation.isEmptyStr(mobile)) {
                                tvMobile.setVisibility(View.GONE);
                                findViewById(R.id.img_mobile).setVisibility(View.GONE);
                            } else
                                tvMobile.setText(mobile);
                            /*
                            ===================================== Shop Opening Closing Days ========================================
                             */
                            productlocation = json_result.get("city_name").getAsString() + "," + json_result.get("state_name").getAsString() + "," + json_result.get("country_name").getAsString();
                            JsonArray openCloseDayArray = result.getAsJsonArray("opening_time");

                            if (openCloseDayArray != null && openCloseDayArray.size() > 0) {
                                openingClosingRelativeLayout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < openCloseDayArray.size(); i++) {
                                    JsonObject jsonObjectDays = (JsonObject) openCloseDayArray.get(i);
                                    OpenCloseShopData openCloseShopData = new OpenCloseShopData(Validation.isEmptyStr(jsonObjectDays.get("days").getAsString())?"":jsonObjectDays.get("days").getAsString().substring(0, 3), jsonObjectDays.get("open_time") == null ? "" : jsonObjectDays.get("open_time").getAsString(), jsonObjectDays.get("close_time") == null ? "" : jsonObjectDays.get("close_time").getAsString());
                                    if (Validation.containsIgnoreCase(jsonObjectDays.get("days").getAsString(), "mon") && Validation.containsIgnoreCase(jsonObjectDays.get("days").getAsString(), "fri")) {
                                        for (int j = 0; j < 5; j++) {
                                            String[] daysName = {"Mon", "Tue", "Wed", "Thu", "Fri"};
                                            OpenCloseShopData openCloseShopData1 = new OpenCloseShopData(daysName[j], openCloseShopData.openingTime, openCloseShopData.closingTime);
                                            openCloseDayArrayList.add(openCloseShopData1);
                                            AndroidUtils.showErrorLog(context, "openCloseShopData", openCloseShopData);
                                        }
                                    } else {
                                        openCloseDayArrayList.add(openCloseShopData);
                                    }
                                    removeUnavailedDays(openCloseDayArrayList);
                                }
                                if (openCloseDayArrayList.size() > 0) {
                                    mLayoutManagerShoplist = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                    openShopList.setLayoutManager(mLayoutManagerShoplist);
                                    openCloseDaysRecyclerAdapter = new OpenCloseDaysRecyclerAdapter(context, openCloseDayArrayList);
                                    openShopList.setAdapter(openCloseDaysRecyclerAdapter);
                                } else {
                                    openingClosingRelativeLayout.setVisibility(View.GONE);
                                }
                            }else {
                                openingClosingRelativeLayout.setVisibility(View.GONE);
                            }

                            tvshopName.setText(product_name);
                            if (Validation.isEmptyStr(description)) {
                                findViewById(R.id.descriptionLayout).setVisibility(View.GONE);
                            }
                            tvDiscription.setText(description);
                            setUpViewPager();

                            progress_handler.hide();

                            linearProductDetail.setVisibility(View.VISIBLE);


                        } else {
                            progress_handler.hide();
                            linearProductDetail.setVisibility(View.GONE);

                        }


                    }


                });

    }

    private void removeUnavailedDays(ArrayList<OpenCloseShopData> openCloseDayArrayList) {
        for (int i = 0; i < openCloseDayArrayList.size(); i++) {
            if (Validation.isEmptyStr(openCloseDayArrayList.get(i).openingTime) || Validation.isEmptyStr(openCloseDayArrayList.get(i).closingTime)) {
                openCloseDayArrayList.remove(i);
                i--;
            }
        }
    }


    private void setUpViewPager() {

        viewpageradapter = new ShopViewPagerAdapter(ShopDetailActivity.this, imageList);
        vp.setAdapter(viewpageradapter);
        vp.setCurrentItem(currentPage);


        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == viewpageradapter.getCount() - 1) {
                    currentPage = 0;
                }
                vp.setCurrentItem(currentPage++, true);
            }
        };


        banner_timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, 3000);


        circleIndicator.setViewPager(vp);
    }


    private void initView() {
        //AndroidUtils.showToast(context, "This is my Toast");
        shopProductsLayout = (RelativeLayout) findViewById(R.id.shop_products_relative_layout);
        progress_handler = new ProgressBarHandler(this);
        listfootername = (TextView) findViewById(R.id.listfootername);
        tv_list_quantity = (TextView) findViewById(R.id.tv_list_quantity);
        imageList = new ArrayList<>();

        relativeRateReview = (RelativeLayout) findViewById(R.id.relativeRateReview);
        openingClosingRelativeLayout = (RelativeLayout) findViewById(R.id.opening_closing_relative_layout);
        relativeLayoutlViewAllProducts = (RelativeLayout) findViewById(R.id.rl_viewall_products);
        viewpager_container_shopdetail = (RelativeLayout) findViewById(R.id.viewpager_container_shopdetail);

//        AndroidUtils.setGradientColor(viewpager_container_shopdetail, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.black), ContextCompat.getColor(context, R.color.white), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);

        linearlayoutShare = (LinearLayout) findViewById(R.id.linearlayoutShare);

        linearlayoutLocation = (LinearLayout) findViewById(R.id.linearlayoutLocation);

        tvRatingAverage = (TextView) findViewById(R.id.tvRatingAverage);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvPhone = (TextView) findViewById(R.id.tvPhone);


        tvTotal_rating_review = (TextView) findViewById(R.id.tvToatal_rating_review);

        tvServiceBuy = (TextView) findViewById(R.id.tvServiceBuy);

        reviewList = (RecyclerView) findViewById(R.id.reviewList);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        reviewList.setLayoutManager(mLayoutManager);

        openShopList = (RecyclerView) findViewById(R.id.openShopList);
        productRecyclerView = (RecyclerView) findViewById(R.id.recyclerproduct);
        llmanagerProductList = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        productRecyclerView.setLayoutManager(llmanagerProductList);

        relativeRateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not").contains("not")) {
                    startActivity(new Intent(ShopDetailActivity.this, LoginDashboard.class));
                } else {
                    if (!appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "not").equals(SharedPreferenceConstants.USER_TYPE_BUYER.toString())) {
                        AndroidUtils.showToast(context, "Only buyer can rate or write reviews");
                    } else {
                        Intent rate_us = new Intent(ShopDetailActivity.this, RateUsActivity.class);
                        rate_us.putExtra("isShopDetail", true);
                        rate_us.putExtra("product_id", product_id);
                        rate_us.putExtra("product_name", tvshopName.getText().toString());
                        rate_us.putExtra("product_price", "");
                        rate_us.putExtra("product_image", imageList.get(0).MediaUrl);
                        startActivity(rate_us);
                    }
                }
            }
        });


        linearlayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean permission_status = CheckPermission.checkPermissions(ShopDetailActivity.this);
                if (permission_status) {
                    progressBarHandler.show();
                    LocationManagerCheck locationManagerCheck = new LocationManagerCheck(ShopDetailActivity.this);
                    Location location = null;
                    if (locationManagerCheck.isLocationServiceAvailable()) {
                        Intent intent = new Intent(ShopDetailActivity.this, GoogleMapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("product_location", productlocation);
                        ShopDetailActivity.this.startActivity(intent);
                        progressBarHandler.hide();
                    } else {
                        locationManagerCheck.createLocationServiceError(ShopDetailActivity.this);
                        progress_handler.hide();
                    }
                }
            }
        });
        linearlayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                // Uri screenshotUri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/" + R.drawable.ic_app_icon);
                String strShareMessage = "\nLet me recommend you this application\n\n";
                strShareMessage = strShareMessage.concat(getString(R.string.share_base_url)).concat("/").concat(productUrlPart);

                // share.setType("image");
                //  share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                share.putExtra(Intent.EXTRA_TEXT, strShareMessage);


                startActivity(Intent.createChooser(share, "Share using"));
            }
        });

        RelativeProductDetail = (LinearLayout) findViewById(R.id.RelativeProductDetail);
        linearProductDetail = (LinearLayout) findViewById(R.id.linearProductDetail);
        tvshopName = (TextView) findViewById(R.id.tvShopName);


        tvDiscription = (TextView) findViewById(R.id.tvDiscription);
        tvSpecification = (TextView) findViewById(R.id.tvSpecification);


        btnServiceEnquiry = (ImageButton) findViewById(R.id.btnService_enquiry);

        vp = (HorizontalInfiniteCycleViewPager) findViewById(R.id.viewpager_custom);
        viewpagerindicator = (LinearLayout) findViewById(R.id.viewpagerindicator);

        progressbarFive = (StackedHorizontalProgressBar) findViewById(R.id.progressbarFive);
        progressbarFive.setMax(max);
        progressbarFive.setProgress(10);
        progressbarFive.setSecondaryProgress(0);

        progressbarFour = (StackedHorizontalProgressBar) findViewById(R.id.progressbarFour);
        progressbarFour.setMax(max);
        progressbarFour.setProgress(7);
        progressbarFour.setSecondaryProgress(3);

        progressbarThree = (StackedHorizontalProgressBar) findViewById(R.id.progressbarThree);
        progressbarThree.setMax(max);
        progressbarThree.setProgress(5);
        progressbarThree.setSecondaryProgress(5);

        progressbarTwo = (StackedHorizontalProgressBar) findViewById(R.id.progressbarTwo);
        progressbarTwo.setMax(max);
        progressbarTwo.setProgress(4);
        progressbarTwo.setSecondaryProgress(6);

        progressbarOne = (StackedHorizontalProgressBar) findViewById(R.id.progressbarOne);
        progressbarOne.setMax(max);
        progressbarOne.setProgress(3);
        progressbarOne.setSecondaryProgress(7);


        btnServiceEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceEnquiry serviceEnquiry = new ServiceEnquiry(product_id, context);
                FragmentManager fm = getSupportFragmentManager();
                serviceEnquiry.show(fm, "enquiry");

            }
        });


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
                    Intent intent = new Intent(ShopDetailActivity.this, MyCartActivity.class);
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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        showDate(year, monthOfYear + 1, dayOfMonth);
    }


    private void showDate(int year, int month, int day) {
        date = (new StringBuilder()).append(year).append("-").append(month).append("-").append(day).toString();
        if (isStartDate == 0) {
            etStatDate.setText(date);
        } else if (isStartDate == 1) {
            etEndDate.setText(date);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (shopDetailActivity == 1) {

            shopDetailActivity = 2;
        } else {
            tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
        }

    }


}
