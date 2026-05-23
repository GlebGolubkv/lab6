package client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/** JavaFX UI bootstrap; launched from {@link client.Client#main}. */
public class GuiBootstrap extends Application {

    @Override
    public void start(Stage primaryStage) {
        AuthView authView = new AuthView(session -> new MainView(session).show());
        authView.show();
    }
}
