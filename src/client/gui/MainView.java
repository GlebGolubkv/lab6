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
import javafx.scene.Cursor;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Главное окно приложения: таблица коллекции, визуализация, журнал команд и панель действий.
 */
public class MainView {

    private static final List<String> COLUMN_KEYS = List.of(
            "col.key", "col.id", "col.name", "col.x", "col.y", "col.creation",
            "col.participants", "col.albums", "col.genre", "col.label", "col.owner");

    private final Session session;
    private final ClientService service = new ClientService();
    private final Localization loc = Localization.getInstance();

    private Stage stage;
    private TableView<MusicBandEntry> table;
    private TableColumn<MusicBandEntry, Void> rowNumberColumn;
    private TextArea logArea;
    private CanvasPanel canvas;
    private Label userLabel;
    private TextField filterField;
    private ComboBox<String> filterColumnBox;
    private Button resetSortButton;
    private ComboBox<Locale> languageBox;
    private final Map<String, TableColumn<MusicBandEntry, String>> columnsByKey = new LinkedHashMap<>();
    private final List<CollectionUtils.SortLevel> sortLevels = new ArrayList<>();
    private boolean sortActive;
    private Label sortSummaryLabel;
    private Label canvasTitleLabel;
    private Label languageCaptionLabel;
    private Label logCaptionLabel;
    private VBox commandColumn;

    private List<MusicBandEntry> rawEntries = new ArrayList<>();
    private MusicBandEntry lastSelectedEntry;
    private Timeline pollTimeline;

    /**
     * @param session авторизованная сессия после {@link AuthView}
     */
    public MainView(Session session) {
        this.session = session;
    }

    /**
     * Строит интерфейс, запускает опрос коллекции и отображает окно.
     */
    public void show() {
        stage = new Stage();
        BorderPane root = new BorderPane();
        root.setTop(buildTopBar());
        root.setCenter(buildMainArea());
        root.setRight(buildCommandColumn());

        Scene scene = new Scene(root, 1280, 760);
        stage.setScene(scene);
        stage.setTitle(loc.get("main.title"));
        stage.setOnCloseRequest(e -> onExit());

        wireLanguageSelector();
        applyLocalization();
        stage.show();
        startPolling();
        refreshCollection(true);
    }

    private void wireLanguageSelector() {
        languageBox.setCellFactory(cb -> new ListCell<>() {
            /**
             * Показывает локализованное название языка в списке выбора.
             */
            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.displayName(item));
            }
        });
        languageBox.setButtonCell(new ListCell<>() {
            /**
             * Показывает выбранный язык на кнопке комбобокса.
             */
            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.displayName(item));
            }
        });
        languageBox.valueProperty().addListener((o, a, b) -> {
            if (b != null) {
                loc.setLocale(b);
                applyLocalization();
                applyTableFilterSort();
            }
        });
        languageBox.setValue(loc.getLocale());
    }

    private HBox buildTopBar() {
        userLabel = new Label();
        languageCaptionLabel = new Label();
        languageBox = new ComboBox<>(FXCollections.observableArrayList(Localization.SUPPORTED));

        HBox bar = new HBox(16, userLabel, new Separator(Orientation.VERTICAL),
                languageCaptionLabel, languageBox);
        bar.setPadding(new Insets(8));
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    
    private VBox buildMainArea() {
        canvas = new CanvasPanel();
        canvas.setOnSelect(entry -> table.getSelectionModel().select(entry));
        canvas.setOnDoubleSelect(this::openEditDialogForEntry);

        canvasTitleLabel = new Label();
        VBox canvasBlock = new VBox(6, canvasTitleLabel, canvas);
        VBox.setVgrow(canvas, Priority.ALWAYS);
        canvas.setMinHeight(200);
        canvas.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        sortSummaryLabel = new Label();
        sortSummaryLabel.setWrapText(true);
        sortSummaryLabel.setStyle("-fx-text-fill: #444; -fx-font-size: 12px;");
        VBox tableBlock = new VBox(8, buildFilterRow(), sortSummaryLabel, buildTable());
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setMinHeight(160);

        VBox logBlock = buildLog();
        VBox.setVgrow(logBlock, Priority.NEVER);

        VBox area = new VBox(12, canvasBlock, tableBlock, logBlock);
        area.setPadding(new Insets(8));
        VBox.setVgrow(canvasBlock, Priority.ALWAYS);
        VBox.setVgrow(tableBlock, Priority.SOMETIMES);
        return area;
    }

    private VBox buildCommandColumn() {
        commandColumn = new VBox(6);
        commandColumn.setPadding(new Insets(8));
        commandColumn.setAlignment(Pos.TOP_CENTER);
        commandColumn.setPrefWidth(200);
        commandColumn.setMinWidth(180);
        commandColumn.setFillWidth(true);

        commandColumn.getChildren().addAll(
                labCommand(CommandType.HELP),
                labCommand(CommandType.INFO),
                labCommand(CommandType.SHOW),
                labCommand(CommandType.INSERT, this::onAdd),
                labCommand(CommandType.UPDATE, this::onEdit),
                labCommand(CommandType.REMOVE_KEY, this::onDelete),
                labCommand(CommandType.CLEAR),
                labCommand(CommandType.EXECUTE_SCRIPT, this::onScript),
                labCommand(CommandType.REMOVE_LOWER, this::onRemoveLower),
                labCommand(CommandType.REPLACE_IF_GREATER, this::onReplaceGreater),
                labCommand(CommandType.REPLACE_IF_LOWER, this::onReplaceLower),
                labCommand(CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS, this::onCountParticipants),
                labCommand(CommandType.FILTER_LESS_THEN_LABEL, this::onFilterLabel),
                labCommand(CommandType.PRINT_FIELD_DESCENDING_LABEL),
                exitButton()
        );

        for (javafx.scene.Node node : commandColumn.getChildren()) {
            if (node instanceof Button b) {
                b.setMaxWidth(Double.MAX_VALUE);
            }
        }
        return commandColumn;
    }

    private Button labCommand(CommandType type) {
        return labCommand(type, null);
    }

    private Button labCommand(CommandType type, Runnable action) {
        Button b = new Button(loc.commandLabel(type));
        b.setMnemonicParsing(false);
        b.setUserData(type);
        b.setFocusTraversable(false);
        b.setOnAction(e -> {
            if (action != null) {
                action.run();
            } else {
                onCommand(type);
            }
        });
        return b;
    }

    private void onCommand(CommandType type) {
        switch (type) {
            case HELP, INFO, SHOW, PRINT_FIELD_DESCENDING_LABEL -> runSimpleCommand(type, null, null);
            case CLEAR -> {
                if (InputDialogs.confirm(stage, type.getCommandName(), loc.get("prompt.clear.confirm"))) {
                    runSimpleCommand(type, null, null);
                }
            }
            default -> {
            }
        }
    }

    private Button exitButton() {
        Button b = new Button(loc.get("exit"));
        b.setUserData("cmd.exit");
        b.setMnemonicParsing(false);
        b.setStyle("-fx-background-color: #ffc8c8; -fx-text-fill: #4a1515;");
        b.setOnAction(e -> onExit());
        return b;
    }

    private HBox buildFilterRow() {
        filterField = new TextField();
        filterField.textProperty().addListener((o, a, b) -> applyTableFilterSort());

        filterColumnBox = new ComboBox<>();
        wireFilterColumnSelector();
        filterColumnBox.getItems().setAll(COLUMN_KEYS);
        filterColumnBox.setValue("col.key");
        filterColumnBox.valueProperty().addListener((o, a, b) -> applyTableFilterSort());

        Label filterLabel = new Label();
        Label filterColumnLabel = new Label();
        filterLabel.setId("filterLabel");
        filterColumnLabel.setId("filterColumnLabel");

        resetSortButton = new Button();
        resetSortButton.setId("resetSortButton");
        resetSortButton.setOnAction(e -> onResetSort());

        HBox row = new HBox(8, filterLabel, filterField, filterColumnLabel, filterColumnBox, resetSortButton);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void onResetSort() {
        sortActive = false;
        sortLevels.clear();
        refreshSortUi();
        applyTableFilterSort();
    }

    private void wireFilterColumnSelector() {
        filterColumnBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.get(item));
            }
        });
        filterColumnBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.get(item));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private TableView<MusicBandEntry> buildTable() {
        table = new TableView<>();
        columnsByKey.clear();
        table.getColumns().add(buildRowNumberColumn());
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
                lastSelectedEntry = b;
            }
            if (canvas != null) {
                canvas.setSelectedEntry(b);
            }
        });
        table.setRowFactory(tv -> {
            TableRow<MusicBandEntry> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openEditDialogForEntry(row.getItem());
                }
            });
            return row;
        });
        return table;
    }

    private TableColumn<MusicBandEntry, Void> buildRowNumberColumn() {
        TableColumn<MusicBandEntry, Void> column = new TableColumn<>();
        column.setUserData("col.row");
        column.setSortable(false);
        column.setReorderable(false);
        column.setResizable(false);
        column.setPrefWidth(48);
        column.setMinWidth(40);
        column.setMaxWidth(56);
        column.setText(loc.get("col.row"));
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(cd -> null);
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
                setStyle("-fx-alignment: CENTER-RIGHT; -fx-text-fill: #888;");
            }
        });
        rowNumberColumn = column;
        return column;
    }

    private TableColumn<MusicBandEntry, String> col(String key, java.util.function.Function<MusicBandEntry, String> fn) {
        TableColumn<MusicBandEntry, String> c = new TableColumn<>();
        c.setUserData(key);
        c.setSortable(false);
        c.setGraphic(buildColumnHeader(key));
        c.setCellValueFactory(cd -> new SimpleStringProperty(fn.apply(cd.getValue())));
        c.setPrefWidth(90);
        columnsByKey.put(key, c);
        return c;
    }

    private HBox buildColumnHeader(String columnKey) {
        Label title = new Label();
        title.setId("col-title");
        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setCursor(Cursor.HAND);
        header.setUserData(columnKey);
        header.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                onColumnHeaderClicked(columnKey);
            }
        });
        return header;
    }

    private void onColumnHeaderClicked(String columnKey) {
        int index = indexOfSortLevel(columnKey);
        if (index >= 0) {
            CollectionUtils.SortLevel current = sortLevels.get(index);
            sortLevels.set(index, new CollectionUtils.SortLevel(columnKey, !current.ascending()));
        } else {
            sortLevels.add(new CollectionUtils.SortLevel(columnKey, true));
        }
        sortActive = true;
        applyTableFilterSort();
    }

    private void refreshColumnHeaders() {
        for (Map.Entry<String, TableColumn<MusicBandEntry, String>> e : columnsByKey.entrySet()) {
            String key = e.getKey();
            if (!(e.getValue().getGraphic() instanceof HBox header)) {
                continue;
            }
            int levelIndex = indexOfSortLevel(key);
            boolean active = sortActive && levelIndex >= 0;
            for (javafx.scene.Node child : header.getChildren()) {
                if (child instanceof Label label && "col-title".equals(label.getId())) {
                    String text = loc.get(key);
                    if (active) {
                        CollectionUtils.SortLevel level = sortLevels.get(levelIndex);
                        String arrow = level.ascending() ? "\u2191" : "\u2193";
                        text = text + " " + (levelIndex + 1) + arrow;
                    }
                    label.setText(text);
                    label.setStyle(active
                            ? "-fx-font-weight: bold; -fx-text-fill: #1565c0;"
                            : "-fx-font-weight: normal; -fx-text-fill: -fx-text-base-color;");
                }
            }
        }
    }

    private int indexOfSortLevel(String columnKey) {
        for (int i = 0; i < sortLevels.size(); i++) {
            if (sortLevels.get(i).columnKey().equals(columnKey)) {
                return i;
            }
        }
        return -1;
    }

    private void refreshSortUi() {
        refreshColumnHeaders();
        refreshSortSummary();
    }

    private void refreshSortSummary() {
        if (sortSummaryLabel == null) {
            return;
        }
        if (!sortActive || sortLevels.isEmpty()) {
            sortSummaryLabel.setText(loc.get("main.sort.none"));
            return;
        }
        StringBuilder text = new StringBuilder(loc.get("main.sort.by")).append(" ");
        for (int i = 0; i < sortLevels.size(); i++) {
            if (i > 0) {
                text.append(loc.get("main.sort.then")).append(" ");
            }
            CollectionUtils.SortLevel level = sortLevels.get(i);
            String arrow = level.ascending() ? "\u2191" : "\u2193";
            text.append(loc.get(level.columnKey())).append(" ").append(arrow);
        }
        sortSummaryLabel.setText(text.toString());
    }

    private VBox buildLog() {
        logCaptionLabel = new Label();
        logCaptionLabel.setWrapText(true);
        logCaptionLabel.setStyle("-fx-text-fill: #666;");
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefRowCount(9);
        logArea.setMinHeight(180);
        logArea.setMaxHeight(300);
        logArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        VBox box = new VBox(4, logCaptionLabel, logArea);
        box.setPadding(new Insets(0, 8, 8, 8));
        return box;
    }

    private void onExit() {
        stopPolling();
        try {
            session.getNetworkManager().close();
        } catch (Exception ignored) {
        }
        if (stage != null) {
            stage.close();
        }
        Platform.exit();
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
        }));
    }

    private void applyTableFilterSort() {
        if (table == null || filterField == null || filterColumnBox == null) {
            return;
        }
        List<MusicBandEntry> view = CollectionUtils.filterAndSort(
                rawEntries,
                filterField.getText(),
                filterColumnBox.getValue(),
                List.copyOf(sortLevels),
                sortActive,
                loc.getLocale());
        table.setItems(FXCollections.observableArrayList(view));
        refreshSortUi();
        if (canvas != null) {
            canvas.setEntries(view, false);
        }
        syncSelectionAfterFilter(view);
    }

    private void syncSelectionAfterFilter(List<MusicBandEntry> visible) {
        MusicBandEntry hint = selectionHint();
        if (hint == null) {
            table.getSelectionModel().clearSelection();
            if (canvas != null) {
                canvas.setSelectedEntry(null);
            }
            return;
        }
        MusicBandEntry stillVisible = visible.stream()
                .filter(e -> e.getBandKey() == hint.getBandKey())
                .findFirst()
                .orElse(null);
        if (stillVisible != null) {
            table.getSelectionModel().select(stillVisible);
            if (canvas != null) {
                canvas.setSelectedEntry(stillVisible);
            }
        } else {
            table.getSelectionModel().clearSelection();
            if (canvas != null) {
                canvas.setSelectedEntry(null);
            }
        }
    }

    private MusicBandEntry selected() {
        return table.getSelectionModel().getSelectedItem();
    }

    
    private MusicBandEntry selectionHint() {
        MusicBandEntry current = selected();
        return current != null ? current : lastSelectedEntry;
    }

    private boolean isOwner(MusicBandEntry entry) {
        return entry != null && entry.getOwnerId() == session.getUserId();
    }

    /**
     * Редактирование по двойному клику: только свои объекты, без сообщения об ошибке.
     */
    private void openEditDialogForEntry(MusicBandEntry entry) {
        if (entry == null || !isOwner(entry)) {
            return;
        }
        lastSelectedEntry = entry;
        table.getSelectionModel().select(entry);
        if (canvas != null) {
            canvas.setSelectedEntry(entry);
        }
        BandDialog.showEdit(stage, entry.getMusicBand()).ifPresent(b ->
                runSimpleCommand(CommandType.UPDATE, String.valueOf(entry.getMusicBand().getId()), b));
    }

    private void onAdd() {
        MusicBandEntry sel = selectionHint();
        String defaultKey = sel != null ? String.valueOf(sel.getBandKey()) : "";
        Optional<String> key = InputDialogs.prompt(stage, CommandType.INSERT.getCommandName(),
                loc.get("prompt.insert.key"), defaultKey, true);
        if (key.isEmpty()) {
            return;
        }
        BandDialog.showCreate(stage).ifPresent(b ->
                runSimpleCommand(CommandType.INSERT, key.get().isEmpty() ? "0" : key.get(), b));
    }

    private void onEdit() {
        MusicBandEntry sel = selectionHint();
        String defaultId = sel != null ? String.valueOf(sel.getMusicBand().getId()) : "";
        Optional<String> idText = InputDialogs.prompt(stage, CommandType.UPDATE.getCommandName(),
                loc.get("prompt.update.id"), defaultId, false);
        if (idText.isEmpty()) {
            return;
        }
        int id = parseIntOrWarn(idText.get(), loc.get("error.invalid_id"));
        if (id < 0) {
            return;
        }
        MusicBandEntry entry = findEntryById(id).orElse(sel);
        if (entry != null && !isOwner(entry)) {
            InputDialogs.info(stage, loc.get("error.title"), loc.get("error.not_owner"));
            return;
        }
        MusicBand template = entry != null ? entry.getMusicBand() : null;
        Optional<MusicBand> band = template != null
                ? BandDialog.showEdit(stage, template)
                : BandDialog.showEdit(stage, null, id);
        band.ifPresent(b -> runSimpleCommand(CommandType.UPDATE, String.valueOf(id), b));
    }

    private void onDelete() {
        MusicBandEntry sel = selectionHint();
        String defaultKey = sel != null ? String.valueOf(sel.getBandKey()) : "";
        Optional<String> keyText = InputDialogs.prompt(stage, CommandType.REMOVE_KEY.getCommandName(),
                loc.get("prompt.remove_key"), defaultKey, false);
        if (keyText.isEmpty()) {
            return;
        }
        int key = parseIntOrWarn(keyText.get(), loc.get("error.invalid_key"));
        if (key < 0) {
            return;
        }
        MusicBandEntry entry = findEntryByKey(key).orElse(sel);
        if (entry != null && !isOwner(entry)) {
            InputDialogs.info(stage, loc.get("error.title"), loc.get("error.not_owner"));
            return;
        }
        runSimpleCommand(CommandType.REMOVE_KEY, String.valueOf(key), null);
    }

    private void onScript() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(CommandType.EXECUTE_SCRIPT.getCommandName());
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            runSimpleCommand(CommandType.EXECUTE_SCRIPT, file.getAbsolutePath(), null);
        }
    }

    private void onCountParticipants() {
        CommandType type = CommandType.COUNT_BY_NUMBER_OF_PARTICIPANTS;
        InputDialogs.promptCommandResult(stage, type.getCommandName(),
                loc.get("prompt.count.participants"), "",
                value -> runCommandForDialog(type, value, null));
    }

    private void onFilterLabel() {
        MusicBandEntry sel = selectionHint();
        String defaultLabel = sel != null && sel.getMusicBand().getLabel().getBands() != null
                ? String.valueOf(sel.getMusicBand().getLabel().getBands()) : "";
        CommandType type = CommandType.FILTER_LESS_THEN_LABEL;
        InputDialogs.promptCommandResult(stage, type.getCommandName(),
                loc.get("prompt.filter.label"), defaultLabel,
                value -> runCommandForDialog(type, value, null));
    }

    private void onRemoveLower() {
        BandDialog.showCreate(stage).ifPresent(b ->
                runSimpleCommand(CommandType.REMOVE_LOWER, null, b));
    }

    private void onReplaceGreater() {
        promptReplace(CommandType.REPLACE_IF_GREATER);
    }

    private void onReplaceLower() {
        promptReplace(CommandType.REPLACE_IF_LOWER);
    }

    private void promptReplace(CommandType type) {
        MusicBandEntry hint = selectionHint();
        String defaultKey = hint != null ? String.valueOf(hint.getBandKey()) : "";
        Optional<String> keyText = InputDialogs.prompt(stage, type.getCommandName(),
                loc.get("prompt.replace.key"), defaultKey, false);
        if (keyText.isEmpty()) {
            return;
        }
        int key = parseIntOrWarn(keyText.get(), loc.get("error.invalid_key"));
        if (key < 0) {
            return;
        }
        MusicBandEntry entry = findEntryByKey(key).orElse(hint);
        if (entry == null) {
            InputDialogs.info(stage, loc.get("error.title"), loc.get("error.invalid_key"));
            return;
        }
        if (!isOwner(entry)) {
            InputDialogs.info(stage, loc.get("error.title"), loc.get("error.not_owner"));
            return;
        }
        BandDialog.showEdit(stage, entry.getMusicBand()).ifPresent(b ->
                runSimpleCommand(type, String.valueOf(key), b));
    }

    private Optional<MusicBandEntry> findEntryByKey(int key) {
        return rawEntries.stream().filter(e -> e.getBandKey() == key).findFirst();
    }

    private Optional<MusicBandEntry> findEntryById(int id) {
        return rawEntries.stream().filter(e -> e.getMusicBand().getId() == id).findFirst();
    }

    private int parseIntOrWarn(String text, String errorMessage) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            InputDialogs.info(stage, loc.get("error.title"), errorMessage);
            return -1;
        }
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

    private CompletableFuture<String> runCommandForDialog(CommandType type, String arg, MusicBand band) {
        return service.runCommand(session, type, arg, band).handle((response, err) -> {
            Platform.runLater(() -> {
                if (err == null) {
                    handleResponse(response);
                    refreshCollection(false);
                } else {
                    appendLog(loc.get("error.network") + ": " + err.getMessage());
                }
            });
            if (err != null) {
                String msg = err.getMessage() != null ? err.getMessage() : err.toString();
                return loc.get("error.network") + ": " + msg;
            }
            return formatResponseText(response);
        });
    }

    private String formatResponseText(Response response) {
        if (response == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String message = response.getMessage();
        if (message != null && !message.isBlank()) {
            sb.append(message);
        }
        if (response.getData() != null && response.getData().length() > 0) {
            String data = response.getData().toString();
            if (!data.isBlank()) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(data);
            }
        }
        return sb.toString();
    }

    private void handleResponse(Response response) {
        if (response == null) {
            return;
        }
        appendLog(response.getMessage());
        if (response.getData() != null && response.getData().length() > 0) {
            appendLog(response.getData().toString());
        }
    }

    private void appendLog(String line) {
        if (line == null || line.isBlank()) {
            return;
        }
        logArea.appendText(line.stripTrailing() + "\n");
    }

    private void applyLocalization() {
        if (stage == null) {
            return;
        }
        stage.setTitle(loc.get("main.title"));
        userLabel.setText(loc.get("main.user") + ": " + session.getUsername());
        languageCaptionLabel.setText(loc.get("main.language") + ":");
        canvasTitleLabel.setText(loc.get("main.canvas"));
        if (logCaptionLabel != null) {
            logCaptionLabel.setText(loc.get("main.log"));
        }

        BorderPane root = (BorderPane) stage.getScene().getRoot();
        VBox main = (VBox) root.getCenter();
        VBox tableBlock = (VBox) main.getChildren().get(1);
        HBox filterRow = (HBox) tableBlock.getChildren().get(0);
        for (javafx.scene.Node node : filterRow.getChildren()) {
            if (node instanceof Label label) {
                if ("filterLabel".equals(label.getId())) {
                    label.setText(loc.get("main.filter") + ":");
                } else if ("filterColumnLabel".equals(label.getId())) {
                    label.setText(loc.get("main.filter.column") + ":");
                }
            } else if (node instanceof Button button && "resetSortButton".equals(button.getId())) {
                button.setText(loc.get("main.sort.reset"));
            }
        }

        String selectedFilterKey = filterColumnBox.getValue();
        if (selectedFilterKey == null || !COLUMN_KEYS.contains(selectedFilterKey)) {
            selectedFilterKey = "col.key";
        }
        filterColumnBox.setValue(null);
        filterColumnBox.setValue(selectedFilterKey);

        for (javafx.scene.Node node : commandColumn.getChildren()) {
            if (node instanceof Button b) {
                if (b.getUserData() instanceof CommandType type) {
                    b.setText(loc.commandLabel(type));
                    b.setMnemonicParsing(false);
                } else if ("cmd.exit".equals(b.getUserData())) {
                    b.setText(loc.get("exit"));
                    b.setMnemonicParsing(false);
                }
            }
        }

        if (rowNumberColumn != null) {
            rowNumberColumn.setText(loc.get("col.row"));
        }

        refreshSortUi();

        applyTableFilterSort();
    }
}
