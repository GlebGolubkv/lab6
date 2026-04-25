package client;

import common.*;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;
import common.dataclasses.Colors;

import java.io.IOException;
import java.util.Scanner;

public class ClientTerminalManager {

    public Scanner scanner;
    public ClientNetworkManager clientNetworkManager;

    public ClientTerminalManager(ClientNetworkManager clientNetworkManager, Scanner scanner) {
        this.clientNetworkManager = clientNetworkManager;
        this.scanner = scanner;
    }


    public void start() throws IOException, InterruptedException, RuntimeException {
        System.out.println("Please enter command: ");

        while (true) {

            try{

            System.out.println();
            System.out.print("> ");
            String command = scanner.nextLine().toLowerCase().trim();
            System.out.println();

            Request request = makeRequest(command, scanner);


            Response response = clientNetworkManager.sendRequest(request);
            if (response.isSuccess()) {
                System.out.println(Colors.CYAN + response.getMessage() + Colors.RESET);

                if (response.getData() != null) {
                    System.out.println(response.getData());
                }
            } else {
                System.err.println(response.getMessage());
                System.out.println();

            }} catch (Exception e) {
                System.err.println("Error: " + e.getMessage());;
            }

        }
    }

    public Request makeRequest(String line, Scanner scanner) {

        CommandType commandName;
        String argumentName = null;
        MusicBand inputMusicBand = null;


        String[] command = line.split("\\s+");

        if (command[0].equals("exit")){
            System.out.println("Shouting down...");
            System.exit(0);
        }
        // проверка на 3 и более аргументов
        if (command.length > 2) {
            throw  new IllegalArgumentException("There are too many arguments for the function");
        }

        // Получаем аргумент
        if (command.length > 1) {
            argumentName = command[1].toLowerCase();
        }



        // Проверяем команду
        if (CommandType.contains(command[0])) {
            commandName = CommandType.fromName(command[0]);
        } else {
            throw new IllegalArgumentException("Unknown command: " + command[0]);
        }

        // Проверяем MusicBand
        if (commandName.requiresMusicBand()) {
            if (commandName == CommandType.UPDATE) {

                inputMusicBand = BandsInputTerminal.getInstance().inputBand(Integer.parseInt(command[1]), scanner);
            } else {
                inputMusicBand = BandsInputTerminal.getInstance().inputBand(0, scanner);
            }
        }

        //Проверка аргумента
        if (commandName.validateInput(argumentName, inputMusicBand)){
            return new Request(commandName, argumentName, inputMusicBand);
        } else {
            throw new IllegalArgumentException(commandName.getValidationError(argumentName, inputMusicBand));
        }


    }


}
