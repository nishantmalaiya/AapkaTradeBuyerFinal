package com.aapkatrade.buyer.home.banner_home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aapkatrade.buyer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class viewpageradapter_home extends PagerAdapter {

    private Context mContext;
    ArrayList<String> imageIdList;


    public viewpageradapter_home(Context mContext, ArrayList<String> imageIdList) {
        this.imageIdList = imageIdList;
        this.mContext = mContext;

    }


    public int getCount() {
        return imageIdList != null ? imageIdList.size() : -1;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);

        Picasso.with(mContext).load(imageIdList.get(position)).into(imageView);


        container.addView(itemView);

        return itemView;
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}