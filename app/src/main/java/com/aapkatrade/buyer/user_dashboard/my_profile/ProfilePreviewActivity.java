package com.aapkatrade.buyer.user_dashboard.my_profile;
import android.content.Context;
import android.content.Intent;
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
import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.user_dashboard.changepassword.ChangePassword;
import com.aapkatrade.buyer.videoplay.VideoPalyActivity;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePreviewActivity extends AppCompatActivity
{

    private TextView  textViewName, tvMobile, tvEmail, tvUserType;
    private LinearLayout linearLayoutLagout, linearLayoutResetpassword,linearLayoutProfileVideo;
    private AppSharedPreference appSharedPreference;
    private ImageView btnEdit,imageViewProfileVideo;
    private Context context;
    private CircleImageView userimage;
    String usertype;
    int profile_preview_activity;
    RelativeLayout relativeLayoutProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_preview);

        context = ProfilePreviewActivity.this;

        appSharedPreference = new AppSharedPreference(this);

        setUpToolBar();

        setup_layout();

    }

    private void setup_layout()
    {

        relativeLayoutProfile = (RelativeLayout) findViewById(R.id.relativeLayoutProfile);

        imageViewProfileVideo = (ImageView)  findViewById(R.id.imageViewProfileVideo);

        userimage = (CircleImageView) findViewById(R.id.imageviewpp);

        btnEdit = (ImageView) findViewById(R.id.btnEdit);

        linearLayoutLagout = (LinearLayout) findViewById(R.id.linearLayoutLogout);

        linearLayoutResetpassword = (LinearLayout) findViewById(R.id.linearLayoutResetpassword);

        imageViewProfileVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfilePreviewActivity.this, VideoPalyActivity.class);
                intent.putExtra("video_url", appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), "").toString());
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

                save_shared_pref("notlogin", "notlogin", "notlogin", "profile_pic");
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

        if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null )
        {
            String Username = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "not");
            String Emailid = appSharedPreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "not");

            String user_image = appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "");

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("1")) {
                usertype = "Buyer";
            }
            else if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("2"))
            {

                if(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), "").toString().equals(""))
                {

                    Log.e("shared-----","");
                }
                else
                    {
                    Picasso.with(context)
                            .load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                            .error(R.drawable.navigation_profile_bg)
                            .placeholder(R.drawable.navigation_profile_bg)
                            .error(R.drawable.navigation_profile_bg)
                            .into(imageViewProfileVideo);
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
            AndroidUtils.showErrorLog(context, "Image URL onCreate" , appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""));


            if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))){

                Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                        .error(R.drawable.ic_profile_user)
                        .into(userimage);
            }

         }
    }

    private void setUpToolBar()
    {
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    public void save_shared_pref(String user_id, String user_name, String email_id, String profile_pic)
    {
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_ID.toString(), user_id);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), user_name);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), email_id);
        appSharedPreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
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
    protected void onResume()
    {
        super.onResume();


        if (profile_preview_activity == 1) {

            profile_preview_activity = 2;
        }
        else
            {

            if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin") != null )
            {

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

                if (usertype.equals("Seller"))
                {
                    if(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), "").toString().equals(""))
                    {
                        Log.e("shared-----","");
                    }
                    else
                    {
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
                AndroidUtils.showErrorLog(context, "Image URL onCreate" , appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""));


                if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "")))
                {

                    Picasso.with(context).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                            .error(R.drawable.ic_profile_user)
                            .into(userimage);
                }

            }

        }

    }


}


