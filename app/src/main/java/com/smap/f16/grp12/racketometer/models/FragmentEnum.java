package com.smap.f16.grp12.racketometer.models;

/**
 * Implementation inspired by this stackoverflow answer
 * http://stackoverflow.com/a/7996474
 */
public enum FragmentEnum {

    OVERVIEW(0),
    HISTORY(1),
    DETAILS(2);

    private int _value;

    FragmentEnum(int Value) {
        this._value = Value;
    }

    public int getValue() {
        return _value;
    }

    public static FragmentEnum fromInt(int i) {
        for (FragmentEnum b : FragmentEnum .values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }
}
