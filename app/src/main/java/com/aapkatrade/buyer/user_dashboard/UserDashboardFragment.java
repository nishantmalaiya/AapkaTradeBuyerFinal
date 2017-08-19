package com.aapkatrade.buyer.user_dashboard;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.my_profile.MyProfileActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashboardFragment extends Fragment {

    private RecyclerView dashboardlist;
    private DashboardAdapter dashboardAdapter;
    private ArrayList<DashboardData> dashboardDatas = new ArrayList<>();
    private AppSharedPreference appSharedPreference;
    private TextView tvMobile, tvEmail, textViewName, tvUserType;
    private ImageView btnEdit;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView.LayoutManager layoutManager;
    private CircleImageView imageviewpp;
    private String user_image;
    ImageView imageViewProfileVideo;


    public UserDashboardFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dashboard, container, false);
        appSharedPreference = new AppSharedPreference(getActivity());
        progressBarHandler = new ProgressBarHandler(getActivity());
        tvUserType = (TextView) v.findViewById(R.id.tvUserType);

        imageViewProfileVideo = (ImageView) v.findViewById(R.id.imageViewProfileVideo);

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("2")) {
            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), "").toString().equals("")) {

                Log.e("shared-----", "");
            } else {

                String profile_video_gif = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString());
                Ion.with(imageViewProfileVideo).load(profile_video_gif);
                Log.e("profile_video_gif-----", profile_video_gif + "");
              /*  Picasso.with(getActivity())
                        .load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                        .error(R.drawable.navigation_profile_bg)
                        .placeholder(R.drawable.navigation_profile_bg)
                        .error(R.drawable.navigation_profile_bg)
                        .into(imageViewProfileVideo);*/
            }



            imageViewProfileVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), "").toString().equals("")) {

                        Log.e("shared-----", "");
                    } else {

                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        intent.setDataAndType(Uri.parse(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), "").toString()), "video/*");

                        startActivity(Intent.createChooser(intent, "Complete action using"));




              /*  Picasso.with(getActivity())
                        .load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                        .error(R.drawable.navigation_profile_bg)
                        .placeholder(R.drawable.navigation_profile_bg)
                        .error(R.drawable.navigation_profile_bg)
                        .into(imageViewProfileVideo);*/
                    }

                }
            });
        }

        setup_layout(v);

        dashboardlist = (RecyclerView) v.findViewById(R.id.dashboardlist);

        layoutManager = new LinearLayoutManager(getActivity());
        setup_data();
        dashboardlist.setNestedScrollingEnabled(false);

        return v;
    }

    private void setup_layout(View v) {
        imageviewpp = (CircleImageView) v.findViewById(R.id.imageviewpp);

        String a = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "demo");

        if (Validation.isNonEmptyStr(a)) {
            Picasso.with(getContext())
                    .load(a)
                    .error(R.drawable.banner)
                    .placeholder(R.drawable.default_noimage)
                    .error(R.drawable.default_noimage)
                    .into(imageviewpp);
        }
        textViewName = (TextView) v.findViewById(R.id.textViewName);
        tvMobile = (TextView) v.findViewById(R.id.tvMobile);
        tvEmail = (TextView) v.findViewById(R.id.tvEmail);
        btnEdit = (ImageView) v.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(i);
            }
        });

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not") != null) {
            String Username = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not");
            String Emailid = appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");
            if (Username.contains("not")) {
                textViewName.setText("");
                tvEmail.setText("");
            } else {
                textViewName.setText(Username);
                tvEmail.setText(Emailid);
            }
        }


    }

    private void setup_data() {
        dashboardDatas.clear();
        try {

            if (appSharedPreference.sharedPreferences != null) {

                String userid = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "0");

                String user_detail_webserviceurl = (getResources().getString(R.string.webservice_base_url)) + "/userdata";

                userdata_webservice(user_detail_webserviceurl, appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), SharedPreferenceConstants.USER_TYPE.toString()), userid);
            } else {
                Log.e("null_sharedPreferences", "sharedPreferences");
            }

        } catch (Exception e) {
        }


    }

    public void userdata_webservice(String url, final String user_type, String user_id) {
        Log.e("url", url);
        Log.e("user_type", user_type);
        Log.e("user_id", user_id);
        progressBarHandler.show();
        Ion.with(getActivity())
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", user_type)
                .setBodyParameter("id", user_id)
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(getContext(), "result_myProfile", result);
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(getContext(), "result_myProfile", "result_myProfile is not null ");
                            String order_quantity = result.get("order").getAsString();


                            if (appSharedPreference.getSharedPref((SharedPreferenceConstants.USER_TYPE.toString())).equals(SharedPreferenceConstants.USER_TYPE_BUYER.toString())) {
                                tvUserType.setText("Welcome Buyer");
                                appSharedPreference.setSharedPref(SharedPreferenceConstants.ORDER_QUANTITY.toString(), order_quantity);
                                dashboardDatas.add(new DashboardData("", "My Profile", R.drawable.ic_my_profile, R.drawable.circle_teal, false, ""));
                                dashboardDatas.add(new DashboardData("", "Change Password", R.drawable.ic_change_password, R.drawable.circle_purple, false, ""));
                                dashboardDatas.add(new DashboardData("", "Order", R.drawable.ic_my_order, R.drawable.circle_sienna, true, order_quantity));


                            } else if (appSharedPreference.getSharedPref((SharedPreferenceConstants.USER_TYPE.toString())).equals(SharedPreferenceConstants.USER_TYPE_SELLER.toString())) {
                                tvUserType.setText("Welcome Seller");

                                appSharedPreference.setSharedPref(SharedPreferenceConstants.ORDER_QUANTITY.toString(), order_quantity);
                                dashboardDatas.add(new DashboardData("", "My Profile", R.drawable.ic_my_profile, R.drawable.circle_random_color1_sellerdashboard, false, ""));

                                dashboardDatas.add(new DashboardData("", "Company/Shop List", R.drawable.ic_company_shop_list, R.drawable.circle_random_color2_sellerdashboard, true, result.get("company").getAsString()));


                                dashboardDatas.add(new DashboardData("", "Product Management", R.drawable.ic_add_company,R.drawable.circle_random_color3_sellerdashboard, true, result.get("product").getAsString()));



                                dashboardDatas.add(new DashboardData("", "Service Management", R.drawable.ic_service_management,R.drawable.circle_random_color4_sellerdashboard, false, ""));


                                dashboardDatas.add(new DashboardData("", "Order", R.drawable.ic_my_order,R.drawable.circle_random_color5_sellerdashboard, true, order_quantity));


                                dashboardDatas.add(new DashboardData("", "Enquiry Management", R.drawable.ic_enquiry_management, R.drawable.circle_random_color6_sellerdashboard, true, result.get("enquiry").getAsString()));
                                dashboardDatas.add(new DashboardData("", "Bank Details", R.drawable.ic_bank_details, R.drawable.circle_random_color7_sellerdashboard, false, ""));

                                dashboardDatas.add(new DashboardData("", "Sales Transaction", R.drawable.ic_sales_transaction, R.drawable.circle_random_color8_sellerdashboard, false, ""));
                                dashboardDatas.add(new DashboardData("", "Bill Payment", R.drawable.ic_bill_payment,R.drawable.circle_random_color9_sellerdashboard, false, ""));
                                dashboardDatas.add(new DashboardData("", "Change Password", R.drawable.ic_change_password,R.drawable.circle_random_color4_sellerdashboard, false, ""));
                                dashboardDatas.add(new DashboardData("", "Bill History", R.drawable.ic_bill_history,R.drawable.circle_random_color7_sellerdashboard, false, ""));


                            }
                            dashboardlist.setLayoutManager(layoutManager);
                            dashboardAdapter = new DashboardAdapter(getActivity(), dashboardDatas);
                            dashboardlist.setAdapter(dashboardAdapter);


                        }
                    }


                });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            String Username = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not");
            String Emailid = appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");

            if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))) {
                Picasso.with(getActivity()).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                        .error(R.drawable.ic_profile_user)
                        .into(imageviewpp);
            }

            if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString(), ""))) {


                Ion.with(imageViewProfileVideo).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString(), "")).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {

                    }
                });

            }

            if (Username.equals("notlogin")) {
                textViewName.setText("");
                tvEmail.setText("");
            } else {
                textViewName.setText(Username);
                tvEmail.setText(Emailid);
            }
        }


    }
}