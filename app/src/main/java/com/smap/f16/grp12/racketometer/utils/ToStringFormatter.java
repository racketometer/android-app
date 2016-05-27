package com.smap.f16.grp12.racketometer.utils;

import java.text.DecimalFormat;

/**
 * Helper class to format values to strings.
 */
public class ToStringFormatter {
    private static final DecimalFormat decimal = new DecimalFormat("#.00");

    /**
     * Limit decimals of a double to a specified length.
     * @param value The value to format.
     * @param decimals The number of decimals.
     * @return The double value as a string with specified number of decimals.
     */
    public static String FromDouble(double value, int decimals) {
        String format = "#.";

        if(decimals == 0) {
            format = "#";
        } else {
            for (int i = 0; i < decimals; i++) {
                format += "0";
            }
        }

        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

    /**
     * Limit decimals of a double to a default length (2 decimals).
     * @param value The value to format.
     * @return The double value as a string with default number of decimals.
     */
    public static String FromDouble(double value) {
        if(value == 0) {
            return "0";
        }
        return decimal.format(value);
    }
}
