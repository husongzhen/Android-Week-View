package com.alamkanak.weekview.sample.apiclient.model;

import android.util.SparseArray;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：husongzhen on 17/4/21 14:58
 * 邮箱：husongzhen@musikid.com
 */

public class WeekEvents {


    private SparseArray<List<WeekViewEvent>> array;

    public WeekEvents() {
        this.array = new SparseArray<>();
    }


    public void addEvent(int year, int mouth, WeekViewEvent event) {
        List<WeekViewEvent> events = getEvents(year, mouth);
        if (events.contains(event)) {
            events.remove(event);
        }
        events.add(event);
    }

    public List<WeekViewEvent> getEvents(int year, int mouth) {
        int key = getKey(year, mouth);
        List<WeekViewEvent> events = array.get(key);
        if (events == null) {
            events = new ArrayList<>();
            array.put(key, events);
        }
        return events;
    }


//    public WeekViewEvent getEvent(int year, int mouth, WeekViewEvent event) {
//        List<WeekViewEvent> events = getEvents(year, mouth);
//        events.contains()
//        return events;
//    }


    private int getKey(int year, int mouth) {
        return year * 12 + mouth;
    }


}
