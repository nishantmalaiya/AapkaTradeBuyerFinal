package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.editproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.home.HomeActivity;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EditProductActivity extends AppCompatActivity
{

    private File docFile = new File("");
    private ArrayList<ProductMediaData> productImagesDataArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductImagesAdapter adapter;
    private ArrayList<Bitmap> multiple_images;
    private EditText etProductName, etProductPrice, etProductPriceDiscount, etProductWeight, etDescription, etMaxorderQuantity, etProductLength, etProductWidth, etProductHeight;
    private TextView save;
    private List<Part> files = new ArrayList();
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressBarHandler;
    private Context context;
    private Spinner spUnitCategory;
    private PagingSpinner pagingSpinner;
    private ArrayList<City> unitList = new ArrayList<>();
    private String unitID, dynamicFormData, productId = "0", productName, price, discount, shortDescription, maxOrderQuantity, weight, width, height, length;
    private ArrayList<DynamicFormEntity> dynamicFormEntityArrayList = new ArrayList<>();
    private LinearLayout llSellerProductDetailContainer;
    public static ArrayList<ProductMediaData> productMediaDatasDelete = new ArrayList<>();
    private ArrayList<KeyValue> imageUrlList = new ArrayList<>();
    private ArrayList<Part> submitImgList = new ArrayList<>();
    private ArrayList<String> submitImgDelList = new ArrayList<>();

    public boolean isFragment = false;
    private boolean isAllFieldsSet = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);
        context = EditProductActivity.this;
        appSharedpreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);

        productMediaDatasDelete.clear();

        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId") == null ? "0" : getIntent().getStringExtra("productId");
        }
        setUpToolBar();
        initView();
        setupRecyclerView();
        loadUnitWebService();
    }



    @Override
    public void onBackPressed() {
        if (!isFragment) {
            super.onBackPressed();
        } else {
            isFragment = false;
            getSupportFragmentManager().popBackStack();
        }
    }

    private void loadProductDataWebService() {
        AndroidUtils.showErrorLog(context, "     productId     ", productId);
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url).concat("/edit_product_data"))
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id", productId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "result loadProductDataWebService -----> ", result);
                        progressBarHandler.hide();
                        if (result != null) {
                            if (result.get("error").getAsString().contains("false")) {
                                JsonObject jsonObject = result.get("result").getAsJsonObject();
                                productName = jsonObject.get("name").getAsString();
                                price = jsonObject.get("price").getAsString();
                                discount = jsonObject.get("discount").getAsString();
                                unitID = jsonObject.get("unit_id").getAsString();
                                shortDescription = jsonObject.get("short_des").getAsString();
                                maxOrderQuantity = jsonObject.get("max_order_qty").getAsString();
                                weight = jsonObject.get("weight").getAsString();
                                width = jsonObject.get("width").getAsString();
                                height = jsonObject.get("height").getAsString();
                                length = jsonObject.get("length").getAsString();

                                etProductName.setText(productName);
                                etProductPrice.setText(price);
                                etProductPriceDiscount.setText(discount);
                                etMaxorderQuantity.setText(maxOrderQuantity);
                                etProductWeight.setText(weight);
                                etProductHeight.setText(height);
                                etProductWidth.setText(width);
                                etProductLength.setText(length);
                                etDescription.setText(shortDescription);
                                if (Validation.isNumber(unitID) && unitList.size() >= (Integer.valueOf(unitID) - 1)) {
                                    spUnitCategory.setSelection(Integer.valueOf(unitID));
                                }

                                JsonArray imageJsonArray = jsonObject.get("images").getAsJsonArray();


                                for (int i = 0; i < imageJsonArray.size(); i++) {
                                    JsonObject imageObject = imageJsonArray.get(i).getAsJsonObject();
                                    String imageId = imageObject.get("id").getAsString();
                                    String imageUrl = imageObject.get("image_url").getAsString();
                                    if (Validation.isNonEmptyStr(imageUrl)) {
                                        AndroidUtils.showErrorLog(context, "imagepathurl---------------------" + imageUrl);
                                        imageUrlList.add(new KeyValue(imageId, imageUrl));
                                    }
                                }

                                if (imageUrlList.size() > 0) {
                                    for (int i = 0; i < imageUrlList.size(); i++) {
                                        AndroidUtils.showErrorLog(context, "downloadImage---------------------" + imageUrlList.get(i));
                                        downloadImage(i);
                                    }
                                }


                                JsonArray jsonResultArray = jsonObject.get("dynamic").getAsJsonArray();
                                if (jsonResultArray != null && jsonResultArray.size() > 0) {

                                    for (int i = 0; i < jsonResultArray.size(); i++) {
                                        JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
                                        dynamicFormEntity.setMultiple(false);
                                        dynamicFormEntity.setName(jsonObject1.get("name").getAsString());
                                        dynamicFormEntity.setLabel(jsonObject1.get("label").getAsString());
                                        dynamicFormEntity.setType(jsonObject1.get("type").getAsString());
                                        if (jsonObject1.get("is_multiple").getAsString().contains("true")) {
                                            dynamicFormEntity.setMultiple(true);
                                            if (jsonObject1.get("value") != null) {
                                                JsonArray multipleValueArray = jsonObject1.get("value").getAsJsonArray();
                                                if (multipleValueArray != null && multipleValueArray.size() > 0) {
                                                    for (int j = 0; j < multipleValueArray.size(); j++) {
                                                        JsonObject jsonObject2 = (JsonObject) multipleValueArray.get(j);
                                                        dynamicFormEntity.addToFormValueArrayList(new FormValue(jsonObject2.get("name").getAsString(), jsonObject2.get("value").getAsString(), jsonObject2.get("checked").getAsBoolean()));
                                                    }
                                                }
                                            }
                                        } else {

                                            dynamicFormEntity.setValue(jsonObject1.get("value").getAsString());
                                            dynamicFormEntity.addToFormValueArrayList(new FormValue(jsonObject1.get("label") == null ? "" : jsonObject1.get("label").getAsString(), jsonObject1.get("value") == null ? "" : jsonObject1.get("value").getAsString()));
                                        }
                                        dynamicFormEntityArrayList.add(dynamicFormEntity);
                                    }
                                    AndroidUtils.showErrorLog(context, "dynamicFormEntityArrayList size : " + dynamicFormEntityArrayList.size(), dynamicFormEntityArrayList.toString());

                                } else {
                                    AndroidUtils.showErrorLog(context, "jsonResultArray is null or jsonResultArray.size == 0");
                                }
                                createDynamicForm();


                            } else {
                                AndroidUtils.showErrorLog(context, "loadProductDataWebService -----> error -> true");
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

        productImagesDataArrayList.add(new ProductMediaData(pictureFile.getAbsolutePath(), "", null, ""));
        adapter.notifyDataSetChanged();
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

    private File getOutputMediaFile(int index) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        String mImageName = getFileName(imageUrlList.get(index).value.toString());
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


    private void createDynamicForm() {
        if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {
            for (DynamicFormEntity dynamicFormEntity : dynamicFormEntityArrayList) {
                String title = dynamicFormEntity.getName();
                String type = dynamicFormEntity.getType();
                if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("dropdown")) {
                    createDynamicSpinner(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("checkbox")) {
                    createDynamicCheckList(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (dynamicFormEntity.isMultiple() && type.equalsIgnoreCase("radio")) {
                    createDynamicRadioGroup(title, type, dynamicFormEntity.getFormValueArrayList());
                } else if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("number") || type.equalsIgnoreCase("textarea")) {
                    createDynamicEditText(title, type, dynamicFormEntity.getValue());
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
            for(int i = 0; i < formValueArrayList.size(); i++){
                if(formValueArrayList.get(i).isSelected()){
                    spinner.setSelection(i);
                }
            }
        }

        llSellerProductDetailContainer.addView(view);

    }

    private void createDynamicEditText(String title, String type, String value) {
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
        if(Validation.isNonEmptyStr(value)){
            editText.setText(value);
        }
        llSellerProductDetailContainer.addView(view);
    }

    private void initView() {
        spUnitCategory = (Spinner) findViewById(R.id.spUnitCategory);
        etProductName = (EditText) findViewById(R.id.etproductname);
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
                submitImgList.clear();
                if(submitImgDelList!=null)
                submitImgDelList.clear();
                submitImgList = submitImages();
                submitImgDelList = submitDeletedImages();

                AndroidUtils.showErrorLog(context, "productImagesDataArrayList............." + submitImgList.size());
                AndroidUtils.showErrorLog(context, "productMediaDatasDeleteList.............", submitImgDelList == null ? "no delter list" : submitImgDelList.toString());
                AndroidUtils.showErrorLog(context, "getDynamicSelectedData............." + dynamicFormData);

                validateFields();
                AndroidUtils.showErrorLog(context, "isAllFieldsSet............****>>>>." + isAllFieldsSet);
                if(isAllFieldsSet){
                    hitUpdateProductWebservice();
                }
            }
        });

        pagingSpinner = (PagingSpinner) findViewById(R.id.pagingSpinner);
        pagingSpinner.setShopType(1);
        pagingSpinner.setSellerId(appSharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()));
        pagingSpinner.setShopData(productId);

    }

    private void validateFields() {

        isAllFieldsSet = true;

        if (productImagesDataArrayList.size() <= 1) {

            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Upload/Capture at least one Image.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............productImagesDataArrayList" + isAllFieldsSet);
            isAllFieldsSet = false;

        } else if (!Validation.isNumber(pagingSpinner.getShopId()) && pagingSpinner.getShopId().equals("0")) {
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Please Select Company/Shop.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............companyId" + isAllFieldsSet);
            isAllFieldsSet = false;
        } else if (Validation.isEmptyStr(etProductName.getText().toString())) {
            AndroidUtils.showErrorLog(context, "productname**************************");
            etProductName.setError("Product Name can not be empty.");
            AndroidUtils.showSnackBar(llSellerProductDetailContainer, "Product Name can not be empty.");
            AndroidUtils.showErrorLog(context, "isAllFieldsSet.............etProductName" + isAllFieldsSet);
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
                if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {
                    isAllFieldsSet = false;

                } else {
                    isAllFieldsSet = true;
                }
            }
        }


    }


    private String getDynamicSelectedData() {

        if (dynamicFormEntityArrayList != null && dynamicFormEntityArrayList.size() > 0) {

            JSONArray jsonArray = new JSONArray();
            for (DynamicFormEntity dynamicFormEntity : dynamicFormEntityArrayList) {
                String title = dynamicFormEntity.getName();
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
                        if (customCheckList.getSelectedCheckList() != null && customCheckList.getSelectedCheckList().size() > 0) {
                            jsonObject.put(title, customCheckList.getSelectedCheckList().get(0).getValue());
                        }
                    } catch (ArrayIndexOutOfBoundsException | JSONException e) {
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

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        productImagesDataArrayList.add(new ProductMediaData("first", "", null, ""));
        adapter = new ProductImagesAdapter(context, productImagesDataArrayList, this);
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

                   /*     Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
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
                        }*/

                        File finalFile = new File(ImageUtils.getRealPathFromURI(context, data.getClipData().getItemAt(k).getUri()));

                        productImagesDataArrayList.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
                        AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                        adapter.notifyDataSetChanged();

                        if (productImagesDataArrayList.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    }

                } else {
                       /* InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Uri tempUri = ImageUtils.getImageUri(context, bitmap);
*/
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(ImageUtils.getRealPathFromURI(context, data.getData()));

                    productImagesDataArrayList.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));

                    AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                    adapter.notifyDataSetChanged();

                    if (productImagesDataArrayList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                }
            }
            if (requestCode == 10) {
                /*AndroidUtils.showErrorLog(context, "docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = ImageUtils.getImageUri(context, photo);
*/
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(ImageUtils.getRealPathFromURI(context, data.getData()));

                productImagesDataArrayList.add(new ProductMediaData(finalFile.getAbsolutePath(), "", null, ""));
                AndroidUtils.showErrorLog(context, "docfile", finalFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }


        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "Exception", e.toString());
        }

    }

    private ArrayList<String> submitDeletedImages() {
        ArrayList<String> files = new ArrayList<>();
        if (productMediaDatasDelete != null && productMediaDatasDelete.size() > 0) {
            for (int k = 0; k < productMediaDatasDelete.size(); k++) {
                ProductMediaData file = productMediaDatasDelete.get(k);
                if (!file.isVideo && isExistingImage(file) && savebitmap(file.imagePath)!=null) {
                    files.add(imageUrlList.get(getPositionOfExistingImage(file)).key.toString());
                }
            }
            return files;
        }
        return null;
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
        if (productImagesDataArrayList != null && productImagesDataArrayList.size() > 0) {
            for (int i = 1; i < productImagesDataArrayList.size(); i++) {
                ProductMediaData file = productImagesDataArrayList.get(i);
                if (!file.isVideo && !isExistingImage(file) && savebitmap(file.imagePath)!=null) {
                    files.add(new FilePart("image[]", savebitmap(file.imagePath)));
                    AndroidUtils.showErrorLog(context, "----------------submitImages--->", files.get(files.size()-1).getFilename());
                }
            }
            return files;
        }
        return null;
    }

    private boolean isExistingImage(ProductMediaData file) {
        for (int i = 0; i < imageUrlList.size(); i++) {
            AndroidUtils.showErrorLog(context, "------isExistingImage--imageUrlList.get(i)------->" + imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1] + "-------file.imagePath---------->" + file.imagePath.split("/")[file.imagePath.split("/").length - 1]);
            if (file.imagePath.split("/")[file.imagePath.split("/").length - 1].equals(imageUrlList.get(i).value.toString().split("/")[imageUrlList.get(i).value.toString().split("/").length - 1])) {
                return true;
            }
        }
        return false;
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


    /* private ArrayList<Part> getImagesData(String key, ArrayList<Part> partArrayList){
        ArrayList<Part> localPartArrayList = new ArrayList<>();
        if(partArrayList == null){
            localPartArrayList.add(new FilePart("image", savebitmap("[]")));
        }
        return localPartArrayList;
    }
*/
    private void hitUpdateProductWebservice() {
        progressBarHandler.show();
/*

        for(Part part:submitImgList)
        AndroidUtils.showErrorLog(context, "hitUpdateProductWebservice----------called",part.getFilename());




          if (submitImgList == null || submitImgList.size() == 0) {
            if (submitImgDelList == null || submitImgDelList.size() == 0) {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("image", "[]")
                        .setMultipartParameter("delimg", "[]")
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AndroidUtils.showErrorLog(context, "result--1--------" + context.getClass().getSimpleName() + "------" + result);

                    }
                });
            } else {

                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("image", "[]")
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AndroidUtils.showErrorLog(context, "result--2--------" + context.getClass().getSimpleName() + "------" + result);

                    }
                });
            }
        } else {



            if (submitImgDelList == null || submitImgDelList.size() == 0) {

                for(Part part:submitImgList) {
                    AndroidUtils.showErrorLog(context, "submitImgList-----------", part.getFilename());
                }

                AndroidUtils.showErrorLog(context,"submitImgList size-----------",submitImgList.size());

                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .addMultipartParts(submitImgList)
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("delimg", "[]")
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AndroidUtils.showErrorLog(context, "result--3--------" + context.getClass().getSimpleName() + "------" + result);

                    }
                });
            } else {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .addMultipartParts(submitImgList)
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AndroidUtils.showErrorLog(context, "result--4--------" + context.getClass().getSimpleName() + "------" + result);

                    }
                });
            }
        }






*/




























        if (submitImgList == null || submitImgList.size()==0) {
            if (submitImgDelList == null || submitImgDelList.size()==0) {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("image", "[]")
                        .setMultipartParameter("delimg", "[]")
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "result-----1-----" + context.getClass().getSimpleName() + "------" + result);

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
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("image", "[]")
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "result----2------" + context.getClass().getSimpleName() + "------" + result);

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
        } else {
            if (submitImgDelList == null || submitImgDelList.size()==0 ) {
                Ion.with(context)
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .setMultipartParameter("delimg", "[]")
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "result----3------" + context.getClass().getSimpleName() + "------" + result);

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
                        .load(getString(R.string.webservice_base_url).concat("/edit_product/").concat(productId))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .addMultipartParts(submitImages())
                        .setMultipartParameter("delimg", submitImgDelList.toString())
                        .setMultipartParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setMultipartParameter("name", etProductName.getText().toString())
                        .setMultipartParameter("shop_id", pagingSpinner.getShopId())
                        .setMultipartParameter("price", etProductPrice.getText().toString())
                        .setMultipartParameter("discount", etProductPriceDiscount.getText().toString())
                        .setMultipartParameter("unit_id", unitID)
                        .setMultipartParameter("short_des", etDescription.getText().toString())
                        .setMultipartParameter("max_order_qty", etMaxorderQuantity.getText().toString())
                        .setMultipartParameter("weight", etProductWeight.getText().toString())
                        .setMultipartParameter("length", etProductLength.getText() == null ? "0" : etProductLength.getText().toString())
                        .setMultipartParameter("width", etProductWidth.getText() == null ? "0" : etProductWidth.getText().toString())
                        .setMultipartParameter("height", etProductHeight.getText() == null ? "0" : etProductHeight.getText().toString())
                        .setMultipartParameter("dynamic", Validation.isEmptyStr(dynamicFormData) ? "[]" : dynamicFormData)
                        .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        AndroidUtils.showErrorLog(context, "result---4-------" + context.getClass().getSimpleName() + "------" + result);

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

    private void loadUnitWebService() {
        unitList.clear();
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url).concat("/").concat("dropdown"))
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

                        loadProductDataWebService();
                    }

                });
    }

}