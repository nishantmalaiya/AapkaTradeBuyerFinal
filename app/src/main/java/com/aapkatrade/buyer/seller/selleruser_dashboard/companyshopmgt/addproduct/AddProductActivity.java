package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ImageUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.location.GeoCoderAddress;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity
{

    Context context;
    private TextView btnUpload;
    private int count = -1;
    private EditText etProductName, etDeliverLocation, etPrice, etCrossedPrice, etDescription, etDiscount, etarea_location, etpincode, etaddress;
    ImageView uploadButton;
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
    private GeoCoderAddress GeocoderAsync;
    private int current_state_index;
    private int step1FieldsSet=-1;
    RelativeLayout relativeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        context = AddProductActivity.this;

        relativeImage = (RelativeLayout) findViewById(R.id.relativeImage);

        relativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhoto();
            }
        });

        setUpToolBar();


        setupRecyclerView();

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ProductImagesAdapter(AddProductActivity.this, productImagesDatas);
        layoutManager = new LinearLayoutManager(AddProductActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);

        productImagesDatas.add(new ProductImagesData(docFile.getAbsolutePath(), ""));

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
    protected void onResume() {
        super.onResume();
      //  spService_type.setSelection(0);
        AndroidUtils.showErrorLog(context, "onResume");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        multiple_images = new ArrayList<>();
        Log.e("hi", "requestCode : " + requestCode + "result code : " + resultCode);
        try {
            if (requestCode == 11) {
                if (data.getClipData() != null) {

                    data.getClipData().getItemCount();

                    for (int k = 0; k < 4; k++) {

                        Uri selectedImage = data.getClipData().getItemAt(k).getUri();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        multiple_images.add(bitmap);


                        Log.e("doc", "***START.****** ");
                        if (ImageUtils.sizeOf(bitmap) > 2048) {
                            Log.e("doc", "if doc file path 1");

                            docFile = getFile(ImageUtils.resize(bitmap, bitmap.getHeight() / 2, bitmap.getWidth() / 2));
                            Log.e("doc", "if doc file path" + docFile.getAbsolutePath());
                        } else {

                            Log.e("doc", " else doc file path 1");
                            docFile = getFile(bitmap);
                            Log.e("doc", " else doc file path" + docFile.getAbsolutePath());
                        }

                        productImagesDatas.add(new ProductImagesData(docFile.getAbsolutePath(), ""));
                        Log.e("docfile", docFile.getAbsolutePath());


                        adapter.notifyDataSetChanged();
                        if (productImagesDatas.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);

                        }


                    }

                } else {


                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Uri tempUri = getImageUri(AddProductActivity.this, bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        productImagesDatas.add(new ProductImagesData(finalFile.getAbsolutePath(), ""));
                        Log.e("docfile", finalFile.getAbsolutePath());

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

                Log.e("docfile10", "Sachin sdnsdfjsd fsdjfsd fnmsdabf");

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(AddProductActivity.this, photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                productImagesDatas.add(new ProductImagesData(finalFile.getAbsolutePath(), ""));
                Log.e("docfile", finalFile.getAbsolutePath());

                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }




        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }


    private File getFile(Bitmap photo) {
        Uri tempUri = null;
        if (photo != null) {
            tempUri = getImageUri(AddProductActivity.this, photo);
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


    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = null;
        int idx = 0;
        if (uri != null) {
            cursor = AddProductActivity.this.getContentResolver().query(uri, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        }
        return cursor.getString(idx);
    }



}
