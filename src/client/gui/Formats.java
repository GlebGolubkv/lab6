package client.gui;

import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Утилиты форматирования чисел и дат для отображения в интерфейсе с учётом локали.
 */
public final class Formats {

    /** Запрещает создание экземпляров утилитарного класса. */
    private Formats() {
    }

    /**
     * Форматирует целое число по правилам указанной локали.
     *
     * @param locale локаль отображения
     * @param value  значение
     * @return строковое представление числа
     */
    public static String formatNumber(Locale locale, long value) {
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    /**
     * Форматирует вещественное число по правилам указанной локали.
     *
     * @param locale локаль отображения
     * @param value  значение
     * @return строковое представление числа
     */
    public static String formatNumber(Locale locale, double value) {
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    /**
     * Форматирует дату и время; для {@code null} возвращает пустую строку.
     *
     * @param locale   локаль отображения
     * @param dateTime момент времени
     * @return отформатированная дата-время или пустая строка
     */
    public static String formatDateTime(Locale locale, ZonedDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale);
        return dateTime.format(formatter);
    }
}
