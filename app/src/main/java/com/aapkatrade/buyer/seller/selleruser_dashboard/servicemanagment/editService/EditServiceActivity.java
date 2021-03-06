package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.editService;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductMediaData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.DynamicFormEntity;
import com.aapkatrade.buyer.welcome.GradientParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditServiceActivity extends AppCompatActivity {
    private File docFile;
    private ArrayList<ProductMediaData> productImagesDatas = new ArrayList<>();
    private RecyclerView recyclerView;

    private ArrayList<Bitmap> multiple_images;
    private EditText etservicename, etserviceOffers, etProductPriceDiscount, etProductWeight, etDescription, etMaxorderQuantity, etProductLength, etProductWidth, etProductHeight;
    private TextView save, tv_shopname, tv_shopCategory, tvHeading;
    private List<Part> files = new ArrayList();
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressBarHandler;
    private Context context;
    private Spinner spCompanyListService;
    private ArrayList<City> cityList = new ArrayList<>();
    private ArrayList<City> unitList = new ArrayList<>();
    private String servicename, service_category_name, serviceid, service_image, companyId, service_shop_name;
    private ArrayList<DynamicFormEntity> dynamicFormEntityArrayList = new ArrayList<>();

    int page = 0;
    ImageView image, service_image_container;
    ArrayList<CompanyDropdownDatas> companyDropdownDatas = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;

    Button saveandupdatebtn;
    private boolean isAllFieldsSet = true;
    RelativeLayout rl_add_service_image_container;
    LinearLayout mainLayout_editService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        context = EditServiceActivity.this;
        appSharedpreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        if (getIntent() != null) {
            serviceid = getIntent().getStringExtra("serviceid");
            servicename = getIntent().getStringExtra("servicename");
            service_category_name = getIntent().getStringExtra("service_category_name");
            service_image = getIntent().getStringExtra("service_image");
            service_shop_name = getIntent().getStringExtra("service_shop_name");
        }
        setUpToolBar();
        initView();
        callShopDetailActivity();

        setdefaultvalues();

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


    private void callShopDetailActivity() {


        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/service_detail")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("service_id", serviceid)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {


                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        JsonObject jsonObject = result.getAsJsonObject("result");
                        String image = jsonObject.get("image").getAsString();
                        String title_name = jsonObject.get("title_name").getAsString();
                        String description = jsonObject.get("description").getAsString();
                        String offers = jsonObject.get("offers").getAsString();
                        Ion.with(context)
                                .load(image)
                                .intoImageView(service_image_container);


                        etservicename.setText(title_name);
                        etDescription.setText(description);
                        etserviceOffers.setText(offers);
                        AndroidUtils.showErrorLog(context, result);
                        AndroidUtils.showToast(context, result.get("message").getAsString());

                    } else {
                        AndroidUtils.showErrorLog(context, "error", result.get("message").getAsString());

                    }

                } else {
                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                }


            }
        });


    }

    private void setdefaultvalues() {

        tv_shopname.setText("Service Name:" + service_shop_name);


        Ion.with(context)
                .load(service_image)
                .intoImageView(service_image_container);
        tv_shopCategory.setText("Category Name:" + service_category_name);


    }


    private void initView() {
        tv_shopname = (TextView) findViewById(R.id.tv_shopname);
        tv_shopCategory = (TextView) findViewById(R.id.tv_shopCategory);
        service_image_container = (ImageView) findViewById(R.id.img_add_service);
        image = (ImageView) findViewById(R.id.add_service_img);
        rl_add_service_image_container = (RelativeLayout) findViewById(R.id.rl_add_service_image_container);
        mainLayout_editService = (LinearLayout) findViewById(R.id.mainLayout_editService);
        //spCompanyListService = (Spinner) findViewById(R.id.spCompanyListService);
        etservicename = (EditText) findViewById(R.id.etservicename);
        etserviceOffers = (EditText) findViewById(R.id.etserviceOffers);
        etDescription = (EditText) findViewById(R.id.etDescription);
        saveandupdatebtn = (Button) findViewById(R.id.saveandupdatebtn);
        tvHeading = (TextView) findViewById(R.id.listfootername);
        tvHeading.setText("Edit Service");

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
                    callEditServiceWebservice();
                }
            }
        });

    }

    private void ValidateFields() {

        isAllFieldsSet = true;

        if (!ConnetivityCheck.isNetworkAvailable(context)) {
            AndroidUtils.showSnackBar(mainLayout_editService, "No Internet Connection available.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............isNetworkAvailable" + isAllFieldsSet);
            isAllFieldsSet = false;

        }

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

    private void callEditServiceWebservice() {


        progressBarHandler.show();

        AndroidUtils.showErrorLog(context, "callAddProductWebservice----------called");

        if (docFile != null)

        {
            Ion.with(context)
                    .load(getString(R.string.webservice_base_url) + "/edit_service")
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setMultipartFile("image", "image/jpg", docFile)
                    .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setMultipartParameter("service_id", serviceid)

                    .setMultipartParameter("service_name", servicename)


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
        } else {


            Ion.with(context)
                    .load(getString(R.string.webservice_base_url) + "/edit_service")
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setMultipartParameter("image", "")
                    .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setMultipartParameter("service_id", serviceid)

                    .setMultipartParameter("service_name", servicename)


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
}
