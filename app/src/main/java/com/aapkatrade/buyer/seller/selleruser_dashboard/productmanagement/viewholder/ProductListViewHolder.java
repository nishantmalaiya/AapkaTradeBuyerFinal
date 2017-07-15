package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aapkatrade.buyer.R;
/**
 * Created by PPC16 on 7/10/2017.
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder
{
    public View view;
    public TextView  productName,tvProductShopName,tvProductCategoryName,tvProductStateName;
    public ImageView imgProduct;
    public Button btnEdit,btnDelete;

    public ProductListViewHolder(View itemView)
    {
        super(itemView);

       productName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductShopName = (TextView) itemView.findViewById(R.id.tvCompanyName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);

        tvProductStateName = (TextView) itemView.findViewById(R.id.tvStateName);

        imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);

        btnEdit = (Button) itemView.findViewById(R.id.btnEdit);

        btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

        view = itemView;
    }
}

