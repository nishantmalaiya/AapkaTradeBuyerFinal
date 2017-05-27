package com.aapkatrade.buyer.categories_tab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class CategoriesListHolder extends RecyclerView.ViewHolder
{

    View view;
    public RelativeLayout relativeLayout1;
    public RelativeLayout linearlayoutMap;
    public TextView tvProductName;
    public TextView tvProductCategoryname;
    public TextView distance;
    public TextView tvProductDestination;
    public ImageView productimage;




    public CategoriesListHolder(View itemView)
    {

        super(itemView);

        relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeProductDetail) ;

        linearlayoutMap = (RelativeLayout) itemView.findViewById(R.id.linearlayoutMap);

        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

        distance=(TextView)itemView.findViewById(R.id.product_distance) ;

        tvProductDestination = (TextView) itemView.findViewById(R.id.tvProductDestination);

        productimage = (ImageView) itemView.findViewById(R.id.productImage);

        tvProductCategoryname = (TextView) itemView.findViewById(R.id.tvProductCategoryname);


        view = itemView;
    }
}