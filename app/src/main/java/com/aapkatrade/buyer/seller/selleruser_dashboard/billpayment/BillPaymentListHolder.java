package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentListHolder extends RecyclerView.ViewHolder {

    View view;
    TextView machineType, machineNo, machinePrize;
    ImageView machine_img;


    CheckBox machineSelection;

    public BillPaymentListHolder(View itemView) {

        super(itemView);
        machineSelection = (CheckBox) itemView.findViewById(R.id.checkedBoxMachineSelection);

        machineType = (TextView) itemView.findViewById(R.id.tvMachineType);
        machineNo = (TextView) itemView.findViewById(R.id.machineNo);
        machinePrize = (TextView) itemView.findViewById(R.id.machinePrize);
        machine_img = (ImageView) itemView.findViewById(R.id.machine_img);
        view = itemView;
    }
}