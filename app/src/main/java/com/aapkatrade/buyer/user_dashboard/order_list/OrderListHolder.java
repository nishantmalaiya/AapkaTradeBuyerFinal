package com.aapkatrade.buyer.user_dashboard.order_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPC16 on 17-Jan-17.
 */

public class OrderListHolder extends RecyclerView.ViewHolder {

    View view;
    TextView tvOrderDate, tvOrderPrize, productName;
    Button btn_trackorder;

    CircleImageView productImage;
    ImageView img_order_detail;
    Button orderTrack, btn_cancel_order;

    public OrderListHolder(View itemView) {
        super(itemView);


        productName = (TextView) itemView.findViewById(R.id.productName);

        img_order_detail = (ImageView) itemView.findViewById(R.id.img_order_detail);
        btn_cancel_order = (Button) itemView.findViewById(R.id.btn_cancel_order);

        orderTrack = (Button) itemView.findViewById(R.id.track_order);
        tvOrderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
        tvOrderPrize = (TextView) itemView.findViewById(R.id.tvOrderPrize);


        productImage = (CircleImageView) itemView.findViewById(R.id.circleImage);
        view = itemView;


    }
}
