package client.gui;

import common.dataclasses.Coordinates;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicGenre;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Модальный диалог создания и редактирования объекта {@link MusicBand} с проверкой полей на клиенте.
 */
public class BandDialog extends Dialog<MusicBand> {

    private final TextField nameField = new TextField();
    private final TextField xField = new TextField();
    private final TextField yField = new TextField();
    private final TextField participantsField = new TextField();
    private final TextField albumsField = new TextField();
    private final ComboBox<MusicGenre> genreBox = new ComboBox<>();
    private final TextField labelField = new TextField();
    private final Integer fixedId;
    private final ZonedDateTime creationDate;
    private MusicBand validatedBand;

    /**
     * @param owner    родительское окно
     * @param editMode {@code true} — редактирование существующей группы
     * @param existing текущие данные или {@code null} при добавлении
     */
    public BandDialog(Window owner, boolean editMode, MusicBand existing) {
        this(owner, editMode, existing, null);
    }

    /**
     * Полная инициализация диалога; {@code overrideId} задаёт id при update по ключу сервера.
     */
    private BandDialog(Window owner, boolean editMode, MusicBand existing, Integer overrideId) {
        this.fixedId = overrideId != null ? overrideId
                : (existing != null ? existing.getId() : null);
        this.creationDate = existing != null ? existing.getCreationDate() : ZonedDateTime.now();
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        Localization loc = Localization.getInstance();
        setTitle(editMode ? loc.get("band.dialog.edit") : loc.get("band.dialog.add"));
        DialogFormHelper.stripDialogIcon(this);

        genreBox.getItems().addAll(MusicGenre.values());
        genreBox.setPromptText("null");

        if (existing != null) {
            nameField.setText(existing.getName());
            xField.setText(String.valueOf(existing.getCoordinates().getX()));
            yField.setText(String.valueOf(existing.getCoordinates().getY()));
            participantsField.setText(String.valueOf(existing.getNumberOfParticipants()));
            albumsField.setText(String.valueOf(existing.getAlbumsCount()));
            genreBox.setValue(existing.getGenre());
            labelField.setText(existing.getLabel().getBands() != null
                    ? String.valueOf(existing.getLabel().getBands()) : "");
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

        for (TextField field : List.of(nameField, xField, yField, participantsField, albumsField, labelField)) {
            field.textProperty().addListener((o, a, b) ->
                    DialogFormHelper.clear(nameField, xField, yField, participantsField, albumsField, labelField));
        }

        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, this::onOk);

        setResultConverter(btn -> btn == ButtonType.OK ? validatedBand : null);
    }

    private void onOk(ActionEvent event) {
        validatedBand = null;
        Optional<MusicBand> built = tryBuildBand();
        if (built.isEmpty()) {
            event.consume();
        } else {
            validatedBand = built.get();
        }
    }

    private Optional<MusicBand> tryBuildBand() {
        Localization loc = Localization.getInstance();
        DialogFormHelper.clear(nameField, xField, yField, participantsField, albumsField, labelField);

        List<String> messages = new ArrayList<>();
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            DialogFormHelper.markInvalid(nameField, loc.get("error.empty"));
            messages.add(loc.get("error.empty") + " (" + loc.get("band.name") + ")");
        }

        Integer x = parseX(loc, messages);
        Double y = parseY(loc, messages);
        Long participants = parsePositiveLong(participantsField, loc.get("col.participants"), loc, messages);
        Long albums = parsePositiveLong(albumsField, loc.get("col.albums"), loc, messages);
        Integer labelBands = parseLabel(loc, messages);

        if (!messages.isEmpty()) {
            return Optional.empty();
        }

        Coordinates coordinates = new Coordinates(x, y);
        common.dataclasses.Label label = new common.dataclasses.Label(labelBands);
        int id = fixedId != null ? fixedId : 0;
        try {
            return Optional.of(new MusicBand(id, name, coordinates, creationDate,
                    participants, albums, genreBox.getValue(), label));
        } catch (IllegalArgumentException e) {
            DialogFormHelper.markInvalid(nameField, e.getMessage());
            return Optional.empty();
        }
    }

    private Integer parseX(Localization loc, List<String> messages) {
        String raw = xField.getText().trim();
        if (raw.isEmpty()) {
            DialogFormHelper.markInvalid(xField, loc.get("error.coord.empty"));
            messages.add(loc.get("error.coord.empty") + " (X)");
            return 0;
        }
        try {
            int x = Integer.parseInt(raw);
            if (x > 254) {
                DialogFormHelper.markInvalid(xField, loc.get("error.x.max"));
                messages.add(loc.get("error.x.max"));
                return 0;
            }
            return x;
        } catch (NumberFormatException e) {
            DialogFormHelper.markInvalid(xField, loc.get("error.coord.integer"));
            messages.add(loc.get("error.coord.integer") + " (X)");
            return 0;
        }
    }

    private Double parseY(Localization loc, List<String> messages) {
        String raw = yField.getText().trim();
        if (raw.isEmpty()) {
            DialogFormHelper.markInvalid(yField, loc.get("error.coord.empty"));
            messages.add(loc.get("error.coord.empty") + " (Y)");
            return 0.0;
        }
        try {
            double y = Double.parseDouble(raw);
            if (y > 93) {
                DialogFormHelper.markInvalid(yField, loc.get("error.y.max"));
                messages.add(loc.get("error.y.max"));
                return 0.0;
            }
            return y;
        } catch (NumberFormatException e) {
            DialogFormHelper.markInvalid(yField, loc.get("error.coord.number"));
            messages.add(loc.get("error.coord.number") + " (Y)");
            return 0.0;
        }
    }

    private Long parsePositiveLong(TextField field, String fieldName, Localization loc, List<String> messages) {
        String raw = field.getText().trim();
        if (raw.isEmpty()) {
            DialogFormHelper.markInvalid(field, loc.get("error.number.empty"));
            messages.add(loc.get("error.number.empty") + " (" + fieldName + ")");
            return 0L;
        }
        try {
            long value = Long.parseLong(raw);
            if (value <= 0) {
                DialogFormHelper.markInvalid(field, loc.get("error.number.positive"));
                messages.add(loc.get("error.number.positive") + " (" + fieldName + ")");
                return 0L;
            }
            return value;
        } catch (NumberFormatException e) {
            DialogFormHelper.markInvalid(field, loc.get("error.number.format"));
            messages.add(loc.get("error.number.format") + " (" + fieldName + ")");
            return 0L;
        }
    }

    private Integer parseLabel(Localization loc, List<String> messages) {
        String raw = labelField.getText().trim();
        if (raw.isEmpty()) {
            DialogFormHelper.markInvalid(labelField, loc.get("error.invalid_label"));
            messages.add(loc.get("error.invalid_label"));
            return 0;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            DialogFormHelper.markInvalid(labelField, loc.get("error.invalid_label"));
            messages.add(loc.get("error.invalid_label"));
            return 0;
        }
    }

    /**
     * Открывает диалог добавления новой группы.
     *
     * @param owner родительское окно
     * @return созданный объект или пусто при отмене
     */
    public static Optional<MusicBand> showCreate(Window owner) {
        BandDialog dialog = new BandDialog(owner, false, null);
        return dialog.showAndWait();
    }

    /**
     * Открывает диалог редактирования с сохранением идентификатора из {@code existing}.
     *
     * @param owner    родительское окно
     * @param existing редактируемая группа
     * @return обновлённый объект или пусто при отмене
     */
    public static Optional<MusicBand> showEdit(Window owner, MusicBand existing) {
        BandDialog dialog = new BandDialog(owner, true, existing);
        return dialog.showAndWait();
    }

    /**
     * Открывает диалог редактирования с явно заданным идентификатором (команда update).
     *
     * @param owner    родительское окно
     * @param existing текущие поля группы
     * @param id       идентификатор на сервере
     * @return обновлённый объект или пусто при отмене
     */
    public static Optional<MusicBand> showEdit(Window owner, MusicBand existing, int id) {
        BandDialog dialog = new BandDialog(owner, true, existing, id);
        return dialog.showAndWait();
    }
}
