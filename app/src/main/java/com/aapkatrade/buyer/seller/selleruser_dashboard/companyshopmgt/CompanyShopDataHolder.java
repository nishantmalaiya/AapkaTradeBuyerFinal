package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;


/**
 * Created by PPC16 on 10-Mar-17.
 */

public class CompanyShopDataHolder extends RecyclerView.ViewHolder {
    public TextView tvCompanyShop;
    public RelativeLayout rlAddProduct, rlEdit;

    public CompanyShopDataHolder(View itemView) {
        super(itemView);
        tvCompanyShop = (TextView) itemView.findViewById(R.id.tvCompanyShop);
        rlAddProduct = (RelativeLayout) itemView.findViewById(R.id.rlAddProduct);
        rlEdit = (RelativeLayout) itemView.findViewById(R.id.rlEdit);
    }
}