package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.List;


public class ProductImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductMediaData> itemList;
    private Context context;
    private final int userAdded = 0, image = 1;
    private Activity activity;


    public ProductImagesAdapter(Context context, List<ProductMediaData> itemList, Activity activity) {
        this.itemList = itemList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AndroidUtils.showErrorLog(context, "viewType---------------" + viewType);
        switch (viewType) {
            case userAdded:
                View v1 = inflater.inflate(R.layout.row_user_added, parent, false);
                viewHolder = new ProductUserHolder(v1);
                break;
            case image:
                View v2 = inflater.inflate(R.layout.row_product_images, parent, false);
                viewHolder = new ProductMediaHolder(v2);
                break;

        }
        return viewHolder;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AndroidUtils.showErrorLog(context, "Hi holder.getItemViewType() "+ holder.getItemViewType());

        switch (holder.getItemViewType())
        {
            case userAdded:
                final ProductUserHolder homeHolder_User = (ProductUserHolder) holder;
                homeHolder_User.relativeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AndroidUtils.showErrorLog(context, "Hi " + context.getClass().getName() + " 2nd hi");
                        if (activity instanceof AddProductActivity) {
                            ((AddProductActivity) activity).picPhoto();
                        } else if (activity instanceof AddCompanyShopActivity) {
                            ((AddCompanyShopActivity) activity).picPhoto();
                        }
                    }
                });
                break;


            case image:
                final ProductMediaHolder homeHolder = (ProductMediaHolder) holder;
                AndroidUtils.showErrorLog(context, "itemimage", itemList.get(position).imagePath);

                if (itemList.get(position).isVideo)
                {
                    File imgFile = new File(itemList.get(position).videoThumbnail);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
                        homeHolder.previewImage.setImageDrawable(drawable);
                    }

                }
                else
                    {

                    if (Validation.isEmptyStr(itemList.get(position).imagePath)) {
                        Ion.with(context)
                                .load(itemList.get(position).imageUrl)
                                .withBitmap().asBitmap()
                                .setCallback(new FutureCallback<Bitmap>() {
                                    @Override
                                    public void onCompleted(Exception e, Bitmap result) {
                                        if (result != null)
                                            homeHolder.previewImage.setImageBitmap(result);
                                    }
                                });
                    } else {
                        File imgFile = new File(itemList.get(position).imagePath);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
                            homeHolder.previewImage.setImageDrawable(drawable);
                        }
                    }

                }




                homeHolder.cancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                AndroidUtils.showErrorLog(context, "data-----------" + itemList);


                break;

        }


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public int getItemViewType(int position) {

        AndroidUtils.showErrorLog(context, "itemlist----------------" + itemList.get(position).imagePath);

        if (itemList.get(position).imagePath.equals("first")) {
            return userAdded;
        } else {
            return image;
        }

    }


}
