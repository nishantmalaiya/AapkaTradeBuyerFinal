package com.aapkatrade.buyer.Home;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.dialogs.track_order.TrackOrderDialog;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.login.LoginDashboard;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aapkatrade.buyer.Home.aboutus.AboutUsFragment;
import com.aapkatrade.buyer.Home.cart.MyCartActivity;
import com.aapkatrade.buyer.Home.navigation.NavigationFragment;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.contact_us.ContactUsFragment;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.location.Mylocation;
import com.aapkatrade.buyer.login.LoginActivity;
import com.aapkatrade.buyer.user_dashboard.UserDashboardFragment;
import com.aapkatrade.buyer.user_dashboard.my_profile.ProfilePreviewActivity;
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
    private ContactUsFragment contactUsFragment;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rl_main_content = (RelativeLayout) findViewById(R.id.rl_main_content);

        progressBarHandler = new ProgressBarHandler(this);

        linearlayout_home = (LinearLayout) findViewById(R.id.ll_toolbar_container_home);

        appSharedPreference = new AppSharedPreference(HomeActivity.this);

        AppConfig.set_defaultfont(HomeActivity.this);

        aboutUsFragment = new AboutUsFragment();

        contactUsFragment = new ContactUsFragment();

        userDashboardFragment = new UserDashboardFragment();


        permission_status = CheckPermission.checkPermissions(HomeActivity.this);

        if (permission_status) {
            setContentView(R.layout.activity_homeactivity);
            //prefs = getSharedPreferences(shared_pref_name, Activity.MODE_PRIVATE);
            context = this;
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


        } else {
            setContentView(R.layout.activity_homeactivity);
            //prefs = getSharedPreferences(shared_pref_name, Activity.MODE_PRIVATE);
            context = this;
            rlTutorial = (RelativeLayout) findViewById(R.id.rlFirstTime);
            rl_main_content = (RelativeLayout) findViewById(R.id.rl_main_content);
            //  permissions  granted.
            setupToolBar();
            //setupNavigation();
            setupNavigationCustom();
            setupDashFragment();
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            setup_bottomNavigation();
            checked_wifispeed();
            AppConfig.deleteCache(HomeActivity.this);


        }


    }

    private void checked_wifispeed() {
        int a = ConnetivityCheck.get_wifi_speed(this);
        Log.e("a", a + "");
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


    private void setupToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(null);
        ImageView home_link = (ImageView) toolbar.findViewById(R.id.iconHome);
        AndroidUtils.setImageColor(home_link, context, R.color.white);
        home_link.setVisibility(View.GONE);

        logoWord=(ImageView)toolbar.findViewById(R.id.logoWord);
    }

    private void replaceFragment(Fragment newFragment, String tag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_layout, newFragment, tag).addToBackStack(tag);
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
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        DashboardFragment dashboardFragment = (DashboardFragment) fm.findFragmentByTag(homeFragment.getClass().getName());
        ContactUsFragment showcontactUsFragment = (ContactUsFragment) fm.findFragmentByTag(contactUsFragment.getClass().getName());
        AboutUsFragment showaboutUsFragment = (AboutUsFragment) fm.findFragmentByTag(aboutUsFragment.getClass().getName());

        UserDashboardFragment showuserdashboardfragment = (UserDashboardFragment) fm.findFragmentByTag(userDashboardFragment.getClass().getName());

        if (dashboardFragment != null && dashboardFragment.isVisible())
        {

            double_back_pressed("finish");
            //finish();

            Log.e("myfragment_visible", "myfragment visible");
        } else if (showcontactUsFragment != null && showcontactUsFragment.isVisible())
        {
            double_back_pressed("finish");
            // finish();
            Log.e("contact us visible", "contact us visible");

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


    public void loadLocale() {

        String language = appSharedPreference.getSharedPref(SharedPreferenceConstants.LANGUAGE.toString(), "");
        AppConfig.setLocaleFa(HomeActivity.this, language);
        Log.e("language", language);
        // changeLang(language);
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        appSharedPreference.setSharedPref(SharedPreferenceConstants.LANGUAGE.toString(), lang);
        Log.e("language_pref", langPref + "****" + lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CheckPermission.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_about_us, R.color.dark_green);

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
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.dark_green));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setSelectedBackgroundVisible(true);
        bottomNavigation.setAccentColor(getResources().getColor(R.color.white));
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
                        // showOrHideBottomNavigation(true);

                        break;
                    case 1:

                        Log.e("time  fragment", String.valueOf(System.currentTimeMillis()));

                        if (aboutUsFragment == null) {
                            aboutUsFragment = new AboutUsFragment();
                        }
                        String aboutUsFragment_tagName = aboutUsFragment.getClass().getName();
                        replaceFragment(aboutUsFragment, aboutUsFragment_tagName);
                        // showOrHideBottomNavigation(true);
                        break;


                    case 2:

                        FragmentManager fm = getSupportFragmentManager();
                        TrackOrderDialog track_orderDialog = new TrackOrderDialog();
                        track_orderDialog.show(fm, "Track Order");


//                        Intent i =new Intent(HomeActivity.this, Order_detail.class);
//                       i.putExtra("class_name", "");
//                       i.putExtra("result","");
//                        startActivity(i);
                        //goToMyApp(true);


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
                        if (contactUsFragment == null) {
                            contactUsFragment = new ContactUsFragment();
                        }
                        String contact_us_fragment = contactUsFragment.getClass().getName();
                        replaceFragment(contactUsFragment, contact_us_fragment);
                        //showOrHideBottomNavigation(true);
                        break;


                }
                // Do something cool here...
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void setup_scrollview(final NestedScrollView scrollView) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+

            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int pos = scrollView.getChildCount() - 1;
                    if (oldScrollY < scrollY) {

                        showOrHideBottomNavigation(true);
//                    setForceTitleHide(true);
//                    setForceTitleHide(true);
                    } else {
                        showOrHideBottomNavigation(false);
                    }

                    if (oldScrollY == scrollY)
                    {
                        showOrHideBottomNavigation(true);

                    }

                }
            });
        } else {

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                float height;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = event.getAction();
                    float height2 = event.getY();
                    if (action == MotionEvent.ACTION_DOWN) {
                        height = height2;
                    } else if (action == MotionEvent.ACTION_UP) {
                        if (this.height > height2) {
                            Log.e("up", "Scrolled up");
                            showOrHideBottomNavigation(false);
                        } else if (this.height < height2) {
                            Log.e("down", "Scrolled down");
                            showOrHideBottomNavigation(true);
                        }
                    }
                    return false;
                }

            });


            // Pre-Marshmallow
        }


    }

    private void setForceTitleHide(boolean forceTitleHide) {
        AHBottomNavigation.TitleState state = forceTitleHide ? AHBottomNavigation.TitleState.ALWAYS_HIDE : AHBottomNavigation.TitleState.ALWAYS_SHOW;
        bottomNavigation.setTitleState(state);
    }


    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true);
        } else {
            bottomNavigation.hideBottomNavigation(true);
        }

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

    public void goToMyApp(boolean googlePlay) {
        //true if Google Play, false if Amazone Store
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((googlePlay ? "market://details?id=" : "amzn://apps/android?p=") + getPackageName())));
        } catch (ActivityNotFoundException e1) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((googlePlay ? "http://play.google.com/store/apps/details?id=" : "http://www.amazon.com/gp/mas/dl/android?p=") + getPackageName())));
            } catch (ActivityNotFoundException e2) {
                AndroidUtils.showToast(context, "You don't have any app that can open this link.");
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AndroidUtils.showErrorLog(context, "testing", appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()));
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

        if (home_activity == 1) {

            home_activity = 2;
        } else {
            tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
        }

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            if (NavigationFragment.profilePic != null && Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))) {
                Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                        .error(R.drawable.ic_profile_user)
                        .into(NavigationFragment.profilePic);
            }
        }



    }


}



