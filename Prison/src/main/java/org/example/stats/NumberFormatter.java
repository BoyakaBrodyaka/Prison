package org.example.stats;

import java.text.DecimalFormat;

public class NumberFormatter {
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public static String format(double value) {
        if (value >= 1000000000) {
            return df.format(value / 1000000000) + "B";
        } else if (value >= 1000000) {
            return df.format(value / 1000000) + "M";
        } else if (value >= 1000) {
            return df.format(value / 1000) + "K";
        } else {
            return df.format(value);
        }
    }

    public static String formatPlain(double value) {
        return df.format(value);
    }

    public static double parse(String value) throws NumberFormatException {
        value = value.toUpperCase();
        if (value.endsWith("B")) {
            return Double.parseDouble(value.substring(0, value.length() - 1)) * 1000000000;
        } else if (value.endsWith("M")) {
            return Double.parseDouble(value.substring(0, value.length() - 1)) * 1000000;
        } else if (value.endsWith("K")) {
            return Double.parseDouble(value.substring(0, value.length() - 1)) * 1000;
        } else {
            return Double.parseDouble(value);
        }
    }
}

