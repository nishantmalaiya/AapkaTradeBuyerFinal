package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by PPC16 on 10-Mar-17.
 */

public class CompanyShopDataHolder extends RecyclerView.ViewHolder {
    public TextView tvCompanyShop, rowRightIconDescription;
    public ImageView rowRightIcon;
    public CardView cardLayout;
    public CircleImageView company_image;

    public CompanyShopDataHolder(View itemView) {
        super(itemView);
        tvCompanyShop = (TextView) itemView.findViewById(R.id.tvCompanyShop);
        rowRightIcon = (ImageView) itemView.findViewById(R.id.rowRightIcon);
        rowRightIconDescription = (TextView) itemView.findViewById(R.id.rowRightIconDescription);
        cardLayout = (CardView) itemView.findViewById(R.id.cardLayout);
        company_image = (CircleImageView) itemView.findViewById(R.id.imageCompany);

    }
}