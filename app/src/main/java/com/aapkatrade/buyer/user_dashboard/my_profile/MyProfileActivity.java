package com.aapkatrade.buyer.user_dashboard.my_profile;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.dialogs.UpdateUserDataVerifyDialog;
import com.aapkatrade.buyer.dialogs.loginwithoutregistration.LoginWithoutRegistrationDialog;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductMediaData;
import com.aapkatrade.buyer.videoplay.VideoPlayActivity;
import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.animation;

public class MyProfileActivity extends AppCompatActivity {

    private AppSharedPreference app_sharedpreference;
    public static EditText etMobileNo, etEmail;
    public EditText etFName, etLName, etAddress;
    private ProgressBarHandler p_handler;
    private TextView tvMyProfileDetailHeading;
    private CoordinatorLayout coordinatorlayout_myprofile;
    private Context context;
    private String fname, lname, email, mobile, address, user_image, usertype, videoPath;
    private CircleImageView userImageView;
    private Button btnSave;
    private Bitmap imageForPreview;
    MaterialDialog video_dailog;
    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 111;
    public static String selectedImagePath;
    private final static int SELECT_VIDEO_REQUEST = 100;
    String source_video_folder = null;
    int frameno = 0;
    public static ImageView imageViewProfile, imgEmailEdit, imgMobileEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = MyProfileActivity.this;
        setUpToolBar();

        initView();

    }

    private void initView() {
        app_sharedpreference = new AppSharedPreference(context);

        p_handler = new ProgressBarHandler(context);

        getshared_pref_data();

        Log.e("user_image_profile", user_image);

        coordinatorlayout_myprofile = (CoordinatorLayout) findViewById(R.id.coordinate_myprofile);

        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyProfileActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_url", app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), "").toString());
                startActivity(intent);

            }
        });

        tvMyProfileDetailHeading = (TextView) findViewById(R.id.tvMyProfileDetailHeading);

        userImageView = (CircleImageView) findViewById(R.id.user_imageview);

        etFName = (EditText) findViewById(R.id.etFName);
        tvMyProfileDetailHeading.setText("Hello, " + fname + " To Update your account information.");
        etFName.setText(fname);
        etFName.setSelection(etFName.getText().length());

        etLName = (EditText) findViewById(R.id.etLName);
        etLName.setText(lname);
        etLName.setSelection(etLName.getText().length());


        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setText(email);
        etEmail.setSelection(etEmail.getText().length());


        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etMobileNo.setText(mobile);
        etMobileNo.setSelection(etMobileNo.getText().length());

        etAddress = (EditText) findViewById(R.id.etAddress);
        etAddress.setText(address);


        imgEmailEdit = (ImageView) findViewById(R.id.imgEmailEdit);

        imgMobileEdit = (ImageView) findViewById(R.id.imgMobileEdit);

        imgEmailEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateUserDataVerifyDialog updateUserDataVerifyDialog = new UpdateUserDataVerifyDialog(context);
                updateUserDataVerifyDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoginWithoutRegistrationDialog");

            }

        });

        imgMobileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserDataVerifyDialog updateUserDataVerifyDialog = new UpdateUserDataVerifyDialog(context);
                updateUserDataVerifyDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoginWithoutRegistrationDialog");
            }
        });

        if (etEmail.getText().toString().equals("")) {
            imgEmailEdit.setVisibility(View.INVISIBLE);
        } else {
            etEmail.setKeyListener(null);
            imgEmailEdit.setVisibility(View.VISIBLE);
        }

        if (etMobileNo.getText().toString().equals("")) {
            imgMobileEdit.setVisibility(View.INVISIBLE);
        } else {

            imgMobileEdit.setVisibility(View.VISIBLE);
            etMobileNo.setKeyListener(null);
        }

        etEmail.setKeyListener(null);

        etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().equals("")) {

                    UpdateUserDataVerifyDialog updateUserDataVerifyDialog = new UpdateUserDataVerifyDialog(context);
                    updateUserDataVerifyDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "LoginWithoutRegistrationDialog");

                }
            }
        });

        etMobileNo.setKeyListener(null);

        if (app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "").contains("2")) {

            if (app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), "").toString().equals("")) {

                Log.e("shared-----", "");
            } else {
                Picasso.with(context)
                        .load(app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), ""))
                        .error(R.drawable.navigation_profile_bg)
                        .placeholder(R.drawable.navigation_profile_bg)
                        .error(R.drawable.navigation_profile_bg)
                        .into(imageViewProfile);
            }

        }

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usertype.equals("2")) {
                    showEditVideoPopup();
                } else {
                    picPhoto();

                }

            }
        });

        String imageUrl = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

        if (Validation.isNonEmptyStr(imageUrl)) {
            Picasso.with(context)
                    .load(imageUrl)
                    .error(R.drawable.banner)
                    .placeholder(R.drawable.default_noimage)
                    .error(R.drawable.default_noimage)
                    .into(userImageView);
        }


        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation.isValidEmail(etEmail.getText().toString()) || Validation.isEmptyStr(etEmail.getText().toString())) {

                    if (!Validation.isEmptyStr(etMobileNo.getText().toString()) || Validation.isValidNumber(etMobileNo.getText().toString(), Validation.getNumberPrefix(etMobileNo.getText().toString()))) {
                        edit_profile_webservice();
                    } else {
                        AndroidUtils.showSnackBar(coordinatorlayout_myprofile, "Please Enter Valid Mobile Number");
                        etMobileNo.setError("Please Enter Valid Mobile Number");
                    }
                } else {
                    AndroidUtils.showSnackBar(coordinatorlayout_myprofile, "Please Enter Valid Email Address");
                    etEmail.setError("Please Enter Valid Email Address");
                }
            }
        });
    }

    private void getshared_pref_data() {
        user_image = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "");
        fname = app_sharedpreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "");
        lname = app_sharedpreference.getSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), "");
        email = app_sharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "");
        mobile = app_sharedpreference.getSharedPref(SharedPreferenceConstants.MOBILE.toString(), "");
        address = app_sharedpreference.getSharedPref(SharedPreferenceConstants.ADDRESS.toString(), "");
        usertype = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "");

        Log.e("userType", usertype);

    }

    private void edit_profile_webservice() {
        p_handler.show();

        Ion.with(MyProfileActivity.this)
                .load((getResources().getString(R.string.webservice_base_url)) + "/my_account")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("name", etFName.getText().toString())
                .setBodyParameter("lastname", etLName.getText().toString())
                .setBodyParameter("mobile", etMobileNo.getText().toString())
                .setBodyParameter("email", etEmail.getText().toString())
                .setBodyParameter("address", etAddress.getText().toString())
                .setBodyParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .setBodyParameter("user_type", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result == null) {
                            p_handler.hide();
                            System.out.println("result-----------NULLLLLLL");


                        } else {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {

                                JsonObject jsonObject = result.getAsJsonObject();

                                String message = jsonObject.get("message").getAsString();

                                if (message.equals("No any changes to update!")) {

                                    showMessage(message);
                                    p_handler.hide();

                                } else {

                                    JsonArray jsonResultArray = jsonObject.getAsJsonArray("result");

                                    for (int i = 0; i < jsonResultArray.size(); i++) {

                                        JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                        String update_name = jsonObject1.get("name").getAsString();
                                        String update_lastname = jsonObject1.get("lastname").getAsString();
                                        String update_mobile = jsonObject1.get("mobile").getAsString();
                                        String update_address = jsonObject1.get("address").getAsString();

                                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.USER_NAME.toString(), update_name);
                                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), update_name);

                                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), update_lastname);

                                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.MOBILE.toString(), update_mobile);
                                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.ADDRESS.toString(), update_address);

                                        System.out.println("Username Data-----------" + update_name);

                                        showMessage(message);
                                        p_handler.hide();

                                    }

                                }

                            } else {
                                JsonObject jsonObject = result.getAsJsonObject();
                                String message = jsonObject.get("message").getAsString();
                                showMessage(message);
                                p_handler.hide();

                            }

                        }
                    }
                });

    }

    private void showMessage(String message) {
        AndroidUtils.showSnackBar(coordinatorlayout_myprofile, message);
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.logoWord).setVisibility(View.GONE);
        TextView header_name = (TextView) findViewById(R.id.header_name);
        header_name.setVisibility(View.VISIBLE);
        header_name.setText(getResources().getString(R.string.my_profile_heading));
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_home_menu, menu);
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

    void picPhoto() {
        String str[] = new String[]{"Camera", "Gallery"};
        new AlertDialog.Builder(this).setItems(str,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in;
                        if (which == 1) {
                            in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        } else {
                            which = 2;
                            in = new Intent();
                            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                        startActivityForResult(Intent.createChooser(in, "Select profile picture"), which);
                    }
                }).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:

                File imagefile = new File(ImageUtils.getRealPathFromURI(context, data.getData()));
                call_myprofile_webservice(imagefile);

                break;


            case CAMERA_RQ:

                System.out.println("Saved Video =================");

                if (resultCode == RESULT_OK) {
                    final File file = new File(data.getData().getPath());


                    //Toast.makeText(this, String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();
                    //Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

                    File videothumbnail = ImageUtils.getFile(context, thumb);

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    //use one of overloaded setDataSource() functions to set your data source
                    retriever.setDataSource(context, Uri.fromFile(file));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);

                    System.out.println("timeInMillisec-------" + timeInMillisec);

                    retriever.release();

                    if (timeInMillisec >= 120000) {

                        AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");

                    } else if (timeInMillisec <= 30000) {


                        AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
                    } else {

                        edit_profilevideo_webservice(file, videothumbnail);

                    }

                } else if (data != null) {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    if (e != null) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case REQUEST_TAKE_GALLERY_VIDEO:

                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                    System.out.println("selectedImage----------------" + selectedImage);

                    String filemanagerstring = selectedImage.getPath();

                    // MEDIA GALLERY
                    selectedImagePath = getPath(selectedImage);

                    System.out.println("selectedImagePath----------------" + selectedImagePath);

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.MINI_KIND);

                    // imageViewProfile.setImageBitmap(thumb);

                    File video_thumbnail = ImageUtils.getFile(context, thumb);
                    File file = null;
                    if (Validation.isNonEmptyStr(selectedImagePath)) {
                        file = new File(selectedImagePath);
                    }

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    //use one of overloaded setDataSource() functions to set your data source
                    if (file != null)
                        retriever.setDataSource(context, Uri.fromFile(file));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);

                    System.out.println("timeInMillisec-------" + timeInMillisec);

                    retriever.release();

                    // edit_profilevideo_webservice(file,video_thumbnail);

                    if (timeInMillisec >= 120000) {
                        AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");

                    } else if (timeInMillisec <= 30000) {

                        AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
                    } else {
                        edit_profilevideo_webservice(file, video_thumbnail);
                    }
                }
                break;

            case 13:
                if (data != null && data.getData() != null) {


                    captureVideo(requestCode, resultCode, data);
                } else {
                    AndroidUtils.showErrorLog(context, "Data.getData is null in video capture");
                }
                break;

        }


    }

    private void edit_profilevideo_webservice(File videofile, File video_thumnail) {

        p_handler.show();

        String url = getResources().getString(R.string.webservice_base_url) + "/seller_video_update";

        System.out.println("shared----------------------" + videofile);

        Ion.with(MyProfileActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartFile("video_thumbnail", "application/image", video_thumnail)
                .setMultipartFile("profile_video", "application/video", videofile)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        System.out.println("result------------------------------" + result);

                        if (result != null) {
                            if (result.get("error").getAsString().contains("false")) {
                                JsonObject jsonObject_result = result.getAsJsonObject("result");
                                String profile_video = jsonObject_result.get("profile_video").getAsString();

                                app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), profile_video);
                                app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_VIDEO_THUMBNAIL.toString(), jsonObject_result.get("video_thumbnail").getAsString());
                                String a = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString());

                                System.out.println("a------------------------------" + profile_video);

                                Picasso.with(context)
                                        .load(jsonObject_result.get("video_thumbnail").getAsString())
                                        .error(R.drawable.banner)
                                        .placeholder(R.drawable.default_noimage)
                                        .error(R.drawable.default_noimage)
                                        .into(imageViewProfile);
                                p_handler.hide();

                            }
                        } else {

                            AndroidUtils.showErrorLog(context, "hello2", e.toString());
                            p_handler.hide();
                        }

                    }
                });

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }


    private void call_myprofile_webservice(File imagefile) {

        p_handler.show();

        String url = getResources().getString(R.string.webservice_base_url) + "/profilepic_update";

        System.out.println("imagefile------------------------------" + imagefile + app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "") + "user_type--" + app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""));

        Ion.with(MyProfileActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartFile("profile_pic", "application/image", imagefile)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_type", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .setMultipartParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                System.out.println("result------------------------------" + result);

                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        JsonObject jsonObject_result = result.getAsJsonObject("result");
                        String profile_pic = jsonObject_result.get("profile_pic").getAsString();

                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
                        String a = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

                        System.out.println("a------------------------------" + a);

                        Picasso.with(context)
                                .load(a)
                                .error(R.drawable.banner)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into(userImageView);

                        p_handler.hide();
                    }

                } else {

                    AndroidUtils.showErrorLog(context, "hello2", e.toString());

                    p_handler.hide();

                }
            }
        });


    }

    private void showEditVideoPopup2() {
        boolean wrapInScrollView = true;

        video_dailog = new MaterialDialog.Builder(context)
                .customView(R.layout.dailog_pincode, wrapInScrollView)
                .show();

    }

    private void showEditVideoPopup() {
        boolean wrapInScrollView = true;

        video_dailog = new MaterialDialog.Builder(context)
                .title("Choose Option")
                .customView(R.layout.videopic_layout, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        video_dailog.dismiss();
                    }
                })
                .show();
        video_dailog.findViewById(R.id.linearLayoutGalary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                    } catch (Exception ex) {
                        //showMessage("Grant permission first");
                    }
                    video_dailog.dismiss();
                }


            }
        });

        video_dailog.findViewById(R.id.linearLayoutTakeVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*
                    // code buggy code
                    File saveDir = null;
                    saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                    saveDir.mkdirs();

                    MaterialCamera materialCamera = new MaterialCamera(MyProfileActivity.this)
                            .saveDir(saveDir)
                            .showPortraitWarning(true)
                            .allowRetry(true)
                            .countdownMinutes(1.25f)
                            .countdownImmediately(false)
                            .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                            .qualityProfile(MaterialCamera.QUALITY_HIGH)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                            .videoPreferredHeight(240)                         // Sets a preferred height for the recorded video output.
                            .videoPreferredAspect(4f / 3f)                     // Sets a preferred aspect ratio for the recorded video output.
                            //.maxAllowedFileSize(1024 * 1024 * 2)
                            .defaultToFrontFacing(true);

                    // .labelConfirm(R.string.mcam_use_video);

                    materialCamera.start(CAMERA_RQ);*/


                video_dailog.dismiss();
                Intent in = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                in.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
                in.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath() + "my_profile_video.mp4");
                startActivityForResult(Intent.createChooser(in, "Capture Video From Camera"), 13);


            }
        });


        video_dailog.findViewById(R.id.linearLayoutPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
                video_dailog.dismiss();
            }
        });


    }

    private void captureVideo(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri vid = data.getData();
            videoPath = ImageUtils.getRealPathFromURI(context, vid);

            AndroidUtils.showErrorLog(context, "file.getAbsolutePath()", videoPath);

            final File file = new File(videoPath);


            Uri myVideoUri = Uri.parse(file.toString());
        /*    MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();
            mmRetriever.setDataSource(file.getAbsolutePath());

// Array list to hold your frames
            final ArrayList<Bitmap> frames = new ArrayList<Bitmap>();

//Create a new Media Player
            //MediaPlayer mp = MediaPlayer.create(getBaseContext(), myVideoUri);

// Some kind of iteration to retrieve the frames and add it to Array list
            Bitmap bitmap = mmRetriever.getFrameAtTime();
            frames.add(bitmap);

AndroidUtils.showErrorLog(context,"frame_size",frames.size());
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                int i = 0;

                public void run() {
                    imageViewProfile.setImageBitmap(frames.get(i));
                    i++;
                    if (i > frames.size() - 1) {
                        i = 0;
                    }
                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(runnable, 2000);

*/







/*

            ArrayList<BitmapDrawable> bitmapDrawableslist = new ArrayList<BitmapDrawable>();
            final AnimationDrawable animation = new AnimationDrawable();

            for (int k = 0; k < frames.size(); k++) {


                Canvas canvas = new Canvas(frames.get(k));
                canvas.drawColor(Color.GRAY);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(80);
                paint.setColor(Color.BLACK);
                canvas.drawText("Frame " + k, 40, 220, paint);
                BitmapDrawable animation2 = new BitmapDrawable(getResources(), frames.get(k));
                bitmapDrawableslist.add(animation2);

                animation.addFrame(animation2,k);
    animation.addFrame(animation2, 50);
    animation.addFrame(animation2, 30);


            }
            animation.setOneShot(false);


// start the animation!

            imageViewProfile.setImageDrawable(animation);
            animation.start();
*/


            AndroidUtils.showErrorLog(context, "file.getAbsolutePath()", file.getAbsolutePath());

            //Toast.makeText(this, String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();
            //Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

            File videothumbnail = ImageUtils.getFile(context, thumb);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, Uri.fromFile(file));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);

            AndroidUtils.showErrorLog(context, "timeInMillisec-------" + timeInMillisec);

            retriever.release();


            if (timeInMillisec >= 120000) {

                AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");

            } else if (timeInMillisec <= 30000) {


                AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
            } else {

                edit_profilevideo_webservice(file, videothumbnail);
            }

        } else if (data != null) {
            Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
            if (e != null) {
                e.printStackTrace();
                AndroidUtils.showErrorLog(context, e.getMessage());
            }
        }


    }

    private BitmapDrawable getDrawableForFrameNumber(int frameNumber) {
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.GRAY);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(80);
        paint.setColor(Color.BLACK);
        canvas.drawText("Frame " + frameNumber, 40, 220, paint);
        return new BitmapDrawable(getResources(), bitmap);
    }

}
