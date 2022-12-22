package net.cps.server.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static final String ANSI_BLACK_BACKGROUND = "\033[40m";
    public static final String ANSI_BLUE_BOLD = "\033[1;34m";
    public static final String ANSI_COLOR = ANSI_BLACK_BACKGROUND + ANSI_BLUE_BOLD;
    public static final String ANSI_RESET = "\033[0m";
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static PrintWriter logWriter;
    
    private static int id;
    
    /**
     * Logger initiation, `.log` file creation
     *
     * @param logFilePath
     * @throws IOException
     */
    public static void init(String logFilePath) throws IOException {
        if (logWriter != null) {
            Logger.print("Logger error: the server `Logger` has already been initialized.");
            return;
        }
        
        logWriter = new PrintWriter(new FileWriter(logFilePath, true), true);
        id = 1;
    
        String logHead = String.format("Time\tLog ID\t\t\tLog Type\t\t\tMessage");
        logWriter.println(logHead);
        logWriter.close();
    }
    
    /**
     * Log a simple message to the server `.log` file.
    **/
    public static void log(String message) {
        if (logWriter == null) {
            Logger.print("Logger error: the server `Logger` has not been initialized yet.");
            return;
        }
    
        Logger.writeToLog("LOG", message);
    }
    
    /**
     * Log a simple message to the server `.log` file.
     **/
    public static void error(String message) {
        if (logWriter == null) {
            Logger.print("Logger error: the server `Logger` has not been initialized yet.");
            return;
        }
    
        Logger.writeToLog("ERROR", message);
    }
    
    /**
     * Log a simple message to the server `.log` file.
     **/
    protected static void writeToLog(String type, String message) {
        String logLine = String.format("[%s]\t[%d]\t\t\t[%s]\t\t\t%s", DATE_FORMAT.format(new Date()), ++Logger.id, type, message);
        logWriter.println(logLine);
        logWriter.close();
    }
    
    /**
     * Log a message to the system standard output stream.
     **/
    public static void print(String message) {
        System.out.println(ANSI_COLOR + "[SERVER] " + message + ANSI_RESET);
    }
    
    /**
     * Log multiple messages to the system standard output stream.
     **/
    public static void print(String... messages) {
        System.out.println(ANSI_COLOR + "[SERVER] " + messages[0]);
        for (int i = 1 ; i < messages.length ; i++) {
            System.out.println("         " + messages[i]);
        }
        System.out.print(ANSI_RESET);
    }
}

