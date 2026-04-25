package server.head;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.dataclasses.CommandType;
import common.Request;
import common.Response;

import server.data.DataCommands;
import common.dataclasses.MusicBand;
import server.data.generators.IDGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

import java.time.ZonedDateTime;

public class ServerNetworkManager {


    private static final int PORT = 8888;
    private static final String HOSTNAME = "localhost";
    private static final int bufferSize = 65535;
    private static final SocketAddress socketAddress = new InetSocketAddress(HOSTNAME, PORT);
    private static final int TIMEOUT = 3000;
    private volatile boolean shouldShutdown = false;


    public ServerNetworkManager() {
    }

    public void makeShutdown() {
        shouldShutdown = true;
    }


    public void start() throws RuntimeException {


        try (DatagramSocket socket = new DatagramSocket(socketAddress)) {

            socket.setSoTimeout(TIMEOUT);

            System.out.println("Server started on port: " + PORT);

            while (!shouldShutdown) {

                try {

                    byte[] buffer = new byte[bufferSize];

                    // создание пакета и прислушивание порта
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    //сохранение отправителя
                    InetSocketAddress sender = new InetSocketAddress(packet.getAddress(), packet.getPort());
                    Response response;

                    // создание запроса и обработка
                    try {

                        // Блок обработки запроса
                        response = processRequest(packet);

                    } catch (Exception e) {
                        response = new Response(false, "Error while processing request: " + e.getMessage(), null);
                    }

                    System.out.println("Success: " + response.isSuccess());

                    //Блок отправки ответа
                    sendResponse(response, sender, socket);

                } catch (SocketTimeoutException e) {
                    continue;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Response processRequest(DatagramPacket packet) throws IOException {

        Request request = Request.fromJson(new String(packet.getData(), 0, packet.getLength(), "UTF-8"));

        System.out.println();
        System.out.println("Got request: " + request.getCommandType() + " : " + request.getArgument());
        System.out.println("Time: " + ZonedDateTime.now());


        CommandType commandType = request.getCommandType();
        String commandArgument = request.getArgument();
        MusicBand musicBand = request.getMusicBand();

        musicBand = new IDGenerator().validateId(musicBand);

        // Валидация на входе в сервер
        if (commandType.validateInput(commandArgument, musicBand)) {


            return DataCommands.getInstance().createCommand(commandType, commandArgument, musicBand);
        } else {

            throw new IllegalArgumentException("Invalid command type");
        }


    }

    private void sendResponse(Response response, InetSocketAddress sender, DatagramSocket socket) throws UnsupportedEncodingException, JsonProcessingException {
        //Блок отправки ответа
        String responseJson = response.toJson();
        byte[] responseBytes = responseJson.getBytes("UTF-8");
        if (responseBytes.length > bufferSize) {
            responseJson = new Response(false, "The answer is too big: " + responseJson.substring(0, bufferSize - 100) + "\"...[TRUNCATED]\"}").toJson();
            responseBytes = responseJson.getBytes("UTF-8");
        }
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, sender.getAddress(), sender.getPort());

        try {
            socket.send(packet);
            System.out.println("Sent request to: " + sender.getAddress() + ":" + sender.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
