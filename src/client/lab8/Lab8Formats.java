package client.lab8;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Locale-aware formatting for numbers and dates (Lab 8).
 */
public final class Lab8Formats {

    private Lab8Formats() {
    }

    public static String formatNumber(Locale locale, long value) {
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    public static String formatNumber(Locale locale, double value) {
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    public static String formatDateTime(Locale locale, ZonedDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale);
        return dateTime.format(formatter);
    }
}
