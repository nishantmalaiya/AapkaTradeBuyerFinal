package com.aapkatrade.buyer.home;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;


/**
 * Created by Netforce on 7/25/2016.
 */
public class CommonHolder extends RecyclerView.ViewHolder {


    CardView cardview;
    ImageView cimageview;
    TextView tvProductName;


    public CommonHolder(View itemView) {
        super(itemView);
        cardview = (CardView) itemView.findViewById(R.id.cardview);
        cimageview = (ImageView) itemView.findViewById(R.id.img_product_image_list);
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
    }
}
