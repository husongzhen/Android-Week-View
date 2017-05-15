package com.alamkanak.weekview;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.HashSet;
import java.util.Set;

/**
 * 作者：husongzhen on 17/5/13 09:59
 * 邮箱：husongzhen@musikid.com
 */

public class ADayItem {
    private PointF originPoint = new PointF(0f, 0f);
    private long day;
    private RectF rectF;
    private Set<WeekView.EventRect> set;
    private float contentHeight;

    public ADayItem setContentHeight(float contentHeight) {
        this.contentHeight = contentHeight;
        return this;
    }

    public float getContentHeight() {
        return contentHeight;
    }

    public PointF getOriginPoint() {
        return originPoint;
    }

    public ADayItem() {
        this.set = new HashSet<>();
    }

    public long getDay() {
        return day;
    }

    public ADayItem setDay(long day) {
        this.day = day;
        return this;
    }

    public RectF getRectF() {
        return rectF;
    }

    public ADayItem setRectF(RectF rectF) {
        this.rectF = rectF;
        return this;
    }

    public Set<WeekView.EventRect> getSet() {
        return set;
    }

    public ADayItem addEvent(WeekView.EventRect eventRect) {
        set.add(eventRect);
        return this;
    }

    public boolean isZero() {
        return set.size() == 0;
    }

}
