package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;

import static android.R.attr.bitmap;


public class ProductImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final LayoutInflater inflater;
    private List<ProductImagesData> itemList;
    private Context context;
    private ProductImagesHolder viewHolder;
    Bitmap imageForPreview;
    private final int USER_Added = 0,IMAGE = 1;
    AddProductActivity addproductActivity = new AddProductActivity();



    public ProductImagesAdapter(Context context, List<ProductImagesData> itemList,AddProductActivity addproductActivity)

    {

        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.addproductActivity = addproductActivity;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        System.out.println("viewType---------------"+viewType);

        switch (viewType) {
            case USER_Added:
                View v1 = inflater.inflate(R.layout.row_user_added, parent, false);
                viewHolder = new ProductUserHolder(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.row_product_images, parent, false);
                viewHolder = new ProductImagesHolder(v2);
                break;
          
        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        switch (holder.getItemViewType())
        {
            case USER_Added:

                final ProductUserHolder homeHolder_User = (ProductUserHolder) holder;

                homeHolder_User.relativeImage.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        Toast.makeText(context, "Hi "+context.getClass().getSimpleName()+" 2nd hi", Toast.LENGTH_SHORT).show();

                        addproductActivity.picPhoto();


                     
                    }



                });

                break;

            case IMAGE:
                final ProductImagesHolder homeHolder = (ProductImagesHolder) holder;

                Log.e("itemimage", itemList.get(position).image_path);
                if (itemList.get(position).image_path.equals(""))
                {
                    Ion.with(context)
                            .load(itemList.get(position).image_url)
                            .withBitmap().asBitmap()
                            .setCallback(new FutureCallback<Bitmap>() {
                                @Override
                                public void onCompleted(Exception e, Bitmap result) {
                                    if (result != null)
                                        homeHolder.previewImage.setImageBitmap(result);
                                }
                            });

                }
                else
                {
                    File imgFile = new File(itemList.get(position).image_path);

                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
                        homeHolder.previewImage.setImageDrawable(drawable);
                    }
                }

                homeHolder.cancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.remove(position);
                        notifyDataSetChanged();
                        //notifyItemRemoved(position);
                    }
                });
                System.out.println("data-----------" + itemList);


                break;

        }



    }


    @Override
    public int getItemCount() {
        return itemList.size();
        //return itemList.size();
    }


    @Override
    public int getItemViewType(int position)
    {

        System.out.println("itemlist----------------"+itemList.get(position).image_path.toString());

        if (itemList.get(position).image_path.toString().equals("first"))
        {
            return USER_Added;
        }
        else
        {
            return IMAGE;
        }

    }




}