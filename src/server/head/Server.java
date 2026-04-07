package server.head;

import server.data.DataInitializer;

public class Server {

    public static void main(String[] args) {


        /*        String fileName = args[0];*/


        // при желании подставить введение имени файла
        DataInitializer.initialize("map_data");






        try {
            new ServerTerminalManager().start();
            new ServerNetworkManager().start();
        } catch (Exception e) {
            System.out.println("Error in server " + e.getMessage());
        }

    }


}
