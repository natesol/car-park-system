package net.cps.server.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {
    private static final Logger logger = Logger.getLogger("ServerLog");
    private static FileHandler fileHandler;
    
    static {
        try {
            fileHandler = new FileHandler("server.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Log a message to system standard output stream.
    public static void print(String message) {
        System.out.println("[SERVER] " + message + ".");
    }
    
    // Log a message to the log file.
    public static void log(String message) {
        logger.info(message);
    }
}