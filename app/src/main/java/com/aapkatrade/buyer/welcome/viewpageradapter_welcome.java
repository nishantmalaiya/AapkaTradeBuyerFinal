package com.aapkatrade.buyer.welcome;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class viewpageradapter_welcome extends PagerAdapter {

    private Context mContext;
    int[] imagepath, layouts;
    String[] slider_header, slider_footer;
    ArrayList<GradientParameters> gradientParametersArrayList = new ArrayList<>();


    public viewpageradapter_welcome(Context mContext, int layouts[], int imagepaths[], String[] slider_header, String[] slider_footer,
                                    ArrayList<GradientParameters> gradientParametersArrayList) {
        this.imagepath = imagepaths;
        this.mContext = mContext;
        this.layouts = layouts;
        this.slider_header = slider_header;
        this.slider_footer = slider_footer;
        this.gradientParametersArrayList = gradientParametersArrayList;

    }


    public int getCount() {
        return layouts.length;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(layouts[position], container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        ImageView welcome_app_logo=(ImageView)itemView.findViewById(R.id.welcome_app_logo) ;

        GradientParameters gradientParameters = gradientParametersArrayList.get(position);

        Picasso.with(mContext).load(imagepath[position]).into(imageView);

        TextView tvSlideHeader = (TextView) itemView.findViewById(R.id.tvSlideHeader);
        tvSlideHeader.setText(slider_header[position]);
        TextView tvSlideFooter = (TextView) itemView.findViewById(R.id.tvSlidefooter);
        tvSlideFooter.setText(slider_footer[position]);
        if(position==0)
        {

            welcome_app_logo.setVisibility(View.VISIBLE);
        }
        else
        {

            welcome_app_logo.setVisibility(View.GONE);
        }


        AndroidUtils.setGradientColor(itemView, gradientParameters.viewShape, gradientParameters.startColor, gradientParameters.endColor, gradientParameters.orientation, gradientParameters.cornerRadius);
        container.addView(itemView);

        return itemView;


    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);

    }
}