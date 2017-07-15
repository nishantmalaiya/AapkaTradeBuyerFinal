package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final BillPaymentListHolder homeHolder = (BillPaymentListHolder) holder;


        homeHolder.machineNo.setText(itemList.get(position).machineNo);
        homeHolder.machinePrice.setText(new StringBuilder(context.getString(R.string.rupay_text)).append("  ").append(itemList.get(position).machineCost));
        homeHolder.machineType.setText(itemList.get(position).machineType);

        homeHolder.imageView.setBackground(context.getResources().getDrawable(itemList.get(position).background_color));
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

        homeHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((BillPaymentListHolder) holder).machineSelection.isChecked()){
                    ((BillPaymentListHolder) holder).machineSelection.setChecked(false);
                } else {
                    ((BillPaymentListHolder) holder).machineSelection.setChecked(true);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
