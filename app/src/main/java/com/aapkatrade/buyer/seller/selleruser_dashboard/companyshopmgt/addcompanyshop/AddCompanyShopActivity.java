package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSimpleListAdapter;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.home.navigation.entity.SubCategory;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct.ProductImagesAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct.ProductImagesData;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCompanyShopActivity extends AppCompatActivity {
    private Context context;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;
    private String userId, stateID, cityID, categoryID, subCategoryID, serviceType;
    private Spinner spState, spCity, spCategory, spSubCategory, spServiceType;
    private ArrayList<String> stateList, stateIds;
    private ArrayList<City> cityList = new ArrayList<>();
    public ArrayList<Category> listDataHeader = new ArrayList<>();
    private ArrayList<SubCategory> listDataChild = new ArrayList<>();
    private EditText tvAreaLocation;
    File docFile = new File("");
    public ArrayList<ProductImagesData> productImagesDatas = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ProductImagesAdapter adapter;
    Bitmap imageForPreview;
    int values_count = 0;
    ArrayList<Bitmap> multiple_images;
    RelativeLayout rl_layout1_saveandcontinue_container;
    List<Telephony.Mms.Part> files_image = new ArrayList();
    RelativeLayout relativeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_shop);
        context = AddCompanyShopActivity.this;
        setUpToolBar();
        initView();
        getState();
        getCategory();
        getServiceType();
        clickevents();
        relativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
            }
        });
        setupRecyclerView();
    }

    private void clickevents() {
        tvAreaLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAreaLocationIntent();
            }
        });
    }


    private void initView() {
        progressBarHandler = new ProgressBarHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString());
        spState = (Spinner) findViewById(R.id.spState);
        spCity = (Spinner) findViewById(R.id.spCity);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        spSubCategory = (Spinner) findViewById(R.id.spSubCategory);
        spServiceType = (Spinner) findViewById(R.id.spServiceType);
        tvAreaLocation = (EditText) findViewById(R.id.tv_area_location);
        findViewById(R.id.input_layout_sub_category).setVisibility(View.GONE);
        findViewById(R.id.input_layout_city).setVisibility(View.GONE);

        relativeImage = (RelativeLayout) findViewById(R.id.relativeImage);
    }


    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
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

        stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));
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
                    getCity(stateIds.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getCategory() {
        listDataChild.clear();
        listDataHeader.clear();
        progressBarHandler.show();
        Log.e("data", "getCategory Entered");
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
//                        Log.e("data", "getCategory result" + data.toString());
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
                                    Log.e("data", Category.toString());

                                }
                            }
                            setCategoryAdapter();
                        }

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
        Log.e("data", this.listDataHeader.toString());
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
                        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
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
                            cityList.clear();
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


    private void hitAddCompanyShopWebService() {

        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/addCompany")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", userId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "result::::::", result.toString());
                            if (result.get("error").getAsString().equalsIgnoreCase("false")) {
                                if (result.get("message").getAsString().toLowerCase().contains("success")) {

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


    public void callAreaLocationIntent() {
        try {
            progressBarHandler.show();
            startActivityForResult(new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build((Activity) context), 1);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            progressBarHandler.hide();
            AndroidUtils.showErrorLog(context, e.toString());
        }
    }


    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ProductImagesAdapter(context, productImagesDatas);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    void picPhoto()
    {
        String str[] = new String[]{"Camera", "Gallery"};
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
            in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Select Multiple Picture From Gallery"), 11);
        } else {

            in = new Intent();
            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(in, "Capture Image from Camera"), 10);
        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                progressBarHandler.show();
                Place place = PlaceAutocomplete.getPlace(context, data);
                tvAreaLocation.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                progressBarHandler.hide();
                Status status = PlaceAutocomplete.getStatus(this, data);
                AndroidUtils.showErrorLog(context, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                progressBarHandler.hide();
            }
        }


        multiple_images = new ArrayList<>();
        AndroidUtils.showErrorLog(context, "hi", "requestCode : " + requestCode + "result code : " + resultCode);
        try {
            if (requestCode == 11) {
                if (data.getClipData() != null) {

                    data.getClipData().getItemCount();

                    for (int k = 0; k < 4; k++) {

                        Uri selectedImage = data.getClipData().getItemAt(k).getUri();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        multiple_images.add(bitmap);


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

                        productImagesDatas.add(new ProductImagesData(docFile.getAbsolutePath(), ""));
                        AndroidUtils.showErrorLog(context, "docfile", docFile.getAbsolutePath());


                        adapter.notifyDataSetChanged();
                        if (productImagesDatas.size() > 0) {
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

                        productImagesDatas.add(new ProductImagesData(finalFile.getAbsolutePath(), ""));
                        AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                        adapter.notifyDataSetChanged();
                        if (productImagesDatas.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
            if (requestCode == 10) {

                AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = ImageUtils.getImageUri(context, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                productImagesDatas.add(new ProductImagesData(finalFile.getAbsolutePath(), ""));
                AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }




        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "Exception", e.toString());
        }
    }



}
