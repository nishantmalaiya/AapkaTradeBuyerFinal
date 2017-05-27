package com.aapkatrade.buyer.ZoomImage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.uicomponent.customimageview.ZoomableImageView;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC16 on 10-Feb-17.
 */




public class ZoomViewPagerAdapter extends PagerAdapter implements GestureDetector.OnDoubleTapListener
{

     Context mContext;
    ArrayList<String> imageurl = new ArrayList<>();


    public ZoomViewPagerAdapter(Context mContext, ArrayList<String>productImage_url)
    {

        this. imageurl=productImage_url;
        this.mContext=mContext;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }


    public int getCount()
    {

        return imageurl.size();

    }

    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    public Object instantiateItem(final ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.zoom_imageview, container, false);

        final ZoomableImageView imageView = (ZoomableImageView) itemView.findViewById(R.id.action_infolinks_splash);

        System.out.println("position============" + position);
        if(Tabletsize.isTablet(mContext))
        {
            String product_imageurl=imageurl.get(position).replace("small","large");

            Ion.with(imageView)
                    .error(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            imageView.setMaxZoom(4f);
            Log.e("image_large","image_large");

        }
        else if(Tabletsize.isMedium(mContext))
        {
            String product_imageurl=imageurl.get(position).replace("small","medium");

            Ion.with(imageView)
                    .error(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .load(product_imageurl);
            Log.e("image_medium","image_medium");
            imageView.setMaxZoom(4f);

        }
        else if(Tabletsize.isSmall(mContext))
        {


            Ion.with(imageView)
                    .error(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_applogo1))
                    .load(imageurl.get(position));
            imageView.setMaxZoom(4f);


            Log.e("image_small","image_small");
        }

        container.addView(itemView);
        return itemView;

    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
//    private void showEditDialog(Bitmap bitmap) {
//        FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
//
//ZoomImageDialog editNameDialogFragment = ZoomImageDialog.newInstance(bitmap);
//        editNameDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
//        editNameDialogFragment.show(fm, "fragment_edit_name");
//    }


}