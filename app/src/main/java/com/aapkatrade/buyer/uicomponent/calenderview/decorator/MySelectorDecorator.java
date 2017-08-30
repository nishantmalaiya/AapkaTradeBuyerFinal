package com.aapkatrade.buyer.uicomponent.calenderview.decorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewFacade;


/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public MySelectorDecorator(Activity context) {
        drawable = ContextCompat.getDrawable(context,R.drawable.my_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
