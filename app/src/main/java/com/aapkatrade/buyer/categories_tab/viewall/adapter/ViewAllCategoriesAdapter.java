package com.aapkatrade.buyer.categories_tab.viewall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.CategoriesListHolder;
import com.aapkatrade.buyer.categories_tab.ShopListByCategoryActivity;
import com.aapkatrade.buyer.categories_tab.viewall.ViewAllCategoryActivity;
import com.aapkatrade.buyer.categories_tab.viewall.holder.GridViewHolder;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.CommonHolder_listProduct;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.home.CommonHolder;
import com.aapkatrade.buyer.home.CommonHolderGrid;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.location.Mylocation;
import com.aapkatrade.buyer.map.GoogleMapActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.aapkatrade.buyer.shopdetail.productdetail.ProductDetailActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by PPC15 on 8/22/2017.
 */

public class ViewAllCategoriesAdapter extends RecyclerView.Adapter<GridViewHolder> {
    private Context context;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private Mylocation mylocation;
   private AppSharedPreference appSharedPreference;

    public ViewAllCategoriesAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;

        appSharedPreference = new AppSharedPreference(context);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AndroidUtils.showErrorLog(context, "---------------->>>>>", ViewAllCategoryActivity.gridLayoutManager.getSpanCount());
        return ViewAllCategoryActivity.gridLayoutManager.getSpanCount() == 1 ? new GridViewHolder(LayoutInflater.from(context).inflate(R.layout.row_category_list, parent, false)) : new GridViewHolder(LayoutInflater.from(context).inflate(R.layout.row_category_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, final int position) {
        Ion.with(holder.imageView)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .load(categoryArrayList.get(position).getCategoryImage());

        AndroidUtils.showErrorLog(context,"imageurl"+categoryArrayList.get(position).getCategoryImage());

        holder.textView.setText(categoryArrayList.get(position).getCategoryName());

        holder.gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean permission_status = CheckPermission.checkPermissions((Activity) context);

                if (permission_status) {
                    mylocation = new Mylocation(context);
                    LocationManagerCheck locationManagerCheck = new LocationManagerCheck(context);
                    if (locationManagerCheck.isLocationServiceAvailable()) {
                        String currentLatitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), "0.0");
                        String currentLongitude = appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), "0.0");
                        appSharedPreference.getSharedPref(SharedPreferenceConstants.CURRENT_STATE_NAME.toString(), "Haryana");

                        Intent i = new Intent(context, ShopListByCategoryActivity.class);
                        i.putExtra("category_id", categoryArrayList.get(position).getCategoryId());
                        i.putExtra("latitude", currentLatitude);
                        i.putExtra("longitude", currentLongitude);
                        context.startActivity(i);
                    } else {
                        locationManagerCheck.createLocationServiceError((Activity) context);
                    }

                } else {
                    AndroidUtils.showErrorLog(context, "error in permission");
                }

            }


        });

        // AndroidUtils.setGradientColor(holder.gridLayout, GradientDrawable.RECTANGLE, R.color.red_light, R.color.green_gradient2, GradientDrawable.Orientation.BOTTOM_TOP, 0);
    }


    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }
}
