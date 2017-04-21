package com.alamkanak.weekview.utils;

/**
 * 作者：husongzhen on 17/4/20 12:23
 * 邮箱：husongzhen@musikid.com
 */

public class EventTimeUtils {
    private int mHeight = 0;
    private static final EventTimeUtils news = new EventTimeUtils();

    public static final EventTimeUtils news() {
        return news;
    }

    private EventTimeUtils() {
    }

    public void init(int hourHeight) {
        mHeight = hourHeight / 60;
    }


    public int heightToMinute(int height) {
        return height / mHeight;
    }


}
