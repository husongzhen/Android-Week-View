package com.alamkanak.weekview.sample;

import android.os.Bundle;
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

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicTimeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekView.setTimeSet();
    }

    private WeekEvents events = new WeekEvents();

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Toast.makeText(this, "onMonthChange , newYear" + newYear + ", newMonth = " + newMonth, Toast.LENGTH_SHORT).show();
        // Populate the week view with some events.
        List<WeekViewEvent> mevents = events.getEvents(newYear, newMonth);
        if (mevents != null && mevents.size() > 0) {
            return mevents;
        }
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(10, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 23);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime, endTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.addEvent(newYear, newMonth, event);

        //AllDay event
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 23);
        event = new WeekViewEvent(7, getEventTitle(startTime, endTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.addEvent(newYear, newMonth, event);


        //AllDay event
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 2);
        event = new WeekViewEvent(12, getEventTitle(startTime, endTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.addEvent(newYear, newMonth, event);

        //AllDay event
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 2);
        event = new WeekViewEvent(11, getEventTitle(startTime, endTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.addEvent(newYear, newMonth, event);


        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 8);
        startTime.set(Calendar.HOUR_OF_DAY, 2);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, 10);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        event = new WeekViewEvent(8, getEventTitle(startTime, endTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.addEvent(newYear, newMonth, event);

        // All day event until 00:00 next day
        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 10);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, 11);
        event = new WeekViewEvent(8, getEventTitle(startTime, endTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.addEvent(newYear, newMonth, event);
        return events.getEvents(newYear, newMonth);
    }


    @Override
    public void onEventLongPress(MotionEvent e, int pos) {
        WeekView.EventRect eventRect = mWeekView.getEventRect(pos);
        drag.show(eventRect.event, eventRect.rectF, pos);
        mWeekView.setTouchAble(false);
//        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDragUpListener(View drag, int pos, int startSum, int endSum) {
        mWeekView.setTouchAble(true);
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        event.setStartTime(EventTimeUtils.news().getCurrectTime(event.getStartTime(), startSum));
        event.setEndTime(EventTimeUtils.news().getCurrectTime(event.getEndTime(), endSum));
        event.setName(getEventTitle(event.getStartTime()));
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void onDragingsListener(DragScaleView dragScaleView, int pos, int startSum, int endSum) {
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        event.setName(getEventTitle(EventTimeUtils.news().getCurrectTime(event.getStartTime(), startSum)));
    }


}
