package client.gui;

import common.Response;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;
import common.dataclasses.MusicBandEntry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Main Lab 8 window: table, commands, canvas, i18n, background sync.
 */
public class MainView {

    private final Session session;
    private final ClientService service = new ClientService();
    private final Localization loc = Localization.getInstance();

    private Stage stage;
    private TableView<MusicBandEntry> table;
    private TextArea logArea;
    private CanvasPanel canvas;
    private Label userLabel;
    private TextField filterField;
    private ComboBox<String> sortColumnBox;
    private CheckBox sortAscBox;
    private ComboBox<Locale> languageBox;
    private TextField infoField;

    private List<MusicBandEntry> rawEntries = new ArrayList<>();
    private Timeline pollTimeline;

    public MainView(Session session) {
        this.session = session;
    }

    public void show() {
        stage = new Stage();
        BorderPane root = new BorderPane();
        root.setTop(buildTopBar());
        root.setCenter(buildCenter());
        root.setBottom(buildLog());
        root.setRight(buildCanvasPane());

        Scene scene = new Scene(root, 1180, 720);
        stage.setScene(scene);
        stage.setTitle(loc.get("main.title"));
        stage.setOnCloseRequest(e -> {
            stopPolling();
            try {
                session.getNetworkManager().close();
            } catch (Exception ignored) {
            }
            Platform.exit();
        });
        applyLocalization();
        stage.show();
        startPolling();
        refreshCollection(true);
    }

    private HBox buildTopBar() {
        userLabel = new Label();
        languageBox = new ComboBox<>(FXCollections.observableArrayList(Localization.SUPPORTED));
        languageBox.setValue(loc.getLocale());
        languageBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.displayName(item));
            }
        });
        languageBox.setButtonCell(languageBox.getCellFactory().call(null));
        languageBox.valueProperty().addListener((o, a, b) -> {
            if (b != null) {
                loc.setLocale(b);
                canvas.setLocale(b);
                applyLocalization();
                applyTableFilterSort();
            }
        });

        Button refreshBtn = new Button();
        refreshBtn.setOnAction(e -> refreshCollection(false));
        refreshBtn.setId("btnRefresh");

        HBox bar = new HBox(12, new Label(), userLabel, new Separator(Orientation.VERTICAL),
                new Label(), languageBox, refreshBtn);
        bar.setPadding(new Insets(8));
        bar.setId("topBar");
        return bar;
    }

    private SplitPane buildCenter() {
        VBox left = new VBox(8);
        left.setPadding(new Insets(8));
        left.getChildren().addAll(buildFilterRow(), buildCommandBar(), buildTable());
        VBox.setVgrow(left.getChildren().get(2), Priority.ALWAYS);

        SplitPane split = new SplitPane(left);
        split.setOrientation(Orientation.HORIZONTAL);
        return split;
    }

    private HBox buildFilterRow() {
        filterField = new TextField();
        filterField.textProperty().addListener((o, a, b) -> applyTableFilterSort());
        sortColumnBox = new ComboBox<>();
        sortAscBox = new CheckBox();
        sortAscBox.setSelected(true);
        sortAscBox.selectedProperty().addListener((o, a, b) -> applyTableFilterSort());
        sortColumnBox.valueProperty().addListener((o, a, b) -> applyTableFilterSort());

        HBox row = new HBox(8);
        row.getChildren().addAll(new Label(), filterField, new Label(), sortColumnBox, sortAscBox);
        row.setId("filterRow");
        return row;
    }

    private FlowPane buildCommandBar() {
        FlowPane pane = new FlowPane(6, 6);
        pane.getChildren().addAll(
                cmdButton("cmd.help", CommandType.HELP, null),
                cmdButton("cmd.info", CommandType.INFO, null),
                cmdButton("cmd.show", CommandType.SHOW, null),
                cmdButton("main.add", null, this::onAdd),
                cmdButton("main.edit", null, this::onEdit),
                fieldEditButton(),
                cmdButton("main.delete", null, this::onDelete),
                cmdButton("cmd.clear", CommandType.CLEAR, null),
                cmdButton("cmd.script", CommandType.EXECUTE_SCRIPT, this::onScript),
                cmdButton("cmd.count", CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS, this::onCountParticipants),
                cmdButton("cmd.filter_label", CommandType.FILTER_LESS_THEN_LABEL, this::onFilterLabel),
                cmdButton("cmd.print_labels", CommandType.PRINT_FIELD_DESCENDING_LABEL, null),
                cmdButton("cmd.remove_lower", CommandType.REMOVE_LOWER, this::onRemoveLower),
                cmdButton("cmd.replace_greater", CommandType.REPLACE_IF_GREATER, this::onReplaceGreater),
                cmdButton("cmd.replace_lower", CommandType.REPLACE_IF_LOWER, this::onReplaceLower)
        );
        pane.setId("commandBar");
        return pane;
    }

    private Button fieldEditButton() {
        Button b = new Button();
        b.setUserData("main.editField");
        b.setOnAction(e -> onEditField());
        return b;
    }

    private void onEditField() {
        MusicBandEntry entry = selected();
        if (entry == null || !isOwner(entry)) {
            appendLog("Field edit denied");
            return;
        }
        FieldEditDialog.show(stage, entry.getMusicBand()).ifPresent(b ->
                runSimpleCommand(CommandType.UPDATE, String.valueOf(b.getId()), b));
    }

    private Button cmdButton(String key, CommandType type, Runnable custom) {
        Button b = new Button();
        b.setUserData(key);
        b.setOnAction(e -> {
            if (custom != null) {
                custom.run();
            } else {
                runSimpleCommand(type, null, null);
            }
        });
        return b;
    }

    @SuppressWarnings("unchecked")
    private TableView<MusicBandEntry> buildTable() {
        table = new TableView<>();
        table.getColumns().add(col("col.key", e -> String.valueOf(e.getBandKey())));
        table.getColumns().add(col("col.id", e -> String.valueOf(e.getMusicBand().getId())));
        table.getColumns().add(col("col.name", e -> e.getMusicBand().getName()));
        table.getColumns().add(col("col.x", e -> Formats.formatNumber(loc.getLocale(), e.getMusicBand().getCoordinates().getX())));
        table.getColumns().add(col("col.y", e -> Formats.formatNumber(loc.getLocale(), e.getMusicBand().getCoordinates().getY())));
        table.getColumns().add(col("col.creation", e -> Formats.formatDateTime(loc.getLocale(), e.getMusicBand().getCreationDate())));
        table.getColumns().add(col("col.participants", e -> Formats.formatNumber(loc.getLocale(), e.getMusicBand().getNumberOfParticipants())));
        table.getColumns().add(col("col.albums", e -> Formats.formatNumber(loc.getLocale(), e.getMusicBand().getAlbumsCount())));
        table.getColumns().add(col("col.genre", e -> e.getMusicBand().getGenre() != null ? e.getMusicBand().getGenre().name() : ""));
        table.getColumns().add(col("col.label", e -> e.getMusicBand().getLabel().getBands() != null ? String.valueOf(e.getMusicBand().getLabel().getBands()) : ""));
        table.getColumns().add(col("col.owner", e -> String.valueOf(e.getOwnerId())));
        table.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> {
            if (b != null) {
                infoField.setText(canvas.infoText(b));
            }
        });
        return table;
    }

    private TableColumn<MusicBandEntry, String> col(String key, java.util.function.Function<MusicBandEntry, String> fn) {
        TableColumn<MusicBandEntry, String> c = new TableColumn<>();
        c.setUserData(key);
        c.setCellValueFactory(cd -> new SimpleStringProperty(fn.apply(cd.getValue())));
        c.setPrefWidth(90);
        return c;
    }

    private VBox buildCanvasPane() {
        canvas = new CanvasPanel();
        canvas.setOnSelect(entry -> {
            table.getSelectionModel().select(entry);
            infoField.setText(canvas.infoText(entry));
        });
        infoField = new TextField();
        infoField.setEditable(false);
        VBox box = new VBox(8, new Label(), canvas, infoField);
        box.setPadding(new Insets(8));
        box.setPrefWidth(540);
        box.setId("canvasPane");
        return box;
    }

    private TextArea buildLog() {
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(4);
        VBox.setVgrow(logArea, Priority.NEVER);
        return logArea;
    }

    private void startPolling() {
        pollTimeline = new Timeline(new KeyFrame(Duration.seconds(2.5), e -> refreshCollection(true)));
        pollTimeline.setCycleCount(Timeline.INDEFINITE);
        pollTimeline.play();
    }

    private void stopPolling() {
        if (pollTimeline != null) {
            pollTimeline.stop();
        }
    }

    private void refreshCollection(boolean background) {
        service.fetchCollection(session).whenComplete((list, err) -> Platform.runLater(() -> {
            if (err != null) {
                if (!background) {
                    appendLog(loc.get("error.network") + ": " + err.getMessage());
                }
                return;
            }
            rawEntries = new ArrayList<>(list);
            applyTableFilterSort();
            canvas.setEntries(list, !background);
        }));
    }

    private void applyTableFilterSort() {
        List<MusicBandEntry> view = CollectionUtils.filterAndSort(
                rawEntries,
                filterField.getText(),
                sortColumnBox.getValue(),
                sortAscBox.isSelected(),
                loc.getLocale());
        table.setItems(FXCollections.observableArrayList(view));
        canvas.setEntries(view, false);
    }

    private MusicBandEntry selected() {
        return table.getSelectionModel().getSelectedItem();
    }

    private boolean isOwner(MusicBandEntry entry) {
        return entry != null && entry.getOwnerId() == session.getUserId();
    }

    private void onAdd() {
        TextInputDialog keyDialog = new TextInputDialog();
        keyDialog.setHeaderText(loc.get("band.key"));
        String key = keyDialog.showAndWait().orElse("").trim();
        Optional<MusicBand> band = BandDialog.showCreate(stage);
        band.ifPresent(b -> runSimpleCommand(CommandType.INSERT, key.isEmpty() ? "0" : key, b));
    }

    private void onEdit() {
        MusicBandEntry entry = selected();
        if (entry == null) {
            return;
        }
        if (!isOwner(entry)) {
            appendLog("Edit denied: not owner");
            return;
        }
        Optional<MusicBand> band = BandDialog.showEdit(stage, entry.getMusicBand());
        band.ifPresent(b -> runSimpleCommand(CommandType.UPDATE, String.valueOf(b.getId()), b));
    }

    private void onDelete() {
        MusicBandEntry entry = selected();
        if (entry == null) {
            return;
        }
        if (!isOwner(entry)) {
            appendLog("Delete denied: not owner");
            return;
        }
        runSimpleCommand(CommandType.REMOVE_KEY, String.valueOf(entry.getBandKey()), null);
    }

    private void onScript() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            runSimpleCommand(CommandType.EXECUTE_SCRIPT, file.getAbsolutePath(), null);
        }
    }

    private void onCountParticipants() {
        TextInputDialog d = new TextInputDialog();
        d.showAndWait().ifPresent(v -> runSimpleCommand(CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS, v.trim(), null));
    }

    private void onFilterLabel() {
        TextInputDialog d = new TextInputDialog();
        d.showAndWait().ifPresent(v -> runSimpleCommand(CommandType.FILTER_LESS_THEN_LABEL, v.trim(), null));
    }

    private void onRemoveLower() {
        BandDialog.showCreate(stage).ifPresent(b ->
                runSimpleCommand(CommandType.REMOVE_LOWER, null, b));
    }

    private void onReplaceGreater() {
        MusicBandEntry entry = selected();
        if (entry == null) {
            return;
        }
        BandDialog.showEdit(stage, entry.getMusicBand()).ifPresent(b ->
                runSimpleCommand(CommandType.REPLACE_IF_GREATER, String.valueOf(entry.getBandKey()), b));
    }

    private void onReplaceLower() {
        MusicBandEntry entry = selected();
        if (entry == null) {
            return;
        }
        BandDialog.showEdit(stage, entry.getMusicBand()).ifPresent(b ->
                runSimpleCommand(CommandType.REPLACE_IF_LOWER, String.valueOf(entry.getBandKey()), b));
    }

    private void runSimpleCommand(CommandType type, String arg, MusicBand band) {
        service.runCommand(session, type, arg, band).whenComplete((response, err) -> Platform.runLater(() -> {
            if (err != null) {
                appendLog(loc.get("error.network") + ": " + err.getMessage());
                return;
            }
            handleResponse(response);
            refreshCollection(false);
        }));
    }

    private void handleResponse(Response response) {
        if (response == null) {
            return;
        }
        appendLog(response.getMessage());
        if (response.getData() != null) {
            appendLog(response.getData().toString());
        }
    }

    private void appendLog(String line) {
        logArea.appendText(line + "\n");
    }

    private void applyLocalization() {
        stage.setTitle(loc.get("main.title"));
        userLabel.setText(loc.get("main.user") + ": " + session.getUsername());

        HBox top = (HBox) ((BorderPane) stage.getScene().getRoot()).getTop();
        ((Label) top.getChildren().get(0)).setText(loc.get("main.language") + ":");
        ((Button) top.getChildren().get(5)).setText(loc.get("main.refresh"));

        HBox filterRow = (HBox) ((VBox) ((SplitPane) ((BorderPane) stage.getScene().getRoot()).getCenter()).getItems().get(0)).getChildren().get(0);
        ((Label) filterRow.getChildren().get(0)).setText(loc.get("main.filter") + ":");
        ((Label) filterRow.getChildren().get(2)).setText(loc.get("main.sort.column") + ":");
        sortAscBox.setText(loc.get("main.sort.asc"));

        sortColumnBox.getItems().setAll(
                "col.key", "col.id", "col.name", "col.x", "col.y", "col.creation",
                "col.participants", "col.albums", "col.genre", "col.label", "col.owner");
        if (sortColumnBox.getValue() == null) {
            sortColumnBox.setValue("col.key");
        }

        FlowPane cmd = (FlowPane) ((VBox) ((SplitPane) ((BorderPane) stage.getScene().getRoot()).getCenter()).getItems().get(0)).getChildren().get(1);
        for (javafx.scene.Node node : cmd.getChildren()) {
            if (node instanceof Button b && b.getUserData() instanceof String key) {
                b.setText(loc.get(key));
            }
        }

        for (TableColumn<?, ?> c : table.getColumns()) {
            if (c.getUserData() instanceof String key) {
                c.setText(loc.get(key));
            }
        }

        VBox canvasPane = (VBox) ((BorderPane) stage.getScene().getRoot()).getRight();
        ((Label) canvasPane.getChildren().get(0)).setText(loc.get("main.canvas"));
        applyTableFilterSort();
    }
}
