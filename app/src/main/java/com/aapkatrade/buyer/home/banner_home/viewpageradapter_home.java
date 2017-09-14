package com.aapkatrade.buyer.home.banner_home;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class viewpageradapter_home extends PagerAdapter {

    private Context mContext;
    ArrayList<String> imageIdList;

    ProgressBar progressBar;
    private LayoutInflater inflater;


    public viewpageradapter_home(Context mContext, ArrayList<String> imageIdList) {
        this.imageIdList = imageIdList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    public int getCount() {
        return imageIdList != null ? imageIdList.size() : -1;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);


        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
       // progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

        Picasso.with(mContext).load(imageIdList.get(position)).into(imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
               /* if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);

                }*/
            }

            @Override
            public void onError() {


                imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.banner_home));


            }
        });


        container.addView(itemView,0);

        return itemView;
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}