package com.alamkanak.weekview.sample;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.sample.apiclient.model.WeekEvents;
import com.alamkanak.weekview.utils.EventTimeUtils;
import com.alamkanak.weekview.view.DragScaleView;

import java.util.Calendar;
import java.util.List;

import static com.alamkanak.weekview.utils.EventTimeUtils.news;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicTimeActivity extends BaseActivity implements WeekView.EventEditListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekView.setNumberOfVisibleDays(1);
        // Lets change some dimensions to best fit the view.
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mWeekView.setTimeSet();
        mWeekView.setEditListener(this);
        mWeekView.showTimeArea();
//        mWeekView.scrollToTime(4);
    }

    private WeekEvents events = new WeekEvents();

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//        Toast.makeText(this, "onMonthChange , newYear" + newYear + ", newMonth = " + newMonth, Toast.LENGTH_SHORT).show();
        // Populate the week view with some events.
        List<WeekViewEvent> mevents = events.getEvents(newYear, newMonth);
        if (mevents != null && mevents.size() > 0) {
            return mevents;
        }
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.DAY_OF_MONTH, startTime.get(Calendar.DAY_OF_MONTH) -1);
        endTime.add(Calendar.HOUR_OF_DAY, 24);
        endTime.set(Calendar.MINUTE, 0);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setEditAble(true);
        events.addEvent(newYear, newMonth, event);

        return events.getEvents(newYear, newMonth);
    }


//    @Override
//    public void onEventLongPress(MotionEvent e, int pos) {
//    }



    @Override
    public void onUpListener(int pos, float topSum, float bottomSum) {
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        event.setStartTime(news().getCurrectTime(event.getStartTime(), topSum));
        event.setEndTime(news().getCurrectTime(event.getEndTime(), bottomSum));
        event.setName(getEventTitle(event.getStartTime(), event.getEndTime()));
        mWeekView.notifyDatasetChanged();

    }

    @Override
    public void onDragingListener(int pos, float topSum, float bottomSum) {
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        Calendar start = EventTimeUtils.news().getCurrectTime(event.getStartTime(), topSum);
        Calendar end = EventTimeUtils.news().getCurrectTime(event.getEndTime(), topSum);
        event.setName(getEventTitle(start, end));
    }
}
