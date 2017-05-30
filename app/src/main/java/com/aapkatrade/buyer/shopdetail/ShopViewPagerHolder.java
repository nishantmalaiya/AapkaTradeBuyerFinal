package com.aapkatrade.buyer.shopdetail;

import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 10-Feb-17.
 */

public class ShopViewPagerHolder extends RecyclerView.ViewHolder {

    View view;
    ImageView imageView;
    PercentRelativeLayout relativeLayout;

    public ShopViewPagerHolder(View itemView) {

        super(itemView);

        relativeLayout = (PercentRelativeLayout) itemView.findViewById(R.id.relativelayout);

        imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);


        view = itemView;


    }
}