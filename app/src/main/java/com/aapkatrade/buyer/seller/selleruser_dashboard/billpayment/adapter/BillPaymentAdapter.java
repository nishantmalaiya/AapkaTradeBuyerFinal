package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.entity.BillPaymentList;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.viewholder.BillPaymentViewHolder;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.entity.BillPayment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentAdapter extends RecyclerView.Adapter<BillPaymentViewHolder> {
    private List<BillPaymentList> itemList;
    private Context context;
    private BillPaymentActivity billPaymentActivity;

    public BillPaymentAdapter(Context context, List<BillPaymentList> itemList, BillPaymentActivity billPaymentActivity) {
        this.itemList = itemList;
        this.context = context;
        this.billPaymentActivity = billPaymentActivity;
    }

    @Override
    public BillPaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillPaymentViewHolder(LayoutInflater.from(context).inflate(R.layout.billpaymentrow, parent, false));
    }

    @Override
    public void onBindViewHolder(final BillPaymentViewHolder holder, final int position) {
       

        holder.machineNo.setText(itemList.get(position).getMachineNo());
        holder.machinePrice.setText(new StringBuilder(context.getString(R.string.rupay_text)).append("  ").append(itemList.get(position).getMachineCost()));
        holder.machineType.setText(itemList.get(position).getMachineType());
        holder.imageView.setBackground(ContextCompat.getDrawable(context, itemList.get(position).getBackgroundColor()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemList.get(position).setSelected(isChecked);
                billPaymentActivity.commonInterface.getData(true);
            }


        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                } else {
                    holder.checkBox.setChecked(true);
                }
            }
        });
    }


    private ArrayList<Integer> calculateTotalAmountToPay(){
        ArrayList<Integer> selectedList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).isSelected()){
                selectedList.add(i);
            }
        }
        return selectedList;
    }




    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
