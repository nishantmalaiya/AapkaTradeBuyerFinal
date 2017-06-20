package com.aapkatrade.buyer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.ConnectivityNotFound;
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.animation_effects.App_animation;

import com.aapkatrade.buyer.location.GeoCoderAddress;
import com.aapkatrade.buyer.location.Mylocation;
import com.aapkatrade.buyer.service.GpsLocationService;
import com.aapkatrade.buyer.welcome.WelcomeActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private ConnetivityCheck connetivityCheck;
    private TextView tv_aapkatrade;
    private ImageView left_image, right_image;
    private ProgressDialog pd;
    private ImageView circle_image;
    //private Custom_progress_bar custom_progress_bar;
    private Mylocation mylocation;
    private GeoCoderAddress geoCoderAddressAsync;
    private String AddressAsync;
    private AppSharedPreference appSharedpreference;
    private GpsLocationService gps;
    BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        appSharedpreference = new AppSharedPreference(MainActivity.this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);.
                }
            }
        };
        displayFirebaseRegId();

        circle_image = (ImageView) findViewById(R.id.circle_image);

        App_animation.circularanimation(circle_image);

        String fontPath = "impact.ttf";

        // text view label
        tv_aapkatrade = (TextView) findViewById(R.id.tv_aapkatrade);
        left_image = (ImageView) findViewById(R.id.left_panel);
        right_image = (ImageView) findViewById(R.id.right_panel);

        //Animation
        App_animation.left_animation(left_image, this);
        App_animation.right_animation(right_image, this);

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);


        // Applying font
        tv_aapkatrade.setTypeface(tf);
        connetivityCheck = new ConnetivityCheck();
        //custom_progress_bar = new Custom_progress_bar(MainActivity.this);
        // custom_progress_bar.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ConnetivityCheck.isNetworkAvailable(MainActivity.this)) {


                    mylocation = new Mylocation(MainActivity.this);
                    LocationManagerCheck locationManagerCheck = new LocationManagerCheck(MainActivity.this);

                    Location location = null;
                    if (locationManagerCheck.isLocationServiceAvailable()) {
                        AndroidUtils.showErrorLog(MainActivity.this, "viewpager welcome", appSharedpreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()));
                        if (appSharedpreference.getSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString()) == 1) {

                            Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            // appSharedpreference.setSharedPrefInt(SharedPreferenceConstants.IS_FIRST_TIME.toString(), 1);
                            Intent mainIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

                        if (pd != null) {
                            pd.dismiss();
                        }

//                        Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
//                        startService(serviceIntent);


                    } else {
                        locationManagerCheck.createLocationServiceError(MainActivity.this);
                    }


                } else {
                    Intent mainIntent = new Intent(MainActivity.this, ConnectivityNotFound.class);
                    mainIntent.putExtra("callerActivity", MainActivity.class.getName());
                    startActivity(mainIntent);
                    finish();
                    if (pd != null) {
                        pd.dismiss();
                    }
                }
                /* Create an Intent that will start the Menu-Activity. */

            }
        }, SPLASH_DISPLAY_LENGTH);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AndroidUtils.showErrorLog(MainActivity.this, "onActivityResult" + requestCode + "***" + resultCode);
        if (resultCode == 0) {
            switch (requestCode) {

                case 1:
                    LocationManagerCheck locationManagerCheck = new LocationManagerCheck(MainActivity.this);
                    if (locationManagerCheck.isLocationServiceAvailable()) {
                        Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(mainIntent);
                        finish();
                        if (pd != null) {
                            pd.dismiss();
                        }

                        gps = new GpsLocationService(MainActivity.this);

                        // check if GPS enabled

                    } else {
                        locationManagerCheck.createLocationServiceError(MainActivity.this);
                    }


                    break;
            }
        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        System.out.println("regId----------------" + regId);

        Log.e("sachin", "Firebase reg id: " + regId);

      /*  if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
