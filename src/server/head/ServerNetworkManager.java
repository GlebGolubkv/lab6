package server.head;

import common.dataclasses.CommandType;
import common.Request;
import common.Response;

import common.dataclasses.MusicBand;
import server.data.DataCommands;

import java.net.*;

import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerNetworkManager {


    private static final int PORT = 8887;
    private static final String HOSTNAME = "localhost";
    private static final int bufferSize = 65535;
    private static final SocketAddress socketAddress = new InetSocketAddress(HOSTNAME, PORT);
    private static final int TIMEOUT = 3000;
    private DatagramSocket socket;
    private volatile boolean shouldShutdown = false;

    private final ExecutorService readPool = Executors.newFixedThreadPool(8);
    private final ForkJoinPool processPool = new ForkJoinPool(8);
    private final ForkJoinPool sendPool = new ForkJoinPool(4);


    public ServerNetworkManager() {
    }

    public void makeShutdown() {
        shouldShutdown = true;
    }


    public void start() throws RuntimeException {

        try {

            socket = new DatagramSocket(socketAddress);
            socket.setSoTimeout(TIMEOUT);

            System.out.println("Server started on port: " + PORT);

            while (!shouldShutdown) {

                try {
                    byte[] buffer = new byte[bufferSize];

                    // создание пакета и прислушивание порта
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    readPool.submit(() -> handleReceive(packet));
                } catch (SocketTimeoutException ignored) {
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            shutdownPools();
            if (socket != null && !socket.isClosed()) socket.close();

        }
    }


    private void handleReceive(DatagramPacket packet) {

        InetSocketAddress sender = new InetSocketAddress(packet.getAddress(), packet.getPort());

        try {

            Request request = Request.fromJson(new String(packet.getData(), 0, packet.getLength(), "UTF-8"));
            processPool.submit(() -> handleProcess(request, sender));

        } catch (Exception e) {
            sendPool.submit(() -> sendResponse(new Response(false, "Error while processing request: " + e.getMessage()), sender));
        }
    }

    private void shutdownPools() {
        processPool.shutdown();
        sendPool.shutdown();
        readPool.shutdown();
    }

    private void sendResponse(Response response, InetSocketAddress sender) {
        try {

            String responseJson = response.toJson();
            byte[] responseBytes = responseJson.getBytes("UTF-8");

            if (responseBytes.length > bufferSize) {
                responseJson = new Response(false, "The answer is too big: " + responseJson.substring(0, bufferSize - 100) + "\"...[TRUNCATED]\"}").toJson();
                responseBytes = responseJson.getBytes("UTF-8");
            }

            DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, sender.getAddress(), sender.getPort());


            socket.send(packet);
            System.out.println("Sent response to: " + sender.getAddress() + ":" + sender.getPort());


        } catch (Exception e) {
            throw new RuntimeException("Error while sending response: " + e.getMessage());
        }
    }

    private void handleProcess(Request request, InetSocketAddress sender) {

        try {
            Response response = processRequest(request);
            System.out.println("Success: " + response.isSuccess());
            sendPool.submit(() -> sendResponse(response, sender));
        } catch (Exception e) {
            sendPool.submit(() -> sendResponse(new Response(false, "Error while processing request: " + e.getMessage()), sender));
        }
    }

    private Response processRequest(Request request) {

        CommandType commandType = request.getCommandType();
        String commandArgument = request.getArgument();
        MusicBand musicBand = request.getMusicBand();
        int clientId = request.getClientId();

        System.out.println();
        System.out.println("Got request: " + commandType + " : " + commandArgument);
        System.out.println("Client Id: " + clientId);
        System.out.println("Time: " + ZonedDateTime.now());

        if (commandType.validateInput(commandArgument, musicBand)) {
            return DataCommands.getInstance().createCommand(commandType, commandArgument, musicBand, clientId);
        } else {
            throw new IllegalArgumentException("Invalid command type");
        }
    }


}
