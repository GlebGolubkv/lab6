package client.gui;

import common.dataclasses.Coordinates;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicGenre;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Dialog for creating or editing a {@link MusicBand} (Lab 8).
 */
public class BandDialog extends Dialog<MusicBand> {

    private final TextField nameField = new TextField();
    private final TextField xField = new TextField();
    private final TextField yField = new TextField();
    private final TextField participantsField = new TextField();
    private final TextField albumsField = new TextField();
    private final ComboBox<MusicGenre> genreBox = new ComboBox<>();
    private final TextField labelField = new TextField();
    private final boolean editMode;
    private final Integer fixedId;
    private final ZonedDateTime creationDate;

    public BandDialog(Window owner, boolean editMode, MusicBand existing) {
        this.editMode = editMode;
        this.fixedId = existing != null ? existing.getId() : null;
        this.creationDate = existing != null ? existing.getCreationDate() : ZonedDateTime.now();
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        Localization loc = Localization.getInstance();
        setTitle(editMode ? loc.get("band.dialog.edit") : loc.get("band.dialog.add"));

        genreBox.getItems().addAll(MusicGenre.values());
        genreBox.setPromptText("null");

        if (existing != null) {
            nameField.setText(existing.getName());
            xField.setText(String.valueOf(existing.getCoordinates().getX()));
            yField.setText(String.valueOf(existing.getCoordinates().getY()));
            participantsField.setText(String.valueOf(existing.getNumberOfParticipants()));
            albumsField.setText(String.valueOf(existing.getAlbumsCount()));
            genreBox.setValue(existing.getGenre());
            labelField.setText(existing.getLabel().getBands() != null ? String.valueOf(existing.getLabel().getBands()) : "");
        }

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(12));
        int row = 0;
        grid.addRow(row++, new javafx.scene.control.Label(loc.get("band.name")), nameField);
        grid.addRow(row++, new javafx.scene.control.Label("X"), xField);
        grid.addRow(row++, new javafx.scene.control.Label("Y"), yField);
        grid.addRow(row++, new javafx.scene.control.Label(loc.get("col.participants")), participantsField);
        grid.addRow(row++, new javafx.scene.control.Label(loc.get("col.albums")), albumsField);
        grid.addRow(row++, new javafx.scene.control.Label(loc.get("col.genre")), genreBox);
        grid.addRow(row, new javafx.scene.control.Label(loc.get("col.label")), labelField);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(btn -> btn == ButtonType.OK ? buildBand() : null);
    }

    private MusicBand buildBand() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(Localization.getInstance().get("error.empty"));
        }
        int x = Integer.parseInt(xField.getText().trim());
        double y = Double.parseDouble(yField.getText().trim());
        long participants = Long.parseLong(participantsField.getText().trim());
        long albums = Long.parseLong(albumsField.getText().trim());
        Coordinates coordinates = new Coordinates(x, y);
        common.dataclasses.Label label = new common.dataclasses.Label(Integer.parseInt(labelField.getText().trim()));
        int id = fixedId != null ? fixedId : 0;
        return new MusicBand(id, name, coordinates, creationDate, participants, albums, genreBox.getValue(), label);
    }

    public static Optional<MusicBand> showCreate(Window owner) {
        BandDialog dialog = new BandDialog(owner, false, null);
        return dialog.showAndWait();
    }

    public static Optional<MusicBand> showEdit(Window owner, MusicBand existing) {
        BandDialog dialog = new BandDialog(owner, true, existing);
        return dialog.showAndWait();
    }
}
