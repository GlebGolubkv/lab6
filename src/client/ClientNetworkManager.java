package client;

import common.Request;
import common.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * Менеджер UDP-соединения клиента с сервером: отправка запросов и получение ответов.
 */
public class ClientNetworkManager implements AutoCloseable {

    private static final int BUFFER_SIZE = 65535;
    /** Ожидание ответа сервера, мс. */
    private static final int TIMEOUT = 10_000;
    private final InetSocketAddress serverAddress;
    private DatagramChannel channel;

    /**
     * Создаёт менеджер и устанавливает соединение с сервером по указанному хосту и порту.
     *
     * @param host адрес сервера
     * @param port порт сервера
     * @throws IOException при ошибке открытия канала
     */
    public ClientNetworkManager(String host, int port) throws IOException {
        this.serverAddress = new InetSocketAddress(host, port);
        connect();
    }

    /**
     * Открывает неблокирующий UDP-канал для обмена данными с сервером.
     *
     * @throws IOException при ошибке создания или настройки канала
     */
    public void connect() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        System.out.println("Connected to " + serverAddress.getAddress() + " : " + serverAddress.getPort());

    }

    /**
     * Сериализует запрос в JSON, отправляет его на сервер и ожидает ответ в пределах таймаута.
     *
     * @param request запрос к серверу
     * @return ответ сервера или ответ с признаком неуспеха, если ответ не получен вовремя
     * @throws IOException при ошибке сетевого взаимодействия
     * @throws InterruptedException при прерывании ожидания ответа
     */
    public Response sendRequest(Request request) throws IOException, InterruptedException {

        int sleepMs = 100;
        int maxAttempts = TIMEOUT / sleepMs;

        
        String jsonRequest = request.toJson();
        ByteBuffer requestBuffer = ByteBuffer.wrap(jsonRequest.getBytes(StandardCharsets.UTF_8));

        channel.send(requestBuffer, serverAddress);

        ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        int attempts = 0;

        while (attempts < maxAttempts) {
            responseBuffer.clear();

            
            SocketAddress sender = channel.receive(responseBuffer);

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

    /**
     * Закрывает UDP-канал, если он был открыт.
     *
     * @throws Exception при ошибке закрытия канала
     */
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
