package com.aapkatrade.buyer.uicomponent.customcardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC17 on 07-Jun-17.
 */

public class CustomCardViewHeader extends CardView {
    private View view;
    private ImageView imageViewLeft, imageViewRight;
    private TextView textViewTitle;

    public CustomCardViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCardViewHeader);

        String title = a.getString((R.styleable.CustomCardViewHeader_titleText));
        textViewTitle.setText(title);
        Drawable leftsrc = a.getDrawable((R.styleable.CustomCardViewHeader_leftSrc));
        imageViewLeft.setImageDrawable(leftsrc);



        Drawable rightsrc = a.getDrawable((R.styleable.CustomCardViewHeader_rightSrc));
        imageViewRight.setImageDrawable(rightsrc);
    }

    protected void init(Context context) {
        if (this.isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        imageViewLeft = (ImageView) findViewById(R.id.cardview_header_icon_left);
        imageViewRight = (ImageView) findViewById(R.id.cardview_header_icon_right);
        textViewTitle = (TextView) view.findViewById(R.id.cardview_header_tv_title);
    }

    protected int layoutId() {
        return R.layout.cardview_header;
    }


    public void setTitle(String title) {
        if (this.textViewTitle == null) {
            return;
        }
        this.textViewTitle.setText(title);
    }

    public void setTitle(@StringRes int title) {
        if (this.textViewTitle == null) {
            return;
        }
        this.textViewTitle.setText(title);
    }


    public void setImageDrawableLeft(Drawable icon) {
        if (this.imageViewLeft == null) {
            return;
        }
        this.imageViewLeft.setImageDrawable(icon);
    }

    public void setImageDrawableLeft(@DrawableRes int icon) {
        if (this.imageViewLeft == null) {
            return;
        }
        this.imageViewLeft.setImageResource(icon);
    }


    public void setImageDrawableRight(Drawable icon) {
        if (this.imageViewRight == null) {
            return;
        }
        this.imageViewRight.setImageDrawable(icon);
    }

    public void setImageDrawableRight(@DrawableRes int icon) {
        if (this.imageViewRight == null) {
            return;
        }
        this.imageViewRight.setImageResource(icon);
    }

    public void setImageRightVisibility() {

        if (this.imageViewRight.getVisibility() == View.GONE) {
            this.imageViewRight.setVisibility(View.VISIBLE);
        }


    }


    public void setImageLeftVisibility() {
        if (this.imageViewLeft.getVisibility() == View.GONE) {
            this.imageViewLeft.setVisibility(View.VISIBLE);
        }
    }

    public ImageView getRightImageView(){
        if(imageViewRight == null){
            return null;
        }
        return imageViewRight;
    }

    public ImageView getLeftImageView(){
        if(imageViewLeft == null){
            return null;
        }
        return imageViewLeft;
    }


    public void setImageLeftRotation(int angleOfRotation) {
        if (this.imageViewLeft!=null) {
            this.imageViewLeft.setRotation(angleOfRotation);
        }
    }

    public void setImageRightRotation(int angleOfRotation) {
        if (this.imageViewRight!=null) {
            this.imageViewRight.setRotation(angleOfRotation);
        }
    }
}
