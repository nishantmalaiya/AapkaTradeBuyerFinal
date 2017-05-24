package com.aapkatrade.buyer.dialogs.track_order;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.CommomDataTrackingList;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.CommomHolder_Order_Tracking;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.CommonHolder_listProduct;
import com.aapkatrade.buyer.general.Tabletsize;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PPC17 on 08-May-17.
 */

public class CommonHolderOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<CommomDataTrackingList> commomDatasTrackingList;
    private String arrangementtype, categorytype;
    private View v;
    private String TAG;
    float initialX, initialY;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    public CommonHolderOrderListAdapter(Context context, ArrayList<CommomDataTrackingList> commomDatasTrackingList, String arrangementtype, String categorytype) {
        this.context = context;
        this.commomDatasTrackingList = commomDatasTrackingList;
        this.arrangementtype = arrangementtype;
        this.categorytype = categorytype;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        TAG = context.getClass().getSimpleName();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        CommonHolder_listProduct viewHolder_listProduct;


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        v = inflater.inflate(R.layout.activity_order, parent, false);
        viewHolder_listProduct = new CommonHolder_listProduct(v);
        return viewHolder_listProduct;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {


        final CommomHolder_Order_Tracking holder = new CommomHolder_Order_Tracking(v);
        holder.tvAddress.setText(commomDatasTrackingList.get(position).address);
        holder.tvProductName.setText(commomDatasTrackingList.get(position).productname);
        holder.tvproductprize.setText(commomDatasTrackingList.get(position).price);
        holder.tvExpectedDeliveryTime.setText(commomDatasTrackingList.get(position).expected_Delivery_Address);



        if (Tabletsize.isTablet(context)) {
            Picasso.with(context)
                    .load(commomDatasTrackingList.get(position).imageurl)
                    .error(R.drawable.banner)
                    .placeholder(R.drawable.default_noimage)
                    .error(R.drawable.default_noimage)
                    .into(holder.product_imageview);
            String product_imageurl = commomDatasTrackingList.get(position).imageurl.replace("small", "large");

            Ion.with(holder.product_imageview)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            Log.e("image_large", "image_large");

        } else if (Tabletsize.isMedium(context)) {
            String product_imageurl = commomDatasTrackingList.get(position).imageurl.replace("small", "medium");

            Ion.with(holder.product_imageview)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            Log.e("image_medium", "image_medium" + product_imageurl);

        } else if (Tabletsize.isSmall(context)) {
            String product_imageurl = commomDatasTrackingList.get(position).imageurl.replace("small", "medium");

            Ion.with(holder.product_imageview)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(product_imageurl);

            Log.e("image_small", "image_small");
        }


        Log.e("imageurl", commomDatasTrackingList.get(0).imageurl);


    }

    @Override
    public int getItemCount() {
        return commomDatasTrackingList.size();
    }


}