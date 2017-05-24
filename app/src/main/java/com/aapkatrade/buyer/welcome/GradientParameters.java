package com.aapkatrade.buyer.welcome;


import android.graphics.drawable.GradientDrawable;

/**
 * Created by PPC17 on 20-May-17.
 */

public class GradientParameters {


    public int viewShape, endColor, cornerRadius, startColor;
    public GradientDrawable.Orientation orientation;

    public GradientParameters( int viewShape,int startColor, int endColor, int cornerRadius, GradientDrawable.Orientation orientation) {

        this.viewShape = viewShape;
        this.endColor = endColor;
        this.cornerRadius = cornerRadius;
        this.startColor = startColor;
        this.orientation = orientation;
    }
}
