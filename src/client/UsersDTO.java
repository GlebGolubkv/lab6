package client;

import common.Request;
import common.Response;
import common.dataclasses.CommandType;

import java.util.Scanner;

public class UsersDTO {

    private int id;
    private final Scanner scanner;
    private final ClientNetworkManager clientNetworkManager;


    public UsersDTO(Scanner scanner, ClientNetworkManager clientNetworkManager) {
        this.scanner = scanner;
        this.clientNetworkManager = clientNetworkManager;
    }

    public int getId() {
        return id;
    }


    public void enterCommand() {

        while (true) {
            System.out.println();
            System.out.println("Press 1 if you want to sing in\nPress 2 if you want to sing up");
            String command = scanner.nextLine();
            try {

                if (command.equals("1")) {

                    id = singIn();
                    if (id > 0) {
                        break;
                    }

                } else if (command.equals("2")) {
                    id = sinUp();
                    if (id > 0) {
                        break;
                    }
                } else {
                    System.err.println("Invalid command");
                }
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
    }

    private int singIn() {

        Request request = makeRequest();


        request.setCommandType(CommandType.LOG_IN_USER);

        return getIdFromServer(request);
    }

    private int sinUp() {

        Request request = makeRequest();

        request.setCommandType(CommandType.INSERT_USER);

        return getIdFromServer(request);

    }

    private Request makeRequest() {

        System.out.println("Please enter the user name");
        String name = scanner.nextLine().trim();
        System.out.println("Please enter the user password");
        String password = scanner.nextLine().trim();

        if ((name.split("\\s+").length > 1) || (password.split("\\s+").length > 1) || name.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        String args = name + " " + password;

        return new Request(null, args, null, 0);
    }

    private int getIdFromServer(Request request) {

        try {
            Response response = clientNetworkManager.sendRequest(request);
            System.out.println(response.getMessage());
            int client_id = response.getServerData();
            return client_id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
