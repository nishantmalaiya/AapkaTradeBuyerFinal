package com.aapkatrade.buyer.user_dashboard.order_list.new_order;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;

import com.aapkatrade.buyer.dialogs.CancelOrderDialog;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.Order_detail;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.order_list.cancel_order_fragment.CancelOrderFragment;
import com.aapkatrade.buyer.user_dashboard.order_list.order_details.OrderDetailsActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PPC16 on 17-Jan-17.
 */

public class NewOrderListAdapter extends RecyclerView.Adapter<NewOrderListViewHolder> {
    private Fragment fragment;
    private List<NewOrderListData> itemList;
    private Context context;
    NewOrderListViewHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    private List<NewOrderListData> newOrderListDatas;
    ProgressBarHandler progressBarHandler;

    public NewOrderListAdapter(Context context, List<NewOrderListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        appSharedPreference = new AppSharedPreference(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");
        progressBarHandler = new ProgressBarHandler(context);
    }
    public NewOrderListAdapter(Context context, List<NewOrderListData> itemList, Fragment fragment) {
        this.itemList = itemList;
        this.context = context;
        appSharedPreference = new AppSharedPreference(context);
        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");
        progressBarHandler = new ProgressBarHandler(context);
        this.fragment = fragment;
    }

    @Override
    public NewOrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewOrderListViewHolder(LayoutInflater.from(context).inflate(R.layout.row_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NewOrderListViewHolder holder, final int position) {
        holder.productName.setText(itemList.get(position).product_name);
        holder.tvOrderDate.setText(itemList.get(position).order_date);
        holder.tvOrderPrice.setText(itemList.get(position).product_price);

        if(fragment!=null && fragment instanceof CancelOrderFragment){
            holder.buttonStripLayout.setVisibility(View.GONE);
        }

        AndroidUtils.setBackgroundSolidEachRadius(holder.buttonTrackOrder, context, R.color.greenshad_Order, 20, 0, 0, 20, GradientDrawable.RECTANGLE);
        AndroidUtils.setBackgroundSolidEachRadius(holder.buttonCancelOrder, context, R.color.greenshad_Order, 0, 20, 20, 0, GradientDrawable.RECTANGLE);

        Picasso.with(context).load(itemList.get(position).image_url)

                .error(R.drawable.ic_profile_user)
                .into (holder.productImage);


        holder.buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(itemList.get(position).order_id, position);
                cancelOrderDialog.show(fm, "Track Order");
            }
        });


        holder.buttonTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitTrackOrderWebService(position);
            }
        });
        holder.imgOrderDetail.setVisibility(View.GONE);

        holder.imgOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("OrderId", itemList.get(position).order_id);
                i.putExtra("userId", userId);

                context.startActivity(i);*/
            }
        });
    }


    private void hitTrackOrderWebService(int position) {
        progressBarHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url).concat("/order_track"))
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("order_id", itemList.get(position).order_id)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (result != null) {
                    String error = result.get("error").getAsString();
                    if (error.contains("false")) {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, result.toString());
                        Intent go_to_trackorder = new Intent(context, Order_detail.class);
                        go_to_trackorder.putExtra("class_name", context.getClass().getName());
                        go_to_trackorder.putExtra("result", result.toString());
                        context.startActivity(go_to_trackorder);
                    } else {
                        progressBarHandler.hide();
                    }
                }

                progressBarHandler.hide();
                AndroidUtils.showErrorLog(context, "result", result);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

