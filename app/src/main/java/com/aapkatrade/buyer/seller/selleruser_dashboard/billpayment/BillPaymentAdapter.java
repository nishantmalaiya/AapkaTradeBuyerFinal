package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

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
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.CheckPermission;
import com.aapkatrade.buyer.general.LocationManagerCheck;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.map.GoogleMapActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.aapkatrade.buyer.user_dashboard.DashboardHolder;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<BillPaymentListData> itemList;
    public Activity context;
    private BillPaymentListHolder viewHolder;
    private ProgressBarHandler progressBarHandler;

    int totalBillAmount = 0;


    public BillPaymentAdapter(Activity context, List<BillPaymentListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.billpaymentrow, parent, false);

        viewHolder = new BillPaymentListHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final BillPaymentListHolder homeHolder = (BillPaymentListHolder) holder;


        homeHolder.machineNo.setText(itemList.get(position).machineNo);
        homeHolder.machinePrize.setText(new StringBuilder(context.getString(R.string.rupay_text)).append("  ").append(itemList.get(position).machineCost));
        homeHolder.machineType.setText(itemList.get(position).machineType);

        homeHolder.machine_img.setBackground(context.getResources().getDrawable(itemList.get(position).background_color));
        homeHolder.machineSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                itemList.get(position).selected = isChecked;
                if (isChecked) {
                    totalBillAmount = totalBillAmount + Integer.parseInt(itemList.get(position).machineCost);
                } else {
                    totalBillAmount = totalBillAmount - Integer.parseInt(itemList.get(position).machineCost);
                }
                BillPaymentActivity.commonInterface.getData(totalBillAmount);
            }


        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
