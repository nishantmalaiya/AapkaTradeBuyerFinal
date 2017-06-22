package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop;

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
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSimpleListAdapter;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.home.navigation.entity.SubCategory;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct.ProductImagesAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct.ProductMediaData;
import com.afollestad.materialcamera.MaterialCamera;
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
import java.net.URI;
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
    public ArrayList<ProductMediaData> productMediaDatas = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ProductImagesAdapter adapter;
    Bitmap imageForPreview;
    int values_count = 0;
    ArrayList<Bitmap> multiple_images;
    RelativeLayout rl_layout1_saveandcontinue_container;
    List<Telephony.Mms.Part> files_image = new ArrayList();
    RelativeLayout relativeImage;
    String videoPath;


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
        /*relativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
            }
        });*/
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


    private void initView()
    {
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

//        relativeImage = (RelativeLayout) findViewById(R.id.relativeImage);
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


    private void getState()
    {

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


    /*private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ProductImagesAdapter(context, productMediaDatas, this);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }*/


    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        productMediaDatas.add(new ProductMediaData("first", "",null,""));

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

    private void performVideoAction(int which)
    {
        Intent in;
        if (which == 1) {
            in = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Select Video From Gallery"), 12);
        } else {

            in = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            in.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 80);
            in.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath()+"videocapture_example.mp4");

            startActivityForResult(Intent.createChooser(in, "Capture Video From Camera"), 13);

            /*try {
                // code buggy code
                File saveDir = null;
                saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                saveDir.mkdirs();

                MaterialCamera materialCamera = new MaterialCamera(AddCompanyShopActivity.this)
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

                materialCamera.start(13);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
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
        } else
            {
                in = new Intent();
                in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(in, "Capture Image from Camera"), 10);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
            if (requestCode == 11)
            {
                if (data.getClipData() != null)
                {

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

                        productMediaDatas.add(new ProductMediaData(docFile.getAbsolutePath(), "",null,""));
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

                        productMediaDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "",null,""));
                        AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                        adapter.notifyDataSetChanged();
                        if (productMediaDatas.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }

            } else if (requestCode == 10)
            {

                AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = ImageUtils.getImageUri(context, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                productMediaDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "",null,""));
                AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }

            if (requestCode == 12)
            {


                    Uri selectedImage = data.getData();

                     System.out.println("selectedImage----"+selectedImage);

                    AndroidUtils.showToast(context,selectedImage.toString());

                 String selectedImagePath = getPath(context, selectedImage);

                AndroidUtils.showToast(context, "selectedImagePath----------------" + selectedImagePath);

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

                AndroidUtils.showToast(context, "timeInMillisec-------" + timeInMillisec);

                retriever.release();

                // edit_profilevideo_webservice(file,video_thumbnail);

                if (timeInMillisec >= 120000) {
                    AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");

                } else if (timeInMillisec <= 30000) {
                    AndroidUtils.showToast(context, "Video timing should be between 30 to 120 second only");
                } else {
                    productMediaDatas.add(new ProductMediaData("","",file, video_thumbnail.getAbsolutePath()));
                    AndroidUtils.showErrorLog(context, "docfile", video_thumbnail.getAbsolutePath());

                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }


            } else if (requestCode == 13)
            {

                System.out.println("Saved Video ================="+data.getData().getPath());

                if (resultCode == RESULT_OK)
                {

                    Uri vid = data.getData();
                    videoPath =ImageUtils.getRealPathFromURI(context,vid);

                    AndroidUtils.showErrorLog(context,"file.getAbsolutePath()",videoPath);

                    final File file = new File(videoPath);

                    AndroidUtils.showErrorLog(context,"file.getAbsolutePath()",file.getAbsolutePath());

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

                        productMediaDatas.add(new ProductMediaData("","",file, videothumbnail.getAbsolutePath()));
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


        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "Exception", e.toString());
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat){
            filePath = generateFromKitkat(uri,context);
        }

        if(filePath != null){
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);

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
    private String generateFromKitkat(Uri uri, Context context){
        String filePath = null;
        if(DocumentsContract.isDocumentUri(context, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Audio.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);



            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }
















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

                final String id = DocumentsContract.getDocumentId(uri);
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
                final String[] selectionArgs = new String[] {
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
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

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
