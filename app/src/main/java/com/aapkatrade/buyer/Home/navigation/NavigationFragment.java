package com.aapkatrade.buyer.Home.navigation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.Home.navigation.adapter.NavigationAdapter;
import com.aapkatrade.buyer.Home.navigation.entity.CategoryHome;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.CallWebService;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.TaskCompleteReminder;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.login.LoginActivity;
import com.aapkatrade.buyer.login.LoginDashboard;
import com.aapkatrade.buyer.privacypolicy.PrivacyPolicyActivity;
import com.aapkatrade.buyer.termandcondition.TermsAndConditionActivity;
import com.aapkatrade.buyer.user_dashboard.my_profile.MyProfileActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment {
    public static ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    private View view;
    private int lastExpandedPosition = -1;
    private AppSharedPreference appSharedpreference;
    public static final String PREFS_NAME = "call_recorder";
    private List<String> categoryids;
    private List<String> categoryname;
    private Context context;
    private TextView textViewName, emailid;
    private NavigationAdapter category_adapter;
    public ArrayList<CategoryHome> listDataHeader = new ArrayList<>();
    private RelativeLayout rlprofilepic, rlLogout, rlPolicy, rlTerms, rlInvite;
    private View rlMainContent;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView navigationRecycleview;
    private LinearLayoutManager navigationLinearLayoutManager;
    private ImageView navigationClose;
    public static CircleImageView profilePic;


    public NavigationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation, container, false);
        context = getContext();
        progressBarHandler = new ProgressBarHandler(context);
        appSharedpreference = new AppSharedPreference(context);

        initView(view);
        return view;


    }


    private void initView(View view) {


        rlprofilepic = (RelativeLayout) view.findViewById(R.id.navigation_profile);
        rlprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not").contains("not")) {
                    startActivity(new Intent(getActivity(), LoginDashboard.class));
                } else {
                    Log.e("hiiii", appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not"));
                    startActivity(new Intent(getActivity(), MyProfileActivity.class));

                    //showOrHideBottomNavigation(true);
                }

            }
        });

        rlInvite = (RelativeLayout) view.findViewById(R.id.rl_invite);

        rlInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                // Uri screenshotUri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/" + R.drawable.ic_app_icon);
                String strShareMessage = "\nLet me recommend you this application\n\n";
                strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                // share.setType("image");
                //  share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                share.putExtra(Intent.EXTRA_TEXT, strShareMessage);


                startActivity(Intent.createChooser(share, "Share using"));


            }
        });

        profilePic = (CircleImageView) view.findViewById(R.id.circular_profile_image_home);
        navigationClose = (ImageView) view.findViewById(R.id.navigation_close);
        rlLogout = (RelativeLayout) view.findViewById(R.id.rl_logout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPref("notlogin", "notlogin", "notlogin", "notlogin");
                Intent Homedashboard = new Intent(getActivity(), HomeActivity.class);
                Homedashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Homedashboard);

            }
        });

        rlTerms = (RelativeLayout) view.findViewById(R.id.rl_terms);
        rlTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TermsAndConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        rlPolicy = (RelativeLayout) view.findViewById(R.id.rl_policy);
        rlPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrivacyPolicyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        navigationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                HomeActivity.tvCartCount.setText(String.valueOf(appSharedpreference.getSharedPrefInt("cart_count", 0)));
            }
        });
        categoryname = new ArrayList<>();
        categoryids = new ArrayList<>();
        textViewName = (TextView) view.findViewById(R.id.tv_name);
        emailid = (TextView) view.findViewById(R.id.tv_email);
        prepareListData();
        navigationLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        navigationRecycleview = (RecyclerView) this.view.findViewById(R.id.recycle_view_navigation);
        navigationRecycleview.setLayoutManager(navigationLinearLayoutManager);

//        rlCategory = (RelativeLayout) this.view.findViewById(R.id.rl_category);

        if (appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not") != null) {
            String userName = appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not");
            String emailId = appSharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");


            if (userName.contains("not")) {
                rlLogout.setVisibility(View.GONE);
                setData(getString(R.string.welcomeguest), "");
            } else {
                rlLogout.setVisibility(View.VISIBLE);
                setData(userName, emailId);

                String a = appSharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

                if (Validation.isNonEmptyStr(a)) {
                    Picasso.with(context)
                            .load(a)
                            .error(R.drawable.banner)
                            .placeholder(R.drawable.default_noimage)
                            .error(R.drawable.default_noimage)
                            .into(profilePic);
                }


            }
        } else {
            AndroidUtils.showErrorLog(getActivity(), "sharedpref null");
        }
    }

    public void setup(int id, final DrawerLayout drawer, Toolbar toolbar) {

        view = getActivity().findViewById(id);

        mDrawerLayout = drawer;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.drawer_open, R
                .string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                rlMainContent = getActivity().findViewById(R.id.rl_main_content);
                rlMainContent.setBackgroundColor(Color.parseColor("#33000000"));
                hideSoftKeyboard(getActivity());
                super.onDrawerOpened(drawerView);
                if (appSharedpreference != null) {
                    HomeActivity.tvCartCount.setText(String.valueOf(appSharedpreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
                } else {
                    HomeActivity.tvCartCount.setText("0");
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (appSharedpreference != null) {
                    HomeActivity.tvCartCount.setText(String.valueOf(appSharedpreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
                    System.out.println("hi I am Sachin" + String.valueOf(appSharedpreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
                } else {
                    HomeActivity.tvCartCount.setText("0");
                }
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        toolbar.setNavigationIcon(R.drawable.menu_24dp);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(context, R.drawable.menu24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
                System.out.println("I am Sachin");
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerLayout.closeDrawers();
    }

    @SuppressWarnings("ConstantConditions")
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }


    public void setData(String username, String email) {
        textViewName.setText(username);
        emailid.setText(email);
    }

    private void prepareListData() {
        getCategory();
    }

    private void getCategory() {

        HashMap<String, String> webservice_body_parameter = new HashMap<>();
        webservice_body_parameter.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        webservice_body_parameter.put("type", "category");

        HashMap<String, String> webservice_header_type = new HashMap<>();
        webservice_header_type.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        CallWebService.getcountrystatedata(context, "category", getResources().getString(R.string.webservice_base_url) + "/dropdown", webservice_body_parameter, webservice_header_type);

        CallWebService.taskCompleteReminder = new TaskCompleteReminder() {

            @Override
            public void Taskcomplete(JsonObject data) {

                if (data != null) {
                    Log.e("data", data.toString());
                    JsonObject jsonObject = data.getAsJsonObject();
                    JsonArray jsonResultArray = jsonObject.getAsJsonArray("result");

                    for (int i = 0; i < jsonResultArray.size(); i++) {

                        JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                        CategoryHome categoryHome = new CategoryHome(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString(), jsonObject1.get("icon").getAsString());

                        listDataHeader.add(categoryHome);

                        Log.e("listDataHeader_cate", categoryHome.toString() + "----------------->" + categoryHome.getCategoryId() + "----------------->" + categoryHome.getCategoryName());
                    }

                    if (listDataHeader != null) {
                        Collections.sort(listDataHeader, new Comparator<CategoryHome>() {
                            @Override
                            public int compare(CategoryHome o1, CategoryHome o2) {
                                return o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName());
                            }
                        });
                    }
                }
                setRecycleviewAdapter();


            }


        };


    }

    private void setRecycleviewAdapter() {
        if (listDataHeader.size() != 0) {
            category_adapter = new NavigationAdapter(context, listDataHeader);
            navigationRecycleview.setAdapter(category_adapter);
        }
    }


    public void saveSharedPref(String user_id, String user_name, String email_id, String profile_pic) {
        appSharedpreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), user_id);
        appSharedpreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), user_name);
        appSharedpreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), email_id);
        appSharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            AndroidUtils.showErrorLog(context, "USERNAME***--->>" + appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin"));
            String userName = appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin");
            String emailId = appSharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "notlogin");

            if (userName.contains("notlogin")) {
                setData(getString(R.string.welcomeguest), "");

                rlLogout.setVisibility(View.GONE);

                Log.e("Shared_pref2", "null" + userName);
            } else {
                rlLogout.setVisibility(View.VISIBLE);

                setData(userName, emailId);
            }
        } else {
            Log.e("Shared_pref1", "null");
        }


    }
}
