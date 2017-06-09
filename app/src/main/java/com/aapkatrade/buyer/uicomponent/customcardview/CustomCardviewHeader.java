package com.aapkatrade.buyer.uicomponent.customcardview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 07-Jun-17.
 */

public class CustomCardviewHeader extends CardView {
    private View view;
    private ImageView imageView_left, imageView_right;
    private TextView textView_title;

    public CustomCardviewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCardviewHeader);

        String title = a.getString((R.styleable.CustomCardviewHeader_titleText));
        textView_title.setText(title);
        Drawable leftsrc = a.getDrawable((R.styleable.CustomCardviewHeader_leftSrc));
        imageView_left.setImageDrawable(leftsrc);



        Drawable rightsrc = a.getDrawable((R.styleable.CustomCardviewHeader_rightSrc));
        imageView_right.setImageDrawable(rightsrc);
    }

    protected void init(Context context) {
        if (this.isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        imageView_left = (ImageView) findViewById(R.id.cardview_header_icon_left);
        imageView_right = (ImageView) findViewById(R.id.cardview_header_icon_right);
        textView_title = (TextView) view.findViewById(R.id.cardview_header_tv_title);
    }

    protected int layoutId() {
        return R.layout.cardview_header;
    }


    public void setTitle(String title) {
        if (this.textView_title == null) {
            return;
        }
        this.textView_title.setText(title);
    }

    public void setTitle(@StringRes int title) {
        if (this.textView_title == null) {
            return;
        }
        this.textView_title.setText(title);
    }


    public void setImageDrawableLeft(Drawable icon) {
        if (this.imageView_left == null) {
            return;
        }
        this.imageView_left.setImageDrawable(icon);
    }

    public void setImageDrawableLeft(@DrawableRes int icon) {
        if (this.imageView_left == null) {
            return;
        }
        this.imageView_left.setImageResource(icon);
    }


    public void setImageDrawableRight(Drawable icon) {
        if (this.imageView_right == null) {
            return;
        }
        this.imageView_right.setImageDrawable(icon);
    }

    public void setImageDrawableRight(@DrawableRes int icon) {
        if (this.imageView_right == null) {
            return;
        }
        this.imageView_right.setImageResource(icon);
    }

    public void setImageRightVisibility() {

        if (this.imageView_right.getVisibility() == View.GONE) {
            this.imageView_right.setVisibility(View.VISIBLE);
        }


    }


    public void setImageLeftVisibility() {

        if (this.imageView_left.getVisibility() == View.GONE) {
            this.imageView_left.setVisibility(View.VISIBLE);
        }


    }


}
