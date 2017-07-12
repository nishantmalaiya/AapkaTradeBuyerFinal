package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.sellerproduct;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aapkatrade.buyer.R;
/**
 * Created by PPC16 on 7/10/2017.
 */

public class SellerProductHolder extends RecyclerView.ViewHolder
{
    View view;
    TextView  productName,tvProductShopName,tvProductCategoryName,tvProductStateName;
    ImageView imgProduct;
    RelativeLayout relativeEdit,relativeDelete,relativeUpdate;

    public SellerProductHolder(View itemView)
    {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductShopName = (TextView) itemView.findViewById(R.id.tvProductShopName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvProductCategoryName);

        tvProductStateName = (TextView) itemView.findViewById(R.id.tvProductStateName);

        imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);

        relativeEdit = (RelativeLayout) itemView.findViewById(R.id.relativeEdit);

        relativeDelete = (RelativeLayout) itemView.findViewById(R.id.relativeDelete);

        relativeUpdate = (RelativeLayout) itemView.findViewById(R.id.relativeUpdate);


        view = itemView;
    }
}

