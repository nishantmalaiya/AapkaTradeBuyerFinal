package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentListHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView machineType, machineNo, machinePrice;
    public ImageView imageView;
    public LinearLayout linearLayout;

    CheckBox machineSelection;

    public BillPaymentListHolder(View itemView) {

        super(itemView);
        machineSelection = (CheckBox) itemView.findViewById(R.id.checkedBoxMachineSelection);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        machineType = (TextView) itemView.findViewById(R.id.tvMachineType);
        machineNo = (TextView) itemView.findViewById(R.id.machineNo);
        machinePrice = (TextView) itemView.findViewById(R.id.machinePrize);
        imageView = (ImageView) itemView.findViewById(R.id.machine_img);
        view = itemView;
    }
}