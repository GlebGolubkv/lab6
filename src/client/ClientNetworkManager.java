package client;

import common.Request;
import common.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class ClientNetworkManager implements AutoCloseable {

    private static final int BUFFER_SIZE = 65535;
    private static final int TIMEOUT = 5000;
    private final InetSocketAddress serverAddress;
    private DatagramChannel channel;


    public ClientNetworkManager(String host, int port) throws IOException {
        this.serverAddress = new InetSocketAddress(host, port);
        connect();
    }

    public void connect() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        System.out.println("Connected to " + serverAddress.getAddress() + " : " + serverAddress.getPort());

    }


    public Response sendRequest(Request request) throws IOException, InterruptedException {




        int sleepMs = 100;
        int maxAttempts = TIMEOUT / sleepMs;



        // подготавливаем request к отправке
        String jsonRequest = request.toJson();
        ByteBuffer requestBuffer = ByteBuffer.wrap(jsonRequest.getBytes(StandardCharsets.UTF_8));

        // отправляем в неблокирующем режиме
        channel.send(requestBuffer, serverAddress);



        ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        int attempts = 0;

        while (attempts < maxAttempts) {
            responseBuffer.clear();

            // получаем пакет и записываем его размер
            SocketAddress sender  = channel.receive(responseBuffer);


            if (sender != null) {

                responseBuffer.flip();

                byte[] responseByte = new byte[responseBuffer.remaining()];
                responseBuffer.get(responseByte);
                String responseString = new String(responseByte, StandardCharsets.UTF_8);
                Response response = Response.fromJson(responseString);
                return response;

            }

            attempts++;
            Thread.sleep(sleepMs);

        }

        return new Response(false, "Couldn't get a response from the server.");


    }

    @Override
    public void close() throws Exception {
        if (channel != null && channel.isConnected()) {
            try {
                channel.close();
                System.out.println("Closed connection to " + serverAddress.getAddress() + " : " + serverAddress.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
