package com.aapkatrade.buyer.user_dashboard.order_list.order_details.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 15-May-17.
 */

class OrderDetailHolder extends RecyclerView.ViewHolder {

    ImageView imgProductImage;
    TextView tvProductName, tvPrize, tvProductQty, tvDateTime,tvDiscount;
    RelativeLayout rlCancelSubOrder;

    public OrderDetailHolder(View itemView) {
        super(itemView);
        imgProductImage = (ImageView) itemView.findViewById(R.id.imgProductImage);
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
        tvDiscount=(TextView)itemView.findViewById(R.id.tvProductDiscount);
        tvPrize = (TextView) itemView.findViewById(R.id.tvOrderPrize);
        tvProductQty = (TextView) itemView.findViewById(R.id.tvProductQty);
        tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
        rlCancelSubOrder=(RelativeLayout)itemView.findViewById(R.id.rlCancelSubOrder);

    }
}
