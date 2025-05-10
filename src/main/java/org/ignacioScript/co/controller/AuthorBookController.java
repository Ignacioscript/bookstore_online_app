package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.util.ConsoleColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AuthorBookController {

    private static AuthorBookService authorBookService;
    private static AuthorService authorService;
    private static BookService bookService;
    private static Scanner scanner;
    private static BookController bookController;
    private static AuthorController authorController;

    public AuthorBookController(Scanner scanner, AuthorBookService authorBookService, AuthorService authorService, BookService bookService) {
        AuthorBookController.authorBookService = authorBookService;
        AuthorBookController.authorService = authorService;
        AuthorBookController.bookService = bookService;
        AuthorBookController.scanner = scanner;
        AuthorBookController.bookController = new BookController(scanner, bookService);
        AuthorBookController.authorController = new AuthorController(scanner, authorService);
    }

    public void displayMenu() {
        while (true) {
            ConsoleColor.println("\n===== Author and Books Main Menu =====", ConsoleColor.CYAN);
            ConsoleColor.println("1. Manage Authors", ConsoleColor.BLUE);
            ConsoleColor.println("2. Manage Books", ConsoleColor.BLUE);
            ConsoleColor.println("3. Manage Author-Book Relationships", ConsoleColor.BLUE);
            ConsoleColor.println("4. Back to Main Menu", ConsoleColor.BLUE);
            ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> authorController.displayAuthorMenu();
                case 2 -> bookController.displayBookMenu();
                case 3 -> displayAuthorBookMenu();
                case 4 -> {
                    ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
                    MenuController.runMenu();
                    return;
                }
                default -> ConsoleColor.println("Invalid choice. Please try again.", ConsoleColor.RED);
            }
        }
    }

    public void displayAuthorBookMenu() {
        ConsoleColor.println("\n===== Author-Book Menu =====", ConsoleColor.CYAN);
        ConsoleColor.println("1. Create Author", ConsoleColor.BLUE);
        ConsoleColor.println("2. Create Book", ConsoleColor.BLUE);
        ConsoleColor.println("3. Create Author-Book Relationship", ConsoleColor.BLUE);
        ConsoleColor.println("4. Create Author with Multiple Books", ConsoleColor.BLUE);
        ConsoleColor.println("5. View All Authors and Books", ConsoleColor.BLUE);
        ConsoleColor.println("6. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> createAuthor();
            case 2 -> createBook();
            case 3 -> createAuthorBookRelationship();
            case 4 -> createAuthorWithMultipleBooks();
            case 5 -> viewAllAuthorBookRelationships();
            case 6 -> ConsoleColor.println("Returning to Main Menu...", ConsoleColor.GREEN);
            default -> ConsoleColor.println("Invalid choice. Please try again.", ConsoleColor.RED);
        }
    }

    public Author createAuthor() {
        try {
            return authorController.createAuthor();
        } catch (Exception e) {
            ConsoleColor.println("Error creating author: " + e.getMessage(), ConsoleColor.RED);
            throw new RuntimeException("Error creating author: " + e.getMessage());
        }
    }

    public Book createBook() {
        try {
            return bookController.createBook();
        } catch (Exception e) {
            ConsoleColor.println("Error creating book: " + e.getMessage(), ConsoleColor.RED);
            throw new RuntimeException("Error creating book: " + e.getMessage());
        }
    }

    public void createAuthorBookRelationshipMenu() {
        try {
            ConsoleColor.println("===== Create Author-Book Relationship =====", ConsoleColor.CYAN);
            ConsoleColor.println("1. Search for a book", ConsoleColor.BLUE);
            ConsoleColor.println("2. List all books", ConsoleColor.BLUE);
            ConsoleColor.println("3. Search for an author", ConsoleColor.BLUE);
            ConsoleColor.println("4. List all authors", ConsoleColor.BLUE);
            ConsoleColor.println("5. Back to Main Menu", ConsoleColor.BLUE);
            ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> searchForBook();
                case 2 -> listAllBooks();
                case 3 -> searchForAuthor();
                case 4 -> listAllAuthors();
                case 5 -> ConsoleColor.println("Returning to Main Menu...", ConsoleColor.GREEN);
                default -> ConsoleColor.println("Invalid choice. Please try again.", ConsoleColor.RED);
            }
        } catch (Exception e) {
            ConsoleColor.println("Error creating author-book relationship: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void searchForBook() {
        ConsoleColor.print("Enter book title or ISBN to search: ", ConsoleColor.PURPLE);
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooksByKeyword(keyword);
        if (books.isEmpty()) {
            ConsoleColor.println("No books found.", ConsoleColor.RED);
        } else {
            ConsoleColor.println("Books found:", ConsoleColor.WHITE);
            for (Book book : books) {
                ConsoleColor.println(book.toString(), ConsoleColor.WHITE);
            }
        }
    }

    private void listAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            ConsoleColor.println("No books available.", ConsoleColor.RED);
        } else {
            ConsoleColor.println("All available books:", ConsoleColor.WHITE);
            for (Book book : books) {
                ConsoleColor.println(book.toString(), ConsoleColor.WHITE);
            }
        }
    }

    private Author searchForAuthor() {
        ConsoleColor.print("Enter author name or last name to search: ", ConsoleColor.PURPLE);
        String keyword = scanner.nextLine();
        List<Author> authors = authorService.searchAuthorsByKeyword(keyword);
        if (authors.isEmpty()) {
            ConsoleColor.println("No authors found.", ConsoleColor.RED);
            return null;
        } else {
            ConsoleColor.println("Authors found:", ConsoleColor.WHITE);
            for (Author author : authors) {
                ConsoleColor.println(author.toString(), ConsoleColor.WHITE);
                return author;
            }
        }
        return null;
    }

    private void listAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            ConsoleColor.println("No authors available.", ConsoleColor.RED);
        } else {
            ConsoleColor.println("All available authors:", ConsoleColor.WHITE);
            for (Author author : authors) {
                ConsoleColor.println(author.toString(), ConsoleColor.WHITE);
            }
        }
    }

    public void createAuthorBookRelationship() {
        ConsoleColor.println("===== Create Author-Book Relationship =====", ConsoleColor.CYAN);
        try {
            ConsoleColor.println("Let's start by creating a Book", ConsoleColor.WHITE);
            Book book = createBook();
            ConsoleColor.println("Now let's create an Author", ConsoleColor.WHITE);
            Author author = createAuthor();
            if (author != null && book != null) {
                AuthorBook authorBook = new AuthorBook(book, author);
                authorBookService.saveAuthorBook(authorBook);
                ConsoleColor.println("Author-Book relationship created successfully!", ConsoleColor.GREEN);
            } else {
                ConsoleColor.println("Invalid Author or Book ID.", ConsoleColor.RED);
            }
        } catch (Exception e) {
            ConsoleColor.println("Error creating Author-Book relationship: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    public void createAuthorWithMultipleBooks() {
        Map<Author, List<Book>> authorBookMap = new HashMap<>();
        ConsoleColor.println("===== Create Author with Multiple Books =====", ConsoleColor.CYAN);

        try {
            ConsoleColor.println("Let's start by creating an Author", ConsoleColor.WHITE);
            Author author = createAuthor();
            ConsoleColor.println("Now let's create multiple Books", ConsoleColor.WHITE);
            List<Book> books = bookController.createMultipleBooks();
            authorBookMap.put(author, books);
            if (author != null && books != null) {
                for (Book book : books) {
                    AuthorBook authorBook = new AuthorBook(book, author);
                    authorBookService.saveAuthorBook(authorBook);
                }
                ConsoleColor.println("Author-Book relationships created successfully!", ConsoleColor.GREEN);
            } else {
                ConsoleColor.println("Invalid Author or Book ID.", ConsoleColor.RED);
            }
            assert author != null;
            ConsoleColor.println("Author: " + author.getFirstName() + " " + author.getLastName(), ConsoleColor.WHITE);
            ConsoleColor.println("Books:", ConsoleColor.WHITE);
            ConsoleColor.println(authorBookMap.getOrDefault(author, books).toString(), ConsoleColor.WHITE);
        } catch (Exception e) {
            ConsoleColor.println("Error creating Author-Book relationship: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    public void viewAllAuthorBookRelationships() {
        try {
            List<AuthorBook> authorBooks = authorBookService.getAllAuthorBooks();
            if (authorBooks.isEmpty()) {
                ConsoleColor.println("No Author-Book relationships found.", ConsoleColor.RED);
            } else {
                ConsoleColor.println("All Author-Book Relationships:", ConsoleColor.CYAN);
                for (AuthorBook authorBook : authorBooks) {
                    ConsoleColor.println(authorBook.toString(), ConsoleColor.WHITE);
                }
            }
        } catch (Exception e) {
            ConsoleColor.println("Error viewing Author-Book relationships: " + e.getMessage(), ConsoleColor.RED);
        }
    }
}