package com.alamkanak.weekview.sample;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.sample.apiclient.TimestampTool;
import com.alamkanak.weekview.sample.apiclient.model.WeekEvents;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.alamkanak.weekview.sample.apiclient.TimestampTool.sdf_yMd;
import static com.alamkanak.weekview.utils.EventTimeUtils.news;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicActivity extends BaseActivity implements WeekView.EventEditListener, WeekView.EmptyViewClickListener, WeekView.OutCreateClickListener {


    private Date start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeekView.setEditListener(this);
        mWeekView.setEmptyViewClickListener(this);
        mWeekView.setOutCreateClickListener(this);
        mWeekView.scrollToTime(6);
        mWeekView.setSortAllDayEvents(new WeekView.SortAllDayEvents() {
            @Override
            public List<WeekView.EventRect> sort(List<WeekView.EventRect> lists) {
                return lists;
            }
        });
        mWeekView.setPastEventEditAble(false);
    }


    private WeekEvents events = new WeekEvents();


    @Override
    public void onEventClick(WeekView.EventRect event, RectF eventRect) {
        super.onEventClick(event, eventRect);
    }

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
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 24);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(111, "24", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(120, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(110, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 23);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);


        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(23, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);


        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(24, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);


        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        event.setmEditColor(getResources().getColor(R.color.uu_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        event.setmEditColor(getResources().getColor(R.color.iu_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);

        //AllDay event
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 23);
        event = new WeekViewEvent(7, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_04));
        event.setmEditColor(getResources().getColor(R.color.uu_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(12, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmEditColor(getResources().getColor(R.color.ue_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(22, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmEditColor(getResources().getColor(R.color.ue_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(32, getEventTitle(startTime), null, startTime, endTime, true);
        event.setFinish(true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmEditColor(getResources().getColor(R.color.ue_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(33, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmEditColor(getResources().getColor(R.color.ue_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(11, getEventTitle(startTime), null, startTime, endTime, true);
        event.setFinish(true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_03));
        event.setmEditColor(getResources().getColor(R.color.ue_color));
        event.setFinish(true);
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
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
        event = new WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true);
        event.setColor(getResources().getColor(R.color.event_color_01));
        event.setmEditColor(getResources().getColor(R.color.ie_color));
        event.setmTitleColor(getResources().getColor(R.color.ie_title_color));
        events.addEvent(newYear, newMonth, event);


        return events.getEvents(newYear, newMonth);
    }


    @Override
    protected String getEventTitle(Calendar time) {
        return "my name is = " + TimestampTool.sdf_all.format(time.getTime());
    }

//    @Override
//    public void onEventLongPress(MotionEvent e, int pos) {
//        WeekView.EventRect eventRect = mWeekView.getEventRect(pos);
//        Toast.makeText(this, "Long pressed event: " + eventRect.event.getName(), Toast.LENGTH_SHORT).show();
////        mWeekView.setNextPager();
//    }


    @Override
    public void onUpListener(int pos, float topSum, float bottomSum) {
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        event.setStartTime(news().getCurrectTime(event.getStartTime(), topSum));
        event.setEndTime(news().getCurrectTime(event.getEndTime(), bottomSum));
        event.setName(getEventTitle(event.getStartTime()));
        mWeekView.notifyDatasetChanged();

    }

    @Override
    public void onDragingListener(int pos, float topSum, float bottomSum) {
        WeekView.EventRect rect = mWeekView.getEventRect(pos);
        WeekViewEvent event = rect.originalEvent;
        Calendar start = news().getCurrectTime(event.getStartTime(), topSum);
        event.setName(getEventTitle(start));
    }

    @Override
    public void onEmptyViewClicked(Calendar time, boolean pastEventEditAble) {
        int year = time.get(Calendar.YEAR);
        int mouth = time.get(Calendar.MONTH);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        Calendar startTime = (Calendar) time.clone();
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, hour + 1);
        WeekViewEvent event = new WeekViewEvent(-1, "新建日程", startTime, endTime);
        event.setColor(getResources().getColor(R.color.create_event_color));
        event.setCreate(true);
        if (pastEventEditAble) {
            events.addEvent(year, mouth + 1, event);
            mWeekView.notifyDatasetChanged();
        } else {
            if (event.isAboveToday()) {
                events.addEvent(year, mouth + 1, event);
                mWeekView.notifyDatasetChanged();
            }
        }
        Toast.makeText(this, " click event: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOutCreateEventClick(WeekView.EventRect createEvent) {
        Calendar time = createEvent.event.getEndTime();
        int year = time.get(Calendar.YEAR);
        int mouth = time.get(Calendar.MONTH);
        List<WeekViewEvent> list = events.getEvents(year, mouth + 1);
        if (list.contains(createEvent.event)) {
            list.remove(createEvent.event);
        }
        mWeekView.notifyDatasetChanged();
    }
}
