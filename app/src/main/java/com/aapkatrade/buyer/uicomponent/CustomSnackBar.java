package com.aapkatrade.buyer.uicomponent;/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Raphaël Bussa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;

/**
 * Created by raphaelbussa on 06/05/16.
 */
@SuppressWarnings("SpellCheckingInspection")
public class CustomSnackBar {

    private LayoutInflater layoutInflater;
    private int layout;
    private int background;
    private View contentView;
    private LENGTH duration;
    private boolean swipe;
    private static Context context;

    private Snackbar snackbar;
    private Snackbar.SnackbarLayout snackbarView;

    private CustomSnackBar(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.duration = LENGTH.LONG;
        this.background = -1;
        this.layout = -1;
        this.swipe = true;
    }

    public static CustomSnackBar Builder(Context context) {
        context = context;
        return new CustomSnackBar(context);
    }

    public CustomSnackBar layout(int layout) {
        this.layout = layout;
        return this;
    }

    public CustomSnackBar background(int background) {
        this.background = background;
        return this;
    }

    public CustomSnackBar duration(LENGTH duration) {
        this.duration = duration;
        return this;
    }

    public CustomSnackBar swipe(boolean swipe) {
        this.swipe = swipe;
        return this;
    }

    public CustomSnackBar build(View view) {
        if (view == null) throw new CustomSnackbarException("view can not be null");
        if (layout == -1) throw new CustomSnackbarException("layout must be setted");
        switch (duration) {
            case INDEFINITE:
                snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
                break;
            case SHORT:
                snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
                break;
            case LONG:
                snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
                break;
        }
        snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();

        if (!swipe) {
            snackbarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    snackbarView.getViewTreeObserver().removeOnPreDrawListener(this);
                    ((CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams()).setBehavior(null);
                    return true;
                }
            });
        }

        snackbarView.setPadding(0, 0, 0, 0);
        if (background != -1){
            snackbarView.setBackgroundResource(background);
//            AndroidUtils.setGradientColor(snackbarView, GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.toast_end_color), ContextCompat.getColor(context, R.color.toast_start_color), GradientDrawable.Orientation.TOP_BOTTOM, 50);

        }

        contentView = layoutInflater.inflate(layout, null);
        snackbarView.addView(contentView, 0);
        return this;
    }

    public void setText(String msg) {
        TextView tvMsg = (TextView) snackbarView.findViewById(R.id.msg);
        tvMsg.setText(msg);
    }

    public void show() {
        snackbar.show();
    }

    public boolean isShowing() {
        return snackbar != null && snackbar.isShown();
    }

    public void dismiss() {
        if (snackbar != null) snackbar.dismiss();
    }

    public View getContentView() {
        return contentView;
    }

    public enum LENGTH {
        INDEFINITE, SHORT, LONG
    }

    public class CustomSnackbarException extends RuntimeException {

        public CustomSnackbarException(String detailMessage) {
            super(detailMessage);
        }

    }

}