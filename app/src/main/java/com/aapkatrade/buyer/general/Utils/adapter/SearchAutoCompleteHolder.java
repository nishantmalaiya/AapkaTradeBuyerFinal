package com.aapkatrade.buyer.general.Utils.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPC16 on 7/24/2017.
 */

public class SearchAutoCompleteHolder extends RecyclerView.ViewHolder
{

    public TextView tv_shop_name, tvDistance,tvCategoryName;

    public SearchAutoCompleteHolder(View itemView)
    {
        super(itemView);

        tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);

        tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);

        tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
    }

}