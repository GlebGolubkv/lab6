package client.gui;

import common.dataclasses.Coordinates;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicGenre;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

import java.util.Optional;

/**
 * Edit a single field of a {@link MusicBand} (Lab 8).
 */
public final class FieldEditDialog {

    private FieldEditDialog() {
    }

    public static Optional<MusicBand> show(Window owner, MusicBand original) {
        Dialog<MusicBand> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(Localization.getInstance().get("band.dialog.edit"));

        ComboBox<String> fieldBox = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(
                "name", "x", "y", "participants", "albums", "genre", "label"));
        TextField valueField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(12));
        grid.addRow(0, new javafx.scene.control.Label("Field"), fieldBox);
        grid.addRow(1, new javafx.scene.control.Label("Value"), valueField);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn != ButtonType.OK) {
                return null;
            }
            return applyField(original, fieldBox.getValue(), valueField.getText().trim());
        });
        return dialog.showAndWait();
    }

    private static MusicBand applyField(MusicBand o, String field, String value) {
        String name = o.getName();
        Coordinates coords = o.getCoordinates();
        int x = coords.getX();
        double y = coords.getY();
        long participants = o.getNumberOfParticipants();
        long albums = o.getAlbumsCount();
        MusicGenre genre = o.getGenre();
        Integer labelBands = o.getLabel().getBands();

        switch (field) {
            case "name" -> name = value;
            case "x" -> x = Integer.parseInt(value);
            case "y" -> y = Double.parseDouble(value);
            case "participants" -> participants = Long.parseLong(value);
            case "albums" -> albums = Long.parseLong(value);
            case "genre" -> genre = value.isEmpty() ? null : MusicGenre.valueOf(value.toUpperCase());
            case "label" -> labelBands = Integer.parseInt(value);
            default -> throw new IllegalArgumentException("Unknown field");
        }
        return new MusicBand(o.getId(), name, new Coordinates(x, y), o.getCreationDate(),
                participants, albums, genre, new common.dataclasses.Label(labelBands));
    }
}
