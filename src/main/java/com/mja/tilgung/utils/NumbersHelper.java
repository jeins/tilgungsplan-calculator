package com.mja.tilgung.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumbersHelper {
    private static String FORMAT_PATTERN = "##,###.00";
    private static Locale DEFAULT_LOCALE = Locale.GERMANY;

    public static String formatDENumber(double num) {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(NumbersHelper.DEFAULT_LOCALE);
        DecimalFormat decimalFormat = new DecimalFormat(NumbersHelper.FORMAT_PATTERN, formatSymbols);

        return decimalFormat.format(num);
    }

    public static double reformatDENumber(String num) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(NumbersHelper.DEFAULT_LOCALE);
        Number number = null;

        try {
            number = numberFormat.parse(num);
        } catch (ParseException e) {
            e.printStackTrace();

            return 0;
        }

        return number.doubleValue();
    }

    public static double round(double num) {
        String formattedNumber = NumbersHelper.formatDENumber(num);

        return NumbersHelper.reformatDENumber(formattedNumber);
    }
}
