package com.tverdokhlebd.mining.utils;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Calendar;
import java.util.Date;

/**
 * Utils for working with time.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TimeUtils {

    /** Period of repeated task. */
    public static final long REPEATED_TASK_PERIOD = SECONDS.toMillis(10);

    /**
     * Adds minutes to date.
     *
     * @param currentDate current date
     * @param amount amount of minutes
     * @return date with added amount of minutes
     */
    public static Date addMinutes(Date currentDate, int amount) {
        Calendar now = Calendar.getInstance();
        now.setTime(currentDate);
        now.add(Calendar.MINUTE, amount);
        return now.getTime();
    }

}
