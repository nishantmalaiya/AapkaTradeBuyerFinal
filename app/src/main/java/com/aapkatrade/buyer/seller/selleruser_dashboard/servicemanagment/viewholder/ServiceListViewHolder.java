package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class ServiceListViewHolder extends RecyclerView.ViewHolder
{
    public View view;
    public TextView  serviceName,tvServiceShopName,tvServiceCategoryName,tvServiceStateName;
    public ImageView imgService,imgviewServiceEdit;
    public RelativeLayout relativeEdit,relativeDelete,relativeUpdate;

    public ServiceListViewHolder(View itemView)
    {
        super(itemView);


        serviceName = (TextView) itemView.findViewById(R.id.tv_service_name);

        tvServiceShopName = (TextView) itemView.findViewById(R.id.tvServiceShopName);

        tvServiceCategoryName = (TextView) itemView.findViewById(R.id.tv_Service_category_name);



        imgService = (ImageView) itemView.findViewById(R.id.imgService);
        imgviewServiceEdit = (ImageView) itemView.findViewById(R.id.imgviewServiceEdit);

      /*  relativeEdit = (RelativeLayout) itemView.findViewById(R.id.relativeEdit);

        relativeDelete = (RelativeLayout) itemView.findViewById(R.id.relativeDelete);

        relativeUpdate = (RelativeLayout) itemView.findViewById(R.id.relativeUpdate);*/


        view = itemView;
    }
}

