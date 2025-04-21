package org.ignacioScript.co;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        // Define the file path for storing books
        String filePath = "src/main/resources/books.txt";

        // Create a list of books
        List<Book> books = Arrays.asList(

                new Book("3216549870123", "Design Patterns", "Elements of Reusable Object-Oriented Software", "Addison-Wesley", LocalDate.of(1994, 10, 31), 54.99, 25),
                new Book("6549873210987", "Refactoring", "Improving the Design of Existing Code", "Addison-Wesley", LocalDate.of(1999, 7, 8), 47.99, 18),
                new Book("7891234567890", "The Pragmatic Programmer", "Your Journey to Mastery", "Addison-Wesley", LocalDate.of(1999, 10, 20), 42.99, 22)

        );

        // Initialize the BookFileManager
        BookFileManager bookFileManager = new BookFileManager(filePath);

        try {
            // Save the books to the file
            bookFileManager.saveBooks(books);
            System.out.println("Books have been successfully saved to " + filePath);

            // Log the operation
            FileLogger.log("Books saved to file: " + filePath);
        } catch (Exception e) {
            System.err.println("An error occurred while saving books: " + e.getMessage());
            FileLogger.log("Error saving books: " + e.getMessage());
        }


    }
}