package com.smap.f16.grp12.racketometer.utils;

import com.smap.f16.grp12.racketometer.models.Session;

import java.util.Comparator;

/**
 * Sort collection of sessions according to date.
 * Inspired by: http://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property#answer-2784576
 */
class DateComparator implements Comparator<Session> {
    @Override
    public int compare(Session lhs, Session rhs) {
        return lhs.getDate().compareTo(rhs.getDate());
    }
}
