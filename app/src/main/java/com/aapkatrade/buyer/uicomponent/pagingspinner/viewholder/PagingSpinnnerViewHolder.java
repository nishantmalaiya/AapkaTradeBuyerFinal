package com.aapkatrade.buyer.uicomponent.pagingspinner.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPC15 on 7/19/2017.
 */

public class PagingSpinnnerViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayout;
    public RelativeLayout containershoplist;
    public TextView spinnerItemName;
    public CircleImageView circleImageView;
    public TextView tvshopdropdownShopname;
    public TextView tvshopdropdownCategoryname;
    public RelativeLayout rootContainer;

    public PagingSpinnnerViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.container_simple_spinner);
        containershoplist = (RelativeLayout) itemView.findViewById(R.id.containershoplist);
        spinnerItemName = (TextView) itemView.findViewById(R.id.tvSpCategory);
        circleImageView = (CircleImageView) itemView.findViewById(R.id.shopimage);
        tvshopdropdownShopname = (TextView) itemView.findViewById(R.id.tvshopdropdownshopname);
        tvshopdropdownCategoryname = (TextView) itemView.findViewById(R.id.tvshopdropdownshopcategory);
        rootContainer = (RelativeLayout) itemView.findViewById(R.id.rootContainer);
    }
}