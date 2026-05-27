package client.gui;

import client.ClientInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Точка входа JavaFX-приложения: инициализация клиента и показ окна авторизации.
 * Запускается из {@link client.Client#main}.
 */
public class GuiBootstrap extends Application {

    /**
     * Запускает GUI: инициализирует клиент, открывает {@link AuthView}, после входа — {@link MainView}.
     *
     * @param primaryStage главное окно JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        ClientInitializer.initialize();
        AuthView authView = new AuthView(session -> {
            try {
                new MainView(session).show();
            } catch (Exception e) {
                e.printStackTrace();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                DialogFormHelper.stripDialogIcon(alert);
                alert.showAndWait();
            }
        });
        authView.show();
    }
}
