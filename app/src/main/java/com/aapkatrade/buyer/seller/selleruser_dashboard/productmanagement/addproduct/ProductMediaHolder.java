package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 21-Feb-17.
 */

public class ProductMediaHolder extends RecyclerView.ViewHolder {

    public View view;
    public ImageView previewImage;
    public TextView cancelImage;
    public ImageView playImage;
    public CardView cardImage;

    public ProductMediaHolder(View itemView) {
        super(itemView);

        cardImage = (CardView) itemView.findViewById(R.id.cardImage);
        previewImage = (ImageView) itemView.findViewById(R.id.previewImage);
        cancelImage = (TextView) itemView.findViewById(R.id.cancelImage);
        playImage = (ImageView) itemView.findViewById(R.id.play_image);
        view = itemView;
    }
}
