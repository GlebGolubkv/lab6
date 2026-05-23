package client;

import client.gui.GuiBootstrap;
import javafx.application.Application;

/**
 * Client entry point: launches the JavaFX GUI ({@link client.gui}).
 * Console client classes ({@link ClientTerminalManager}, {@link UsersDTO}) are kept unchanged.
 */
public class Client {

    public static void main(String[] args) {
        Application.launch(GuiBootstrap.class, args);
    }
}



