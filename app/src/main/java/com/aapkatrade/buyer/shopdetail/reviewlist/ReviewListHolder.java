package com.aapkatrade.buyer.shopdetail.reviewlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aapkatrade.buyer.R;

/**
 * Created by PPC16 on 4/8/2017.
 */

public class ReviewListHolder extends RecyclerView.ViewHolder
{
    View view;
    TextView title ,message_description, username, deliver_date,tvRating;
    ImageView imgStar;
    RelativeLayout relativeRating;


    public ReviewListHolder(View itemView)
    {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.tvtitle);

        message_description = (TextView) itemView.findViewById(R.id.tvMessage) ;

        username = (TextView) itemView.findViewById(R.id.tvShopName);

        deliver_date = (TextView) itemView.findViewById(R.id.deliver_date);

        tvRating = (TextView) itemView.findViewById(R.id.tvRating);

        imgStar = (ImageView) itemView.findViewById(R.id.imgStar);

        deliver_date = (TextView) itemView.findViewById(R.id.deliver_date);

        relativeRating = (RelativeLayout) itemView.findViewById(R.id.relativeRating);

        view = itemView;

    }
}