package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BookFileManager {

    private final String filePath;

    public BookFileManager(String filePath) {
        this.filePath = filePath;
        FileLogger.log("Initialized BookFileManager for: " + filePath);
    }

    //write books using BufferedWriter
    public void saveBooks(List<Book> books) {
        FileLogger.log("Starting to save " + books.size() + " books");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(bookToString(book));
                writer.newLine();
                FileLogger.log("Save book: " + book.getBookTitle());
            }
            FileLogger.log("Successfully saved all books");

        }catch (IOException e) {
                FileLogger.log("ERROR saving books: " + e.getMessage());
                throw new RuntimeException("Save Operation failed", e);
        }
    }

    // Read books using BufferedReader
    public List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(stringToBook(line));
            }
        }
        return books;
    }


    // Helper methods
    // Convert  Book Object to CSV String
    private String  bookToString(Book book) {
        return String.join(",",
                String.valueOf(book.getBookId()),
                book.getIsbn(),
                book.getBookTitle(),
                book.getDescription(),
                book.getPublisher(),
                book.getPublicationDate().toString(),
                book.getPrice().toString(),
                book.getStock().toString()
                );
    }

    // Convert String CSV Book Object
    private Book stringToBook(String line) {
        String[] parts = line.split(",");
        return new Book(
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                LocalDate.parse(parts[5]),
                Double.parseDouble(parts[6]),
                Integer.parseInt(parts[7])
        );
    }
}
