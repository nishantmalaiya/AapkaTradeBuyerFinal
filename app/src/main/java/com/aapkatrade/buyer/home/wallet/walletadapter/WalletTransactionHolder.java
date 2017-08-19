package com.aapkatrade.buyer.home.wallet.walletadapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 17-Aug-17.
 */

public class WalletTransactionHolder extends RecyclerView.ViewHolder {

    CardView cardview;
    ImageView transaction_type_imgvew,imgvew_transaction_status;
    TextView tv_transactiontypeName,tv_transactionDate,tvTransactionid,tv_transaction_amount;


    public WalletTransactionHolder(View itemView) {
        super(itemView);
        cardview = (CardView) itemView.findViewById(R.id.cardview_wallet_transaction);
        transaction_type_imgvew = (ImageView) itemView.findViewById(R.id.transaction_type_imgvew);
        imgvew_transaction_status=(ImageView)itemView.findViewById(R.id.imgvew_transaction_status);
        tv_transactiontypeName = (TextView) itemView.findViewById(R.id.tv_transactiontypeName);
        tv_transactionDate=(TextView) itemView.findViewById(R.id.tv_transactionDate);
        tvTransactionid=(TextView) itemView.findViewById(R.id.tvTransactionid);
        tv_transaction_amount=(TextView) itemView.findViewById(R.id.tv_transaction_amount);

    }


}
