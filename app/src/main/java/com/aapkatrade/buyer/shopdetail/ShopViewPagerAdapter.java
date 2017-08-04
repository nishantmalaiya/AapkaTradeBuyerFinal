package com.aapkatrade.buyer.shopdetail;

import android.content.Context;
import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.ZoomImage.ZoomImageDialog;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC16 on 10-Feb-17.
 */


public class ShopViewPagerAdapter extends PagerAdapter {

    public Context context;
    private ArrayList<String> imageUrlArrayList = new ArrayList<>();


    public ShopViewPagerAdapter(Context context, ArrayList<String> imageUrlArrayList) {
        this.imageUrlArrayList = imageUrlArrayList;
        this.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public int getCount() {
        return imageUrlArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewpager_product_detail, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_viewpager_detail);


        AndroidUtils.showErrorLog(context, "position============" + position);

        String product_imageurl = imageUrlArrayList.get(position);


        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentZoomImageView = new Intent(context, ZoomImageDialog.class);
                intentZoomImageView.putStringArrayListExtra("imageUrlArrayList", imageUrlArrayList);
                context.startActivity(intentZoomImageView);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}