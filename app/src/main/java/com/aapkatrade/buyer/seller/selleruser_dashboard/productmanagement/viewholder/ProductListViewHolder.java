package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aapkatrade.buyer.R;
/**
 * Created by PPC16 on 7/10/2017.
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder
{

    public View view;
    public TextView  productName,tvProductShopName,tvProductCategoryName,tvProductStateName;
    public ImageView imgProduct;
    public Button btnEdit,btnDelete,btnPolicyUpdate;
    public ToggleButton radioButtonStatus;
    public CardView cardView;


    public ProductListViewHolder(View itemView)
    {
        super(itemView);
        cardView=(CardView)itemView.findViewById(R.id. card_view);
        productName = (TextView) itemView.findViewById(R.id.tvProductName);

        tvProductShopName = (TextView) itemView.findViewById(R.id.tvCompanyName);

        tvProductCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);

        tvProductStateName = (TextView) itemView.findViewById(R.id.tvStateName);

        imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);

        btnEdit = (Button) itemView.findViewById(R.id.btnEdit);

        btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

        btnPolicyUpdate = (Button) itemView.findViewById(R.id.btnPolicyUpdate);

        radioButtonStatus = (ToggleButton) itemView.findViewById(R.id.radioButtonStatus);

        view = itemView;
    }
}

