package com.aapkatrade.buyer.categories_tab.viewall.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.uicomponent.customimageview.MyImageView;

/**
 * Created by PPC15 on 8/22/2017.
 */

public class GridViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout gridLayout;
    public ImageView imageView;
    public TextView textView;

    public GridViewHolder(View itemView) {
        super(itemView);
        gridLayout = itemView.findViewById(R.id.gridLayout);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.tvTitle);
    }
}
