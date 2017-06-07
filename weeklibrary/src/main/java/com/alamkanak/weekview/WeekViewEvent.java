package com.alamkanak.weekview;

import java.io.Serializable;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.alamkanak.weekview.WeekViewUtil.*;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class WeekViewEvent {
    private String mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private String mName;
    private String mLocation;
    private int mColor;
    private int mEditColor;
    private boolean mAllDay;
    private boolean isEditAble = false;
    private boolean isFinish = false;
    private boolean isCreate = false;
    private long orderBy = -1;
    private SimpleDateFormat sdf_yMd = new SimpleDateFormat("yyyyMMdd");

    public long getOrderBy() {
        if (orderBy == -1) {
            return mStartTime.getTimeInMillis();
        }
        return orderBy;
    }

    public void setOrderBy(long orderBy) {
        this.orderBy = orderBy;
    }

    private Serializable realVo;

    private boolean compToday;

    public void setRealVo(Serializable realVo) {
        this.realVo = realVo;
    }

    public Serializable getRealVo() {
        return realVo;
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public void setEditAble(boolean editAble) {
        isEditAble = editAble;
    }

    public boolean isEditAble() {
        return isEditAble;
    }

    public WeekViewEvent() {


    }

    /**
     * Initializes the event for week view.
     *
     * @param id          The id of the event.
     * @param name        Name of the event.
     * @param startYear   Year when the event starts.
     * @param startMonth  Month when the event starts.
     * @param startDay    Day when the event starts.
     * @param startHour   Hour (in 24-hour format) when the event starts.
     * @param startMinute Minute when the event starts.
     * @param endYear     Year when the event ends.
     * @param endMonth    Month when the event ends.
     * @param endDay      Day when the event ends.
     * @param endHour     Hour (in 24-hour format) when the event ends.
     * @param endMinute   Minute when the event ends.
     */
    public WeekViewEvent(String id, String name, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        this.mId = id;

        this.mStartTime = Calendar.getInstance();
        this.mStartTime.set(Calendar.YEAR, startYear);
        this.mStartTime.set(Calendar.MONTH, startMonth - 1);
        this.mStartTime.set(Calendar.DAY_OF_MONTH, startDay);
        this.mStartTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.mStartTime.set(Calendar.MINUTE, startMinute);

        this.mEndTime = Calendar.getInstance();
        this.mEndTime.set(Calendar.YEAR, endYear);
        this.mEndTime.set(Calendar.MONTH, endMonth - 1);
        this.mEndTime.set(Calendar.DAY_OF_MONTH, endDay);
        this.mEndTime.set(Calendar.HOUR_OF_DAY, endHour);
        this.mEndTime.set(Calendar.MINUTE, endMinute);

        this.mName = name;
    }

    /**
     * Initializes the event for week view.
     *
     * @param id        The id of the event.
     * @param name      Name of the event.
     * @param location  The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime   The time when the event ends.
     * @param allDay    Is the event an all day event.
     */
    public WeekViewEvent(String id, String name, String location, Calendar startTime, Calendar endTime, boolean allDay) {
        this.mId = id;
        this.mName = name;
        this.mLocation = location;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mAllDay = allDay;
    }


    public WeekViewEvent(int id, String name, String location, Calendar startTime, Calendar endTime, boolean allDay) {
        this(id + "", name, location, startTime, endTime, allDay);
    }

    /**
     * Initializes the event for week view.
     *
     * @param id        The id of the event.
     * @param name      Name of the event.
     * @param location  The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime   The time when the event ends.
     */
    public WeekViewEvent(String id, String name, String location, Calendar startTime, Calendar endTime) {
        this(id, name, location, startTime, endTime, false);
    }

    /**
     * Initializes the event for week view.
     *
     * @param id        The id of the event.
     * @param name      Name of the event.
     * @param startTime The time when the event starts.
     * @param endTime   The time when the event ends.
     */
    public WeekViewEvent(String id, String name, Calendar startTime, Calendar endTime) {
        this(id, name, null, startTime, endTime);
    }


    /**
     * Initializes the event for week view.
     *
     * @param id        The id of the event.
     * @param name      Name of the event.
     * @param startTime The time when the event starts.
     * @param endTime   The time when the event ends.
     */
    public WeekViewEvent(int id, String name, Calendar startTime, Calendar endTime) {
        this(id + "", name, null, startTime, endTime);
    }

    public Calendar getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        this.mEndTime = endTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public int getmEditColor() {
        return mEditColor;
    }

    public void setmEditColor(int mEditColor) {
        this.mEditColor = mEditColor;
    }

    public boolean isAllDay() {
        return mAllDay;
    }

    public void setAllDay(boolean allDay) {
        this.mAllDay = allDay;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekViewEvent that = (WeekViewEvent) o;
        return mId.equals(that.mId);
    }

//    @Override
//    public int hashCode() {
//        return (int) (mId ^ (mId >>> 32));
//    }

    public List<WeekViewEvent> splitWeekViewEvents() {
        //This function splits the WeekViewEvent in WeekViewEvents by day
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        // The first millisecond of the next day is still the same day. (no need to split events for this).
        Calendar endTime = (Calendar) this.getEndTime().clone();
        endTime.add(Calendar.MILLISECOND, -1);
        if (!isSameDay(this.getStartTime(), endTime)) {
            endTime = (Calendar) this.getStartTime().clone();
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);
            setEndTime(endTime);
            events.add(this);
//            WeekViewEvent event1 = new WeekViewEvent(this.getId(), this.getName(), this.getLocation(), this.getStartTime(), endTime, this.isAllDay());
//            event1.setColor(this.getColor());
//            events.add(event1);
//
//
//            // Add other days.
//            Calendar otherDay = (Calendar) this.getStartTime().clone();
//            otherDay.add(Calendar.DATE, 1);
//            while (!isSameDay(otherDay, this.getEndTime())) {
//                Calendar overDay = (Calendar) otherDay.clone();
//                overDay.set(Calendar.HOUR_OF_DAY, 0);
//                overDay.set(Calendar.MINUTE, 0);
//                Calendar endOfOverDay = (Calendar) overDay.clone();
//                endOfOverDay.set(Calendar.HOUR_OF_DAY, 23);
//                endOfOverDay.set(Calendar.MINUTE, 59);
//                WeekViewEvent eventMore = new WeekViewEvent(this.getId(), this.getName(), null, overDay, endOfOverDay, this.isAllDay());
//                eventMore.setColor(this.getColor());
//                events.add(eventMore);
//                // Add next day.
//                otherDay.add(Calendar.DATE, 1);
//            }
//
//            // Add last day.
//            Calendar startTime = (Calendar) this.getEndTime().clone();
//            startTime.set(Calendar.HOUR_OF_DAY, 0);
//            startTime.set(Calendar.MINUTE, 0);
//            if (startTime.getTime().getTime() != getEndTime().getTime().getTime()) {
//                WeekViewEvent event2 = new WeekViewEvent(this.getId(), this.getName(), this.getLocation(), startTime, this.getEndTime(), this.isAllDay());
//                event2.setColor(this.getColor());
//                events.add(event2);
//            }
        } else {
            int hour = getEndTime().get(Calendar.HOUR_OF_DAY);
            int minute = getEndTime().get(Calendar.MINUTE);
            if (0 == hour && minute == 0) {
                endTime = (Calendar) this.getStartTime().clone();
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                setEndTime(endTime);
            }
            events.add(this);
        }

        return events;
    }

    public void updateStartTime(Calendar startTime) {


    }


    public boolean isAboveToday() {
        Calendar startDate = getStartTime();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String start = sdf.format(startDate.getTime());
        String day = sdf.format(today.getTime());
        if (start.compareTo(day) >= 0) {
            return true;
        }
        return false;
    }
}
