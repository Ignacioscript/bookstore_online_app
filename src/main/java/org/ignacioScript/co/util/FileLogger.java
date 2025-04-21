package org.ignacioScript.co.util;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String LOG_FILE = System.getProperty("log.file", "logs/bookstore.logs");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        //create a logs directory if it doesn't exist
        new File("logs").mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            PrintWriter out = new PrintWriter(writer);


            String timestamp = java.time.LocalDateTime.now().format(DATE_TIME);
            out.println(timestamp + " - " + message);

        }catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }

    }
}
