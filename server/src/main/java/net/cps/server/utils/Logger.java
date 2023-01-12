package net.cps.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Logger for the server.
 **/
public class Logger {
    
    public static final String ANSI_BLACK_BACKGROUND = "\033[40m";
    public static final String ANSI_WHITE = "\033[0;37m";
    public static final String ANSI_RED = "\033[0;31m";
    public static final String ANSI_YELLOW = "\033[0;33m";
    public static final String ANSI_BLUE_BOLD = "\033[1;34m";
    public static final String ANSI_PURPLE_BOLD = "\033[1;35m";
    public static final String ANSI_CYAN_BOLD = "\033[1;36m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_SERVER = ANSI_PURPLE_BOLD;
    public static final String ANSI_MESSAGE = ANSI_RESET;
    public static final String ANSI_WARNING = ANSI_YELLOW;
    public static final String ANSI_ERROR = ANSI_RED;
    
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static File logFile;
    private static PrintWriter writer;
    private static int logId = 0;
    
    /**
     * Static class.
     **/
    private Logger () {}
    
    /**
     * Logger initiation method, `.log` file creation.
     **/
    public static void init (String logFilePath) throws IOException {
        if (writer != null) {
            Logger.print("Warning: the server `Logger` has already been initialized.");
            return;
        }
        
        try {
            logFile = new File(logFilePath);
            writer = new PrintWriter(logFile);
        }
        catch (IOException e) {
            Logger.print("Error: could not open log file: " + logFilePath, "operation ended with: " + e);
        }
        
        writer.format("%-25s %-20s %-20s %s\n", "Time", "ID", "Type", "Message");
        writer.flush();
    }
    
    /**
     * Get the `.log` file full path.
     **/
    public static String getLogFile () {
        return logFile.getAbsolutePath();
    }
    
    /**
     * Log a message to the system standard output stream.
     **/
    public static void print (String message) {
        System.out.print("[" + ANSI_SERVER + "SERVER" + ANSI_RESET + "] ");
        
        if (message.startsWith("Warning:")) {
            System.out.print(ANSI_WARNING + message + ANSI_RESET);
        }
        else if (message.startsWith("Error:")) {
            System.out.print(ANSI_ERROR + message + ANSI_RESET);
        }
        else {
            System.out.print(ANSI_MESSAGE + message + ANSI_RESET);
        }
        
        System.out.println();
    }
    
    /**
     * Log multiple messages to the system standard output stream.
     **/
    public static void print (String... messages) {
        System.out.print("[" + ANSI_SERVER + "SERVER" + ANSI_RESET + "] ");
    
        String ANSI_CURRENT_DEFAULT = messages[0].startsWith("Warning:") ? ANSI_WARNING : messages[0].startsWith("Error:") ? ANSI_ERROR : ANSI_MESSAGE;
    
        System.out.print(ANSI_CURRENT_DEFAULT + messages[0] + ANSI_RESET + "\n");
        for (int i = 1; i < messages.length; i++) {
            if (messages[i].startsWith("Warning:")) {
                System.out.print("         " + ANSI_WARNING + messages[i] + ANSI_RESET + "\n");
                ANSI_CURRENT_DEFAULT = ANSI_WARNING;
            }
            else if (messages[i].startsWith("Error:")) {
                System.out.print("         " + ANSI_ERROR + messages[i] + ANSI_RESET + "\n");
                ANSI_CURRENT_DEFAULT = ANSI_ERROR;
            }
            else {
                System.out.print("         " + ANSI_CURRENT_DEFAULT + messages[i] + ANSI_RESET + "\n");
            }
        }
    }
    
    /**
     * Log a simple message to the server `.log` file - INFO type.
     **/
    public static void info (String message) {
        if (writer == null) {
            Logger.print("Error: the server `Logger` has not been initialized yet.");
            return;
        }
        
        Logger.writeToLog("INFO", message);
    }
    
    /**
     * Log a warning message to the server `.log` file - WARNING type.
     **/
    public static void warning (String message) {
        if (writer == null) {
            Logger.print("Error: the server `Logger` has not been initialized yet.");
            return;
        }
        
        Logger.writeToLog("WARNING", message);
    }
    
    /**
     * Log an error message to the server `.log` file - ERROR type.
     **/
    public static void error (String message) {
        if (writer == null) {
            Logger.print("Error: the server `Logger` has not been initialized yet.");
            return;
        }
        
        Logger.writeToLog("ERROR", message);
    }
    
    /**
     * Inner Utility - format the given message and write it to the `.log` file.
     **/
    private static void writeToLog (String logType, String message) {
        logId++;
        writer.format("%-25s %-20s %-20s %s\n", dateFormatter.format(new Date()), logId, logType, message);
        writer.flush();
    }
    
}

