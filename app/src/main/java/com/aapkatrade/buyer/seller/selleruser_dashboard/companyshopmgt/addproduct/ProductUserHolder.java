package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 6/19/2017.
 */

public class ProductUserHolder  extends RecyclerView.ViewHolder
{

    View view;
   public RelativeLayout relativeImage ;

    public ProductUserHolder(View itemView)
    {
        super(itemView);

        relativeImage = (RelativeLayout) itemView.findViewById(R.id.relativeImage);

        view = itemView;
    }
}

