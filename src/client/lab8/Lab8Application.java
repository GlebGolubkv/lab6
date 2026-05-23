package client.lab8;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Lab 8 JavaFX client entry point.
 */
public class Lab8Application extends Application {

    @Override
    public void start(Stage primaryStage) {
        Lab8AuthView authView = new Lab8AuthView(session -> new Lab8MainView(session).show());
        authView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
