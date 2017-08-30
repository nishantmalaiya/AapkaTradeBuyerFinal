package com.aapkatrade.buyer.uicomponent.calenderview;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by PPC16 on 8/28/2017.
 */

class BetterViewPager {
    public BetterViewPager(Context context) {

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
