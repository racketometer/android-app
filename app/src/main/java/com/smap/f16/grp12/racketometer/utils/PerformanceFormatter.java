package com.smap.f16.grp12.racketometer.utils;

import com.smap.f16.grp12.racketometer.models.Session;

/**
 * converts session stats to a formatted performance string
 */
public class PerformanceFormatter {
    public static String Performance(Session item) {
        double accumulatedPerformance = item.getAgility() + item.getPower() + item.getSpeed();
        return String.valueOf(accumulatedPerformance);
    }
}
