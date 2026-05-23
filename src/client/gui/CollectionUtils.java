package client.gui;

import common.dataclasses.MusicBand;
import common.dataclasses.MusicBandEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Table filter and sort via Stream API (Lab 8 requirement).
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static List<MusicBandEntry> filterAndSort(
            List<MusicBandEntry> source,
            String filterText,
            String sortColumnKey,
            boolean ascending,
            Locale locale) {

        Stream<MusicBandEntry> stream = source.stream();

        if (filterText != null && !filterText.isBlank()) {
            String needle = filterText.trim().toLowerCase(Locale.ROOT);
            stream = stream.filter(e -> rowText(e, locale).toLowerCase(Locale.ROOT).contains(needle));
        }

        Comparator<MusicBandEntry> comparator = comparatorFor(sortColumnKey, locale);
        if (!ascending) {
            comparator = comparator.reversed();
        }

        return stream.sorted(comparator).collect(Collectors.toList());
    }

    private static String rowText(MusicBandEntry entry, Locale locale) {
        MusicBand b = entry.getMusicBand();
        return String.join(" ",
                String.valueOf(entry.getBandKey()),
                String.valueOf(b.getId()),
                b.getName(),
                Formats.formatNumber(locale, b.getCoordinates().getX()),
                Formats.formatNumber(locale, b.getCoordinates().getY()),
                Formats.formatDateTime(locale, b.getCreationDate()),
                Formats.formatNumber(locale, b.getNumberOfParticipants()),
                Formats.formatNumber(locale, b.getAlbumsCount()),
                b.getGenre() != null ? b.getGenre().name() : "",
                b.getLabel() != null ? String.valueOf(b.getLabel().getBands()) : "",
                String.valueOf(entry.getOwnerId())
        );
    }

    private static Comparator<MusicBandEntry> comparatorFor(String column, Locale locale) {
        return switch (column == null ? "col.key" : column) {
            case "col.id" -> Comparator.comparing(e -> e.getMusicBand().getId(), Comparator.nullsLast(Integer::compareTo));
            case "col.name" -> Comparator.comparing(e -> e.getMusicBand().getName(), Comparator.nullsLast(String::compareToIgnoreCase));
            case "col.x" -> Comparator.comparingInt(e -> e.getMusicBand().getCoordinates().getX());
            case "col.y" -> Comparator.comparingDouble(e -> e.getMusicBand().getCoordinates().getY());
            case "col.creation" -> Comparator.comparing(e -> e.getMusicBand().getCreationDate(), Comparator.nullsLast(Comparator.naturalOrder()));
            case "col.participants" -> Comparator.comparingLong(e -> e.getMusicBand().getNumberOfParticipants());
            case "col.albums" -> Comparator.comparingLong(e -> e.getMusicBand().getAlbumsCount());
            case "col.genre" -> Comparator.comparing(e -> e.getMusicBand().getGenre() != null ? e.getMusicBand().getGenre().name() : "");
            case "col.label" -> Comparator.comparing(e -> e.getMusicBand().getLabel().getBands(), Comparator.nullsLast(Integer::compareTo));
            case "col.owner" -> Comparator.comparingInt(MusicBandEntry::getOwnerId);
            default -> Comparator.comparingInt(MusicBandEntry::getBandKey);
        };
    }
}
