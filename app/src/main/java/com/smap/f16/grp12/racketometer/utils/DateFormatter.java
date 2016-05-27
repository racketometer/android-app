package com.smap.f16.grp12.racketometer.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * converts date to a formatted string
 */
public class DateFormatter {
    private static final DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");

    public static String Date(DateTime d) {
        return format.print(d);
    }
}
