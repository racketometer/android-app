package com.smap.f16.grp12.racketometer.utils;

import com.smap.f16.grp12.racketometer.models.Attributes;
import com.smap.f16.grp12.racketometer.models.Session;

import java.util.List;

/**
 * Calculate performance values.
 */
public class PerformanceFormatter {
    public static String Performance(Session item) {
        double accumulatedPerformance = item.getAgility() + item.getPower() + item.getSpeed();
        return String.valueOf(accumulatedPerformance);
    }

    public static double PerformanceAsDouble(Session item) {
        return  item.getAgility() + item.getPower() + item.getSpeed();
    }

    /**
     * Get averaged attribute values from a list of sessions.
     * @param sessions The sessions.
     * @return The calculated attributes.
     */
    public static Attributes getAveragedAttributes(List<Session> sessions) {
        int sessionsLength = sessions.size();
        double speed = 0;
        double agility = 0;
        double power = 0;

        if(sessionsLength != 0) {
            for (int i = 0; i < sessionsLength; i++) {
                Session session = sessions.get(i);
                speed += session.getSpeed();
                agility += session.getAgility();
                power += session.getPower();
            }

            speed = speed / sessionsLength;
            agility = agility / sessionsLength;
            power = power / sessionsLength;
        }

        return new Attributes(agility, power, speed);
    }
}
