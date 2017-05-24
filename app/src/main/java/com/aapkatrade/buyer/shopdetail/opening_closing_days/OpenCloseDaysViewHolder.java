package com.aapkatrade.buyer.shopdetail.opening_closing_days;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 4/10/2017.
 */

public class OpenCloseDaysViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView tvDayName, tvOpeningTime, tvClosingTime;
    public RelativeLayout relativeOpenShop;


    public OpenCloseDaysViewHolder(View itemView) {
        super(itemView);
        relativeOpenShop = (RelativeLayout) itemView.findViewById(R.id.relativeOpenShop);
        tvDayName = (TextView) itemView.findViewById(R.id.tvdays);
        tvOpeningTime = (TextView) itemView.findViewById(R.id.tvopentime);
        tvClosingTime = (TextView) itemView.findViewById(R.id.tvclosetime);
        view = itemView;
    }
}