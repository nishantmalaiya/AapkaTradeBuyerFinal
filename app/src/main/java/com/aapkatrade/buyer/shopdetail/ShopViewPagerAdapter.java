package com.aapkatrade.buyer.shopdetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC16 on 10-Feb-17.
 */


public class ShopViewPagerAdapter extends PagerAdapter {

    public Context context;
    ArrayList<ShopDetailMediaDatas> imageUrlArrayList = new ArrayList<>();
    ArrayList<String> imagedatas = new ArrayList<>();
    View itemView;
    AppSharedPreference appSharedPreference;


    public ShopViewPagerAdapter(Context context, ArrayList<ShopDetailMediaDatas> imageUrlArrayList) {
        this.imageUrlArrayList = imageUrlArrayList;
        this.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        appSharedPreference = new AppSharedPreference(context);

        for (int k = 0; k < imageUrlArrayList.size(); k++) {

            if (!imageUrlArrayList.get(k).isVideo)

                imagedatas.add(imageUrlArrayList.get(k).MediaUrl);
        }
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
    public Object instantiateItem(final ViewGroup container, final int position) {

        itemView = LayoutInflater.from(context).inflate(R.layout.viewpager_product_detail, container, false);
        ImageView imgview_video = (ImageView) itemView.findViewById(R.id.img_video);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_viewpager_detail);
        AndroidUtils.showErrorLog(context, "list12============" + imageUrlArrayList.get(position).MediaUrl);

        if (imageUrlArrayList.get(position).isVideo) {

            AndroidUtils.showErrorLog(context, "position============" + imageUrlArrayList.get(position).MediaUrl + "");
            imgview_video.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (Validation.isNonEmptyStr(imageUrlArrayList.get(position).MediaUrl)) {


                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        intent.setDataAndType(Uri.parse(imageUrlArrayList.get(position).MediaUrl), "video/*");

                        context.startActivity(Intent.createChooser(intent, "Complete action using"));

                    } else {

                        AndroidUtils.showErrorLog(context, "video Url", imageUrlArrayList.get(position).MediaUrl);

                    }
                }
            });


        } else {

            imgview_video.setVisibility(View.GONE);
            Ion.with(imageView)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(imageUrlArrayList.get(position).MediaUrl);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentZoomImageView = new Intent(context, ZoomImageDialog.class);
                    intentZoomImageView.putStringArrayListExtra("imageUrlArrayList", imagedatas);
                    context.startActivity(intentZoomImageView);
                }
            });
        }


        //AndroidUtils.setBackgroundStroke(imageView, context, R.color.green, 20, 5);


        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}