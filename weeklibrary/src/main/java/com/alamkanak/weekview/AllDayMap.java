package com.alamkanak.weekview;

import java.util.HashMap;

/**
 * 作者：husongzhen on 17/5/13 09:20
 * 邮箱：husongzhen@musikid.com
 */

public class AllDayMap extends HashMap<Long, ADayItem> {


    public AllDayMap() {
    }

    public void add(long day, ADayItem event) {
        put(day, event.setDay(day));
    }


    public ADayItem obtionItem(long day) {
        ADayItem result = get(day);
        if (result == null) {
            result = new ADayItem();
        }
        return result;
    }
}
