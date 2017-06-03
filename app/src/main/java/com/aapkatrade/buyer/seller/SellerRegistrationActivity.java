package com.aapkatrade.buyer.seller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.Home.buyerregistration.entity.BuyerRegistration;
import com.aapkatrade.buyer.Home.buyerregistration.entity.City;
import com.aapkatrade.buyer.Home.buyerregistration.entity.Country;
import com.aapkatrade.buyer.Home.buyerregistration.entity.SellerRegistration;
import com.aapkatrade.buyer.Home.buyerregistration.spinner_adapter.SpBussinessAdapter;
import com.aapkatrade.buyer.Home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.login.ActivityOTPVerify;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerRegistrationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static SellerRegistration formSellerData = new SellerRegistration();
    private int isAllFieldSet = 0;
    private LinearLayout uploadCard;
    private Spinner spBussinessCategory, spState, spCity;
    private String[] spBussinessName = {"Please Select Business Type", "Licence", "Personal"};
    private EditText etProductName, etFirstName, etLastName, etDOB, etEmail, etMobileNo, etAddress, etPassword, etReenterPassword, et_tin_number,
            et_tan_number, etReferenceNo;
    private TextView tvSave, uploadMsg, tv_agreement;
    private LinearLayout registrationLayout;
    private ArrayList<Country> countryList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> stateIds = new ArrayList<>();
    private ArrayList<City> cityList = new ArrayList<>();
    private LinearLayout businessDetails, uploadView, uploadPDFView;
    private File compIncorpFile = new File(""), docFile = new File("");
    private boolean isReqCode = false, isCompIncorp = false;
    private ImageView uploadImage, uploadPDFButton, openCalander, cancelImage, cancelFile;
    AppSharedPreference app_sharedpreference;
    private CircleImageView circleImageView, previewPDF;
    private Bitmap imageForPreview;
    HashMap<String, String> webservice_header_type = new HashMap<>();
    private String busiType = "", countryID = "101", stateID, cityID, isAddVendorCall = "false", business_id;
    private RelativeLayout spBussinessCategoryLayout, previewImageLayout, previewPDFLayout, dobLayout;
    private DatePickerDialog datePickerDialog;
    ProgressBarHandler progressBarHandler;
    private CardView businessDetailsCard;
    private RelativeLayout relativeCompanyListheader;
    private Context context;
    private CheckBox agreement_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        context = SellerRegistrationActivity.this;
        app_sharedpreference = new AppSharedPreference(context);
        //isAddVendorCall = app_sharedpreference.getSharedPref("isAddVendorCall");
        setUpToolBar();
        initView();
        saveUserTypeInSharedPreferences();
        setUpBusinessCategory();
        getState();

        uploadPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCompIncorp = true;
                performImgPicAction(2);
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("reach", "reach");
                if (app_sharedpreference.sharedPreferences != null) {

                    Log.e("reach", "reach1");
                    /*
                    Seller Registration
                     */


                        Log.e("reach", "reach2");
                        setSellerFormData();
                        validateFields(String.valueOf(1));
                        if (isAllFieldSet == 0) {
                            callWebServiceForSellerRegistration();
                        }




                }
            }


        });

        openCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SellerRegistrationActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(now);
                dpd.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImageLayout.setVisibility(View.GONE);
            }
        });

        cancelFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewPDFLayout.setVisibility(View.GONE);
            }
        });


    }

    private void showDate(int year, int month, int day) {
        etDOB.setTextColor(getResources().getColor(R.color.black));
        etDOB.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }

    private void saveUserTypeInSharedPreferences() {
        uploadView.setVisibility(View.GONE);
        spBussinessCategoryLayout.setVisibility(View.GONE);
        etProductName.setVisibility(View.GONE);
        dobLayout.setVisibility(View.GONE);
        uploadCard.setVisibility(View.GONE);
        relativeCompanyListheader.setVisibility(View.GONE);
        businessDetailsCard.setVisibility(View.GONE);
        if (app_sharedpreference != null) {
            if (app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), "0").equals(SharedPreferenceConstants.USER_TYPE_SELLER.toString()) || isAddVendorCall.equals("true")) {
                etAddress.setVisibility(View.GONE);
//                findViewById(R.id.height1).setVisibility(View.GONE);
                Log.e("user", "user");
            }

        } else {
            Log.e("user3", "user3");
        }
    }


    private void callWebServiceForSellerRegistration() {
        String URL =  getResources().getString(R.string.webservice_base_url) + "/sellerregister";

        if (docFile.getAbsolutePath().equals("/")) {
            Log.e("reach", "NUL_______DOCCCCCCCLICENCE");
            showmessage("Please Upload Company Document");

        } else {
            if (formSellerData.getBusinessType().contains("1"))

            {
                Log.e("work1", "work1");

                if (compIncorpFile.getAbsolutePath().equals("/")) {
                    Log.e("reach", "NUL_______compDOCCCCCCCLiCENSE");
                    showmessage("Please Upload Company Incorporation ( PDF Only )");

                } else {
                    progressBarHandler.show();

                    Ion.with(SellerRegistrationActivity.this)
                            .load(URL)
                            .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3").progress(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            Log.e("status", downloaded + "  * " + total);
                        }
                    })
                            .setMultipartFile("personal_doc", "image*//*", docFile)
                            //.setMultipartFile("personal_doc", "image*//*", docFile)
                            .setMultipartFile("comp_incorporation", "image*//*", compIncorpFile)
                            .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                            .setMultipartParameter("business_type", formSellerData.getBusinessType())
                            .setMultipartParameter("business_id", business_id)
                            .setMultipartParameter("companyname", formSellerData.getCompanyName())
                            .setMultipartParameter("name", formSellerData.getFirstName())
                            .setMultipartParameter("lastname", formSellerData.getLastName())
                            .setMultipartParameter("dob", formSellerData.getDOB())
                            .setMultipartParameter("mobile", formSellerData.getMobile())
                            .setMultipartParameter("email", formSellerData.getEmail())
                            .setMultipartParameter("password", formSellerData.getPassword())
                            .setMultipartParameter("country_id", formSellerData.getCountryId())
                            .setMultipartParameter("state_id", formSellerData.getStateId())
                            .setMultipartParameter("city_id", formSellerData.getCityId())
                            .setMultipartParameter("client_id", formSellerData.getClientId())
                            .setMultipartParameter("shopname", formSellerData.getShopName())
                            .setMultipartParameter("tin_number", "521651")
                            .setMultipartParameter("tan_number", "13546848")
                            .setMultipartParameter("tc", "fdssd")
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    progressBarHandler.hide();
AndroidUtils.showErrorLog(context,"work1 response"+result);

                                    if (result != null) {
                                        Log.e("registration_seller", result.toString());
                                        if (result.get("error").getAsString().equals("false")) {



                                            Log.e("registration_seller", "done");
                                            AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());

                                            Intent call_to_startactivity = new Intent(SellerRegistrationActivity.this, ActivityOTPVerify.class);
                                            call_to_startactivity.putExtra("class_name","SellerRegistrationActivity");
                                            startActivity(call_to_startactivity);


                                        } else {
                                            AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());
                                        }
                                    } else {
                                        Log.e("result_seller_error", e.toString());
                                        showmessage(e.toString());
                                    }

                                }

                            });


                }
            } else {
                Log.e("work2", "work2");
                progressBarHandler.show();

                Ion.with(SellerRegistrationActivity.this)
                        .load(URL)
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3").progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        Log.e("status", downloaded + "  * " + total);
                    }
                })
                        //.setMultipartFile("company_doc", "image*//*", docFile)
                        .setMultipartFile("personal_doc", "image*//*", docFile)

                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("business_type", formSellerData.getBusinessType())
                        .setMultipartParameter("business_id", business_id)
                        .setMultipartParameter("companyname", formSellerData.getCompanyName())
                        .setMultipartParameter("name", formSellerData.getFirstName())
                        .setMultipartParameter("lastname", formSellerData.getLastName())
                        .setMultipartParameter("dob", formSellerData.getDOB())
                        .setMultipartParameter("mobile", formSellerData.getMobile())
                        .setMultipartParameter("email", formSellerData.getEmail())
                        .setMultipartParameter("password", formSellerData.getPassword())
                        .setMultipartParameter("country_id", formSellerData.getCountryId())
                        .setMultipartParameter("state_id", formSellerData.getStateId())
                        .setMultipartParameter("city_id", formSellerData.getCityId())
                        .setMultipartParameter("client_id", formSellerData.getClientId())
                        .setMultipartParameter("shopname", formSellerData.getShopName())
                        .setMultipartParameter("tin_number", "521651")
                        .setMultipartParameter("tan_number", "13546848")
                        .setMultipartParameter("tc", "fdssd")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBarHandler.hide();



                                if (result != null) {
                                    Log.e("registration_seller", result.toString());
                                    if (result.get("error").getAsString().equals("false")) {
                                        AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());
                                        Log.e("registration_seller", "done");
                                        Intent call_to_startactivity = new Intent(SellerRegistrationActivity.this, ActivityOTPVerify.class);
                                        call_to_startactivity.putExtra("class_name","SellerRegistrationActivity");
                                        startActivity(call_to_startactivity);
                                    } else {

                                        AndroidUtils.showSnackBar(registrationLayout, result.get("message").getAsString());
                                    }

                                } else {
                                    Log.e("result_seller_error", e.toString());
                                    showmessage(e.toString());
                                }

                            }

                        });


            }
        }
    }


    private File getFile(Bitmap photo) {
        Uri tempUri = null;
        if (photo != null) {
            tempUri = getImageUri(context, photo);
        }
        File finalFile = new File(getRealPathFromURI(tempUri));
        Log.e("data", getRealPathFromURI(tempUri));

        return finalFile;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = null;
        int idx = 0;
        if (uri != null) {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        }
        return cursor.getString(idx);
    }



    private void setUpBusinessCategory() {

        SpBussinessAdapter spadapter = new SpBussinessAdapter(getApplicationContext(), spBussinessName);

        spBussinessCategory.setDropDownHorizontalOffset(Gravity.CENTER);

        spBussinessCategory.setAdapter(spadapter);
        spBussinessCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                busiType = String.valueOf(position);
                if (position == 0) {
                    uploadCard.setVisibility(View.GONE);
                    findViewById(R.id.input_layout_tin_number).setVisibility(View.GONE);
                    findViewById(R.id.input_layout_tan_number).setVisibility(View.GONE);
                }
                if (position == 2) {
                    uploadCard.setVisibility(View.VISIBLE);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadPDFView.setVisibility(View.GONE);
                    ((TextInputLayout) findViewById(R.id.input_layout_shop_name)).setHint(getString(R.string.shop_name));
                    uploadMsg.setText(getString(R.string.personal_doc));
                    findViewById(R.id.input_layout_tin_number).setVisibility(View.GONE);
                    findViewById(R.id.input_layout_tan_number).setVisibility(View.GONE);

                } else if (position == 1) {
                    uploadCard.setVisibility(View.VISIBLE);
                    uploadView.setVisibility(View.VISIBLE);
                    uploadPDFView.setVisibility(View.VISIBLE);
                    ((TextInputLayout) findViewById(R.id.input_layout_shop_name)).setHint(getString(R.string.company_name_heading));
                    findViewById(R.id.input_layout_tin_number).setVisibility(View.VISIBLE);
                    findViewById(R.id.input_layout_tan_number).setVisibility(View.VISIBLE);
                    uploadMsg.setText(getString(R.string.comp_doc));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void getState() {
        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
        stateIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_ids)));
        CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(context, stateList);
        spState.setAdapter(spinnerArrayAdapter);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList = new ArrayList<>();
                AndroidUtils.showErrorLog(context, "State Id is ::::::::" + position);
                if (position > 0) {
                    stateID = String.valueOf(position);
                    findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
                  //  findViewById(R.id.view1).setVisibility(View.VISIBLE);

                    getCity(String.valueOf(stateIds.get(position)));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getCity(String stateId) {
        progressBarHandler.show();
        findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
        Ion.with(context)
                .load("http://aapkatrade.com/slim/dropdown")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "city")
                .setBodyParameter("id", stateId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        Log.e("city result ", result == null ? "null" : result.toString());

                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");

                            City cityEntity_init = new City("-1", "Please Select City");
                            cityList.add(cityEntity_init);

                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                City cityEntity = new City(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString());
                                cityList.add(cityEntity);
                            }

                            SpCityAdapter spCityAdapter = new SpCityAdapter(context, cityList);
                            spCity.setAdapter(spCityAdapter);

                            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    cityID = cityList.get(position).cityId;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            AndroidUtils.showToast(context, "! Invalid city");
                        }
                    }

                });

    }


    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void initView() {

        businessDetailsCard = (CardView) findViewById(R.id.businessDetailsCard);
        et_tin_number = (EditText) findViewById(R.id.et_tin_number);
        et_tan_number = (EditText) findViewById(R.id.et_tan_number);
        relativeCompanyListheader = (RelativeLayout) findViewById(R.id.relativeCompanyListheader);
        uploadCard = (LinearLayout) findViewById(R.id.uploadCard);
        progressBarHandler = new ProgressBarHandler(this);
        registrationLayout = (LinearLayout) findViewById(R.id.registrationLayout);
        spBussinessCategory = (Spinner) findViewById(R.id.spBussinessCategory);
        spState = (Spinner) findViewById(R.id.spStateCategory);
        spCity = (Spinner) findViewById(R.id.spCityCategory);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setText(getString(R.string.save));
        uploadMsg = (TextView) findViewById(R.id.uploadMsg);
        etProductName = (EditText) findViewById(R.id.etshopname);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        businessDetails = (LinearLayout) findViewById(R.id.businessDetails);
        spBussinessCategoryLayout = (RelativeLayout) findViewById(R.id.spBussinessCategoryLayout);
        etReenterPassword = (EditText) findViewById(R.id.etReenterPassword);
        etReferenceNo = (EditText) findViewById(R.id.etReferenceNo);
        uploadView = (LinearLayout) findViewById(R.id.uploadView);
        uploadPDFView = (LinearLayout) findViewById(R.id.uploadPDFView);
        circleImageView = (CircleImageView) findViewById(R.id.previewImage);
        previewPDF = (CircleImageView) findViewById(R.id.previewPDF);
        uploadImage = (ImageView) findViewById(R.id.uploadButton);
        uploadPDFButton = (ImageView) findViewById(R.id.uploadPDFButton);
        openCalander = (ImageView) findViewById(R.id.openCalander);
        previewImageLayout = (RelativeLayout) findViewById(R.id.previewImageLayout);
        previewPDFLayout = (RelativeLayout) findViewById(R.id.previewPDFLayout);
        cancelImage = (ImageView) findViewById(R.id.cancelImage);
        cancelFile = (ImageView) findViewById(R.id.cancelFile);
        dobLayout = (RelativeLayout) findViewById(R.id.dobLayout);
        webservice_header_type.put("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3");
        business_id = app_sharedpreference.getSharedPref("business_id") == null ? "" : app_sharedpreference.getSharedPref("business_id");
        agreement_check = (CheckBox) findViewById(R.id.agreement_check);
        tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        tv_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreement_check.isChecked()) {
                    agreement_check.setChecked(false);
                } else {
                    agreement_check.setChecked(true);
                }
            }
        });


        City cityEntity_init = new City("-1", "Please Select City");
        cityList.add(cityEntity_init);
        SpCityAdapter spCityAdapter = new SpCityAdapter(context, cityList);
        spCity.setAdapter(spCityAdapter);


    }

    private void validateFields(String userType) {
        isAllFieldSet = 0;
        Log.e("reach", "validateFiledsCalled");
        if (isAddVendorCall.equals("true")) {
            if (formSellerData != null) {

                Log.e("reach", formSellerData.toString() + "            DATAAAAAAAAA");
                if (Validation.isEmptyStr(formSellerData.getFirstName())) {
                    putError(0);
                    isAllFieldSet++;
                } else if (Validation.isEmptyStr(formSellerData.getLastName())) {
                    putError(1);
                    isAllFieldSet++;
                } else if (!Validation.isValidNumber(formSellerData.getMobile(), Validation.getNumberPrefix(formSellerData.getMobile()))) {
                    putError(3);
                    isAllFieldSet++;
                } else if (Validation.isEmptyStr(formSellerData.getBusinessType())
                        || formSellerData.getBusinessType().equals("0")) {
                    showmessage("Please Select Business Category");
                    isAllFieldSet++;
                } else if (Validation.isEmptyStr(etProductName.getText().toString())) {
                    putError(12);
                    isAllFieldSet++;
                } else if (formSellerData.getBusinessType().equals("1") && Validation.isEmptyStr(et_tin_number.getText().toString())) {
                    putError(13);
                    isAllFieldSet++;
                } else if (formSellerData.getBusinessType().equals("1") && Validation.isEmptyStr(et_tan_number.getText().toString())) {
                    putError(14);
                    isAllFieldSet++;
                } else if (!(Validation.isNonEmptyStr(formSellerData.getStateId()) &&
                        Integer.parseInt(formSellerData.getStateId()) > 0)) {
                    showmessage("Please Select State");
                    isAllFieldSet++;
                } else if (!(Validation.isNonEmptyStr(formSellerData.getCityId()) &&
                        Integer.parseInt(formSellerData.getCityId()) > 0)) {
                    showmessage("Please Select City");
                    isAllFieldSet++;
                } else if (!Validation.isValidDOB(formSellerData.getDOB())) {
                    putError(6);
                    isAllFieldSet++;
                } else if (!Validation.isValidEmail(formSellerData.getEmail())) {
                    putError(2);
                    isAllFieldSet++;
                } else if (!Validation.isValidPassword(formSellerData.getPassword())) {
                    putError(4);
                    isAllFieldSet++;
                } else if (!Validation.isValidPassword(formSellerData.getConfirmPassword())) {
                    putError(4);
                    isAllFieldSet++;
                } else if (!Validation.isPasswordMatching(formSellerData.getPassword(), formSellerData.getConfirmPassword())) {
                    putError(5);
                    isAllFieldSet++;
                } else if (!agreement_check.isChecked()) {
                    putError(16);
                }


            }
            Log.d("error", "error Null");
        }


    }

    private void putError(int id) {
        Log.e("reach", "       )))))))))" + id);
        switch (id) {
            case 0:
                etFirstName.setError("First Name Can't be empty");
                showmessage("First Name Can't be empty");
                break;
            case 1:
                etLastName.setError("Last Name Can't be empty");
                showmessage("Last Name Can't be empty");
                break;
            case 2:
                etEmail.setError("Please Enter Valid Email");
                showmessage("Please Enter Valid Email");
                break;
            case 3:
                etMobileNo.setError("Please Enter Valid Mobile Number");
                showmessage("Please Enter Valid Mobile Number");
                break;
            case 4:
                etPassword.setError(getResources().getString(R.string.password_validing_text));
                showmessage(getResources().getString(R.string.password_validing_text));
                break;
            case 5:
                etReenterPassword.setError("Password did not match");
                showmessage("Password did not match");
                break;
            case 6:
//                etDOB.setError("Please Select Date");
                showmessage("Please Select Date");
                break;
            case 9:
                etAddress.setError("Address Can't be empty");
                showmessage("Address Can't be empty");
                break;
            case 10:
                showmessage("Please Enter Valid UserName");
                break;
            case 12:
                if (formSellerData.getBusinessType().equals("1")) {
                    etProductName.setError("Please Enter Shop Name");
                    showmessage("Please Enter Shop Name");
                } else if (formSellerData.getBusinessType().equals("2")) {
                    etProductName.setError("Please Enter Company Name");
                    showmessage("Please Enter Company Name");
                }
                break;
            case 13:
                et_tin_number.setError("Tin Number Can't be empty");
                showmessage("Tin Number Can't be empty");
                break;
            case 14:
                et_tan_number.setError("Tan Number Can't be empty");
                showmessage("Tan Number Can't be empty");
                break;
            case 15:
                etReenterPassword.setError(getResources().getString(R.string.password_validing_text));
                showmessage(getResources().getString(R.string.password_validing_text));
                break;
            case 16:
                showmessage("Please Check the Agreement");
                break;

            default:
                break;
        }
    }

    public void showmessage(String message) {
        AndroidUtils.showSnackBar(registrationLayout, message);
    }

    void picPhoto() {
        String str[] = new String[]{"Camera", "Gallery", "PDF Files"};
        new AlertDialog.Builder(this).setItems(str,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performImgPicAction(which);
                    }
                }).show();
    }

    void performImgPicAction(int which) {
        Intent in;
        if (which == 1) {
            in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(in, "Select profile picture"), 11);
        } else if (which == 2) {


            MaterialFilePicker filePicker = new MaterialFilePicker();


            filePicker.withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.pdf$"))
                    .withFilterDirectories(false)
                    .withHiddenFiles(true)
                    .start();
        } else {
            in = new Intent();
            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(in, "Select profile picture"), 11);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("hi", "requestCode : " + requestCode + "result code : " + resultCode);

        try {
            if (requestCode == 1) {

                Log.e("hi", " if else if 1 ");
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                File file = new File(filePath);
                if (!filePath.equals("result_file_path")) {
                    if (isCompIncorp) {
                        previewPDFLayout.setVisibility(View.VISIBLE);
                        previewPDF.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pdf));
                        compIncorpFile = file;
                        isCompIncorp = false;
                    } else {
                        previewImageLayout.setVisibility(View.VISIBLE);
                        circleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pdf));
                        docFile = file;
                    }
                }
                Log.e("hi", "pdf file path : " + file.getAbsolutePath() + "\n" + filePath);


            } else if (requestCode == 11) {
                Log.e("hi", " if else if 2 ");
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
                                FileDescriptor fileDescriptor = pfd.getFileDescriptor();

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
                try {
                    previewImageLayout.setVisibility(View.VISIBLE);
                    Log.e("doc", "***START.****** ");
                    if (ImageUtils.sizeOf(imageForPreview) > 2048) {
                        Log.e("doc", "if doc file path 1");
                        circleImageView.setImageBitmap(ImageUtils.resize(imageForPreview, imageForPreview.getHeight() / 2, imageForPreview.getWidth() / 2));
                        docFile = getFile(ImageUtils.resize(imageForPreview, imageForPreview.getHeight() / 2, imageForPreview.getWidth() / 2));
                        Log.e("doc", "if doc file path" + docFile.getAbsolutePath());
                    } else {
                        circleImageView.setImageBitmap(imageForPreview);
                        Log.e("doc", " else doc file path 1");
                        docFile = getFile(imageForPreview);
                        Log.e("doc", " else doc file path" + docFile.getAbsolutePath());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setSellerFormData() {

        formSellerData.setBusinessType(busiType);
        formSellerData.setCompanyName(etProductName.getText().toString());
        formSellerData.setShopName(etProductName.getText().toString());
        formSellerData.setFirstName(etFirstName.getText().toString());
        formSellerData.setLastName(etLastName.getText().toString());
        formSellerData.setEmail(etEmail.getText().toString());
        formSellerData.setDOB(etDOB.getText() == null ? "1992-10-10" : etDOB.getText().toString());
        formSellerData.setMobile(etMobileNo.getText().toString());
        formSellerData.setPassword(etPassword.getText().toString());
        formSellerData.setConfirmPassword(etReenterPassword.getText().toString());
        formSellerData.setClientId(AppConfig.getCurrentDeviceId(context));
        formSellerData.setBusinessType(busiType == null ? "" : busiType);
        formSellerData.setCountryId(countryID == null ? "" : countryID);
        formSellerData.setStateId(stateID == null ? "" : stateID);
        formSellerData.setCityId(cityID == null ? "" : cityID);
    }





    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        showDate(year, monthOfYear + 1, dayOfMonth);
    }
}