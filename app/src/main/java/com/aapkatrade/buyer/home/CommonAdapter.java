package com.aapkatrade.buyer.home;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.categories_tab.CategoriesListHolder;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.CommonHolder_listProduct;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.map.GoogleMapActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.aapkatrade.buyer.shopdetail.productdetail.ProductDetailActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Shankit on 7/25/2016.
 */
public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public Context context;
    private ArrayList<CommonData> commonDatas;
    private String arrangementtype, categorytype;
    private View v;
    private String TAG;
    float initialX, initialY;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progressBarHandler;



    public CommonAdapter(Context context, ArrayList<CommonData> commonDatas, String arrangementtype, String categorytype)
    {
        this.context = context;
        this.commonDatas = commonDatas;
        this.arrangementtype = arrangementtype;
        this.categorytype = categorytype;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        TAG = context.getClass().getSimpleName();

        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);

        System.out.println("search_list----------"+arrangementtype);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        CommonHolder viewHolder1;
        CommonHolderGrid viewHolder2;
        CommonHolder_listProduct viewHolder_listProduct;
        CategoriesListHolder categoriesListHolder;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (arrangementtype == "list")
        {
            v = inflater.inflate(R.layout.row_dashboard, parent, false);
            viewHolder2 = new CommonHolderGrid(v);
            return viewHolder2;
        }
        else if (arrangementtype == "search_list")
        {

            System.out.println("search_list-------------");
            v = inflater.inflate(R.layout.product_list_item2, parent, false);
            categoriesListHolder = new CategoriesListHolder(v);
            return categoriesListHolder;

        }
        else if (arrangementtype == "list_product")
        {
            v = inflater.inflate(R.layout.row_dashboard_product_track_list, parent, false);

            viewHolder_listProduct = new CommonHolder_listProduct(v);
            return viewHolder_listProduct;
        }
        else if (arrangementtype == "OrderedProductList")
        {
            v = inflater.inflate(R.layout.activity_order, parent, false);
            viewHolder_listProduct = new CommonHolder_listProduct(v);
            return viewHolder_listProduct;
        }
        else
        {
            v = inflater.inflate(R.layout.row_dashboard_grid, parent, false);
            viewHolder1 = new CommonHolder(v);
            return viewHolder1;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position)
    {
        if (arrangementtype == "list")
        {

            final CommonHolder holder = new CommonHolder(v);

            if (Tabletsize.isTablet(context)) {
                Picasso.with(context)
                        .load(commonDatas.get(position).imageurl)
                        .error(R.drawable.banner)
                        .placeholder(R.drawable.default_noimage)
                        .error(R.drawable.default_noimage)
                        .into(holder.cimageview);
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "large");

                Ion.with(holder.cimageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_large", "image_large");

            } else if (Tabletsize.isMedium(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(holder.cimageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_medium", "image_medium" + product_imageurl);

            } else if (Tabletsize.isSmall(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(holder.cimageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);

                Log.e("image_small", "image_small");
            }


            Log.e("imageurl", commonDatas.get(0).imageurl);

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("time Adapter", String.valueOf(System.currentTimeMillis()));

                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra("product_id", commonDatas.get(position).id);
                    context.startActivity(intent);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

                }
            });
            holder.tvProductName.setText(commonDatas.get(position).name);

        }
        else if (arrangementtype == "search_list")
        {

            final CategoriesListHolder homeHolder = new CategoriesListHolder(v);

            homeHolder.tvProductName.setText(commonDatas.get(position).name);

            //System.out.println("tvProductCategoryname----------"+commonDatas.get(position).categoryName);

            homeHolder.tvProductCategoryname.setText(commonDatas.get(position).categoryName);

            homeHolder.tvProductDestination.setText(commonDatas.get(position).product_location);

            homeHolder.distance.setText(commonDatas.get(position).distance);

            if (Tabletsize.isTablet(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "large");

                Ion.with(homeHolder.productimage)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_large", "image_large"+product_imageurl);

            } else if (Tabletsize.isMedium(context)) {
                String product_imageurl = commonDatas.get(position).imageurl == null ? "" : commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(homeHolder.productimage)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_medium", "image_medium" + product_imageurl);

            } else if (Tabletsize.isSmall(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(homeHolder.productimage)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);

                Log.e("image_small", "image_small");
            }


            Picasso.with(context).load(commonDatas.get(position).imageurl)
                    .placeholder(R.drawable.default_noimage)
                    .error(R.drawable.default_noimage)
                    .into(homeHolder.productimage);


            homeHolder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AndroidUtils.showErrorLog(context, "product_id", commonDatas.get(position).id);
                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra("product_id", commonDatas.get(position).id);
                    intent.putExtra("product_location", commonDatas.get(position).product_location);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });


            homeHolder.linearlayoutMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    boolean permission_status = CheckPermission.checkPermissions((Activity) context);


                    if (permission_status)
                    {

                        LocationManagerCheck locationManagerCheck = new LocationManagerCheck(context);
                        Location location = null;
                        if (locationManagerCheck.isLocationServiceAvailable())
                        {

                            if (Looper.myLooper() == null)
                            {
                                Looper.prepare();

                            }
                            Log.e("time_taken 1", (System.currentTimeMillis() / 1000) + "");
                            progressBarHandler.show();
                            Intent intent = new Intent(context, GoogleMapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("product_location", commonDatas.get(position).shop_location);
                            context.startActivity(intent);

                            progressBarHandler.hide();
                            Log.e("time_taken 2", (System.currentTimeMillis() / 1000) + "");

                        } else {
                            locationManagerCheck.createLocationServiceError((Activity) context);
                        }

                    }

                }
            });


        }
        else if (arrangementtype == "list_product") {


            final CommonHolder_listProduct viewHolder_listProduct = new CommonHolder_listProduct(v);

            if (Tabletsize.isTablet(context)) {
                Picasso.with(context)
                        .load(commonDatas.get(position).imageurl)
                        .error(R.drawable.banner)
                        .placeholder(R.drawable.default_noimage)
                        .error(R.drawable.default_noimage)
                        .into(viewHolder_listProduct.product_imageview);
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "large");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_large", "image_large");

            } else if (Tabletsize.isMedium(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_medium", "image_medium" + product_imageurl);

            } else if (Tabletsize.isSmall(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);

                Log.e("image_small", "image_small");
            }


            Log.e("imageurl", commonDatas.get(0).imageurl);


            viewHolder_listProduct.tvProductName.setText(commonDatas.get(position).name);

            AndroidUtils.setBackgroundSolid(viewHolder_listProduct.rl_product_addtocart, context, R.color.white, 15, GradientDrawable.OVAL);
            AndroidUtils.setBackgroundSolid(viewHolder_listProduct.rl_product_description, context, R.color.white, 15, GradientDrawable.OVAL);


            AndroidUtils.setImageColor(viewHolder_listProduct.product_addcard, context, R.color.color_voilet);
            AndroidUtils.setImageColor(viewHolder_listProduct.product_description, context, R.color.color_voilet);


            viewHolder_listProduct.product_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String product_id = commonDatas.get(position).id;
                    String product_name = commonDatas.get(position).name;
                    String price = commonDatas.get(position).price;

                    callwebservice__add_tocart(product_id, "", product_name, price, "1");


                }
            });
            viewHolder_listProduct.product_addcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    AndroidUtils.showErrorLog(context, "___________PRODUCT ID--(((---------->"+ commonDatas.get(position).id);

                    intent.putExtra("productId", commonDatas.get(position).id);
                    intent.putExtra("product_id", commonDatas.get(position).id);
                    intent.putExtra("product_name", commonDatas.get(position).name);
                    intent.putExtra("product_price", commonDatas.get(position).price);
                    intent.putExtra("product_image", commonDatas.get(position).imageurl);
                    context.startActivity(intent);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

                }
            });


            viewHolder_listProduct.cardview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    viewHolder_listProduct.rl_cartview.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (viewHolder_listProduct.rl_cartview.getVisibility() == View.VISIBLE) {
                                viewHolder_listProduct.rl_cartview.setVisibility(View.GONE);
                            }
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                    return true;
                }
            });

        } else if (arrangementtype == "OrderedProductList") {

            CommonHolder_listProduct viewHolder_listProduct = new CommonHolder_listProduct(v);


            if (Tabletsize.isTablet(context)) {
                Picasso.with(context)
                        .load(commonDatas.get(position).imageurl)
                        .error(R.drawable.banner)
                        .placeholder(R.drawable.default_noimage)
                        .error(R.drawable.default_noimage)
                        .into(viewHolder_listProduct.product_imageview);
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "large");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_large", "image_large");

            } else if (Tabletsize.isMedium(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);
                Log.e("image_medium", "image_medium" + product_imageurl);

            } else if (Tabletsize.isSmall(context)) {
                String product_imageurl = commonDatas.get(position).imageurl.replace("small", "medium");

                Ion.with(viewHolder_listProduct.product_imageview)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(product_imageurl);

                Log.e("image_small", "image_small");
            }


            Log.e("imageurl", commonDatas.get(0).imageurl);


            viewHolder_listProduct.tvProductName.setText(commonDatas.get(position).name);

            AndroidUtils.setBackgroundSolid(viewHolder_listProduct.rl_product_addtocart, context, R.color.white, 15, GradientDrawable.OVAL);
            AndroidUtils.setBackgroundSolid(viewHolder_listProduct.rl_product_description, context, R.color.white, 15, GradientDrawable.OVAL);


            AndroidUtils.setImageColor(viewHolder_listProduct.product_addcard, context, R.color.color_voilet);
            AndroidUtils.setImageColor(viewHolder_listProduct.product_description, context, R.color.color_voilet);


            viewHolder_listProduct.product_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String product_id = commonDatas.get(position).id;
                    String product_name = commonDatas.get(position).name;
                    String price = commonDatas.get(position).price;

                    callwebservice__add_tocart(product_id, "", product_name, price, "1");


                }
            });
            viewHolder_listProduct.product_addcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product_id", commonDatas.get(position).id);
                    intent.putExtra("product_name", commonDatas.get(position).name);
                    intent.putExtra("product_price", commonDatas.get(position).price);
                    intent.putExtra("product_image", commonDatas.get(position).imageurl);
                    context.startActivity(intent);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

                }
            });

//
        } else
        {

            System.out.println("grid data-----------------"+ commonDatas.get(position).name);

            final CommonHolderGrid grid_holder = new CommonHolderGrid(v);

            Picasso.with(context).load(commonDatas.get(position).imageurl)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .into(grid_holder.product_imageview);

            grid_holder.tvProductName.setText(commonDatas.get(position).name);
            grid_holder.rl_grid_row_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    intent.putExtra("product_id", commonDatas.get(position).id.toString());
                    context.startActivity(intent);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });

            if (Tabletsize.isTablet(context)) {
                if (position % 3 == 0) {
                    grid_holder.view_grid_left.setVisibility(View.GONE);
                    grid_holder.view_grid_right.setVisibility(View.GONE);
                } else {
                    grid_holder.view_grid_left.setVisibility(View.VISIBLE);
                    grid_holder.view_grid_right.setVisibility(View.GONE);
                }

            } else {
                if (position % 2 == 0) {

                    grid_holder.view_grid_left.setVisibility(View.GONE);
                    grid_holder.view_grid_right.setVisibility(View.GONE);


                } else {
                    grid_holder.view_grid_left.setVisibility(View.VISIBLE);
                    grid_holder.view_grid_right.setVisibility(View.GONE);
                }


            }
        }


    }

    @Override
    public int getItemCount()
    {
        return commonDatas.size();
    }

       private void callwebservice__add_tocart(String product_id, String device_id, String product_name, String price, String qty)
       {

           progressBarHandler.show();
           System.out.println("price-----------------------" + price);

           String login_url = context.getResources().getString(R.string.webservice_base_url) + "/add_cart";

           String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
           if (user_id.equals("notlogin"))
           {
               user_id="";
           }

           Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("product_id", product_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .setBodyParameter("name", product_name)
                .setBodyParameter("price", price)
                .setBodyParameter("quantity", "1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        System.out.println("result-----------"+result);

                        if (result!=null)
                        {

                            String error_message = result.get("error").getAsString();

                            if (error_message.equals("false"))
                            {

                                System.out.println("result--------------" + result);
                                String message = result.get("message").getAsString();
                                JsonObject jsonObject = result.getAsJsonObject("result");

                                if (message.equals("This Item Already Exist....."))
                                {
                                    progressBarHandler.hide();
                                    AndroidUtils.showToast(context, "This Item Already Exist in Cart");
                                }
                                else if (message.equals("Product Quantity exceeded."))
                                {

                                    progressBarHandler.hide();
                                    AndroidUtils.showToast(context,"Product Quantity exceeded");
                                }
                                else
                                {
                                    AndroidUtils.showToast(context,"Product Successfully Added on Cart.");
                                    String cart_count = jsonObject.get("total_qty").getAsString();

                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));
                                    //int j = appSharedPreference.getSharedPrefInt("cart_count",0);
                                    ShopDetailActivity.tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
                                    progressBarHandler.hide();
                                }

                            }
                            else
                            {
                                 progressBarHandler.hide();
                                AndroidUtils.showToast(context,"Server is not responding. Please try again.");
                            }

                        }
                        else
                        {

                            progressBarHandler.hide();
                            AndroidUtils.showToast(context,"Server is not responding. Please try again.");
                        }

                    }
                });

    }


}
