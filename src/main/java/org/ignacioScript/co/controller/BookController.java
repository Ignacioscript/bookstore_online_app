package org.ignacioScript.co.controller;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.seeder.BookSeeder;
import org.ignacioScript.co.service.BookService;
import org.ignacioScript.co.util.ConsoleColor;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.BookValidator;

import java.time.LocalDate;
import java.util.*;

public class BookController {

    private BookService bookService;
    private static Scanner scanner;

    public BookController(Scanner scanner, BookService bookService) {
        this.bookService = bookService;
        BookController.scanner = scanner;
    }

    public void displayBookMenu() {
        while (true) {
            displayMenuOption();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 0 -> BookSeeder.seedBooks(bookService);
                case 1 -> createBook();
                case 2 -> viewBooks();
                case 3 -> searchForBook();
                case 4 -> {
                    ConsoleColor.println("Returning to Main Menu...\n", ConsoleColor.GREEN);
                    return;
                }
                default -> ConsoleColor.println("Invalid choice. Please try again.\n", ConsoleColor.RED);
            }
        }
    }

    private void displayMenuOption() {
        ConsoleColor.println("\n===== Manage Books =====", ConsoleColor.CYAN);
        ConsoleColor.println("0. Seed Books", ConsoleColor.BLUE);
        ConsoleColor.println("1. Create Book", ConsoleColor.BLUE);
        ConsoleColor.println("2. View All Books", ConsoleColor.BLUE);
        ConsoleColor.println("3. Find Book by Title", ConsoleColor.BLUE);
        ConsoleColor.println("4. Back to Main Menu", ConsoleColor.BLUE);
        ConsoleColor.print("Enter your choice: ", ConsoleColor.PURPLE);
    }

    public Book createBook() {
        FileLogger.logApp("BookController - Creating a new book");
        try {
            String isbn;
            while (true) {
                ConsoleColor.print("Enter ISBN: ", ConsoleColor.PURPLE);
                try {
                    isbn = scanner.nextLine();
                    BookValidator.validateIsbn(isbn);
                    break;
                } catch (Exception e) {
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String bookTitle;
            while (true) {
                ConsoleColor.print("Enter book title: ", ConsoleColor.PURPLE);
                try {
                    bookTitle = scanner.nextLine();
                    BookValidator.validateTitle(bookTitle);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateTitle");
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String bookDescription;
            while (true) {
                ConsoleColor.print("Enter book description: ", ConsoleColor.PURPLE);
                try {
                    bookDescription = scanner.nextLine();
                    BookValidator.validateDescription(bookDescription, 100, 20);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateDescription");
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            String bookPublisher;
            while (true) {
                ConsoleColor.print("Enter book publisher: ", ConsoleColor.PURPLE);
                try {
                    bookPublisher = scanner.nextLine();
                    BookValidator.validatePublisher(bookPublisher);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublisher");
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            LocalDate publicationDate;
            while (true) {
                ConsoleColor.print("Enter publication date format (yyyy-mm-dd): ", ConsoleColor.PURPLE);
                try {
                    publicationDate = LocalDate.parse(scanner.nextLine());
                    BookValidator.validatePublicationDate(publicationDate);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePublicationDate");
                    ConsoleColor.println("Error: Invalid date format, please try again...", ConsoleColor.RED);
                }
            }

            double price;
            while (true) {
                ConsoleColor.print("Enter price: ", ConsoleColor.PURPLE);
                try {
                    price = Double.parseDouble(scanner.nextLine());
                    BookValidator.validatePrice(price);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validatePrice");
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            int stock;
            while (true) {
                ConsoleColor.print("Enter stock: ", ConsoleColor.PURPLE);
                try {
                    stock = Integer.parseInt(scanner.nextLine());
                    BookValidator.validateStock(stock);
                    break;
                } catch (Exception e) {
                    FileLogger.logError("BookController - input error BookValidator.validateStock");
                    ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
                }
            }

            int bookId = getBookId() + 1;
            Book book = new Book(isbn, bookTitle, bookDescription, bookPublisher, publicationDate, price, stock);
            book.setBookId(bookId);
            bookService.saveBook(book);

            FileLogger.logApp("BookController - book created: " + book);
            ConsoleColor.println("Book created successfully! with ID: " + book.getBookId(), ConsoleColor.GREEN);
            return book;

        } catch (Exception e) {
            FileLogger.logError("BookController - Error creating book: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void viewBooks() {
        FileLogger.logApp("BookController - Viewing all books");
        try {
            List<Book> books = bookService.getAllBooks();
            ConsoleColor.println("\n===== All Books =====", ConsoleColor.CYAN);
            for (Book book : books) {
                ConsoleColor.println(book.toString(), ConsoleColor.WHITE);
            }
        } catch (Exception e) {
            FileLogger.logError("BookController - Error viewing books: " + e.getMessage());
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private void searchForBook() {
        ConsoleColor.println("\n===== Search for Book =====", ConsoleColor.CYAN);
        ConsoleColor.print("Enter book title: ", ConsoleColor.PURPLE);
        String keyword = scanner.nextLine();
        try {
            List<Book> books = bookService.searchBooksByKeyword(keyword.toLowerCase());
            if (books.isEmpty()) {
                ConsoleColor.println("No books found.", ConsoleColor.RED);
            } else {
                ConsoleColor.println("Books found:", ConsoleColor.CYAN);
                for (Book book : books) {
                    ConsoleColor.println(book.toString(), ConsoleColor.WHITE);
                }
            }
        } catch (Exception e) {
            ConsoleColor.println("Error: " + e.getMessage(), ConsoleColor.RED);
        }
    }

    private int getBookId() {
        int id = 0;
        try {
            List<Book> books = bookService.getAllBooks();
            for (Book book : books) {
                id = Math.max(id, book.getBookId());
            }
            return id;
        } catch (Exception e) {
            FileLogger.logError("BookController - Error getting book ID: " + e.getMessage());
            ConsoleColor.println("Error retrieving book ID.", ConsoleColor.RED);
            return -1;
        }
    }

    public List<Book> createMultipleBooks() {
        ConsoleColor.println("===== Create Multiple Books =====", ConsoleColor.CYAN);
        ConsoleColor.println("Let's add the first book", ConsoleColor.BLUE);
        List<Book> books = new ArrayList<>();
        while (true) {
            ConsoleColor.print("Do you want to add a new book? (yes/no): ", ConsoleColor.PURPLE);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("no")) {
                break;
            } else if (answer.equalsIgnoreCase("yes")) {
                Book book = createBook();
                books.add(book);
            } else {
                ConsoleColor.println("Invalid input. Please enter 'yes' or 'no'.", ConsoleColor.RED);
            }
        }
        return books;
    }
}