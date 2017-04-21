package com.alamkanak.weekview.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import com.alamkanak.weekview.WeekViewEvent;

/**
 * 作者：husongzhen on 17/4/20 10:18
 * 邮箱：husongzhen@musikid.com
 */

public class EventTextUtils {

    @NonNull
    public static final String getEventTitle(WeekViewEvent event) {
        // Prepare the name of the event.
        SpannableStringBuilder bob = new SpannableStringBuilder();
        if (event.getName() != null) {
            bob.append(event.getName());
            bob.setSpan(new StyleSpan(Typeface.BOLD), 0, bob.length(), 0);
            bob.append(' ');
        }

        // Prepare the location of the event.
        if (event.getLocation() != null) {
            bob.append(event.getLocation());
        }
        return bob.toString();
    }


}
