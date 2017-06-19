package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

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
import com.aapkatrade.buyer.R;
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


    public ProductImagesAdapter(Context context, List<ProductImagesData> itemList)
    {

        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = inflater.inflate(R.layout.row_product_images, parent, false);
        viewHolder = new ProductImagesHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final ProductImagesHolder homeHolder = (ProductImagesHolder) holder;

        Log.e("itemimage", itemList.get(position).image_path);
        if (itemList.get(position).image_path.equals(""))
        {
            Ion.with(context)
                    .load(itemList.get(position).image_url)
                    .withBitmap().asBitmap()
                    .setCallback(new FutureCallback<Bitmap>()
                    {
                        @Override
                        public void onCompleted(Exception e, Bitmap result)
                        {
                            if (result != null)
                                homeHolder.previewImage.setImageBitmap(result);
                        }
                    });

        }
        else
        {

            File imgFile = new File(itemList.get(position).image_path);

            if(imgFile.exists())
            {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
                homeHolder.previewImage.setImageDrawable(drawable);
            }


           /* Ion.with(context)
                    .load(itemList.get(position).image_path)
                    .withBitmap().asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (result != null)
                                homeHolder.previewImage.setImageBitmap(result);
                        }
                    });*/



        }
        //Ion.with(homeHolder.previewImage).load(itemList.get(position).image_path);


        viewHolder.cancelImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                itemList.remove(position);
                notifyDataSetChanged();
                //notifyItemRemoved(position);

            }
        });
        System.out.println("data-----------" + itemList);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
        //return itemList.size();
    }





}