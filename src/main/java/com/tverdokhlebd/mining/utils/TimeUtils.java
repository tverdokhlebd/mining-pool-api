package com.tverdokhlebd.mining.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Utils for working with time.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class TimeUtils {

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
