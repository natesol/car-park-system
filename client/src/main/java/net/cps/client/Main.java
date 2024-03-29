package net.cps.client;

import java.util.TimeZone;

public class Main {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 3000;
    
    private static CPSClient client;
    
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
        
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 0 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        
        client = CPSClient.getClient(host, port);
        System.out.println("[CLIENT] client connected to - host: '" + host + "', port: '" + port + "'.");

        App.render(client);
    }
}
