package com.aapkatrade.buyer.user_dashboard.my_profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.user_dashboard.changepassword.ChangePassword;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePreviewActivity extends AppCompatActivity {

    private TextView textViewName, tvMobile, tvEmail, tvUserType;
    private LinearLayout linearLayoutLagout, linearLayoutResetpassword, linearLayoutProfileVideo;
    private AppSharedPreference appSharedPreference;
    private ImageView btnEdit, imageViewProfileVideo, imgvew_fb, imgvew_twitter, imgvew_whatsapp, imgvew_google_plus, img_sms;
    private Context context;
    private CircleImageView userimage;
    String usertype;
    int profile_preview_activity;
    RelativeLayout relativeLayoutProfile;
    private String my_profile_gif;

    private ProgressBarHandler p_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_preview);

        context = ProfilePreviewActivity.this;

        appSharedPreference = new AppSharedPreference(this);

        setUpToolBar();

        setup_layout();

    }

    private void setup_layout() {

        relativeLayoutProfile = (RelativeLayout) findViewById(R.id.relativeLayoutProfile);

        imageViewProfileVideo = (ImageView) findViewById(R.id.imageViewProfileVideo);
        p_handler = new ProgressBarHandler(context);

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("2")) {

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString(), "").toString().equals("")) {

                Log.e("shared-----", "");
            } else {

                p_handler.show();
                my_profile_gif = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString());
                Ion.with(imageViewProfileVideo).load(my_profile_gif).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView result) {
                        p_handler.hide();
                    }
                });








               /* Picasso.with(context)
                        .load(app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                        .error(R.drawable.navigation_profile_bg)
                        .placeholder(R.drawable.navigation_profile_bg)
                        .error(R.drawable.navigation_profile_bg)
                        .into(imageViewProfile);*/
            }

        }


        imgvew_fb = (ImageView) findViewById(R.id.imgvew_fb);
        imgvew_twitter = (ImageView) findViewById(R.id.imgvew_fb);
        imgvew_whatsapp = (ImageView) findViewById(R.id.imgvew_whatsapp);
        imgvew_google_plus = (ImageView) findViewById(R.id.imgvew_google_plus);
        img_sms = (ImageView) findViewById(R.id.img_sms);
        img_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_share("com.android.sms");
            }
        });
        imgvew_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_share("com.google.android.apps.plus");
            }
        });

        imgvew_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_share("com.whatsapp");
            }
        });

        imgvew_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call_share("com.facebook.katana");

            }
        });
        imgvew_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_share("com.twitter.android");
            }
        });


        userimage = (CircleImageView) findViewById(R.id.imageviewpp);

        btnEdit = (ImageView) findViewById(R.id.btnEdit);

        linearLayoutLagout = (LinearLayout) findViewById(R.id.linearLayoutLogout);

        linearLayoutResetpassword = (LinearLayout) findViewById(R.id.linearLayoutResetpassword);

        imageViewProfileVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(ProfilePreviewActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_url", appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), "").toString());

                */
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString())), "video/mp4");
                startActivity(intent);


            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent profile_edit = new Intent(ProfilePreviewActivity.this, MyProfileActivity.class);
                startActivity(profile_edit);
            }


        });

        linearLayoutLagout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_shared_pref("notlogin", "notlogin", "notlogin", "profile_pic", "");
                Intent Homedashboard = new Intent(ProfilePreviewActivity.this, HomeActivity.class);
                Homedashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Homedashboard);

            }
        });


        linearLayoutResetpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent change_password = new Intent(ProfilePreviewActivity.this, ChangePassword.class);
                startActivity(change_password);
            }
        });


        textViewName = (TextView) findViewById(R.id.textViewName);

        tvMobile = (TextView) findViewById(R.id.tvMobile);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvUserType = (TextView) findViewById(R.id.tvUserType);

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            String Username = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not");
            String Emailid = appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");

            String user_image = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "");

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains(SharedPreferenceConstants.USER_TYPE_BUYER.toString())) {
                usertype = "Buyer";
            }

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains(SharedPreferenceConstants.USER_TYPE_SELLER.toString())) {

                if (appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString(), "").toString().equals("")) {

                    Log.e("shared-----", "");
                } else {

                    p_handler.show();
                    my_profile_gif = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_ViDEO_GIF.toString());
                    Ion.with(imageViewProfileVideo).load(my_profile_gif).setCallback(new FutureCallback<ImageView>() {
                        @Override
                        public void onCompleted(Exception e, ImageView result) {
                            p_handler.hide();
                        }
                    });


                }
                usertype = "Seller";
            } else if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("3")) {
                usertype = "Associate";
            }

            Log.e("user_image", user_image);

            textViewName.setText(Username);

            tvEmail.setText(Emailid);

            Log.e("user_image2", user_image);
            tvUserType.setText(usertype);
            AndroidUtils.showErrorLog(context, "Image URL onCreate", appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""));


            if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))) {

                Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                        .error(R.drawable.ic_profile_user)
                        .into(userimage);
            }

        }
    }

    private void call_share(String s) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        // Uri screenshotUri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/" + R.drawable.ic_app_icon);
        String strShareMessage = "\nLet me recommend you this application\n\n";
        strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=com.aapkatrade.buyer";

        // share.setType("image");
        //  share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        // share.setPackage(s);
        share.putExtra(Intent.EXTRA_TEXT, strShareMessage);


        startActivity(Intent.createChooser(share, "Share using"));


    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    public void save_shared_pref(String user_id, String user_name, String email_id, String profile_pic, String user_type) {
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), user_id);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), user_name);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), email_id);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), user_type);
    }

    /*
    @Override
    protected void onRestart() {
        super.onRestart();

        AndroidUtils.showErrorLog(context, "Image URL onRestart" , appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""));

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {
            textViewName.setText(appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not"));
            tvEmail.setText(appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not"));
            Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                    .error(R.drawable.ic_profile_user)
                    .into(userimage);
            tvUserType.setText(getString(R.string.wecome_buyer));
        }
      }*/


    @Override
    protected void onResume() {
        super.onResume();

        if (profile_preview_activity == 1) {

            profile_preview_activity = 2;
        } else {

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null) {

                String Username = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not");

                String Emailid = appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");

                String user_image = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "");

                if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("1")) {
                    usertype = "Buyer";
                } else if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("2")) {
                    usertype = "Seller";
                } else if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("3")) {
                    usertype = "Associate";
                }

                Log.e("user_image", user_image);

                textViewName.setText(Username);

                tvEmail.setText(Emailid);

                if (usertype.equals("Seller")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), "").toString().equals("")) {
                        Log.e("shared-----", "");
                    } else {
                        Picasso.with(context)
                                .load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                                .error(R.drawable.navigation_profile_bg)
                                .placeholder(R.drawable.navigation_profile_bg)
                                .error(R.drawable.navigation_profile_bg)
                                .into(imageViewProfileVideo);
                    }
                }

                Log.e("user_image2", user_image);
                tvUserType.setText(usertype);
                AndroidUtils.showErrorLog(context, "Image URL onCreate", appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""));


                if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))) {

                    Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                            .error(R.drawable.ic_profile_user)
                            .into(userimage);
                }

            }

        }

    }


}


