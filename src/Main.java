import data.DataCommands;
import data.DataInitializer;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        String fileName = args[0];


        DataInitializer.initialize(fileName);

        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println();
            System.out.print("Enter command: ");

            String line = scanner.nextLine().trim();
            DataCommands.getInstance().createCommandByName(line);
        }


    }
}
