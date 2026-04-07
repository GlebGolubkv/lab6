package server.head;

import server.data.ClassesManager;

import java.util.Scanner;

public class ServerTerminalManager {

    public ServerTerminalManager() {}

    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            ClassesManager.getInstance().saveCollectionToFile();
            System.out.println("Collection saved");
            System.out.println("Shutting down...");

        }));


        Thread console = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true) {

                String command = scanner.nextLine().trim().toLowerCase();

                if (command.equals("save")) {
                    ClassesManager.getInstance().saveCollectionToFile();
                }


            }
        });

        console.start();
    }
}
