package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.DynamicFormEntity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;
import com.aapkatrade.buyer.uicomponent.customchecklist.CustomCheckList;
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

public class AddProductActivity extends AppCompatActivity {
    private File docFile = new File("");
    private ArrayList<ProductMediaData> productImagesDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductImagesAdapter adapter;
    private ArrayList<Bitmap> multiple_images;
    private EditText etproductname, etProductPrice, etProductPriceDiscount, etProductWeight, etDescription, etMaxorderQuantity, etProductLength, etProductWidth, etProductHeight;
    private TextView save;
    private List<Part> files = new ArrayList();
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressBarHandler;
    private Context context;
    private Spinner spCompanyList, spUnitCategory;
    private ArrayList<City> cityList = new ArrayList<>();
    private ArrayList<City> unitList = new ArrayList<>();
    private String cityID, unitID, shopId;
    private ArrayList<DynamicFormEntity> dynamicFormEntityArrayList = new ArrayList<>();
    private LinearLayout llSellerProductDetailContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);
        context = AddProductActivity.this;
        appSharedpreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        if (getIntent() != null) {
            shopId = getIntent().getStringExtra("shopId");
        }

        setUpToolBar();
        initView();
        loadDynamicForm(shopId);
        setupRecyclerView();
        getCity("");
        getUnit();

    }

    private void loadDynamicForm(String shopId) {
        AndroidUtils.showErrorLog(context, "     shop_id     ", shopId == null ? "0" : shopId);
        progressBarHandler.show();
        Ion.with(context)
                .load(new StringBuilder(getString(R.string.webservice_base_url)).append("/list_formdata").toString())
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("shop_id", shopId == null ? "0" : shopId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();

                        if (result != null) {
                            if (result.get("status").getAsString().contains("true")) {
                                JsonArray jsonResultArray = result.get("result").getAsJsonArray();
                                if (jsonResultArray != null && jsonResultArray.size() > 0) {

                                    for (int i = 0; i < jsonResultArray.size(); i++) {
                                        JsonObject jsonObject = (JsonObject) jsonResultArray.get(i);
                                        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
                                        dynamicFormEntity.setMultiple(false);
                                        dynamicFormEntity.setName(jsonObject.get("name").getAsString());
                                        dynamicFormEntity.setLabel(jsonObject.get("label").getAsString());
                                        dynamicFormEntity.setType(jsonObject.get("type").getAsString());
                                        if (jsonObject.get("is_multiple").getAsString().contains("true")) {
                                            dynamicFormEntity.setMultiple(true);
                                            if (jsonObject.get("value") != null) {
                                                JsonArray multipleValueArray = jsonObject
                                                        .get("value").getAsJsonArray();
                                                if (multipleValueArray != null && multipleValueArray.size() > 0) {
                                                    for (int j = 0; j < multipleValueArray.size(); j++) {
                                                        JsonObject jsonObject1 = (JsonObject) multipleValueArray.get(j);
                                                        dynamicFormEntity.addToFormValueArrayList(new FormValue(jsonObject1.get("name").getAsString(), jsonObject1.get("value").getAsString()));
                                                    }
                                                }
                                            }
                                        } else {
                                            dynamicFormEntity.addToFormValueArrayList(new FormValue(jsonObject.get("label") == null ? "" : jsonObject.get("label").getAsString(), jsonObject.get("value") == null ? "" : jsonObject.get("value").getAsString()));
                                        }
                                        dynamicFormEntityArrayList.add(dynamicFormEntity);
                                    }
                                    AndroidUtils.showErrorLog(context, "dynamicFormEntityArrayList size : " + dynamicFormEntityArrayList.size(), dynamicFormEntityArrayList.toString());

                                } else {
                                    AndroidUtils.showErrorLog(context, "jsonResultArray is null or jsonResultArray.size == 0");
                                }
                            }
                            createDynamicForm();
                        } else {
                            AndroidUtils.showErrorLog(context, "list formdata showErrorLog", e.toString());
                        }
                    }
                });
    }


    private void createDynamicForm() {
        if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {
            for (DynamicFormEntity dynamicFormEntity : dynamicFormEntityArrayList) {
                String title = dynamicFormEntity.getLabel();
                String type = dynamicFormEntity.getType();
                if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("dropdown")) {
//                    createDynamicSpinner(title, type, dynamicFormEntity.getFormValueArrayList());

                    createDynamicCheckList(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("checkbox")) {
                    createDynamicCheckList(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("radio")) {
                    createDynamicRadioGroup(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("number") || type.equalsIgnoreCase("textarea")) {
                    createDynamicEditText(title, type);
                }
            }
        }
    }

    private void createDynamicRadioGroup(String title, String type, ArrayList<FormValue> formValueArrayList) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_check_list, null, false);
        CustomCheckList customCheckList = (CustomCheckList) view.findViewById(R.id.customCheckList);
        customCheckList.setTag(title);
        customCheckList.setBasicRequiredValues(title, formValueArrayList, true);
        llSellerProductDetailContainer.addView(view);
    }

    private void createDynamicCheckList(String title, String type, ArrayList<FormValue> formValueArrayList) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_check_list, null, false);
        CustomCheckList customCheckList = (CustomCheckList) view.findViewById(R.id.customCheckList);
        customCheckList.setTag(title);
        customCheckList.setBasicRequiredValues(title, formValueArrayList, false);
        llSellerProductDetailContainer.addView(view);

        AndroidUtils.showErrorLog(context, "+++++++++++customCheckList.getTag("+title+")++++++++++++", null == customCheckList.findViewWithTag(title));
    }

    private void createDynamicSpinner(String title, String type, ArrayList<FormValue> formValueArrayList) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_spinner, null, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setTag(title);
        if (type.equalsIgnoreCase("dropdown")) {
            formValueArrayList.add(0, new FormValue("", "Please Select " + title));
            CustomSpinnerAdapter spinnerArrayAdapter = new CustomSpinnerAdapter(context, formValueArrayList);
            spinner.setAdapter(spinnerArrayAdapter);
        }

        llSellerProductDetailContainer.addView(view);

    }

    private void createDynamicEditText(String title, String type) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_edittext, null, false);
        EditText editText = (EditText) view.findViewById(R.id.edittext);
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout);
        editText.setTag(title);
        if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("textarea")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (type.equalsIgnoreCase("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        textInputLayout.setHint(title);
        llSellerProductDetailContainer.addView(view);
    }

    private void initView() {
        spCompanyList = (Spinner) findViewById(R.id.spCompanyList);
        spUnitCategory = (Spinner) findViewById(R.id.spUnitCategory);
        etproductname = (EditText) findViewById(R.id.etproductname);
        etProductPrice = (EditText) findViewById(R.id.et_product_price);
        etProductPriceDiscount = (EditText) findViewById(R.id.et_product_price_discount);
        etDescription = (EditText) findViewById(R.id.et_description);
        etProductWeight = (EditText) findViewById(R.id.et__product_weight);
        etMaxorderQuantity = (EditText) findViewById(R.id.et_maxorderquantity);
        etProductLength = (EditText) findViewById(R.id.et_product_length);
        etProductWidth = (EditText) findViewById(R.id.et_product_width);
        etProductHeight = (EditText) findViewById(R.id.et_product_height);
        save = (TextView) findViewById(R.id.tvSaveButton);
        llSellerProductDetailContainer = (LinearLayout) findViewById(R.id.llSellerProductDetailContainer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Validation.isNonEmptyStr(etproductname.getText().toString())) {
                    if (Validation.isNonEmptyStr(etProductPrice.getText().toString())) {
                        if (Validation.isNonEmptyStr(etProductPriceDiscount.getText().toString())) {
                            if (Validation.isNonEmptyStr(etDescription.getText().toString())) {
                                if (Validation.isNonEmptyStr(etProductWeight.getText().toString())) {
                                    if (Validation.isNonEmptyStr(etMaxorderQuantity.getText().toString())) {
                                        if (ConnetivityCheck.isNetworkAvailable(context)) {
                                            callAddProductWebservice();
                                        } else {
                                            AndroidUtils.showToast(context, "Please Connect Netwrok");
                                        }
                                    } else {
                                        AndroidUtils.showToast(context, "Please Enter Product Max Order Quantity");
                                    }
                                } else {
                                    AndroidUtils.showToast(context, "Please Enter Product Weight");
                                }
                            } else {
                                AndroidUtils.showToast(context, "Please Enter Product Description");
                            }
                        } else {
                            AndroidUtils.showToast(context, "Please Enter Product Discount");
                        }
                    } else {
                        AndroidUtils.showToast(context, "Please Enter Product Price");
                    }
                } else {
                    AndroidUtils.showToast(context, "Please Enter Product Name");
                }*/

                if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {
                    for (DynamicFormEntity dynamicFormEntity : dynamicFormEntityArrayList) {
                        String title = dynamicFormEntity.getLabel();
                        String type = dynamicFormEntity.getType();
                        if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("dropdown")) {

                            collectDynamicCheckListData(title, type, dynamicFormEntity.getFormValueArrayList());
//                            collectDynamicSpinnerData(title, type, dynamicFormEntity.getFormValueArrayList());
                        } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("checkbox")) {
                            collectDynamicCheckListData(title, type, dynamicFormEntity.getFormValueArrayList());
                        } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("radio")) {
                            collectDynamicRadioGroupData(title, type, dynamicFormEntity.getFormValueArrayList());
                        } else if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("number") || type.equalsIgnoreCase("textarea")) {
                            collectDynamicEditTextData(title, type);
                        }
                    }
                }

            }
        });


    }

    private void collectDynamicRadioGroupData(String title, String type, ArrayList<FormValue> formValueArrayList) {
        CustomCheckList customCheckList = (CustomCheckList) llSellerProductDetailContainer.findViewWithTag(title);
        AndroidUtils.showErrorLog(context, "*(((((((((((((((((RadioGroup)))))))))*", customCheckList.getSelectedCheckList());
    }

    private void collectDynamicCheckListData(String title, String type, ArrayList<FormValue> formValueArrayList) {
        CustomCheckList customCheckList = (CustomCheckList) llSellerProductDetailContainer.findViewWithTag(title);
        AndroidUtils.showErrorLog(context, "+++++++++++customCheckList.getTag(" + title + ")2++++++++++++", null == customCheckList.findViewWithTag(title));

        AndroidUtils.showErrorLog(context, "*(((((((((((((((((CheckList)))))))))*", customCheckList.getSelectedCheckList());
    }

    private void collectDynamicEditTextData(String title, String type) {
        EditText editText = (EditText) llSellerProductDetailContainer.findViewWithTag(title);
        AndroidUtils.showErrorLog(context, "*(((((((((((((((((Edit Text)))))))))*", editText.getText());
    }

    private void collectDynamicSpinnerData(String title, String type, ArrayList<FormValue> formValueArrayList) {
        Spinner spinner = (Spinner) llSellerProductDetailContainer.findViewWithTag(title);
        AndroidUtils.showErrorLog(context, "*(((((((((((((((((Spinner)))))))))*", formValueArrayList.get(spinner.getSelectedItemPosition()));
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

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        productImagesDatas.add(new ProductMediaData("first", "", null, ""));
        adapter = new ProductImagesAdapter(AddProductActivity.this, productImagesDatas, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

                        productImagesDatas.add(new ProductMediaData(docFile.getAbsolutePath(), "", null, ""));
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

                        productImagesDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));

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

                Uri tempUri = ImageUtils.getImageUri(AddProductActivity.this, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(ImageUtils.getRealPathFromURI(context, tempUri));

                productImagesDatas.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
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

    private void callAddProductWebservice() {
        progressBarHandler.show();

        for (int i = 0; i < productImagesDatas.size(); i++) {
            if (i == 0) {

            } else {
                files.add(new FilePart("image[]", savebitmap(productImagesDatas.get(i).imagePath)));
                Log.e("sellDatas", files.toArray().toString());
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
                .setMultipartParameter("price", etProductPrice.getText().toString())
                .setMultipartParameter("unit_id", unitID)
                .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                .setMultipartParameter("product_weight", appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .setMultipartParameter("length", etProductLength.getText().toString())
                .setMultipartParameter("width", etProductWidth.getText().toString())
                .setMultipartParameter("height", etProductHeight.getText().toString())
                .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                System.out.println("result------------------------------" + result);

                progressBarHandler.hide();
                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        AndroidUtils.showToast(context, result.get("message").getAsString());
                    }
                } else {
                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                    progressBarHandler.hide();
                }
            }
        });
    }


    private void getCity(String stateId) {
        progressBarHandler.show();
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
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
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


    private void getUnit() {
        unitList.clear();
        progressBarHandler.show();
        // findViewById(R.id.input_layout_city).setVisibility(View.VISIBLE);
        Ion.with(context)
                .load("http://staging.aapkatrade.com/slim/dropdown")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "unit")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
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
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            AndroidUtils.showToast(context, "! Invalid city");
                        }
                    }

                });
    }


}
