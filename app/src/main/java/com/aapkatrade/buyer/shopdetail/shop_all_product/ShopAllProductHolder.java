package com.aapkatrade.buyer.shopdetail.shop_all_product;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 4/21/2017.
 */

public class ShopAllProductHolder extends RecyclerView.ViewHolder
{

    public View view;
    public TextView tvProductName, tvProductUnit, tvProductPrice,tv_qty;
    public ImageView productImage;
    public Button addToCartButton;
    LinearLayout dropdown_ll;


    public ShopAllProductHolder(View itemView)
    {
        super(itemView);

        dropdown_ll = (LinearLayout) itemView.findViewById(R.id.dropdown_ll);

        tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);


        productImage = (ImageView) itemView.findViewById(R.id.imgProduct);
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
        tvProductUnit = (TextView) itemView.findViewById(R.id.tvProductUnit);
        tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
        addToCartButton = (Button) itemView.findViewById(R.id.buttonAddtoCart);



        view = itemView;
    }
}