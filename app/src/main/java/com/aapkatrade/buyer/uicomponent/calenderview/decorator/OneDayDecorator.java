package com.aapkatrade.buyer.uicomponent.calenderview.decorator;

import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;


import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewFacade;

import java.util.Date;

/**
 * Decorate a day by making the text big and bold
 */
public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;

    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
    }


    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
