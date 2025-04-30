package org.ignacioScript.co.util;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String ERROR_LOG_FILE = System.getProperty("log.file", "logs/error.log");
    private static final String INFO_LOG_FILE = System.getProperty("log.file", "logs/info.log");
    private static final String APPLICATION_LOG_FILE = System.getProperty("log.file", "logs/application.log");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    public static void logError(String message) {
        //create a logs directory if it doesn't exist
        new File("logs").mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            PrintWriter out = new PrintWriter(writer);


            String timestamp = java.time.LocalDateTime.now().format(DATE_TIME);
            out.println(timestamp + " - " + message);

        }catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }

    }

    public static void logInfo(String message) {
        //create a logs directory if it doesn't exist
        new File("logs").mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INFO_LOG_FILE, true))) {
            PrintWriter out = new PrintWriter(writer);


            String timestamp = java.time.LocalDateTime.now().format(DATE_TIME);
            out.println(timestamp + " - " + message);

        }catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }

    }

    public static void logApp(String message) {
        //create a logs directory if it doesn't exist
        new File("logs").mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATION_LOG_FILE, true))) {
            PrintWriter out = new PrintWriter(writer);


            String timestamp = java.time.LocalDateTime.now().format(DATE_TIME);
            out.println(timestamp + " - " + message);

        }catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }

    }
}
