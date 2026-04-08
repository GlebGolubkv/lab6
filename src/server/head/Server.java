package server.head;

import server.data.DataInitializer;

import java.util.Scanner;

public class Server {

    public static void main(String[] args) {


        String fileName = args[0];


        // при желании подставить введение имени файла
        DataInitializer.initialize(fileName);
        Scanner scanner = new Scanner(System.in);







        try {
            new ServerTerminalManager().start();
            new ServerNetworkManager().start();
        } catch (Exception e) {
            System.out.println("Error in server " + e.getMessage());
        }

    }


}
