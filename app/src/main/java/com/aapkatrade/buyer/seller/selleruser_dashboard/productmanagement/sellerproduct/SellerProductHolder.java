package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.sellerproduct;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.aapkatrade.buyer.R;
/**
 * Created by PPC16 on 7/10/2017.
 */

public class SellerProductHolder extends RecyclerView.ViewHolder
{
    View view;
    TextView  productName,tvProductShopName,tvProductCategoryName,tvProductStateName;

    public SellerProductHolder(View itemView)
    {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.productName);

        tvProductShopName = (TextView) itemView.findViewById(R.id.tvProductShopName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvProductCategoryName);

        tvProductStateName = (TextView) itemView.findViewById(R.id.tvProductStateName);

        view = itemView;
    }
}

