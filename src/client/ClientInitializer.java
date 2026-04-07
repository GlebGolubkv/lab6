package client;


import common.BandsInputTerminal;
import common.JsonDataMapper;

public class ClientInitializer {


    public ClientInitializer() {}


    public static void initialize() {

        JsonDataMapper.initialize();
        BandsInputTerminal.initialize();
    }

    
}
