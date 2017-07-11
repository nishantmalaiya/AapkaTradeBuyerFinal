package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
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
    private String cityID, unitID, shopId, dynamicFormData, companyId;
    private ArrayList<DynamicFormEntity> dynamicFormEntityArrayList = new ArrayList<>();
    private LinearLayout llSellerProductDetailContainer;
    int page = 0;

    ArrayList<CompanyDropdownDatas> companyDropdownDatas = new ArrayList<>();
    CustomSpinnerAdapter customSpinnerAdapter;

    private boolean isAllFieldsSet = true;


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

        setupSpinner();


    }

    private void setupSpinner() {


        callCompanyListWebservice(++page);


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            spCompanyList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int totalItemCount = companyDropdownDatas.size();

                    if (totalItemCount == scrollY) {
                        if (totalItemCount > 0) {
                            page = page + 1;
                            callCompanyListWebservice(++page);
                        }
                    }
                }
            });
        }*/
        getUnit();

    }


    private void callCompanyListWebservice(int i) {


        String CompanyListWebserviceUrl = getString(R.string.webservice_base_url) + "/shoplist";


        Ion.with(context)
                .load(CompanyListWebserviceUrl)
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .setBodyParameter("lat", "0")
                .setBodyParameter("long", "0")
                .setBodyParameter("apply", "1")

//                .setBodyParameter("page", i + "")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {


                        JsonArray jsonArray_response = result.getAsJsonArray("result");
                        companyDropdownDatas.add(new CompanyDropdownDatas("", "", "", ""));
                        for (int i = 0; i < jsonArray_response.size(); i++) {


                            JsonObject jsonObject = jsonArray_response.get(i).getAsJsonObject();
                            String companyId = jsonObject.get("id").getAsString();
                            String companyImageUrl = jsonObject.get("image_url").getAsString();
                            String companyName = jsonObject.get("name").getAsString();
                            String comapanyCategory = jsonObject.get("category_name").getAsString();

                            companyDropdownDatas.add(new CompanyDropdownDatas(companyId, companyImageUrl, companyName, comapanyCategory));


                        }

                        customSpinnerAdapter = new CustomSpinnerAdapter(context, companyDropdownDatas);

                        spCompanyList.setAdapter(customSpinnerAdapter);
                        spCompanyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 0) {
                                    companyId = companyDropdownDatas.get(position).comapanyId;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }


                }


                AndroidUtils.showErrorLog(context, "ShopListResponse", result);


            }
        });


        // CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter()


    }


    private void loadDynamicForm(final String shopId) {

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
                                AndroidUtils.showErrorLog(context, "YYYYYYYYYYYYYYYYYYYYYYYYY" + shopId, jsonResultArray);
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
                    createDynamicSpinner(title, type, dynamicFormEntity.getFormValueArrayList());
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

        AndroidUtils.showErrorLog(context, "+++++++++++customCheckList.getTag(" + title + ")++++++++++++", null == customCheckList.findViewWithTag(title));
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

                validateFields();
                AndroidUtils.showErrorLog(context, "isAllFieldsSet............." + isAllFieldsSet);
                if (isAllFieldsSet) {
                    callAddProductWebservice();
                }
            }
        });


    }

    private void validateFields() {

        isAllFieldsSet = true;

        if (productImagesDatas.size() <= 1) {

            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Upload/Capture at least one Image.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............productImagesDatas" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (!Validation.isNumber(companyId)) {
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select Company/Shop.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............companyId" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etproductname.getText().toString())) {
            AndroidUtils.showErrorLog(context, companyId + "**************************");
            etproductname.setError("Product Name can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Name can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etproductname" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etProductPrice.getText().toString())) {
            etProductPrice.setError("Product Price can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Price can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etProductPrice" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etProductPriceDiscount.getText().toString())) {
            etProductPriceDiscount.setError("Product Discount can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Discount can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etProductPriceDiscount" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (!Validation.isNumber(unitID)) {
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select Unit.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............unitID" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etDescription.getText().toString())) {
            etDescription.setError("Product Description can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Description can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etDescription" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (Validation.isEmptyStr(etProductWeight.getText().toString())) {
            etProductWeight.setError("Product Weight can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Weight can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etProductWeight" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (Validation.isEmptyStr(etMaxorderQuantity.getText().toString())) {
            etMaxorderQuantity.setError("Product Quantity can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Quantity can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etMaxorderQuantity" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (!ConnetivityCheck.isNetworkAvailable(context)) {
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "No Internet Connection available.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............isNetworkAvailable" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else {
            dynamicFormData = getDynamicSelectedData();
            if (Validation.isEmptyStr(dynamicFormData)) {
                AndroidUtils.showErrorLog(context, "isAllFieldsSet.............dynamicFormData" + isAllFieldsSet);
                isAllFieldsSet = false;
            }
        }


    }


    private String getDynamicSelectedData() {

        if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {

            JSONArray jsonArray = new JSONArray();
            for (DynamicFormEntity dynamicFormEntity : dynamicFormEntityArrayList) {
                String title = dynamicFormEntity.getLabel();
                String type = dynamicFormEntity.getType();
                JSONObject jsonObject = new JSONObject();
                if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("dropdown")) {
                    Spinner spinner = (Spinner) llSellerProductDetailContainer.findViewWithTag(title);
                    AndroidUtils.showErrorLog(context, "*(((((((((((((((((Spinner)))))))))*", dynamicFormEntity.getFormValueArrayList().get(spinner.getSelectedItemPosition()));


                    if (spinner.getSelectedItemPosition() < 1) {
                        AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select " + title + ".");
                        AndroidUtils.showErrorLog(context, "isAllFieldsSet.............dropdown" + isAllFieldsSet);
                        isAllFieldsSet = false;
                    }


                    try {
                        jsonObject.put(title, dynamicFormEntity.getFormValueArrayList().get(spinner.getSelectedItemPosition()).getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("checkbox")) {
                    CustomCheckList customCheckList = (CustomCheckList) llSellerProductDetailContainer.findViewWithTag(title);
                    AndroidUtils.showErrorLog(context, "+++++++++++customCheckList.getTag(" + title + ")2++++++++++++", null == customCheckList.findViewWithTag(title));

                    AndroidUtils.showErrorLog(context, "*(((((((((((((((((CheckList)))))))))*", customCheckList.getSelectedCheckList());

                    JSONArray jsonArray1 = new JSONArray();
                    for (FormValue formValue : customCheckList.getSelectedCheckList()) {
                        jsonArray1.put(formValue.getValue());
                    }

                    if (customCheckList.getSelectedCheckList() != null && customCheckList.getSelectedCheckList().size() == 0) {
                        AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select at least one " + title + ".");
                        AndroidUtils.showErrorLog(context, "isAllFieldsSet.............CheckList" + isAllFieldsSet);
                        isAllFieldsSet = false;
                    }


                    try {
                        jsonObject.put(title, jsonArray1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("radio")) {
                    CustomCheckList customCheckList = (CustomCheckList) llSellerProductDetailContainer.findViewWithTag(title);
                    AndroidUtils.showErrorLog(context, "*(((((((((((((((((RadioGroup)))))))))*", customCheckList.getSelectedCheckList());

                    JSONArray jsonArray1 = new JSONArray();
                    for (FormValue formValue : customCheckList.getSelectedCheckList()) {
                        jsonArray1.put(formValue.getValue());
                    }

                    if (customCheckList.getSelectedCheckList() != null && customCheckList.getSelectedCheckList().size() == 0) {
                        AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select " + title + ".");
                        AndroidUtils.showErrorLog(context, "isAllFieldsSet.............radio" + isAllFieldsSet);
                        isAllFieldsSet = false;
                    }

                    try {
                        jsonObject.put(title, customCheckList.getSelectedCheckList().get(0).getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("number") || type.equalsIgnoreCase("textarea")) {
                    EditText editText = (EditText) llSellerProductDetailContainer.findViewWithTag(title);

                    AndroidUtils.showErrorLog(context, "*(((((((((((((((((Edit Text)))))))))*", editText.getText());

                    if (Validation.isEmptyStr(editText.getText().toString())) {
                        editText.setError(title + " can not be empty.");
                        AndroidUtils.showSnackBar(llSellerProductDetailContainer, title + " can not be empty.");
                        AndroidUtils.showErrorLog(context, "isAllFieldsSet.............editText.getText().toString()" + isAllFieldsSet);
                        isAllFieldsSet = false;

                    }
                    try {
                        jsonObject.put(title, editText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dynamic_data", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidUtils.showErrorLog(context, jsonObject);
            return jsonObject.toString();
        }


        return null;
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

    private void callAddProductWebservice() {
        progressBarHandler.show();

        AndroidUtils.showErrorLog(context, "callAddProductWebservice----------called");

        Ion.with(AddProductActivity.this)
                .load(getString(R.string.webservice_base_url) + "/add_product")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .addMultipartParts(submitImages())
                .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setMultipartParameter("name", etproductname.getText().toString())
                .setMultipartParameter("shop_id", companyId)
                .setMultipartParameter("price", etProductPrice.getText().toString())
                .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                .setMultipartParameter("unit_id", unitID)
                .setMultipartParameter("short_des", etDescription.getText().toString())
                .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                .setMultipartParameter("weight", etProductWeight.getText().toString())
                .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                .setMultipartParameter("dynamic", dynamicFormData)

                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                AndroidUtils.showErrorLog(context, "result----------" + context.getClass().getSimpleName() + "------"+result);

                progressBarHandler.hide();
                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        AndroidUtils.showToast(context, result.get("message").getAsString());
                        if(Validation.containsIgnoreCase(result.get("message").getAsString(), "Added") || Validation.containsIgnoreCase(result.get("message").getAsString(), "Successfully")){
                            onBackPressed();
                        }
                    }
                } else {
                    AndroidUtils.showErrorLog(context, "hello2", e.toString());
                }
            }
        });
    }


    private void getUnit() {
        unitList.clear();
        progressBarHandler.show();
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

                            unitList.add(new City("-1", "Please Select Unit"));

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
                                    AndroidUtils.showErrorLog(context, "Unit id is : ", unitID);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            AndroidUtils.showToast(context, "! Invalid Unit");
                        }
                    }

                });
    }


}
