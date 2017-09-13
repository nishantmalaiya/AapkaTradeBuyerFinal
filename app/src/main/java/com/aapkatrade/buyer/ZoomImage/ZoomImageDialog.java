package com.aapkatrade.buyer.ZoomImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aapkatrade.buyer.shopdetail.ShopDetailMediaDatas;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by PPC15 on 31-03-2017.
 */

public class ZoomImageDialog extends AppCompatActivity {
    private Bitmap bitmap;
    private SubsamplingScaleImageView imageView;
    Context context;
    ArrayList<String> imageurl = new ArrayList<>();
    ZoomViewPagerAdapter zoomViewPagerAdapter;
    ViewPager viewpager_custom;
    private int dotsCount;
    Timer banner_timer = new Timer();
    int currentPage = 0;
    CircleIndicator circleIndicator;
    ArrayList<ShopDetailMediaDatas> imageUrlArrayList = new ArrayList<>();

    public ZoomImageDialog(Context context, ArrayList<ShopDetailMediaDatas> imageUrlArrayList) {
        this.context = context;
        this.imageUrlArrayList = imageUrlArrayList;
    }

    public ZoomImageDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zoom_image_dialog_layout);
        imageurl = getIntent().getStringArrayListExtra("imageUrlArrayList");
        if (imageurl != null)
            AndroidUtils.showErrorLog(context, imageurl.toString());
        initView();

        setupviewpager(imageurl);


    }

    private void setupviewpager(ArrayList<String> imageIdList) {


        zoomViewPagerAdapter = new ZoomViewPagerAdapter(this, imageIdList);
        viewpager_custom.setAdapter(zoomViewPagerAdapter);

        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == zoomViewPagerAdapter.getCount() - 1) {
                    currentPage = 0;
                }
                viewpager_custom.setCurrentItem(currentPage++, true);
            }
        };
        circleIndicator.setViewPager(viewpager_custom);

    }


    private void initView() {
        viewpager_custom = (ViewPager) findViewById(R.id.viewpager_zoom);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_custom);
    }


}
