package com.aapkatrade.buyer.user_dashboard.order_list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aapkatrade.buyer.R;

import com.aapkatrade.buyer.dialogs.CancelOrderDialog;
import com.aapkatrade.buyer.dialogs.track_order.orderdetail.Order_detail;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.user_dashboard.order_list.order_details.OrderDetailsActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 17-Jan-17.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<OrderListData> itemList;
    private Context context;
    OrderListHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    private List<OrderListData> orderListDatas;
    ProgressBarHandler progressBarHandler;

    public OrderListAdapter(Context context, List<OrderListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);

        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");
        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_order_list, parent, false);
        viewHolder = new OrderListHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        OrderListHolder homeHolder = (OrderListHolder) holder;


        homeHolder.productName.setText(itemList.get(position).product_name);

        homeHolder.tvOrderDate.setText(itemList.get(position).order_date);
        homeHolder.tvOrderPrize.setText(itemList.get(position).product_price);

        Picasso.with(context).load(itemList.get(position).image_url)

                .error(R.drawable.ic_profile_user)
                .into(((OrderListHolder) holder).productImage);


        homeHolder.img_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("OrderId", itemList.get(position).order_id);
                i.putExtra("userId", userId);

                context.startActivity(i);


            }
        });

        homeHolder.btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();


                CancelOrderDialog cancel_order_dialog = new CancelOrderDialog(itemList.get(position).order_id,position);
                cancel_order_dialog.show(fm, "Track Order");
            }
        });




        homeHolder.orderTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTrackOrderWebservice();
            }

            private void callTrackOrderWebservice() {
                progressBarHandler.show();

                String track_order_url = context.getString(R.string.webservice_base_url) + "/order_track";


                Ion.with(context)
                        .load(track_order_url)
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

                        Log.e("result", result.toString());
//               JsonObject res result.get("otp_id").getAsString();


                    }
                });


            }
        });


    }


    private void showMessage(String s) {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }


}

