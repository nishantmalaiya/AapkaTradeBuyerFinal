package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPC16 on 21-Feb-17.
 */

public class ProductMediaHolder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView previewImage;
    public ImageView cancelImage;

    public ProductMediaHolder(View itemView) {
        super(itemView);

        previewImage = (ImageView) itemView.findViewById(R.id.previewImage);
        cancelImage = (ImageView) itemView.findViewById(R.id.cancelImage);
        view = itemView;
    }
}
