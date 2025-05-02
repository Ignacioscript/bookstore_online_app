package org.ignacioScript.co;

import org.ignacioScript.co.controller.AuthorBookController;
import org.ignacioScript.co.controller.AuthorController;
import org.ignacioScript.co.controller.BookController;
import org.ignacioScript.co.io.AuthorBookFileManager;
import org.ignacioScript.co.io.AuthorFileManager;
import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize file managers
        AuthorFileManager authorFileManager = new AuthorFileManager("src/main/resources/authors.csv");
        BookFileManager bookFileManager = new BookFileManager("src/main/resources/books.csv");
        AuthorBookFileManager authorBookFileManager = new AuthorBookFileManager(
                "src/main/resources/author_book.csv",
                "src/main/resources/books.csv",
                "src/main/resources/authors.csv"
        );

        // Initialize services
        AuthorService authorService = new AuthorService(authorFileManager);
        BookService bookService = new BookService(bookFileManager);
        AuthorBookService authorBookService = new AuthorBookService(authorBookFileManager);

        // Initialize controllers
        AuthorController authorController = new AuthorController(scanner, authorService);
        BookController bookController = new BookController(scanner, bookService);
        AuthorBookController authorBookController = new AuthorBookController(authorBookService, authorService, bookService);

        while (true) {
            displayMainMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1 -> authorController.sortAuthors(scanner);
                case 2 -> bookController.sortBooks(scanner);
                case 3 -> authorBookController.createAuthorBookRelationship(scanner);
                case 4 -> viewAllAuthorBookRelationships(authorBookService);
                case 5 -> {
                    System.out.println("Exiting the application...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Manage Authors (Sort)");
        System.out.println("2. Manage Books (Sort)");
        System.out.println("3. Create Author-Book Relationship");
        System.out.println("4. View All Author-Book Relationships");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    private static void viewAllAuthorBookRelationships(AuthorBookService authorBookService) {
        System.out.println("\n===== Author-Book Relationships =====");
        try {
            var relationships = authorBookService.getAllAuthorBooks();
            if (relationships.isEmpty()) {
                System.out.println("No relationships found.");
            } else {
                for (var relationship : relationships) {
                    System.out.println("Author: " + relationship.getAuthor().getFirstName() + " " +
                            relationship.getAuthor().getLastName() + " - Book: " + relationship.getBook().getBookTitle());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}