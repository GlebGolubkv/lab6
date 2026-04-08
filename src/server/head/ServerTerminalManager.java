package server.head;

import server.data.ClassesManager;


public class ServerTerminalManager {

    public ServerTerminalManager() {
    }

    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            ClassesManager.getInstance().saveCollectionToFile();
            System.out.println("Collection saved");
            System.out.println("Shutting down...");

        }));

    }

}
