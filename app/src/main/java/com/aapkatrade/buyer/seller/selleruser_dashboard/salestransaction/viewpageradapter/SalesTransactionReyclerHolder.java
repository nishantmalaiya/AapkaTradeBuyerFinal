package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 21-Jun-17.
 */

class SalesTransactionReyclerHolder extends RecyclerView.ViewHolder{


    TextView SalesMachineTxnData,TxnStatus,TxnAmount,tv_saleshistorietxnid;
    public SalesTransactionReyclerHolder(View itemView) {
        super(itemView);
        SalesMachineTxnData=(TextView)itemView.findViewById(R.id.tv_saleshistories);
        TxnStatus=(TextView)itemView.findViewById(R.id.tvTxnStatus);
        TxnAmount=(TextView)itemView.findViewById(R.id.Salesamount);
        tv_saleshistorietxnid=(TextView)itemView.findViewById(R.id.tv_saleshistorietxnid);
    }
}
