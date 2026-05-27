package server.head;

import server.data.DataInitializer;

import java.util.Scanner;

/**
 * Точка входа серверного приложения: инициализация данных, сеть и консоль управления.
 */
public class Server {

    /**
     * Запускает сервер: инициализация подсистем, UDP-сеть и поток консольных команд.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {

        DataInitializer.initialize();
        Scanner scanner = new Scanner(System.in);

        try {
            ServerNetworkManager serverNetworkManager = new ServerNetworkManager();

            new ServerTerminalManager(scanner, serverNetworkManager).start();
            serverNetworkManager.start();

        } catch (Exception e) {
            System.out.println("Error in server " + e.getMessage());
        }

    }

}
