package com.aapkatrade.buyer.user_dashboard.order_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 17-Jan-17.
 */

public class OrderListViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView tvOrderDate, tvOrderPrice, productName;
    public Button buttonTrackOrder, buttonCancelOrder;
    public ImageView productImage, imgOrderDetail;
    public LinearLayout buttonStripLayout;

    public OrderListViewHolder(View itemView) {
        super(itemView);
        productName = (TextView) itemView.findViewById(R.id.productName);
        imgOrderDetail = (ImageView) itemView.findViewById(R.id.img_order_detail);
        buttonCancelOrder = (Button) itemView.findViewById(R.id.btn_cancel_order);
        buttonTrackOrder = (Button) itemView.findViewById(R.id.track_order);
        tvOrderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
        tvOrderPrice = (TextView) itemView.findViewById(R.id.tvOrderPrize);
        productImage = (ImageView) itemView.findViewById(R.id.image_view);
        buttonStripLayout = (LinearLayout) itemView.findViewById(R.id.buttonStripLayout);
        view = itemView;
    }
}
