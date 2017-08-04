package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.BuildConfig;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop.EditCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.editproduct.EditProductActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class ProductImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductMediaData> itemList;
    private Context context;
    private final int userAdded = 0, image = 1;
    private Activity activity;
    public static CommonInterface commonInterface;


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
        AndroidUtils.showErrorLog(context, "Hi holder.getItemViewType() " + holder.getItemViewType());

        switch (holder.getItemViewType()) {
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
                        } else if (activity instanceof EditCompanyShopActivity) {
                            ((EditCompanyShopActivity) activity).picPhoto();
                        } else if (activity instanceof EditProductActivity) {
                            ((EditProductActivity) activity).picPhoto();
                        }

                    }
                });
                break;

            case image:
                final ProductMediaHolder homeHolder = (ProductMediaHolder) holder;
                AndroidUtils.showErrorLog(context, "itemimage", itemList.get(position).imagePath);

                if (itemList.get(position).isVideo) {
                    homeHolder.playImage.setVisibility(View.VISIBLE);
                    if (itemList.get(position).videoFile == null) {
                        AndroidUtils.showErrorLog(context, "itemimage", itemList.get(position).videoThumbnail);

                        Picasso.with(context).load(itemList.get(position).videoThumbnail)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into(homeHolder.previewImage);
                    } else {
                        File imgFile = new File(itemList.get(position).videoThumbnail);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
                            homeHolder.previewImage.setImageDrawable(drawable);
                        }
                    }
                } else {

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
                        if (activity instanceof EditCompanyShopActivity) {
                            EditCompanyShopActivity.productMediaDatasDelete.add(itemList.get(position));
                            itemList.remove(position);
                            notifyDataSetChanged();
                        } else if (activity instanceof EditProductActivity) {
                            EditProductActivity.productMediaDatasDelete.add(itemList.get(position));
                            itemList.remove(position);
                            notifyDataSetChanged();
                        }

                    }
                });
                AndroidUtils.showErrorLog(context, "data-----------" + itemList);

                homeHolder.cardImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemList.get(position).isVideo) {
                            File file = itemList.get(position).videoFile;
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (itemList.get(position).videoFile == null) {
                                intent.setDataAndType(/*itemList.get(position).videoThumbnail.replace("png", "mp4")*/FileProvider.getUriForFile(context, "com.aapkatrade.buyer.provider",file), "video/*");
                            } else {

                                intent.setDataAndType(/*Uri.fromFile(file)*/FileProvider.getUriForFile(context, "com.aapkatrade.buyer.provider",file), "video/*");
                            }
                            context.startActivity(intent);
                        } else {
                            File file = new File(itemList.get(position).imagePath);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            Uri data = Uri.parse("file://" + file.getAbsolutePath());
                            Uri data = FileProvider.getUriForFile(context,
                                    "com.aapkatrade.buyer.provider",
                                    file);
                            intent.setDataAndType(data, "image");
                            context.startActivity(intent);


                        }
                    }
                });

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

        if (itemList.get(position).imagePath != null && itemList.get(position).imagePath.equals("first")) {
            return userAdded;
        } else {
            return image;
        }
    }


}
