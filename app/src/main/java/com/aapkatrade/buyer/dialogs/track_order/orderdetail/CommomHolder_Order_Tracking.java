package com.aapkatrade.buyer.dialogs.track_order.orderdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 08-May-17.
 */

public class CommomHolder_Order_Tracking extends RecyclerView.ViewHolder {

    public TextView tvProductName, tvShopName, tvsellerName, tvproductprize, tvAddress, tvExpectedDeliveryTime;
    public ImageView product_imageview, product_description;





    public CommomHolder_Order_Tracking(View itemView) {


        super(itemView);
        tvsellerName = (TextView) itemView.findViewById(R.id.tvProductName);
        tvproductprize = (TextView) itemView.findViewById(R.id.tvProductPrize);
        tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);

        tvProductName = (TextView) itemView.findViewById(R.id.tvShopName);
        product_description = (ImageView) itemView.findViewById(R.id.img_product_information);
        product_imageview = (ImageView) itemView.findViewById(R.id.img_view_orderlist);



        tvExpectedDeliveryTime = (TextView) itemView.findViewById(R.id.tvExpectedDeliveryTime);


    }
}
