package com.aapkatrade.buyer.categories_tab;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.map.GoogleMapActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class CategoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<CategoriesListData> itemList;
    public Activity context;
    private CategoriesListHolder viewHolder;
    private ProgressBarHandler progressBarHandler;


    public CategoriesListAdapter(Activity context, List<CategoriesListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_list_item2, parent, false);

        viewHolder = new CategoriesListHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CategoriesListHolder homeHolder = (CategoriesListHolder) holder;

        homeHolder.tvProductName.setText(itemList.get(position).shopName);


        if (Tabletsize.isTablet(context)) {
            String product_imageurl = itemList.get(position).shopImage.replace("small", "large");

            Ion.with(homeHolder.productimage)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            Log.e("image_large", "image_large"+product_imageurl);

        } else if (Tabletsize.isMedium(context)) {
            String product_imageurl = itemList.get(position).shopImage == null ? "" : itemList.get(position).shopImage.replace("small", "medium");

            Ion.with(homeHolder.productimage)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            Log.e("image_medium", "image_medium" + product_imageurl);

        } else if (Tabletsize.isSmall(context)) {
            String product_imageurl = itemList.get(position).shopImage.replace("small", "medium");

            Ion.with(homeHolder.productimage)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);

            Log.e("image_small", "image_small");
        }

        Log.e("distance", "distance"+itemList.get(position).shopImage);
        Picasso.with(context).load(itemList.get(position).shopImage)
                .placeholder(R.drawable.default_noimage)
                .error(R.drawable.default_noimage)
                .into(homeHolder.productimage);

        homeHolder.distance.setText(itemList.get(position).distance);

        homeHolder.tvProductDestination.setText(itemList.get(position).shopLocation);

        homeHolder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidUtils.showErrorLog(context, "product_id", itemList.get(position).shopId);
                Intent intent = new Intent(context, ShopDetailActivity.class);
                intent.putExtra("product_id", itemList.get(position).shopId);
                intent.putExtra("product_location", itemList.get(position).shopLocation);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });


        homeHolder.linearlayoutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean permission_status = CheckPermission.checkPermissions(context);


                if (permission_status)
                {

                    LocationManagerCheck locationManagerCheck = new LocationManagerCheck(
                            context);
                    Location location = null;
                    if (locationManagerCheck.isLocationServiceAvailable()) {

                        if (Looper.myLooper() == null) {
                            Looper.prepare();

                        }
                        Log.e("time_taken 1", (System.currentTimeMillis() / 1000) + "");
                        progressBarHandler.show();
                        Intent intent = new Intent(context, GoogleMapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("product_location", itemList.get(position).shopLocation);
                        context.startActivity(intent);

                        progressBarHandler.hide();
                        Log.e("time_taken 2", (System.currentTimeMillis() / 1000) + "");

                    } else {
                        locationManagerCheck.createLocationServiceError(context);
                    }

                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
