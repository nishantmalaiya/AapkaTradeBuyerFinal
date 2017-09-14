package com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 13-Jun-17.
 */

class BillHistoryListHolder extends RecyclerView.ViewHolder {


    TextView tvBillhistories, tvPaymentStatus, tvBillhistoryAmount, tvPaymentDate, tvPaymentTime;

    public BillHistoryListHolder(View view) {
        super(view);
        tvBillhistories = (TextView) view.findViewById(R.id.tv_billhistories);
        tvPaymentStatus = (TextView) view.findViewById(R.id.payment_mode_status);
        tvBillhistoryAmount = (TextView) view.findViewById(R.id.payment_amount);
        tvPaymentDate = (TextView) view.findViewById(R.id.tv_bill_date);
        tvPaymentTime = (TextView) view.findViewById(R.id.tv_bill_time);


    }
}
