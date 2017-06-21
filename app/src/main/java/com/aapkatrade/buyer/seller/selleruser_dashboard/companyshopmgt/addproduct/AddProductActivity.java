package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.Comapany;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.location.GeoCoderAddress;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity
{

    private TextView btnUpload;
    private int count = -1;
    private ArrayList<Comapany> companyList = new ArrayList<>();
    File docFile = new File("");
    public ArrayList<ProductImagesData> productImagesDatas = new ArrayList<>();
    RecyclerView recyclerView;
    ProductImagesAdapter adapter;
    ArrayList<Bitmap> multiple_images;
    List<Telephony.Mms.Part> files_image = new ArrayList();
    private GeoCoderAddress GeocoderAsync;
    private int current_state_index;
    private int step1FieldsSet=-1;
    RelativeLayout relativeImage;
    EditText etproductname,et_product_price,et_product_price_discount,et__product_weight,et_description,et_maxorderquantity,et_product_length,et_product_width,et_product_height;
    TextView tvplaceOrder;
    List<Part> files = new ArrayList();
    private AppSharedPreference app_sharedpreference;
    private ProgressBarHandler p_handler;
    private Context context;
    Spinner spCompanyList,spUnitCategory;
    private ArrayList<City> cityList = new ArrayList<>();
    private ArrayList<City> unitList = new ArrayList<>();
    String cityID,unitID;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        context = AddProductActivity.this;

        app_sharedpreference = new AppSharedPreference(context);

        p_handler = new ProgressBarHandler(context);

//        daysTileView = (DaysTileView) findViewById(R.id.daysTileView);
//        daysTileView.setBackgroundColor(R.color.green);
//        daysTileView.setDayName("Mon - Fri");

//        daysTileView2 = (DaysTileView) findViewById(R.id.daysTileView2);
//        daysTileView2.setBackgroundColor(R.color.md_material_blue_600);
//        daysTileView2.setDayName("Saturday");

//        daysTileView3 = (DaysTileView) findViewById(R.id.daysTileView3);
//        daystileview3.setbackgroundcolor(r.color.red);
//        daystileview3.setDayName("Sunday");


//        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AndroidUtils.showToast(AddProductActivity.this, daysTileView.getOpeningTime()+"   "+daysTileView.getClosingTime());
//                AndroidUtils.showToast(AddProductActivity.this, daysTileView2.getOpeningTime()+"   "+daysTileView2.getClosingTime());
//
//            }
//        });

        context = AddProductActivity.this;

       /* relativeImage = (RelativeLayout) findViewById(R.id.relativeImage);

        relativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
            }
        });
      */
        setUpToolBar();

        setuplayout();

        setupRecyclerView();

        getCity("");

        getUnit();

    }

    private void setuplayout()
    {
        spCompanyList= (Spinner) findViewById(R.id.spCompanyList);

        spUnitCategory= (Spinner) findViewById(R.id.spUnitCategory);

        etproductname = (EditText) findViewById(R.id.etproductname);

        et_product_price = (EditText) findViewById(R.id.et_product_price);

        et_product_price_discount = (EditText) findViewById(R.id.et_product_price_discount);

        et_description = (EditText) findViewById(R.id.et_description);

        et__product_weight = (EditText) findViewById(R.id.et__product_weight);

        et_maxorderquantity = (EditText) findViewById(R.id.et_maxorderquantity);

        et_product_length = (EditText) findViewById(R.id.et_product_length);

        et_product_width = (EditText) findViewById(R.id.et_product_width);

        et_product_height = (EditText) findViewById(R.id.et_product_height);

        tvplaceOrder = (TextView) findViewById(R.id.tvplaceOrder);

        tvplaceOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Validation.isNonEmptyStr(etproductname.getText().toString()))
                {

                    if (Validation.isNonEmptyStr(et_product_price.getText().toString()))
                    {

                        if (Validation.isNonEmptyStr(et_product_price_discount.getText().toString())){


                            if (Validation.isNonEmptyStr(et_description.getText().toString()))
                            {

                                if (Validation.isNonEmptyStr(et__product_weight.getText().toString()))
                                {

                                    if (Validation.isNonEmptyStr(et_maxorderquantity.getText().toString()))
                                    {

                                        //if (ConnetivityCheck.isNetworkAvailable())
                                         call_add_product_webservice();

                                    }
                                    else
                                    {

                                        AndroidUtils.showToast(context,"Please Enter Product Max Order Quantity");
                                    }

                                }
                                else
                                {

                                    AndroidUtils.showToast(context,"Please Enter Product Weight");
                                }


                            }
                            else
                            {

                                AndroidUtils.showToast(context,"Please Enter Product Description");
                            }

                        }
                        else
                        {

                            AndroidUtils.showToast(context,"Please Enter Product Discount");
                        }

                    }
                    else
                    {

                        AndroidUtils.showToast(context,"Please Enter Product Price");
                    }

                }
                else
                {
                    AndroidUtils.showToast(context,"Please Enter Product Name");
                }


            }
        });


    }

    private void setUpToolBar()
    {
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

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }



    }

    private void setupRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        productImagesDatas.add(new ProductImagesData("first", ""));

        adapter = new ProductImagesAdapter(AddProductActivity.this, productImagesDatas,AddProductActivity.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(adapter);

    }


    public void picPhoto()
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

    void performImgPicAction(int which)
    {
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


   /* @Override
    protected void onResume() {
        super.onResume();
      //  spService_type.setSelection(0);
        AndroidUtils.showErrorLog(context, "onResume");
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        multiple_images = new ArrayList<>();

        AndroidUtils.showErrorLog(context, "hi", "requestCode : " + requestCode + "result code : " + resultCode);

        try
        {
            if (requestCode == 11)
            {
                if (data.getClipData() != null)
                {

                    data.getClipData().getItemCount();

                    for (int k = 0; k < 4; k++)
                    {

                        Uri selectedImage = data.getClipData().getItemAt(k).getUri();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        multiple_images.add(bitmap);

                        AndroidUtils.showErrorLog(context, "doc", "***START.****** ");

                        if (ImageUtils.sizeOf(bitmap) > 2048)
                        {
                            AndroidUtils.showErrorLog(context, "doc", "if doc file path 1");
                            docFile = ImageUtils.getFile(context, ImageUtils.resize(bitmap, bitmap.getHeight() / 2, bitmap.getWidth() / 2));
                            AndroidUtils.showErrorLog(context, "doc", "if doc file path" + docFile.getAbsolutePath());
                        }
                        else
                        {
                            AndroidUtils.showErrorLog(context, "doc", " else doc file path 1");
                            docFile = ImageUtils.getFile(context, bitmap);
                            AndroidUtils.showErrorLog(context, "doc", " else doc file path" + docFile.getAbsolutePath());
                        }

                        productImagesDatas.add(new ProductImagesData(docFile.getAbsolutePath(), ""));
                        AndroidUtils.showErrorLog(context, "docfile", docFile.getAbsolutePath());

                        adapter.notifyDataSetChanged();

                        if (productImagesDatas.size() > 0)
                        {
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    }

                }
                else
                {
                    try
                    {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Uri tempUri = ImageUtils.getImageUri(context, bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                        productImagesDatas.add(new ProductImagesData(finalFile.getAbsolutePath(), ""));

                        AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                        adapter.notifyDataSetChanged();

                        if (productImagesDatas.size() > 0)
                        {
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (requestCode == 10)
            {
                AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = ImageUtils.getImageUri(AddProductActivity.this, photo);

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



    private void call_add_product_webservice()
    {
        p_handler.show();

        for (int i = 0; i <productImagesDatas.size(); i++)
        {
            if (i==0){

            }
            else
                {
                files.add(new FilePart("image[]", savebitmap(productImagesDatas.get(i).image_path)));
                Log.e("sellDatas",files.toArray().toString());
            }

        }

        String url = getResources().getString(R.string.webservice_base_url) + "/add_product";

        Ion.with(AddProductActivity.this)
                .load(url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .addMultipartParts(files)
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("user_id", "3")
                .setMultipartParameter("name", etproductname.getText().toString())
                .setMultipartParameter("company_id", cityID)
                 .setMultipartParameter("price", et_product_price.getText().toString())
                .setMultipartParameter("unit_id", unitID)
                .setMultipartParameter("max_order_qty", et_maxorderquantity.getText().toString())
                .setMultipartParameter("product_weight", app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .setMultipartParameter("length", et_product_length.getText().toString())
                .setMultipartParameter("width", et_product_width.getText().toString())
                .setMultipartParameter("height", et_product_height.getText().toString())
                .setMultipartParameter("discount", et_product_price_discount.getText().toString())
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                System.out.println("result------------------------------" + result);

                p_handler.hide();
               /* if (result != null)
                {
                    if (result.get("error").getAsString().contains("false"))
                    {
                        JsonObject jsonObject_result = result.getAsJsonObject("result");
                        String profile_pic = jsonObject_result.get("profile_pic").getAsString();

                        app_sharedpreference.setSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), profile_pic);
                        String a = app_sharedpreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString());

                        System.out.println("a------------------------------" + a);

                        *//*  Picasso.with(context)
                                .load(a)
                                .error(R.drawable.banner)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into(userImageView);

                        p_handler.hide();*//*

                    }
                }
                else
                 {
                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                    p_handler.hide();
                }*/
            }
        });
    }






    private void getCity(String stateId)
    {
        p_handler.show();
       // findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
        Ion.with(context)
                .load("http://aapkatrade.com/slim/listCompany")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "company")
                .setBodyParameter("user_id", "3")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        p_handler.hide();
                        Log.e("city result ", result == null ? "null" : result.toString());

                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");

                            City cityEntity_init = new City("-1", "Please Select Company");
                            cityList.add(cityEntity_init);

                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                City cityEntity = new City(jsonObject1.get("companyId").getAsString(), jsonObject1.get("name").getAsString());
                                cityList.add(cityEntity);
                            }

                            SpCityAdapter spCityAdapter = new SpCityAdapter(context, cityList);
                            spCompanyList.setAdapter(spCityAdapter);

                            spCompanyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



    private void getUnit()
    {
        unitList.clear();
        p_handler.show();
        // findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
        Ion.with(context)
                .load("http://staging.aapkatrade.com/slim/dropdown")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "unit")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        p_handler.hide();
                        Log.e("city result ", result == null ? "null" : result.toString());

                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");


                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                City cityEntity = new City(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString());
                                unitList.add(cityEntity);
                            }

                            SpCityAdapter spCityAdapter = new SpCityAdapter(context, unitList);
                            spUnitCategory.setAdapter(spCityAdapter);

                            spUnitCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    unitID = unitList.get(position).cityId;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        } else {
                            AndroidUtils.showToast(context, "! Invalid city");
                        }
                    }

                });
    }




}
