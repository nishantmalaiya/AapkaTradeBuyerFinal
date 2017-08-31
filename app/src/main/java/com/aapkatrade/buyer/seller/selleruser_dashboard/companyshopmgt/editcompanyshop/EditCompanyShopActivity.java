package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.general.Utils.ParseUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSimpleListAdapter;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.home.navigation.entity.SubCategory;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopManagementActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductImagesAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductMediaData;
import com.aapkatrade.buyer.uicomponent.customcardview.CustomCardViewHeader;
import com.aapkatrade.buyer.uicomponent.customspinner.CountryStateSelectSpinner;
import com.aapkatrade.buyer.uicomponent.customspinner.Idtype;
import com.aapkatrade.buyer.uicomponent.daystile.DaysTileView;
import com.afollestad.materialcamera.MaterialCamera;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


public class EditCompanyShopActivity extends AppCompatActivity {

    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private String userId, categoryID, subCategoryID, serviceType, videoPath, tempCityID, tempsubcategoryId, countryName, stateName, cityName;
    public static String stateID, countryID, cityID;
    private Spinner spCategory, spSubCategory, spServiceType;
    CountryStateSelectSpinner spState, spCity, spCountry;


    private ArrayList<String> stateList, stateIds;
    private ArrayList<City> cityList = new ArrayList<>();
    private ArrayList<Category> listDataHeader = new ArrayList<>();
    private ArrayList<SubCategory> listDataChild = new ArrayList<>();
    private EditText etCompanyShopName, etAreaLocation, etCompanyAddress, etPinCode, etMobileNo, etPhoneNo, etEmail, etWebURL, etFBUrl, etTwitterUrl, etYoutubeURL, etGooglePlusURL, etDescription;
    private File docFile = new File("");
    private ArrayList<ProductMediaData> productMediaDatas = new ArrayList<>();
    public static ArrayList<ProductMediaData> productMediaDatasDelete = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductImagesAdapter adapter;
    private ArrayList<Bitmap> multipleImages;
    private RelativeLayout addCompanyShopLayout;
    private Button btnSave;
    private boolean isAllFieldsValidate = true, isGeneralDetailsCompleted = true;
    private DaysTileView daysTileView1, daysTileView2, daysTileView3;
    private CustomCardViewHeader generalDetailsHeader, shopDetailsHeader;
    private LinearLayout llShopDetailsContainer, llGeneralContainer;
    private String company_name, product_type, country_id, state_id, city_id, area, pincode, mobile, phone, email_id, web_url, category_id, sub_cat_id, facebookurl, twitterurl, googleplusurl, youtubeurl, short_description, address, videoURL, shopId;
    private boolean cityFlag = false, subCategoryFlag = false;
    private ArrayList<KeyValue> imageUrlList = new ArrayList<>();

    private ArrayList<String> submitImgDelList = new ArrayList<>();
    private ArrayList<Part> submitImgList = new ArrayList<>();
    public static CommonInterface commonInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_shop);
        context = EditCompanyShopActivity.this;
        if (getIntent() != null) {
            shopId = getIntent().getStringExtra("shopId");
        }
        init_country_state_data();

        productMediaDatasDelete.clear();
        setUpToolBar();
        initView();
        getState();
        getCategory();
        getServiceType();
        takeAreaLocation();
        setupRecyclerView();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
                if (submitImgDelList != null)
                    submitImgDelList.clear();
                submitImgList = submitImages();

                submitImgDelList = submitDeletedImages();

                AndroidUtils.showErrorLog(context, "submitImgDelList-------" + submitImgDelList);

                if (isAllFieldsValidate) {
                    AndroidUtils.showErrorLog(context, "working 1", "working isAllFieldsValidate");
                    hitUpdateCompanyShopWebService();
                }
            }
        });


        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
                Idtype idtype = (Idtype) object;
                String type = idtype.type;
                if (type.equals("country")) {


                    countryID = idtype.id;
                    if (spState != null) {
                        AndroidUtils.showErrorLog(context, "spState not null");
                        spState.setText("Select State");
                        spCity.setText("Select City");
                        stateID = "";

                        // spState.hitStateWebService(true);

                    }


                } else if (type.toLowerCase().equals("state")) {
                    stateID = idtype.id;

                    spCity.setText("Select City");
                    //spCity.hitCityWebService(true);
                } else if (type.equals("city")) {
                    cityID = idtype.id;


                }


                AndroidUtils.showErrorLog(context, "integer value", type + "*******" + idtype.id);

                return null;
            }
        };


    }

    private void init_country_state_data() {
        stateID = "";
        countryID = "";
        cityID = "";

    }

    private void getCompanyShopDetails() {
        AndroidUtils.showErrorLog(context, "Shop Id is ------------------>>>>>", shopId);
        Ion.with(getApplicationContext())
                .load(getResources().getString(R.string.webservice_base_url) + "/shop_detail/" + shopId)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "getCompanyShopDetails_response", result);
                        JsonObject jsonObject = result.getAsJsonObject("result");
                        company_name = jsonObject.get("company_name").getAsString();
                        etCompanyShopName.setText(company_name);
                        product_type = jsonObject.get("product_type").getAsString();
                        if (product_type.contains("0")) {
                            spServiceType.setSelection(1);

                        } else {
                            spServiceType.setSelection(2);
                        }


                        state_id = jsonObject.get("state_id").getAsString();
                        country_id = jsonObject.get("country_id").getAsString();

                        city_id = jsonObject.get("city_id").getAsString();
                        selectStateCity(country_id, state_id, city_id);

                        area = jsonObject.get("area").getAsString();
                        etAreaLocation.setText(area);
                        pincode = jsonObject.get("pincode").getAsString();

                        etPinCode.setText(pincode);
                        mobile = jsonObject.get("mobile").getAsString();
                        countryName = jsonObject.get("country_name").getAsString();
                        stateName = jsonObject.get("state_name").getAsString();
                        cityName = jsonObject.get("city_name").getAsString();
                        spCountry.setText(countryName);
                        spState.setText(stateName);
                        spCity.setText(cityName);
                        etMobileNo.setText(mobile);
                        phone = jsonObject.get("phone").getAsString();
                        etPhoneNo.setText(phone);
                        email_id = jsonObject.get("email_id").getAsString();
                        etEmail.setText(email_id);
                        category_id = jsonObject.get("category_id").getAsString();
                        sub_cat_id = jsonObject.get("sub_cat_id").getAsString();
                        setSelectedCategory(category_id, sub_cat_id);

                        web_url = jsonObject.get("web_url").getAsString();
                        etWebURL.setText(web_url);

                        facebookurl = jsonObject.get("facebookurl").getAsString();
                        etFBUrl.setText(facebookurl);
                        twitterurl = jsonObject.get("twitterurl").getAsString();
                        etTwitterUrl.setText(twitterurl);
                        googleplusurl = jsonObject.get("googleplusurl").getAsString();
                        etGooglePlusURL.setText(googleplusurl);
                        youtubeurl = jsonObject.get("youtubeurl").getAsString();
                        etYoutubeURL.setText(youtubeurl);
                        short_description = jsonObject.get("short_des").getAsString();
                        etDescription.setText(short_description);
                        address = jsonObject.get("address").getAsString();
                        etCompanyAddress.setText(address);


                        JsonArray openingtime = result.getAsJsonArray("opening_time");

                        for (int k = 4; k < openingtime.size(); k++) {

                            JsonObject jsonObjectOpenClose = (JsonObject) openingtime.get(k);
                            daysTileView2.setOpenTimeSpinnerByValue(jsonObjectOpenClose.get("open_time").getAsString());
                            daysTileView2.setCloseTimeSpinnerByValue(jsonObjectOpenClose.get("close_time").getAsString());

                            switch (k) {
                                case 4:
                                    daysTileView1.setOpenTimeSpinnerByValue(jsonObjectOpenClose.get("open_time").getAsString());
                                    daysTileView1.setCloseTimeSpinnerByValue(jsonObjectOpenClose.get("close_time").getAsString());
                                    break;
                                case 5:
                                    daysTileView2.setOpenTimeSpinnerByValue(jsonObjectOpenClose.get("open_time").getAsString());
                                    daysTileView2.setCloseTimeSpinnerByValue(jsonObjectOpenClose.get("close_time").getAsString());
                                    break;
                                case 6:
                                    daysTileView3.setOpenTimeSpinnerByValue(jsonObjectOpenClose.get("open_time").getAsString());
                                    daysTileView3.setCloseTimeSpinnerByValue(jsonObjectOpenClose.get("close_time").getAsString());
                                    break;


                            }


                        }

                        videoURL = jsonObject.get("shop_video").getAsString();
                        if (Validation.isNonEmptyStr(videoURL) && !videoURL.equals("No Video")) {
                            AndroidUtils.showErrorLog(context, "videoURL---------------------" + videoURL);
                            productMediaDatas.add(new ProductMediaData(videoURL.replace("mp4", "png")));
                            adapter.notifyDataSetChanged();
                        }

                        JsonArray jsonArray = jsonObject.getAsJsonArray("product_images");

                        for (int i = 0; i < jsonArray.size(); i++) {
                            AndroidUtils.showErrorLog(context, "imagepathurl---------------------" + jsonArray.get(i).getAsJsonObject().get("image_url").getAsString());
                            imageUrlList.add(new KeyValue(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), jsonArray.get(i).getAsJsonObject().get("image_url").getAsString()));
                        }
                        if (imageUrlList.size() > 0) {
                            for (int i = 0; i < imageUrlList.size(); i++) {
                                AndroidUtils.showErrorLog(context, "downloadImage---------------------" + imageUrlList.get(i).key);
                                downloadImage(i);
                            }
                        }
                    }
                });
    }

    private void downloadImage(final int index) {
        progressBarHandler.show();

        Ion.with(this).load(imageUrlList.get(index).value.toString()).withBitmap().asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        progressBarHandler.hide();
                        // do something with your bitmap
                        if (result == null) {
                            AndroidUtils.showErrorLog(context, "Problems in downloading image result == null.");
                        } else {
                            storeImage(result, index);
                        }
                    }
                });


    }

    private void storeImage(Bitmap image, int index) {
        File pictureFile = getOutputMediaFile(index);
        if (pictureFile == null) {
            AndroidUtils.showErrorLog(context, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            AndroidUtils.showErrorLog(context, "File not found: " + e.getMessage());
        } catch (IOException e) {
            AndroidUtils.showErrorLog(context, "Error accessing file: " + e.getMessage());
        }
        AndroidUtils.showErrorLog(context, "Image file exists with path-------------- : ", pictureFile.getAbsolutePath());// e.getMessage());

        productMediaDatas.add(new ProductMediaData(pictureFile.getAbsolutePath(), "", null, ""));
        adapter.notifyDataSetChanged();
    }

    private File getOutputMediaFile(int index) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = getFileName(imageUrlList.get(index).value.toString()) + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private String getFileName(String s) {
        if (Validation.isNonEmptyStr(s)) {
            String s1[] = s.split("/");
            String s2 = s1[s1.length - 1];
            return s2;
        }
        return new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidUtils.showErrorLog(context, "________________^^^^^^^onDestroyonDestroyonDestroy^^^^^^^^^_________________");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (mediaStorageDir.isDirectory()) {
            String[] children = mediaStorageDir.list();
            for (String aChildren : children) {
                AndroidUtils.showErrorLog(context, "________________^^^^^^^onDestroyonDestroyonDestroy^^^^^^^^^Entry_________________");

                new File(mediaStorageDir, aChildren).delete();
            }
        }
    }

    private void setSelectedCategory(String category_id, String sub_cat_id) {

        AndroidUtils.showErrorLog(context, "categoryid123------------------", category_id + "***" + sub_cat_id);
        AndroidUtils.showErrorLog(context, "listDataHeader------------------", listDataHeader.size());
        for (int i = 0; i < listDataHeader.size(); i++) {
            if (listDataHeader.get(i).getCategoryId().equals(category_id)) {

                subCategoryFlag = true;
                tempsubcategoryId = sub_cat_id;
                spCategory.setSelection(i);
            }
        }


    }

    private void selectStateCity(String country_id, String state_id, String city_id) {



        /*for (int i = 0; i < stateIds.size(); i++) {
            if (stateIds.get(i).equals(state_id)) {

                cityFlag = true;
                tempCityID = city_id;
                spState.setSelection(i);

            }

        }*/


    }

    private void validateFields() {
        isAllFieldsValidate = true;
        if (productMediaDatas.size() > 0) {
            if (productMediaDatas.get(0).imagePath != null && productMediaDatas.get(0).imagePath.equalsIgnoreCase("first")) {
                productMediaDatas.remove(0);
            }
        }

        if (!isImageExists(productMediaDatas)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please Capture/Upload at least one Imaage.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(etCompanyShopName.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter company/shop name.");
            etCompanyShopName.setError("Please enter company/shop name.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (!Validation.isPincode(etPinCode.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter Valid Pincode.");
            etPinCode.setError("Please enter Valid Pincode.");
            isAllFieldsValidate = false;
        } else if (Validation.isEmptyStr(etAreaLocation.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter company/shop address.");
            etAreaLocation.setError("Please enter company/shop address.");
            isAllFieldsValidate = false;
        } else if (Validation.isEmptyStr(serviceType)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please select service type.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(categoryID)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please select category.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(subCategoryID)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please select sub category.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(etAreaLocation.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter company/shop Area.");
            etAreaLocation.setError("Please enter company/shop Area.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(stateID)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please select state.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(cityID)) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please select city.");
            isAllFieldsValidate = false;
            isGeneralDetailsCompleted = false;
        } else if (Validation.isEmptyStr(etCompanyAddress.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter company/shop Address.");
            etCompanyAddress.setError("Please enter company/shop Address.");
            isAllFieldsValidate = false;
        } else if (!Validation.isValidNumber(etMobileNo.getText().toString(), Validation.getNumberPrefix(etMobileNo.getText().toString()))) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter valid mobile number.");
            etMobileNo.setError("Please enter valid mobile number.");
            isAllFieldsValidate = false;
        } else if (Validation.isEmptyStr(etMobileNo.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter valid mobile number.");
            etPhoneNo.setError("Please enter valid mobile number.");
            isAllFieldsValidate = false;
        } else if (Validation.isEmptyStr(etDescription.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter description.");
            etDescription.setError("Please enter description.");
            isAllFieldsValidate = false;
        } else if (!Validation.isValidEmail(etEmail.getText().toString())) {
            AndroidUtils.showSnackBar(addCompanyShopLayout, "Please enter valid E-mail.");
            etEmail.setError("Please enter valid E-mail.");
            isAllFieldsValidate = false;
        }
    }

    private void takeAreaLocation() {
        etAreaLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etAreaLocation.getWindowToken(), 0);
                }
            }
        });
        etAreaLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressBarHandler.show();
                    startActivityForResult(new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build((Activity) context), 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    progressBarHandler.hide();
                    AndroidUtils.showErrorLog(context, e.toString());
                }
            }
        });
    }

    private void initView() {
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        spState = (CountryStateSelectSpinner) findViewById(R.id.spState);
        spCity = (CountryStateSelectSpinner) findViewById(R.id.spCity);
        spCountry = (CountryStateSelectSpinner) findViewById(R.id.spCountry);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        spSubCategory = (Spinner) findViewById(R.id.spSubCategory);
        spServiceType = (Spinner) findViewById(R.id.spServiceType);
        etAreaLocation = (EditText) findViewById(R.id.tv_area_location);
        findViewById(R.id.input_layout_sub_category).setVisibility(View.GONE);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setText("Update Company/Shop");
        etCompanyShopName = (EditText) findViewById(R.id.etCompanyShopName);
        etPinCode = (EditText) findViewById(R.id.et_pin_code);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etWebURL = (EditText) findViewById(R.id.etWebURL);
        etFBUrl = (EditText) findViewById(R.id.etFBUrl);
        etTwitterUrl = (EditText) findViewById(R.id.etTwitterUrl);
        etYoutubeURL = (EditText) findViewById(R.id.etYoutubeURL);
        etGooglePlusURL = (EditText) findViewById(R.id.etGooglePlusURL);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etCompanyAddress = (EditText) findViewById(R.id.etCompanyAddress);
        addCompanyShopLayout = (RelativeLayout) findViewById(R.id.addCompanyShopLayout);

        daysTileView1 = (DaysTileView) findViewById(R.id.daysTileView);
        daysTileView1.setBackgroundColor(R.color.green);
        daysTileView1.setDayName("Mon - Fri");

        daysTileView2 = (DaysTileView) findViewById(R.id.daysTileView2);
        daysTileView2.setBackgroundColor(R.color.md_material_blue_600);
        daysTileView2.setDayName("Saturday");

        daysTileView3 = (DaysTileView) findViewById(R.id.daysTileView3);
        daysTileView3.setBackgroundColor(R.color.red);
        daysTileView3.setDayName("Sunday");

        generalDetailsHeader = (CustomCardViewHeader) findViewById(R.id.generalDetailsHeader);
        generalDetailsHeader.setTitle("General Information");
        generalDetailsHeader.setImageDrawableLeft(ContextCompat.getDrawable(context, R.drawable.ic_info));

        shopDetailsHeader = (CustomCardViewHeader) findViewById(R.id.shopDetailsHeader);
        shopDetailsHeader.setTitle("Shop/Company Details Information");
        shopDetailsHeader.setImageDrawableLeft(ContextCompat.getDrawable(context, R.drawable.ic_info));

        llGeneralContainer = (LinearLayout) findViewById(R.id.llGeneralContainer);
        llShopDetailsContainer = (LinearLayout) findViewById(R.id.llShopDetailsContainer);

        llShopDetailsContainer.setVisibility(View.GONE);
        generalDetailsHeader.setImageRightRotation(180);

        generalDetailsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llGeneralContainer.getVisibility() == View.VISIBLE) {
                    llGeneralContainer.setVisibility(View.GONE);
                    generalDetailsHeader.setImageRightRotation(0);
                    llShopDetailsContainer.setVisibility(View.VISIBLE);
                    shopDetailsHeader.setImageRightRotation(180);
                } else {
                    llGeneralContainer.setVisibility(View.VISIBLE);
                    generalDetailsHeader.setImageRightRotation(180);
                    llShopDetailsContainer.setVisibility(View.GONE);
                    shopDetailsHeader.setImageRightRotation(0);
                }
            }
        });

        shopDetailsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llShopDetailsContainer.getVisibility() == View.VISIBLE) {
                    llShopDetailsContainer.setVisibility(View.GONE);
                    shopDetailsHeader.setImageRightRotation(0);
                    llGeneralContainer.setVisibility(View.VISIBLE);
                    generalDetailsHeader.setImageRightRotation(180);
                } else {
                    llShopDetailsContainer.setVisibility(View.VISIBLE);
                    shopDetailsHeader.setImageRightRotation(180);
                    llGeneralContainer.setVisibility(View.GONE);
                    generalDetailsHeader.setImageRightRotation(0);
                }
            }
        });


        etCompanyAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    llGeneralContainer.setVisibility(View.GONE);
                    generalDetailsHeader.setImageRightRotation(0);
                }

            }
        });


        etPinCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    llShopDetailsContainer.setVisibility(View.VISIBLE);
                    shopDetailsHeader.setImageRightRotation(180);
                    etCompanyAddress.requestFocus();
                    return true;
                }
                return false;
            }
        });

    }

    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        back_imagview.setVisibility(View.VISIBLE);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }

    private void getState() {

       /* stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        stateIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_ids)));
        AndroidUtils.showErrorLog(context, stateList.toString());
        spState.setAdapter(new CustomSpinnerAdapter(context, stateList));
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0) {
                    findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
                    stateID = String.valueOf(position);
                    getCity(stateIds.get(position), cityFlag);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });*/

    }

    private void getCategory() {
        listDataChild.clear();
        listDataHeader.clear();
        progressBarHandler.show();
        AndroidUtils.showErrorLog(context, "data", "getCategory Entered");
        Ion.with(context)
                .load("http://aapkatrade.com/slim/dropdown")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "category")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject data) {
                        progressBarHandler.hide();
//                        AndroidUtils.showErrorLog(context, "data", "getCategory result" + data.toString());
                        if (data != null) {
                            JsonObject jsonObject = data.getAsJsonObject();
                            JsonArray jsonResultArray = jsonObject.getAsJsonArray("result");

                            listDataHeader = new ArrayList<>();
                            listDataChild.add(new SubCategory("-1", "Please Select SubCategory"));

                            listDataHeader.add(new Category("-1", "Please Select Category", "", listDataChild));
                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                JsonArray json_subcategory = jsonObject1.getAsJsonArray("subcategory");

                                if (json_subcategory != null) {
                                    listDataChild = new ArrayList<>();
                                    for (int k = 0; k < json_subcategory.size(); k++) {
                                        JsonObject jsonObject_subcategory = (JsonObject) json_subcategory.get(k);
                                        SubCategory subCategory = new SubCategory(jsonObject_subcategory.get("id").getAsString(), jsonObject_subcategory.get("name").getAsString());
                                        listDataChild.add(subCategory);
                                    }
                                    Category Category = new Category(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString(), jsonObject1.get("icon").getAsString(), listDataChild);
                                    listDataHeader.add(Category);
                                    AndroidUtils.showErrorLog(context, "data", Category.toString());

                                }
                            }
                            setCategoryAdapter();
                        }
                        getCompanyShopDetails();
                    }

                });


    }

    private void getServiceType() {
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Please Select Service Type");
        arrayList.add("Multiple/Group Product");
        arrayList.add("Only Services");
        spServiceType.setAdapter(new CustomSimpleListAdapter(context, arrayList));
        spServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    serviceType = arrayList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCategoryAdapter() {
        AndroidUtils.showErrorLog(context, "data", this.listDataHeader.toString());
        CustomSimpleListAdapter categoriesAdapter = new CustomSimpleListAdapter(context, this.listDataHeader);
        spCategory.setAdapter(categoriesAdapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryID = listDataHeader.get(position).getCategoryId();
                    listDataChild = new ArrayList<>();
                    listDataChild = listDataHeader.get(position).getSubCategoryList();
                    if (listDataChild.size() > 0) {


                        findViewById(R.id.input_layout_sub_category).setVisibility(View.VISIBLE);
                        CustomSimpleListAdapter adapter = new CustomSimpleListAdapter(context, listDataChild);
                        spSubCategory.setAdapter(adapter);
                        if (subCategoryFlag) {
                            for (int i = 0; i < listDataChild.size(); i++) {
                                if (listDataChild.get(i).subCategoryId.equals(tempsubcategoryId)) {
                                    spSubCategory.setSelection(i);
                                }
                            }
                        }
                        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                if (position >= 0) {
                                    subCategoryID = listDataChild.get(position).subCategoryId;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        findViewById(R.id.input_layout_sub_category).setVisibility(View.GONE);
                        subCategoryID = "";
                        listDataChild = new ArrayList<>();
                        listDataChild.add(new SubCategory("0", "No SubCategory Found"));
                        CustomSimpleListAdapter adapter = new CustomSimpleListAdapter(context, listDataChild);
                        spSubCategory.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (subCategoryFlag) {
            subCategoryFlag = false;


        }
    }


    private void hitUpdateCompanyShopWebService() {

        for (int i = 0; i < productMediaDatas.size(); i++) {
            AndroidUtils.showErrorLog(context, "media data : " + i, "productMediaDatas.get(i).imagePath : " + productMediaDatas.get(i).imagePath);
            AndroidUtils.showErrorLog(context, "media data : " + i, "productMediaDatas.get(i).imageUrl : " + productMediaDatas.get(i).imageUrl);
            AndroidUtils.showErrorLog(context, "media data : " + i, "productMediaDatas.get(i).videoThumbnail : " + productMediaDatas.get(i).videoThumbnail);
            AndroidUtils.showErrorLog(context, "media data : " + i, "productMediaDatas.get(i).productMediaDatas.get(i).videoFile == null : " + productMediaDatas.get(i).videoFile == null);
            AndroidUtils.showErrorLog(context, "media data : " + i, "productMediaDatas.get(i).isVideo : " + productMediaDatas.get(i).isVideo);
        }
        AndroidUtils.showErrorLog(context, "user_id", userId);
        AndroidUtils.showErrorLog(context, "company_name", etCompanyShopName.getText().toString());
        AndroidUtils.showErrorLog(context, "address", etCompanyAddress.getText().toString());
        AndroidUtils.showErrorLog(context, "category_id", categoryID);
        AndroidUtils.showErrorLog(context, "area", etAreaLocation.getText().toString());
        AndroidUtils.showErrorLog(context, "state_id", stateID);
        AndroidUtils.showErrorLog(context, "submitImages----------", submitImages() == null ? null : submitImages().size());
        AndroidUtils.showErrorLog(context, "product_type", serviceType);
        AndroidUtils.showErrorLog(context, "sub_cat_id", subCategoryID);
        AndroidUtils.showErrorLog(context, "city_id", cityID);
        AndroidUtils.showErrorLog(context, "open_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getOpeningTimeID(), daysTileView2.getOpeningTimeID(), daysTileView3.getOpeningTimeID()})));
        AndroidUtils.showErrorLog(context, "mobile", etMobileNo.getText().toString());
        AndroidUtils.showErrorLog(context, "pincode", etPinCode.getText().toString());
        AndroidUtils.showErrorLog(context, "email_id", etEmail.getText().toString());
        AndroidUtils.showErrorLog(context, "phone", etPhoneNo.getText().toString());
        AndroidUtils.showErrorLog(context, "facebookurl", etFBUrl.getText().toString());
        AndroidUtils.showErrorLog(context, "short_des", etDescription.getText().toString());
        AndroidUtils.showErrorLog(context, "web_url", etWebURL.getText().toString());
        AndroidUtils.showErrorLog(context, "googleplusurl", etGooglePlusURL.getText().toString());
        AndroidUtils.showErrorLog(context, "twitterurl", etTwitterUrl.getText().toString());
        AndroidUtils.showErrorLog(context, "youtubeurl", etYoutubeURL.getText().toString());
        AndroidUtils.showErrorLog(context, "lat", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0"));
        AndroidUtils.showErrorLog(context, appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0"));

        progressBarHandler.show();


        if (submitVideo() == null) {

            AndroidUtils.showErrorLog(context, "submitDeletedImages--------------", submitImgDelList);

            if (submitImgDelList == null || submitImgDelList.size() == 0) {
                AndroidUtils.showErrorLog(context, "submitDeletedImages::::if--------------::NULL", submitDeletedImages());

                Ion.with(context)
                        .load(getString(R.string.webservice_base_url) + "/editCompany")
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("shop_id", shopId)
                        .setMultipartParameter("user_id", userId)
                        .setMultipartParameter("company_name", etCompanyShopName.getText().toString())
                        .setMultipartParameter("address", etCompanyAddress.getText().toString())
                        .setMultipartParameter("category_id", categoryID)
                        .setMultipartParameter("sub_cat_id", subCategoryID)
                        .setMultipartParameter("area", etAreaLocation.getText().toString())
                        .setMultipartParameter("country_id", countryID)
                        .setMultipartParameter("state_id", stateID)
                        .setMultipartParameter("city_id", cityID)
                        .setMultipartParameter("product_type", serviceType)
                        .setMultipartParameter("mobile", etMobileNo.getText().toString())
                        .setMultipartParameter("pincode", etPinCode.getText().toString())
                        .setMultipartParameter("email_id", etEmail.getText().toString())
                        .setMultipartParameter("phone", etPhoneNo.getText().toString())
                        .setMultipartParameter("facebookurl", etFBUrl.getText().toString())
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("web_url", etWebURL.getText().toString())
                        .setMultipartParameter("googleplusurl", etGooglePlusURL.getText().toString())
                        .setMultipartParameter("twitterurl", etTwitterUrl.getText().toString())
                        .setMultipartParameter("open_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getOpeningTimeID(), daysTileView2.getOpeningTimeID(), daysTileView3.getOpeningTimeID()})))
                        .setMultipartParameter("close_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getClosingTimeID(), daysTileView2.getClosingTimeID(), daysTileView3.getClosingTimeID()})))
                        .setMultipartParameter("youtubeurl", etYoutubeURL.getText().toString())
                        .setMultipartParameter("lat", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0"))
                        .setMultipartParameter("long", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0"))
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("delimg", "")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBarHandler.hide();
                                if (result != null) {
                                    AndroidUtils.showErrorLog(context, "result::::::", result);
                                    if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                        if (Validation.containsIgnoreCase(result.get("message").getAsString(), "successfully updated")) {
                                            AndroidUtils.showErrorLog(context, "result:::success:::", result);
                                            AndroidUtils.showToast(context, result.get("message").getAsString());
                                            doExitReveal(false);

                                        } else {
                                            AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                        }

                                    } else {
                                        AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                    }

                                } else {
                                    AndroidUtils.showErrorLog(context, "result::::::NULL");
                                }
                            }
                        });
            } else {

                for (int i = 0; i < submitImgDelList.size(); i++) {
                    AndroidUtils.showErrorLog(context, "submitDeletedImages----", submitImgDelList.get(i).toString());

                }


                Ion.with(context)
                        .load(getString(R.string.webservice_base_url) + "/editCompany")
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("shop_id", shopId)
                        .setMultipartParameter("user_id", userId)
                        .setMultipartParameter("company_name", etCompanyShopName.getText().toString())
                        .setMultipartParameter("address", etCompanyAddress.getText().toString())
                        .setMultipartParameter("category_id", categoryID)
                        .setMultipartParameter("sub_cat_id", subCategoryID)
                        .setMultipartParameter("area", etAreaLocation.getText().toString())
                        .setMultipartParameter("country_id", countryID)
                        .setMultipartParameter("state_id", stateID)
                        .setMultipartParameter("city_id", cityID)
                        .setMultipartParameter("product_type", serviceType)
                        .setMultipartParameter("mobile", etMobileNo.getText().toString())
                        .setMultipartParameter("pincode", etPinCode.getText().toString())
                        .setMultipartParameter("email_id", etEmail.getText().toString())
                        .setMultipartParameter("phone", etPhoneNo.getText().toString())
                        .setMultipartParameter("facebookurl", etFBUrl.getText().toString())
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("web_url", etWebURL.getText().toString())
                        .setMultipartParameter("googleplusurl", etGooglePlusURL.getText().toString())
                        .setMultipartParameter("twitterurl", etTwitterUrl.getText().toString())
                        .setMultipartParameter("open_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getOpeningTimeID(), daysTileView2.getOpeningTimeID(), daysTileView3.getOpeningTimeID()})))
                        .setMultipartParameter("close_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getClosingTimeID(), daysTileView2.getClosingTimeID(), daysTileView3.getClosingTimeID()})))
                        .setMultipartParameter("youtubeurl", etYoutubeURL.getText().toString())
                        .setMultipartParameter("lat", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0"))
                        .setMultipartParameter("long", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0"))
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                progressBarHandler.hide();
                                if (result != null) {
                                    AndroidUtils.showErrorLog(context, "result::::::", result);
                                } else {
                                    AndroidUtils.showErrorLog(context, "result::::::NULL");
                                }
                            }
                        });
            }
        } else {
            if (submitImgDelList == null || submitImgDelList.size() == 0) {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url) + "/editCompany")
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("user_id", userId)
                        .setMultipartParameter("shop_id", shopId)

                        .setMultipartParameter("company_name", etCompanyShopName.getText().toString())
                        .setMultipartParameter("address", etCompanyAddress.getText().toString())
                        .setMultipartParameter("category_id", categoryID)
                        .setMultipartParameter("area", etAreaLocation.getText().toString())
                        .setMultipartParameter("country_id", countryID)
                        .setMultipartParameter("state_id", stateID)
                        .setMultipartParameter("product_type", serviceType)
                        .setMultipartParameter("sub_cat_id", subCategoryID)
                        .setMultipartParameter("city_id", cityID)
                        .setMultipartParameter("mobile", etMobileNo.getText().toString())
                        .setMultipartParameter("pincode", etPinCode.getText().toString())
                        .setMultipartParameter("email_id", etEmail.getText().toString())
                        .setMultipartParameter("phone", etPhoneNo.getText().toString())
                        .setMultipartParameter("facebookurl", etFBUrl.getText().toString())
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("web_url", etWebURL.getText().toString())
                        .setMultipartParameter("googleplusurl", etGooglePlusURL.getText().toString())
                        .setMultipartParameter("twitterurl", etTwitterUrl.getText().toString())
                        .setMultipartParameter("open_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getOpeningTimeID(), daysTileView2.getOpeningTimeID(), daysTileView3.getOpeningTimeID()})))
                        .setMultipartParameter("close_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getClosingTimeID(), daysTileView2.getClosingTimeID(), daysTileView3.getClosingTimeID()})))
                        .setMultipartParameter("youtubeurl", etYoutubeURL.getText().toString())
                        .setMultipartParameter("lat", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0"))
                        .setMultipartParameter("long", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0"))
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartFile("shop_video", "application/video", submitVideo())

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBarHandler.hide();
                                if (result != null) {
                                    AndroidUtils.showErrorLog(context, "result::::::", result);
                                    if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                        if (Validation.containsIgnoreCase(result.get("message").getAsString().toLowerCase(), "successfully added")) {
                                            AndroidUtils.showErrorLog(context, "result:::success:::", result);
                                            AndroidUtils.showToast(context, result.get("message").getAsString());
                                            doExitReveal(false);
                                        } else {
                                            AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                        }

                                    } else {
                                        AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                    }

                                } else {
                                    AndroidUtils.showErrorLog(context, "result::::::NULL");
                                }
                            }
                        });
            } else {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url) + "/editCompany")
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("user_id", userId)
                        .setMultipartParameter("shop_id", shopId)
                        .setMultipartParameter("company_name", etCompanyShopName.getText().toString())
                        .setMultipartParameter("address", etCompanyAddress.getText().toString())
                        .setMultipartParameter("category_id", categoryID)
                        .setMultipartParameter("area", etAreaLocation.getText().toString())
                        .setMultipartParameter("country_id", countryID)
                        .setMultipartParameter("state_id", stateID)
                        .setMultipartParameter("product_type", serviceType)
                        .setMultipartParameter("sub_cat_id", subCategoryID)
                        .setMultipartParameter("city_id", cityID)
                        .setMultipartParameter("mobile", etMobileNo.getText().toString())
                        .setMultipartParameter("pincode", etPinCode.getText().toString())
                        .setMultipartParameter("email_id", etEmail.getText().toString())
                        .setMultipartParameter("phone", etPhoneNo.getText().toString())
                        .setMultipartParameter("facebookurl", etFBUrl.getText().toString())
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("web_url", etWebURL.getText().toString())
                        .setMultipartParameter("googleplusurl", etGooglePlusURL.getText().toString())
                        .setMultipartParameter("twitterurl", etTwitterUrl.getText().toString())
                        .setMultipartParameter("open_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getOpeningTimeID(), daysTileView2.getOpeningTimeID(), daysTileView3.getOpeningTimeID()})))
                        .setMultipartParameter("close_id", String.valueOf(ParseUtils.stringArrayToJsonObject(new String[]{daysTileView1.getClosingTimeID(), daysTileView2.getClosingTimeID(), daysTileView3.getClosingTimeID()})))
                        .setMultipartParameter("youtubeurl", etYoutubeURL.getText().toString())
                        .setMultipartParameter("lat", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0"))
                        .setMultipartParameter("long", appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0"))
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartFile("shop_video", "application/video", submitVideo())

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBarHandler.hide();
                                if (result != null) {
                                    AndroidUtils.showErrorLog(context, "result::::::", result);
                                    if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                        if (Validation.containsIgnoreCase(result.get("message").getAsString().toLowerCase(), "successfully added")) {
                                            AndroidUtils.showErrorLog(context, "result:::success:::", result);
                                            AndroidUtils.showToast(context, result.get("message").getAsString());
                                            doExitReveal(false);
                                        } else {
                                            AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                        }

                                    } else {
                                        AndroidUtils.showErrorLog(context, "error::::::TRUE");
                                    }

                                } else {
                                    AndroidUtils.showErrorLog(context, "result::::::NULL");
                                }
                            }
                        });
            }
        }
    }

    void doExitReveal(final boolean isBack) {
        if (!isBack) {
            Intent bankDetails = new Intent(context, CompanyShopManagementActivity.class);
            bankDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(bankDetails);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        doExitReveal(true);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        productMediaDatas.add(new ProductMediaData("first", "", null, ""));

        adapter = new ProductImagesAdapter(context, productMediaDatas, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);

    }

    public void picPhoto() {
        String str[] = new String[]{"Image", "Video"};
        new AlertDialog.Builder(context).setItems(str,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str1[] = new String[]{"Capture", "Upload"};
                        if (which == 0) {

                            new AlertDialog.Builder(context).setItems(str1,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            performImgPicAction(which);
                                        }
                                    }).show();

                        } else {
                            new AlertDialog.Builder(context).setItems(str1,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            performVideoAction(which);
                                        }
                                    }).show();
                        }
                    }
                }).show();

    }

    private void performVideoAction(int which) {
        Intent in;
        if (which == 1) {
            in = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Select Video From Gallery"), 12);
        } else {
            in = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            in.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 80);
            in.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath() + "videocapture_example.mp4");
            startActivityForResult(Intent.createChooser(in, "Capture Video From Camera"), 13);
        }
    }

    private void performImgPicAction(int which) {
        Intent in;
        if (which == 1) {
            in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Select Multiple Picture From Gallery"), 11);
        } else {
            in = new Intent();
            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(in, "Capture Image from Camera"), 10);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                progressBarHandler.hide();
                Place place = PlaceAutocomplete.getPlace(context, data);
                etAreaLocation.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                progressBarHandler.hide();
                Status status = PlaceAutocomplete.getStatus(this, data);
                AndroidUtils.showErrorLog(context, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                progressBarHandler.hide();
            }
        }


        multipleImages = new ArrayList<>();
        AndroidUtils.showErrorLog(context, "hi", "requestCode : " + requestCode + "result code : " + resultCode);
        try {
            if (requestCode == 11) {
                uploadImage(requestCode, resultCode, data);

            } else if (requestCode == 10) {

                captureImage(requestCode, resultCode, data);
            }

            if (requestCode == 12) {

                uploadVideo(requestCode, resultCode, data);


            } else if (requestCode == 13) {

                AndroidUtils.showErrorLog(context, "Saved Video =================" + data.getData().getPath());

                captureVideo(requestCode, resultCode, data);
            }


        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "Exception", e.toString());
        }
    }

    private File submitVideo() {
        if (productMediaDatas != null && productMediaDatas.size() > 0) {
            for (ProductMediaData file : productMediaDatas) {
                if (file.isVideo) {
                    AndroidUtils.showErrorLog(context, " video file.toString()", file.toString());
                    return file.videoFile;
                }
            }
        }
        return null;
    }

    private ArrayList<Part> submitImages() {
        ArrayList<Part> files = new ArrayList<>();
        if (productMediaDatas != null && productMediaDatas.size() > 0) {
            for (ProductMediaData file : productMediaDatas) {
                if (!file.isVideo && !isExistingImage(file) && savebitmap(file.imagePath) != null) {
                    files.add(new FilePart("image[]", savebitmap(file.imagePath)));
                    AndroidUtils.showErrorLog(context, files.toArray().toString());
                }
            }
            return files;
        }
        return null;
    }

    private boolean isExistingImage(ProductMediaData file) {
        for (int i = 0; i < imageUrlList.size(); i++) {
            AndroidUtils.showErrorLog(context, "------isExistingImage--imageUrlList.get(i)------->" +
                    imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1]
                    + "-------file.imagePath---------->" +
                    file.imagePath.split("/")[file.imagePath.split("/").length - 1]);
            if (file.imagePath.split("/")[file.imagePath.split("/").length - 1].equals(imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1])) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> submitDeletedImages() {

        ArrayList<String> files = new ArrayList<>();
        if (productMediaDatasDelete != null && productMediaDatasDelete.size() > 0) {
            AndroidUtils.showErrorLog(context, "productMediaDatasDelete---", productMediaDatasDelete.size());

            for (int k = 0; k < productMediaDatasDelete.size(); k++) {
                ProductMediaData file = productMediaDatasDelete.get(k);

                AndroidUtils.showErrorLog(context, " !file.isVideo", !file.isVideo);
                AndroidUtils.showErrorLog(context, "isExistingImagel", isExistingImage(file));
                AndroidUtils.showErrorLog(context, " savebitmap(file.imagePath)!=null", savebitmap(file.imagePath) != null);

                if (!file.isVideo && savebitmap(file.imagePath) != null) {
                    files.add(imageUrlList.get(getPositionOfExistingImage(file)).key.toString());
                }
            }
            return files;
        }
        return null;
    }

    private int getPositionOfExistingImage(ProductMediaData file) {
        for (int i = 0; i < imageUrlList.size(); i++) {
            AndroidUtils.showErrorLog(context, "------isExistingImage--imageUrlList.get(i)------->" + imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1] + "-------file.imagePath---------->" + file.imagePath.split("/")[file.imagePath.split("/").length - 1]);
            if (file.imagePath.split("/")[file.imagePath.split("/").length - 1].equals(imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1])) {
                return i;
            }
        }
        return 0;
    }

    private File savebitmap(String filePath) {
        File file = new File(filePath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("png")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else {
                return null;
            }
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private void captureVideo(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri vid = data.getData();
            videoPath = ImageUtils.getRealPathFromURI(context, vid);

            AndroidUtils.showErrorLog(context, "file.getAbsolutePath()", videoPath);

            final File file = new File(videoPath);

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
                if (!isVideoExists(productMediaDatas)) {
                    productMediaDatas.add(new ProductMediaData("", "", file, videothumbnail.getAbsolutePath()));
                } else {
                    for (int i = 0; i < productMediaDatas.size(); i++) {
                        if (productMediaDatas.get(i).isVideo) {
                            productMediaDatas.get(i).videoFile = file;
                            productMediaDatas.get(i).videoThumbnail = videothumbnail.getAbsolutePath();
                        }
                    }
                }
                AndroidUtils.showErrorLog(context, "docfile", videothumbnail.getAbsolutePath());

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }

        } else if (data != null) {
            Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
            if (e != null) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadVideo(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = data.getData();

        AndroidUtils.showErrorLog(context, "selectedImage----" + selectedImage);

        // AndroidUtils.showToast(context, selectedImage.toString());

        String selectedImagePath = getPath(context, selectedImage);

        // AndroidUtils.showToast(context, "selectedImagePath----------------" + selectedImagePath);

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

        //AndroidUtils.showToast(context, "timeInMillisec-------" + timeInMillisec);

        retriever.release();

        if (timeInMillisec >= 120000) {
            AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
        } else if (timeInMillisec <= 30000) {
            AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
        } else {
            if (!isVideoExists(productMediaDatas)) {
                productMediaDatas.add(new ProductMediaData("", "", file, video_thumbnail.getAbsolutePath()));
            } else {
                for (int i = 0; i < productMediaDatas.size(); i++) {
                    if (productMediaDatas.get(i).isVideo) {
                        productMediaDatas.get(i).videoFile = file;
                        productMediaDatas.get(i).videoThumbnail = video_thumbnail.getAbsolutePath();
                    }
                }
            }
            AndroidUtils.showErrorLog(context, "docfile", video_thumbnail.getAbsolutePath());
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private void captureImage(int requestCode, int resultCode, Intent data) {
        AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

        Bitmap photo = (Bitmap) data.getExtras().get("data");

        Uri tempUri = ImageUtils.getImageUri(context, photo);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

        productMediaDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
        AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);


    }

    private void uploadImage(int requestCode, int resultCode, Intent data) throws IOException {
        if (data.getClipData() != null) {


            for (int k = 0; k < data.getClipData().getItemCount(); k++) {

                Uri selectedImage = data.getClipData().getItemAt(k).getUri();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                multipleImages.add(bitmap);


                AndroidUtils.showErrorLog(context, "doc", "***START.****** ");
                if (ImageUtils.sizeOf(bitmap) > 2048) {
                    AndroidUtils.showErrorLog(context, "doc", "if doc file path 1");
                    docFile = ImageUtils.getFile(context, ImageUtils.resize(bitmap, bitmap.getHeight() / 2, bitmap.getWidth() / 2));
                    AndroidUtils.showErrorLog(context, "doc", "if doc file path" + docFile.getAbsolutePath());
                } else {
                    AndroidUtils.showErrorLog(context, "doc", " else doc file path 1");
                    docFile = ImageUtils.getFile(context, bitmap);
                    AndroidUtils.showErrorLog(context, "doc", " else doc file path" + docFile.getAbsolutePath());
                }

                productMediaDatas.add(new ProductMediaData(docFile.getAbsolutePath(), "", null, ""));
                AndroidUtils.showErrorLog(context, "docfile", docFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                if (productMediaDatas.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);

                }


            }

        } else {


            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Uri tempUri = ImageUtils.getImageUri(context, bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                productMediaDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
                AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                if (productMediaDatas.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private boolean isVideoExists(ArrayList<ProductMediaData> productMediaDataArrayList) {
        for (ProductMediaData productMediaData : productMediaDataArrayList) {
            if (productMediaData.isVideo) return true;
        }
        return false;
    }

    private boolean isImageExists(ArrayList<ProductMediaData> productMediaDataArrayList) {
        for (ProductMediaData productMediaData : productMediaDataArrayList) {
            if (!productMediaData.isVideo) return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Audio.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                String id = "0";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    id = DocumentsContract.getDocumentId(uri);
                }
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
