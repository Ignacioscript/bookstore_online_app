package org.ignacioScript.co;

import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        // Define the file paths for storing books and authors
        String bookFilePath = "src/main/resources/books.csv";
        String authorFilePath = "src/main/resources/authors.csv";

        // Create a list of books
        List<Book> books = Arrays.asList(
                new Book("3216549870123", "Design Patterns", "Elements of Reusable Object-Oriented Software", "Addison-Wesley", LocalDate.of(1994, 10, 31), 54.99, 25),
                new Book("6549873210987", "Refactoring", "Improving the Design of Existing Code", "Addison-Wesley", LocalDate.of(1999, 7, 8), 47.99, 18),
                new Book("7891234567890", "The Pragmatic Programmer", "Your Journey to Mastery", "Addison-Wesley", LocalDate.of(1999, 10, 20), 42.99, 22)
        );

        // Create a list of authors
        List<Author> authors = Arrays.asList(
                new Author("Erich", "Gamma", "Co-author of Design Patterns", "Swiss"),
                new Author("Martin", "Fowler", "Author of Refactoring and software design expert", "British"),
                new Author("Andrew", "Hunt", "Co-author of The Pragmatic Programmer", "American"),
                new Author("David", "Thomas", "Co-author of The Pragmatic Programmer", "American")
        );

        // Initialize the BookFileManager
        BookFileManager bookFileManager = new BookFileManager(bookFilePath);

        // Initialize the AuthorFileManager
        AuthorFileManager authorFileManager = new AuthorFileManager(authorFilePath);

        try {
            // Save the books to the file
            bookFileManager.save(books);
            System.out.println("Books have been successfully saved to " + bookFilePath);
            FileLogger.log("Books saved to file: " + bookFilePath);

            // Save the authors to the file
            authorFileManager.save(authors);
            System.out.println("Authors have been successfully saved to " + authorFilePath);
            FileLogger.log("Authors saved to file: " + authorFilePath);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            FileLogger.log("Error: " + e.getMessage());
        }
    }

    //TODO create test for File Managers and create a class to handle the library like database file, here in main just the GUI
    //TODO also create methods to add one or delete or search specific book, use keyword logic from the lab
}



