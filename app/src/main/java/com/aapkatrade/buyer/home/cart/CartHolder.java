package com.aapkatrade.buyer.home.cart;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.aapkatrade.buyer.R;


/**
 * Created by PPC16 on 5/1/2017.
 */

public class CartHolder extends RecyclerView.ViewHolder
{

    public View view;
    public TextView tvProductName, tvProductShopName,tvProductPrice,textView64,tvProductSubtotalPrice;
    public ImageView productimage;
    public Button buttonAddtoCart;
    public Spinner spinner;
    public LinearLayout dropdown_ll;

    public CartHolder(View itemView)
    {
        super(itemView);

        dropdown_ll = (LinearLayout) itemView.findViewById(R.id.dropdown_ll);

        textView64 = (TextView) itemView.findViewById(R.id.textView64);

        tvProductSubtotalPrice = (TextView)  itemView.findViewById(R.id.tvProductSubtotalPrice);

//        spinner=(Spinner)itemView.findViewById(R.id.spinner_addto_card);
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        productimage = (ImageView) itemView.findViewById(R.id.imgProduct);

        tvProductShopName = (TextView) itemView.findViewById(R.id.tvProductShopName);

        tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);

        //  tvProductNumber = (TextView) itemView.findViewById(R.id.tvProductNumber);

        buttonAddtoCart = (Button) itemView.findViewById(R.id.buttonAddtoCart);



        view = itemView;
    }
}
