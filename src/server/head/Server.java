package server.head;

import server.data.DataInitializer;

import java.util.Scanner;

public class Server {

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
