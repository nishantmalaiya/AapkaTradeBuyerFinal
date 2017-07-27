package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.addService;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductImagesAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductMediaData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.DynamicFormEntity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;
import com.aapkatrade.buyer.uicomponent.customchecklist.CustomCheckList;
import com.aapkatrade.buyer.uicomponent.pagingspinner.PagingSpinner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity {
    private File docFile;
    private ArrayList<ProductMediaData> productImagesDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private PagingSpinner pagingSpinner;
    private ArrayList<Bitmap> multiple_images;
    private EditText etservicename, etserviceOffers, etProductPriceDiscount, etProductWeight, etDescription, etMaxorderQuantity, etProductLength, etProductWidth, etProductHeight;
    private TextView save,tvHeading;
    private List<Part> files = new ArrayList();
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressBarHandler;
    private Context context;
   // private Spinner spCompanyListService;
    private ArrayList<City> cityList = new ArrayList<>();
    private ArrayList<City> unitList = new ArrayList<>();
    private String cityID, unitID, shopId, dynamicFormData, companyId;
    private ArrayList<DynamicFormEntity> dynamicFormEntityArrayList = new ArrayList<>();
    private LinearLayout mainLayout;
    int page = 0;
    ImageView image, service_image_container;
    ArrayList<CompanyDropdownDatas> companyDropdownDatas = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;

    Button saveandupdatebtn;
    private boolean isAllFieldsSet = true;
    RelativeLayout rl_add_service_image_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_service);
        context = AddServiceActivity.this;
        appSharedpreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        if (getIntent() != null) {
            shopId = getIntent().getStringExtra("shopId");
        }
        setUpToolBar();
        initView();
        setupSpinner();

    }

    private void setupSpinner() {


        pagingSpinner = (PagingSpinner) findViewById(R.id.pagingSpinner);
        pagingSpinner.setShopType(0);
        pagingSpinner.setSellerId(appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()));




    }


    private void initView() {
        service_image_container = (ImageView) findViewById(R.id.img_add_service);
        image = (ImageView) findViewById(R.id.add_service_img);
        rl_add_service_image_container = (RelativeLayout) findViewById(R.id.rl_add_service_image_container);
        //spCompanyListService = (Spinner) findViewById(R.id.spCompanyListService);
        etservicename = (EditText) findViewById(R.id.etservicename);
        etserviceOffers = (EditText) findViewById(R.id.etserviceOffers);
        etDescription = (EditText) findViewById(R.id.etDescription);
        saveandupdatebtn = (Button) findViewById(R.id.saveandupdatebtn);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        tvHeading=(TextView) findViewById(R.id.listfootername);
        tvHeading.setText("Add Service");
        HandleClickEvent();


    }

    private void HandleClickEvent() {
        rl_add_service_image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setInterpolator(new LinearInterpolator());
                picPhoto();

                image.setRotation(45);

                image.startAnimation(rotate);
            }
        });
        saveandupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateFields();
                if (isAllFieldsSet) {
                    callAddServiceWebservice();
                }
            }
        });

    }

    private void ValidateFields() {

        isAllFieldsSet = true;

        if (docFile == null) {

            AndroidUtils.showSnackBar(mainLayout, "Please Upload/Capture Service Image.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............productImagesDatas" + isAllFieldsSet);
            isAllFieldsSet = false;

        }
        else if (!Validation.isNumber(pagingSpinner.getShopId()) && pagingSpinner.getShopId().equals("0")) {
            AndroidUtils.showSnackBar(mainLayout, "Please Select Company/Shop.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............companyId" + isAllFieldsSet);
            isAllFieldsSet = false;
        }


        else if (!Validation.isNumber(companyId)) {
            AndroidUtils.showSnackBar(mainLayout, "Please Select Company/Shop.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............companyId" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etservicename.getText().toString())) {
            AndroidUtils.showErrorLog(context, companyId + "**************************");
            etservicename.setError("Service Name can not be empty.");
            AndroidUtils.showSnackBar(mainLayout, "Product Name can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etproductname" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etserviceOffers.getText().toString())) {
            etProductPriceDiscount.setError("Product Discount can not be empty.");
            AndroidUtils.showSnackBar(mainLayout, "Product Discount can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etProductPriceDiscount" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etDescription.getText().toString())) {
            etDescription.setError("Product Description can not be empty.");
            AndroidUtils.showSnackBar(mainLayout, "Product Description can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etDescription" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (!ConnetivityCheck.isNetworkAvailable(context)) {
            AndroidUtils.showSnackBar(mainLayout, "No Internet Connection available.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............isNetworkAvailable" + isAllFieldsSet);
            isAllFieldsSet = false;

        }

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


    public void picPhoto() {
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

            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Select Multiple Picture From Gallery"), 11);
        } else {

            in = new Intent();
            in.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(in, "Capture Image from Camera"), 10);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        multiple_images = new ArrayList<>();

        AndroidUtils.showErrorLog(context, "hi", "requestCode : " + requestCode + "result code : " + resultCode);

        try {
            if (requestCode == 11) {


                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                service_image_container.setImageBitmap(bitmap);
                docFile = new File("");
                docFile = ImageUtils.getFile(context, ImageUtils.resize(bitmap, bitmap.getHeight() / 2, bitmap.getWidth() / 2));


                AndroidUtils.showErrorLog(context, "hi", "requestCode : " + requestCode + "image_path : " + docFile.getAbsolutePath());


            }

            if (requestCode == 10) {
                AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");


                service_image_container.setImageBitmap(photo);

                Uri tempUri = ImageUtils.getImageUri(context, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                docFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                // productImagesDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
                AndroidUtils.showErrorLog(context, "docfile", docFile.getAbsolutePath());


            }


        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "Exception", e.toString());
        }

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


    private ArrayList<Part> submitImages() {
        ArrayList<Part> files = new ArrayList<>();

        if (productImagesDatas != null && productImagesDatas.size() > 0) {
            for (int i = 1; i < productImagesDatas.size(); i++) {
                ProductMediaData file = productImagesDatas.get(i);
                if (!file.isVideo) {
                    files.add(new FilePart("image[]", savebitmap(file.imagePath)));
                    AndroidUtils.showErrorLog(context, files.toArray().toString());
                }
            }
            return files;
        }
        return null;
    }

    private void callAddServiceWebservice() {
        progressBarHandler.show();

        AndroidUtils.showErrorLog(context, "callAddProductWebservice----------called");

        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/add_service")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartFile("image", "image/jpg", docFile)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_id", appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))

                .setMultipartParameter("service_name", etservicename.getText().toString())
                .setMultipartParameter("shop_id", companyId)

                .setMultipartParameter("offers", etserviceOffers.getText().toString())

                .setMultipartParameter("description", etDescription.getText().toString())


                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                AndroidUtils.showErrorLog(context, "result----------" + context.getClass().getSimpleName() + "------" + result);





                AndroidUtils.showErrorLog(context, "result----------" + context.getClass().getSimpleName() + "------" + result);

                progressBarHandler.hide();
                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        AndroidUtils.showToast(context, result.get("message").getAsString());
                        if (Validation.containsIgnoreCase(result.get("message").getAsString(), "Added") || Validation.containsIgnoreCase(result.get("message").getAsString(), "Successfully")) {
                            onBackPressed();
                        }
                    }
                } else {
                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                }
            }
        });
    }


}

