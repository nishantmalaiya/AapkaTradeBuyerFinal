package com.aapkatrade.buyer.ZoomImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
    ArrayList<String> imageurl=new ArrayList<>();
    ZoomViewPagerAdapter zoomViewPagerAdapter;
    ViewPager viewpager_custom;
    private int dotsCount;
    Timer banner_timer = new Timer();
    int currentPage = 0;
    CircleIndicator circleIndicator;
    public ZoomImageDialog() {




    }

//    public ZoomImageDialog(Context context, Bitmap bitmap) {
//       this.context=context;
//        this.bitmap=bitmap;
//    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zoom_image_dialog_layout);
       // goto_zoom_imageview.putStringArrayListExtra("imageurl",imageurl);
        imageurl= getIntent().getStringArrayListExtra("imageUrlArrayList");
        if(imageurl!=null)
        AndroidUtils.showErrorLog(context,imageurl.toString());
        initView();

        setupviewpager(imageurl);


    }

    private void setupviewpager(ArrayList<String> imageIdList) {



        zoomViewPagerAdapter  = new ZoomViewPagerAdapter(this, imageIdList);
        viewpager_custom.setAdapter(zoomViewPagerAdapter);

        //viewpager_custom.setCurrentItem(currentPage);
            //setUiPageViewController();

            final Handler handler = new Handler();

            final Runnable update = new Runnable() {
                public void run() {
                    if (currentPage == zoomViewPagerAdapter.getCount() - 1) {
                        currentPage = 0;
                    }
                    viewpager_custom.setCurrentItem(currentPage++, true);
                }
            };


//            banner_timer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    handler.post(update);
//                }
//            }, 0, 3000);

        circleIndicator.setViewPager(viewpager_custom);

    }


    private void initView() {
        viewpager_custom=(ViewPager)findViewById(R.id.viewpager_zoom) ;
        circleIndicator=(CircleIndicator)findViewById(R.id.indicator_custom) ;


//       ZoomableImageView mImageView = (ZoomableImageView)findViewById(R.id.action_infolinks_splash);
//        mImageView.setMaxZoom(3f);
//        mImageView.setImageBitmap(bitmap);



    }
//    private void setUiPageViewController() {
//
//        dotsCount = zoomViewPagerAdapter.getCount();
//        dots = new ImageView[dotsCount];
//
//        for (int i = 0; i < dotsCount; i++) {
//            dots[i] = new ImageView(getApplicationContext());
//            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselected_item));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//
//            params.setMargins(4, 0, 4, 0);
//
//            viewpagerindicator.addView(dots[i], params);
//        }
//
//        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
//
//
//    }




}
