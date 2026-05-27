package client.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Окно авторизации и регистрации: подключение к серверу, выбор языка интерфейса.
 */
public class AuthView {

    private final ClientService service = new ClientService();
    private final Localization loc = Localization.getInstance();
    private final java.util.function.Consumer<Session> onSuccess;

    private TextField hostField;
    private TextField portField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;
    private ComboBox<java.util.Locale> languageBox;
    private Stage stage;

    /**
     * @param onSuccess обработчик успешного входа; получает созданную {@link Session}
     */
    public AuthView(java.util.function.Consumer<Session> onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * Создаёт и отображает окно входа.
     */
    public void show() {
        stage = new Stage();
        hostField = new TextField("localhost");
        portField = new TextField("8887");
        usernameField = new TextField();
        passwordField = new PasswordField();
        statusLabel = new Label();
        languageBox = new ComboBox<>(javafx.collections.FXCollections.observableArrayList(Localization.SUPPORTED));
        wireLanguageSelector();
        languageBox.setValue(loc.getLocale());
        languageBox.valueProperty().addListener((o, a, b) -> {
            if (b != null) {
                loc.setLocale(b);
                refreshTexts();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        int r = 0;
        Label lHost = new Label();
        Label lPort = new Label();
        Label lUser = new Label();
        Label lPass = new Label();
        grid.add(lHost, 0, r);
        grid.add(hostField, 1, r++);
        grid.add(lPort, 0, r);
        grid.add(portField, 1, r++);
        grid.add(lUser, 0, r);
        grid.add(usernameField, 1, r++);
        grid.add(lPass, 0, r);
        grid.add(passwordField, 1, r++);

        Button loginBtn = new Button();
        Button registerBtn = new Button();
        loginBtn.setOnAction(e -> authenticate(false));
        registerBtn.setOnAction(e -> authenticate(true));

        HBox buttons = new HBox(10, loginBtn, registerBtn);
        buttons.setAlignment(Pos.CENTER);

        Label lLang = new Label();
        VBox root = new VBox(12,
                new Label(), languageBox, grid, buttons, statusLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        VBox.setMargin(languageBox, new Insets(0, 0, 8, 0));

        grid.getProperties().put("lHost", lHost);
        grid.getProperties().put("lPort", lPort);
        grid.getProperties().put("lUser", lUser);
        grid.getProperties().put("lPass", lPass);
        root.getProperties().put("title", root.getChildren().get(0));
        root.getProperties().put("lLang", lLang);
        root.getProperties().put("loginBtn", loginBtn);
        root.getProperties().put("registerBtn", registerBtn);

        Scene scene = new Scene(root, 420, 380);
        stage.setScene(scene);
        refreshTexts();
        stage.show();
    }

    private void wireLanguageSelector() {
        languageBox.setCellFactory(cb -> new ListCell<>() {
            /**
             * Показывает локализованное название языка в списке выбора.
             */
            @Override
            protected void updateItem(java.util.Locale item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.displayName(item));
            }
        });
        languageBox.setButtonCell(new ListCell<>() {
            /**
             * Показывает выбранный язык на кнопке комбобокса.
             */
            @Override
            protected void updateItem(java.util.Locale item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : loc.displayName(item));
            }
        });
    }

    private void refreshTexts() {
        stage.setTitle(loc.get("auth.title"));
        Label title = (Label) ((VBox) stage.getScene().getRoot()).getProperties().get("title");
        title.setText(loc.get("auth.title"));
        GridPane grid = (GridPane) ((VBox) stage.getScene().getRoot()).getChildren().get(2);
        ((Label) grid.getProperties().get("lHost")).setText(loc.get("auth.host"));
        ((Label) grid.getProperties().get("lPort")).setText(loc.get("auth.port"));
        ((Label) grid.getProperties().get("lUser")).setText(loc.get("auth.username"));
        ((Label) grid.getProperties().get("lPass")).setText(loc.get("auth.password"));
        ((Label) ((VBox) stage.getScene().getRoot()).getProperties().get("lLang")).setText(loc.get("main.language"));
        ((Button) ((VBox) stage.getScene().getRoot()).getProperties().get("loginBtn")).setText(loc.get("auth.login"));
        ((Button) ((VBox) stage.getScene().getRoot()).getProperties().get("registerBtn")).setText(loc.get("auth.register"));
    }

    private void authenticate(boolean register) {
        statusLabel.setText("...");
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid port");
            return;
        }
        String host = hostField.getText().trim();
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText(loc.get("error.empty"));
            return;
        }
        service.authenticate(host, port, user, pass, register).whenComplete((session, err) -> Platform.runLater(() -> {
            if (err != null) {
                statusLabel.setText(err.getMessage());
                return;
            }
            stage.hide();
            onSuccess.accept(session);
        }));
    }
}
