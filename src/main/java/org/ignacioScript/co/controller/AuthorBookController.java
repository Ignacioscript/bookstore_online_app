package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.AuthorBookService;
import org.ignacioScript.co.service.AuthorService;
import org.ignacioScript.co.service.BookService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AuthorBookController {

    private static AuthorBookService authorBookService;
    private  static AuthorService authorService;
    private  static BookService bookService;
    private static Scanner scanner;

    public AuthorBookController(Scanner scanner, AuthorBookService authorBookService, AuthorService authorService, BookService bookService) {
        AuthorBookController.authorBookService = authorBookService;
        AuthorBookController.authorService = authorService;
        AuthorBookController.bookService = bookService;
        AuthorBookController.scanner = scanner;
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
        System.out.println("4.view All Author-Book Relationships");
        System.out.println("5.Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> createAuthor();
            case 2 -> createBook();
            case 3 -> createAuthorBookRelationship(new Author(), new Book());
            case 4 -> viewAllAuthorBookRelationships();
            case 5 -> System.out.println("Returning to Main Menu...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }



    public void createAuthor() {

        try {
            System.out.println("Insert author name:");
            String authorName = scanner.nextLine();
            System.out.println("Insert author last name:");
            String authorLastName = scanner.nextLine();
            System.out.println("Insert author bio:");
            String authorBio = scanner.nextLine();
            System.out.println("Insert nationality:");
            String nationality = scanner.nextLine();


            Author author = new Author(authorName, authorLastName, authorBio, nationality);
            authorService.saveAuthor(author);
            System.out.println("Author created successfully!");
            System.out.println("Want to create a book? (yes/no)");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                createBook();
            } else {
                System.out.println("Returning to Main Menu...");
            }

        }catch (Exception e) {
            System.out.println("Error creating author: " + e.getMessage());
        }
    }

    public void createBook() {
        try {
            System.out.println("Insert book ISBN:");
            String isbn = scanner.nextLine();
            System.out.println("Insert book title:");
            String bookTitle = scanner.nextLine();
            System.out.println("Insert book description:");
            String bookDescription = scanner.nextLine();
            System.out.println("Insert book publisher:");
            String bookPublisher = scanner.nextLine();
            System.out.println("Insert publication date (YYYY-MM-DD):");
            LocalDate publicationDate = LocalDate.parse(scanner.nextLine());
            System.out.println("Insert price:");
            Double price = Double.parseDouble(scanner.nextLine());
            System.out.println("Insert stock:");
            Integer stock = Integer.parseInt(scanner.nextLine());
            Book book = new Book(isbn ,bookTitle, bookDescription, bookPublisher, publicationDate, price, stock);
            bookService.saveBook(book);
            System.out.println("Book created successfully!");

            System.out.println("Want to create an author? (yes/no)");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                createAuthor();
            } else {
                System.out.println("Returning to Main Menu...");
            }
        } catch (Exception e) {
            System.out.println("Error creating book: " + e.getMessage());
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

    private void searchForAuthor() {
        System.out.println("Enter author name or last name to search:");
        String keyword = scanner.nextLine();
        List<Author> authors = authorService.searchAuthorsByKeyword(keyword);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            System.out.println("Authors found:");
            for (Author author : authors) {
                System.out.println(author);
            }
        }
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

    public void createAuthorBookRelationship(Author author, Book book) {
        try {
            System.out.println("Insert author ID:");
            int authorId = Integer.parseInt(scanner.nextLine());
            System.out.println("Insert book ID:");
            int bookId = Integer.parseInt(scanner.nextLine());
            Author selectedAuthor = authorService.findAuthorById(authorId);
            Book selectedBook = bookService.findBookById(bookId);
            if (selectedAuthor != null && selectedBook != null) {
                AuthorBook authorBook = new AuthorBook(selectedBook, selectedAuthor);
                authorBookService.saveAuthorBook(authorBook);
                System.out.println("Author-Book relationship created successfully!");
            } else {
                System.out.println("Invalid Author or Book ID.");
            }
        } catch (Exception e) {
            System.out.println("Error creating Author-Book relationship: " + e.getMessage());
        }
    }

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


