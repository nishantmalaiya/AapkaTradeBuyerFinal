package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;


/**
 * Created by PPC16 on 10-Mar-17.
 */

public class CompanyShopDataHolder extends RecyclerView.ViewHolder {
    public TextView tvCompanyShop;
    public ImageView imageViewEditCompany;
    public CardView cardLayout;

    public CompanyShopDataHolder(View itemView) {
        super(itemView);
        tvCompanyShop = (TextView) itemView.findViewById(R.id.tvCompanyShop);
        imageViewEditCompany = (ImageView) itemView.findViewById(R.id.imageViewEditCompany);
        cardLayout = (CardView) itemView.findViewById(R.id.cardLayout);
    }
}