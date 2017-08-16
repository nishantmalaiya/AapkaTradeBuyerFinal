package com.aapkatrade.buyer.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.chat.ChatActivity;
import com.aapkatrade.buyer.contact_us.ContactUsActivity;
import com.aapkatrade.buyer.dialogs.ChatDialogFragment;
import com.aapkatrade.buyer.dialogs.track_order.TrackOrderDialog;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.login.LoginDashboard;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aapkatrade.buyer.home.aboutus.AboutUsFragment;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.home.navigation.NavigationFragment;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.location.Mylocation;
import com.aapkatrade.buyer.user_dashboard.UserDashboardFragment;
import com.aapkatrade.buyer.user_dashboard.my_profile.ProfilePreviewActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private NavigationFragment drawer;
    private Toolbar toolbar;
    private DashboardFragment homeFragment;
    private AboutUsFragment aboutUsFragment;
    private Context context;
    public static String shared_pref_name = "aapkatrade";
    private AppConfig aa;
    private AHBottomNavigation bottomNavigation;
    private CoordinatorLayout coordinatorLayout;
    private UserDashboardFragment userDashboardFragment;
    private ProgressBar progressBar;
    private Boolean permission_status;
    public static String userid, username;
    private NestedScrollView scrollView;
    private float initialX, initialY;
    public static RelativeLayout rl_main_content, rlTutorial;
    private AppSharedPreference appSharedPreference;
    private final int SPEECH_RECOGNITION_CODE = 1;
    private Mylocation mylocation;
    private boolean doubleBackToExitPressedOnce = false;
    private LinearLayout linearlayout_home;
    private ProgressBarHandler progressBarHandler;
    public TextView countTextView;
    private FrameLayout redCircle;
    public static TextView tvCartCount;
    int home_activity = 1;
    ImageView logoWord;
    RelativeLayout ChatContainer;
    String chatid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preInit();

        setContentView(R.layout.activity_homeactivity);
        initView();


    }

    private void initView() {

        rl_main_content = (RelativeLayout) findViewById(R.id.rl_main_content);

        progressBarHandler = new ProgressBarHandler(this);

        linearlayout_home = (LinearLayout) findViewById(R.id.ll_toolbar_container_home);

        appSharedPreference = new AppSharedPreference(HomeActivity.this);

        //prefs = getSharedPreferences(shared_pref_name, Activity.MODE_PRIVATE);
        context = this;
        ChatContainer = (RelativeLayout) findViewById(R.id.container_chat_icon);
        rlTutorial = (RelativeLayout) findViewById(R.id.rlFirstTime);
        //  permissions  granted.
        setupToolBar();
        //setupNavigation();
        setupNavigationCustom();
        setupDashFragment();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        setup_bottomNavigation();
        AppConfig.deleteCache(HomeActivity.this);


        if (permission_status && (rl_main_content.getVisibility() == View.GONE)) {

            rl_main_content.setVisibility(View.VISIBLE);


        }


        ChatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_chat();
            }
        });
    }

    private void preInit() {
        AppConfig.set_defaultfont(HomeActivity.this);

        aboutUsFragment = new AboutUsFragment();

        userDashboardFragment = new UserDashboardFragment();

        permission_status = CheckPermission.checkPermissions(HomeActivity.this);
    }


    private void setupNavigationCustom() {
        drawer = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        drawer.setup(R.id.fragment, (DrawerLayout) findViewById(R.id.drawer), toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

        final MenuItem alertMenuItem = menu.findItem(R.id.cart_total_item);

        RelativeLayout badgeLayout = (RelativeLayout) alertMenuItem.getActionView();

        tvCartCount = (TextView) badgeLayout.findViewById(R.id.tvCartCount);

        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });


        return true;
    }




    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(null);
        logoWord = (ImageView) toolbar.findViewById(R.id.logoWord);
    }

    private void replaceFragment(Fragment newFragment, String tag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_layout, newFragment, tag).addToBackStack(tag);
        transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        transaction.commit();
    }

    private void setupDashFragment() {
        if (homeFragment == null) {
            homeFragment = new DashboardFragment();
        }
        String tagName = homeFragment.getClass().getName();
        replaceFragment(homeFragment, tagName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        switch (id) {
            case R.id.cart_total_item:

                // Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();

                if (appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0) == 0) {
                    AndroidUtils.showToast(context, "My Cart have no items please add items in cart.");
                } else {
                    Intent intent = new Intent(HomeActivity.this, MyCartActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.login:


                if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {

                    Intent i = new Intent(HomeActivity.this, LoginDashboard.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    break;
                } else {
                    Intent i = new Intent(HomeActivity.this, ProfilePreviewActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                    break;
                }


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DashboardFragment dashboardFragment = (DashboardFragment) fm.findFragmentByTag(homeFragment.getClass().getName());
        AboutUsFragment showaboutUsFragment = (AboutUsFragment) fm.findFragmentByTag(aboutUsFragment.getClass().getName());

        UserDashboardFragment showuserdashboardfragment = (UserDashboardFragment) fm.findFragmentByTag(userDashboardFragment.getClass().getName());

        if (dashboardFragment != null && dashboardFragment.isVisible()) {

            double_back_pressed("finish");
            //finish();

            Log.e("myfragment_visible", "myfragment visible");
        } else if (showaboutUsFragment != null && showaboutUsFragment.isVisible()) {
            double_back_pressed("finish");
            //finish();
            Log.e("showabout visible", "showaboutUsFragment visible");
        } else if (showuserdashboardfragment != null && showuserdashboardfragment.isVisible()) {
            double_back_pressed("finish");
            //finish();
            Log.e("userdashboard visible", "userdashboard visible");
        } else {

            double_back_pressed("onbackpressed");
            // super.onBackPressed();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CheckPermission.MULTIPLE_PERMISSIONS: {


                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()) == 1) {
                        rlTutorial.setVisibility(View.GONE);
                    } else {
                        appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString(), 1);
                        rlTutorial.setVisibility(View.VISIBLE);


                        rlTutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rlTutorial.setVisibility(View.GONE);
                            }
                        });
                    }
                    Log.e("permission_granted", "permission_granted");
                    // permissions granted.
                } else {
                    Log.e("permission_requried", "permission_requried");
                    // no permissions granted.
                }
                return;
            }
        }
    }


    private void setup_bottomNavigation() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordination_home_activity);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        scrollView = (NestedScrollView) findViewById(R.id.scroll_main);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_navigation_home, R.color.dark_green);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_home_dashboard_aboutus, R.color.orange);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.track, R.color.dark_green);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_home_bottom_account, R.color.dark_green);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_chat, R.color.dark_green);

        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d("DemoActivity", "BottomNavigation Position: " + y);
            }
        });
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setSelectedBackgroundVisible(true);
        bottomNavigation.setAccentColor(ContextCompat.getColor(context, R.color.white));
        bottomNavigation.setInactiveColor(Color.parseColor("#000000"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setColored(false);
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {

                    case 0:
                        if (homeFragment == null) {
                            homeFragment = new DashboardFragment();
                        }
                        String tagName = homeFragment.getClass().getName();
                        replaceFragment(homeFragment, tagName);
                        break;

                    case 1:

                        Log.e("time  fragment", String.valueOf(System.currentTimeMillis()));

                        if (aboutUsFragment == null) {
                            aboutUsFragment = new AboutUsFragment();
                        }
                        String aboutUsFragment_tagName = aboutUsFragment.getClass().getName();
                        replaceFragment(aboutUsFragment, aboutUsFragment_tagName);

                        break;


                    case 2:

                        FragmentManager fm = getSupportFragmentManager();
                        TrackOrderDialog track_orderDialog = new TrackOrderDialog();
                        track_orderDialog.show(fm, "Track Order");


                        break;
                    case 3:
                        if (userDashboardFragment == null) {
                            userDashboardFragment = new UserDashboardFragment();
                        }

                        // startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not").contains("not")) {
                            startActivity(new Intent(HomeActivity.this, LoginDashboard.class));
                        } else {
                            Log.e("hiiii", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "not"));
                            String tagName_dashboardFragment = userDashboardFragment.getClass().getName();
                            replaceFragment(userDashboardFragment, tagName_dashboardFragment);
                            //showOrHideBottomNavigation(true);
                        }

                        break;

                    case 4:

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        ChatDialogFragment chatDialogFragment = new ChatDialogFragment(context);
                        chatDialogFragment.show(fragmentManager, "Chat Dialog");


                        break;


                }
                // Do something cool here...
                return true;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    AndroidUtils.showToast(context, text);
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void double_back_pressed(String type) {
        if (type.contains("finish")) {
            if (doubleBackToExitPressedOnce) {

                finish();

                return;
            }

            this.doubleBackToExitPressedOnce = true;
            AndroidUtils.showToast(context, "Press again to quit.");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            if (doubleBackToExitPressedOnce) {


                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            AndroidUtils.showToast(context, "Please click BACK again to exit");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        AndroidUtils.showErrorLog(context, "testing", appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()));
      /*  if (appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()) == 1) {
            rlTutorial.setVisibility(View.GONE);
        } else {
            appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString(), 1);
            rlTutorial.setVisibility(View.VISIBLE);


            rlTutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlTutorial.setVisibility(View.GONE);
                }
            });
        }*/

        if (home_activity == 1) {

            home_activity = 2;
        } else {
            if (tvCartCount != null) {
                tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
            }
        }

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            if (NavigationFragment.profilePic != null && Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))) {
                Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                        .error(R.drawable.ic_profile_user)
                        .into(NavigationFragment.profilePic);
            }
        }

        if (NavigationFragment.mDrawerLayout != null) {
            NavigationFragment.mDrawerLayout.closeDrawer(Gravity.LEFT, false);
        }
    }

    public void open_chat() {


        chatid = appSharedPreference.getSharedPref(SharedPreferenceConstants.TEMP_CHAT_ID.toString(), "");
        AndroidUtils.showErrorLog(HomeActivity.this, "%%%%%%%%" + chatid);
        if (Validation.isEmptyStr(chatid)) {
            ChatDialogFragment serviceEnquiry = new ChatDialogFragment(context);


            FragmentManager fm = getSupportFragmentManager();
            serviceEnquiry.show(fm, "enquiry");


        } else {
            callChatListWebService(chatid);


        }


    }



    private void callChatListWebService(String ChatId) {


        progressBarHandler.show();

        String urlValidateUser = getResources().getString(R.string.webservice_base_url) + "/chat_list";


        Ion.with(context)
                .load(urlValidateUser)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("chat_id", ChatId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {

                            AndroidUtils.showErrorLog(context, result.toString());
                            progressBarHandler.hide();


                            if (result.get("error").getAsString().contains("false")) {

                                JsonArray jsonarray_result = result.getAsJsonArray("result");
                                String jsonarray_string = jsonarray_result.toString();
                                AndroidUtils.showErrorLog(context, "Data Stored", jsonarray_string);
                                appSharedPreference.setSharedPref(SharedPreferenceConstants.TEMP_CHAT_ID.toString(), chatid);


                                Intent i = new Intent(context, ChatActivity.class);
                                i.putExtra("chatid", chatid);
                                i.putExtra("className", "");
                                i.putExtra("jsonarray_string", jsonarray_string);

                                startActivity(i);
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, e.toString());
                            progressBarHandler.hide();
                        }

                    }
                });


    }


}



