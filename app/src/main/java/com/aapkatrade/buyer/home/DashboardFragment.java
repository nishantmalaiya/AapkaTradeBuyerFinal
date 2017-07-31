package com.aapkatrade.buyer.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.aapkatrade.buyer.home.banner_home.viewpageradapter_home;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.particular_data.ParticularActivity;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;

import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.location.GeoCoderAddress;
import com.aapkatrade.buyer.location.Mylocation;
import com.aapkatrade.buyer.search.Search;
import com.aapkatrade.buyer.service.GpsLocationService;
import com.aapkatrade.buyer.uicomponent.customviewpager.WrappingViewPager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    Context context;
    int currentPage = 0;
    LinearLayout viewpagerindicator;
    private RecyclerView recyclerLatestDeals, recyclerlatestupdate;
    private LinearLayoutManager llManagerEclipseCollection;

    ArrayList<CommonData> commonDatas_latestpost = new ArrayList<>();
    ArrayList<CommonData> commonDatas_latestupdate = new ArrayList<>();
    private CommonAdapter commonAdapter_latestpost, commonAdapter_latestproduct;
    ProgressBarHandler progress_handler;
    private int dotsCount;
    private ArrayList<String> imageIdList;
    private ImageView[] dots;
    GpsLocationService gps;
    Button btn_tryagain;
    public static SearchView searchView;
    ImageView home_ads;
    private StikkyHeaderBuilder.ScrollViewBuilder stikkyHeader;
    RelativeLayout view_all_latest_post, view_all_latest_update;
    LinearLayout rl_retry;
    WrappingViewPager vp;
    NestedScrollView scrollView;
    Timer banner_timer = new Timer();
    CoordinatorLayout coordinatorLayout;
    GridLayoutManager gridLayoutManager;
    viewpageradapter_home viewpageradapter;
    JsonObject home_result;
    RelativeLayout rl_searchview_dashboard;
    CircleIndicator circleIndicator;
    View view;
    Mylocation mylocation;
    private GeoCoderAddress geoCoderAddressAsync;
    private String AddressAsync, currentLatitude, currentLongitude, stateName;
    AppSharedPreference appSharedPreference;
    String user_id;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_dashboard_new, container, false);
            appSharedPreference = new AppSharedPreference(getActivity());
            initializeview(view, container);
        }
        return view;
    }


    private void setupviewpager(ArrayList<String> imageIdList) {
        viewpageradapter = new viewpageradapter_home(getActivity(), imageIdList);
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

    private void initializeview(View v, ViewGroup v2) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        progress_handler = new ProgressBarHandler(getActivity());

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordination_home);
        coordinatorLayout.setVisibility(View.INVISIBLE);

        home_ads = (ImageView) v.findViewById(R.id.home_ads);


        circleIndicator = (CircleIndicator) view.findViewById(R.id.indicator_custom);
        vp = (WrappingViewPager) view.findViewById(R.id.viewpager_custom);
        context = getActivity();

        llManagerEclipseCollection = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        viewpagerindicator = (LinearLayout) view.findViewById(R.id.viewpagerindicator);
        // latestproductadapter = new latestproductadapter(context, commonDatas);
        recyclerLatestDeals = (RecyclerView) view.findViewById(R.id.recyclerlatestDeals);
        recyclerLatestDeals.setLayoutManager(llManagerEclipseCollection);

        rl_retry = (LinearLayout) view.findViewById(R.id.rl_retry);
        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);

        recyclerlatestupdate = (RecyclerView) view.findViewById(R.id.recyclerlatestupdate);
        if (Tabletsize.isTablet(getActivity())) {
            gridLayoutManager = new GridLayoutManager(context, 3);
        } else {

            gridLayoutManager = new GridLayoutManager(context, 2);
        }

        recyclerlatestupdate.setLayoutManager(gridLayoutManager);
        recyclerlatestupdate.setHasFixedSize(true);

        view_all_latest_post = (RelativeLayout) view.findViewById(R.id.rl_viewall_latest_post);
        view_all_latest_post.setOnClickListener(this);


        view_all_latest_update = (RelativeLayout) view.findViewById(R.id.rl_viewall_latest_update);
        view_all_latest_update.setOnClickListener(this);

        user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");


        if (!(user_id != null && user_id.equals("notlogin"))) {
            update_token(user_id);
        }


        get_home_data();

        btn_tryagain = (Button) view.findViewById(R.id.btn_tryagain);

        rl_searchview_dashboard = (RelativeLayout) v.findViewById(R.id.rl_searchview);

        rl_searchview_dashboard.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                LocationManagerCheck locationManagerCheck = new LocationManagerCheck(getActivity());

                if (locationManagerCheck.isLocationServiceAvailable()) {
                    progress_handler.show();


                    gps = new GpsLocationService(getActivity());

                    // check if GPS enabled
                    if (gps.canGetLocation()) {


                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        String statename = gps.getStaeName();


                        Intent intentAsync = new Intent(getActivity(), Search.class);
                        intentAsync.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentAsync.putExtra("classname", "homeactivity");
                        intentAsync.putExtra("state_name", statename);
                        intentAsync.putExtra("latitude", String.valueOf(latitude));
                        intentAsync.putExtra("longitude", String.valueOf(longitude));
                        getActivity().startActivity(intentAsync);

                        progress_handler.hide();
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }


                } else {
                    locationManagerCheck.createLocationServiceError(getActivity());
                }


            }


        });


    }

    private void update_token(String id) {


        Ion.with(getActivity())
                .load(getResources().getString(R.string.webservice_base_url) + "/update_token")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", id)
                .setBodyParameter("type", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "0"))
                .setBodyParameter("token", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                    }
                });

    }

    public void get_home_data() {
        progress_handler.show();

        coordinatorLayout.setVisibility(View.INVISIBLE);

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        if (user_id.equals("notlogin")) {
            user_id = "";
        }

        commonDatas_latestpost.clear();
        commonDatas_latestupdate.clear();
        Ion.with(getActivity())
                .load(getResources().getString(R.string.webservice_base_url) + "/home")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("city_id", "")
                .setBodyParameter("user_type", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "notlogin"))
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {
                            home_result = result;
                            if (rl_retry.getVisibility() == View.VISIBLE) {
                                rl_retry.setVisibility(View.GONE);
                            }

                         /*   Log.e(AppConfig.getCurrentDeviceId(context)+"-data===============", result.toString().substring(0,4000));
                            Log.e(AppConfig.getCurrentDeviceId(context)+"-data===============", result.toString().substring(4000, result.toString().length()-1));
                         */
                            JsonObject jsonResult = result.getAsJsonObject("result");

                            String cart_count = jsonResult.get("cart_count") == null ? "0" : jsonResult.get("cart_count").getAsString();


                            appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));
                            //int j = appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(),0);

                            if (HomeActivity.tvCartCount != null) {
                                HomeActivity.tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));

                            }


                            JsonArray jsonarray_top_banner = jsonResult.getAsJsonArray("top_banner");
                            imageIdList = new ArrayList<>();

                            for (int l = 0; l < jsonarray_top_banner.size(); l++) {
                                JsonObject jsonObject_top_banner = (JsonObject) jsonarray_top_banner.get(l);
                                String banner_imageurl = jsonObject_top_banner.get("image_url").getAsString();

                                imageIdList.add(banner_imageurl);
                            }

                            setupviewpager(imageIdList);

                            JsonArray latest_deals = jsonResult.getAsJsonArray("latest_post");

                            JsonArray latest_update = jsonResult.getAsJsonArray("latest_update");

                            System.out.println("latest_update---------" + latest_update.toString());
                            System.out.println("latest_deals---------" + latest_deals.toString());

                            for (int i = 0; i < latest_deals.size(); i++) {

                                JsonObject jsonObject_latest_post = (JsonObject) latest_deals.get(i);

                                System.out.println("jsonArray jsonObject2" + jsonObject_latest_post.toString());

                                String product_id = jsonObject_latest_post.get("id").getAsString();

                                String product_name = jsonObject_latest_post.get("prodname").getAsString();

                                String imageurl = jsonObject_latest_post.get("image_url").getAsString();

                                System.out.println("imageurl--------------" + imageurl + product_name);

                                AndroidUtils.showErrorLog(context, "Product_iamge", imageurl);

                                String productlocation = jsonObject_latest_post.get("city_name").getAsString() + "," +
                                        jsonObject_latest_post.get("state_name").getAsString() + "," +
                                        jsonObject_latest_post.get("country_name").getAsString();
                                String categoryName = jsonObject_latest_post.get("category_name").getAsString();
                                commonDatas_latestpost.add(new CommonData(product_id, product_name, "", imageurl, productlocation, categoryName));

                            }

                            commonAdapter_latestpost = new CommonAdapter(context, commonDatas_latestpost, "list", "latestdeals");
                            recyclerLatestDeals.setAdapter(commonAdapter_latestpost);


                            commonAdapter_latestpost.notifyDataSetChanged();

                            for (int i = 0; i < latest_update.size(); i++) {

                                JsonObject jsonObject_latest_update = (JsonObject) latest_update.get(i);

                                System.out.println("jsonArray jsonObject2" + jsonObject_latest_update.toString());

                                String update_product_id = jsonObject_latest_update.get("id").getAsString();

                                String update_product_name = jsonObject_latest_update.get("prodname").getAsString();

                                System.out.println("update_product_name-----------------" + update_product_name);

                                String imageurl = jsonObject_latest_update.get("image_url").getAsString();

                                String productlocation = jsonObject_latest_update.get("city_name").getAsString() + "," +
                                        jsonObject_latest_update.get("state_name").getAsString() + "," +
                                        jsonObject_latest_update.get("country_name").getAsString();

                                String categoryName = jsonObject_latest_update.get("category_name").getAsString();
                                commonDatas_latestupdate.add(new CommonData(update_product_id, update_product_name, "", imageurl, productlocation, categoryName));

                            }

                            commonAdapter_latestproduct = new CommonAdapter(context, commonDatas_latestupdate, "gridtype", "latestupdate");
                            recyclerlatestupdate.setAdapter(commonAdapter_latestproduct);
                            commonAdapter_latestproduct.notifyDataSetChanged();
                            if (scrollView.getVisibility() == View.INVISIBLE) {
                                scrollView.setVisibility(View.VISIBLE);
                            }

                            progress_handler.hide();
                            coordinatorLayout.setVisibility(View.VISIBLE);

                        } else {


                            progress_handler.hide();
                            coordinatorLayout.setVisibility(View.VISIBLE);
                            connection_problem_message();

                        }
                    }

                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        banner_timer.cancel();
    }


    private void replaceFragment(Fragment newFragment, String tag) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_layout, newFragment, tag).addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_viewall_latest_post:
                go_to_product_list_activity();
                break;

            case R.id.rl_viewall_latest_update:
                go_to_latest_update_list_activity();
                break;
        }


    }

    private void go_to_product_list_activity() {
        if (home_result != null) {
            boolean permission_status = CheckPermission.checkPermissions((Activity) context);

            if (permission_status) {
                mylocation = new Mylocation(context);
                LocationManagerCheck locationManagerCheck = new LocationManagerCheck(context);
                if (locationManagerCheck.isLocationServiceAvailable()) {
                    String currentLatitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0.0");
                    String currentLongitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0.0");
                    appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_STATE_NAME.toString(), "Haryana");

                    Intent i = new Intent(context, ParticularActivity.class);
                    i.putExtra("url", getResources().getString(R.string.webservice_base_url) + "/latestpost");
                    i.putExtra("latitude", currentLatitude);
                    i.putExtra("longitude", currentLongitude);
                    context.startActivity(i);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

                } else {
                    locationManagerCheck.createLocationServiceError((Activity) context);
                }

            } else {
                AndroidUtils.showErrorLog(context, "error in permission");
            }

        }
    }

    private void go_to_latest_update_list_activity() {
        if (home_result != null) {

            boolean permission_status = CheckPermission.checkPermissions((Activity) context);

            if (permission_status) {
                mylocation = new Mylocation(context);
                LocationManagerCheck locationManagerCheck = new LocationManagerCheck(context);
                if (locationManagerCheck.isLocationServiceAvailable()) {
                    String currentLatitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0.0");
                    String currentLongitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0.0");
                    appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_STATE_NAME.toString(), "Haryana");

                    Intent i = new Intent(context, ParticularActivity.class);
                    i.putExtra("url", getResources().getString(R.string.webservice_base_url) + "/latestupdate");
                    i.putExtra("latitude", currentLatitude);
                    i.putExtra("longitude", currentLongitude);
                    context.startActivity(i);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

                } else {
                    locationManagerCheck.createLocationServiceError((Activity) context);
                }

            } else {
                AndroidUtils.showErrorLog(context, "error in permission");
            }


        }
    }


    public void connection_problem_message() {
        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });

        rl_retry.setVisibility(View.VISIBLE);


    }


}
