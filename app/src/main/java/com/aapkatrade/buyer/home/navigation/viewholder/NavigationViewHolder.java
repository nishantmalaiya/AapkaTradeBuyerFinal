package com.aapkatrade.buyer.home.navigation.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 22-Feb-17.
 */
public class NavigationViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout rl_category_container;
    public ImageView imageViewIcon;
    public TextView tvCategoryname;
    public View splitLine;

    public NavigationViewHolder(View itemView) {
        super(itemView);
        rl_category_container =  itemView.findViewById(R.id.ll_category_container);
        imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
        tvCategoryname = (TextView) itemView.findViewById(R.id.lblListHeader);
        splitLine = itemView.findViewById(R.id.splitLine);
    }
}
