package com.alamkanak.weekview.utils;

import java.util.Calendar;

/**
 * 作者：husongzhen on 17/4/20 12:23
 * 邮箱：husongzhen@musikid.com
 */

public class EventTimeUtils {
    private float mHeight = 0;
    private float maxMinute = 60;
    private static final EventTimeUtils news = new EventTimeUtils();

    public static final EventTimeUtils news() {
        return news;
    }

    private EventTimeUtils() {
    }


    /**
     * 初始化一个小时的高度
     *
     * @param hourHeight
     */
    public void init(float hourHeight) {
        mHeight = hourHeight / maxMinute;
    }


    /**
     * 高度转化为分钟
     *
     * @param height
     * @return
     */
    public float heightToMinute(float height) {
        if (height == 0){
            return 0;
        }
        return height / mHeight;
    }


    public Calendar getCurrectTime(Calendar data, final float direct) {
        int start = (int) (data.get(Calendar.HOUR_OF_DAY) * 60
                        + data.get(Calendar.MINUTE) + EventTimeUtils.news().heightToMinute(direct));
        int hour = start / 60;
        int minute = start % 60;
        Calendar result = (Calendar) data.clone();
        result.set(Calendar.HOUR_OF_DAY, hour);
        result.set(Calendar.MINUTE, minute);
        return result;
    }


}
