package client;


import java.util.Scanner;

public class Client {

    public static void main(String[] args) {


        ClientInitializer.initialize();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter host: (localhost)");
        String host = hostInput(scanner);

        System.out.println("Please enter port: (8888)");
        int port = portInput(scanner);




        try (ClientNetworkManager clientNetworkManager = new ClientNetworkManager(host, port)) {

            new ClientTerminalManager(clientNetworkManager, scanner).start();


        } catch (Exception e) {
            System.err.println("Error during the client's work: " + e.getMessage());
        }
    }


    private static String hostInput(Scanner scanner) {
        return scanner.nextLine();
    }

    private static int portInput(Scanner scanner) {
        while (true) {
            try {
               return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid port");
            }
        }
    }

}



