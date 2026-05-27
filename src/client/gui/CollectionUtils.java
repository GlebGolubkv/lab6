package client.gui;

import common.dataclasses.MusicBand;
import common.dataclasses.MusicBandEntry;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Фильтрация и сортировка записей коллекции для таблицы главного окна.
 */
public final class CollectionUtils {

    /**
     * Один уровень сортировки: столбец и направление.
     *
     * @param columnKey ключ столбца ({@code col.id}, …)
     * @param ascending {@code true} — по возрастанию / старые даты / короче строка
     */
    public record SortLevel(String columnKey, boolean ascending) {
    }

    /** Запрещает создание экземпляров утилитарного класса. */
    private CollectionUtils() {
    }

    /**
     * Фильтрует по одному столбцу и сортирует по цепочке уровней (сначала первый, затем второй и т.д.).
     *
     * @param source          исходный список записей
     * @param filterText      значение фильтра (пустое — без фильтрации)
     * @param filterColumnKey столбец для фильтра
     * @param sortLevels      уровни сортировки по порядку приоритета (пустой — без сортировки)
     * @param sortActive      {@code false} — порядок как с сервера
     * @param locale          локаль для форматирования фильтра
     * @return новый список после фильтрации и опциональной сортировки
     */
    public static List<MusicBandEntry> filterAndSort(
            List<MusicBandEntry> source,
            String filterText,
            String filterColumnKey,
            List<SortLevel> sortLevels,
            boolean sortActive,
            Locale locale) {

        Stream<MusicBandEntry> stream = source.stream();
        String filterCol = filterColumnKey == null ? "col.key" : filterColumnKey;

        if (filterText != null && !filterText.isBlank()) {
            String needle = filterText.trim();
            stream = stream.filter(e -> matchesColumnFilter(e, needle, filterCol, locale));
        }

        if (sortActive && sortLevels != null && !sortLevels.isEmpty()) {
            Comparator<MusicBandEntry> combined = null;
            for (SortLevel level : sortLevels) {
                Comparator<MusicBandEntry> step = comparatorFor(level.columnKey(), locale);
                if (!level.ascending()) {
                    step = step.reversed();
                }
                combined = combined == null ? step : combined.thenComparing(step);
            }
            stream = stream.sorted(combined);
        }

        return stream.collect(Collectors.toList());
    }

    /**
     * Полное совпадение значения выбранного столбца с текстом фильтра (без подстрок).
     */
    private static boolean matchesColumnFilter(MusicBandEntry entry, String needle, String columnKey, Locale locale) {
        return tableCellValue(entry, columnKey, locale).equalsIgnoreCase(needle);
    }

    /**
     * Текст ячейки в том же виде, что в таблице {@link MainView}.
     */
    static String tableCellValue(MusicBandEntry entry, String columnKey, Locale locale) {
        MusicBand band = entry.getMusicBand();
        return switch (columnKey) {
            case "col.id" -> String.valueOf(band.getId());
            case "col.name" -> band.getName() != null ? band.getName() : "";
            case "col.x" -> Formats.formatNumber(locale, band.getCoordinates().getX());
            case "col.y" -> Formats.formatNumber(locale, band.getCoordinates().getY());
            case "col.creation" -> band.getCreationDate() != null
                    ? Formats.formatDateTime(locale, band.getCreationDate()) : "";
            case "col.participants" -> Formats.formatNumber(locale, band.getNumberOfParticipants());
            case "col.albums" -> Formats.formatNumber(locale, band.getAlbumsCount());
            case "col.genre" -> band.getGenre() != null ? band.getGenre().name() : "";
            case "col.label" -> band.getLabel() != null && band.getLabel().getBands() != null
                    ? String.valueOf(band.getLabel().getBands()) : "";
            case "col.owner" -> String.valueOf(entry.getOwnerId());
            default -> String.valueOf(entry.getBandKey());
        };
    }

    private enum ColumnSortKind {
        NUMERIC,
        STRING_BY_LENGTH,
        DATE_TIME
    }

    private static ColumnSortKind sortKindFor(String column) {
        return switch (column) {
            case "col.name", "col.genre" -> ColumnSortKind.STRING_BY_LENGTH;
            case "col.creation" -> ColumnSortKind.DATE_TIME;
            default -> ColumnSortKind.NUMERIC;
        };
    }

    private static Comparator<MusicBandEntry> comparatorFor(String column, Locale locale) {
        return switch (sortKindFor(column)) {
            case STRING_BY_LENGTH -> stringLengthComparator(column);
            case DATE_TIME -> Comparator.comparing(
                    e -> e.getMusicBand().getCreationDate(),
                    Comparator.nullsLast(Comparator.<ZonedDateTime>naturalOrder()));
            case NUMERIC -> numericComparator(column);
        };
    }

    private static Comparator<MusicBandEntry> stringLengthComparator(String column) {
        return Comparator
                .comparingInt((MusicBandEntry e) -> stringSortValue(e, column).length())
                .thenComparing(e -> stringSortValue(e, column), String.CASE_INSENSITIVE_ORDER);
    }

    private static String stringSortValue(MusicBandEntry entry, String column) {
        MusicBand band = entry.getMusicBand();
        return switch (column) {
            case "col.name" -> band.getName() != null ? band.getName() : "";
            case "col.genre" -> band.getGenre() != null ? band.getGenre().name() : "";
            default -> "";
        };
    }

    private static Comparator<MusicBandEntry> numericComparator(String column) {
        return switch (column) {
            case "col.id" -> Comparator.comparing(
                    e -> e.getMusicBand().getId(),
                    Comparator.nullsLast(Integer::compareTo));
            case "col.x" -> Comparator.comparingInt(e -> e.getMusicBand().getCoordinates().getX());
            case "col.y" -> Comparator.comparingDouble(e -> e.getMusicBand().getCoordinates().getY());
            case "col.participants" -> Comparator.comparingLong(e -> e.getMusicBand().getNumberOfParticipants());
            case "col.albums" -> Comparator.comparingLong(e -> e.getMusicBand().getAlbumsCount());
            case "col.label" -> Comparator.comparing(
                    e -> e.getMusicBand().getLabel().getBands(),
                    Comparator.nullsLast(Integer::compareTo));
            case "col.owner" -> Comparator.comparingInt(MusicBandEntry::getOwnerId);
            default -> Comparator.comparingInt(MusicBandEntry::getBandKey);
        };
    }
}
