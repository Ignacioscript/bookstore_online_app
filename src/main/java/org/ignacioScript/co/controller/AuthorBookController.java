package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AuthorBookController {

    private static AuthorBookService authorBookService;
    private  static AuthorService authorService;
    private  static BookService bookService;
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
            System.out.println("\n===== Author and Books Main Menu =====");
            System.out.println("1. Manage Authors");
            System.out.println("2. Manage Books");
            System.out.println("3. Manage Author-Book Relationships");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            AuthorController authorController = new AuthorController(scanner, authorService);
            BookController bookController = new BookController(scanner, bookService);
            switch (choice) {
                case 1 -> authorController.displayAuthorMenu();
                case 2 -> bookController.displayBookMenu();
                case 3 -> displayAuthorBookMenu();
                case 4 -> {
                    System.out.println("Returning to Main Menu... \n");
                    MenuController.runMenu();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }




    public void displayAuthorBookMenu() {
        System.out.println("\n===== Author-Book Menu =====");
        System.out.println("1.create Author");
        System.out.println("2.create Book");
        System.out.println("3.create Author-Book Relationship");
        System.out.println("4.create Author with Multiple Books");
        System.out.println("5.view All Authors and Books");
        System.out.println("6.Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> createAuthor();
            case 2 -> createBook();
            case 3 -> createAuthorBookRelationship();
            case 4 -> createAuthorWithMultipleBooks();
            case 5 -> viewAllAuthorBookRelationships();
            case 6 -> System.out.println("Returning to Main Menu...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }



    public Author createAuthor() {


        try {
            return authorController.createAuthor();
        }catch (Exception e) {
            System.out.println("Error creating author: " + e.getMessage());
            throw new RuntimeException("Error creating author: " + e.getMessage());
        }
    }

    public Book createBook() {
        try {
            return bookController.createBook();
        } catch (Exception e) {
            System.out.println("Error creating book: " + e.getMessage());
            throw new RuntimeException("Error creating book: " + e.getMessage());
        }
    }

    public void createAuthorBookRelationshipMenu() {
       try {
            System.out.println("===== Create Author-Book Relationship =====");
            System.out.println("1. Search for a book");
            System.out.println("2. List all books");
            System.out.println("3. Search for an author");
            System.out.println("4. List all authors");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> searchForBook();
                case 2 -> listAllBooks();
                case 3 -> searchForAuthor();
                case 4 -> listAllAuthors();
                case 5 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error creating author-book relationship: " + e.getMessage());
        }
       }

    private void searchForBook() {
        System.out.println("Enter book title or ISBN to search:");
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooksByKeyword(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("Books found:");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private void listAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("All available books:");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private Author searchForAuthor() {
        Author authorFounded;
        System.out.println("Enter author name or last name to search:");
        String keyword = scanner.nextLine();
        List<Author> authors = authorService.searchAuthorsByKeyword(keyword);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            System.out.println("Authors found:");
            for (Author author : authors) {
                System.out.println(author);
                authorFounded = author;
                return authorFounded;
            }
        }
        return null;
    }

    private void listAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors available.");
        } else {
            System.out.println("All available authors:");
            for (Author author : authors) {
                System.out.println(author);
            }
        }

    }

    public void createAuthorBookRelationship() {

        System.out.println("===== Create Author-Book Relationship =====");
        try {
            System.out.println("Let's start by creating a Book");
                Book book = createBook();
            System.out.println("Now let's create an Author");
                Author author = createAuthor();
            if (author != null && book != null) {
                AuthorBook authorBook = new AuthorBook(book, author);
                authorBookService.saveAuthorBook(authorBook);
                System.out.println("Author-Book relationship created successfully!");
            } else {
                System.out.println("Invalid Author or Book ID.");
            }
        } catch (Exception e) {
            System.out.println("Error creating Author-Book relationship: " + e.getMessage());
        }
    }

    public void createAuthorWithMultipleBooks() {
        Map<Author, List<Book>> authorBookMap = new HashMap<>();
        System.out.println("===== Create Author with Multiple Books =====");

        try {
            System.out.println("Let's start by creating an Author");
            Author author = createAuthor();
            System.out.println("Now let's create multiple Books");
            List<Book> books = bookController.createMultipleBooks();
            authorBookMap.put(author, books);
            if (author != null && books != null) {
                for (Book book : books) {
                    AuthorBook authorBook = new AuthorBook(book, author);
                    authorBookService.saveAuthorBook(authorBook);
                }
                System.out.println("Author-Book relationships created successfully!");
            } else {
                System.out.println("Invalid Author or Book ID.");
            }
            System.out.println("Author: " + author.getFirstName() + " " + author.getLastName());
            System.out.println("Books:");
            System.out.println(authorBookMap.getOrDefault(author, books));

        } catch (Exception e) {
            System.out.println("Error creating Author-Book relationship: " + e.getMessage());
        }


    }

    public void getAuthorBooks() {
        System.out.println("===== Get Author's Books =====");
        try {
           Author author =  searchForAuthor();
           Map<Author, List<Book>> authorListMap = new HashMap<>();
            if (author != null) {
                List<Book> books = authorListMap.get(author);
                if (books != null && !books.isEmpty()) {
                    System.out.println("Books by " + author.getFirstName() + " " + author.getLastName() + ":");
                    for (Book book : books) {
                        System.out.println(book);
                    }
                } else {
                    System.out.println("No books found for this author.");
                }
            } else {
                System.out.println("Author not found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving author's books: " + e.getMessage());
        }
    }

    /**
     * This method retrieves and displays all Author-Book relationships.
     */

    public void viewAllAuthorBookRelationships() {
        try {
            List<AuthorBook> authorBooks = authorBookService.getAllAuthorBooks();
            if (authorBooks.isEmpty()) {
                System.out.println("No Author-Book relationships found.");
            } else {
                System.out.println("All Author-Book Relationships:");
                for (AuthorBook authorBook : authorBooks) {
                    System.out.println(authorBook);
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing Author-Book relationships: " + e.getMessage());
        }

    }
}


