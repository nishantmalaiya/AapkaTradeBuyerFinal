package com.aapkatrade.buyer.user_dashboard.my_profile;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity
{
    private AppSharedPreference app_sharedpreference;
    private EditText etFName,etLName, etEmail, etMobileNo, etAddress;
    private ProgressBarHandler p_handler;
    private TextView tvMyProfileDetailHeading;
    private CoordinatorLayout coordinatorlayout_myprofile;
    private Context context;
    private String fname, lname, email, mobile, address, user_image,usertype;
    private CircleImageView userImageView;
    private Button btnSave;
    private Bitmap imageForPreview;
    MaterialDialog video_dailog;
    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 111;
    public static String videopath;
    public static  String selectedImagePath;
    private final static int SELECT_VIDEO_REQUEST=100;
    String source_video_folder = null;
    ImageView imageViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = MyProfileActivity.this;
        setUpToolBar();

        initView();

    }

    private void initView()
    {
        app_sharedpreference = new AppSharedPreference(context);

        p_handler = new ProgressBarHandler(context);

        getshared_pref_data();

        Log.e("user_image_profile", user_image);

        coordinatorlayout_myprofile = (CoordinatorLayout) findViewById(R.id.coordinate_myprofile);

        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);

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


        etEmail.setKeyListener(null);

        etMobileNo.setKeyListener(null);

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditVideoPopup();

            }
        });

        String imageUrl = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

        if (Validation.isNonEmptyStr(imageUrl))
        {
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

    private void getshared_pref_data()
    {
        user_image = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "");
        fname = app_sharedpreference.getSharedPref(SharedPreferenceConstants.FIRST_NAME.toString(), "");
        lname = app_sharedpreference.getSharedPref(SharedPreferenceConstants.LAST_NAME.toString(), "");
        email = app_sharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString(), "");
        mobile = app_sharedpreference.getSharedPref(SharedPreferenceConstants.MOBILE.toString(), "");
        address = app_sharedpreference.getSharedPref(SharedPreferenceConstants.ADDRESS.toString(), "");
        usertype = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "");

    }

    private void edit_profile_webservice()
    {
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
                .setBodyParameter("user_id",  app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .setBodyParameter("user_type",app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
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

    private void showMessage(String message)
    {
        AndroidUtils.showSnackBar(coordinatorlayout_myprofile, message);
    }

    private void setUpToolBar()
    {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    public void save_shared_pref(String user_id, String user_name, String email_id, String lname, String dob, String address, String mobile, String order_quantity, String product_quantity, String company_quantity, String vendor_quantity, String network_quantity)
    {
        app_sharedpreference.setSharedPref("userid", user_id);
        app_sharedpreference.setSharedPref("username", user_name);
        app_sharedpreference.setSharedPref("emailid", email_id);
        app_sharedpreference.setSharedPref("lname", lname);
        app_sharedpreference.setSharedPref("address", address);
        app_sharedpreference.setSharedPref("mobile", mobile);
    }


    void picPhoto()
    {
        String str[] = new String[]{"Camera", "Gallery"};
        new AlertDialog.Builder(this).setItems(str,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        performImgPicAction(which);

                        Intent in;
                        if (which == 1) {
                            in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        }
                        else
                        {
                            which =2;
                            in = new Intent();
                            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                        startActivityForResult(Intent.createChooser(in, "Select profile picture"), which);

                    }
                }).show();
    }

    void performImgPicAction(int which)
    {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        switch (requestCode)
        {
            case 1:

                try {

                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inDither = false;
                    option.inPurgeable = true;
                    option.inInputShareable = true;
                    option.inTempStorage = new byte[32 * 1024];
                    option.inPreferredConfig = Bitmap.Config.RGB_565;
                    if (Build.VERSION.SDK_INT < 19) {

                        imageForPreview = BitmapFactory.decodeFile(getFilesDir().getPath(), option);

                    } else {
                        if (data.getData() != null) {

                            ParcelFileDescriptor pfd;
                            try {
                                pfd = getContentResolver()
                                        .openFileDescriptor(data.getData(), "r");
                                if (pfd != null) {
                                    FileDescriptor fileDescriptor = pfd
                                            .getFileDescriptor();

                                    imageForPreview = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, option);

                                }
                                pfd.close();


                            } catch (FileNotFoundException e) {
                                Log.e("FileNotFoundException", e.toString());
                            } catch (IOException e) {
                                Log.e("IOException", e.toString());
                            }
                        } else {

                            imageForPreview = (Bitmap) data.getExtras().get("data");

                            Log.e("data_not_found", "data_not_found");
                        }

                    }

                    userImageView.setVisibility(View.VISIBLE);

                    userImageView.setImageBitmap(imageForPreview);
                    File imagefile = getFile(imageForPreview);

                    call_myprofile_webservice(imagefile);


                } catch (Exception e) {
                    AndroidUtils.showErrorLog(context, "error in myprofile", e.toString());
                }

                break;


            case 2:

                try {

                    BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inDither = false;
                    option.inPurgeable = true;
                    option.inInputShareable = true;
                    option.inTempStorage = new byte[32 * 1024];
                    option.inPreferredConfig = Bitmap.Config.RGB_565;
                    if (Build.VERSION.SDK_INT < 19) {

                        imageForPreview = BitmapFactory.decodeFile(getFilesDir().getPath(), option);

                    } else {
                        if (data.getData() != null) {

                            ParcelFileDescriptor pfd;
                            try {
                                pfd = getContentResolver()
                                        .openFileDescriptor(data.getData(), "r");
                                if (pfd != null) {
                                    FileDescriptor fileDescriptor = pfd
                                            .getFileDescriptor();

                                    imageForPreview = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, option);

                                }
                                pfd.close();


                            } catch (FileNotFoundException e) {
                                Log.e("FileNotFoundException", e.toString());
                            } catch (IOException e) {
                                Log.e("IOException", e.toString());
                            }
                        } else {

                            imageForPreview = (Bitmap) data.getExtras().get("data");

                            Log.e("data_not_found", "data_not_found");
                        }

                    }

                    userImageView.setVisibility(View.VISIBLE);

                    userImageView.setImageBitmap(imageForPreview);
                    File imagefile = getFile(imageForPreview);

                    call_myprofile_webservice(imagefile);


                } catch (Exception e) {
                    AndroidUtils.showErrorLog(context, "error in myprofile", e.toString());
                }

                break;



            case CAMERA_RQ:

                System.out.println("Saved Video =================");

                if (resultCode == RESULT_OK)
                {
                    final File file = new File(data.getData().getPath());

                    //Toast.makeText(this, String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();
                    //Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

                    File videofile = getFile(thumb);

                    edit_profilevideo_webservice(videofile);

                }
                else if (data != null)
                {
                    Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                    if (e != null)
                    {
                        e.printStackTrace();
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case SELECT_VIDEO_REQUEST:

                if(requestCode == SELECT_VIDEO_REQUEST && resultCode == RESULT_OK)
                {
                    if(data.getData()!=null)
                    {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Video.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                        int   columnindex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                        cursor.moveToFirst();
                        String file_path = cursor.getString(columnindex);

                        videopath= cursor.getString(columnindex);
                        File oldfile=new File(videopath);

                       /* if (GeneralUtils.checkIfFileExistAndNotEmpty(file_path)) {
                            new Video_compressor(this).execute();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), file_path + " not found", Toast.LENGTH_LONG).show();
                        }*/


                        File newFile=new File(source_video_folder);
                        try {
                            copyFile(oldfile,newFile);
                        } catch (IOException e) {
                            Log.e("IOException",e.toString());
                        }
                        // videofile=savevideo(videopath);
                        Log.e(getClass().getName(), "file_path"+file_path);
                        Uri   fileUri = Uri.parse("file://" + file_path);

                        Toast.makeText(getApplicationContext(), fileUri.toString() , Toast.LENGTH_LONG).show();

                        cursor.close();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to select video" , Toast.LENGTH_LONG).show();
                    }
                }
                break;


        case REQUEST_TAKE_GALLERY_VIDEO:

        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO)
            {
                Uri selectedImage = data.getData();

                System.out.println("selectedImage----------------" + selectedImage);

                String filemanagerstring = selectedImage.getPath();

                // MEDIA GALLERY
                selectedImagePath = getPath(selectedImage);

                System.out.println("selectedImagePath----------------" + selectedImagePath);

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.MINI_KIND);

               // imageViewProfile.setImageBitmap(thumb);

                File videofile = getFile(thumb);

                edit_profilevideo_webservice(videofile);


             /*   if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {

                    System.out.println("android----------------"+selectedImage);

                    String wholeID = DocumentsContract.getDocumentId(selectedImage);

                    String id = wholeID.split(":")[0];

                    String[] column = { MediaStore.Video.Media.DATA };
                    String sel = MediaStore.Video.Media._ID + "=?";

                    Cursor cursor = context.getContentResolver().
                            query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{ id }, null);

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    System.out.println("columnIndex---------------"+columnIndex);

                    cursor.moveToFirst();

                        String  filePath = cursor.getString(columnIndex);
                        videopath= cursor.getString(columnIndex);

                        System.out.println("videopath---------------"+filePath);

                        //videofile=savevideo(videopath);
                       *//* Log.e("filePath", filePath);
                        if (GeneralUtils.checkIfFileExistAndNotEmpty(filePath)) {
                            new Video_compressor(this).execute();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), filePath + " not found", Toast.LENGTH_LONG).show();
                        }*//*

                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);

                        chooseVideo.setImageBitmap(thumb);



                    cursor.close();
                }
                else
                {
                    System.out.println("selectedImage----------------" + selectedImage);

                    String filemanagerstring = selectedImage.getPath();

                    // MEDIA GALLERY
                    selectedImagePath = getPath(selectedImage);

                    System.out.println("selectedImagePath----------------" + selectedImagePath);

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Video.Thumbnails.MINI_KIND);

                    chooseVideo.setImageBitmap(thumb);
                }*/
            }

        }
        break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void edit_profilevideo_webservice(File videofile)
    {

        p_handler.show();

        String url = getResources().getString(R.string.webservice_base_url) + "/seller_video_update";

        System.out.println("shared----------------------"+app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""));


        Ion.with(MyProfileActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartFile("profile_video", "application/image", videofile)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result)
            {

                System.out.println("result------------------------------"+result);

                if (result != null)
                {
                    if (result.get("error").getAsString().contains("false"))
                    {
                        JsonObject jsonObject_result = result.getAsJsonObject("result");
                        String profile_video = jsonObject_result.get("profile_video").getAsString();

                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString(), profile_video);
                        String a = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_VIDEO.toString());

                        System.out.println("a------------------------------"+a);

                        Picasso.with(context)
                                .load(a)
                                .error(R.drawable.banner)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into(imageViewProfile);
                        p_handler.hide();

                    }

                }
                else
                {

                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                    p_handler.hide();
                }
            }
        });

    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor =  getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null)
        {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        }
        else
            return null;
    }


    private void call_myprofile_webservice(File imagefile)
    {

        String url = getResources().getString(R.string.webservice_base_url) + "/profilepic_update";

        System.out.println("imagefile------------------------------"+imagefile+app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "")+"user_type--"+ app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""));

        Ion.with(MyProfileActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartFile("profile_pic", "application/image", imagefile)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_type", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .setMultipartParameter("user_id", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result)
            {

                System.out.println("result------------------------------"+result);

                if (result != null)
                {
                    if (result.get("error").getAsString().contains("false"))
                    {
                        JsonObject jsonObject_result = result.getAsJsonObject("result");
                        String profile_pic = jsonObject_result.get("profile_pic").getAsString();

                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
                        String a = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

                        System.out.println("a------------------------------"+a);

                        Picasso.with(context)
                                .load(a)
                                .error(R.drawable.banner)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into(userImageView);

                    }

                } else {

                    AndroidUtils.showErrorLog(context, "hello2", e.toString());

                }
            }
        });


    }

    private File getFile(Bitmap photo)
    {
        Uri tempUri = null;
        if (photo != null) {
            tempUri = getImageUri(MyProfileActivity.this, photo);
        }
        File finalFile = new File(getRealPathFromURI(tempUri));
        Log.e("data", getRealPathFromURI(tempUri));

        return finalFile;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = null;
        int idx = 0;
        if (uri != null) {
            cursor = MyProfileActivity.this.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        }
        return cursor.getString(idx);
    }


    public static String copyFile(File src, File dst) throws IOException
    {
        FileInputStream var2 = new FileInputStream(src);
        FileOutputStream var3 = new FileOutputStream(dst);
        byte[] var4 = new byte[1024];

        int var5;
        while((var5 = var2.read(var4)) > 0) {
            var3.write(var4, 0, var5);
        }
        var2.close();
        var3.close();
        return dst.getAbsolutePath();
    }



    private  void showEditVideoPopup()
    {
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
            public void onClick(View v)
            {

                if (ContextCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
                }
                else
                {
                    try
                    {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
                    }catch (Exception ex){
                        //showMessage("Grant permission first");
                    }
                    video_dailog.dismiss();
                }


            }
        });

        video_dailog.findViewById(R.id.linearLayoutTakeVideo).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    // code buggy code
                    File saveDir = null;
                    saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                    saveDir.mkdirs();

                    MaterialCamera materialCamera = new MaterialCamera(MyProfileActivity.this)
                            .saveDir(saveDir)
                            .showPortraitWarning(true)
                            .allowRetry(true)
                            .countdownMinutes(0.25f)
                            .countdownImmediately(false)
                            .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                            .qualityProfile(MaterialCamera.QUALITY_LOW)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                            .videoPreferredHeight(240)                         // Sets a preferred height for the recorded video output.
                            .videoPreferredAspect(4f / 3f)                     // Sets a preferred aspect ratio for the recorded video output.
                            .maxAllowedFileSize(1024 * 1024 * 2)
                            .defaultToFrontFacing(true);

                    // .labelConfirm(R.string.mcam_use_video);

                    materialCamera.start(CAMERA_RQ);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                video_dailog.dismiss();
            }
        });


        video_dailog.findViewById(R.id.linearLayoutPicture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                picPhoto();
                video_dailog.dismiss();
            }
        });




    }


}
