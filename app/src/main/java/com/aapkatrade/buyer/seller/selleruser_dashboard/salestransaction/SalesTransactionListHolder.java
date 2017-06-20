package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 13-Jun-17.
 */

class SalesTransactionListHolder extends RecyclerView.ViewHolder {


    TextView tvBillhistories,tvPaymentStatus,tvBillhistoryAmount;
    public SalesTransactionListHolder(View view) {
        super(view);
        tvBillhistories=(TextView)view.findViewById(R.id.tv_billhistories);
        tvPaymentStatus=(TextView)view.findViewById(R.id.tvPaymentStatus);
        tvBillhistoryAmount=(TextView)view.findViewById(R.id.billhistoryamount);



    }
}
