package server.head;

import java.util.Scanner;

/**
 * Управление консольным вводом сервера (команда exit для остановки сети).
 */
public class ServerTerminalManager {

    private final Scanner scanner;
    private final ServerNetworkManager serverNetworkManager;

    /**
     * Создаёт менеджер консоли сервера.
     *
     * @param scanner              источник строковых команд
     * @param serverNetworkManager сетевой менеджер для остановки
     */
    public ServerTerminalManager(Scanner scanner, ServerNetworkManager serverNetworkManager) {
        this.scanner = scanner;
        this.serverNetworkManager = serverNetworkManager;
    }

    /**
     * Запускает фоновый поток чтения консольных команд и обработчик завершения JVM.
     */
    public void start() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("Shutting down...");

        }));

        Thread thread = new Thread(() -> {

            System.out.println("Console active (commands: exit)");

            while (true) {
                String command = scanner.nextLine().toLowerCase().trim();

                switch (command) {

                    case "exit" -> {
                        serverNetworkManager.makeShutdown();
                    }

                }
            }

        });
        thread.setDaemon(true);
        thread.start();

    }

}
