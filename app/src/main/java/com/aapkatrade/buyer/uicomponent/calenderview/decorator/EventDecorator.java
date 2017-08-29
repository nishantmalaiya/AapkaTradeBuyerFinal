package com.aapkatrade.buyer.uicomponent.calenderview.decorator;



import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.DayViewFacade;
import com.aapkatrade.buyer.uicomponent.calenderview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
